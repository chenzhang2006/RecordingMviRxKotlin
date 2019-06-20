package com.chenzhang.mvi

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
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
import com.chenzhang.recording_mvi_rx_kotlin.R.id.menuRefresh
import com.chenzhang.recording_mvi_rx_kotlin.R.layout
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject


/**
 * This is the main ViewController of this demo application.
 *
 * This Demo Android app is to showcase Model-View-Intent(MVI) architecture with RxJava implementing the reactive flow, written in Kotlin and supported by Dagger2
 *
 * Detailed documentation at https://github.com/chenzhang2006/RecordingMviRxKotlin or README.md
 */

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

    //There are 2 types of refresh gestures: swipe-down and options-menu. Use PublishSubject to merge the two
    private val refreshIntentPublisher = PublishSubject.create<RecordingsIntent.RefreshIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MviApplication.appComponent.inject(this)

        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        //menu icon, navView and its standard setup
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            Snackbar.make(contentMainContainer, getString(R.string.nav_menu_clicked_message, menuItem.title), Snackbar.LENGTH_LONG).show()
            drawerLayout.closeDrawers()
            true
        }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recordingsAdapter
        }

        bind()
    }

    override fun intents(): Observable<RecordingsIntent> = Observable.merge(initialIntent(), deleteIntent(), refreshIntent())

    private fun initialIntent() = Observable.just(RecordingsIntent.InitialIntent)

    private fun deleteIntent() = recordingsAdapter.deleteObservable.map { recording -> RecordingsIntent.DeleteIntent(recording) }

    //swipe-to-refresh intent along with option-menu-refresh intent
    private fun refreshIntent() = RxSwipeRefreshLayout.refreshes(swipeRefresh)
            .map { RecordingsIntent.RefreshIntent }
            .mergeWith(refreshIntentPublisher)

    @TargetApi(VERSION_CODES.N)
    override fun render(state: RecordingsViewState) {
        //Rx#scan initial state, so states can be accumulated in ViewModel; Ignored by View layer
        if (state == RecordingsViewState.scanInitialState()) return

        if(swipeRefresh.isRefreshing){
            swipeRefresh.isRefreshing = false
        }

        if (state.isLoading) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            recorderUsageContainer.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recorderUsageContainer.visibility = View.VISIBLE
            if (state.recordings.isEmpty()) {
                recyclerView.visibility = View.INVISIBLE
                Snackbar.make(contentMainContainer, getString(R.string.empty_recording_message), Snackbar.LENGTH_LONG).show()
            } else {
                recyclerView.visibility = View.VISIBLE
                recordingsAdapter.setItems(state.recordings)
            }
            recorderUsage.text = getString(R.string.recorder_usage, state.recorderUsage)
            if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
                recorderUsageBar.setProgress(state.recorderUsage, true)
            } else {
                recorderUsageBar.progress = state.recorderUsage
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun bind() {
        //Have to bind output from viewModel -> View first, then bind Intents from View -> ViewModel
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

        disposables.add(recordingsAdapter.playObservable.subscribe { recording ->
            Snackbar.make(contentMainContainer, getString(R.string.recording_play_message, recording.title), Snackbar.LENGTH_LONG).show()
        })

        disposables.add(recordingsAdapter.downloadObservable.subscribe { recording ->
            Snackbar.make(contentMainContainer, "downloading ${recording.title}", Snackbar.LENGTH_LONG).show()
        })

        refreshIntentPublisher.doOnSubscribe { disposables.add(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            menuRefresh -> refreshIntentPublisher.onNext(RecordingsIntent.RefreshIntent)
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        //release CompositeDisposable
        disposables.dispose()
    }

}
