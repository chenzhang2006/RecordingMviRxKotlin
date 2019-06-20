package com.chenzhang.mvi.mvibase

import io.reactivex.Observable


/**
 * UI intent
 */
interface MviIntent

/**
 * Contains all required data to render a view
 */
interface MviViewState

/**
 * Subscribe to MviView's MviIntent
 * Process and emit MviViewState, which will be subscribed to UI's MviView.render()
 */
interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun processIntents(intents: io.reactivex.Observable<I>)
    fun states(): Observable<S>
}


/**
 * Emit its intents to a view model
 * Subscribe to a view model for rendering its UI.
 */
interface MviView<I : MviIntent, in S : MviViewState> {
    fun intents(): Observable<I>
    fun render(state: S)
}

/**
 * Business layer action; Mapped from MviIntent(UI layer)
 */
interface MviAction

/**
 * Result from processed business Logic
 */
interface MviResult