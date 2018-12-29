package com.example.shalom.driverapp.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.entities.Driver;

public class Sign_upActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText userName = (EditText) findViewById(R.id.Username);
        final EditText password = (EditText) findViewById(R.id.Password);
        final EditText email = (EditText) findViewById(R.id.E_mail);
        final EditText phoneNumber = (EditText) findViewById(R.id.Phone_number);
        final EditText bankNumber = (EditText) findViewById(R.id.Bank_account);
        final EditText firstName = (EditText) findViewById(R.id.First_name);
        final EditText lastName = (EditText) findViewById(R.id.Last_name);
        final EditText id = (EditText) findViewById(R.id.ID);
        Button btnRegister = (Button) findViewById(R.id.Sign_up_button);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Driver driver=new Driver();
                SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
                String newUser = userName.getText().toString();
                String newPassword = password.getText().toString();
                String newEmail = email.getText().toString();
                String newId = id.getText().toString();
                String newPhoneNumber = phoneNumber.getText().toString();
                String newBankAccount = bankNumber.getText().toString();
                String newFirstName = firstName.getText().toString();
                String newLastName = lastName.getText().toString();

                driver.setFirstName(newFirstName);
                driver.setLastName(newLastName);
                driver.setId(newId);
                driver.setEmail(newEmail);
                driver.setCelNumber(newPhoneNumber);
                driver.setBankAccountNumber(newBankAccount);
                DBManagerFactory.getBL().addDriver(driver);

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("Username", newUser);
                editor.putString("First name", newFirstName);
                editor.putString("Last name", newLastName);
                editor.putString("ID", newId);
                editor.putString("E-Mail", newEmail);
                editor.putString("Phone number", newPhoneNumber);
                editor.putString("Bank account", newBankAccount);
                editor.putString("Password", newPassword);
                editor.commit();

                Toast.makeText(getApplicationContext(), "Values where saved", Toast.LENGTH_LONG).show();

                Intent loginScreen = new Intent(Sign_upActivity.this, LoginActivity.class);
                startActivity(loginScreen);
            }
        });

    }

}


