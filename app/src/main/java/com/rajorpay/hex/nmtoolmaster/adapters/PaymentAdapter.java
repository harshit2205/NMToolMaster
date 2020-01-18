package com.rajorpay.hex.nmtoolmaster.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rajorpay.hex.nmtoolmaster.Models.PaymentVO;
import com.rajorpay.hex.nmtoolmaster.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {
    List<PaymentVO> payments;

    public void setPayments(List<PaymentVO> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    public PaymentAdapter(List<PaymentVO> payments){
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.payment_holder, viewGroup, false);
        return new PaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder paymentViewHolder, int i) {
        long yourmilliseconds = Long.parseLong(payments.get(i).getPaidOn());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        paymentViewHolder.bindViews(sdf.format(resultdate), payments.get(i).getPaidFor());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }
}

class PaymentViewHolder extends RecyclerView.ViewHolder {
    TextView paidFor;
    TextView paidOn;

    public PaymentViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
        paidFor = itemView.findViewById(R.id.holder_paid_for);
        paidOn = itemView.findViewById(R.id.holder_paid_on);
    }

    public void bindViews(String paidOnString, String paidForString){
        paidFor.setText(paidForString);
        paidOn.setText(paidOnString);
    }
}
