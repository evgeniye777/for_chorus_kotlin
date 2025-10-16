package com.example.for_chour_kotlin.presentations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups
import com.example.for_chour_kotlin.presentations._interfaces.OnMainMenuListener

class AppAllGroupsAdapter(
    private var groups: List<AppAllGroups>,
    private val listener: OnMainMenuListener
) : RecyclerView.Adapter<AppAllGroupsAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_group)

        fun bind(group: AppAllGroups) {
            nameTextView.text = group.name ?: "Неизвестно"

            itemView.setOnClickListener {
                // Передаем объект в интерфейс обратного вызова
                listener.onMainMenuUpdate(group)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_menu, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    fun setGroups(newGroups: List<AppAllGroups>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}
