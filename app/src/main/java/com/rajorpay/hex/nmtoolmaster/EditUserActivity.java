package com.rajorpay.hex.nmtoolmaster;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Models.IdPasswordVO;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.Utils.ValidationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditUserActivity extends AppCompatActivity {

    static Customer customer = null;
    IdPasswordVO idPasswordVO;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference custRef = database.getReference().child(NMToolConstants.CUSTOMER);
    final DatabaseReference idPassRef = database.getReference().child(NMToolConstants.PASSLIST);

    @BindView(R.id.edit_stb_number)
    EditText _stbNumber;
    @BindView(R.id.edit_user_search)
    ImageView searchButton;
    @BindView(R.id.edit_user_initial_text)TextView initialText;
    @BindView(R.id.edit_user_panel4)
    LinearLayout loadingPanel;
    @BindView(R.id.edit_user_no_record_found)TextView noRecordFound;
    @BindView(R.id.edit_user_scroll1)
    ScrollView infoPanel;

    @BindView(R.id.edit_user_name)
    EditText userName;
    @BindView(R.id.edit_user_phonenr)EditText phoneNr;
    @BindView(R.id.edit_user_locality)
    Spinner locality;
    @BindView(R.id.edit_user_address)EditText userAddress;
    @BindView(R.id.edit_user_box_number)TextView boxNumber;
    @BindView(R.id.edit_user_box_type)Spinner boxType;
    @BindView(R.id.edit_user_package)EditText packageAmount;
    @BindView(R.id.edit_user_id)TextView userId;
    @BindView(R.id.edit_user_password)TextView password;


    @BindView(R.id.edit_user_update)
    Button update;
    @BindView(R.id.edit_user_delete)Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit User Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateSTBNumber(_stbNumber.getText().toString())){
                    loadValue(_stbNumber.getText().toString());
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeConfirmation();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
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

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.setArchived(true);
                custRef.child(customer.getCustId()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditUserActivity.this, "data Archived", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
                loadValue(_stbNumber.getText().toString());
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
                if(customer != null){
                    if(validateAll(userName.getText().toString(), phoneNr.getText().toString(),boxNumber.getText().toString())) {
                        updateCustomerObj();
                        custRef.child(customer.getCustId()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditUserActivity.this, "data updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dialog.dismiss();
                loadValue(_stbNumber.getText().toString());
            }
        });
        dialog.show();
    }

    private void loadValue(String stbNumber){
        initialText.setVisibility(View.GONE);
        infoPanel.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);
        noRecordFound.setVisibility(View.GONE);
        customer = null;
        clearFields();
        Query query = custRef.orderByChild(NMToolConstants.BOX_NUMBER).equalTo(stbNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    customer = snapshot.getValue(Customer.class);
                }
                if(customer == null || customer.isArchived()){
                    loadingPanel.setVisibility(View.GONE);
                    noRecordFound.setVisibility(View.VISIBLE);
                }else{
                    populateValuesInEditText(customer);
                    loadingPanel.setVisibility(View.GONE);
                    infoPanel.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditUserActivity.this, "couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });

        idPasswordVO = null;
        query = idPassRef.orderByChild(NMToolConstants.ID).equalTo(stbNumber);
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
                Toast.makeText(EditUserActivity.this, "couldn't fetch Id Password Details",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateValuesInEditText(Customer customer){
        userName.setText(customer.getName());
        phoneNr.setText(customer.getPhoneNumber());
        ArrayAdapter<CharSequence> localityAdapter = ArrayAdapter.createFromResource(this, R.array.localities, android.R.layout.simple_spinner_item);
        localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int spinnerPosition = localityAdapter.getPosition(customer.getLocality());
        locality.setSelection(spinnerPosition);
        userAddress.setText(customer.getAddress());
        boxNumber.setText(customer.getBoxNumber());
        ArrayAdapter<CharSequence> boxTypeAdapter = ArrayAdapter.createFromResource(this, R.array.settopboxtype, android.R.layout.simple_spinner_item);
        spinnerPosition = boxTypeAdapter.getPosition(customer.getBoxType());
        boxType.setSelection(spinnerPosition);
        packageAmount.setText(customer.getPackageAmount());
    }

    private void updateCustomerObj(){
        customer.setName(userName.getText().toString());
        customer.setPhoneNumber(phoneNr.getText().toString());
        customer.setLocality(locality.getSelectedItem().toString());
        customer.setAddress(userAddress.getText().toString());
        customer.setBoxType(boxType.getSelectedItem().toString());
        customer.setPackageAmount(packageAmount.getText().toString());
    }

    private boolean validateSTBNumber(String stbNumber){
        if(stbNumber.equals("")){
            return false;
        }else return true;
    }

    public static void setCustomer(Customer customerObj) {
        customer = customerObj;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void clearFields(){
        userName.setText("");
        phoneNr.setText("");
        locality.setSelection(0);
        userAddress.setText("");
        boxNumber.setText("");
        boxType.setSelection(0);
        packageAmount.setText("");
    }

    private boolean validateAll(String userName, String phoneNo, String boxNumber) {
        String errorText ;
        String errorTextName = ValidationUtil.nameValidator(userName);
        String errorTextSTBNr = ValidationUtil.stbNrValidator(boxNumber);
        String errorTextPhonenr = ValidationUtil.phoneNrValidator(phoneNo);
        if(!TextUtils.equals(errorTextName,ValidationUtil.VALID)){
            Toast.makeText(EditUserActivity.this, errorTextName,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextSTBNr,ValidationUtil.VALID)){
            Toast.makeText(EditUserActivity.this, errorTextSTBNr,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextPhonenr,ValidationUtil.VALID)){
            Toast.makeText(EditUserActivity.this, errorTextPhonenr,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
