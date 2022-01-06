package com.nic.sqlite.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.sqlite.NoteModel;
import com.nic.sqlite.R;
import com.nic.sqlite.SqliteHelepr;
import com.nic.sqlite.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    EditText user_name;
    TextInputEditText password;
    Button sign_in_btn;
    CheckBox remember;
    RelativeLayout sign_up_btn;
    RadioButton remember_me_btn;
    TextView forgot_password,sign_up;
    ArrayList<NoteModel> userList;
    String user_name_db="",user_password_db="",user_mobile_no;
    public  static SqliteHelepr database_helper;
    private PrefManager prefManager;
    byte[] user_profile;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE
    ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen);
        database_helper = new SqliteHelepr(LoginScreen.this);
        prefManager = new PrefManager(this);
        sign_in_btn =findViewById(R.id.sign_btn);
        sign_up_btn=findViewById(R.id.sign_up_btn);
        remember=findViewById(R.id.remember);
        user_name=findViewById(R.id.user_name_txt);
        password=findViewById(R.id.password);
        forgot_password=findViewById(R.id.forgot_txt);
        sign_in_btn.setOnClickListener(this);
        sign_up_btn.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        getUserDetails();
        checkPermissions();

        if(prefManager.getUserName()!=null&& !prefManager.getUserName().equals("")){
            if(prefManager.getUserPassword()!=null&& !prefManager.getUserPassword().equals("")){
                remember.setChecked(true);
                user_name.setText(prefManager.getUserName());
                password.setText(prefManager.getUserPassword());
            }
            else {
                remember.setChecked(false);
            }
        }
        else {
            remember.setChecked(false);
        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                }
                else {

                }
            }
        });
    }

    public boolean getUserDetails(){
        userList=new ArrayList<>();
        userList.addAll(database_helper.getUserDetails());
        if(userList.size()>0) {
            user_name_db = userList.get(0).getUser_name();
            user_password_db = userList.get(0).getUser_password();
            user_mobile_no = userList.get(0).getUser_mobile_no();
            user_profile = userList.get(0).getUser_profile();

            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_btn:
                checkLoginScreen();
                break;

            case R.id.sign_up_btn:
                gotoChessTimerAndRegisterScreen("SignUp");
                break;

            case R.id.forgot_txt:
                showForgotLayoutDialog();
                break;

        }

    }


    private  void  checkLoginScreen(){
        if(getUserDetails()){
            if(!user_name.getText().toString().equals("")){
                if(!password.getText().toString().equals("")){
                    if(user_name_db.equals(user_name.getText().toString())){
                        if(user_password_db.equals(password.getText().toString())){
                            if(remember.isChecked()){
                                prefManager.setUserName(user_name_db);
                                prefManager.setUserPassword(user_password_db);
                            }
                            else {
                                prefManager.setUserName("");
                                prefManager.setUserPassword("");

                            }
                            gotoChessTimerAndRegisterScreen("Login");

                        }
                        else {
                            Utils.showAlert(LoginScreen.this,"Please Enter Correct Password");
                        }
                    }
                    else {
                        Utils.showAlert(LoginScreen.this,"Please Enter Correct User Name");
                    }
                }
                else {
                    Utils.showAlert(LoginScreen.this,"Please Enter Password!");
                }
            }
            else {
                Utils.showAlert(LoginScreen.this,"Please Enter User Name!");
            }
        }
        else {
            Utils.showAlert(LoginScreen.this,"Please SignUp!");
        }

    }
    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        //finish();
                        return;
                    }
                }
                // all permissions were granted
                //initialize();
                break;
        }
    }

    public void gotoChessTimerAndRegisterScreen(String type){
        Intent intent=null;
        if(type.equals("Login")){
            intent=new Intent(LoginScreen.this, HomePage.class);
        }
        else if(type.equals("SignUp")){
            intent=new Intent(LoginScreen.this, RegistrationScreen.class);
        }
        startActivity(intent);
        //finish();
    }

    public void showForgotLayoutDialog(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.forgot_dialog_layout, null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setView(dialogView, 0, 0, 0, 0);
            alertDialog.setCancelable(false);
            alertDialog.show();




            Button btnOk = (Button) dialogView.findViewById(R.id.sign_up_btn);
            ImageView close= dialogView.findViewById(R.id.close_icon);
            EditText mobile_number = dialogView.findViewById(R.id.mobile);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getUserDetails()) {
                        if (!mobile_number.getText().toString().equals("")) {
                            if (user_mobile_no.equals(mobile_number.getText().toString())) {
                                checkForSmsPermission();
                                alertDialog.dismiss();
                            } else {
                                Utils.showAlert(LoginScreen.this, "Please Enter Registered Mobile Number!");
                            }
                        } else {
                            Utils.showAlert(LoginScreen.this, "Please Enter Mobile Number");
                        }
                    }
                    else {
                        Utils.showAlert(LoginScreen.this, "You Don't have a any account yet Please Register!");
                    }
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
        }

    }

    public void sendSMS(){
        //String otp_string=getSaltString();
        //Getting intent and PendingIntent instance
        /*Intent intent=new Intent(getApplicationContext(),OTPVerification.class);
        intent.putExtra("phone_number",mobile_number.getText().toString());
        intent.putExtra("otp_number",otp_string);
*/
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 1, null,0);

//Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms= SmsManager.getDefault();
        sms.sendTextMessage(user_mobile_no, null, "Your Password is "+user_password_db, pi,null);
        //startActivity(intent);

    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            //Log.d("TAG", getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {
            // Permission already granted. Enable the SMS button.
            //enableSmsButton();
            sendSMS();
        }
    }


}
