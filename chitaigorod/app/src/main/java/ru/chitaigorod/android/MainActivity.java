package ru.chitaigorod.android;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.webkit.*;
import android.net.*;
import android.annotation.*;
import android.util.*;
import android.support.annotation.*;
import ru.chitaigorod.android.view.*;
import com.arlib.floatingsearchview.*;
import ru.chitaigorod.android.utils.*;
import java.security.*;

public class MainActivity extends AppCompatActivity implements BaseFragment.BaseFragmentCallbacks
{

	@Override
	public void onLoadPage(String url)
	{
		wv.loadUrl(url);
	}

	@Override
	public void onAttachSearchViewToDrawer(FloatingSearchView searchView)
	{
		// TODO: Implement this method
	}
	
	public final String Tag = "MainPage";
	private WebView wv;
	private AppCompatActivity activity;
   
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		activity = this;
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

		@Override
		public void onPageFinished(WebView view, String url)
		{
			String tag = Utils.router(url);
			view.loadUrl(Utils.getJSByTag( activity, tag));
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
	}
	
	private class MyWebChromeClient extends WebChromeClient 
	{
		@Override
		public boolean onConsoleMessage(ConsoleMessage cmsg)
		{
			Log.d(Tag, cmsg.message());
			
			if (cmsg.message().startsWith("MAGIC"))
			{
				String msg = cmsg.message().substring(5); // strip off prefix

				/* process HTML */

				return true;
			}

			return false;
			// TODO: Implement this method
			//return super.onConsoleMessage(consoleMessage);
		}
		
	}
}
