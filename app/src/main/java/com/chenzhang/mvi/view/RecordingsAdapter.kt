package com.chenzhang.mvi.view

import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.data.RecordingType
import com.chenzhang.mvi.data.RecordingType.MOVIE
import com.chenzhang.mvi.data.RecordingType.MV
import com.chenzhang.mvi.data.RecordingType.SPORT
import com.chenzhang.mvi.data.RecordingType.TV
import com.chenzhang.recording_mvi_rx_kotlin.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.recording_item_collapsed.view.*
import kotlinx.android.synthetic.main.recording_item_expanded.view.*
import org.apache.commons.lang3.time.FastDateFormat
import java.lang.IllegalStateException

/**
 * RecyclerView's adapter, to manage View bindings for itemView and pass-through of user gestures
 */
class RecordingsAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var items: MutableList<Pair<Recording, Boolean>> = mutableListOf()
    private val deleteSubject = PublishSubject.create<Recording>()
    private val playSubject = PublishSubject.create<Recording>()
    private val downloadSubject = PublishSubject.create<Recording>()

    //expose "Delete" user-action via Subject
    val deleteObservable: Observable<Recording>
        get() = deleteSubject

    //expose "Play" user-action via Subject
    val playObservable: Observable<Recording>
        get() = playSubject

    //expose "Download" user-action via Subject
    val downloadObservable: Observable<Recording>
        get() = downloadSubject

    fun setItems(data: List<Recording>) {
        items = data.map { it to false }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                ITEM_VIEW_COLLAPSED ->
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.recording_item_collapsed, parent, false)
                            .let { CollapsedItemViewHolder(it) }
                else ->
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.recording_item_expanded, parent, false)
                            .let { ExpandedItemViewHolder(it) }
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is CollapsedItemViewHolder -> holder.bind(items[position].first)
            is ExpandedItemViewHolder -> holder.bind(items[position].first)
            else -> throw IllegalStateException("wrong viewHolder type is received")
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int = if (items[position].second) ITEM_VIEW_EXPANDED else ITEM_VIEW_COLLAPSED

    companion object {
        private const val ITEM_VIEW_COLLAPSED = 0
        private const val ITEM_VIEW_EXPANDED = 1
        private val recordingTimeFormatterLong = FastDateFormat.getInstance("MMM d h:mm a")
        private val recordingTimeFormatterShort = FastDateFormat.getInstance("M/d")
    }

    inner class ExpandedItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            itemView.setOnClickListener {
                items[layoutPosition] = items[layoutPosition].run { first to false }

                //Utilize "transition animation" feature that comes with RecyclerView with #notifyItemChanged
                notifyItemChanged(layoutPosition)
            }

            itemView.deleteButton.setOnClickListener { deleteSubject.onNext(items[layoutPosition].first) }
            itemView.playButton.setOnClickListener { playSubject.onNext(items[layoutPosition].first) }

            var downloading = false
            itemView.downloadView.setOnClickListener {
                if (!downloading) {
                    /* This is for ONLY DEMO purpose that we simulate the download progress right here. By MVI's unidirectional data flow, View should pass Download intent
                    to business logic, which generate new ViewState then render() into View. Will update this later when I get a chance*/
                    (it as LottieAnimationView).animateDownload()

                    downloadSubject.onNext(items[layoutPosition].first)
                    downloading = true
                }
            }
        }

        fun bind(recording: Recording) {
            with(itemView) {
                expandedRecordingTitle.text = recording.title
                recordingDesc.text = recording.description
                expandedRecordingType.setImageResource(getTypeBadge(recording.recordingType))
                expandedRecordingTime.text = recording.recordingTime?.let { itemView.context.getString(R.string.recording_time, recordingTimeFormatterLong.format(it)) }.orEmpty()
            }
        }

        //private extension function for Lottie
        private fun LottieAnimationView.animateDownload() {
            setAnimation("download_progress.json", LottieAnimationView.CacheStrategy.Strong)
            ValueAnimator.ofFloat(0f, 1.0f).setDuration(3000).apply {
                addUpdateListener { valueAnimator ->
                    progress = valueAnimator.animatedValue as Float

                    if (progress == 1.0f) {
                        setAnimation("check_mark.json", LottieAnimationView.CacheStrategy.Strong)
                        ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000).apply {
                            addUpdateListener { checkAnimator ->
                                progress = checkAnimator.animatedValue as Float
                                if (checkAnimator.animatedValue as Float == 1.0f) {
                                    itemView.downloadView.visibility = View.GONE
                                }
                            }
                            start()
                        }
                    }
                }
                start()
            }
        }
    }

    inner class CollapsedItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            itemView.setOnClickListener {
                items[layoutPosition] = items[layoutPosition].run { first to true }

                //Utilize "transition animation" feature that comes with RecyclerView with #notifyItemChanged
                notifyItemChanged(layoutPosition)
            }
        }

        fun bind(recording: Recording) {
            with(itemView) {
                collapsedRecordingTitle.text = recording.title
                collapsedRecordingType.setImageResource(getTypeBadge(recording.recordingType))
                collapsedRecordingTime.text = recording.recordingTime?.let { itemView.context.getString(R.string.recording_time, recordingTimeFormatterShort.format(it)) }.orEmpty()
            }
        }
    }

    private fun getTypeBadge(recordingType: RecordingType) =
            when (recordingType) {
                MOVIE -> R.drawable.ic_movie_black_24dp
                TV -> R.drawable.ic_tv_black_24dp
                MV -> R.drawable.ic_mv_black_24dp
                SPORT -> R.drawable.ic_sport_black_24dp
            }

}
