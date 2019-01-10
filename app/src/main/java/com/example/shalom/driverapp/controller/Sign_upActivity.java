package com.example.shalom.driverapp.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.entities.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.shalom.driverapp.model.entities.Driver.IDCheck;

public class Sign_upActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password;
    EditText email;
    EditText phoneNumber;
    EditText bankNumber;
    EditText fullName;
    EditText id;
    Button btnRegister;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        password = (EditText) findViewById(R.id.Password);
        email = (EditText) findViewById(R.id.E_mail);
        phoneNumber = (EditText) findViewById(R.id.Phone_number);
        bankNumber = (EditText) findViewById(R.id.Bank_account);
        fullName = (EditText) findViewById(R.id.Full_name);
        id = (EditText) findViewById(R.id.ID);
        btnRegister = (Button) findViewById(R.id.Sign_up_button);
        btnRegister.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString();
        if (emailInput.isEmpty()) {
            email.setError(getString(R.string.fill_email));
            //  id.setErrorEnabled(true);
            email.requestFocus();
            Toast.makeText(this, getString(R.string.fill_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (!emailInput.contains("@")) {
            //  id.setErrorEnabled(true);
            id.setError(getString(R.string.contains));
            email.requestFocus();
            Toast.makeText(this, getString(R.string.contains), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //  email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getText().toString();
        if (passwordInput.isEmpty()) {
            password.setError(getString(R.string.fill_password));
            // password.setErrorEnabled(true);
            password.requestFocus();
            Toast.makeText(this, getString(R.string.fill_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (passwordInput.length() < 6) {
            password.setError(getString(R.string.length_password));
            //    password.setErrorEnabled(true);
            password.requestFocus();
            Toast.makeText(this, getString(R.string.length_password), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //     password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateId() {
        String IdNumberInput = id.getText().toString();
        if (IdNumberInput.isEmpty()) {
            id.setError(getString(R.string.fill_id));
            //    id.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this, getString(R.string.fill_id), Toast.LENGTH_LONG).show();
            return false;
        } else if (!IDCheck(IdNumberInput)) {
            id.setError(getString(R.string.Extract_id));
            //   id.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this, getString(R.string.Extract_id), Toast.LENGTH_LONG).show();
            return false;
        } else if (IdNumberInput.length() != 9) {
            id.setError(getString(R.string.length_id));
            //id.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this, getString(R.string.length_id), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //id.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFullName() {
        String UserNameInput = fullName.getText().toString();
        if (UserNameInput.isEmpty()) {
            fullName.setError(getString(R.string.fill_userName));
            //  fullName.setErrorEnabled(true);
            fullName.requestFocus();
            Toast.makeText(this, getString(R.string.fill_userName), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //   fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCreditCard() {
        String creditCardInput = bankNumber.getText().toString();
        if (creditCardInput.isEmpty()) {
            bankNumber.setError(getString(R.string.fill_creditCard));
            //   bankNumber.setErrorEnabled(true);
            bankNumber.requestFocus();
            Toast.makeText(this, getString(R.string.fill_creditCard), Toast.LENGTH_LONG).show();
            return false;
        } else {
            // bankNumber.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = phoneNumber.getText().toString();
        if (phoneInput.isEmpty()) {
            phoneNumber.setError(getString(R.string.fill_phone));
            phoneNumber.requestFocus();
            //   phoneNumber.setErrorEnabled(true);
            Toast.makeText(this, getString(R.string.fill_phone), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (phoneInput.length() != 9 && phoneInput.length() != 10) {
            phoneNumber.setError(getString(R.string.length_phone));
            phoneNumber.requestFocus();
            //   phoneNumber.setErrorEnabled(true);
            Toast.makeText(this, getString(R.string.length_phone), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //  phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateFullName() || !validateId() || !validatePhone() || !validateEmail() || !validateCreditCard() || !validatePassword())
            return;
        else
            signUp();
    }

    public void onClick(View v) {
        if (v == btnRegister) {
            confirmInput(v);
        }
    }

    private void signUp() {
        final Driver driver = new Driver();
        String newPassword = password.getText().toString();
        String newEmail = email.getText().toString();
        String newId = id.getText().toString();
        String newPhoneNumber = phoneNumber.getText().toString();
        String newBankAccount = bankNumber.getText().toString();
        String newFullName = fullName.getText().toString();
        driver.setFullName(newFullName);
        try {
            driver.setId(newId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.setEmail(newEmail);
        try {
            driver.setCelNumber(newPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.setBankAccountNumber(newBankAccount);
        try {
            driver.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    return DBManagerFactory.getBL().addDriver(driver);
                                }
                            }.execute();
                            Intent loginScreen = new Intent(Sign_upActivity.this, LoginActivity.class);
                            startActivity(loginScreen);
                        }
                        else
                        {
                            Toast.makeText(Sign_upActivity.this, "Error in firebase", Toast.LENGTH_SHORT).show();
                        }

                    }


                });


    }
}