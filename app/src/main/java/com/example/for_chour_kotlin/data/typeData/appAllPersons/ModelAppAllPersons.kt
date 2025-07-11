package com.example.for_chour_kotlin.data.typeData.appAllPersons

data class AppAllPersons(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String? = null,
    var name: String,
    var vName: Int = 1,
    var email: String,
    var vEmail: Int = 1,
    var dateReg: String,
    var vDateReg: Int = 1,
    var dateLast: String,
    var vDateLast: Int = 1,
    var birthDay: String,
    var vBirthDay: Int = 1,
    var gender: String,
    var vGender: Int = 1,
    var access: String,
    var accessP: String,
    var groups: MutableList<String> = mutableListOf(),
    var groupsP: String,
    var visible: Int = 1
)