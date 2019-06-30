package jp.dip.hirotann.appointmentdesk

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.R.attr.onClick

class ListAdapter (private val myDataset: ArrayList<String>, val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false) as TextView
        return ViewHolder(view = view, itemClick = itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUp(myDataset[position])
    }

    override fun getItemCount(): Int {
        return this.myDataset.count()
    }

    class ViewHolder(view: View, val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        private val textView : TextView = view.findViewById(R.id.item_text_view)

        fun setUp(itemName: String) {
            this.textView.text = itemName
            this.itemView.setOnClickListener { itemClick(itemName) }

        }
    }
}
