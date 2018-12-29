package com.example.shalom.driverapp.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shalom.driverapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName;
    EditText etPassword;
    Button loginButton;
    Button signUpButton;
    CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberMeCheckBox = (CheckBox) findViewById(R.id.remember_me_CheckBox);
        userName = (EditText) findViewById(R.id.username_s);
        etPassword = (EditText) findViewById(R.id.password_s);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        signUpButton = (Button) findViewById(R.id.sign_up_button_s);
        signUpButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v == loginButton) {

            String user = userName.getText().toString();
            String password = etPassword.getText().toString();


            SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);

            //String savedPassword = preferences.getString(password, "");
            //String savedUserName = preferences.getString(user, "");

            String userDetails = preferences.getString(user + password + "data", "No information on that user.");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("display", userDetails);
            editor.commit();

            Intent displayScreen = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(displayScreen);
        }
        if (v == signUpButton) {
            Intent signUpScreen = new Intent(LoginActivity.this, Sign_upActivity.class);
            startActivity(signUpScreen);
        }
    }


}


