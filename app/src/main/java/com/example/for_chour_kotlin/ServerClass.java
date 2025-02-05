package com.example.for_chour_kotlin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerClass {
    String name="",request="";
    public  String postRequest(FragmentActivity content, Context context) {
        final String[] answer = {""};
        RequestQueue requestQueue = Volley.newRequestQueue(content);
        String url = "https://chelny-dieta.ru/server.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //String p = response.substring(5);
                answer[0] = response;
                vivodMes(response,context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer[0] = ""+error;
                vivodMes(error.toString(),context);
                }
        }){
            @Override
            protected Map<String,String> getParams(){
                //"INSERT INTO account (login,password,email,name,birthday) VALUES ('$login','$password','$email','$name','$birthday')"
                Map<String, String>  params = new HashMap<String, String>();
                params.put(name, request);
                return params;}
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;}};
        requestQueue.add(stringRequest);
        return answer[0];
    }
    public void getRequestINSERT(String name0, String request0) {
        name = name0;
        request=request0;
    }
    public void vivod(String str,Context context) {
        Toast.makeText(context,"Вы выбрали "
                        + str,
                Toast.LENGTH_SHORT).show();
    }

    public void vivodMes(String text,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("info").setMessage(text).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });;
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
