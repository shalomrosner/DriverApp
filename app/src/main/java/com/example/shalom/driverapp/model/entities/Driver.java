package com.example.shalom.driverapp.model.entities;

import android.location.Location;

import com.example.shalom.driverapp.R;

public class Driver {
    String fullName;
    String id;
    String celNumber;
    String email;
    String bankAccountNumber;
    String password;
    Location currentLocation;

    public Driver(String fullName, String id, String celNumber, String email, String bankAccountNumber,String password,Location currentLocation) {
        this.fullName = fullName;
        this.id = id;
        this.celNumber = celNumber;
        this.email = email;
        this.bankAccountNumber = bankAccountNumber;
        this.password= password;
        this.currentLocation= currentLocation;


    }
    public Driver(){
        this.fullName = "";
        this.id = "";
        this.celNumber = "";
        this.email = "";
        this.bankAccountNumber = "";
        this.password = "";
    }
    public String getFullName() {

        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws Exception {
        if(id.length()==9)
            if (IDCheck(id))
                this.id = id;
            else
                throw new Exception (String.valueOf(R.string.Extract_id));
        else
            throw new Exception(String.valueOf(R.string.length_id));
    }
    public static boolean IDCheck(String strID)
    {
        int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        int count = 0;
        if (strID == null)
            return false;
        for (int i = 0; i < 9; i++)
        {
            int num = Integer.parseInt(strID.substring(i, i+1)) * id_12_digits[i];
            if (num > 9)
                num = (num / 10) + (num % 10);
            count += num;
        }
        return (count % 10 == 0);
    }

    public String getCelNumber() {
        return celNumber;
    }

    public void setCelNumber(String celNumber) throws Exception {
        if (celNumber.length() == 9 || celNumber.length() == 10)
            this.celNumber = celNumber;

        else
            throw new Exception(String.valueOf(R.string.length_phone));
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if(password.length()>=6)
            this.password = password;
        else
            throw new Exception(String.valueOf(R.string.length_password));
    }

}

