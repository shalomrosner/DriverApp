package com.example.shalom.driverapp.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shalom.driverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail;
    EditText etPassword;
    Button loginButton;
    Button signUpButton;
    CheckBox rememberMeCheckBox;
    SharedPreferences preferences;
    FirebaseAuth auth;
    public static final String mypreference = "MYPREFS";
    public static final String userNameS = "Username";
    public static final String passwordS = "password";
    public static final String rememberMeS = "rememberMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rememberMeCheckBox = (CheckBox) findViewById(R.id.remember_me_CheckBox);
        etEmail = (EditText) findViewById(R.id.username_s);
        etPassword = (EditText) findViewById(R.id.password_s);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        signUpButton = (Button) findViewById(R.id.sign_up_button_s);
        signUpButton.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        FetchData();
    }

    private boolean validateEmail() {
        String emailInput = etEmail.getText().toString();
        if (emailInput.isEmpty()) {
            etEmail.setError(getString(R.string.fill_email));
            //  id.setErrorEnabled(true);
            etEmail.requestFocus();
            Toast.makeText(this, getString(R.string.fill_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (!emailInput.contains("@")) {
            //  id.setErrorEnabled(true);
            // id.setError(getString(R.string.contains));
            etEmail.requestFocus();
            Toast.makeText(this, getString(R.string.contains), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //  email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = etPassword.getText().toString();
        if (passwordInput.isEmpty()) {
            etPassword.setError(getString(R.string.fill_password));
            //  etPassword.setErrorEnabled(true);
            etPassword.requestFocus();
            Toast.makeText(this, getString(R.string.fill_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (passwordInput.length() < 6) {
            etPassword.setError(getString(R.string.length_password));
            //  etPassword.setErrorEnabled(true);
            etPassword.requestFocus();
            Toast.makeText(this, getString(R.string.length_password), Toast.LENGTH_LONG).show();
            return false;
        } else {
            //    etPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void onClick(View v) {

        if (v == loginButton) {

            if (!validateEmail() || !validatePassword())
                return;

            auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                StoreData();
                                Intent loginScreen = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(loginScreen);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Username or Password are incorrect", Toast.LENGTH_LONG).show();
                            }

                        }


                    });


        }

        if (v == signUpButton) {
            Intent signUpScreen = new Intent(LoginActivity.this, Sign_upActivity.class);
            startActivity(signUpScreen);
        }
    }

    public void FetchData() {

        preferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        if (preferences.contains(userNameS) && preferences.contains(passwordS)) {
            if (preferences.getBoolean(rememberMeS, false))
            {
                etEmail.setText(preferences.getString(userNameS, ""));
                etPassword.setText(preferences.getString(passwordS, ""));

                loginButton.setEnabled(true);
            }
        } else {
            loginButton.setEnabled(false);
            return;
        }
    }

    public void StoreData() {

        if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
            String n = etEmail.getText().toString();
            String p = etPassword.getText().toString();
            boolean c = rememberMeCheckBox.isChecked();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(userNameS, n);
            editor.putString(passwordS, p);
            editor.putString(rememberMeS, p);
            editor.commit();

        }
    }
}



