package com.example.for_chour_kotlin.data.typeData.appStSongsHistory

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppStSongsHistory(
    private val database: SQLiteDatabase
) : DataOperations<AppStSongsHistory> {

    private lateinit var nameTable: String
    val jsW: JsonWork = JsonWork()

    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppStSongsHistory> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appstsongshistory = mutableListOf<AppStSongsHistory>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val committer = cursor.getString(cursor.getColumnIndex("committer"))
                val date_write = cursor.getString(cursor.getColumnIndex("date_write"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val purpose = cursor.getInt(cursor.getColumnIndex("purpose"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val comments = cursor.getString(cursor.getColumnIndex("comments"))
                appstsongshistory.add(
                    AppStSongsHistory(
                        id,
                        committer,
                        date_write,
                        date,
                        purpose,
                        jsW.jsonToListMH(data),
                        comments
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appstsongshistory
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppStSongsHistory): Int {
        val values = ContentValues().apply {
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", jsW.listToJsonMH(item.data))
            put("comments", item.comments)
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppStSongsHistory): Int {
        val values = ContentValues().apply {
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", jsW.listToJsonMH(item.data))
            put("comments", item.comments)
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppStSongsHistory): Int {
        return destroyItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppStSongsHistory): Int {
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
    purpose INTEGER NOT NULL  DEFAULT 0,
    data TEXT NOT NULL,
    comments TEXT NOT NULL
)
""".trimIndent()
            database.execSQL(createTableQuery)
            return 1
        } catch (e: Exception) {
            return -1
        }
    }
}