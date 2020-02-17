package com.rajorpay.hex.nmtoolmaster;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rajorpay.hex.nmtoolmaster.Models.EmployeeVO;
import com.rajorpay.hex.nmtoolmaster.Models.IdPasswordVO;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEmpActivity extends AppCompatActivity {

    final DatabaseReference empRef = FirebaseDatabase.getInstance().getReference().child(NMToolConstants.EMPLOYEE);
    final DatabaseReference idPassRef = FirebaseDatabase.getInstance().getReference().child(NMToolConstants.PASSLIST);
    EmployeeVO employeeVO = null;
    IdPasswordVO idPasswordVO = null;

    @BindView(R.id.edit_emp_code)EditText _empCode;
    @BindView(R.id.edit_emp_search)
    ImageView _search;
    @BindView(R.id.edit_emp_name)
    EditText _empName;
    @BindView(R.id.edit_emp_phonenr)EditText _empPhoneNr;
    @BindView(R.id.edit_emp_designation)EditText _empDesignation;
    @BindView(R.id.edit_emp_salary)EditText _empSalary;
    @BindView(R.id.edit_emp_id)
    TextView userId;
    @BindView(R.id.edit_emp_password)TextView password;
    @BindView(R.id.edit_emp_update)
    Button updateEmp;
    @BindView(R.id.edit_emp_delete)Button deleteEmp;

    @BindView(R.id.edit_emp_scroll1)
    ScrollView infoPanel;
    @BindView(R.id.edit_emp_panel4)
    LinearLayout loadingPanel;
    @BindView(R.id.edit_emp_no_record_found)TextView noRecordFound;
    @BindView(R.id.edit_emp_initial_text)TextView initialText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emp);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Employee Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadValue(_empCode.getText().toString());
            }
        });

        updateEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeConfirmation();
            }
        });

        deleteEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
            }
        });
    }

    private void deleteConfirmation(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirm_delete_dialog);
        dialog.setTitle("Delete User Details");

        // set the custom dialog components - text, image and button
        Button confirm = dialog.findViewById(R.id.dialog_confirm);
        Button dismiss = dialog.findViewById(R.id.dialog_dismiss);
        TextView infoText = dialog.findViewById(R.id.dialog_info_text);

        infoText.setText("Are You Sure to Delete the Employee Details?\\n Data will be archived.");

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeVO.setArchived(true);
                empRef.child(employeeVO.getEmpId()).setValue(employeeVO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditEmpActivity.this, "data Archived", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
                loadValue(_empCode.getText().toString());
            }
        });
        dialog.show();
    }

    private void takeConfirmation(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.setTitle("Update User Details");

        // set the custom dialog components - text, image and button
        Button confirm = dialog.findViewById(R.id.dialog_confirm);
        Button dismiss = dialog.findViewById(R.id.dialog_dismiss);


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeVO != null){
                        updateEmployeeObj(employeeVO);
                        empRef.child(employeeVO.getEmpId()).setValue(employeeVO).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditEmpActivity.this, "data updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                dialog.dismiss();
                loadValue(_empCode.getText().toString());
                }
        });
        dialog.show();
    }

    private void loadValue(String empCode){
        initialText.setVisibility(View.GONE);
        infoPanel.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);
        noRecordFound.setVisibility(View.GONE);
        employeeVO = null;
        clearFields();
        Log.d("NM_TOOLstr",NMToolConstants.EMP_ID +" : "+empCode);
        Query query = empRef.orderByChild(NMToolConstants.EMP_ID).equalTo(empCode);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    employeeVO = snapshot.getValue(EmployeeVO.class);
                }
                if(employeeVO == null || employeeVO.isArchived()){
                    loadingPanel.setVisibility(View.GONE);
                    noRecordFound.setVisibility(View.VISIBLE);
                }else{
                    populateValuesInEditText(employeeVO);
                    loadingPanel.setVisibility(View.GONE);
                    infoPanel.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditEmpActivity.this, "couldn't fetch Employee Details",Toast.LENGTH_SHORT).show();
            }
        });

        idPasswordVO = null;
        query = idPassRef.orderByChild(NMToolConstants.ID).equalTo(empCode);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    idPasswordVO = snapshot.getValue(IdPasswordVO.class);
                }
                if(idPasswordVO == null){
                    userId.setText("Not saved");
                    password.setText("Not saved");
                }else{
                    userId.setText(idPasswordVO.getId());
                    password.setText(idPasswordVO.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditEmpActivity.this, "couldn't fetch Id Password Details",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateValuesInEditText(EmployeeVO employeeVO){
        _empName.setText(employeeVO.getName());
        _empPhoneNr.setText(employeeVO.getPhoneNr());
        _empDesignation.setText(employeeVO.getDesignation());
        _empSalary.setText(Integer.toString(employeeVO.getSalery()));
    }

    private void clearFields(){
        _empName.setText("");
        _empPhoneNr.setText("");
        _empDesignation.setText("");
        _empSalary.setText("");
    }

    private void updateEmployeeObj(EmployeeVO employeeVO) {
        employeeVO.setName(_empName.getText().toString());
        employeeVO.setPhoneNr(_empPhoneNr.getText().toString());
        employeeVO.setDesignation(_empDesignation.getText().toString());
        employeeVO.setSalery(Integer.parseInt(_empSalary.getText().toString()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
