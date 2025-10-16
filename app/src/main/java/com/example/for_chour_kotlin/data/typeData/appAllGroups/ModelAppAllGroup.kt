package com.example.for_chour_kotlin.data.typeData.appAllGroups

data class AppAllGroups(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String,
    var name: String? = null,
    var date_create: String? = null,
    var location: String? = null,
    var creator: String? = null,
    var listNameBases: HashMap<String,String> = HashMap(),
    var data: MutableList<TypeData>? = null,
    var nNotification: Int = 0,
    var visible: Int = 1
)
data class TypeData(
    val data: List<Item>,
    val type: String
)
data class Item(
    val id: String,
    val name: String,
    val flag: String? = null,
    val obligated: List<String>? = null
)