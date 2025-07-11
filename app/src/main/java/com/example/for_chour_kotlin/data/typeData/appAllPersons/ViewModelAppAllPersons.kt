package com.example.for_chour_kotlin.data.typeData.appAllPersons

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppAllPersons() : ViewModel(), DataOperations<AppAllPersons> {

    var database: SQLiteDatabase? = null
    var localbdAppAllPersons: LocalBDAppAllPersons? = null
    var repositoryAppAllPersons: RepositoryAppAllPersons? = null

    private val _groups = MutableLiveData<List<AppAllPersons>?>()
    val groups: LiveData<List<AppAllPersons>?> get() = _groups

    lateinit var nameTable: String

    // Метод для подключения к таблице(создает при отстутствии)
    override fun connection(database0: SQLiteDatabase?, nameTable: String): Int {
        if (database0 != null) { database = database0 }
        this.nameTable = nameTable
        if (database == null || nameTable.isEmpty()) { return -1 }
        localbdAppAllPersons = LocalBDAppAllPersons(database!!)
        repositoryAppAllPersons = RepositoryAppAllPersons(_groups)
        repositoryAppAllPersons?.setItems(localbdAppAllPersons!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppAllPersons): Int {
        val newId: Int = localbdAppAllPersons?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppAllPersons?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppAllPersons): Int {
        val n: Int = localbdAppAllPersons?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppAllPersons?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppAllPersons): Int {
        val n: Int = localbdAppAllPersons?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppAllPersons?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppAllPersons): Int {
        val n: Int = localbdAppAllPersons?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppAllPersons?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppAllPersons?.deleteTable(nameTable) ?: -1
        if (n == 1) { setItems(null) }
        return n
    }

    // Метод для создания таблиц
    override fun createTable(nameTable0: String): Int {
        if (nameTable0.isEmpty()) { return -1 }
        val n = localbdAppAllPersons?.createTable(nameTable) ?: -1
        return n
    }
}