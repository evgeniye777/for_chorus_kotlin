package com.example.for_chour_kotlin.data_manager.connectionSQL

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DataBases(private val mContext: Context) : SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "person.db"
        private var DB_PATH = ""
        private const val DB_VERSION = 19
    }

    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false

    init {
        DB_PATH = mContext.applicationInfo.dataDir + "/databases/"
        copyDataBase()
        this.readableDatabase
    }

    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists()) {
                dbFile.delete()
            }
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            this.close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput: InputStream = mContext.assets.open(DB_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) {
            mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): SQLiteDatabase? {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDataBase
    }

    override fun close() {
        mDataBase?.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Здесь можно создать таблицы, если это необходимо
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            mNeedUpdate = true
        }
    }
}