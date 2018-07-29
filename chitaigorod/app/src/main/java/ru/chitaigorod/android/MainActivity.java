package ru.chitaigorod.android;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.webkit.*;
import android.net.*;
import android.annotation.*;
import android.util.*;
import android.support.annotation.*;

public class MainActivity extends AppCompatActivity
{
	private WebView wv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		wv = (WebView) findViewById(R.id.webView);
		
		wv.setWebViewClient(new MyWebViewClient());
		wv.setWebChromeClient(new MyWebChromeClient());
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);
		// указываем страницу загрузки
		wv.loadUrl("https://chitai-gorod.ru/"); 
	}
	
	
	
	private class MyWebViewClient extends WebViewClient 
	{
			@SuppressWarnings("deprecation")
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				final Uri uri = Uri.parse(url);
				return handleUri(view, uri);
			}

			@RequiresApi(Build.VERSION_CODES.N)
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				final Uri uri = request.getUrl();
				return handleUri(view, uri);
			}

			private boolean handleUri(WebView wv, final Uri uri) {
		
				final String host = uri.getHost();
				final String scheme = uri.getScheme();
				
				wv.loadUrl(uri.toString());
				// Based on some condition you need to determine if you are going to load the url 
				// in your web view itself or in a browser. 
				// You can use `host` or `scheme` or any part of the `uri` to decide.
				return true;
			}
	}
	
	private class MyWebChromeClient extends WebChromeClient 
	{
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage)
		{
			// TODO: Implement this method
			return super.onConsoleMessage(consoleMessage);
		}
		
	}
}
