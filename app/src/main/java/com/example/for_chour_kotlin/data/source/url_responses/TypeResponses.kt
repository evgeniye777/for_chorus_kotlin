package com.example.for_chour_kotlin.data.source.url_responses

import com.example.for_chour_kotlin.data.source.url_responses.verif_options.CheckAuthorization
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.InAccount
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingGroupData
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingParticipantsData
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingUpdatesGroups
import com.example.for_chour_kotlin.data.source.url_responses.verif_options.UploadingUpdatesParticipants

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
        val checkAuthorization = CheckAuthorization()
        checkAuthorization.start(onSuccess, onFailure)
    }

    fun inAccount(loginIn: String, hash_password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        AccountHolder.hashPassword = hash_password
        val inAccount = InAccount()
        inAccount.start(loginIn,hash_password,onSuccess, onFailure)
    }

    fun uploadingGroupData(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val uploadingGroupData = UploadingGroupData()
        uploadingGroupData.start(onSuccess, onFailure)
    }

    fun uploadingUpdatesGroups(versions:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        val uploadingUpdatesGroups = UploadingUpdatesGroups()
        uploadingUpdatesGroups.start(versions,onSuccess, onFailure)
    }

    fun uploadingParticipantsData(hash_name_group:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        val uploadingParticipantsData = UploadingParticipantsData()
        uploadingParticipantsData.start(hash_name_group,onSuccess, onFailure)
    }

    fun uploadingUpdatesParticipants(hash_name_group:String,list_versions:String,onSuccess: () -> Unit, onFailure: () -> Unit) {
        val uploadingUpdatesParticipants = UploadingUpdatesParticipants()
        uploadingUpdatesParticipants.start(hash_name_group,list_versions,onSuccess, onFailure)
    }
}

