package com.rajorpay.hex.nmtoolmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajorpay.hex.nmtoolmaster.InfoActivity;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.R;
import com.rajorpay.hex.nmtoolmaster.Utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class STBAdapter extends RecyclerView.Adapter<STBViewHolder> {

    private Context context;
    private int comparisionBasis = 0;
    private List<Customer> customers;
    private List<Customer> filteredList;

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    public STBAdapter(List<Customer> customers, int comparisionBasis, Context context){
            this.comparisionBasis = comparisionBasis;
            this.filteredList = customers;
            this.context = context;
    }

    public void setComparisionBasis(int comparisionBasis) {
        this.comparisionBasis = comparisionBasis;
    }

    @NonNull
    @Override
    public STBViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stb_holder, viewGroup, false);
        return new STBViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull STBViewHolder stbViewHolder, int i) {
        switch(comparisionBasis){
            case 0:
                stbViewHolder.bindView(context, filteredList.get(i), filteredList.get(i).getBoxNumber());
                break;
            case 1:
                stbViewHolder.bindView(context, filteredList.get(i), filteredList.get(i).getName());
                break;
            default:
                stbViewHolder.bindView(context, filteredList.get(i), filteredList.get(i).getBoxNumber());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : customers) {
                        switch (comparisionBasis){
                            case 0:
                                if (row.getBoxNumber().toLowerCase().contains(charString.toLowerCase()) ) {
                                    filteredList.add(row);
                                }
                                break;
                            case 1:
                                if(row.getName().toLowerCase().contains(charString.toLowerCase())){
                                    filteredList.add(row);
                                }
                                break;
                        }
                    }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}

class STBViewHolder extends RecyclerView.ViewHolder{
    Customer customer;
    Context context;
    ImageView allDetails;
    @BindView(R.id.holder_stb_info) TextView stbInfo;

    public STBViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
        stbInfo = itemView.findViewById(R.id.holder_stb_info);
        allDetails = itemView.findViewById(R.id.holder_enter);
        allDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoActivity.class);
                InfoActivity.customer = customer;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void bindView(Context context, Customer customer, String text){
        this.customer = customer;
        this.context = context;
        stbInfo.setText(text);
    }
}