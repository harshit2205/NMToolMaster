package com.rajorpay.hex.nmtoolmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajorpay.hex.nmtoolmaster.ComplaintInfoActivity;
import com.rajorpay.hex.nmtoolmaster.Models.ComplaintVO;
import com.rajorpay.hex.nmtoolmaster.R;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintHolder> {
    private List<ComplaintVO> complaints;
    private Context context;

    public ComplaintAdapter(List<ComplaintVO> complaints, Context context) {
        this.complaints = complaints;
        this.context = context;
    }

    public void setComplaints(List<ComplaintVO> complaints) {
        this.complaints = complaints;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.complaint_holder, viewGroup, false);
        return new ComplaintHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder complaintHolder, int i) {
        ComplaintVO complaintVO = complaints.get(i);
        complaintHolder.bindView(complaintVO,context);
    }

    @Override
    public int getItemCount() {
            return complaints.size();
    }
}

class ComplaintHolder extends RecyclerView.ViewHolder{

    private TextView stbnr;
    private TextView status;
    private TextView assignedTo;
    private TextView description;
    private TextView dateOfComplaint;
    private ImageView holderInfo;

    public ComplaintHolder(@NonNull View itemView) {
        super(itemView);
        holderInfo = itemView.findViewById(R.id.holder_info);
        dateOfComplaint = itemView.findViewById(R.id.date_of_complaint);
        stbnr = itemView.findViewById(R.id.holder_stbnr);
        status = itemView.findViewById(R.id.holder_complaint_status);
        assignedTo = itemView.findViewById(R.id.holder_assigned_to);
        description = itemView.findViewById(R.id.holder_description);
    }

    public void bindView(final ComplaintVO complaintVO, final Context context){
        Long inTime = Long.parseLong(complaintVO.getInTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(inTime);

        dateOfComplaint.setText(sdf.format(resultdate));
        stbnr.setText(complaintVO.getBoxNumber());
        status.setText(complaintVO.getStatus());
        if(status.getText().toString().equals(NMToolConstants.STATUS_RAISED)){
            status.setTextColor(Color.parseColor("#878787"));
        }else if(status.getText().toString().equals(NMToolConstants.STATUS_APPROVED)){
            status.setTextColor(Color.parseColor("#FFCC00"));
        }else if(status.getText().toString().equals(NMToolConstants.STATUS_ASSIGNED)){
            status.setTextColor(Color.parseColor("#3AC871"));
        }else if(status.getText().toString().equals(NMToolConstants.STATUS_COMPLETED)){
            status.setTextColor(Color.parseColor("#000000"));
        }
        assignedTo.setText(complaintVO.getAssignedTo());
        if(!TextUtils.isEmpty(complaintVO.getDescription())){
            description.setVisibility(View.VISIBLE);
            description.setText(complaintVO.getDescription());
        }
        holderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ComplaintInfoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComplaintInfoActivity.infoInitialiser(complaintVO);
                context.startActivity(i);
            }
        });
    }

}
