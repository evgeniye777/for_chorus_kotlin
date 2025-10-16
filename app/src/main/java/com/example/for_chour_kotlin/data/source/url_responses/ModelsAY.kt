package com.example.for_chour_kotlin.data.source.url_responses

import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData.appAllGroups.ViewModelAppAllGroups
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.ViewModelAppDataParticipant
import com.example.for_chour_kotlin.presentations.activities.MainActivity

data class AccountData(
    val version: String?,
    val hashName: String,
    val login: String,
    val name: String?,
    val email: String?,
    val dateReg: String?,
    val dateLast: String?,
    val birthDay: String?,
    val gender: String?,
    val access: String?,
    val groups: String?,
    val hashPassword: String?
)

object AccountHolder {
    var version: String? = null
    var hashName: String? = null
    var login: String? = null
    var name: String? = null
    var email: String? = null
    var dateReg: String? = null
    var dateLast: String? = null
    var birthDay: String? = null
    var gender: String? = null
    var access: String? = null
    var _groups: MutableList<String>? = null

    var groups: Any?
        get() = _groups
        set(value) {
            when (value) {
                is String -> {
                    // Если присвоена строка, парсим JSON в HashMap
                    _groups = JsonWork().jsonToList(value)
                }
                is HashMap<*, *> -> {
                    // Если присвоена HashMap, приводим и сохраняем
                    @Suppress("UNCHECKED_CAST")
                    _groups = value as MutableList<String>
                }
                else -> {
                    _groups = null
                }
            }
        }
    // Добавьте этот геттер для удобства
    val groupsMap: MutableList<String>?
        get() = _groups

    var hashPassword: String? = null
    fun clean() {
        version = null
        hashName = null
        login = null
        name = null
        email = null
        dateReg = null
        dateLast = null
        birthDay = null
        gender = null
        access = null
        groups = null
        hashPassword = null
    }
}

object AuthorizationState {
    var lastErrorMessage: String? = null
    var dataAuthorization: String? = null

    var database: SQLiteDatabase? = null

    var localDataAY: LocalDataAY? = null
    var mainActivity: MainActivity? = null

    var typeResponses: TypeResponses? = null

    var stateIn: Boolean? = false

    var groups: ViewModelAppAllGroups? = null
    var participants: ViewModelAppDataParticipant? = null
    fun clean() {
        lastErrorMessage = null
        dataAuthorization = null
        database = null
        localDataAY = null
        mainActivity =null
        typeResponses = null
        groups = null
    }
}
