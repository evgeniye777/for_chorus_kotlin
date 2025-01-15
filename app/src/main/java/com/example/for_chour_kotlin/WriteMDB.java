package com.example.for_chour_kotlin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.for_chour_kotlin.PersonsInfo.infoOnePerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteMDB {
    private DataBases basa;
    static private SQLiteDatabase mdb;
    FragmentActivity activity;
   public WriteMDB(FragmentActivity content) {
       activity = content;
        basa = new DataBases(content);//в первой БД хранятся данные которые я буду впоследствии обновлять, меняя версию БД
        try {basa.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");}
        try {mdb = basa.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;}
    }
    public List<String> readSpinerMas(String toDay) {
        List<String> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "st", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mas.add(cursor.getString(2).toString());
            cursor.moveToNext();
        }
        return mas;
    }
    public List<infoOnePerson> readPersonMas() {
        List<infoOnePerson> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "persons", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            infoOnePerson infoPerson = new infoOnePerson(cursor.getString(5),0);
            infoPerson.setGender(Integer.parseInt(cursor.getString(6)));
            mas.add(infoPerson);
            cursor.moveToNext();
        }
        return mas;
    }

    public void writeSt(String date, int gender, List<infoOnePerson> list)
    {
       updateST(date,gender,list);
    }

    private ContentValues getContentValuesST(String date,int gender,List<infoOnePerson> list) {
        ContentValues values = new ContentValues();
        values.put("", date);
        values.put("version", "-1");
        values.put("date", date);
        values.put("gender", gender);
        int n = 1;
        for (infoOnePerson s: list) {
            if (s.getState()==1) {values.put("c"+n, "p");}
            else {
                if (gender==0||gender==s.getGender()) {values.put("c"+n, "n");}
                else {values.put("c"+n, "d");}
            }
            n++;
        }
        while(n<=50) {
            values.put("c"+n, "");
            n++;
        }
        vivod(""+values.size());
        return values;
    }

    public void updateST(String date,int gender,List<infoOnePerson> list) {
        ContentValues values = getContentValuesST(date,gender,list);
        vivod(""+mdb.insert("st",null,values));
    }

    public void vivod(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }
}
