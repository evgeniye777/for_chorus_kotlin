package com.example.for_chour_kotlin.data.typeData.appStPersonsSinch

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppStPersonsSinch(
    private val database: SQLiteDatabase
) : DataOperations<AppStPersonsSinch> {

    private lateinit var nameTable: String
    val jsW: JsonWork = JsonWork()

    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppStPersonsSinch> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appstpersonssinch = mutableListOf<AppStPersonsSinch>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val committer = cursor.getString(cursor.getColumnIndex("committer"))
                val date_write = cursor.getString(cursor.getColumnIndex("date_write"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val purpose = cursor.getInt(cursor.getColumnIndex("purpose"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val comments = cursor.getString(cursor.getColumnIndex("comments"))
                val c = cursor.getString(cursor.getColumnIndex("c"))
                appstpersonssinch.add(
                    AppStPersonsSinch(
                        id,
                        committer,
                        date_write,
                        date,
                        purpose,
                        data,
                        comments,
                        jsW.jsonToList(c)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appstpersonssinch
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppStPersonsSinch): Int {
        val values = ContentValues().apply {
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", item.data)
            put("comments", item.comments)
            put("c", jsW.listToJson(item.c))
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppStPersonsSinch): Int {
        val values = ContentValues().apply {
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", item.data)
            put("comments", item.comments)
            put("c", jsW.listToJson(item.c))
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppStPersonsSinch): Int {
        return destroyItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppStPersonsSinch): Int {
        return database.delete(nameTable, "id=?", arrayOf(item.id.toString()))
    }

    // Метод для удаления таблицы
    override fun deleteTable(nameTable: String): Int {
        try {
            database.execSQL("DROP TABLE IF EXISTS $nameTable")
            return 1
        } catch (e: Exception) {
            return -1
        }
    }

    // Метод для проверки существования таблицы
    private fun isTableExists(tableName: String): Boolean {
        val cursor = database.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='$tableName'",
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Метод для создания новой таблицы
    override fun createTable(nameTable: String): Int {
        try {
            val createTableQuery = """
CREATE TABLE IF NOT EXISTS $nameTable (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    committer TEXT NOT NULL,
    date_write TEXT NOT NULL,
    date TEXT NOT NULL,
    purpose INTEGER NOT NULL,
    data TEXT NOT NULL,
    comments TEXT NOT NULL,
    c TEXT NOT NULL
)
""".trimIndent()
            database.execSQL(createTableQuery)
            return 1
        } catch (e: Exception) {
            return -1
        }
    }
}