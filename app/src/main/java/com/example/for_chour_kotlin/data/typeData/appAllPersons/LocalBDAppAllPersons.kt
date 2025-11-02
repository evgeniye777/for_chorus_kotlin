package com.example.for_chour_kotlin.data.typeData.appAllPersons

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppAllPersons(
    private val database: SQLiteDatabase
) : DataOperations<AppAllPersons> {

    private lateinit var nameTable: String
    val jsW: JsonWork = JsonWork()
    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppAllPersons> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appallpersons = mutableListOf<AppAllPersons>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hash_name = cursor.getString(cursor.getColumnIndex("hash_name"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val v_name = cursor.getInt(cursor.getColumnIndex("v_name"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val v_email = cursor.getInt(cursor.getColumnIndex("v_email"))
                val date_reg = cursor.getString(cursor.getColumnIndex("date_reg"))
                val v_date_reg = cursor.getInt(cursor.getColumnIndex("v_date_reg"))
                val date_last = cursor.getString(cursor.getColumnIndex("date_last"))
                val v_date_last = cursor.getInt(cursor.getColumnIndex("v_date_last"))
                val birth_day = cursor.getString(cursor.getColumnIndex("birth_day"))
                val v_birth_day = cursor.getInt(cursor.getColumnIndex("v_birth_day"))
                val gender = cursor.getString(cursor.getColumnIndex("gender"))
                val v_gender = cursor.getInt(cursor.getColumnIndex("v_gender"))
                val access = cursor.getString(cursor.getColumnIndex("access"))
                val access_p = cursor.getString(cursor.getColumnIndex("access_p"))
                val groups = cursor.getString(cursor.getColumnIndex("groups"))
                val groups_p = cursor.getString(cursor.getColumnIndex("groups_p"))
                val visible = cursor.getInt(cursor.getColumnIndex("visible"))
                appallpersons.add(AppAllPersons(
                    id,
                    version,
                    hash_name,
                    name,
                    v_name,
                    email,
                    v_email,
                    date_reg,
                    v_date_reg,
                    date_last,
                    v_date_last,
                    birth_day,
                    v_birth_day,
                    gender,
                    v_gender,
                    access,
                    access_p,
                    jsW.jsonToListH(groups),
                    groups_p,
                    visible
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appallpersons
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppAllPersons): Int {
        val values = ContentValues().apply {
            put("version", item.version)
            put("hash_name", item.hashName)
            put("name", item.name)
            put("v_name", item.vName)
            put("email", item.email)
            put("v_email", item.vEmail)
            put("date_reg", item.dateReg)
            put("v_date_reg", item.vDateReg)
            put("date_last", item.dateLast)
            put("v_date_last", item.vDateLast)
            put("birth_day", item.birthDay)
            put("v_birth_day", item.vBirthDay)
            put("gender", item.gender)
            put("v_gender", item.vGender)
            put("access", item.access)
            put("access_p", item.accessP)
            put("groups", jsW.listToJsonH(item.groups))
            put("groups_p", item.groupsP)
            put("visible", item.visible)
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppAllPersons): Int {
        val values = ContentValues().apply {
            put("version", item.version)
            put("hash_name", item.hashName)
            put("name", item.name)
            put("v_name", item.vName)
            put("email", item.email)
            put("v_email", item.vEmail)
            put("date_reg", item.dateReg)
            put("v_date_reg", item.vDateReg)
            put("date_last", item.dateLast)
            put("v_date_last", item.vDateLast)
            put("birth_day", item.birthDay)
            put("v_birth_day", item.vBirthDay)
            put("gender", item.gender)
            put("v_gender", item.vGender)
            put("access", item.access)
            put("access_p", item.accessP)
            put("groups", jsW.listToJsonH(item.groups))
            put("groups_p", item.groupsP)
            put("visible", item.visible)
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppAllPersons): Int {
        item.visible = -3
        return updateItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppAllPersons): Int {
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
    name TEXT,
    v_name INTEGER,
    email TEXT,
    v_email INTEGER,
    date_reg TEXT,
    v_date_reg INTEGER,
    date_last TEXT,
    v_date_last INTEGER,
    birth_day TEXT,
    v_birth_day INTEGER,
    gender TEXT,
    v_gender INTEGER,
    access TEXT,
    access_p TEXT,
    groups TEXT,
    groups_p TEXT,
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