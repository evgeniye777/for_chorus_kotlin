package com.example.for_chour_kotlin.data.source.url_responses

import com.example.for_chour_kotlin.data.source.url_responses.verif_options.ChangePersonForGroup
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.CheckAuthorization
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.InAccount
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.SendStPersonData
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingGroupData
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingParticipantsData
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingStPersonsSinch
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingUpdatesGroups
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingUpdatesParticipants
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingUpdatesStPersonsSinch
import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons

class TypeResponses {
    val localDataAY: LocalDataAY = LocalDataAY()
    init {
        localDataAY.createTableIfNotExists()
        AuthorizationState.localDataAY = localDataAY
    }

    /**
     * Проверка авторизации с колбэками.
     * @param onSuccess вызывается при успешной авторизации
     * @param onFailure вызывается при неуспешной авторизации
     */
    fun checkAuthorization(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==0*/true) {
            val checkAuthorization = CheckAuthorization()
            checkAuthorization.start(onSuccess, onFailure)
        }
    }

    fun inAccount(loginIn: String, hash_password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==0*/true) {
            AccountHolder.hashPassword = hash_password
            val inAccount = InAccount()
            inAccount.start(loginIn, hash_password, onSuccess, onFailure)
        }
    }

    fun uploadingGroupData(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==0*/true) {
            val uploadingGroupData = UploadingGroupData()
            uploadingGroupData.start(onSuccess, onFailure)
        }
    }

    fun uploadingUpdatesGroups(versions:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==0*/true) {
            val uploadingUpdatesGroups = UploadingUpdatesGroups()
            uploadingUpdatesGroups.start(versions, onSuccess, onFailure)
        }
    }

    fun uploadingParticipantsData(hash_name_group:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==1*/true) {
            val uploadingParticipantsData = UploadingParticipantsData()
            uploadingParticipantsData.start(hash_name_group, onSuccess, onFailure)
        }
    }

    fun uploadingUpdatesParticipants(hash_name_group:String,list_versions:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==1*/true) {
            val uploadingUpdatesParticipants = UploadingUpdatesParticipants()
            uploadingUpdatesParticipants.start(hash_name_group, list_versions, onSuccess, onFailure)
        }
    }

    fun uploadingStPersonsSinch(hash_name_group:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==2*/true) {
            val uploadingStPersonsSinch = UploadingStPersonsSinch()
            uploadingStPersonsSinch.start(hash_name_group, onSuccess, onFailure)
        }
    }

    fun uploadingUpdatesStPersonsSinch(hash_name_group:String,last_index:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==2*/true) {
            val uploadingUpdatesStPersonsSinch = UploadingUpdatesStPersonsSinch()
            uploadingUpdatesStPersonsSinch.start(hash_name_group, last_index, onSuccess, onFailure)
        }
    }

    fun sendStPersonsData(hash_name_group:String,last_index:String,appStPersons: AppStPersons, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==2*/true) {
            val sendStPersonData = SendStPersonData()
            sendStPersonData.start(hash_name_group, last_index, appStPersons, onSuccess, onFailure)
        }
    }

    fun changePersonForGroup(hash_name_add: String,hash_name_group: String,name:String,gender:String,access:String,post:String,allowed:String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (/*AuthorizationState.stateIn==null||AuthorizationState.stateIn==1*/true) {
            val changePersonForGroup = ChangePersonForGroup()
            changePersonForGroup.start(
                hash_name_add,
                hash_name_group,
                name,
                gender,
                access,
                post,
                allowed,
                onSuccess,
                onFailure
            )
        }
    }
}

