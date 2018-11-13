package ru.chitaigorod.android.utils;
import android.webkit.*;
import android.net.*;
import android.support.annotation.*;
import android.os.*;
import java.io.*;

public class MyWebViewClient extends WebViewClient 
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
		
		String file = isMyRes(uri);

		if (file != null){
			try {				
				File fil = new File("/storage/emulated/0/cg/chitaigorod/app/src/main/assets/"+file);
				FileInputStream stream = new FileInputStream(fil);
				String tContents = "";


				int size = stream.available();
				byte[] buffer = new byte[size];
				stream.read(buffer);
				stream.close();
				tContents = new String(buffer);

				String docty = file.contains("scripts/") ? "text/javascript" : "text/html";
				ByteArrayInputStream EMPTY = new ByteArrayInputStream(tContents.getBytes());
				return new WebResourceResponse(docty, "utf-8", EMPTY);					

			} catch (IOException e) {
				// Handle exceptions here
			}
		}
		//String buff = Utils.getJSByTag((AppCompatActivity)getApplicationContext(), isMyRes(uri));
		if(isMyRes(uri) != null){
			InputStream gg = null;
			try
			{
				gg = view.getContext().getAssets().open(isMyRes(uri));
			}
			catch (IOException e)
			{}

			return new WebResourceResponse(
				"text/html", "utf-8", gg);				
		}else{
			return super.shouldInterceptRequest(view, url);
		}
	}



	@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {	
		Uri uri = request.getUrl();
		String file = isMyRes(uri);

		if (file != null){
			try {				
				File fil = new File("/storage/emulated/0/cg/chitaigorod/app/src/main/assets/"+file);
				FileInputStream stream = new FileInputStream(fil);
				String tContents = "";


				int size = stream.available();
				byte[] buffer = new byte[size];
				stream.read(buffer);
				stream.close();
				tContents = new String(buffer);

				String docty = file.contains("scripts/") ? "text/javascript" : "text/html";
				ByteArrayInputStream EMPTY = new ByteArrayInputStream(tContents.getBytes());
				return new WebResourceResponse(docty, "utf-8", EMPTY);					

			} catch (IOException e) {
				// Handle exceptions here
			}
		}
		
		//String buff = Utils.getJSByTag((AppCompatActivity)getApplicationContext(), isMyRes(uri));
		if(isMyRes(uri) != null){
			InputStream gg = null;
			try
			{
				gg = view.getContext().getAssets().open(isMyRes(uri));
			}
			catch (IOException e)
			{}

			return new WebResourceResponse(
				"text/html", "utf-8", gg);				
		}else{
			return super.shouldInterceptRequest(view, request);
		}
	}

	private String isMyRes(Uri uri){

		if(uri.getPathSegments().size() == 0){
			return "api/index.html";
		}	
		if(uri.getPath().contains("main.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}	
		if(uri.getPath().contains("city.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("jquery.cookie.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("elasticsearch.min.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("search.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("bodybuilder.min.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("item.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("account.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		if(uri.getPath().contains("cart.js")){
			return "api/scripts/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
		}
		
		return null;
	}
}

