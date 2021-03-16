package com.elevenzon.sqlite.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elevenzon.sqlite.MainActivity;
import com.elevenzon.sqlite.R;

public class ChessTimer extends AppCompatActivity implements View.OnClickListener {
    LinearLayout timer1_ll,timer2_ll;
    TextView timer1,timer2;
    ImageView timer1_img,timer2_img;
    RelativeLayout timer1_img_rl,timer2_img_rl;

    CountDownTimer count=null,count1=null;
    int s1=300,s2=300;
    boolean timer11=false,timer22=false;
    public static  MediaPlayer mp;

    Bitmap photo;

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


        timer1_img_rl.setOnClickListener(this);
        timer2_img_rl.setOnClickListener(this);
        timer1_ll.setOnClickListener(this);
        timer2_ll.setOnClickListener(this);
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
                if (ContextCompat.checkSelfPermission(ChessTimer.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChessTimer.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestStoragePermission();
                }
                break;
            case R.id.time_img2_rl:
                if (ContextCompat.checkSelfPermission(ChessTimer.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChessTimer.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                } else {
                    requestStoragePermission();
                }
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


}
