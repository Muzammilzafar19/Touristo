package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

public class EventsDetailsActivity extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);

        wv =  findViewById(R.id.webview);
        wv.setBackgroundColor(0);
        wv.setBackgroundResource(android.R.color.black);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        String pageLink="file:///android_asset/htmlpage/"+ Objects.requireNonNull(getIntent().getExtras()).getString("pagename")+".html";
         Log.i("chklink",pageLink);
        wv.loadUrl(pageLink);

    }
}
