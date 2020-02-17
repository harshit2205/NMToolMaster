package com.rajorpay.hex.nmtoolmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {
    List<Customer> customers;

    public void setUserList(List<Customer> customers){
        this.customers = customers;
        this.notifyDataSetChanged();
    }

    public UserListAdapter(List<Customer> customers){
        this.customers = customers;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_info_holder, parent, false);
        return new UserViewHolder(parent.getContext(), v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindView(customers.get(position));
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder{

    TextView userName;
    TextView stbNumber;
    TextView userAddress;
    TextView userPackage;
    TextView paymentHeader;
    TextView payInfo;
    Button raiseComplaint;
    Customer customer;
    public UserViewHolder(final Context parent, @NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.holder_user_name);
        stbNumber = itemView.findViewById(R.id.holder_user_stb);
        userAddress = itemView.findViewById(R.id.holder_user_address);
        userPackage = itemView.findViewById(R.id.holder_user_package);
        payInfo = itemView.findViewById(R.id.holder_pay_info);
        raiseComplaint = itemView.findViewById(R.id.raise_complaint);
        raiseComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void bindView(Customer _customer){
        customer = _customer;
        userName.setText(_customer.getName());
        stbNumber.setText(_customer.getBoxNumber());
        userAddress.setText(_customer.getAddress());
        userPackage.setText(_customer.getPackageAmount());
        payInfo.setText("last paid till "+_customer.getPaidTill());
    }
}

