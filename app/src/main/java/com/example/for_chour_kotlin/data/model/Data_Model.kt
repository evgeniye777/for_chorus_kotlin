package com.example.for_chour_kotlin.data.model

data class AppStSongsHistory(
    var id: Int = 0, // первичный ключ
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int = 0,
    var data: MutableList<HashMap<String,String>>? = null,
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
    var data: MutableList<HashMap<String,String>>? = null,
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

