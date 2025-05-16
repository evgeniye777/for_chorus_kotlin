package com.example.for_chour_kotlin.domain.cases

import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.model.AppGroupDataParticipant
import com.example.for_chour_kotlin.data.model.AppStPersons

class MakeStatus {
    lateinit var appStPersons: List<AppStPersons>
/*
    fun get(participant: AppGroupDataParticipant, status: Boolean, purpose:Int,date: String? = null):String? {
        if (listST == null) {
            if (mPerson?.state == 0) {
                mPerson?.state = 1
                selectLayout?.setBackgroundResource(R.drawable.rect2)
            } else if (!(mPerson?.allowed in 0..1 && (purpose == 0 ||purpose == 4 ||purpose == 5  ||(purpose == 3 && mPerson?.allowed == 1) || mPerson?.gender == purpose))) {
                selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                mPerson?.state = 0
            } else {
                mPerson?.state = 0
                selectLayout?.setBackgroundResource(R.drawable.rect1)
            }
        } else {
            val purpose: Int = spinnerPurpose.selectedItemPosition
            val listNow: MutableList<String> = listInfoRec[cursorRec].getRecList()
            var id: Int = (mPerson?.id ?: 1) - 1

            try {
                if (listNow[id].isNotEmpty() && listNow[id] != "p") {
                    listNow[id] = "p"
                    selectLayout?.setBackgroundResource(R.drawable.rect2)
                } else if (!(mPerson?.allowed in 0..1 && (purpose == 0 || (purpose == 3 && mPerson?.allowed == 1) || mPerson?.gender == purpose))) {
                    selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                    listNow[id] = "d"
                } else {
                    listNow[id] = "n"
                    selectLayout?.setBackgroundResource(R.drawable.rect_not)
                }
            } catch (_: Exception) {}
        }
    }*/
}