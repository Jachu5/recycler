package com.unchained.itemtouchexample

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.domain.ViewModel
import kotlinx.android.synthetic.main.item_view.view.*

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var titleView: TextView = itemView.item_view_title

    fun bind(beer: ViewModel) =
            with(itemView) {
                titleView.text = beer.title
            }
}
