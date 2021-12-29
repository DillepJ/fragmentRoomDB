package com.nic.sqlite.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.nic.sqlite.NoteModel;
import com.nic.sqlite.R;
import com.nic.sqlite.SqliteHelepr;
import com.nic.sqlite.Utils.Utils;
import com.skyhope.textrecognizerlibrary.TextScanner;
import com.skyhope.textrecognizerlibrary.callback.TextExtractCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nic.sqlite.SqliteHelepr.user_details_table;

public class RegistrationScreen extends AppCompatActivity {

    EditText user_name,dob,password,mobile_no;
    int year=0,day=0,month=0;
    Button sign_up_btn;
    ImageView edit_icon;
    CircleImageView profile_image;
    RelativeLayout profile_image_rl;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;
    public  static SqliteHelepr database_helper;
    String profile_string="";
    ArrayList<NoteModel> userList;
    int user_id_db;
    String user_name_db="",user_password_db="",user_mobile_no,user_dob_db;
    byte[] user_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        user_name=findViewById(R.id.username);
        dob=findViewById(R.id.dob);
        password=findViewById(R.id.password);
        mobile_no=findViewById(R.id.mobile);
        edit_icon=findViewById(R.id.edit_icon);
        sign_up_btn=findViewById(R.id.sign_up_btn);
        profile_image=findViewById(R.id.image_icon);
        profile_image_rl=findViewById(R.id.profile_img_rl);
        database_helper = new SqliteHelepr(RegistrationScreen.this);

        dob.setFocusable(false);
        getUserDetails();
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(0);
                pickdate();
            }
        });
        onIntialsetup();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name_=user_name.getText().toString();
                String dob_=dob.getText().toString();
                String password_=password.getText().toString();
                String mobile_no_=mobile_no.getText().toString();
                long id=0;
                if(profile_string.equals("captured")){
                    byte[] profile=imageViewToByte(profile_image);
                    if(!user_name.getText().toString().equals("")){
                        if(!mobile_no.getText().toString().equals("")) {
                            if(isValidMobile(mobile_no.getText().toString())) {
                                if (!dob.getText().toString().equals("")) {
                                    if (!password.getText().toString().equals("")) {

                                        if(getUserDetails().size()>0) {
                                            id = database_helper.updateUserDetails(user_name_, dob_, password_, profile, mobile_no_,user_id_db);
                                            if (id > 0) {
                                                Toast.makeText(RegistrationScreen.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }
                                        else {
                                            id = database_helper.insertUserDetails(user_name_, dob_, password_, profile, mobile_no_);
                                            if (id > 0) {
                                                Toast.makeText(RegistrationScreen.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }

                                        //saveUserDetails(profile);
                                    } else {
                                        Utils.showAlert(RegistrationScreen.this, "Please Enter Password!");
                                    }

                                } else {
                                    Utils.showAlert(RegistrationScreen.this, "Please Select Date of Birth!");
                                }
                            }
                            else {
                                Utils.showAlert(RegistrationScreen.this,"Please Enter Valid Mobile Number!");
                            }
                        }
                        else {
                            Utils.showAlert(RegistrationScreen.this,"Please Enter Mobile Number!");
                        }
                    }
                    else {
                        Utils.showAlert(RegistrationScreen.this,"Please Enter User Name!");
                    }
                }
                else {
                    Utils.showAlert(RegistrationScreen.this,"Please Capture Your Image!");
                }

            }
        });


        profile_image_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });


    }

    public void openCamera(){
        if (ContextCompat.checkSelfPermission(RegistrationScreen.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    /*Toast.makeText(RegistrationScreen.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();*/
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            requestStoragePermission();
        }
    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            dob.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };

    public void pickdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                photo = (Bitmap) data.getExtras().get("data");
                profile_image.setImageBitmap(photo);
                edit_icon.setVisibility(View.VISIBLE);
                profile_string = "captured";
                getTextFromBitmap(photo);
            }
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String ima = new String(byteArray);
        String image3 = ima;
        return byteArray;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RegistrationScreen.this,
                                    new String[]{Manifest.permission.CAMERA}, 100);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    public void saveUserDetails(byte[] profile){
        long id=0;
        SQLiteDatabase sqLiteDatabase = database_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user_name.getText().toString());
        values.put("dob",dob.getText().toString());
        values.put("password",password.getText().toString());
        values.put("profile_img",profile);

        //inserting new row
        id= sqLiteDatabase.insert(user_details_table, null , values);
        if(id>0){
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        //close database connection
        sqLiteDatabase.close();

    }

    public boolean isValidMobile(String phone2) {
        boolean check= false;
        boolean startDigitCheck = false;
        boolean sameDigitCheck= false;
        String[] startDigit=new String[] {"0","1","2","3","4","5"};
        String[] sameDigit=new String[] {"6666666666","7777777777","8888888888","9999999999"};
        for(int i=0;i<startDigit.length;i++){
            if(phone2.startsWith(startDigit[i])){
                startDigitCheck=false;
                return false;
            }else {
                startDigitCheck=true;
            }
        }

        if(startDigitCheck){
            for(int i=0;i<sameDigit.length;i++){
                if(phone2.equals(sameDigit[i])){
                    sameDigitCheck=false;
                    return false;
                }else {
                    sameDigitCheck=true;
                }
            }

        }else {
            return  false;
        }
        if(sameDigitCheck){
            check = phone2.length() == 10;
        }
        else {
            return  false;
        }
        if(check){
            return check;
        }else {
            return  false;
        }

    }

    public ArrayList<NoteModel> getUserDetails(){
        userList=new ArrayList<>();
        userList.addAll(database_helper.getUserDetails());
        if(userList.size()>0) {

            return userList;
        }
        else {
            return userList;
        }
    }

    public void onIntialsetup(){
        if(userList.size()>0) {
            user_id_db = userList.get(0).getUser_id();
            user_name_db = userList.get(0).getUser_name();
            user_password_db = userList.get(0).getUser_password();
            user_dob_db = userList.get(0).getUser_dob();
            user_mobile_no = userList.get(0).getUser_mobile_no();
            user_profile = userList.get(0).getUser_profile();
            profile_string = "captured";
            profile_image.setImageBitmap(getImage(user_profile));
            user_name.setText(user_name_db);
            password.setText(user_password_db);
            mobile_no.setText(user_mobile_no);
            dob.setText(user_dob_db);

        }
        else {
            profile_string = "";
            profile_image.setImageResource(R.drawable.dslr_camera);
            user_name.setText("");
            password.setText("");
            mobile_no.setText("");
            dob.setText("");

        }
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public void getTextFromBitmap(Bitmap bitmap) {
        /*TextScanner.getInstance(this)
                .init()
                .load(uri) // uri or bitmap
                .getCallback(new TextExtractCallback() {
                    @Override
                    public void onGetExtractText(List<String> textList) {
                        // Here you will get list of text
                        Log.d("Text Is>> ",""+textList);

                    }
                });*/
        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = recognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            //get text from sb until there is no text
            for (int i = 0; i < items.size(); i++) {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            //set text to edit text
            Log.d("Text Is>> ",""+sb.toString());
            //mResultEt.setText(sb.toString());
        }
    }


}
