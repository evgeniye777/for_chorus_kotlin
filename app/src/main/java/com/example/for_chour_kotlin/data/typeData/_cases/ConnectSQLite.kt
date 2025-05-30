package com.example.for_chour_kotlin.data.typeData._cases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.source.local.DataBases
import java.io.IOException

class ConnectSQLite(private val context: Context) {
    private val databaseHelper: DataBases = DataBases(context)
    private var database: SQLiteDatabase

    init {
        updateDatabase()
        database = databaseHelper.writableDatabase
    }

    private fun updateDatabase() {
        try {
            databaseHelper.updateDataBase()
        } catch (e: IOException) {
            throw Error("Unable to update database")
        }
    }
    fun getDataBase(): SQLiteDatabase {
        return database
    }
}