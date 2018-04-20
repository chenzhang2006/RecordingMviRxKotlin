package com.chenzhang.mvi.injection

import com.chenzhang.mvi.MainActivity
import com.chenzhang.mvi.view.MainFragment
import com.chenzhang.mvi.view.Page1Fragment
import com.chenzhang.mvi.view.Page2Fragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: MainFragment)
    fun inject(target: Page1Fragment)
    fun inject(target: Page2Fragment)
}
