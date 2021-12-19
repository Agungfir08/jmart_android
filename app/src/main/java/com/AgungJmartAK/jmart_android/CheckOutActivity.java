package com.AgungJmartAK.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.model.Product;
import com.AgungJmartAK.jmart_android.request.PaymentRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class to inform user about
 * product they want to buy
 *
 * @author Agung Firmansyah
 * */
public class CheckOutActivity extends AppCompatActivity {
    TextView category, name, discPrice, count, quantity, originalPrice,
            shipCost, discount, total;
    EditText address;
    Button increment, decrement, checkout;
    private int prodCount = 1;
    Product product = ProductFragment.getProductDetail();
    private double discountedPrice = product.price*(100.0-product.discount)/100.0;
    Account account = LoginActivity.getLoggedAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        category = findViewById(R.id.coCategory);
        name = findViewById(R.id.coName);
        discPrice = findViewById(R.id.coDiscPrice);
        count = findViewById(R.id.coCount);
        increment = findViewById(R.id.incButton);
        decrement = findViewById(R.id.dcrButton);
        quantity = findViewById(R.id.coQuanitity);
        originalPrice = findViewById(R.id.coRealPrice);
        shipCost = findViewById(R.id.coShipCost);
        discount = findViewById(R.id.coDiscount);
        total = findViewById(R.id.coTotal);
        checkout = findViewById(R.id.checkoutButton);
        address = findViewById(R.id.delivAddress);

        increment.setOnClickListener(clickListener);
        decrement.setOnClickListener(clickListener);

        category.setText(product.category.toString());
        name.setText(product.name);
        discPrice.setText(String.valueOf(discountedPrice));
        originalPrice.setText(String.valueOf(product.price*prodCount));
        discount.setText(String.valueOf(product.discount)+"%");
        total.setText(String.valueOf(10000.0+discountedPrice*prodCount));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deliveryAddress = address.getText().toString();
                Response.Listener<String> respList = response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object!=null){
                            Toast.makeText(CheckOutActivity.this, "Success.",Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(CheckOutActivity.this, "Failed.",Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(CheckOutActivity.this, "Failed",Toast.LENGTH_SHORT).show();
                    }
                };

                Response.ErrorListener errorListener = error -> {
                    Toast.makeText(CheckOutActivity.this, "Error!",Toast.LENGTH_SHORT).show();
                };

                PaymentRequest request = new PaymentRequest(account.id, product.id,prodCount,deliveryAddress, product.shipmentPlans, respList,errorListener);
                RequestQueue queue = Volley.newRequestQueue(CheckOutActivity.this);
                queue.add(request);
            }
        });
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.incButton:
                    prodCount++;
                    count.setText(prodCount+"");
                    quantity.setText(prodCount+" Items");
                    discPrice.setText(String.valueOf(discountedPrice*prodCount));
                    originalPrice.setText(String.valueOf(product.price*prodCount));
                    total.setText(String.valueOf(10000.0+discountedPrice*prodCount));
                    break;
                case R.id.dcrButton:
                    if(prodCount>0){
                        prodCount--;
                        count.setText(prodCount+"");
                        quantity.setText(prodCount+" Items");
                        discPrice.setText(String.valueOf(discountedPrice*prodCount));
                        originalPrice.setText(String.valueOf(product.price*prodCount));
                        total.setText(String.valueOf(10000.0+discountedPrice*prodCount));
                    }
                    break;
            }
        }
    };
}