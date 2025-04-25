package com.example.for_chour_kotlin.data.model

data class AppAllGroups(
    val id: Int = 0,
    val version: Int = -1,
    val hashName: String,
    val name: String,
    val date: String,
    val location: String,
    val creator: String,
    val bases: String
)

data class AppAllPersons(
    val id: Int = 0,
    val version: Int = -1,
    val hashName: String,
    val login: String? = null,
    val password: String? = null,
    val hashPassword: String? = null,
    val name: String? = null,
    val vName: Int = 1,
    val email: String? = null,
    val vEmail: Int = 1,
    val dateReg: String? = null,
    val vDateReg: Int = 1,
    val dateLast: String? = null,
    val vDateLast: Int = 1,
    val birthDay: String? = null,
    val vBirthDay: Int = 1,
    val gender: String? = null,
    val vGender: Int = 1,
    val access: String? = null,
    val groups: String? = null
)

data class AppGroupChorus63(
    val id: Int = 0,
    val version: Int = -1,
    val hashName: String?,
    val date: String? = null,
    val pName: String?,
    val pGender: Int?,
    val post: String? = null,
    val allowed: Int = 0,
    val access: Int = 0,
    val visible: Int = 1
)
