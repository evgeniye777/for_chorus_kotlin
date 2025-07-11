package com.example.for_chour_kotlin.data.typeData.appStPersonsSinch

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppStPersonsSinch() : ViewModel(), DataOperations<AppStPersonsSinch> {

    var database: SQLiteDatabase? = null
    var localbdAppStPersonsSinch: LocalBDAppStPersonsSinch? = null
    var repositoryAppStPersonsSinch: RepositoryAppStPersonsSinch? = null

    private val _groups = MutableLiveData<List<AppStPersonsSinch>?>()
    val groups: LiveData<List<AppStPersonsSinch>?> get() = _groups

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
        localbdAppStPersonsSinch = LocalBDAppStPersonsSinch(database!!)
        repositoryAppStPersonsSinch = RepositoryAppStPersonsSinch(_groups)
        repositoryAppStPersonsSinch?.setItems(localbdAppStPersonsSinch!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppStPersonsSinch): Int {
        val newId: Int = localbdAppStPersonsSinch?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppStPersonsSinch?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStPersonsSinch): Int {
        val n: Int = localbdAppStPersonsSinch?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppStPersonsSinch?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppStPersonsSinch): Int {
        val n: Int = localbdAppStPersonsSinch?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersonsSinch?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppStPersonsSinch): Int {
        val n: Int = localbdAppStPersonsSinch?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersonsSinch?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppStPersonsSinch?.deleteTable(nameTable) ?: -1
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
        val n = localbdAppStPersonsSinch?.createTable(nameTable) ?: -1
        return n
    }
}