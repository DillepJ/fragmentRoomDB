package com.nic.sqlite.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.sqlite.ImageZoom.ImageMatrixTouchHandler;
import com.nic.sqlite.NoteModel;
import com.nic.sqlite.R;
import com.nic.sqlite.SqliteHelepr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    CircleImageView profile_image;
    GifImageView gifImageView;
    TextView user_name;
    RelativeLayout game_assistance_rl,qr_reader_rl,cricket_score_rl,mind_game_rl,phone_pe_rl,my_contact_rl;
    public  static SqliteHelepr database_helper;
    PrefManager prefManager;

    byte[] user_profile;
    ArrayList<NoteModel> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        prefManager = new PrefManager(this);
        database_helper = new SqliteHelepr(HomePage.this);
        profile_image=findViewById(R.id.profile);
        back=findViewById(R.id.back_icon);
        gifImageView=findViewById(R.id.gif_view);
        user_name=findViewById(R.id.user_name);
        qr_reader_rl=findViewById(R.id.qr_rl);
        game_assistance_rl =findViewById(R.id.game_assistance);
        cricket_score_rl=findViewById(R.id.cricket_score_rl);
        phone_pe_rl=findViewById(R.id.phone_pe_rl);
        mind_game_rl=findViewById(R.id.mind_game_rl);
        my_contact_rl=findViewById(R.id.my_contact_rl);
        getUserDetails();
        game_assistance_rl.setOnClickListener(this::onClick);
        qr_reader_rl.setOnClickListener(this::onClick);
        cricket_score_rl.setOnClickListener(this::onClick);
        mind_game_rl.setOnClickListener(this::onClick);
        phone_pe_rl.setOnClickListener(this::onClick);
        my_contact_rl.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        getTimeMange();
        if(getUserDetails()) {
            profile_image.setImageBitmap(getImage(user_profile));
        }
        user_name.setText(prefManager.getUserName());

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandedImage(profile_image.getDrawable());
            }
        });
    }
    public boolean getUserDetails(){
        userList=new ArrayList<>();
        userList.addAll(database_helper.getUserDetails());
        if(userList.size()>0) {
            user_profile = userList.get(0).getUser_profile();
            return true;
        }
        else {
            return false;
        }
    }

    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageDrawable(profile);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(this);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(ImagePopupLayout);

            final AlertDialog alert = dialogBuilder.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_zoomInOut;
            alert.show();
            alert.getWindow().setBackgroundDrawableResource(R.color.full_transparent);
            alert.setCanceledOnTouchOutside(true);

            zoomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_icon:
                onBackPressed();
                break;

            case R.id.qr_rl:
                gotoChessTimerAndRegisterScreen("QR");
                break;

            case R.id.game_assistance:
                gotoChessTimerAndRegisterScreen("CHESS");
                break;

            case R.id.cricket_score_rl:
                gotoChessTimerAndRegisterScreen("CRICKET");
                break;

            case R.id.mind_game_rl:
                gotoChessTimerAndRegisterScreen("MIND_GAME");
                break;

            case R.id.phone_pe_rl:
                gotoChessTimerAndRegisterScreen("phone_pe_rl");
                break;

            case R.id.my_contact_rl:
                gotoChessTimerAndRegisterScreen("my_contact_rl");
                break;
        }

    }

    public void gotoChessTimerAndRegisterScreen(String type){
        Intent intent=null;
        if(type.equals("CHESS")){
            intent=new Intent(HomePage.this, ChessTimer.class);
        }
        else if(type.equals("QR")){
            intent=new Intent(HomePage.this, QRCodeReaderActivity.class);
        }
        else if(type.equals("CRICKET")){
            intent=new Intent(HomePage.this, TabLayoutActivity.class);
        }
        else if(type.equals("MIND_GAME")){
            intent=new Intent(HomePage.this, MindReadingGame.class);
        }
        else if(type.equals("phone_pe_rl")){
            intent=new Intent(HomePage.this, ContactList.class);
        }
        else if(type.equals("my_contact_rl")){
            intent=new Intent(HomePage.this, MyContacts.class);
        }
        startActivity(intent);
        //finish();
    }


    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public void getTimeMange(){
        try {
            DateTimeFormatter dtf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));


            }
                    }
        catch (Exception e){
        }

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_morning);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_after_noon);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_evening_1);
        }
        else if(timeOfDay >= 21 && timeOfDay < 24){
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_night_1);
        }
    }

    private void getTimeFromAndroid() {
        Date currentTime = Calendar.getInstance().getTime();
        int hours = currentTime.getHours();
        int min = currentTime.getMinutes();

        if(hours>=1 || hours<=12){
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_morning_gif_1);
        }else if(hours>=12 || hours<=16){
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_afternoon_gif_1);
        }else if(hours>=16 || hours<=21){
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_evening_gif_1);
        }else if(hours>=21 || hours<=24){
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
            gifImageView.setImageResource(R.drawable.good_night_gif_1);
        }
    }

}
