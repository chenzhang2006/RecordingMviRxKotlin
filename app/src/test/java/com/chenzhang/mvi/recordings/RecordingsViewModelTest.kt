package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import mockit.Injectable
import mockit.NonStrictExpectations
import mockit.integration.junit4.JMockit
import org.apache.log4j.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * I used jMockit for mocking framework. Mockito is fine, too, but with following gotcha:
 * 1. Need special setup for mocking "closed" classes, which is the case by default in Kotlin.
 *    ref: http://hadihariri.com/2016/10/04/Mocking-Kotlin-With-Mockito/
 * 2. Reserved keywords conflict with Kotlin, e.g. "when", so they need to be escaped.
 */
@RunWith(JMockit::class)
class RecordingsViewModelTest {

    @Injectable
    private lateinit var apiRepository: ApiRepository

    private lateinit var viewModel: RecordingsViewModel
    private lateinit var testObserver: TestObserver<RecordingsViewState>
    private lateinit var recordings: List<Recording>
    private lateinit var LOG: Logger

    @Before
    fun setup() {
        viewModel = RecordingsViewModel(RecordingsIntentProcessors(apiRepository))
        testObserver = viewModel.states().test()
        recordings = listOf(
                Recording("100", "title100", "disc100", false),
                Recording("200", "title200", "disc200", false),
                Recording("300", "title300", "disc300", false)
        )
        setupTestScheduler()
        Log4JConfiguratorForTest.configure()
        LOG = Logger.getLogger(this.javaClass)
    }

    @Test
    fun testInitIntentLoadViewState() {
        object : NonStrictExpectations() {
            init {
                apiRepository.loadRecordings()
                result = Single.just(recordings)
            }
        }
        viewModel.processIntents(Observable.just(RecordingsIntent.InitialIntent))
        testObserver.assertValueAt(1, { s: RecordingsViewState -> s.isLoading })
        testObserver.assertValueAt(2, { s: RecordingsViewState -> !s.isLoading && s.recordings.size == 3 })
        LOG.info("test finished")
    }

    /**
     * Default scheduler returned by AndroidSchedulers.mainThread() is an instance of LooperScheduler
     * and relies on Android dependencies that are not available in JUnit tests.
     * We can avoid this issue by initializing RxAndroidPlugins with a different Scheduler
     */
    private fun setupTestScheduler() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { _ -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }
}
