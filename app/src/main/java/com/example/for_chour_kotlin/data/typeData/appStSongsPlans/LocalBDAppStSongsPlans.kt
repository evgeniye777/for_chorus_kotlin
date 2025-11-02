package com.example.for_chour_kotlin.data.typeData.appStSongsPlans

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppStSongsPlans(
    private val database: SQLiteDatabase
) : DataOperations<AppStSongsPlans> {

    private lateinit var nameTable: String
    val jsW: JsonWork = JsonWork()

    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppStSongsPlans> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appstsongsplans = mutableListOf<AppStSongsPlans>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val committer = cursor.getString(cursor.getColumnIndex("committer"))
                val date_write = cursor.getString(cursor.getColumnIndex("date_write"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val purpose = cursor.getInt(cursor.getColumnIndex("purpose"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val comments = cursor.getString(cursor.getColumnIndex("comments"))
                val visible = cursor.getInt(cursor.getColumnIndex("visible"))
                appstsongsplans.add(
                    AppStSongsPlans(
                        id,
                        version,
                        committer,
                        date_write,
                        date,
                        purpose,
                        jsW.jsonToListMH(data),
                        comments,
                        visible
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appstsongsplans
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppStSongsPlans): Int {
        val values = ContentValues().apply {
            put("version", item.version)
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", jsW.listToJsonMH(item.data))
            put("comments", item.comments)
            put("visible", item.visible)
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppStSongsPlans): Int {
        val values = ContentValues().apply {
            put("version", item.version)
            put("committer", item.committer)
            put("date_write", item.dateWrite)
            put("date", item.date)
            put("purpose", item.purpose)
            put("data", jsW.listToJsonMH(item.data))
            put("comments", item.comments)
            put("visible", item.visible)
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppStSongsPlans): Int {
        item.visible = -3
        return updateItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppStSongsPlans): Int {
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
    id INTEGER UNIQUE,
    version INTEGER,
    committer TEXT,
    date_write TEXT,
    date TEXT,
    purpose INTEGER,
    data TEXT,
    comments TEXT,
    visible INTEGER
)
""".trimIndent()
            database.execSQL(createTableQuery)
            return 1
        } catch (e: Exception) {
            return -1
        }
    }
}