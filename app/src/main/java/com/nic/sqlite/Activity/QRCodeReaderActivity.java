package com.nic.sqlite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.nic.sqlite.R;
import com.nic.sqlite.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeReaderActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    RelativeLayout whole_ll,swipe_rl,next_rl;
    View bar;
    Animation animation;
    public static MediaPlayer mp;
    boolean link_status=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_reader_view);

         bar = findViewById(R.id.bar);
         whole_ll=findViewById(R.id.whole_ll);
         swipe_rl=findViewById(R.id.swipe_ll);
         next_rl=findViewById(R.id.next_rl);
         animation = AnimationUtils.loadAnimation(this, R.anim.scanning_animation_view);
        bar.setVisibility(View.VISIBLE);
        bar.startAnimation(animation);
        //animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);
                //animation.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
               /*bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);*/
            //animation.start();
            }
        });

        mp = MediaPlayer.create(this,R.raw.beep);

        resultTextView=findViewById(R.id.qr_code_text);
        qrCodeReaderView=findViewById(R.id.qrdecoderview);

        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);




        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        //qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setSoundEffectsEnabled(true);

        qrCodeReaderView.startCamera();
        whole_ll.setOnTouchListener(new RelativeLayoutTouchListener(this));

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(link_status) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultTextView.getText().toString()));
                    startActivity(browserIntent);
                }
                else {
                    Utils.showAlert(QRCodeReaderActivity.this,"It's not a valid url");
                }
            }
        });



    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mp.start();
        SpannableString content = new SpannableString(text) ;
        content.setSpan( new UnderlineSpan() , 0 , text.length() , 0 ) ;
        resultTextView.setText(content);
        bar.setVisibility(View.GONE);
        bar.animate().cancel();
        //animation.cancel();
        Utils.showAlert(this,"QrCode Successfully Read QRCode : "+ text);
        extractURL(text);
        qrCodeReaderView.stopCamera();

        //finish();

    }
    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    public class RelativeLayoutTouchListener implements View.OnTouchListener{



            static final String logTag = "ActivitySwipeDetector";
            private Activity activity;
            static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
            private float downX, downY, upX, upY;

            // private MainActivity mMainActivity;

        public RelativeLayoutTouchListener(Activity mainActivity) {
            activity = mainActivity;
        }

            public void onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!");
            Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
            swipe_rl.setVisibility(View.VISIBLE);
            next_rl.setVisibility(View.GONE);
            // activity.doSomething();
        }

            public void onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!");
            Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
            swipe_rl.setVisibility(View.GONE);
            next_rl.setVisibility(View.VISIBLE);


            // activity.doSomething();
        }

            public void onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!");
            Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
            // activity.doSomething();
        }

            public void onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!");
            Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
            // activity.doSomething();
        }

            public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // swipe horizontal?
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    // swipe vertical?
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            this.onTopToBottomSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            this.onBottomToTopSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    return false; // no swipe horizontally and no swipe vertically
                }// case MotionEvent.ACTION_UP:
            }
            return false;
        }

        }

    // Function to extract all the URL
    // from the string
    public  void extractURL(
            String str)
    {

        // Creating an empty ArrayList
        List<String> list
                = new ArrayList<>();

        // Regular Expression to extract
        // URL from the string
        String regex
                = "\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])";

        // Compile the Regular Expression
        Pattern p = Pattern.compile(
                regex,
                Pattern.CASE_INSENSITIVE);

        // Find the match between string
        // and the regular expression
        Matcher m = p.matcher(str);

        // Find the next subsequence of
        // the input subsequence that
        // find the pattern
        while (m.find()) {

            // Find the substring from the
            // first index of match result
            // to the last index of match
            // result and add in the list
            list.add(str.substring(
                    m.start(0), m.end(0)));
        }

        // IF there no URL present
        if (list.size() == 0) {
            System.out.println("-1");
            return;
        }
        else {
            link_status=true;
        }

        // Print all the URLs stored
        for (String url : list) {
            System.out.println(url);
        }
    }


}
