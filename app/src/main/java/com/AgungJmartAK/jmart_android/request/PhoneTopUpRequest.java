package com.AgungJmartAK.jmart_android.request;

import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class PhoneTopUpRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7447/phoneTopUp/topUpbyPhone";
    private final Map<String ,String> params;

    public PhoneTopUpRequest(int buyerId, int productId, String phoneNumber,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener){
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("phoneNumber", phoneNumber);
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
