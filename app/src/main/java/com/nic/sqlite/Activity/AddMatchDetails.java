package com.nic.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.nic.sqlite.MainActivity;
import com.nic.sqlite.R;

public class AddMatchDetails extends AppCompatActivity {
    WebView webView;
    Button goto_web_view, goto_Cricket_Score_Board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_details);

        webView = findViewById(R.id.web_view);
        goto_web_view = findViewById(R.id.ButtonStartA);
        goto_Cricket_Score_Board = findViewById(R.id.ButtonAdvanceSettingS);

        webView.setVisibility(View.GONE);
        goto_web_view.setVisibility(View.GONE);
        goto_Cricket_Score_Board.setVisibility(View.VISIBLE);

        //webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
       /* webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);*/
        WebViewClientActivity webViewClient = new WebViewClientActivity(this);
        webView.setWebViewClient(webViewClient);
        /*webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("Log1", "onPermissionRequest");
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d("Log2", request.getOrigin().toString());
                        if(request.getOrigin().toString().equals("file:///")) {
                            Log.d("Log3", "GRANTED");
                            request.grant(request.getResources());
                        } else {
                            Log.d("Log4", "DENIED");
                            request.deny();
                        }
                    }
                });
            }


        });*/

    }

    public void onContinueMatch(View view){
        Intent intent = new Intent(AddMatchDetails.this,CricketScoreBoard.class);
        startActivity(intent);
    }
    public void onPaymentGateway(View view){
        Intent intent = new Intent(AddMatchDetails.this,RazorPaymentActivity.class);
        startActivity(intent);
    }

    public void gotoWebView(View view){
       /* webView.setVisibility(View.VISIBLE);
        goto_Cricket_Score_Board.setVisibility(View.GONE);
        goto_web_view.setVisibility(View.GONE);
        gotoGoogle();*/
        Intent intent = new Intent(AddMatchDetails.this,WebViewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    else if(goto_Cricket_Score_Board.getVisibility()==View.VISIBLE){
                        finish();
                    }
                    else  {
                        webView.setVisibility(View.GONE);
                        goto_Cricket_Score_Board.setVisibility(View.VISIBLE);
                        goto_web_view.setVisibility(View.VISIBLE);
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    public void gotoGoogle(){
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://10.163.19.140:8080/rdweb/project/agmt/app/project/view/login.php");
        webView.requestFocus();
    }

}
