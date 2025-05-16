package com.example.for_chour_kotlin.domain;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.for_chour_kotlin.data.source.remote.ServerClass;
import com.example.for_chour_kotlin.data.source.local.DataBases;
import com.example.for_chour_kotlin.domain.entities.infoOnePerson;
import com.example.for_chour_kotlin.domain.entities.infoOneRec;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        List<infoOneRec> listinfoRec = new ArrayList<>();
        listSpinner = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mdb.rawQuery("SELECT * FROM app_st_persons_chorus63_sinch", null);
            if (cursor != null && cursor.moveToLast()) {
                // Получаем индексы столбцов по их названиям
                int dateWriteIndex = cursor.getColumnIndex("date_write");
                int dateIndex = cursor.getColumnIndex("date");
                int purposeIndex = cursor.getColumnIndex("purpose");

                do {
                    String date_write = cursor.getString(dateWriteIndex);
                    String date = cursor.getString(dateIndex);
                    if (date.equals(toDay)) {
                        listSpinner.add("Сегодня");
                    } else {
                        listSpinner.add(date);
                    }

                    int purpose = 0;
                    try {
                        purpose = Integer.parseInt(cursor.getString(purposeIndex));
                    } catch (NumberFormatException e) {
                        Log.e("ReadOneRecMas", "Ошибка при парсинге purpose: " + e.getMessage());
                    }

                    infoOneRec infoRec = new infoOneRec(date_write, date, purpose);
                    List<String> listRec = new ArrayList<>();

                    for (int n = 7; n < 82; n++) {
                        String cur = cursor.getString(n);
                        listRec.add(cur != null ? cur : "");
                    }

                    infoRec.addToList(listRec);
                    listinfoRec.add(infoRec);
                } while (cursor.moveToPrevious());
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Закрываем курсор
            }
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
        cursor = mdb.rawQuery("SELECT * FROM " + "app_group_chorus63"+" ORDER BY "+"p_name", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = Integer.parseInt(cursor.getString(0));
            infoOnePerson infoPerson = new infoOnePerson(id,cursor.getString(4),0);
            infoPerson.setGender(Integer.parseInt(cursor.getString(5)));
            infoPerson.setAllowed(Integer.parseInt(cursor.getString(7)));
            infoPerson.setVisible(Integer.parseInt(cursor.getString(9)));
            if (infoPerson.getVisible()==1)  mas.add(infoPerson);
            cursor.moveToNext();
        }
        return mas;
    }

    public void writeSt(String date, int purpose, List<infoOnePerson> list)
    {
        ContentValues values = getContentValuesST(date,purpose,list);
        vivod(""+mdb.insert("app_st_persons_chorus63_sinch",null,values));
    }

    public void overWriteSt(String date, int purpose, List<String> list)
    {
        ContentValues values = getContentValuesSTover(date,purpose,list);
        vivod(""+mdb.update("app_st_persons_chorus63_sinch",values,"date=?",new String[] {date}));
    }

    public void muDelo()
    {
        mdb.execSQL("DELETE FROM " + "app_st_persons_chorus63_sinch");
        mdb.close();
    }

    private ContentValues getContentValuesST(String date,int purpose,List<infoOnePerson> list) {
        request=date+",";
        ContentValues values = new ContentValues();

        //надо изменить
        values.put("committer", "Гузенко Евгений");

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        values.put("date_write", formattedDateTime);

        values.put("date", date);
        values.put("purpose", purpose);
        for (infoOnePerson s: list) {
            if (s.getState()==1) {values.put("c"+s.getId(), "p");}
            else {
                    if ((s.getAllowed() ==0|| s.getAllowed() == 1)&&(purpose == 0 || purpose == 3 && s.getAllowed() == 1 || purpose == s.getGender())) {
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

        values.put("committer", "Гузенко Евгений");

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        values.put("date_write", formattedDateTime);

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
        cursor = mdb.rawQuery("SELECT * FROM " + "app_st_persons_chorus63_sinch", null);
        cursor.moveToFirst();
        int count = cursor.getColumnCount();
        while (!cursor.isAfterLast()) {
            for (int i=1;i<count;i++) {
                String data = cursor.getString(i);
                if (data!=null&&!data.isEmpty()) {
                    request.append(cursor.getColumnName(i)).append("¦").append(cursor.getString(i)).append("¦¦");
                }
            }
            if (request.length()>0) {
                int lastIndex = request.lastIndexOf("¦¦");
                if (lastIndex!=-1) {request.delete(lastIndex, lastIndex + 2);}
                request.append("¦¦¦");
            }
            cursor.moveToNext();
        }
        cursor.close();
        ServerClass serverClass = new ServerClass();
        vivodMes(request.toString());
        serverClass.getRequestINSERT("WriteAll", request.toString());
        return serverClass.postRequest(activity,context,null);
    }


    public String Update() {
        ServerClass serverClass = new ServerClass();
        serverClass.getRequestINSERT("UpdateAll", request.toString());
        String answer = serverClass.postRequest(activity, context, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                int pEnd = result.lastIndexOf("\"");
                result = result.substring(1,pEnd);
                vivodMes(result.replace(";",";\n\n\n"));
                String[] mas = result.split(";");
                ContentValues values;
                int n_date=1;
                String message="error: "; int i=0;
                for (String str: mas) {
                    values = new ContentValues();
                    String[] masCell = str.split(",");
                    for (String cell:masCell) {
                        String[] masDate = cell.split(":");
                        try {
                            values.put(masDate[0],masDate[1]);
                        }catch (Exception e) {message+=""+i+". "+masDate+"; ";}
                    }
                    message+="\nРезультат: "+mdb.insert("app_st_persons_chorus63_sinch",null,values)+"\n";
                    i++;
                }
                vivodMes(message);
            }

            @Override
            public void onError(String error) {

            }
        });

       return "";
    }

    public void Del(String date) {
        mdb.delete("app_st_persons_chorus63_sinch","date=?",new String[] {date});
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
    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(String error);
    }
}
