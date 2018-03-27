package com.chenzhang.mvi

import android.app.Application
import com.chenzhang.mvi.application.Log4JConfigurator
import com.chenzhang.mvi.injection.AppComponent
import com.chenzhang.mvi.injection.AppModule
import com.chenzhang.mvi.injection.DaggerAppComponent

class MviApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        Log4JConfigurator.configureLogging()
    }
}
