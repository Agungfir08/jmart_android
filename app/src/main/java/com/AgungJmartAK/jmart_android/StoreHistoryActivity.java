package com.AgungJmartAK.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.model.Payment;
import com.AgungJmartAK.jmart_android.request.PaymentRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * This class to inform user about their
 * store order history
 *
 * @author Agung Firmansyah
 */
public class StoreHistoryActivity extends AppCompatActivity {
    EditText cancelId, submitId, submitReceipt, acceptId;
    Button cancelBtn, submitBtn, acceptBtn;
    TextView mainContent;

    private static final Gson gson = new Gson();

    private static ArrayList<Payment> paymentList = new ArrayList<>();

    private static String status = "";

    private static String productId = "";
    Account account = LoginActivity.getLoggedAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_history);
        cancelId = findViewById(R.id.etCancelByStore);
        submitId = findViewById(R.id.etSubmitIdStore);
        submitReceipt = findViewById(R.id.etSubmitReceiptStore);
        acceptId = findViewById(R.id.etAcceptByStore);
        cancelBtn = findViewById(R.id.btnCancelByStore);
        submitBtn = findViewById(R.id.btnSubmitByStore);
        acceptBtn = findViewById(R.id.btnAcceptByStore);
        mainContent = findViewById(R.id.storeHist);



        Response.ErrorListener errorListener = error -> {
            Toast.makeText(StoreHistoryActivity.this, "System error.",Toast.LENGTH_SHORT).show();
        };

        Response.Listener<String> respListPayment = response -> {
            try {
                JSONArray array = new JSONArray(response);
                paymentList = gson.fromJson(array.toString(), new TypeToken<ArrayList<Payment>>() {
                }.getType());
                if(paymentList.size()>0){
                    for (int i=paymentList.size()-1;i>=0;i--){
                        status += "\n\nPayment Id: "+String.valueOf(paymentList.get(i).id)+"\nProduct Id: "+String.valueOf(paymentList.get(i).productId)+"\nProduct count: "+String.valueOf(paymentList.get(i).productCount)+"\nBuyer Id: "+String.valueOf(paymentList.get(i).buyerId)+"\nShipment Address: "+paymentList.get(i).shipment.address+"\nShipment plan: "+shipmentPlanCheck(paymentList.get(i).shipment.plan)+"\nShipment cost: 10.000"+"\nStatus:";
                        for(int j = 0; j < paymentList.get(i).history.size();j++){
                            status+="\n"+paymentList.get(i).history.get(j).status.toString()+"\nDate: "+paymentList.get(i).history.get(j).date.toString();
                        }
                        status+="\n________________________________________";
                    }
                }
                mainContent.setText(status);
            } catch (JSONException e) {
                Toast.makeText(StoreHistoryActivity.this, "find payments is failed",Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(StoreHistoryActivity.this);
        queue.add(PaymentRequest.getPaymentByProduct(AboutMeActivity.productIdList,respListPayment,errorListener));

        cancelBtn.setOnClickListener(o->{
            String payId = cancelId.getText().toString();
            int id =-1; boolean idFound = false;
            try {
                id = Integer.parseInt(payId);
            } catch (NumberFormatException e) {
                Toast.makeText(StoreHistoryActivity.this, "fail to parse.",Toast.LENGTH_SHORT).show();
            }

            for(int i=0;i<paymentList.size();i++){
                if(paymentList.get(i).id == id) idFound = true;
            }

            if(!idFound)
                id = -1;

            Response.Listener<String> listener = response -> {
                boolean resp = Boolean.parseBoolean(response);
                if (resp)
                    Toast.makeText(StoreHistoryActivity.this, "success.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StoreHistoryActivity.this, "failed.", Toast.LENGTH_SHORT).show();
            };

            Response.ErrorListener errListener = error -> {
                Toast.makeText(StoreHistoryActivity.this, "Something Error.",Toast.LENGTH_SHORT).show();
            };

            RequestQueue requestQueue = Volley.newRequestQueue(StoreHistoryActivity.this);
            requestQueue.add(PaymentRequest.cancelPayment(id, listener, errListener));
        });

        submitBtn.setOnClickListener(o->{
            String receipt = submitReceipt.getText().toString();
            String payId = submitId.getText().toString();
            int id =-1; boolean idFound = false;
            try {
                id = Integer.parseInt(payId);
            } catch (NumberFormatException e) {
                Toast.makeText(StoreHistoryActivity.this, "fail to parse.",Toast.LENGTH_SHORT).show();
            }

            for(int i=0;i<paymentList.size();i++){
                if(paymentList.get(i).id == id) idFound = true;
            }

            if(!idFound)
                id = -1;
            Response.Listener<String> listener = response -> {
                boolean resp = Boolean.parseBoolean(response);
                if (resp)
                    Toast.makeText(StoreHistoryActivity.this, "success.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StoreHistoryActivity.this, "failed.", Toast.LENGTH_SHORT).show();
            };

            Response.ErrorListener errListener = error -> {
                Toast.makeText(StoreHistoryActivity.this, "Something Error.",Toast.LENGTH_SHORT).show();
            };
            RequestQueue requestQueue = Volley.newRequestQueue(StoreHistoryActivity.this);
            requestQueue.add(PaymentRequest.submitPayment(id, receipt,listener, errListener));

        });

        acceptBtn.setOnClickListener(o->{
            String payId = acceptId.getText().toString();
            int id =-1; boolean idFound = false;
            try {
                id = Integer.parseInt(payId);
            } catch (NumberFormatException e) {
                Toast.makeText(StoreHistoryActivity.this, "fail to parse.",Toast.LENGTH_SHORT).show();
            }

            for(int i=0;i<paymentList.size();i++){
                if(paymentList.get(i).id == id) idFound = true;
            }

            if(!idFound)
                id = -1;

            Response.Listener<String> listener = response -> {
                boolean resp = Boolean.parseBoolean(response);
                if (resp){
                    Toast.makeText(StoreHistoryActivity.this, "success.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(StoreHistoryActivity.this, "failed.",Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener errListener = error -> {
                Toast.makeText(StoreHistoryActivity.this, "Something Error.",Toast.LENGTH_SHORT).show();
            };

            RequestQueue requestQueue = Volley.newRequestQueue(StoreHistoryActivity.this);
            requestQueue.add(PaymentRequest.acceptPayment(id, listener, errListener));
        });

    }

    public static String shipmentPlanCheck(byte plan){
        switch (plan){
            case (byte)(1<<0): return "INSTANT";
            case (byte)(1<<1): return "SAME DAY";
            case (byte)(1<<2): return "NEXT DAY";
            case (byte) (1<<3): return "REGULER";
            case (byte) (1<<4): return "KARGO";
            default: return "unknown";
        }
    }
}