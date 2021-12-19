package com.AgungJmartAK.jmart_android.request;

import com.AgungJmartAK.jmart_android.model.ProductCategory;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

public class RequestFactory
{
    private static final String URL_FORMAT_ID = "http://10.0.2.2:7447/%s/%d";
    private static final String URL_FORMAT_PAGE = "http://10.0.2.2:7447/%s/page?page=%s&pageSize=%s";
    private static final String URL_PRODUCT_FILTERED = "http://10.0.2.2:7447/product/getFiltered?page=%s&pageSize=%s&accountId=%s&search=%s&minPrice=%s&maxPrice=%s&category=%s";
    private static final String URL_PRODUCT_BY_STORE = "http://10.0.2.2:7447/product/%d/store?page=%s&pageSize=%s";

    public static StringRequest getProductByStore (int id, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = String.format(URL_PRODUCT_BY_STORE,id,page,pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public static StringRequest getById
            (
                    String parentURI,
                    int id,
                    Response.Listener<String> listener,
                    Response.ErrorListener errorListener
            )
    {
        String url = String.format(URL_FORMAT_ID, parentURI, id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
    public static StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public static StringRequest getProductFiltered(int page, int pageSize, int accountId, String search, double minPrice, double maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = String.format(URL_PRODUCT_FILTERED,page,pageSize,accountId,search,minPrice,maxPrice,category);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

}
