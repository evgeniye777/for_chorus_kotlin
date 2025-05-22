package com.example.for_chour_kotlin.data.model

data class AppOneGroupModel(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String /*уникальное*/,
    var name: String,
    var date: String,
    var location: String,
    var creator: String,
    //ссылки на базы данных
    var lisnNameBases: List<String>,
    var n_notification: Int = 0
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

//модель одного участника группы
data class AppGroupDataParticipant(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String? = null,
    var date: String? = null,
    var pName: String? = null,
    var pGender: Int? = 0,
    var post: String? = null,
    var allowed: Int = 0,
    var access: Int = 0,
    var visible: Int = 1
)

data class AppStPersonsLocalTime(
    override var id: Int = 0,
    override var committer: String? = null,
    override var dateWrite: String? = null,
    override var date: String? = null,
    override var purpose: Int = 0,
    override var data: String? = null,
    override var comments: String? = null,
    override var c: MutableList<String>  = MutableList(75) { "" }
): AppStPersons

data class AppStPersonsSinch(
    override var sinch: Boolean = true,
    override var id: Int = 0,
    override var committer: String? = null,
    override var dateWrite: String? = null,
    override var date: String? = null,
    override var purpose: Int = 0,
    override var data: String? = null,
    override var comments: String? = null,
    override var c: MutableList<String>  = MutableList(75) { "" }
): AppStPersons,sinchMark

data class AppStSongsHistory(
    var id: Int = 0, // первичный ключ
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int = 0,
    var data: String? = null,
    var comments: String? = null
)

data class AppStSongsPlans(
    override var sinch: Boolean = true,
    var id: Int = 0, // первичный ключ, автоинкремент
    var version: Int = -1,
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int = 0,
    var data: String? = null,
    var comments: String? = null,
    var visible: Int = 1
): sinchMark

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

