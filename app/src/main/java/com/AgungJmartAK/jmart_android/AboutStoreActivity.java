package com.AgungJmartAK.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.model.Product;
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
 * This class to inform user about their store account
 *
 * @author Agung Firmansyah
 * */
public class AboutStoreActivity extends AppCompatActivity {
    TextView strname, stradd, strphone;
    Button history, phoneTopUp;

    private static final Gson gson = new Gson();

    public static ArrayList<Product> productList = new ArrayList<>();

    public static ArrayList<Integer> productIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_store);
        Account account = LoginActivity.getLoggedAccount();

        strname = findViewById(R.id.name_store);
        stradd = findViewById(R.id.address_store);
        strphone = findViewById(R.id.phonenumber_store);
        history = findViewById(R.id.btnhistorystore);
        phoneTopUp = findViewById(R.id.btnPhonetopup);

        strname.setText(account.store.name);
        stradd.setText(account.store.address);
        strphone.setText(account.store.phoneNumber);

        history.setOnClickListener(e->{
            Response.Listener<String> respListProduct = response ->{
                try {
                    JSONArray array = new JSONArray(response);
                    productList = gson.fromJson(array.toString(), new TypeToken<ArrayList<Product>>() {}.getType());
                    if(!productList.isEmpty()){
                        for (int i=0;i<productList.size();i++){
                            productIdList.add(productList.get(i).id);
                        }
                        startActivity(new Intent(AboutStoreActivity.this,StoreHistoryActivity.class));
                    }else{
                        Toast.makeText(AboutStoreActivity.this, "No Product.",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException v) {
                    Toast.makeText(AboutStoreActivity.this, "Failed",Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorListener = error -> {
                Toast.makeText(AboutStoreActivity.this, "Error!",Toast.LENGTH_SHORT).show();
            };
            RequestQueue queue = Volley.newRequestQueue(AboutStoreActivity.this);
            queue.add(RequestFactory.getProductByStore(account.id, 0, 100, respListProduct, errorListener
            ));
        });

        phoneTopUp.setOnClickListener(view -> {
            Intent intent = new Intent(AboutStoreActivity.this,PhoneTopUpActivity.class);
            startActivity(intent);
        });
    }
}