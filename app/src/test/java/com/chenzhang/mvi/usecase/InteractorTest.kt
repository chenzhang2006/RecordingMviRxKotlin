package com.chenzhang.mvi.usecase

import com.chenzhang.mvi.datamodel.Recording
import com.chenzhang.mvi.datarepository.DataRepository
import com.chenzhang.mvi.common.setupTestScheduler
import io.reactivex.Single
import mockit.Injectable
import mockit.NonStrictExpectations
import mockit.integration.junit4.JMockit
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * jMockit is used for mocking here. Mockito is fine, too, but need some tweaks to work with Kotlin
 */
@RunWith(JMockit::class)
class InteractorTest {

    @Injectable
    private lateinit var dataRepository: DataRepository

    private lateinit var loadingInteractor: LoadingInteractor

    @Before
    fun setup(){
        loadingInteractor = LoadingInteractor(dataRepository)

        setupTestScheduler()
    }

    @Test
    fun testEmptyRecording(){
        //Mocking. Similar to Mockito's "when().thenReturn()"
        object: NonStrictExpectations() {
            init {
                dataRepository.loadCurrentRecordings()
                result = Single.just(emptyList<Recording>())
            }
        }

        val testObserver = loadingInteractor.loadRecordings().test()
        testObserver.assertValue(listOf())
    }

    @Test
    fun testUnreasonableUsage(){
        //test if usage outside of range(0..100), return has to be 0% - 100%
        object: NonStrictExpectations() {
            init {
                dataRepository.getRecorderUsage()
                result = Single.just(120)
            }
        }

        val testObserver = loadingInteractor.loadRecorderUsage().test()
        testObserver.assertValue(100)
    }

}