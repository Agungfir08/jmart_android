package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.request.StoreRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is class for store registration
 * @author Agung Firmansyah
 */
public class StoreRegActivity extends AppCompatActivity {

    EditText storeName;
    EditText storeAddress;
    EditText phoneNumber;
    Button registButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_reg);

        Account account = LoginActivity.getLoggedAccount();

        storeName = findViewById(R.id.storeName);
        storeAddress = findViewById(R.id.storeAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        registButton = findViewById(R.id.button9);

        registButton.setOnClickListener( o->{
            int id = account.id;
            String name = storeName.getText().toString();
            String phone = phoneNumber.getText().toString();
            String address = storeAddress.getText().toString();

            Response.ErrorListener errorListener = volleyError->{
                Toast.makeText(getApplicationContext(), "Something error.", Toast.LENGTH_SHORT).show();
            };

            Response.Listener<String> respList = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject!=null){
                        Toast.makeText(StoreRegActivity.this, "Register Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StoreRegActivity.this, AboutMeActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(StoreRegActivity.this, "Register Failed",Toast.LENGTH_SHORT).show();
                }
            };

            StoreRequest request = new StoreRequest(id,name,address,phone,respList,errorListener);
            RequestQueue queue = Volley.newRequestQueue(StoreRegActivity.this);
            queue.add(request);
        });

    }
}