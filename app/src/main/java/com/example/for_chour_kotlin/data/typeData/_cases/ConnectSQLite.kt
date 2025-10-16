package com.example.for_chour_kotlin.data.typeData._cases

import android.content.Context
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.source.local.DataBases
import java.io.IOException

class ConnectSQLite(private val context: Context) {
    private val databaseHelper: DataBases = DataBases(context)

    init {
        updateDatabase()
        AuthorizationState.database = databaseHelper.writableDatabase
    }

    private fun updateDatabase() {
        try {
            databaseHelper.updateDataBase()
        } catch (e: IOException) {
            throw Error("Unable to update database")
        }
    }
}