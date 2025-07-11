package com.example.for_chour_kotlin.data.typeData.appStSongsHistory

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppStSongsHistory() : ViewModel(), DataOperations<AppStSongsHistory> {

    var database: SQLiteDatabase? = null
    var localbdAppStSongsHistory: LocalBDAppStSongsHistory? = null
    var repositoryAppStSongsHistory: RepositoryAppStSongsHistory? = null

    private val _groups = MutableLiveData<List<AppStSongsHistory>?>()
    val groups: LiveData<List<AppStSongsHistory>?> get() = _groups

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
        localbdAppStSongsHistory = LocalBDAppStSongsHistory(database!!)
        repositoryAppStSongsHistory = RepositoryAppStSongsHistory(_groups)
        repositoryAppStSongsHistory?.setItems(localbdAppStSongsHistory!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppStSongsHistory): Int {
        val newId: Int = localbdAppStSongsHistory?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppStSongsHistory?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStSongsHistory): Int {
        val n: Int = localbdAppStSongsHistory?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppStSongsHistory?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppStSongsHistory): Int {
        val n: Int = localbdAppStSongsHistory?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStSongsHistory?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppStSongsHistory): Int {
        val n: Int = localbdAppStSongsHistory?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStSongsHistory?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppStSongsHistory?.deleteTable(nameTable) ?: -1
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
        val n = localbdAppStSongsHistory?.createTable(nameTable) ?: -1
        return n
    }
}