package com.AgungJmartAK.jmart_android.request;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class StoreRequest extends StringRequest{
    private static final String URL = "http://10.0.2.2:7447/account/%d/registerStore";
    private final Map<String ,String> params;

    public StoreRequest(int id, String name, String address, String phoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, String.format(URL, id, name, address, phoneNumber), listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("address", address);
        params.put("phoneNumber", phoneNumber);
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

