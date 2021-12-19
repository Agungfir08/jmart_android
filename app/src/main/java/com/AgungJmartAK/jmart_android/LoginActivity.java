package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.request.LoginRequest;
import com.AgungJmartAK.jmart_android.request.RequestFactory;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is class for user login activity.
 *
 * @author Agung Firmansyah
 */
public class LoginActivity extends AppCompatActivity {
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(LoginActivity.this);
        int accountId = sessionManager.getSession();

        if(accountId!=-1){
            Response.Listener<String> listener = response -> {
                try{
                    JSONObject object = new JSONObject(response);
                    if(object!=null) {
                        loggedAccount = gson.fromJson(object.toString(),Account.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }catch (JSONException e){
                    Toast.makeText(LoginActivity.this, "login is failed.",Toast.LENGTH_SHORT).show();
                }
            };

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(RequestFactory.getById("account",accountId,listener,null));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText etPassword = findViewById(R.id.login_password);
        EditText etEmail = findViewById(R.id.login_email);
        Button loginBtn = findViewById(R.id.login_button);
        TextView reghere = findViewById(R.id.text_register_now);

        reghere.setOnClickListener(
                e->{
                    startActivity(new Intent(this,RegisterActivity.class));
                }
        );

        loginBtn.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            LoginRequest loginRequest = new LoginRequest(email, pass, response -> {
                try{
                    JSONObject object = new JSONObject(response);
                    if(object!=null) {
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                        loggedAccount = gson.fromJson(object.toString(),Account.class);
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.saveSession(loggedAccount);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }catch (JSONException e){
                    Toast.makeText(LoginActivity.this, "login failed.",Toast.LENGTH_SHORT).show();
                }

            },
                    error -> Toast.makeText(LoginActivity.this, "Error.",Toast.LENGTH_SHORT).show());
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);

        });

    }

}