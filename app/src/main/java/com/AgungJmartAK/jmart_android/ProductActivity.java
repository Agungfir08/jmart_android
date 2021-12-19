package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Product;
import com.AgungJmartAK.jmart_android.model.ProductCategory;
import com.AgungJmartAK.jmart_android.request.RequestFactory;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * This class for showing product after the filter applied.
 *
 * @author Agung Firmansyah
 */
public class ProductActivity extends AppCompatActivity {

    private static final Gson gson = new Gson();
    private static ArrayList<Product> productsList = new ArrayList<>();
    ListView listProd;
    EditText etPage;
    Button prevBtn, nextBtn, goBtn;
    final int pageSize = 10;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double lowestPrice = intent.getDoubleExtra("lowestPrice",0.0);
        double highestPrice = intent.getDoubleExtra("highestPrice", 0.0);
        ProductCategory category = (ProductCategory) intent.getSerializableExtra("category");
        Boolean condition = intent.getBooleanExtra("condition",false);

        setContentView(R.layout.activity_product);
        listProd = findViewById(R.id.listview);
        etPage = findViewById(R.id.pageFiltered);
        prevBtn = findViewById(R.id.preevButton);
        nextBtn = findViewById(R.id.neextButton);
        goBtn = findViewById(R.id.gooBtn);

        listProd.setOnItemClickListener((adapterView, view, i, l) -> {
            ProductFragment.selectedProduct = productsList.get(i);
            Intent intent12 = new Intent(ProductActivity.this,ProductDetailActivity.class);
            startActivity(intent12);
        });

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    if (object != null) {
                        productsList = gson.fromJson(object.toString(), new TypeToken<ArrayList<Product>>() {}.getType());

                        ArrayAdapter<Product> listViewAdapter = new ArrayAdapter<Product>(getApplicationContext(), android.R.layout.simple_list_item_1, productsList);
                        if(!productsList.isEmpty())
                            listProd.setAdapter(listViewAdapter);
                        else
                        if(page>0)
                            page--;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = error -> Toast.makeText(ProductActivity.this, "Error!", Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
        requestQueue.add(RequestFactory.getProductFiltered(page,pageSize,-1, name,lowestPrice, highestPrice,category,listener,errorListener
        ));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
                requestQueue.add(RequestFactory.getProductFiltered(page,pageSize,-1, name,lowestPrice, highestPrice,category,listener,errorListener));
            }
        });

        prevBtn.setOnClickListener(view -> {
            if(page>0){
                page--;
                RequestQueue requestQueue1 = Volley.newRequestQueue(ProductActivity.this);
                requestQueue1.add(RequestFactory.getProductFiltered(page,pageSize,-1, name,lowestPrice, highestPrice,category,listener,errorListener));
            }
        });

        Response.Listener<String> secondListener = response -> {
            try {
                JSONArray object = new JSONArray(response);
                if (object != null) {
                    productsList = gson.fromJson(object.toString(), new TypeToken<ArrayList<Product>>() {}.getType());

                    ArrayAdapter<Product> listViewAdapter = new ArrayAdapter<Product>(getApplicationContext(), android.R.layout.simple_list_item_1, productsList);
                    if(!productsList.isEmpty())
                        listProd.setAdapter(listViewAdapter);
                    else{
                        Toast.makeText(ProductActivity.this, "No data in this page.",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(ProductActivity.this,ProductActivity.class);
                        startActivity(intent1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };

        goBtn.setOnClickListener(view -> {
            try{
                page = Integer.parseInt(etPage.getText().toString());
                if(page>=1){
                    RequestQueue requestQueue12 = Volley.newRequestQueue(ProductActivity.this);
                    requestQueue12.add(RequestFactory.getProductFiltered(page-1,pageSize,-1, name,lowestPrice, highestPrice,category,secondListener,errorListener));
                }else{
                    Toast.makeText(ProductActivity.this, "page start with 1.",Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(ProductActivity.this, "failed to parse.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}