package com.rajorpay.hex.nmtoolmaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.ComplaintVO;
import com.rajorpay.hex.nmtoolmaster.adapters.ComplaintAdapter;
import com.rajorpay.hex.nmtoolmaster.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    @BindView(R.id.main_expandable_list)
    ExpandableListView mainList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        //setting the adapter
        mainList.setAdapter(listAdapter);
        addListeners();
    }

    /*
    * preparing list data
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Set Top Box Details");
        listDataHeader.add("Create New Account");
        listDataHeader.add("Edit Account");
        listDataHeader.add("Complaints");
        listDataHeader.add("Payments");
        listDataHeader.add("Statistics");
        listDataHeader.add("Channels and Packages");

        // Adding child data
        List<String> item1 = new ArrayList<String>();
        item1.add("Set Top Box Details");

        List<String> item2 = new ArrayList<String>();
        item2.add("Create New User");
        item2.add("Create New Employee");

        List<String> item3 = new ArrayList<String>();
        item3.add("Edit User Details");
        item3.add("Edit Employee Details");

        List<String> item4 = new ArrayList<String>();
        item4.add("Create New Complaint");
        item4.add("Pnding Coplaints");
        item4.add("Past Complaints");

        List<String> item5 = new ArrayList<String>();
        item5.add("View All Pending Payments");
        item5.add("Payments This Month");

        List<String> item6 = new ArrayList<>();
        item6.add("Statistics");

        List<String> item7 = new ArrayList<>();
        item7.add("Edit Channels and Packages");

        listDataChild.put(listDataHeader.get(0), item1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), item2);
        listDataChild.put(listDataHeader.get(2), item3);
        listDataChild.put(listDataHeader.get(3), item4);
        listDataChild.put(listDataHeader.get(4), item5);
        listDataChild.put(listDataHeader.get(5), item6);
        listDataChild.put(listDataHeader.get(6), item7);
    }


    private void addListeners(){
        // Listview on child click listener
        mainList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent i = null;
                switch (groupPosition){
                    case 0:
                        i = (childPosition == 0)?new Intent(MainActivity.this, AllSTBActivity.class):i;
                        break;
                    case 1:
                        i = (childPosition == 0)?new Intent(MainActivity.this,CreateUserActivity.class):i;
                        break;
                    case 2:
                        break;
                    case 3:
                        i = (childPosition == 1)?new Intent(MainActivity.this, PendingComplaintActivity.class):i;
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
                if( i!= null) {
                    startActivity(i);
                }
                return false;
            }
        });
    }
}
