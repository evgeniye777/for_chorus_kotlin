package com.example.for_chour_kotlin.data.typeData.appStPersonsLocalTime

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppStPersonsLocalTime() : ViewModel(), DataOperations<AppStPersonsLocalTime> {

    var database: SQLiteDatabase? = null
    var localbdAppStPersonsLocalTime: LocalBDAppStPersonsLocalTime? = null
    var repositoryAppStPersonsLocalTime: RepositoryAppStPersonsLocalTime? = null

    private val _groups = MutableLiveData<List<AppStPersonsLocalTime>?>()
    val groups: LiveData<List<AppStPersonsLocalTime>?> get() = _groups

    lateinit var nameTable: String

    // Метод для подключения к таблице(создает при отстутствии)
    override fun connection(database0: SQLiteDatabase?, nameTable: String): Int {
        if (database0 != null) {
            database = database0
        }
        this.nameTable = nameTable
        if (database == null || nameTable.isEmpty()) {
            return -1
        }
        localbdAppStPersonsLocalTime = LocalBDAppStPersonsLocalTime(database!!)
        repositoryAppStPersonsLocalTime = RepositoryAppStPersonsLocalTime(_groups)
        repositoryAppStPersonsLocalTime?.setItems(localbdAppStPersonsLocalTime!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppStPersonsLocalTime): Int {
        val newId: Int = localbdAppStPersonsLocalTime?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppStPersonsLocalTime?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStPersonsLocalTime): Int {
        val n: Int = localbdAppStPersonsLocalTime?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppStPersonsLocalTime?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppStPersonsLocalTime): Int {
        val n: Int = localbdAppStPersonsLocalTime?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersonsLocalTime?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppStPersonsLocalTime): Int {
        val n: Int = localbdAppStPersonsLocalTime?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersonsLocalTime?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppStPersonsLocalTime?.deleteTable(nameTable) ?: -1
        if (n == 1) {
            setItems(null)
        }
        return n
    }

    // Метод для создания таблиц
    override fun createTable(nameTable0: String): Int {
        if (nameTable0.isEmpty()) {
            return -1
        }
        val n = localbdAppStPersonsLocalTime?.createTable(nameTable) ?: -1
        return n
    }
}