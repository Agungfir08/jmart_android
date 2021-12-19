package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.request.TopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * This class to inform user about their account
 *
 * @author Agung Firmansyah
 * */
public class AboutMeActivity extends AppCompatActivity {
    TextView name;
    TextView email;
    TextView balance;
    Button btnTopUp;
    Button btnStoreReg;
    Button accHist;
    Button logOutBtn, store;
    TextView tvCheckStore;
    EditText topUpBalance;
    SessionManager sessionManager;

    public static ArrayList<Integer> productIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Account account = LoginActivity.getLoggedAccount();
        sessionManager= new SessionManager(AboutMeActivity.this);

        name = findViewById(R.id.txName);
        email = findViewById(R.id.txEmail);
        balance = findViewById(R.id.txBalance);
        btnTopUp = findViewById(R.id.button5);
        btnStoreReg = findViewById(R.id.registBtn);
        tvCheckStore = findViewById(R.id.storeCheck);
        topUpBalance = findViewById(R.id.etTopUp);
        accHist = findViewById(R.id.accHistory);
        logOutBtn = findViewById(R.id.logOut);
        store = findViewById(R.id.btnstore);

        accHist.setOnClickListener(e->{
            startActivity(new Intent(AboutMeActivity.this, AccountHistoryActivity.class));
        });

        btnTopUp.setOnClickListener(e->{
            double balance_ = 0.0;

            balance_ = Double.valueOf(topUpBalance.getText().toString());

            Response.Listener<String> respList = response -> {
                boolean resp = Boolean.parseBoolean(response);
                if (resp){
                    Toast.makeText(AboutMeActivity.this, "Success",Toast.LENGTH_SHORT).show();
                    balance.setText(String.valueOf(account.balance));
                    Intent refresh = new Intent(this, AboutMeActivity.class);
                    startActivity(refresh);
                    this.finish();
                }else {
                    Toast.makeText(AboutMeActivity.this, "Failed",Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener errorListener = error -> {
                Toast.makeText(AboutMeActivity.this, "Error!",Toast.LENGTH_SHORT).show();
            };

            TopUpRequest request = new TopUpRequest(account.id, balance_,respList,errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(AboutMeActivity.this);
            requestQueue.add(request);
        });

        name.setText(account.name);
        email.setText(account.email);
        balance.setText(account.toString());

        btnStoreReg.setOnClickListener(e->{startActivity(new Intent(this, StoreRegActivity.class));});

        if(account.store!=null){
            tvCheckStore.setVisibility(View.GONE);
            btnStoreReg.setVisibility(View.GONE);
        }else {
            store.setVisibility(View.GONE);
        }

        logOutBtn.setOnClickListener(view -> {
            SessionManager sessionManager = new SessionManager(AboutMeActivity.this);
            sessionManager.removeSession();

            Intent intent = new Intent(AboutMeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        store.setOnClickListener(v -> {
            Intent storeactivity = new Intent(AboutMeActivity.this, AboutStoreActivity.class);
            startActivity(storeactivity);
        });
    }
}