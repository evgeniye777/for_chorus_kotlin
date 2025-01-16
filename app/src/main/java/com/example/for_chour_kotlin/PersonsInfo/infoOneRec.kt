package com.example.for_chour_kotlin.PersonsInfo

class infoOneRec {
    var id: Int = -1
    var version: Int = -1
    lateinit var date: String
    var purpose: Int = -1
    lateinit var data: String
    lateinit var comments: String
    lateinit var list: List<String>

    constructor(id0: Int, version0: Int,date0: String,purpose0:Int)
    {
        id = id0; version = version0; date = date0; purpose = purpose0;
    }
    fun addToList(c: List<String>) {
        list = c
    }
    fun getRecList(): List<String> {
        return list
    }
    fun getRecDate(): String {
        return date
    }
}
