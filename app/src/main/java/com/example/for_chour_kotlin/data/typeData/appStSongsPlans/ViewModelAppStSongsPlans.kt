package com.example.for_chour_kotlin.data.typeData.appStSongsPlans

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppStSongsPlans() : ViewModel(), DataOperations<AppStSongsPlans> {

    var database: SQLiteDatabase? = null
    var localbdAppStSongsPlans: LocalBDAppStSongsPlans? = null
    var repositoryAppStSongsPlans: RepositoryAppStSongsPlans? = null

    private val _groups = MutableLiveData<List<AppStSongsPlans>?>()
    val groups: LiveData<List<AppStSongsPlans>?> get() = _groups

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
        localbdAppStSongsPlans = LocalBDAppStSongsPlans(database!!)
        repositoryAppStSongsPlans = RepositoryAppStSongsPlans(_groups)
        repositoryAppStSongsPlans?.setItems(localbdAppStSongsPlans!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppStSongsPlans): Int {
        val newId: Int = localbdAppStSongsPlans?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppStSongsPlans?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStSongsPlans): Int {
        val n: Int = localbdAppStSongsPlans?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppStSongsPlans?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppStSongsPlans): Int {
        val n: Int = localbdAppStSongsPlans?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStSongsPlans?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppStSongsPlans): Int {
        val n: Int = localbdAppStSongsPlans?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStSongsPlans?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppStSongsPlans?.deleteTable(nameTable) ?: -1
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
        val n = localbdAppStSongsPlans?.createTable(nameTable) ?: -1
        return n
    }
}