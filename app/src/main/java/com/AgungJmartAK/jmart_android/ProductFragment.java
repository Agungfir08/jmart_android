package com.AgungJmartAK.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
 * This is fragment for showing all the product
 *
 * @author Agung Firmansyah
 * */
public class ProductFragment extends Fragment {
    final int pageSize = 10;
    int page = 0;

    private static final Gson gson = new Gson();
    private static ArrayList<Product> productsList = new ArrayList<>();

    ListView listProd;
    EditText etPage;
    Button prevBtn, nextBtn, goBtn;
    public static Product selectedProduct = null;

    public static Product getProductDetail(){
        return selectedProduct;
    }
    public static ArrayAdapter<Product> listViewAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Account account = LoginActivity.getLoggedAccount();
        if(account.store!=null)
            menu.findItem(R.id.add).setVisible(true);
        else menu.findItem(R.id.add).setVisible(false);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search product here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listViewAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.add:
                startActivity(new Intent(getActivity(), CreateProductActivity.class));
                break;
            case R.id.person:
                startActivity(new Intent(getActivity(), AboutMeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_product, container, false);
        etPage = (EditText) productView.findViewById(R.id.etPage);
        prevBtn = (Button) productView.findViewById(R.id.button4);
        nextBtn = (Button) productView.findViewById(R.id.button5);
        goBtn = (Button) productView.findViewById(R.id.button6);
        listProd = (ListView) productView.findViewById(R.id.lvProduct);

        listProd.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedProduct = productsList.get(i);
            Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
            startActivity(intent);
        });

        Response.Listener<String> listener = response -> {
            try {
                JSONArray object = new JSONArray(response);
                if (object != null) {
                    productsList = gson.fromJson(object.toString(), new TypeToken<ArrayList<Product>>() {
                    }.getType());
                    listViewAdapter = new ArrayAdapter<Product>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            productsList
                    );
                    if(!productsList.isEmpty())
                        listProd.setAdapter(listViewAdapter);
                    else
                    if(page>0)
                        page--;
                    Toast.makeText(getActivity(), "page "+String.valueOf(page+1),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.Listener<String> secondListener = response -> {
            try {
                JSONArray object = new JSONArray(response);
                if (object != null) {
                    productsList = gson.fromJson(object.toString(), new TypeToken<ArrayList<Product>>() {
                    }.getType());
                    ArrayAdapter<Product> listViewAdapter = new ArrayAdapter<Product>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            productsList
                    );
                    if(!productsList.isEmpty())
                        listProd.setAdapter(listViewAdapter);
                    else{
                        Toast.makeText(getActivity(), "No data in this page.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        nextBtn.setOnClickListener(view -> {
            page++;
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(RequestFactory.getPage("product", page, pageSize, listener, null));
        });

        prevBtn.setOnClickListener(view -> {
            if(page>0){
                page--;
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueue.add(RequestFactory.getPage("product", page, pageSize, listener, null));
            }
        });

        goBtn.setOnClickListener(view -> {
            try{
                page = Integer.parseInt(etPage.getText().toString());
                if(page>=1){
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue.add(RequestFactory.getPage("product", page-1, pageSize, secondListener, null));
                }else{
                    Toast.makeText(getActivity(), "page start with 1.",Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "failed to parse.",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(RequestFactory.getPage("product", page, pageSize, listener, null));
        return productView;
    }
}