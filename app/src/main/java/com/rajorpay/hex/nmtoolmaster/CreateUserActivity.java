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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.Models.IdPasswordVO;
import com.rajorpay.hex.nmtoolmaster.Utils.NMToolConstants;
import com.rajorpay.hex.nmtoolmaster.Utils.ValidationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateUserActivity extends AppCompatActivity {

    @BindView(R.id.sign_up_name) EditText signUpName;
    @BindView(R.id.sign_up_stbnr) EditText signUpSTBNr;
    @BindView(R.id.sign_up_package) EditText signUpPackage;
    @BindView(R.id.sign_up_phonenr) EditText signUpPhoneNr;
    @BindView(R.id.sign_up_password) EditText signUpPassword;
    @BindView(R.id.sign_up_address) EditText signUpAddress;
    @BindView(R.id.sign_up_locality) Spinner signUpLocality;
    @BindView(R.id.sign_up_submit) Button submit;
    @BindView(R.id.sign_up_stbType) Spinner signUpStbType;
    @BindView(R.id.sign_up_avatar) ImageView avatar;

    FirebaseAuth firebaseAuth;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Create New User");
        actionBar.setDisplayHomeAsUpEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAll()){
                    name = signUpName.getText().toString().trim();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                String email = signUpSTBNr.getText().toString().trim()+"@gmail.com";
                createUser(email, signUpPassword.getText().toString());
                }
            }
        });

        avatar.bringToFront();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void createUser(String email, final String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(CreateUserActivity.this, "Something Went Wrong. Please Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        Customer customer = new Customer(user.getUid(),
                            name, signUpSTBNr.getText().toString().trim(),
                            signUpStbType.getSelectedItem().toString().trim(),
                            signUpPhoneNr.getText().toString().trim(),
                            signUpPackage.getText().toString().trim(),
                            signUpLocality.getSelectedItem().toString().trim(),
                            signUpAddress.getText().toString().trim(), NMToolConstants.NOT_PAID);
                        FirebaseDatabase.getInstance().getReference().child("Customer").child(
                            user.getUid()).setValue(customer).addOnCompleteListener(CreateUserActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CreateUserActivity.this,
                                            "registered",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        IdPasswordVO idPasswordVO = new IdPasswordVO(
                            signUpSTBNr.getText().toString().trim(),
                            password
                        );
                        FirebaseDatabase.getInstance().getReference().child("PASS").child(
                            user.getUid()).setValue(idPasswordVO).addOnCompleteListener(CreateUserActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CreateUserActivity.this,
                                            "password saved",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        Intent i = new Intent(CreateUserActivity.this, MainActivity.class);
                        i.putExtra("user", user);
                        finishAffinity();
                        startActivity(i);
                    }
                }
            }
        });

    }

    private boolean validateAll() {
        String errorText ;
        String errorTextName = ValidationUtil.nameValidator(signUpName.getText().toString().trim());
        String errorTextSTBNr = ValidationUtil.stbNrValidator(signUpSTBNr.getText().toString().trim());
        String errorTextPhonenr = ValidationUtil.phoneNrValidator(signUpPhoneNr.getText().toString().trim());
        String errorTextPassword = ValidationUtil.passwordValidator(signUpPassword.getText().toString());
        if(!TextUtils.equals(errorTextName,ValidationUtil.VALID)){
            Toast.makeText(CreateUserActivity.this, errorTextName,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextSTBNr,ValidationUtil.VALID)){
            Toast.makeText(CreateUserActivity.this, errorTextSTBNr,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextPhonenr,ValidationUtil.VALID)){
            Toast.makeText(CreateUserActivity.this, errorTextPhonenr,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(errorTextPassword, ValidationUtil.VALID)){
            Toast.makeText(CreateUserActivity.this, errorTextPassword,Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(signUpAddress.getText().toString())){
            errorText = "Please Enter Valid address";
            Toast.makeText(CreateUserActivity.this, errorText,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
