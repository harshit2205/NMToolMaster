package com.rajorpay.hex.nmtoolmaster;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Utils.DateUtils;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.adapters.STBAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class AllSTBActivity extends AppCompatActivity {

    List<Customer> customers;
    List<Customer> filteredData;
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
            if (position == 2) {
                for (Customer customer : customers) {
                    if (!DateUtils.isMoreThanCurrent(customer.getPaidTill())) {
                        filteredData.add(customer);
                    }
                }
                myAdapter.setComparisionBasis(0);
                myAdapter.setCustomers(customers);
            } else if(position == 3) {
                for (Customer customer : customers) {
                    if (customer.getBoxType().equals(NMToolConstants.DEN)) {
                        filteredData.add(customer);
                    }
                }
                myAdapter.setCustomers(filteredData);
            }else if(position == 4) {
                for (Customer customer : customers) {
                    if (customer.getBoxType().equals(NMToolConstants.SITI)) {
                        filteredData.add(customer);
                    }
                }
            }else if(position == 5) {
                for (Customer customer : customers) {
                    if (customer.getBoxType().equals(NMToolConstants.NETVISION)) {
                        filteredData.add(customer);
                    }
                }
            } else {
                    Log.d("NMTool", " " + position);
                    comparisionBasis = position;
                    myAdapter.setComparisionBasis(position);
                    myAdapter.notifyDataSetChanged();
                }
                layout1.setVisibility(View.GONE);
            allSetUpBoxData.setVisibility(View.VISIBLE);
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
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(NMToolConstants.CUSTOMER);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Customer customer = snapshot.getValue(Customer.class);
                    customers.add(customer);
                }
                filteredData = customers;
                myAdapter = new STBAdapter(customers, comparisionBasis, getApplicationContext());
                allListSize.setText("Total items in list: "+customers.size());
                allSetUpBoxData.setAdapter(myAdapter);
                layout1.setVisibility(View.GONE);
                allSetUpBoxData.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
