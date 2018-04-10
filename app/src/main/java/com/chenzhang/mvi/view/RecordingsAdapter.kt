package com.chenzhang.mvi.view

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenzhang.mvi.data.Recording
import com.chenzhang.recording_mvi_rx_kotlin.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.recording_item_collapsed.view.*
import kotlinx.android.synthetic.main.recording_item_expanded.view.*

class RecordingsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<Pair<Recording, Boolean>> = mutableListOf()
    private val deleteSubject = PublishSubject.create<Recording>()
    private val playSubject = PublishSubject.create<Recording>()

    val deleteObservable: Observable<Recording>
        get() = deleteSubject

    val playObservable: Observable<Recording>
        get() = playSubject

    fun setItems(data: List<Recording>) {
        items = data.map { it to false }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == ITEM_VIEW_COLLAPSED) {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.recording_item_collapsed, parent, false)
                        .let { CollapsedItemViewHolder(it) }
            } else {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.recording_item_expanded, parent, false)
                        .let { ExpandedItemViewHolder(it) }
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CollapsedItemViewHolder) {
            holder.bind(items[position].first)
        } else if (holder is ExpandedItemViewHolder) {
            holder.bind(items[position].first)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int = if (items[position].second) ITEM_VIEW_EXPANDED else ITEM_VIEW_COLLAPSED

    companion object {
        private const val ITEM_VIEW_COLLAPSED = 0
        private const val ITEM_VIEW_EXPANDED = 1
    }

    inner class ExpandedItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            itemView.setOnClickListener {
                items[layoutPosition] = items[layoutPosition].run { first to false }
                notifyItemChanged(layoutPosition)
            }

            itemView.expandedDelete.setOnClickListener { deleteSubject.onNext(items[layoutPosition].first) }
            itemView.expandedPlay.setOnClickListener { playSubject.onNext(items[layoutPosition].first) }
        }

        fun bind(recording: Recording) {
            itemView.expandedRecordingTitle.text = recording.title
            itemView.expandedRecordingDesc.text = recording.description
        }
    }

    inner class CollapsedItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            itemView.setOnClickListener {
                items[layoutPosition] = items[layoutPosition].run { first to true }
                notifyItemChanged(layoutPosition)
            }

            itemView.collapsedDelete.setOnClickListener { deleteSubject.onNext(items[layoutPosition].first) }
        }

        fun bind(recording: Recording) {
            itemView.collapsedRecordingTitle.text = recording.title
        }
    }

}
