package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Product;
import com.AgungJmartAK.jmart_android.request.RequestFactory;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is class for product detail activity.
 *
 * @author Agung Firmansyah
 */
public class ProductDetailActivity extends AppCompatActivity {
    private TextView name,price,discount,condition,category,weight,shipment,store;
    Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        Product product = ProductFragment.getProductDetail();
        name = findViewById(R.id.productNameDetail);
        price = findViewById(R.id.productPriceDetail);
        discount = findViewById(R.id.productDiscountDetail);
        condition = findViewById(R.id.productConditionDetail);
        category = findViewById(R.id.productCategoryDetail);
        weight = findViewById(R.id.productWeightDetail);
        shipment = findViewById(R.id.productShipmentDetail);
        store = findViewById(R.id.productStoreDetail);
        buyButton = findViewById(R.id.prodBuy);

        buyButton.setOnClickListener(view -> startActivity(new Intent(ProductDetailActivity.this,CheckOutActivity.class)));

        name.setText(product.name);
        price.setText(String.valueOf(product.price));
        discount.setText(String.valueOf(product.discount));
        if(product.conditionUsed==false){
            condition.setText("New");
        }else{
            condition.setText("False");
        }
        category.setText(product.category.toString());
        weight.setText(String.valueOf(product.weight));
        switch (product.shipmentPlans){
            case (byte)(1<<0): shipment.setText("INSTANT"); break;
            case (byte)(1<<1): shipment.setText("SAME DAY"); break;
            case (byte)(1<<2): shipment.setText("NEXT DAY"); break;
            case (byte) (1<<3): shipment.setText("REGULER"); break;
            case (byte) (1<<4): shipment.setText("KARGO"); break;
            default: shipment.setText("shipment unknown");break;
        }

        Response.Listener<String> respList = response -> {
            try{
                JSONObject object = new JSONObject(response);
                if(object!=null){
                    JSONObject storeObject = object.getJSONObject("store");
                    store.setText(storeObject.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener errorListener = error -> Toast.makeText(ProductDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(RequestFactory.getById("account", product.accountId, respList, errorListener));

    }
}