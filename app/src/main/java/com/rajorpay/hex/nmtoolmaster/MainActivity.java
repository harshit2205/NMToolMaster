package com.rajorpay.hex.nmtoolmaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.ComplaintVO;
import com.rajorpay.hex.nmtoolmaster.adapters.ComplaintAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_new_account)CardView mainNewAccount;
    @OnClick(R.id.main_new_account)
    public void createNewAccount(){
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
    }
    @BindView(R.id.main_view_complaint)CardView mainComplaints;
    @OnClick(R.id.main_view_complaint)
    public void loadComplaints(){
        Intent i = new Intent(MainActivity.this, ComplaintActivity.class);
        startActivity(i);
    }
    @BindView(R.id.main_all_stb)CardView mainAllStb;
    @OnClick(R.id.main_all_stb)
    public void loadAllStb(){
        Intent i = new Intent(MainActivity.this, AllSTBActivity.class);
        startActivity(i);
    }
    @BindView(R.id.main_pending_payment)CardView mainPendingPayments;
    @OnClick(R.id.main_pending_payment)
    public void loadPendingPayments(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}
