package com.elevenzon.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elevenzon.sqlite.NoteModel;
import com.elevenzon.sqlite.R;
import com.elevenzon.sqlite.SqliteHelepr;

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
    RelativeLayout game_assit_rl,qr_reader_rl;
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
        game_assit_rl=findViewById(R.id.game_assistance);
        getUserDetails();
        game_assit_rl.setOnClickListener(this::onClick);
        qr_reader_rl.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        getTimeMange();
        if(getUserDetails()) {
            profile_image.setImageBitmap(getImage(user_profile));
        }
        user_name.setText(prefManager.getUserName());
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
