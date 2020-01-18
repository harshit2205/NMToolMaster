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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.ComplaintVO;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Models.PaymentVO;
import com.rajorpay.hex.nmtoolmaster.Utils.DateUtils;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.adapters.PaymentAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintInfoActivity extends AppCompatActivity {

    private static ComplaintVO complaintVO = null;
    List<PaymentVO> payments;
    PaymentAdapter paymentAdapter;
    @BindView(R.id.info_name) TextView infoName;
    @BindView(R.id.info_phone_nr) TextView infoPhonenr;
    @BindView(R.id.info_stb_nr) TextView infoStbnr;
    @BindView(R.id.info_payment)TextView infoPayment;
    @BindView(R.id.info_locality) TextView infoLocality;
    @BindView(R.id.info_complaint_type) TextView infoComplaintType;
    @BindView(R.id.info_in_time) TextView infoInTime;
    @BindView(R.id.info_out_time) TextView infoOutTime;
    @BindView(R.id.info_complaint_status) Spinner infoStatus;
    @BindView(R.id.info_assigned_to) Spinner infoAssignedTo;
    @BindView(R.id.info_update) Button infoUpdate;
    @BindView(R.id.payment_history)RecyclerView paymentHistory;
    @BindView(R.id.info_loading_history)ProgressBar loadingBar;
    @BindView(R.id.info_no_history)TextView noHistoryText;
    @BindDrawable(R.drawable.button_background_disabled)Drawable buttonDisabled;
    @BindView(R.id.info_payment_history) Button infoPaymentHistory;
    @OnClick(R.id.info_payment_history)
    public void showpayments(){
        if(infoPaymentHistory.getText().equals(NMToolConstants.SHOW)){
            loadingBar.setVisibility(View.VISIBLE);
            payments = new ArrayList<>();
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                    .child(NMToolConstants.PAYMENT).child(complaintVO.getUserId());
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        PaymentVO paymentVO = snapshot.getValue(PaymentVO.class);
                        payments.add(paymentVO);
                        paymentAdapter.setPayments(payments);
                        Log.d("NMTool", payments.size()+" :size12345: ");
                        if(payments.size() == 0){
                            noHistoryText.setVisibility(View.VISIBLE);
                        }else{
                            paymentAdapter.setPayments(payments);
                            paymentHistory.setVisibility(View.VISIBLE);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext()
                            ,"data could'nt be retrived from server",Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.GONE);
                }
            });
            loadingBar.setVisibility(View.GONE);
            Log.d("NMTool", payments.size()+" :size: ");

            infoPaymentHistory.setText(NMToolConstants.HIDE);
        }else{
            refreshElements();
            infoPaymentHistory.setText(NMToolConstants.SHOW);
        }
    }

    public static void infoInitialiser(ComplaintVO complaintVO){
        ComplaintInfoActivity.complaintVO = complaintVO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_info);
        ButterKnife.bind(this);
        payments = new ArrayList<>();
        infoName.setText(complaintVO.getName());
        infoStbnr.setText(complaintVO.getBoxNumber());
        infoComplaintType.setText(complaintVO.getComplaintType());
        disableAllIfCompleted();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Long inTime = Long.parseLong(complaintVO.getInTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(inTime);
        infoInTime.setText(sdf.format(resultdate));
        if(!TextUtils.isEmpty(complaintVO.getOutTime())){
            Long outTime = Long.parseLong(complaintVO.getOutTime());
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate1 = new Date(inTime);
            infoOutTime.setText(sdf.format(resultdate1));
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) infoStatus.getAdapter();
        int spinnerPosition = adapter.getPosition(complaintVO.getStatus());
        infoStatus.setSelection(spinnerPosition);
        ArrayAdapter<String> adapter1 = (ArrayAdapter<String>)infoAssignedTo.getAdapter();
        int spinnerPosition2 = adapter1.getPosition(complaintVO.getAssignedTo());
        infoAssignedTo.setSelection(spinnerPosition2);
        paymentHistory.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(payments);
        paymentHistory.setAdapter(paymentAdapter);
    }

    @OnClick(R.id.info_update)
    public void updateInformation(){
        complaintVO.setStatus(infoStatus.getSelectedItem().toString());
        if(complaintVO.getStatus().equals(NMToolConstants.STATUS_COMPLETED)){
            complaintVO.setOutTime(Long.toString(System.currentTimeMillis()));
        }
        complaintVO.setAssignedTo(infoAssignedTo.getSelectedItem().toString());
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                .child(NMToolConstants.COMPLAINT).child(complaintVO.getUserId()).child(complaintVO.getComplaintId());
        dbReference.setValue(complaintVO);
        onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        refreshElements();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                .child(NMToolConstants.CUSTOMER).child(complaintVO.getUserId());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                if(customer != null){
                    infoPhonenr.setText("0" + customer.getPhoneNumber());
                    infoLocality.setText(customer.getLocality());
                    if(TextUtils.isEmpty(customer.getPaidTill())){
                        infoPayment.setText("No payments made yet.");
                        infoPaymentHistory.setEnabled(false);
                        infoPaymentHistory.setBackground(buttonDisabled);
                        infoPaymentHistory.setText("Disabled");
                    }else if(DateUtils.isMoreThanCurrent(customer.getPaidTill())){
                        infoPayment.setText("Payment is upto date.");
                        infoPayment.setTextColor(Color.parseColor("#3AC871"));
                    }else{
                        infoPayment.setText("payment is outdated.");
                        infoPayment.setTextColor(Color.parseColor("#D50000"));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void disableAllIfCompleted(){
        if(complaintVO.getStatus().equals(NMToolConstants.STATUS_COMPLETED)){
            infoUpdate.setEnabled(false);
            infoUpdate.setBackground(buttonDisabled);
            infoUpdate.setText("Disabled");
        }
    }

    public void refreshElements(){
        loadingBar.setVisibility(View.GONE);
        noHistoryText.setVisibility(View.GONE);
        paymentHistory.setVisibility(View.GONE);
    }
}
