package com.AgungJmartAK.jmart_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.request.PhoneTopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is activity to top up mobile phone credits.
 *
 * @author Agung Firmansyah
 */
public class PhoneTopUpActivity extends AppCompatActivity {
    EditText phoneNumber;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button topUpBtn;
    Account account = LoginActivity.getLoggedAccount();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_top_up);
        phoneNumber = findViewById(R.id.phoneNumberTopUp);
        radioGroup = findViewById(R.id.radioGroupPhone);
        topUpBtn = findViewById(R.id.phoneTABtn);

        topUpBtn.setOnClickListener(view -> {
            String number = phoneNumber.getText().toString();
            int selectedCondition = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedCondition);
            int productId = -1;

            switch (radioButton.getText().toString()){
                case "Rp.50.000 (Pay: 52.000)" :
                    productId = 31;
                    break;
                case "Rp. 100.000 (Pay: 102.000)" :
                    productId = 32;
                    break;
                case "Rp.200.000 (Pay: 202.000)" :
                    productId = 33;
                    break;
                case "Rp. 300.000 (Pay: 302.000)" :
                    productId = 34;
                    break;
            }

            Response.Listener<String> listener = response -> {
                try{
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equalsIgnoreCase("FINISHED"))
                        Toast.makeText(PhoneTopUpActivity.this, "Success",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(PhoneTopUpActivity.this, "Failed",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(PhoneTopUpActivity.this, "Error!",Toast.LENGTH_SHORT).show();
                }
            };

            PhoneTopUpRequest request = new PhoneTopUpRequest(account.id,productId,number,listener,null);
            RequestQueue queue = Volley.newRequestQueue(PhoneTopUpActivity.this);
            queue.add(request);
        });
    }
}