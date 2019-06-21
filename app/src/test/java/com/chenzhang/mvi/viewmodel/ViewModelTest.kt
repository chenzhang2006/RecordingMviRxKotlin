package com.chenzhang.mvi.viewmodel

import com.chenzhang.mvi.common.Log4JConfiguratorForTest
import com.chenzhang.mvi.common.setupTestScheduler
import com.chenzhang.mvi.mvibase.RecordingsIntent
import com.chenzhang.mvi.mvibase.RecordingsViewState
import com.chenzhang.mvi.datamodel.Recording
import com.chenzhang.mvi.datamodel.RecordingType
import com.chenzhang.mvi.usecase.DeletingInteractor
import com.chenzhang.mvi.usecase.LoadingInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import mockit.Injectable
import mockit.NonStrictExpectations
import mockit.integration.junit4.JMockit
import org.apache.log4j.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * jMockit is used for mocking here. Mockito is fine, too, but need some tweaks to work with Kotlin
 */
@RunWith(JMockit::class)
class ViewModelTest {

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
        //Mocking. Similar to Mockito's "when().thenReturn()"
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

}
