package com.AgungJmartAK.jmart_android.request;

import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentRequest extends StringRequest {
    private static final String URL_PAYMENT_REQUEST = "http://10.0.2.2:7447/payment/create";
    private static final String URL_PAYMENT_BY_USER = "http://10.0.2.2:7447/payment/byAccount?buyerId=%s";
    private static final String URL_CANCEL_PAYMENT = "http://10.0.2.2:7447/payment/%d/cancel";
    private static final String URL_ACCEPT_PAYMENT = "http://10.0.2.2:7447/payment/%d/accept";
    private static final String URL_PAYMENT_BY_ID = "http://10.0.2.2:7447/payment/byStore?";
    private static final String PRODUCT_ID = "productId=%d";
    private static final String URL_SUBMIT_PAYMENT = "http://10.0.2.2:7447/payment/%d/submit?receipt=%s";

    private final Map<String ,String> params;

    public PaymentRequest(int buyerId, int productId,int productCount,String shipmentAddress,byte shipmentPlan, Response.Listener<String> listener,
                          Response.ErrorListener errorListener)
    {
        super(Request.Method.POST, URL_PAYMENT_REQUEST, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public static StringRequest getPaymentByUser(int userId,Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_PAYMENT_BY_USER,userId);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public static StringRequest cancelPayment(int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_CANCEL_PAYMENT,id);
        return new StringRequest(Method.POST, url, listener, errorListener);
    }
    public static StringRequest acceptPayment(int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_ACCEPT_PAYMENT,id);
        return new StringRequest(Method.POST, url, listener, errorListener);
    }

    public static StringRequest submitPayment(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_SUBMIT_PAYMENT,id,receipt);
        return new StringRequest(Method.POST, url, listener, errorListener);
    }

    public static StringRequest getPaymentByProduct
            (
                    ArrayList<Integer> productId,
                    Response.Listener<String> listener,
                    Response.ErrorListener errorListener
            ){
        String url_id = String.format(PRODUCT_ID,productId.get(0));
        if(productId.size()>1){
            for(int i=1;i< productId.size();i++){
                url_id += "&" + String.format(PRODUCT_ID,productId.get(i));
            }
        }
        return new StringRequest(Method.GET,URL_PAYMENT_BY_ID+url_id,listener,errorListener);
    }
}
