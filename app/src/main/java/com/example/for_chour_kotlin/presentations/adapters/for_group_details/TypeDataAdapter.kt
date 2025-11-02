package com.example.for_chour_kotlin.presentations.adapters.for_group_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.data.typeData.appAllGroups.TypeData

class TypeDataAdapter : RecyclerView.Adapter<TypeDataAdapter.ViewHolder>() {
    private var dataList: List<TypeData> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<TypeData>?) {
        dataList = list ?: emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvData: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.tvData.text = item.type+"\n"+item.data+"\n\n"
    }

    override fun getItemCount(): Int = dataList.size
}
