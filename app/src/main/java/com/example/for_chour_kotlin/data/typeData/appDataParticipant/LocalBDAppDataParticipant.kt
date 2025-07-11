package com.example.for_chour_kotlin.data.typeData.appDataParticipant
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class LocalBDAppDataParticipant(
    private val database: SQLiteDatabase
) : DataOperations<AppDataParticipant> {

    private lateinit var nameTable: String

    // Локальная выгрузка всех данных
    @SuppressLint("Range")
    override fun readItems(nameTable: String): MutableList<AppDataParticipant> {
        this.nameTable = nameTable

        if (!isTableExists(nameTable)) {
            createTable(nameTable)
        }
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val appdataparticipant = mutableListOf<AppDataParticipant>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val id_c = cursor.getString(cursor.getColumnIndex("id_c"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hash_name = cursor.getString(cursor.getColumnIndex("hash_name"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val p_name = cursor.getString(cursor.getColumnIndex("p_name"))
                val p_gender = cursor.getInt(cursor.getColumnIndex("p_gender"))
                val post = cursor.getString(cursor.getColumnIndex("post"))
                val allowed = cursor.getInt(cursor.getColumnIndex("allowed"))
                val access = cursor.getInt(cursor.getColumnIndex("access"))
                val visible = cursor.getInt(cursor.getColumnIndex("visible"))
                appdataparticipant.add(AppDataParticipant(
                    id,
                    id_c,
                    version,
                    hash_name,
                    date,
                    p_name,
                    p_gender,
                    post,
                    allowed,
                    access,
                    visible
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return appdataparticipant
    }

    // Добавление данных в таблицу
    override fun addItem(item: AppDataParticipant): Int {
        val values = ContentValues().apply {
            put("id_c", item.idC)
            put("version", item.version)
            put("hash_name", item.hashName)
            put("date", item.date)
            put("p_name", item.pName)
            put("p_gender", item.pGender)
            put("post", item.post)
            put("allowed", item.allowed)
            put("access", item.access)
            put("visible", item.visible)
        }
        return database.insert(nameTable, null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных в таблице
    override fun updateItem(item: AppDataParticipant): Int {
        val values = ContentValues().apply {
            put("id_c", item.idC)
            put("version", item.version)
            put("hash_name", item.hashName)
            put("date", item.date)
            put("p_name", item.pName)
            put("p_gender", item.pGender)
            put("post", item.post)
            put("allowed", item.allowed)
            put("access", item.access)
            put("visible", item.visible)
        }
        return database.update(
            nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Возобновляемое удаление из таблицы
    override fun deleteItem(item: AppDataParticipant): Int {
        item.visible = -3
        return updateItem(item)
    }

    // Абсолютное удаление из таблицы
    override fun destroyItem(item: AppDataParticipant): Int {
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
    id_c TEXT NOT NULL UNIQUE,
    version INTEGER NOT NULL  DEFAULT -1,
    hash_name TEXT NOT NULL UNIQUE,
    date TEXT,
    p_name TEXT NOT NULL,
    p_gender INTEGER NOT NULL,
    post TEXT NOT NULL,
    allowed INTEGER NOT NULL  DEFAULT 0,
    access INTEGER NOT NULL  DEFAULT 0,
    visible INTEGER NOT NULL  DEFAULT 1
)
""".trimIndent()
            database.execSQL(createTableQuery)
            return 1
        } catch (e: Exception) {
            return -1
        }
    }
}