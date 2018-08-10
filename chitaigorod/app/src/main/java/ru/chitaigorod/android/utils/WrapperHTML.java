package ru.chitaigorod.android.utils;
import android.webkit.*;
import android.content.*;
import android.net.*;
import android.support.annotation.*;
import android.os.*;
import java.sql.*;
import android.support.v4.app.Fragment;
import java.io.*;
import android.nfc.*;
import java.util.*;
import android.support.v7.app.*;

public class WrapperHTML
{
	private WebView wv;
	private Context ctx;
	private WrapperListener mListener;
	private String FragmentTag;
	public WrapperHTML(Context ctx, String tag){
		this.ctx = ctx;
		this.FragmentTag = tag;
		wv =  new WebView(ctx);

		wv.setWebViewClient(new MyWebViewClient());
		wv.setWebChromeClient(new MyWebChromeClient());
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setAppCacheEnabled(true);
		wv.getSettings().setBlockNetworkImage(true);
		wv.getSettings().setLoadsImagesAutomatically(false);
		
	}
	
	public void setListener(WrapperListener frg){
		if (frg instanceof WrapperListener) {
            mListener = (WrapperListener) frg;
        } else {
            throw new RuntimeException(ctx.toString()
									   + " must implement BaseExampleFragmentCallbacks");
        }
	}
	
	public interface WrapperListener{
		void onPageSuccess(String html);
	}
	
	public void get(String str){
		wv.loadUrl(str);
	}
	
	
	private class MyWebViewClient extends WebViewClient 
	{

		@Override
		public void onPageFinished(WebView view, String url)
		{
			view.loadUrl(Utils.getJSByTag((AppCompatActivity) ctx, FragmentTag));
			//"javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
		}

		
		
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
			if(uri.getScheme() == "https"){
				wv.loadUrl(uri.toString());
				return true;
			}else{
				return false;
			}
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			Uri uri = Uri.parse(url);
			
			if(isBlockRes(uri)){
				ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
				return new WebResourceResponse("text/plain", "utf-8", EMPTY);				
			}else{
				return super.shouldInterceptRequest(view, url);
			}
			// TODO: Implement this method
			
		}
		

		@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {	
			Uri uri = request.getUrl();
			if(isBlockRes(uri)){
				ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
				return new WebResourceResponse("text/plain", "utf-8", EMPTY);				
			}else{
				return super.shouldInterceptRequest(view, request);
			}
		}
		
		private Boolean isBlockRes(Uri uri){
			List blackList = new ArrayList<String>();	
			
			blackList.add("vk.com");
			blackList.add("connect.facebook.net");
			blackList.add("code.jivosite.com");
			blackList.add("mc.yandex.ru");
			blackList.add("www.facebook.com");
			blackList.add("platform.twitter.com");
			blackList.add("code2.jivosite.com");
			blackList.add("www.gdeslon.ru");
			blackList.add("eu-sonar.sociomantic.com");
			blackList.add("aprtx.com");
			blackList.add("syndication.twitter.com");
			blackList.add("sslwidget.criteo.com");
			blackList.add("connect.ok.ru");
			blackList.add("top-fwz1.mail.ru");
			blackList.add("6093007.fls.doubleclick.net");		
			blackList.add("adservice.google.com");
			blackList.add("www.googletagmanager.com");
			
			if(blackList.contains(uri.getHost())){
				return true;
			}
			return false;
		}
	}

	private class MyWebChromeClient extends WebChromeClient 
	{
		@Override
		public boolean onConsoleMessage(ConsoleMessage cmsg)
		{
			//Log.d(Tag, cmsg.message());

			if (cmsg.message().startsWith("MAGIC"))
			{
				String msg = cmsg.message().substring(5); // strip off prefix

				mListener.onPageSuccess(msg);
				/* process HTML */

				return true;
			}

			return false;
			// TODO: Implement this method
			//return super.onConsoleMessage(consoleMessage);
		}

	}
	
}
