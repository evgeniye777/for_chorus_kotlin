package com.example.for_chour_kotlin.data.model

data class AppAllGroups(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String /*уникальное*/,
    var name: String,
    var date: String,
    var location: String,
    var creator: String,
    //ссылки на базы данных
    var lisnNameBases: List<String>
)

data class AppAllPersons(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String /*уникальное*/,
    var login: String? = null,
    var password: String? = null,
    var hashPassword: String? = null,
    var name: String? = null,
    var vName: Int = 1,
    var email: String? = null,
    var vEmail: Int = 1,
    var dateReg: String? = null,
    var vDateReg: Int = 1,
    var dateLast: String? = null,
    var vDateLast: Int = 1,
    var birthDay: String? = null,
    var vBirthDay: Int = 1,
    var gender: String? = null,
    var vGender: Int = 1,
    var access: String? = null,
    var groups: List<String>
)

data class AppGroupDataParticipants(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String? /*уникальное*/,
    var date: String? = null,
    var pName: String?,
    var pGender: Int?,
    var post: String? = null,
    var allowed: Int = 0,
    var access: Int = 0,
    var visible: Int = 1
)

data class AppStPersonsLocalTime(
    var id: Int = 0,
    var committer: String,
    var dateWrite: String,
    var date: String,
    var purpose: Int = 0,
    var data: String,
    var comments: String,
    var c: List<String> = List(75) { "" }
)

data class AppStPersonsSinch(
    var id: Int = 0,
    var committer: String,
    var dateWrite: String,
    var date: String,
    var purpose: Int = 0,
    var data: String,
    var comments: String,
    var c: List<String> = List(75) { "" }
)

data class AppStSongsHistory(
    var id: Int = 0, // первичный ключ
    var committer: String,
    var dateWrite: String,
    var date: String,
    var purpose: Int = 0,
    var data: String,
    var comments: String
)

data class AppStSongsPlans(
    var id: Int = 0, // первичный ключ, автоинкремент
    var version: Int = -1,
    var committer: String,
    var dateWrite: String,
    var date: String,
    var purpose: Int = 0,
    var data: String,
    var comments: String,
    var visible: Int = 1
)

data class Triger(
    var id: Int = 0,
    var name: String,
    var znak: String
)

data class Type(
    var id: Int = 0,
    var name: String? = null,
    var values: List<String?> = List(9) { null } // Список из 9 элементов, инициализированных null
)

