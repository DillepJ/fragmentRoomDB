package com.nic.sqlite.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.sqlite.Adapetr.ContactListAdapter;
import com.nic.sqlite.ImageZoom.ImageMatrixTouchHandler;
import com.nic.sqlite.NoteModel;
import com.nic.sqlite.R;
import com.nic.sqlite.SqliteHelepr;
import com.nic.sqlite.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactList extends AppCompatActivity {

    RelativeLayout add_btn;
    RelativeLayout no_data_gif;
    RecyclerView contact_recycler;
    RelativeLayout list_item_rl;
    public  static SqliteHelepr database_helper;
    ArrayList<NoteModel> contactList;
    CircleImageView image_icon;
    EditText name;
    EditText number;
    RelativeLayout date;
    RelativeLayout profile_img_rl;
    TextView date_text;
    Button submit;
    ScrollView scroll_details_view;
    ImageView back;

    String profile_string="";
    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;
    int year=0,day=0,month=0;

    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        database_helper = new SqliteHelepr(ContactList.this);
        add_btn =findViewById(R.id.add_btn);
        no_data_gif =findViewById(R.id.no_data_found);
        scroll_details_view =findViewById(R.id.scroll_details_view);
        image_icon = findViewById(R.id.image_icon);
        name = findViewById(R.id.username);
        number = findViewById(R.id.mobile);
        date = findViewById(R.id.date_rl);
        date_text = findViewById(R.id.date_text);
        submit = findViewById(R.id.submit_btn);
        list_item_rl = findViewById(R.id.list_item_rl);
        contact_recycler = findViewById(R.id.contact_recycler);
        profile_img_rl = findViewById(R.id.profile_img_rl);
        back = findViewById(R.id.back);

        contact_recycler.setLayoutManager(new LinearLayoutManager(this));
        contact_recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        no_data_gif.setVisibility(View.GONE);
        list_item_rl.setVisibility(View.GONE);
        scroll_details_view.setVisibility(View.GONE);

        getUserDetails();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_details_view.setVisibility(View.VISIBLE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtnContact();
            }
        });
        profile_img_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openCamera();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    public void getUserDetails(){
        contactList =new ArrayList<>();
        contactList.addAll(database_helper.getContactDetails());
        if(contactList.size()>0) {
            list_item_rl.setVisibility(View.VISIBLE);
            no_data_gif.setVisibility(View.GONE);
            scroll_details_view.setVisibility(View.GONE);
            contactListAdapter = new ContactListAdapter(contactList,this);
            contact_recycler.setAdapter(contactListAdapter);
        }
        else {
            no_data_gif.setVisibility(View.VISIBLE);
            list_item_rl.setVisibility(View.GONE);
            scroll_details_view.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                photo = (Bitmap) data.getExtras().get("data");
                image_icon.setImageBitmap(photo);
                profile_string = "captured";
                //getTextFromBitmap(photo);
            }
        }
    }


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

                        date_text.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    public void openCamera(){
        if (ContextCompat.checkSelfPermission(ContactList.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    /*Toast.makeText(RegistrationScreen.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();*/
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            requestStoragePermission();
        }
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
                            ActivityCompat.requestPermissions(ContactList.this,
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
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String ima = new String(byteArray);
        String image3 = ima;
        return byteArray;
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

    public void addBtnContact(){
        String user_name_=name.getText().toString();
        String dob_=date_text.getText().toString();
        String mobile_no_=number.getText().toString();
        long id=0;
        if(profile_string.equals("captured")){
            byte[] profile=imageViewToByte(image_icon);
            if(!user_name_.equals("")){
                if(!mobile_no_.equals("")) {
                    if(isValidMobile(mobile_no_)) {
                        if (!dob_.equals("")) {
                                     id = database_helper.insertContactDetails(user_name_, mobile_no_, dob_, profile);
                                    if (id > 0) {
                                        Toast.makeText(ContactList.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                        getUserDetails();
                                        name.setText("");
                                        date_text.setText("");
                                        number.setText("");
                                        image_icon.setImageResource(R.drawable.dslr_camera);

                                    }

                        } else {
                            Utils.showAlert(ContactList.this, "Please Select Date of Birth!");
                        }
                    }
                    else {
                        Utils.showAlert(ContactList.this,"Please Enter Valid Mobile Number!");
                    }
                }
                else {
                    Utils.showAlert(ContactList.this,"Please Enter Mobile Number!");
                }
            }
            else {
                Utils.showAlert(ContactList.this,"Please Enter User Name!");
            }
        }
        else {
            Utils.showAlert(ContactList.this,"Please Capture Your Image!");
        }
    }

    public void deleteParticularContact(int position){
        removeSingleContact(contactList.get(position).getUser_id());
    }
    public void removeSingleContact(int title) {
        //Open the database
        SQLiteDatabase database = database_helper.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + SqliteHelepr.user_contacts_table + " WHERE " + "user_id" + "= '" + title + "'");

        //Close the database
        database.close();
        getUserDetails();
        contactListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(scroll_details_view.getVisibility()==View.VISIBLE){
            scroll_details_view.setVisibility(View.GONE);
        }
        else {
            finish();
        }
    }
}
