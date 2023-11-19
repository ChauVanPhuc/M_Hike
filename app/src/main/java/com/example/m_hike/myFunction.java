package com.example.m_hike;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class myFunction {

    //check empty
    public boolean checkEmpty(EditText input, String error){
        if (TextUtils.isEmpty(input.getText())){
            input.setError(error);
            return false;
        }
        return true;
    }

    public boolean checkEmpty(TextView input, String error){
        if (input.getText().toString().equals("")){
            input.setError(error);
            return false;
        }
        return true;
    }
    public boolean checkLength(TextView input, String error){
        if (input.getText().toString().length() > 50){
            input.setError(error);
            return false;
        }
        return true;
    }

    public boolean checkPhone(TextView input, String error){
        if (input.getText().toString().length() != 10){
            input.setError(error);
            return false;
        }
        return true;
    }
}
