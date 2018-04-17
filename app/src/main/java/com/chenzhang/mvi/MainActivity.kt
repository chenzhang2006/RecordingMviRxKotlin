package com.chenzhang.mvi

import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProviders
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.chenzhang.mvi.base.MviView
import com.chenzhang.mvi.recordings.RecordingsIntent
import com.chenzhang.mvi.recordings.RecordingsViewModel
import com.chenzhang.mvi.recordings.RecordingsViewModelFactory
import com.chenzhang.mvi.recordings.RecordingsViewState
import com.chenzhang.mvi.view.RecordingsAdapter
import com.chenzhang.recording_mvi_rx_kotlin.R
import com.chenzhang.recording_mvi_rx_kotlin.R.id
import com.chenzhang.recording_mvi_rx_kotlin.R.layout
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MviView<RecordingsIntent, RecordingsViewState> {

    @Inject
    lateinit var recordingsViewModelFactory: RecordingsViewModelFactory

    private val disposables = CompositeDisposable()
    private val viewModel: RecordingsViewModel by lazy {
        ViewModelProviders
                .of(this, recordingsViewModelFactory)
                .get(RecordingsViewModel::class.java)
    }
    private val recordingsAdapter: RecordingsAdapter by lazy { RecordingsAdapter() }
    private val refreshIntentPublisher = PublishSubject.create<RecordingsIntent.RefreshIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MviApplication.appComponent.inject(this)

        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recordingsAdapter
        }

        bind()
    }

    override fun intents(): Observable<RecordingsIntent> = Observable.merge(initialIntent(), deleteIntent(), addIntent(), refreshIntent())

    private fun initialIntent() = Observable.just(RecordingsIntent.InitialIntent)

    private fun deleteIntent() = recordingsAdapter.deleteObservable.map { recording -> RecordingsIntent.DeleteIntent(recording) }

    private fun addIntent() = RxView.clicks(fab).debounce(200, MILLISECONDS).map {
        RecordingsIntent.AddIntent
    }

    //swipe-to-refresh intent along with option-menu-refresh intent
    private fun refreshIntent() = RxSwipeRefreshLayout.refreshes(swipeRefresh)
            .map { RecordingsIntent.RefreshIntent }
            .mergeWith(refreshIntentPublisher)

    @TargetApi(VERSION_CODES.N)
    override fun render(state: RecordingsViewState) {
        //initial viewState used by Rx scan()/reducer
        if (state == RecordingsViewState.initial()) return

        if(swipeRefresh.isRefreshing){
            swipeRefresh.isRefreshing = false
        }

        if (state.isLoading) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            recorderUsage.visibility = View.INVISIBLE
            recorderUsageBar.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recorderUsage.visibility = View.VISIBLE
            recorderUsageBar.visibility = View.VISIBLE
            recordingsAdapter.setItems(state.recordings)
            recorderUsage.text = getString(R.string.recorder_usage, state.recorderUsage)
            recorderUsageBar.setProgress(state.recorderUsage, true)
        }

    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

        recordingsAdapter.playObservable.subscribe { recording ->
            Snackbar.make(contentMainContainer, getString(R.string.recording_play_message, recording.title), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.menuRefresh -> refreshIntentPublisher.onNext(RecordingsIntent.RefreshIntent)
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}
