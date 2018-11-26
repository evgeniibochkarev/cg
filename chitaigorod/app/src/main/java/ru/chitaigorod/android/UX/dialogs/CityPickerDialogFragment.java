package ru.chitaigorod.android.UX.dialogs;


import android.app.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.webkit.*;
import java.io.*;
import org.json.*;
import ru.chitaigorod.android.*;

import android.support.v4.app.DialogFragment;

public class CityPickerDialogFragment extends BaseDialogFragment 
{
	
	@Override
	public void APIResponse(JSONObject json)
	{
		try
		{
			JSONArray data = json.getJSONArray("data");				
		}
		catch (JSONException e)
		{}
		// TODO: Implement this method
	}


    
    private WebView wv;

	public static CityPickerDialogFragment newInstance() {
        CityPickerDialogFragment dialogFragment = new CityPickerDialogFragment();
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }

	@Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_city_picker, container, false);

		wv = (WebView) view.findViewById(R.id.dialog_city_webview);

		wv.setWebViewClient(new MyWebViewCityClient());
		//wv.setWebChromeClient(new MyWebChromeClient(getActivity()));
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);

		
		wv.loadUrl("https://www.chitai-gorod.ru/index.php");
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{	
		super.onViewCreated(view, savedInstanceState);	
	}
	
	private class MyWebViewCityClient extends WebViewClient 
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
			if(uri.toString() == "https"){
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
			if(uri.toString().equals("https://www.chitai-gorod.ru/index.php")){
				return "DialogCityPicker/index.html";
			}
			
			return null;
		}
	
	}
	
}
