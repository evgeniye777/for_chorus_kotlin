package com.example.for_chour_kotlin;

import android.content.Context;
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
    public  String postRequest(FragmentActivity content, Context context) {
        final String[] answer = {""};
        RequestQueue requestQueue = Volley.newRequestQueue(content);
        String url = "https://chelny-dieta.ru/phone.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //String p = response.substring(5);
                answer[0] = response;
vivod(response,context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer[0] = ""+error;
                vivod(error.toString(),context);
                }
        }){
            @Override
            protected Map<String,String> getParams(){
                //"INSERT INTO account (login,password,email,name,birthday) VALUES ('$login','$password','$email','$name','$birthday')"
                Map<String, String>  params = new HashMap<String, String>();
                params.put("WelcomeNewYear", "2025");
                return params;}
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;}};
        requestQueue.add(stringRequest);
        return answer[0];
    }
    private String generateRequestINSERT(String date, List<String> list) {
        String request="INSERT INTO app_st_persons_chorus63 (date,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32,c33,c34,c35,c36,c37,c38,c39,c40,c41,c42,c43,c44,c45,c46,c47,c48,c49,c50) VALUES (";
        return request;
    }
    public void vivod(String str,Context context) {
        Toast.makeText(context,"Вы выбрали "
                        + str,
                Toast.LENGTH_SHORT).show();
    }
}
