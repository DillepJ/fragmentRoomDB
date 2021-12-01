package com.elevenzon.sqlite.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elevenzon.sqlite.R;

public class ChessTimer extends AppCompatActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    LinearLayout timer1_ll,timer2_ll;
    TextView timer1,timer2;
    ImageView timer1_img,timer2_img;
    RelativeLayout timer1_img_rl,timer2_img_rl,timer3_img_rl;

    CountDownTimer count=null,count1=null;
    int s1=300,s2=300;
    boolean timer11=false,timer22=false;
    public static  MediaPlayer mp;

    boolean pause_status =true;
    ImageView settings,pause_resume,refresh;

    Bitmap photo;
    String count_pause="";
    String count1_pause="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_timer);
        intializeUI();
    }

    private void intializeUI() {
        timer1_ll=findViewById(R.id.timer1_ll);
        timer2_ll=findViewById(R.id.timer2_ll);
        timer1=findViewById(R.id.timer1);
        timer2=findViewById(R.id.timer2);
        timer1_img=findViewById(R.id.timer_img1);
        timer2_img=findViewById(R.id.timer_img2);
        timer1_img_rl=findViewById(R.id.time_img1_rl);
        timer2_img_rl=findViewById(R.id.time_img2_rl);
        timer3_img_rl=findViewById(R.id.time_img3_rl);
        settings=findViewById(R.id.settings);
        pause_resume=findViewById(R.id.pause_resume);
        refresh=findViewById(R.id.refresh);

        refresh.setVisibility(View.INVISIBLE);
        pause_resume.setVisibility(View.INVISIBLE);
        timer2_img_rl.setClickable(false);
        timer3_img_rl.setClickable(false);



        timer1_img_rl.setOnClickListener(this);
        timer2_img_rl.setOnClickListener(this);
        timer1_ll.setOnClickListener(this);
        timer2_ll.setOnClickListener(this);
        timer3_img_rl.setOnClickListener(this);
        settings.setOnClickListener(this);
         mp = MediaPlayer.create(this,R.raw.button_clicked);
    }

    public void reverseTimer1(int Seconds){
        //timer1_ll.setEnabled(true);
        count= new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                s1=seconds;
                int minutes = seconds / 60;
                seconds = seconds % 60;
               /* button.setText( String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
*/
                timer1.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));

               // mp.stop();


            }

            public void onFinish() {
               // button.setText("Time Up!!!");
                timer1.setText("Time Up!!");
                //mp.stop();
            }
        };
        count.start();
        count_pause="start";
        count1_pause="stop";
        pause_status=true;
        refresh.setVisibility(View.VISIBLE);
        pause_resume.setVisibility(View.VISIBLE);
        pause_resume.setImageResource(R.drawable.pause_icon);
        timer2_img_rl.setClickable(true);
        timer3_img_rl.setClickable(true);

    }

    public void reverseTimer2(int Seconds){
        //timer2_ll.setEnabled(true);
        count1= new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                s1=seconds;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                /*button.setText( String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));*/
                timer2.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
                //mp.stop();
            }

            public void onFinish() {
                //button.setText("Time Up!!!");
                timer2.setText("Time Up!!");
               // mp.stop();
            }
        };
        count1.start();
        count1_pause="start";
        count_pause="stop";
        pause_status=true;
        refresh.setVisibility(View.VISIBLE);
        pause_resume.setVisibility(View.VISIBLE);
        pause_resume.setImageResource(R.drawable.pause_icon);
        timer2_img_rl.setClickable(true);
        timer3_img_rl.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timer1_ll:
                        mp.start();
                        if((timer11==false&&count!=null)) {
                            count.cancel();
                            timer11=true;
                            //int time=Integer.parseInt(timer1.getText().toString());
                            reverseTimer2(s1);
                            timer1_ll.setEnabled(false);
                            timer2_ll.setEnabled(true);
                            timer1_ll.setBackgroundResource(R.drawable.ll_back);
                            timer2_ll.setBackgroundResource(R.drawable.ll_background);
                            timer2_ll.setElevation((float) 5.0);
                        }


                 else if(timer11==false){
                            timer11=true;
                            //int time=Integer.parseInt(timer1.getText().toString());
                            reverseTimer2(s1);
                            timer1_ll.setEnabled(false);
                            timer2_ll.setEnabled(true);
                            timer2_ll.setElevation((float) 5.0);
                            timer1_ll.setBackgroundResource(R.drawable.ll_back);
                            timer2_ll.setBackgroundResource(R.drawable.ll_background);
                        }
                   else {
                       if(count!=null) {
                           count.cancel();
                       //count1.start();
                       reverseTimer2(stringtoSeconds(timer2.getText().toString()));
                           timer1_ll.setEnabled(false);
                           timer2_ll.setEnabled(true);
                           timer2_ll.setElevation((float) 5.0);
                           timer1_ll.setBackgroundResource(R.drawable.ll_back);
                           timer2_ll.setBackgroundResource(R.drawable.ll_background);
                   }
               }


                break;
            case R.id.timer2_ll:
                    mp.start();
                if(count1!=null&& timer22==false){
                        count1.cancel();
                    timer22=true;
                    reverseTimer1(s2);
                    timer2_ll.setEnabled(false);
                    timer1_ll.setEnabled(true);
                    timer1_ll.setElevation((float) 5.0);
                    timer2_ll.setBackgroundResource(R.drawable.ll_back);
                    timer1_ll.setBackgroundResource(R.drawable.ll_background);
                }


                else if(timer22==false){
                    timer22=true;
                    reverseTimer1(s2);
                    timer2_ll.setEnabled(false);
                    timer1_ll.setEnabled(true);
                    timer1_ll.setElevation((float) 5.0);
                    timer2_ll.setBackgroundResource(R.drawable.ll_back);
                    timer1_ll.setBackgroundResource(R.drawable.ll_background);
                }

                else {
                    if(count1!=null) {
                        //count.start();
                        count1.cancel();
                        reverseTimer1(stringtoSeconds(timer1.getText().toString()));
                        timer2_ll.setEnabled(false);
                        timer1_ll.setEnabled(true);
                        timer2_ll.setBackgroundResource(R.drawable.ll_back);
                        timer1_ll.setBackgroundResource(R.drawable.ll_background);
                    }
                }




                break;

            case R.id.time_img1_rl:
                /*if (ContextCompat.checkSelfPermission(ChessTimer.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChessTimer.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestStoragePermission();
                }*/
                break;
            case R.id.time_img2_rl:
                if(pause_status){
                    pause_resume.setImageResource(R.drawable.resume_icon);
                    pause_status=false;
                    timer2_ll.setClickable(false);
                    timer1_ll.setClickable(false);

                    if(count1_pause=="start"){
                        count1.cancel();
                    }
                    else if(count_pause=="start") {
                        count.cancel();
                    }


                }
                else if(!pause_status){
                    timer2_ll.setClickable(true);
                    timer1_ll.setClickable(true);

                    pause_resume.setImageResource(R.drawable.pause_icon);
                    pause_status=true;
                    if((timer11==false&&count!=null)){
                        reverseTimer2(s1);
                        if(count!=null){
                            count.cancel();
                        }
                    }
                   else {
                        if (count_pause=="stop") {
                            reverseTimer2(stringtoSeconds(timer2.getText().toString()));
                            if(count!=null){
                                count.cancel();
                            }
                        }
                    }
                    if(count1!=null&& timer22==false) {
                        reverseTimer1(s2);
                        if(count1!=null){
                            count1.cancel();
                        }
                    }
                    else {
                        if(count1_pause=="stop"){
                            reverseTimer1(stringtoSeconds(timer1.getText().toString()));
                            if(count1!=null){
                                count1.cancel();
                            }
                        }
                    }
                }


                /*if (ContextCompat.checkSelfPermission(ChessTimer.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChessTimer.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                } else {
                    requestStoragePermission();
                }*/
                break;

            case R.id.time_img3_rl:
                /*Intent intent = getIntent();
                finish();
                startActivity(intent);*/
                timer1_ll.setBackgroundResource(R.drawable.ll_back);
                timer2_ll.setBackgroundResource(R.drawable.ll_back);
                count_pause="";
                count1_pause="";
                pause_status=true;
                refresh.setVisibility(View.GONE);
                pause_resume.setVisibility(View.GONE);
                timer2_img_rl.setClickable(false);
                timer3_img_rl.setClickable(false);
                timer1_ll.setEnabled(true);
                timer2_ll.setEnabled(true);
                timer2.setText("5.00");
                timer1.setText("5.00");
                timer11=false;
                timer22=false;
                if(count!=null){
                    count.cancel();
                }
                if(count1!=null){
                    count1.cancel();
                }

                break;

            case R.id.settings:
                PopupMenu popup = new PopupMenu(ChessTimer.this, v);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.time_menu);
                popup.show();

                break;

        }
    }

    public int stringtoSeconds(String time){
       //mm:ss
        String[] units = time.split(":"); //will break the string up into an array
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        int duration = 60 * minutes + seconds;
        return duration;
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
                            ActivityCompat.requestPermissions(ChessTimer.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            photo = (Bitmap) data.getExtras().get("data");
            timer1_img.setImageBitmap(photo);

            //getLatitude();
            //gpsTracker.getAddressFromLocation(gpsTracker.latitude,gpsTracker.longitude,MainActivity.this,handler);
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {


            photo = (Bitmap) data.getExtras().get("data");
            timer2_img.setImageBitmap(photo);

            //getLatitude();
            //gpsTracker.getAddressFromLocation(gpsTracker.latitude,gpsTracker.longitude,MainActivity.this,handler);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Toast.makeText(this, "Selected Language: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.choose:
                return true;
            /*case R.id.goto_qr:
                Intent intent=new Intent(ChessTimer.this, QRCodeReaderActivity.class);
                startActivity(intent);

                return true;
*/
            case R.id.min_5:
                timer1_ll.setBackgroundResource(R.drawable.ll_back);
                timer2_ll.setBackgroundResource(R.drawable.ll_back);
                count_pause="";
                count1_pause="";
                pause_status=true;
                timer1_ll.setEnabled(true);
                timer2_ll.setEnabled(true);
                refresh.setVisibility(View.GONE);
                pause_resume.setVisibility(View.GONE);
                timer2_img_rl.setClickable(false);
                timer3_img_rl.setClickable(false);
                timer2.setText("5.00");
                timer1.setText("5.00");
                s1=300;s2=300;
                timer11=false;
                timer22=false;
                if(count!=null){
                    count.cancel();
                }
                if(count1!=null){
                    count1.cancel();
                }
                return true;
            case R.id.mini_10:
                timer1_ll.setBackgroundResource(R.drawable.ll_back);
                timer2_ll.setBackgroundResource(R.drawable.ll_back);
                count_pause="";
                count1_pause="";
                pause_status=true;
                refresh.setVisibility(View.GONE);
                pause_resume.setVisibility(View.GONE);
                timer1_ll.setEnabled(true);
                timer2_ll.setEnabled(true);
                timer2_img_rl.setClickable(false);
                timer3_img_rl.setClickable(false);
                timer2.setText("10.00");
                timer1.setText("10.00");
                s1=600;s2=600;
                timer11=false;
                timer22=false;
                if(count!=null){
                    count.cancel();
                }
                if(count1!=null){
                    count1.cancel();
                }
                return true;
            case R.id.mini_15:
                s1=900;s2=900;
                timer1_ll.setEnabled(true);
                timer2_ll.setEnabled(true);
                timer1_ll.setBackgroundResource(R.drawable.ll_back);
                timer2_ll.setBackgroundResource(R.drawable.ll_back);
                count_pause="";
                count1_pause="";
                pause_status=true;
                refresh.setVisibility(View.GONE);
                pause_resume.setVisibility(View.GONE);
                timer2_img_rl.setClickable(false);
                timer3_img_rl.setClickable(false);
                timer2.setText("15.00");
                timer1.setText("15.00");
                timer11=false;
                timer22=false;
                if(count!=null){
                    count.cancel();
                }
                if(count1!=null){
                    count1.cancel();
                }
                return true;
            case R.id.mini_20:
                s1=1200;s2=1200;
                timer1_ll.setBackgroundResource(R.drawable.ll_back);
                timer2_ll.setBackgroundResource(R.drawable.ll_back);
                count_pause="";
                count1_pause="";
                pause_status=true;
                timer1_ll.setEnabled(true);
                timer2_ll.setEnabled(true);
                refresh.setVisibility(View.GONE);
                pause_resume.setVisibility(View.GONE);
                timer2_img_rl.setClickable(false);
                timer3_img_rl.setClickable(false);
                timer2.setText("20.00");
                timer1.setText("20.00");
                timer11=false;
                timer22=false;
                if(count!=null){
                    count.cancel();
                }
                if(count1!=null){
                    count1.cancel();
                }
                return true;

            default:
                return false;
        }
    }
}
