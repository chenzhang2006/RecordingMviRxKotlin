package com.chenzhang.mvi.injection

import com.chenzhang.mvi.MainActivity
import com.chenzhang.mvi.view.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: MainFragment)
}
