package com.example.for_chour_kotlin.data.typeData.appAllGroups

import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelAppAllGroups() : ViewModel(), DataOperations<AppAllGroups> {

    var database: SQLiteDatabase? = null
    var localbdAppAllGroups: LocalBDAppAllGroups? = null
    var repositoryAppAllGroups: RepositoryAppAllGroups? = null

    private val _groups = MutableLiveData<List<AppAllGroups>?>()
    val groups: LiveData<List<AppAllGroups>?> get() = _groups

    lateinit var nameTable: String

    // Метод для подключения к таблице(создает при отстутствии)
    override fun connection(database0: SQLiteDatabase?, nameTable: String): Int {
        if (database0 != null) { database = database0 }
        this.nameTable = nameTable
        if (database == null || nameTable.isEmpty()) { return -1 }
        localbdAppAllGroups = LocalBDAppAllGroups(database!!)
        repositoryAppAllGroups = RepositoryAppAllGroups(_groups)
        repositoryAppAllGroups?.setItems(localbdAppAllGroups!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppAllGroups): Int {
        val newId: Int = localbdAppAllGroups?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppAllGroups?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppAllGroups): Int {
        val n: Int = localbdAppAllGroups?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppAllGroups?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppAllGroups): Int {
        val n: Int = localbdAppAllGroups?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppAllGroups?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppAllGroups): Int {
        val n: Int = localbdAppAllGroups?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppAllGroups?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppAllGroups?.deleteTable(nameTable) ?: -1
        if (n == 1) { setItems(null) }
        return n
    }

    // Метод для создания таблиц
    override fun createTable(nameTable0: String): Int {
        if (nameTable0.isEmpty()) { return -1 }
        val n = localbdAppAllGroups?.createTable(nameTable) ?: -1
        return n
    }
}