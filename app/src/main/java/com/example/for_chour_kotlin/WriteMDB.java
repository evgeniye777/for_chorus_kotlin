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
        cursor.moveToLast();
        mas.add("Сегодня");
        while (!cursor.isBeforeFirst()) {
            mas.add(cursor.getString(2).toString());
            cursor.moveToPrevious();
        }
        return mas;
    }
    public List<infoOnePerson> readPersonMas() {
        List<infoOnePerson> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "persons", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = Integer.parseInt(cursor.getString(0));
            infoOnePerson infoPerson = new infoOnePerson(id,cursor.getString(5),0);
            infoPerson.setGender(Integer.parseInt(cursor.getString(6)));
            infoPerson.setAllowed(Integer.parseInt(cursor.getString(7)));
            if (infoPerson.getAllowed()==0||infoPerson.getAllowed()==1)  mas.add(infoPerson);
            cursor.moveToNext();
        }
        return mas;
    }

    public void writeSt(String date, int gender, List<infoOnePerson> list)
    {
       updateST(date,gender,list);
    }

    private ContentValues getContentValuesST(String date,int purpose,List<infoOnePerson> list) {
        ContentValues values = new ContentValues();
        values.put("version", "-1");
        values.put("date", date);
        values.put("purpose", purpose);
        for (infoOnePerson s: list) {
            if (s.getState()==1) {values.put("c"+s.getId(), "p");}
            else {
                    if (purpose == 0 || purpose == 3 && s.getAllowed() == 1 || purpose == s.getGender()) {
                        values.put("c" + s.getId(), "n");
                    } else {
                        values.put("c" + s.getId(), "d");
                    }
            }
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
