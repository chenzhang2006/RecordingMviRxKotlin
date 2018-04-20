package com.chenzhang.mvi.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenzhang.mvi.MviApplication
import com.chenzhang.mvi.base.MviView
import com.chenzhang.mvi.recordings.Page2ViewModel
import com.chenzhang.mvi.recordings.RecordingsIntent
import com.chenzhang.mvi.recordings.RecordingsViewModelFactory
import com.chenzhang.mvi.recordings.RecordingsViewState
import com.chenzhang.recording_mvi_rx_kotlin.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_page2.*
import org.apache.log4j.Logger
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class Page2Fragment : Fragment(), MviView<RecordingsIntent, RecordingsViewState> {

    @Inject
    lateinit var recordingsViewModelFactory: RecordingsViewModelFactory
    private val disposables = CompositeDisposable()
    private val viewModel: Page2ViewModel by lazy {
        ViewModelProviders
                .of(this, recordingsViewModelFactory)
                .get(Page2ViewModel::class.java)
    }
    private val LOG = Logger.getLogger(this::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MviApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }


    override fun intents(): Observable<RecordingsIntent> = Observable.merge(initialIntent(), addIntent())

    private fun initialIntent() = Observable.just(RecordingsIntent.InitialIntent)

    private fun addIntent() = RxView.clicks(page2Text).debounce(200, MILLISECONDS).map {
        RecordingsIntent.AddIntent
    }

    override fun render(state: RecordingsViewState) {
        LOG.debug("render $state from intent")
        if (state == RecordingsViewState.initial()) return

        if (state.isLoading) {
            page2Text.text = "loading"
        } else {
            page2Text.text = state.recordings[1].title
        }
    }

    private fun bind(){
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}