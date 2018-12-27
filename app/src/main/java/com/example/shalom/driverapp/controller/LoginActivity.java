package com.example.shalom.driverapp.controller;

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
    EditText emailAddress;
    Button loginButton;
    Button signUpButton;
    CheckBox rememberMeCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberMeCheckBox =(CheckBox)findViewById(R.id.Remember_me_CheckBox);
        emailAddress = (EditText) findViewById(R.id.Email);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v == loginButton)
        {
            String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                    "\\@" +

                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                    "(" +

                    "\\." +

                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                    ")+";
            String email = emailAddress.getText().toString();
            Matcher matcher = Pattern.compile(validemail).matcher(email);
            if (matcher.matches()) {

                Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Enter Valid Email-Id", Toast.LENGTH_LONG).show();
            }

        }
        if (v== signUpButton)
        {


        }
    }

}
