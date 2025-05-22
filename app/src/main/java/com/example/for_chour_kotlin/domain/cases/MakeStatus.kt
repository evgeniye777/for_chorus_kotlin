package com.example.for_chour_kotlin.domain.cases

import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.model.AppGroupDataParticipant
import com.example.for_chour_kotlin.data.model.AppStPersons
import com.example.for_chour_kotlin.data.model.sinchMark

class MakeStatus (
    private var appStPersons: AppStPersons
){
    //-1: не синхронизированный, 0: отсутствует, 1 - присутствует, 2 - не обязан
    fun get(participant: AppGroupDataParticipant, purpose:Int):Int? {
        //вытягиваем поля, нужные для использования
        val id: Int = participant.id
        val s: String = appStPersons.c[id]

        if (s!="p") {
            appStPersons.c[id] = "p"
            return R.drawable.rect2;
        }  else {
            if (!(participant.allowed in 0..1 && (purpose == 0 ||purpose == 4 ||purpose == 5  ||(purpose == 3 && participant.allowed == 1) || participant.pGender == purpose))) {
                appStPersons.c[id] = "d"
                   return R.drawable.rect_gone
            }
            else {
                if ((appStPersons as? sinchMark)?.sinch == false) {
                    appStPersons.c[id] = "n"
                    return R.drawable.rect1
                }
                else {
                    appStPersons.c[id] = "n"
                    return R.drawable.rect_not
                }
            }
        }
    }
}