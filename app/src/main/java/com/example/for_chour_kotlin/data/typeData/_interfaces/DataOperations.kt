package com.example.for_chour_kotlin.data.typeData._interfaces

import android.database.sqlite.SQLiteDatabase

interface DataOperations<T> {
    fun createTable(nameTable: String = ""): Int {
        println("createTable")
        return -1
    }
    fun deleteTable(nameTable: String = ""): Int {
        println("delTable")
        return -1
    }
    fun readItems(nameTable: String = "") : MutableList<T> {return mutableListOf()
    }

    fun connection(database: SQLiteDatabase? = null, nameTable: String): Int {
        println("connection")
        return -1
    }

    fun setItems(list: MutableList<T>?) {
        println("setItems")
    }

    fun addItem(item: T): Int {return -1}
    fun updateItem(item: T): Int {return -1}

    fun addOrUpdateItem(item: T): Int {return -1}
    fun deleteItem(item: T): Int {return -1}
    fun destroyItem(item: T): Int {return -1}
}