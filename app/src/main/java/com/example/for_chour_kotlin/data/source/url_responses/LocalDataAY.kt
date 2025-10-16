package com.example.for_chour_kotlin.data.source.url_responses

import android.annotation.SuppressLint
import androidx.core.database.sqlite.transaction

class LocalDataAY() {
    val database = AuthorizationState.database
    fun createTableIfNotExists() {
        database?.execSQL("""
            CREATE TABLE IF NOT EXISTS account (
                field TEXT NOT NULL UNIQUE,
                value TEXT
            )
        """.trimIndent())
    }

    fun getAccountData(): AccountData? {
        val fields = listOf( "version", "hash_name", "login", "name", "email",
            "date_reg", "date_last", "birth_day", "gender", "access", "groups", "hash_password"
        )

        val map = mutableMapOf<String, String?>()

        for (field in fields) {
            map[field] = getFieldString(field)
        }

        val hashNameStr = map["hash_name"]
        val loginStr = map["login"]
        val hashPasswordStr = map["hash_password"]

        if (hashNameStr.isNullOrEmpty() || loginStr.isNullOrEmpty() || hashPasswordStr.isNullOrEmpty()) {
            return null
        }

        return AccountData(
            version = map["version"],
            hashName = hashNameStr,
            login = loginStr,
            name = map["name"],
            email = map["email"],
            dateReg = map["date_reg"],
            dateLast = map["date_last"],
            birthDay = map["birth_day"],
            gender = map["gender"],
            access = map["access"],
            groups = map["groups"],
            hashPassword = map["hash_password"]
        )
    }

    @SuppressLint("Range")
    fun getFieldString(fieldName: String): String? {
        val cursor = database?.query(
            "account",
            arrayOf("value"),
            "field = ?",
            arrayOf(fieldName),
            null, null, null, "1"
        )
        if (cursor==null) return null
        return try {
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow("value"))
            } else null
        } finally {
            cursor.close()
        }
    }

    fun saveAccountData(data: AccountData): Boolean {
        return try {
            database?.transaction {
                try {
                    saveFieldString("version", data.version)
                    saveFieldString("hash_name", data.hashName)
                    if (data.hashPassword!=null) {
                        saveFieldString("hash_password", data.hashPassword)
                    }
                    saveFieldString("login", data.login)
                    saveFieldString("name", data.name)
                    saveFieldString("email", data.email)
                    saveFieldString("date_reg", data.dateReg)
                    saveFieldString("date_last", data.dateLast)
                    saveFieldString("birth_day", data.birthDay)
                    saveFieldString("gender", data.gender)
                    saveFieldString("access", data.access)
                    saveFieldString("groups", data.groups)
                } finally {
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun saveFieldString(field: String, value: String?) {
        val valToSave = value ?: ""
        val stmt = database?.compileStatement("UPDATE account SET value = ? WHERE field = ?")
        if (stmt==null) return
        stmt.bindString(1, valToSave)
        stmt.bindString(2, field)
        val updatedRows = stmt.executeUpdateDelete()
        if (updatedRows == 0) {
            database.execSQL(
                "INSERT INTO account(field, value) VALUES(?, ?)",
                arrayOf(field, valToSave)
            )
        }
    }

    fun clearAccountTable() {
       database?.execSQL("DELETE FROM account")
    }
}