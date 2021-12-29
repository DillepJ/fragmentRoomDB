package com.nic.sqlite.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.nic.sqlite.Activity.Interface.WebAppInterface;
import com.nic.sqlite.R;
import com.nic.sqlite.Utils.Utils;

/** Created By Dileep Kumar
 * **/

public class WebViewActivity extends AppCompatActivity {

    WebView myWebView;
    WebSettings webSettings;
    SwipeRefreshLayout swipeRefreshLayout;
    int initial=0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_sample);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myWebView = (WebView) findViewById(R.id.web_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);



        webSettings = myWebView.getSettings();
        webSettings.setSaveFormData(false);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                //return super.onJsAlert(view, url, message, result);
                int title = view.getDisplay().getDisplayId();
                Utils.showAlert1(WebViewActivity.this,message,"alert",result);
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Utils.showAlert1(WebViewActivity.this,message,"confirm",result);
                //result.confirm();
                return true;
                //return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
               /* Utils.showAlert(MainActivity.this,message);
                result.cancel();
                return true;*/
                return super.onJsAlert(view, url, message, result);
            }


            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin,true,true);
            }

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public boolean onJsTimeout() {
                return super.onJsTimeout();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMyPage(AppConstant.KEY_URL);
            }
        });
        loadMyPage(AppConstant.KEY_URL);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public void loadMyPage(String url){
        if(Utils.isOnline(this)) {
            myWebView.requestFocus();
            myWebView.getSettings().setLightTouchEnabled(true);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setGeolocationEnabled(true);
            myWebView.getSettings().setAllowFileAccess(true);
            myWebView.getSettings().setAllowContentAccess(true);
            myWebView.setSoundEffectsEnabled(true);
            myWebView.clearCache(true);
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            myWebView.getSettings().setUseWideViewPort(true);
            if(initial ==1) {
                myWebView.loadUrl(myWebView.getUrl());

            }
            else {
                myWebView.loadUrl(url);
            }
            swipeRefreshLayout.setRefreshing(true);
            myWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(WebViewActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    //loadMyPage(AppConstant.KEY_URL);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    swipeRefreshLayout.setRefreshing(false);
                    initial=1;
                    /* This call inject JavaScript into the page which just finished loading. */
                    //myWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(1);
        val = val * 100d;
        return val.intValue();
    }


}
