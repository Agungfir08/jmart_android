package com.AgungJmartAK.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AgungJmartAK.jmart_android.request.RegisterRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is class for user register activity.
 *
 * @author Agung Firmansyah
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText etName = findViewById(R.id.name_register_layout);
        EditText etPassword = findViewById(R.id.password_register_layout);
        EditText etEmail = findViewById(R.id.email_register_layout);
        Button registerBtn = findViewById(R.id.register_button);

        registerBtn.setOnClickListener(o->{
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            Response.Listener<String> respList = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject!=null){
                        Toast.makeText(RegisterActivity.this, "Register Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Register Failed",Toast.LENGTH_SHORT).show();

                }
            };

            Response.ErrorListener errorListener = error -> Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();

            RegisterRequest registerRequest = new RegisterRequest(name, email, pass, respList, errorListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        });

    }
}