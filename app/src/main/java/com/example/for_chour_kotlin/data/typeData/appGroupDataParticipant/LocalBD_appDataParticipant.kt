package com.example.for_chour_kotlin.data.typeData.appGroupDataParticipant

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.LocalBdListener

class LocalBD_appDataParticipant(
    private val viewModel: ViewModel_appDataParticipant,
    private val database: SQLiteDatabase
) : AppDataListener<AppGroupDataParticipant>, LocalBdListener<AppGroupDataParticipant> {

    private lateinit var nameTable: String
    init {
        viewModel.listener = this
    }
    // Локальная выгрузка всех участников
    @SuppressLint("Range")
    override fun readItems(nameTable: String) {
        this.nameTable = nameTable
        val cursor: Cursor = database.rawQuery("SELECT * FROM $nameTable", null)
        val participants = mutableListOf<AppGroupDataParticipant>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hashName = cursor.getString(cursor.getColumnIndex("hash_name"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val pName = cursor.getString(cursor.getColumnIndex("p_name"))
                val pGender = cursor.getInt(cursor.getColumnIndex("p_gender"))
                val post = cursor.getString(cursor.getColumnIndex("post"))
                val allowed = cursor.getInt(cursor.getColumnIndex("allowed"))
                val access = cursor.getInt(cursor.getColumnIndex("access"))
                val visible = cursor.getInt(cursor.getColumnIndex("visible"))

                participants.add(
                    AppGroupDataParticipant(
                        id,
                        version,
                        hashName,
                        date,
                        pName,
                        pGender,
                        post,
                        allowed,
                        access,
                        visible
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setItems(participants) // Предполагается, что у вас есть метод setItems в вашем ViewModel
    }

    // Добавление нового участника в таблицу app_group_chorus63
    override fun addItem(item: AppGroupDataParticipant): Int {
        val values = ContentValues().apply {
            put("hash_name", item.hashName)
            put("version", item.version)
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

    // Обновление данных участника в таблице app_group_chorus63
    override fun updateItem(item: AppGroupDataParticipant) {
        val values = ContentValues().apply {
            put("hash_name", item.hashName)
            put("version", item.version)
            put("date", item.date)
            put("p_name", item.pName)
            put("p_gender", item.pGender)
            put("post", item.post)
            put("allowed", item.allowed)
            put("access", item.access)
            put("visible", item.visible)
        }
        database.update(nameTable, values, "id=?", arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Удаление участника из таблицы app_group_chorus63
    override fun deleteItem(item: AppGroupDataParticipant) {
        database.delete(nameTable, "id=?", arrayOf(item.id.toString()))
    }

    // Метод для создания новой таблицы
    override fun createTable(nameTable: String) {
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS $nameTable (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            version INTEGER,
            hash_name TEXT,
            date TEXT,
            p_name TEXT,
            p_gender INTEGER,
            post TEXT,
            allowed INTEGER,
            access INTEGER,
            visible INTEGER
        )
    """.trimIndent()
        database.execSQL(createTableQuery)
    }

    // Метод для удаления таблицы
    override fun deleteTable(nameTable: String) {
        val dropTableQuery = "DROP TABLE IF EXISTS $nameTable"
        database.execSQL(dropTableQuery)
    }



    override fun onAppDataChanged(data: AppGroupDataParticipant, action: Int): Int {
        return when (action) {
            0 -> addItem(data) // Добавить
            1 -> {
                updateItem(data) // Изменить
                data.id // Возвращаем ID
            }
            2 -> {
                deleteItem(data) // Удалить
                -1 // Возвращаем -1, если удалено
            }
            else -> -1 // Возвращаем -1 для неподдерживаемого действия
        }
    }
}
