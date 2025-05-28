package com.example.for_chour_kotlin.data.typeData.OnePersons

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