package com.nic.sqlite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nic.sqlite.R;
import com.nic.sqlite.Utils.Utils;

public class NewLogInActivity extends AppCompatActivity {
    EditText user_name,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen_blue_purple_design);

        initializeLayout();
    }

    private void initializeLayout() {
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
    }

    public void gotoWebView(View signInBtn){
        if(checkLogInScreen()) {
            Intent gotoWebView = new Intent(NewLogInActivity.this, CameraActivity.class);
            startActivity(gotoWebView);
        }
    }

    public boolean checkLogInScreen(){
        if(!user_name.getText().toString().equals("")){
            if(!password.getText().toString().equals("")){
                return true;
            }
            else {
                Utils.showAlert(NewLogInActivity.this,"Please Enter User Password");
                return false;
            }
        }
        else {
            Utils.showAlert(NewLogInActivity.this,"Please Enter User Name");
            return false;
        }
    }
}
