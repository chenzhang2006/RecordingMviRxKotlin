package com.chenzhang.mvi.recordings

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Default scheduler returned by AndroidSchedulers.mainThread() is an instance of LooperScheduler
 * and relies on Android dependencies that are not available in JUnit tests.
 * We can avoid this issue by initializing RxAndroidPlugins with a different Scheduler
 */
fun setupTestScheduler() {
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