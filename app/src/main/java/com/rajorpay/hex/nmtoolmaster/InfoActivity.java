package com.rajorpay.hex.nmtoolmaster;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class InfoActivity extends AppCompatActivity {

    public static Customer customer;
    PaymentAdapter paymentAdapter;
    List<PaymentVO> payments;
    @BindView(R.id.info_name)TextView infoName;
    @BindView(R.id.info_edit_name)EditText infoEditName;
    @BindView(R.id.info_phone_nr)TextView infoPhoneNr;
    @BindView(R.id.info_edit_phone_nr)EditText infoEditPhoneNr;
    @BindView(R.id.info_box_type)TextView infoBoxType;
    @BindView(R.id.info_box_nr)TextView infoBoxNr;
    @BindView(R.id.info_address)TextView infoAddress;
    @BindView(R.id.info_edit_address)EditText infoEditAddress;
    @BindView(R.id.info_package_amount)TextView infoPackageAmount;
    @BindView(R.id.info_edit_package_amount)EditText infoEditPackageAmount;
    @BindView(R.id.info_payment)TextView infoPayment;
    @BindView(R.id.info_payment_history)Button infoPaymentHistory;
    @BindView(R.id.info_no_history)TextView infoNoHistory;
    @BindView(R.id.info_loading_history)ProgressBar infoLoadingHistory;
    @BindView(R.id.payment_history)RecyclerView paymentHistory;
    @BindView(R.id.info_edit_button)Button editInfo;
    @OnClick(R.id.info_edit_button)
    public void editInfo(){
        if(infoName.getVisibility() == View.GONE){
            infoName.setVisibility(View.VISIBLE);
            infoPhoneNr.setVisibility(View.VISIBLE);
            infoAddress.setVisibility(View.VISIBLE);
            infoPackageAmount.setVisibility(View.VISIBLE);
            infoEditName.setVisibility(View.GONE);
            infoEditPhoneNr.setVisibility(View.GONE);
            infoEditAddress.setVisibility(View.GONE);
            infoEditPackageAmount.setVisibility(View.GONE);

        }else {
            infoName.setVisibility(GONE);
            infoPhoneNr.setVisibility(GONE);
            infoAddress.setVisibility(GONE);
            infoPackageAmount.setVisibility(GONE);
            infoEditName.setVisibility(View.VISIBLE);
            infoEditPhoneNr.setVisibility(View.VISIBLE);
            infoEditAddress.setVisibility(View.VISIBLE);
            infoEditPackageAmount.setVisibility(View.VISIBLE);
        }
    }
    @BindDrawable(R.drawable.button_background_disabled)Drawable buttonDisabled;
    @OnClick(R.id.info_payment_history)
    public void showpayments(){
        if(infoPaymentHistory.getText().equals(NMToolConstants.SHOW)){
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
                        paymentAdapter.setPayments(payments);
                        Log.d("NMTool", payments.size()+" :size12345: ");
                        if(payments.size() == 0){
                            infoNoHistory.setVisibility(View.VISIBLE);
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
                    infoLoadingHistory.setVisibility(GONE);
                }
            });
            infoLoadingHistory.setVisibility(GONE);
            Log.d("NMTool", payments.size()+" :size: ");

            infoPaymentHistory.setText(NMToolConstants.HIDE);
        }else{
            refreshElements();
            infoPaymentHistory.setText(NMToolConstants.SHOW);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        paymentHistory.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(payments);
        paymentHistory.setAdapter(paymentAdapter);

        infoName.setText(customer.getName());
        infoEditName.setText(customer.getName());
        infoPhoneNr.setText(customer.getPhoneNumber());
        infoEditPhoneNr.setText(customer.getPhoneNumber());
        infoBoxType.setText(customer.getBoxType());
        infoBoxNr.setText(customer.getBoxNumber());
        infoAddress.setText(customer.getAddress());
        infoEditAddress.setText(customer.getAddress());
        infoPackageAmount.setText(customer.getPackageAmount());
        infoEditPackageAmount.setText(customer.getPackageAmount());
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

    public void refreshElements(){
        infoLoadingHistory.setVisibility(GONE);
        infoNoHistory.setVisibility(GONE);
        paymentHistory.setVisibility(GONE);
    }
}
