package com.AgungJmartAK.jmart_android;

import static com.AgungJmartAK.jmart_android.ProductFragment.listViewAdapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;

import com.AgungJmartAK.jmart_android.model.Account;
import com.AgungJmartAK.jmart_android.model.ProductCategory;

/**
 * This class to find a product with
 * specific information
 *
 * @author Agung Firmansyah
 * */
public class FilterFragment extends Fragment {
    EditText search;
    EditText lowestPrice_;
    EditText highestPrice_;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button aplyBtn;
    ProductCategory category;
    boolean condition = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
        Account account = LoginActivity.getLoggedAccount();
        if(account.store==null)
            menu.findItem(R.id.add).setVisible(false);
        else menu.findItem(R.id.add).setVisible(true);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                break;
            case R.id.add:
                startActivity(new Intent(getActivity(),CreateProductActivity.class));
                break;
            case R.id.person:
                startActivity(new Intent(getActivity(),AboutMeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        search = (EditText) view.findViewById(R.id.etSearch);
        lowestPrice_ = (EditText) view.findViewById(R.id.etLowestPrice);
        highestPrice_ = (EditText) view.findViewById(R.id.etHIghestPrice);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup2);
        aplyBtn = (Button) view.findViewById(R.id.applyBtn);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener((radioGroup, input) -> {
            switch (input){
                case R.id.newCondition2:
                    condition = false;
                    break;
                case R.id.usedCondition2:
                    condition = true;
                    break;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = ProductCategory.valueOf(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        aplyBtn.setOnClickListener(view1 -> {
            int selectedCondition = radioGroup.getCheckedRadioButtonId();
            radioButton = view1.findViewById(selectedCondition);
            String name = search.getText().toString();
            String bottomPrice = lowestPrice_.getText().toString();
            String topPrice = highestPrice_.getText().toString();
            double lowestPrice = 0.0;
            double highestPrice = 0.0;

            highestPrice = Double.valueOf(topPrice);
            lowestPrice = Double.valueOf(bottomPrice);

            Intent intent = new Intent(getActivity(),ProductActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("lowestPrice", lowestPrice);
            intent.putExtra("highestPrice", highestPrice);
            intent.putExtra("condition",condition);
            intent.putExtra("category", category);
            startActivity(intent);

        });

        return view;
    }
}