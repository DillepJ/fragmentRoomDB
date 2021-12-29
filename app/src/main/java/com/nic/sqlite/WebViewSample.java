package com.nic.sqlite;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewSample extends AppCompatActivity {

    private WebView webView;
    String url = "";
    WebSettings webSettings;

    class MyJavaScriptInterface {
        MyJavaScriptInterface() {
        }
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String user,String pass) {
            Log.e("Remember", "user:"+user+" pass:"+pass);
            if (!(user.length() >= 1 && pass.length() >= 1))
                return;


            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserInformation",MODE_PRIVATE);
            if (sharedPreferences.getBoolean("isClickYes", false)) {
                sharedPreferences.edit().putString("us",user).apply();
                sharedPreferences.edit().putString("pass",pass).apply();
            }

            if (!sharedPreferences.getBoolean("isClickYes", false)) {
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext())
                        .setTitle("save information")
                        .setMessage("Are You Want Save Password?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().putString("us",user).apply();
                                sharedPreferences.edit().putString("pass",pass).apply();
                                sharedPreferences.edit().putBoolean("isClickYes",true).apply();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_popup_reminder)
                        .show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_sample);

        webView = findViewById(R.id.web_view);

        webSettings = webView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");

        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.setWebViewClient(new WebViewClient());


        }


        public class myWebViewClient extends WebViewClient{

        }


}