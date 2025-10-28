package com.example.the_planner_semen.my_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.R

class My_Menu_Adapter(
    private val items: List<String>,
    private val listener: InterfaceMenu.OnItemClickListener,
    private val hideFragmentListener: HideFragmentListener
): RecyclerView.Adapter<My_Menu_Adapter.ViewHolder>()  {

    interface HideFragmentListener {
        fun onHideFragment()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.id_text_my_menu)

        init {
            itemView.setOnClickListener {
                // Вызываем метод интерфейса при нажатии
                listener.onItemClick(adapterPosition)
                hideFragmentListener.onHideFragment()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}