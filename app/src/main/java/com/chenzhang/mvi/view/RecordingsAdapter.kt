package com.chenzhang.mvi.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.view.RecordingsAdapter.ItemViewHolder
import com.chenzhang.recording_mvi_rx_kotlin.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.recording_item.view.*

class RecordingsAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    private var items: MutableList<Pair<Recording, Boolean>> = mutableListOf()
    private val deleteSubject = PublishSubject.create<Recording>()

    val deleteObservable: Observable<Recording>
        get() = deleteSubject

    fun setItems(data: List<Recording>) {
        items = data.map { it to false }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.recording_item, parent, false)
                    .let { ItemViewHolder(it) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                items[layoutPosition] = items[layoutPosition].run { first to !second }
                notifyItemChanged(layoutPosition)
            }

            itemView.delete.setOnClickListener { deleteSubject.onNext(items[layoutPosition].first) }
        }

        fun bind(itemData: Pair<Recording, Boolean>) {
            itemView.recordingTitle.text = itemData.first.title
            itemView.recordingDesc.text = itemData.first.description
            itemView.recordingDesc.visibility = if (itemData.second) VISIBLE else GONE
        }
    }

}
