package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.mvibase.RecordingsIntent
import com.chenzhang.mvi.mvibase.RecordingsViewState
import com.chenzhang.mvi.datamodel.Recording
import com.chenzhang.mvi.datamodel.RecordingType
import com.chenzhang.mvi.usecase.DeletingInteractor
import com.chenzhang.mvi.usecase.LoadingInteractor
import com.chenzhang.mvi.viewmodel.RecordingsIntentProcessors
import com.chenzhang.mvi.viewmodel.RecordingsViewModel
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
import java.util.*
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
    private lateinit var loadingInteractor: LoadingInteractor
    @Injectable
    private lateinit var deletingInteractor: DeletingInteractor

    private lateinit var viewModel: RecordingsViewModel
    private lateinit var testObserver: TestObserver<RecordingsViewState>
    private lateinit var recordings: List<Recording>
    private lateinit var logger: Logger

    @Before
    fun setup() {
        viewModel = RecordingsViewModel(RecordingsIntentProcessors(loadingInteractor, deletingInteractor))
        testObserver = viewModel.states().test()
        recordings = listOf(
                Recording("100", "title100", RecordingType.MOVIE, "disc100", Date(1520650800000)),
                Recording("200", "title200", RecordingType.TV, "disc200", Date(1520650800000)),
                Recording("300", "title300", RecordingType.SPORT, "disc300", Date(1520650800000))
        )
        setupTestScheduler()
        Log4JConfiguratorForTest.configure()
        logger = Logger.getLogger(this.javaClass)
    }

    @Test
    fun testInitIntentLoadViewState() {
        object : NonStrictExpectations() {
            init {
                loadingInteractor.loadRecordings()
                result = Single.just(recordings)
            }
        }
        viewModel.processIntents(Observable.just(RecordingsIntent.InitialIntent))
        testObserver.assertValueAt(1) { s: RecordingsViewState -> s.isLoading }
        logger.info("test finished")
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

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }
}
