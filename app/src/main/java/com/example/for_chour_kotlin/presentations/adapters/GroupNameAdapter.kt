package com.example.for_chour_kotlin.presentations.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups
import com.google.gson.Gson
import kotlin.text.isNotEmpty

class GroupNameAdapter(
    private var groups: MutableList<AppAllGroups>?
) : RecyclerView.Adapter<GroupNameAdapter.NameViewHolder>() {

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        val group = groups?.get(position)
        if (group != null) {
            AuthorizationState.groups?.setFocus(group)
            holder.nameTextView.text = group.name ?: "Без имени"  // Обработка null имени, если нужно
            // Обработчик клика на весь элемент
            holder.itemView.setOnClickListener {
                onclickGroup(group)
            }
        } else {
            // Если группа null (редкий случай), скрываем текст и не устанавливаем клик
            holder.nameTextView.text = ""
        }
    }

    override fun getItemCount(): Int = groups?.size ?: 0

    // Метод для обновления данных и обновления списка
    @SuppressLint("NotifyDataSetChanged")
    fun updateGroups(newGroups: MutableList<AppAllGroups>?) {
        groups = newGroups
        notifyDataSetChanged()
    }

    private fun onclickGroup(group : AppAllGroups) {
        val partisipantName = group.listNameBases.get("participant");
        if (partisipantName?.isNotEmpty() ?: false) {

            AuthorizationState.participants?.connection(AuthorizationState.database,partisipantName)

            val participants = AuthorizationState.participants?.participants?.value
            if (participants!=null && participants.isNotEmpty()) {

                val versions: String = participants.joinToString(separator = ",") { "${it.id}:${it.version}" }

                AuthorizationState.typeResponses?.uploadingUpdatesParticipants(group.hashName,versions,{
                    //Вторичный конект
                    AuthorizationState.participants?.connection(AuthorizationState.database,partisipantName)
                },{})
            }
            else {
                AuthorizationState.typeResponses?.uploadingParticipantsData(group.hashName,{
                    //Вторичный конект
                    AuthorizationState.participants?.connection(AuthorizationState.database,partisipantName)
                },{})
            }

        }
    }

    fun chooseFirst() {
        groups?.size?.let {
            if (it >0) {
                onclickGroup(groups!!.get(0))

            }
        }
    }
}
