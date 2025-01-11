package com.example.for_chour_kotlin;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteMDB {
    private DataBases basa;
    static private SQLiteDatabase mdb;
   public WriteMDB(FragmentActivity content) {
        basa = new DataBases(content);//в первой БД хранятся данные которые я буду впоследствии обновлять, меняя версию БД
        try {basa.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");}
        try {mdb = basa.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;}
    }
    public List<String> writeSpinerMas() {
        List<String> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "st", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mas.add(cursor.getString(1).toString());
            cursor.moveToNext();
        }
        return mas;
    }
    public List<String> writePersonMas() {
        List<String> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "persons", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mas.add(cursor.getString(1));
            cursor.moveToNext();
        }
        return mas;
    }
}
