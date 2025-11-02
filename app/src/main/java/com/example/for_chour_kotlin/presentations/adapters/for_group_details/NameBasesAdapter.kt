package com.example.for_chour_kotlin.presentations.adapters.for_group_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NameBasesAdapter : RecyclerView.Adapter<NameBasesAdapter.ViewHolder>() {
    private var nameBases: List<Pair<String, String>> = emptyList()

    fun submitList(map: HashMap<String, String>) {
        nameBases = map.entries.map { it.key to it.value }.sortedBy { it.first }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKeyValue: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (key, value) = nameBases[position]
        holder.tvKeyValue.text = "$key: $value"
    }

    override fun getItemCount(): Int = nameBases.size
}
