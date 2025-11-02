package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountData
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.source.url_responses.LocalDataAY
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class CheckAuthorization() {
    val localDataAY: LocalDataAY? = AuthorizationState.localDataAY
    fun start(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val accountData = localDataAY?.getAccountData()
        if (accountData == null) {
            onFailure()
            return
        }

        AccountHolder.apply {
            version = accountData.version
            hashName = accountData.hashName
            login = accountData.login
            name = accountData.name
            email = accountData.email
            dateReg = accountData.dateReg
            dateLast = accountData.dateLast
            birthDay = accountData.birthDay
            gender = accountData.gender
            access = accountData.access
            groups = accountData.groups
            hashPassword = accountData.hashPassword
        }
        ThreadURL().start(buildPostParams(accountData),onSuccess,onFailure)
    }
    private fun buildPostParams(accountData: AccountData): String {
        val version = accountData.version ?: "-2"
        val params = mapOf(
            "Authorization" to "",
            "CheckUser" to "",
            "hash_name" to accountData.hashName,
            "hash_password" to accountData.hashPassword,
            "version" to version
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}