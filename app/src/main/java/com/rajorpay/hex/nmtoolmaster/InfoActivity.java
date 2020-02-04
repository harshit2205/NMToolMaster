package com.rajorpay.hex.nmtoolmaster;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Models.PaymentVO;
import com.rajorpay.hex.nmtoolmaster.Utils.DateUtils;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.adapters.PaymentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class InfoActivity extends AppCompatActivity {

    public static Customer customer;
    PaymentAdapter paymentAdapter;
    List<PaymentVO> payments;
    @BindView(R.id.info_phone_nr)TextView infoPhoneNr;
    @BindView(R.id.info_stb_number) TextView infoBoxNumber;
    @BindView(R.id.info_box_type)TextView infoBoxType;
    @BindView(R.id.info_address)TextView infoAddress;
    @BindView(R.id.info_package_amount)TextView infoPackageAmount;
    @BindView(R.id.info_locality)TextView infoLocality;
    @BindView(R.id.info_payment)TextView infoPayment;
    @BindView(R.id.info_loading_history)ProgressBar infoLoadingHistory;
    @BindView(R.id.payment_history)RecyclerView paymentHistory;

    @BindDrawable(R.drawable.button_background_disabled)Drawable buttonDisabled;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        ActionBar actionBar =  getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(customer.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

        paymentHistory.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(payments);
        paymentHistory.setAdapter(paymentAdapter);
        infoBoxNumber.setText(customer.getBoxNumber());
        infoPhoneNr.setText(customer.getPhoneNumber());
        infoBoxType.setText(customer.getBoxType());
        infoAddress.setText(customer.getAddress());
        infoLocality.setText(customer.getLocality());
        String packageAmountInt = getResources().getString(R.string.Rs)+" "+customer.getPackageAmount();
        infoPackageAmount.setText(packageAmountInt);
        if(TextUtils.isEmpty(customer.getPaidTill())){
            infoPayment.setText("No payments made yet.");
        }else if(DateUtils.isMoreThanCurrent(customer.getPaidTill())){
            infoPayment.setText("Payment is upto date.");
            infoPayment.setTextColor(Color.parseColor("#3AC871"));
        }else{
            infoPayment.setText("payment is outdated.");
            infoPayment.setTextColor(Color.parseColor("#D50000"));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        showPayments();

    }

    public void showPayments(){
        infoLoadingHistory.setVisibility(View.VISIBLE);
        payments = new ArrayList<>();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                .child(NMToolConstants.PAYMENT).child(customer.getCustId());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PaymentVO paymentVO = snapshot.getValue(PaymentVO.class);
                    payments.add(paymentVO);
                }
                Collections.sort(payments);
                paymentAdapter.setPayments(payments);
                Log.d("NMTool", payments.size()+" :size12345: ");
                paymentAdapter.setPayments(payments);
                paymentHistory.setVisibility(View.VISIBLE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext()
                        ,"data could'nt be retrived from server",Toast.LENGTH_SHORT).show();
                infoLoadingHistory.setVisibility(GONE);
            }
        });
        infoLoadingHistory.setVisibility(GONE);
        Log.d("NMTool", payments.size()+" :size: ");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
