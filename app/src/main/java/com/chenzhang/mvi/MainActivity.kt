package com.chenzhang.mvi

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.chenzhang.mvi.base.MviView
import com.chenzhang.mvi.recordings.RecordingsIntent
import com.chenzhang.mvi.recordings.RecordingsViewModel
import com.chenzhang.mvi.recordings.RecordingsViewState
import com.chenzhang.recording_mvi_rx_kotlin.R
import com.chenzhang.recording_mvi_rx_kotlin.R.id
import com.chenzhang.recording_mvi_rx_kotlin.R.layout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MviView<RecordingsIntent, RecordingsViewState> {

    private val disposables = CompositeDisposable()
    private val viewModel: RecordingsViewModel by lazy {
        ViewModelProviders.of(this).get(RecordingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        helloText.text = "MVI"

        bind()
    }

    override fun intents(): Observable<RecordingsIntent> =
            Observable.merge(
                    initialIntent(),
                    refreshIntent())

    private fun initialIntent() = Observable.just(RecordingsIntent.InitialIntent)
    private fun refreshIntent() = RxView.clicks(refreshButton)
                    .doOnNext { Log.d("MainActivity", "track $it") }
            .map { RecordingsIntent.RefreshIntent }

    override fun render(state: RecordingsViewState) {
        //initial viewState used by Rx scan()/reducer
        if (state == RecordingsViewState.initial()) return

        if (state.isLoading) {
            progressBar.visibility = View.VISIBLE
            helloText.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            helloText.visibility = View.VISIBLE
            helloText.text = if (state.recordings.isEmpty()) state.error.toString() else state.recordings.toString()
        }

    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}
