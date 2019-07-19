package com.webview.pallav.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager; 
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private WebView webView = null;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.webView = (WebView) findViewById(R.id.activity_main_webview);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        webView = initWebview(webView);
        initCookieManager();

        //Saving current date and time
        Helper.saveDateTime(this);
    }


    @Override
    protected void onPause() {
        super.onPause();

        webView.clearCache(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isConnected()) {
            showNoInternetConnectionDialog();
        } else if(isSessionOver()) {
            Log.d(TAG, "session is over. Redirecting user to Login page");
            initCookieManager();
            webView.loadUrl("http://www.as-mexico.com.mx/feedback/login.html");
            Helper.saveDateTime(this);
        }
        else if(webView == null) {
            webView = initWebview(webView);
            initCookieManager();
        }


        else if (!CookieManager.getInstance().hasCookies()) {
            webView = initWebview(webView);
            initCookieManager();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


   //Inflating Menu for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wv_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.refresh:  {
               webView.reload();
               return true;
           }
           case android.R.id.home: {
               if(webView.canGoBack()) {
                   webView.goBack();
               }else {
                  finish();
               }
               return true;
           }
       }
       return super.onOptionsItemSelected(item);
    }


    private WebView initWebview(WebView mywebView) {
        if (isConnected()) {
            WebSettings webSettings = mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
             webView.setWebChromeClient(new WebChromeClient());
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setSupportZoom(true);
            webSettings.setDefaultTextEncodingName("utf-8");

            WebViewClientImpl webViewClient = new WebViewClientImpl(this);
            mywebView.setWebViewClient(webViewClient);

            mywebView.loadUrl("http://www.as-mexico.com.mx/feedback/login.html");

        }

        return mywebView;
    }

    private void initCookieManager(){
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
    }

   private boolean isConnected(){
       ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       return cm.getActiveNetworkInfo() != null;
   }

   private void showNoInternetConnectionDialog() {
       AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
       builder1.setMessage("Please check your internet connection and restart the App.");
       builder1.setCancelable(false);

       builder1.setNegativeButton(
               "Ok",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                       finish();
                   }
               });

       AlertDialog alert11 = builder1.create();
       alert11.show();
   }

    public boolean isSessionOver() {
        //milliseconds

        Date currentDate = new Date();
        String date = Helper.getDateTime(this);
        Date savedDate = new Date(date);

        long different = currentDate.getTime() - savedDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedMinutes > 20;

    }

}