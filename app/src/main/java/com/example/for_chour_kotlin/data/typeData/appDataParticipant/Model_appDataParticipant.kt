package com.example.for_chour_kotlin.data.typeData.appDataParticipant

data class AppDataParticipant(
    var id: Int = 0,
    var idC: String,
    var version: Int = -1,
    var hashName: String? = null,
    var date: String? = null,
    var pName: String? = null,
    var pGender: Int? = null,
    var post: MutableList<String>,
    var allowed: Int = 0,
    var access: Int = 0,
    var visible: Int = 1
)