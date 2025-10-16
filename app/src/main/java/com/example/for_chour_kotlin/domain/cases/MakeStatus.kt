package com.example.for_chour_kotlin.domain.cases

import android.annotation.SuppressLint
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant
import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons

class MakeStatus(
    private val appStPersons: AppStPersons?
) {
    @SuppressLint("SuspiciousIndentation")
    fun getResourceFromAdapter(participant: AppDataParticipant, purpose: Int,status: Boolean): Int {
        return if (status) {
            R.drawable.rect2
        } else {
            if (!(participant.allowed in 0..1 && (purpose == 0 || purpose == 4 || purpose == 5 || (purpose == 3 && participant.allowed == 1) || participant.pGender == purpose))) {
                R.drawable.rect_gone
            } else {
                if (appStPersons==null) {
                    R.drawable.rect1
                }
                else {R.drawable.rect_not}
            }
        }
    }

    fun getDataFromAdapter(participant: AppDataParticipant, purpose: Int,status: Boolean): String {
        return if (status) {
            "p"
        } else {
            if (!(participant.allowed in 0..1 && (purpose == 0 || purpose == 4 || purpose == 5 || (purpose == 3 && participant.allowed == 1) || participant.pGender == purpose))) {
                "d"
            } else {
                "n"
            }
        }
    }
    fun getStatusFromData(participant: AppDataParticipant):Boolean {
        return appStPersons!=null&& appStPersons.c[participant.idC].equals("p")
    }
}