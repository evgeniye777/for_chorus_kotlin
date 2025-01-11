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
    public void vivod(String str,Context context) {
        Toast.makeText(context,"Вы выбрали "
                        + str,
                Toast.LENGTH_SHORT).show();
    }
}
