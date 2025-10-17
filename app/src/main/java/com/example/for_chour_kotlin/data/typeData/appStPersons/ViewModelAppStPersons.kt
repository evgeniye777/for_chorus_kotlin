package com.example.for_chour_kotlin.data.typeData.appStPersons

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant

class ViewModelAppStPersons() : ViewModel(), DataOperations<AppStPersons> {

    var database: SQLiteDatabase? = null
    var localbdAppStPersons: LocalBDAppStPersons? = null
    var repositoryAppStPersons: RepositoryAppStPersons? = null

    private val _groups = MutableLiveData<List<AppStPersons>?>()
    val stPersons: LiveData<List<AppStPersons>?> get() = _groups

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
        localbdAppStPersons = LocalBDAppStPersons(database!!)
        repositoryAppStPersons = RepositoryAppStPersons(_groups)
        repositoryAppStPersons?.setItems(localbdAppStPersons!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppStPersons): Int {
        val newId: Int = localbdAppStPersons?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppStPersons?.addItem(item.copy(id = newId))
            AuthorizationState.dataAuthorization+=""+newId+"; "
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStPersons): Int {
        val n: Int = localbdAppStPersons?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppStPersons?.updateItem(item)
        }
        return n
    }
    override fun addOrUpdateItem(item: AppStPersons): Int {
        val currentList = _groups.value ?: emptyList()
        val existingItem = currentList.find { it.id == item.id }
        return if (existingItem != null) {
            updateItem(item)
        } else {
            addItem(item)
        }
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppStPersons): Int {
        val n: Int = localbdAppStPersons?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersons?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppStPersons): Int {
        val n: Int = localbdAppStPersons?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppStPersons?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppStPersons?.deleteTable(nameTable) ?: -1
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
        val n = localbdAppStPersons?.createTable(nameTable) ?: -1
        return n
    }
}