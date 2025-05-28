package com.example.for_chour_kotlin.data.typeData.OneGroup

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.LocalBdListener

class LocalBD_appOneGroup(
    private val viewModel: ViewModel_appOneGroup,
    private var database: SQLiteDatabase
) : AppDataListener<AppOneGroup>, LocalBdListener<AppOneGroup> {

    private val jsW: JsonWork = JsonWork()

    init {
        viewModel.listener = this
    }

    // Локальная выгрузка всех групп
    @SuppressLint("Range")
    override fun readItems(nameTable: String) {
        val cursor: Cursor = database.rawQuery("SELECT * FROM app_all_groups", null)
        val groups = mutableListOf<AppOneGroup>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hashName = cursor.getString(cursor.getColumnIndex("hash_name"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val location = cursor.getString(cursor.getColumnIndex("location"))
                val creator = cursor.getString(cursor.getColumnIndex("creator"))
                val bases = cursor.getString(cursor.getColumnIndex("bases"))

                groups.add(
                    AppOneGroup(
                        id,
                        version,
                        hashName,
                        name,
                        date,
                        location,
                        creator,
                        jsW.jsonToList(bases) // Преобразуем строку JSON в List<String>
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setItems(groups) // Предполагается, что у вас есть метод setItems в вашем ViewModel
    }

    // Добавление новой группы в таблицу AppAllGroups
    override fun addItem(item: AppOneGroup): Int {
        val values = ContentValues().apply {
            put("hash_name", item.hashName)
            put("name", item.name)
            put("date", item.date)
            put("location", item.location)
            put("creator", item.creator)
            put("bases", jsW.listToJson(item.lisnNameBases)) // Преобразуем список баз в строку
        }
        return database.insert("app_all_groups", null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных группы в таблице AppAllGroups
    override fun updateItem(item: AppOneGroup) {
        val values = ContentValues().apply {
            put("hash_name", item.hashName)
            put("name", item.name)
            put("date", item.date)
            put("location", item.location)
            put("creator", item.creator)
            put("bases", jsW.listToJson(item.lisnNameBases)) // Преобразуем список баз в строку
        }
        database.update(
            "app_all_groups",
            values,
            "id=?",
            arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Удаление группы из таблицы AppAllGroups
    override fun deleteItem(item: AppOneGroup) {
        database.delete("app_all_groups", "id=?", arrayOf(item.id.toString()))
    }

    override fun onAppDataChanged(data: AppOneGroup, action: Int): Int {
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
