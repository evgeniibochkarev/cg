package ru.chitaigorod.android.UX.fragments;

import android.annotation.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;

import android.support.v7.widget.Toolbar;
import ru.chitaigorod.android.UX.dialogs.*;
import ru.chitaigorod.android.interfaces.*;

public class Order2Fragment extends BaseFragment
{

	private WebView wv;
	private ProgressBar pb;
	private EditText tv;
	@Override
	public void APIResponse(JSONObject json)
	{
		try
		{
			String method = json.getString("method");
			String data = json.getString("data");
			//writeFileOnInternalStorage(getActivity(), "text.html", data);
			// writeToFile(data, getActivity());
			//tv.setText(data);
			//wv.loadData(data, "text/html; charset=utf-8", "UTF-8");
			if(method.equals("OrderFragment.getOrderPage")){

			}

		}
		catch (JSONException e)
		{}

		super.APIResponse(json);
	}

	

	public static Order2Fragment newInstance(){
		return new Order2Fragment();
	}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Timber.d("%s - onCreateView", this.getClass().getSimpleName());

        View view = inflater.inflate(R.layout.fragment_order, container, false);


		Toolbar toolbar = (Toolbar)view.findViewById(R.id.fragment_order_toolbar);
		toolbar.setTitle("Оформление заказа");
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		toolbar.setNavigationOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					getActivity().onBackPressed();
				}
			});


		pb = (ProgressBar) view.findViewById(R.id.order_pb);
		wv = (WebView) view.findViewById(R.id.order_webview);
		wv.setWebViewClient(new MyOrder2WebViewClient());
		wv.setWebChromeClient(new MyWebChromeOrder2Client(this));
		wv.getSettings().setJavaScriptEnabled(true);

        //metodi optimizacii
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.getSettings().setAppCacheEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setSavePassword(true);
        wv.getSettings().setSaveFormData(true);
        wv.getSettings().setEnableSmoothTransition(true);




		wv.loadUrl("https://ad.admitad.com/g/q6gfnfvsq01452083376a804937a48/?ulp=https%3A%2F%2Fwww.chitai-gorod.ru%2Findex.php");
		//mFragmentNavigation.get(APIHelper.getData("Order2Fragment.getPage", "{}"));
		return view;
	}
	
	public void showCityPicker(){
		mFragmentNavigation.showDialog(CityPickerDialogFragment.newInstance(new CityPickerDialogInterface(){
											   @Override
											   public void onCityUpdate(boolean p1)
											   {
												   wv.reload();
												   // TODO: Implement this method
												//   wv.loadUrl("")
											   }		
		}));
	}
	
	
	
	
	public class MyWebChromeOrder2Client extends WebChromeClient 
	{
		private Order2Fragment ma;


		public MyWebChromeOrder2Client (Order2Fragment temp){
			ma = temp;
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage cmsg)
		{

			if (cmsg.message().startsWith("ORDER2"))
			{
				String msg = cmsg.message().substring(6); // strip off prefix

				if(msg.equals("showCityPicker")){
					ma.showCityPicker();
				}
				
				if(msg.equals("onPageReady")){
					//ma.onPageFinished();
				}
				
			}


			return false;
			// TODO: Implement this method
			//return super.onConsoleMessage(consoleMessage);
		}
	}
	
	private class MyOrder2WebViewClient extends WebViewClient {
		@TargetApi(Build.VERSION_CODES.N)
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			//view.loadUrl(request.getUrl().toString());
			return handleUrl(view, request.getUrl());
		}


		// Для старых устройств
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//view.loadUrl(url);

			return handleUrl(view, Uri.parse(url));
		}

		private boolean handleUrl(WebView view, Uri uri){
			wv.loadUrl(uri.toString());
			return true;
		}

		@SuppressWarnings("deprecation")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			Uri uri = Uri.parse(url);

	
			if(isMyRes(uri) != null){
				InputStream gg = null;

				try
				{
					gg = getActivity() .getAssets().open(isMyRes(uri)[0]);
				}
				catch (IOException e)
				{}

				return new WebResourceResponse(
					isMyRes(uri)[1], "utf-8", gg);				
			}else{
				return super.shouldInterceptRequest(view, url);
			}
		}



		@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {	
			Uri uri = request.getUrl();
			

			if(isMyRes(uri) != null){
				InputStream gg = null;
				
				try
				{
					gg = getActivity() .getAssets().open(isMyRes(uri)[0]);
				}
				catch (IOException e)
				{}

				return new WebResourceResponse(
					isMyRes(uri)[1], "utf-8", gg);				
			}else{
				return super.shouldInterceptRequest(view, request);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			pb.setVisibility(View.GONE);
			wv.setVisibility(View.VISIBLE);


			super.onPageFinished(view, url);
		}

	
		private String[] isMyRes(Uri uri){
			String[] arr = new String[2];
			if(uri.toString().startsWith("https://www.chitai-gorod.ru/index.php")){
				arr[0] = "FragmentOrder2/index.html";
				arr[1] = "text/html";
				return arr;
			}
			if(uri.getPathSegments().get(uri.getPathSegments().size() - 1).toString().startsWith("style.min.css")){
				arr[0] = "FragmentOrder2/style.min.css";
				arr[1] = "text/css";
				return arr;
			}
			return null;
		}
	}
}
