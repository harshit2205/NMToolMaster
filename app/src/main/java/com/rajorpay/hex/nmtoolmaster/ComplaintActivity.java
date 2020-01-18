package com.rajorpay.hex.nmtoolmaster;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.ComplaintVO;
import com.rajorpay.hex.nmtoolmaster.adapters.ComplaintAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintActivity extends AppCompatActivity {

    @BindView(R.id.main_complaints) RecyclerView complaintSummary;
    @BindView(R.id.complaint_fetch_complaints) LinearLayout complaintFetchingLayout;
    @BindView(R.id.no_complaints) TextView noComplaintLayout;
    @BindView(R.id.complaint_all) LinearLayout allComplaintsLayout;
    @BindView(R.id.complaints_header)TextView complaintsHeader;
    @BindView(R.id.see_all_complaints)Button listToggler;
    @BindDrawable(R.drawable.button_background_disabled)Drawable buttonDisabled;
    @BindDrawable(R.drawable.button_bakground)Drawable buttonBackground;
    @OnClick(R.id.see_all_complaints)
    public void onClick(){
        if(allComplaints.size() != 0 && listToggler.getText().toString().equals("MORE")){
            complaintAdapter.setComplaints(allComplaints);
            complaintAdapter.notifyDataSetChanged();
            listToggler.setText("LESS");
        }else if(complaints.size() != 0 && listToggler.getText().toString().equals("LESS")){
            complaintAdapter.setComplaints(complaints);
            complaintAdapter.notifyDataSetChanged();
            listToggler.setText("MORE");
        }
    }
    List<ComplaintVO> complaints = new ArrayList<>();
    List<ComplaintVO> allComplaints = new ArrayList<>();
    ComplaintAdapter complaintAdapter = new ComplaintAdapter(complaints, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        ButterKnife.bind(this);
        complaintSummary.setLayoutManager(new LinearLayoutManager(this));
        complaintSummary.setAdapter(complaintAdapter);
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
        resetFields();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Complaints");
        complaints = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintFetchingLayout.setVisibility(View.GONE);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot snaps: snapshot.getChildren()){
                        ComplaintVO complaintVO = snaps.getValue(ComplaintVO.class);
                        allComplaints.add(complaintVO);
                        if(!TextUtils.equals(complaintVO.getStatus(),"completed")){
                            complaints.add(complaintVO);
                        }
                    }
                }
                Collections.sort(complaints);
                Collections.sort(allComplaints);
                if(allComplaints.size() == 0){
                    noComplaintLayout.setVisibility(View.VISIBLE);
                }else if(complaints.size() == 0){
                    complaintsHeader.setText("All Complaints");
                    listToggler.setEnabled(false);
                    listToggler.setText("disabled");
                    listToggler.setBackground(buttonDisabled);
                    complaintAdapter.setComplaints(allComplaints);
                    allComplaintsLayout.setVisibility(View.VISIBLE);
                }else{
                    complaintAdapter.setComplaints(complaints);
                    allComplaintsLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ComplaintActivity.this,
                        "Data Could not be fetched due to some issue",Toast.LENGTH_SHORT).show();
                noComplaintLayout.setText("Can't fetch data");
                noComplaintLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void resetFields(){
        listToggler.setEnabled(true);
        listToggler.setBackground(buttonBackground);
        listToggler.setText("MORE");
        complaintFetchingLayout.setVisibility(View.VISIBLE);
        noComplaintLayout.setVisibility(View.GONE);
        allComplaintsLayout.setVisibility(View.GONE);
        allComplaints = new ArrayList<>();
        complaints = new ArrayList<>();
    }
}
