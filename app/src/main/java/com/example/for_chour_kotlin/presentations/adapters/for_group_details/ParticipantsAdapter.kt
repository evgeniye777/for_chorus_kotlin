package com.example.for_chour_kotlin.presentations.adapters.for_group_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant

class ParticipantsAdapter : RecyclerView.Adapter<ParticipantsAdapter.ViewHolder>() {
    private var participantsList: List<AppDataParticipant> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<AppDataParticipant>?) {
        participantsList = list ?: emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPName: TextView = itemView.findViewById(R.id.tvPName)
        val tvPGender: TextView = itemView.findViewById(R.id.tvPGender)
        val tvPost: TextView = itemView.findViewById(R.id.tvPost)
        val tvAllowed: TextView = itemView.findViewById(R.id.tvAllowed)
        val tvAccess: TextView = itemView.findViewById(R.id.tvAccess)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = participantsList[position]
        holder.tvPName.text = participant.pName ?: "Неизвестный участник"

        // pGender: Конвертация (1=Женский, 2=Мужской; измените логику если нужно)
        val genderText = when (participant.pGender) {
            1 -> "Женский"
            2 -> "Мужской"
            else -> participant.pGender.toString()
        }
        holder.tvPGender.text = "Пол: $genderText"

        // post: Join списка
        val postText = participant.post.joinToString(", ")
        holder.tvPost.text = "Пост: $postText"

        holder.tvAllowed.text = "Разрешено: ${participant.allowed}"
        holder.tvAccess.text = "Доступ: ${participant.access}"
    }

    override fun getItemCount(): Int = participantsList.size
}
