package com.example.for_chour_kotlin.presentations.fragments.attendance

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant
import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons
import com.example.for_chour_kotlin.domain.cases.MakeStatus
import com.example.for_chour_kotlin.presentations._interfaces.OnDataListener

class StPersonsAdapter(
    private val participants: List<AppDataParticipant>,
    private val purpose: Int,
    private var appStPersons: AppStPersons? = null,
    private val listener: OnDataListener
) : RecyclerView.Adapter<StPersonsAdapter.StPersonHolder>() {

    private val statusMap = mutableMapOf<String, Boolean>()

    inner class StPersonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectLayout: LinearLayout = itemView.findViewById(R.id.styleLayout)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvText)

        @SuppressLint("SetTextI18n")
        fun bind(participant: AppDataParticipant) {
            nameTextView.text = (participant.pName)

            val makeStatus = MakeStatus(appStPersons)
            statusMap.put(participant.idC,makeStatus.getStatusFromData(participant))

            selectLayout.setBackgroundResource(makeStatus.getResourceFromAdapter(participant, purpose, statusMap[participant.idC]?:false))
            itemView.setOnClickListener {
                if (statusMap.containsKey(participant.idC)) {
                    statusMap[participant.idC] = !statusMap[participant.idC]!!
                    selectLayout.setBackgroundResource(makeStatus.getResourceFromAdapter(participant, purpose, statusMap[participant.idC]?:false))
                    countParticipantsWithStatusOne()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StPersonHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_person, parent, false)
        return StPersonHolder(view)
    }

    override fun onBindViewHolder(holder: StPersonHolder, position: Int) {
        holder.bind(participants[position])
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    fun getAppStPersons(): AppStPersons? {
        val makeStatus = MakeStatus(appStPersons)
        val newData = participants.associate { participant ->
            participant.idC to makeStatus.getDataFromAdapter(participant, purpose, statusMap[participant.idC] ?: false)
        }
        return if (appStPersons == null) {
            null
        } else {
            appStPersons!!.c.clear()
            appStPersons!!.c.putAll(newData)
            appStPersons
        }
    }
    fun createAppStPersons(committer: String?, date: String): AppStPersons? {
        val makeStatus = MakeStatus(appStPersons)
        val newData = participants.associate { participant ->
            participant.idC to makeStatus.getDataFromAdapter(participant, purpose, statusMap[participant.idC] ?: false)
        }
        appStPersons = AppStPersons(
            committer = committer,
            date = date, dateWrite = "", purpose = purpose, c = HashMap(newData))
        this.appStPersons = appStPersons
        return appStPersons
    }

    //Подсчёт данных
    private fun countParticipantsWithStatusOne() {
        var totalCount = 0
        var maleCount = 0
        var femaleCount = 0

        participants.forEach { participant ->
            if (statusMap[participant.idC] == true) {
                totalCount++
                when (participant.pGender) {
                    1 -> maleCount++
                    2 -> femaleCount++
                }
            }
        }

        listener.onDataUpdate(Triple(totalCount, maleCount, femaleCount))
    }

    fun initAppStPersons(): Boolean {
        return appStPersons!=null;
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update() {
        notifyDataSetChanged()
    }
}
