package com.rajorpay.hex.nmtoolmaster;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Utils.DateUtils;
import com.rajorpay.hex.nmtoolmaster.Utils.ExcelUtil;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.adapters.STBAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class AllSTBActivity extends AppCompatActivity {

    List<Customer> customers;
    HashMap<String, List<Customer>> customerAreaMap;
    STBAdapter myAdapter;
    int comparisionBasis = 0;
    @BindView(R.id.all_loading)LinearLayout layout1;
    @BindView(R.id.all_list_size)TextView allListSize;
    @BindView(R.id.all_stb_data)RecyclerView allSetUpBoxData;
    @BindView(R.id.seach_basis)Spinner searchBasis;
    @OnItemSelected(R.id.seach_basis)
    void reloadAdapter(int position) {
        if(myAdapter!= null) {
            layout1.setVisibility(View.VISIBLE);
            Log.d("NMTool", " " + position);
            comparisionBasis = position;
            myAdapter.setComparisionBasis(position);
            myAdapter.notifyDataSetChanged();
            layout1.setVisibility(View.GONE);
            allSetUpBoxData.setVisibility(View.VISIBLE);
            }
        }
    @BindView(R.id.all_export_button)
    Button fetchExcel;
    @OnClick(R.id.all_export_button)
    void FetchExcelReport() {
        if (isWriteStoragePermissionGranted()) {
            ExcelUtil util = new ExcelUtil();
            util.exportUserInfoToExcel(this, customerAreaMap);
        } else {
            Log.d("NM_TOOLstr", "permission not granted");
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("NM_TOOLstr","Permission is granted2");
                return true;
            } else {
                Log.v("NM_TOOLstr","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("NM_TOOLstr","Permission is granted2");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stb);
        ButterKnife.bind(this);
        allSetUpBoxData.setLayoutManager(new LinearLayoutManager(this));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Set Top Box Details");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        customers = new ArrayList<>();
        customerAreaMap = new HashMap<String, List<Customer>>();

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(NMToolConstants.CUSTOMER);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Customer customer = snapshot.getValue(Customer.class);
                    customers.add(customer);
                    customerAreaMap = populateMap(customerAreaMap, customer);
                }
                allListSize.setText("Total Number of Connections: "+customers.size());
                customers = adaptiveListCreator(customerAreaMap);
                myAdapter = new STBAdapter(customers, comparisionBasis, getApplicationContext());
                myAdapter.setCustomers(customers);
                allSetUpBoxData.setAdapter(myAdapter);
                layout1.setVisibility(View.GONE);
                allSetUpBoxData.setVisibility(View.VISIBLE);
                fetchExcel.setEnabled(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllSTBActivity.this, "Couldn't Fetch Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private HashMap<String,List<Customer>> populateMap(HashMap<String,List<Customer>> customerAreaMap, Customer customer){
        String key = customer.getLocality();
        if( customerAreaMap.containsKey(key)){
            List<Customer> customers = customerAreaMap.get(key);
            customers.add(customer);
            customerAreaMap.put(key, customers);
        }else{
            List<Customer> customers = new ArrayList<>();
            customers.add(customer);
            customerAreaMap.put(key, customers);
        }
        return customerAreaMap;
    }

    private List<Customer> adaptiveListCreator(HashMap<String, List<Customer>> customerAreaMap){
        List<Customer> customers = new ArrayList<Customer>();
        Iterator it = customerAreaMap.entrySet().iterator();
        String key = "";
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(!key.equals(pair.getKey())){
                Customer customer = new Customer();
                customer.setLocality((String)pair.getKey());
                customer.setHeader(true);
                customers.add(customer);
            }
            customers.addAll((List<Customer>)pair.getValue());
        }
        return customers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_stb_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                myAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                myAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
}
