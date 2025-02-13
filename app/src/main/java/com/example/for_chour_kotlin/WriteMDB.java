package com.example.for_chour_kotlin;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.for_chour_kotlin.PersonsInfo.infoOnePerson;
import com.example.for_chour_kotlin.PersonsInfo.infoOneRec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteMDB {
    private DataBases basa;
    static private SQLiteDatabase mdb;
    private List<String> listSpinner;
    private String request="";
    private boolean iStoday = false;
    FragmentActivity activity;
    Context context;
   public WriteMDB(FragmentActivity content, Context context0) {
       activity = content;
       context = context0;
        basa = new DataBases(content);//в первой БД хранятся данные которые я буду впоследствии обновлять, меняя версию БД
        try {basa.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");}
        try {mdb = basa.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;}
    }
    public List<infoOneRec> readOneRecMas(String toDay) {
        iStoday = false;
        List<infoOneRec> listinfoRec = new ArrayList<>();
        listSpinner = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "st", null);
        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            infoOneRec infoRec;
            int version = Integer.parseInt(cursor.getString(1));
            String date = cursor.getString(2);
            if (date.equals(toDay)) {listSpinner.add("Сегодня"); iStoday = true;}
            else {listSpinner.add(date);}
            int purpose = Integer.parseInt(cursor.getString(3));
            String c1 = cursor.getString(6);
            infoRec = new infoOneRec(version,date,purpose);
            List<String> listRec = new ArrayList<>();
            int n=6;
            while (n<56) {
                String cur = cursor.getString(n);
                if (cur==null) {cur="";}
                listRec.add(cur);
                n++;
            }
            infoRec.addToList(listRec);
            listinfoRec.add(infoRec);
            cursor.moveToPrevious();
        }
        return listinfoRec;
    }
    public List<String> getSpinnerDay() {
       if (!iStoday) {listSpinner.add(0,"Сегодня");}
       if (listSpinner!=null) {return listSpinner;}
       else {return new ArrayList<>();}
    }
    public List<infoOnePerson> readPersonMas() {
        List<infoOnePerson> mas = new ArrayList<>();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "persons"+" ORDER BY "+"name", null);
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

    public void writeSt(String date, int purpose, List<infoOnePerson> list)
    {
        ContentValues values = getContentValuesST(date,purpose,list);
        vivod(""+mdb.insert("st",null,values));
    }

    public void overWriteSt(String date, int purpose, List<String> list)
    {
        ContentValues values = getContentValuesSTover(date,purpose,list);
        vivod(""+mdb.update("st",values,"date=?",new String[] {date}));
    }

    public void muDelo()
    {
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT date FROM " + "st", null);
        cursor.moveToFirst();
        String str="";
        /*while (!cursor.isAfterLast()) {
            str = cursor.getString(0);
            String strNew = str.substring(4)+"."+str.substring(2,4)+"."+str.substring(0,2);
            ContentValues values = new ContentValues();
            values.put("date", strNew);
            mdb.update("st",values,"date=?",new String[] {str});
            cursor.moveToNext();
        }*/
        mdb.delete("st","version=?",new String[] {"0"});
        cursor.close();
        vivod(str);
    }

    private ContentValues getContentValuesST(String date,int purpose,List<infoOnePerson> list) {
        request=date+",";
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

    private ContentValues getContentValuesSTover(String date,int purpose,List<String> list) {
        ContentValues values = new ContentValues();
        values.put("version", "-1");
        values.put("date", date);
        values.put("purpose", purpose);
        int n=1;
        for (String s: list) {
            values.put("c" + n, s);
            n++;
        }
        vivod(""+values.size());
        return values;
    }
    public String toSet() {
       StringBuilder request= new StringBuilder();
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT * FROM " + "st", null);
        cursor.moveToFirst();
        int count = cursor.getColumnCount();
        while (!cursor.isAfterLast()) {
            for (int i=1;i<count;i++) {
                String data = cursor.getString(i);
                if (data!=null&&!data.isEmpty()) {
                    request.append(cursor.getColumnName(i)).append(":").append(cursor.getString(i)).append(",");
                }
            }
            if (request.length()>0) {
                request.deleteCharAt(request.length()-1);
                request.append(";");
            }
            cursor.moveToNext();
        }
        cursor.close();
        ServerClass serverClass = new ServerClass();
        serverClass.getRequestINSERT("WriteAll", request.toString());
        return serverClass.postRequest(activity,context);
    }


    public String Update() {
       return "";
    }

    public void Del(String date) {
        mdb.delete("st","date=?",new String[] {date});
        vivod("Запись удалена");
    }

    public void vivod(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    public void vivodMes(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("info").setMessage(text);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
