package com.example.for_chour_kotlin.data.typeData.appDataParticipant

data class AppDataParticipant(
    var id: Int = 0,
    var idC: String? = null,
    var version: Int = -1,
    var hashName: String? = null,
    var date: String,
    var pName: String? = null,
    var pGender: Int? = null,
    var post: String? = null,
    var allowed: Int = 0,
    var access: Int = 0,
    var visible: Int = 1
)