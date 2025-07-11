package com.example.for_chour_kotlin.data.typeData.appDataParticipant
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModelAppDataParticipant() : ViewModel(), DataOperations<AppDataParticipant> {

    var database: SQLiteDatabase? = null
    var localbdAppDataParticipant: LocalBDAppDataParticipant? = null
    var repositoryAppDataParticipant: RepositoryAppDataParticipant? = null

    private val _groups = MutableLiveData<List<AppDataParticipant>?>()
    val groups: LiveData<List<AppDataParticipant>?> get() = _groups

    lateinit var nameTable: String

    // Метод для подключения к таблице(создает при отстутствии)
    override fun connection(database0: SQLiteDatabase?, nameTable: String): Int {
        if (database0 != null) { database = database0 }
        this.nameTable = nameTable
        if (database == null || nameTable.isEmpty()) { return -1 }
        localbdAppDataParticipant = LocalBDAppDataParticipant(database!!)
        repositoryAppDataParticipant = RepositoryAppDataParticipant(_groups)
        repositoryAppDataParticipant?.setItems(localbdAppDataParticipant!!.readItems(nameTable))
        return 1
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppDataParticipant): Int {
        val newId: Int = localbdAppDataParticipant?.addItem(item) ?: -1
        if (newId >= 0) {
            repositoryAppDataParticipant?.addItem(item.copy(id = newId))
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppDataParticipant): Int {
        val n: Int = localbdAppDataParticipant?.updateItem(item) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppDataParticipant?.updateItem(item)
        }
        return n
    }

    // Метод для неполного удаления данных из таблиц
    override fun deleteItem(item: AppDataParticipant): Int {
        val n: Int = localbdAppDataParticipant?.deleteItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppDataParticipant?.deleteItem(item)
        }
        return n
    }

    // Метод для полного удаления данных из таблиц
    override fun destroyItem(item: AppDataParticipant): Int {
        val n: Int = localbdAppDataParticipant?.destroyItem(item) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppDataParticipant?.deleteItem(item)
        }
        return n
    }

    // Метод для удаления таблиц
    override fun deleteTable(nameTable0: String): Int {
        val nameTable = nameTable0.ifEmpty { this.nameTable }
        val n = localbdAppDataParticipant?.deleteTable(nameTable) ?: -1
        if (n == 1) { setItems(null) }
        return n
    }

    // Метод для создания таблиц
    override fun createTable(nameTable0: String): Int {
        if (nameTable0.isEmpty()) { return -1 }
        val n = localbdAppDataParticipant?.createTable(nameTable) ?: -1
        return n
    }
}