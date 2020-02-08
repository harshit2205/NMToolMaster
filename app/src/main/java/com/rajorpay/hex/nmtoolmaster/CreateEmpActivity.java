package com.rajorpay.hex.nmtoolmaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.rajorpay.hex.nmtoolmaster.Models.EmployeeVO;
import com.rajorpay.hex.nmtoolmaster.Models.IdPasswordVO;
import com.rajorpay.hex.nmtoolmaster.Utils.ValidationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEmpActivity extends AppCompatActivity {

    @BindView(R.id.sign_up_name)
    EditText _name;
    @BindView(R.id.sign_up_code)EditText _employeeCode;
    @BindView(R.id.sign_up_phonenr)EditText _phoneNr;
    @BindView(R.id.sign_up_password)EditText _password;
    @BindView(R.id.sign_up_designation)EditText _designation;
    @BindView(R.id.sign_up_salery)EditText _salery;
    @BindView(R.id.sign_up_submit)
    Button submit;

    FirebaseAuth firebaseAuth;
    String name = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_emp);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Create New Employee");
        actionBar.setDisplayHomeAsUpEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAll()){

                    name = _name.getText().toString().trim();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    String email = _employeeCode.getText().toString().trim()+"@gmail.com";
                    createUser(email, _password.getText().toString());
                }
            }
        });
    }

    private void createUser(String email, final String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user == null) {
                                Toast.makeText(CreateEmpActivity.this, "Something Went Wrong. Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {
                                EmployeeVO employee = new EmployeeVO(
                                        _name.getText().toString(),
                                        _employeeCode.getText().toString(),
                                        _phoneNr.getText().toString(),
                                        _password.getText().toString(),
                                        _designation.getText().toString(),
                                        Integer.parseInt(_salery.getText().toString())
                                );
                                FirebaseDatabase.getInstance().getReference().child("Employee").child(
                                        user.getUid()).setValue(employee).addOnCompleteListener(CreateEmpActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(CreateEmpActivity.this,
                                                        "registered",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                IdPasswordVO idPasswordVO = new IdPasswordVO(
                                        _name.getText().toString().trim(),
                                        _password.getText().toString()
                                );
                                FirebaseDatabase.getInstance().getReference().child("PASS").child(
                                        user.getUid()).setValue(idPasswordVO).addOnCompleteListener(CreateEmpActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(CreateEmpActivity.this,
                                                        "password saved",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Intent i = new Intent(CreateEmpActivity.this, MainActivity.class);
                                i.putExtra("user", user);
                                finishAffinity();
                                startActivity(i);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validateAll() {
        String errorTextName = ValidationUtil.nameValidator(_name.getText().toString().trim());
        String errorTextPhonenr = ValidationUtil.phoneNrValidator(_phoneNr.getText().toString().trim());
        String errorTextPassword = ValidationUtil.passwordValidator(_password.getText().toString());
        if(!TextUtils.equals(errorTextName,ValidationUtil.VALID)){
            Toast.makeText(this, errorTextName,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextPhonenr,ValidationUtil.VALID)){
            Toast.makeText(this, errorTextPhonenr,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextPassword, ValidationUtil.VALID)){
            Toast.makeText(this, errorTextPassword,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
