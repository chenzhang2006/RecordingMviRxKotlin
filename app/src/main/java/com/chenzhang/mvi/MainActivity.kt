package com.chenzhang.mvi

import android.arch.lifecycle.ViewModelProviders
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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MviApplication.appComponent.inject(this)

        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recordingsAdapter
        }

        bind()
    }

    override fun intents(): Observable<RecordingsIntent> = initialIntent().cast(RecordingsIntent::class.java)

    private fun initialIntent() = Observable.just(RecordingsIntent.InitialIntent)

    override fun render(state: RecordingsViewState) {
        //initial viewState used by Rx scan()/reducer
        if (state == RecordingsViewState.initial()) return

        if (state.isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            recordingsAdapter.setItems(state.recordings)
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
