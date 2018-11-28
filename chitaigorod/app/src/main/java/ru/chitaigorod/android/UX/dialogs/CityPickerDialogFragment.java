package ru.chitaigorod.android.UX.dialogs;




import android.app.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import android.widget.TextView.*;
import java.io.*;
import org.json.*;
import ru.chitaigorod.android.*;

import android.support.v7.widget.Toolbar;
import ru.chitaigorod.android.interfaces.*;


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
	private ProgressBar pg;
	private AutoCompleteTextView til;
	private CityPickerDialogInterface mInterface;
	
	public static CityPickerDialogFragment newInstance(CityPickerDialogInterface in) {
        CityPickerDialogFragment dialogFragment = new CityPickerDialogFragment();
        dialogFragment.mInterface = in;
		return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullscreen);
    }

	@Override
    public void onStart() {
        super.onStart();
		
        Dialog d = getDialog();
	
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            //window.setLayout(width, height);
			window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
			
			window.setDimAmount(0.5f);
			
            //window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_city_picker, container, false);
		
		
		Toolbar tb = (Toolbar) view.findViewById(R.id.dialog_city_picker_toolbar);
		tb.setTitle("Ваш город");
		
		pg = (ProgressBar) view.findViewById(R.id.dialogcitypickerProgressBar1);
		wv = (WebView) view.findViewById(R.id.dialog_city_webview);

		wv.setWebViewClient(new MyWebViewCityClient());
		wv.setWebChromeClient(new MyWebChromeCityClient(this));
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);

		
		wv.loadUrl("https://www.chitai-gorod.ru/index.php");
        
		til = (AutoCompleteTextView) view.findViewById(R.id.dialog_city_name_text);

		til.addTextChangedListener(new TextWatcher() {
				@Override 
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					wv.loadUrl("javascript:userCity.findCity('"+til.getText()+"')");
				} 

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {                
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
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
	public void onPageFinished(){
		pg.setVisibility(View.GONE);
		til.setVisibility(View.VISIBLE);
	}
	public void onSelect(){
		pg.setVisibility(View.VISIBLE);
		til.setVisibility(View.GONE);
		wv.setVisibility(View.GONE);
	}
	public void onClose(){
		if(mInterface != null)
			mInterface.onCityUpdate(true);
			dismiss();
	}
	public void onResize(float h){
		Dialog d = getDialog();
        if (d != null) {
			wv.setVisibility(View.GONE);
			wv.setVisibility(View.VISIBLE);
			//wv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (h * getResources().getDisplayMetrics().density )));
            int width = (int)(.9f * getResources().getDisplayMetrics().widthPixels);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            //window.setWindowAnimations(R.style.alertDialogAnimation);
        
				
		}
	}
	public class MyWebChromeCityClient extends WebChromeClient 
	{
		private CityPickerDialogFragment ma;


		public MyWebChromeCityClient (CityPickerDialogFragment temp){
			ma = temp;
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage cmsg)
		{
			//Log.d(Tag, cmsg.message());
			


			if (cmsg.message().startsWith("CITY"))
			{
				String msg = cmsg.message().substring(4); // strip off prefix

				if(msg.equals("close")){
					ma.onClose();
				}
				if(msg.startsWith("resize")){
					float he = Float.parseFloat( msg.substring(6));
					ma.onResize(he);
				}
				if(msg.equals("onPageReady")){
					ma.onPageFinished();
				}
				if(msg.equals("onSelect")){
					ma.onSelect();
				}
			}
			

			return false;
			// TODO: Implement this method
			//return super.onConsoleMessage(consoleMessage);
		}






	}
	
}
