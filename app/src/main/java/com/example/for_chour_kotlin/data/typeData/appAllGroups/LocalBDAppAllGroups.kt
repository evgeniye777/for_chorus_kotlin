package com.example.for_chour_kotlin.data.typeData.appAllGroups

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppAllGroups(
    private val database: SQLiteDatabase
) : DataOperations<AppAllGroups> {

    private lateinit var nameTable: String
    val jsW: JsonWork = JsonWork()

    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppAllGroups> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appallgroups = mutableListOf<AppAllGroups>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hash_name = cursor.getString(cursor.getColumnIndex("hash_name"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val location = cursor.getString(cursor.getColumnIndex("location"))
                val creator = cursor.getString(cursor.getColumnIndex("creator"))
                val list_name_bases = cursor.getString(cursor.getColumnIndex("list_name_bases"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val n_notification = cursor.getInt(cursor.getColumnIndex("n_notification"))
                val visible = cursor.getInt(cursor.getColumnIndex("visible"))

                    appallgroups.add(
                        AppAllGroups(
                            id,
                            version,
                            hash_name,
                            name,
                            date,
                            location,
                            creator,
                            jsW.jsonToListH(list_name_bases),
                            jsW.jsonToTypeDataList(data),
                            n_notification,
                            visible
                        )
                    )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appallgroups
    }

    // Вспомогательный метод: Проверить существование по id
    private fun existsById(id: Int): Boolean {
        if (id < 0) return false
        val query = "SELECT id FROM $nameTable WHERE id = ? LIMIT 1"
        val cursor = database.rawQuery(query, arrayOf(id.toString()))
        return try {
            cursor.moveToFirst() // Если true — существует
        } finally {
            cursor.close()
        }
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppAllGroups): Int {
        if (existsById(item.id)) {return updateItem(item);}
        val values = ContentValues().apply {
            put("version", item.version)
            put("hash_name", item.hashName)
            put("name", item.name)
            put("date", item.date_create)
            put("location", item.location)
            put("creator", item.creator)
            put("list_name_bases", jsW.listToJsonH(item.listNameBases))
            put("data", item.data?.let { jsW.typeDataListToJson(it) })
            put("n_notification", item.nNotification)
            put("visible", item.visible)
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppAllGroups): Int {
        val values = ContentValues().apply {
            put("version", item.version)
            put("hash_name", item.hashName)
            put("name", item.name)
            put("date", item.date_create)
            put("location", item.location)
            put("creator", item.creator)
            put("list_name_bases", jsW.listToJsonH(item.listNameBases))
            put("data", item.data?.let { jsW.typeDataListToJson(it) })
            put("n_notification", item.nNotification)
            put("visible", item.visible)
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppAllGroups): Int {
        item.visible = -3
        return updateItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppAllGroups): Int {
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
    hash_name TEXT UNIQUE,
    name TEXT UNIQUE,
    date TEXT,
    location TEXT,
    creator TEXT,
    list_name_bases TEXT,
    data TEXT,
    n_notification INTEGER,
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