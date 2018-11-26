package ru.chitaigorod.android.UX.fragments;
import android.annotation.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.dialogs.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.utils.*;

public class CabinetFragment extends BaseFragment implements AuthDialogInterface
{
	private String[] mSnipperData = {"Заказы"};
	private WebView wv;
	private ProgressBar pb;
	private RelativeLayout loginBtn;
	private EditText tv;
	
	@Override
	public void success()
	{
		loginBtn.setVisibility(View.GONE);
	}				
	
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
			if(method.equals("CabinetFragment.showAuth")){
				mFragmentNavigation.showDialog(AuthDialogFragment.newInstance(this));
			}
			if(method.equals("CabinetFragment.hideAuthBtn")){
				loginBtn.setVisibility(View.GONE);
			}
			
			wv.loadUrl("https://www.chitai-gorod.ru/profile/orders/");
		}
		catch (JSONException e)
		{}

		super.APIResponse(json);
	}
 
	public void writeFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){      
		String root = Environment.getExternalStorageDirectory().toString();
		File file = new File(root+"/");
		if(!file.exists()){
			file.mkdir();
		}

		try{
			File gpxfile = new File(file, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();

		}catch (Exception e){
			e.printStackTrace();

		}
	}

	public static CabinetFragment newInstance(){
		return new CabinetFragment();
	}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Timber.d("%s - onCreateView", this.getClass().getSimpleName());

        View view = inflater.inflate(R.layout.fragment_cabinet, container, false);

		/*
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
			});*/

		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_dropdown_item, mSnipperData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.cabinet_spinner);
        spinner.setAdapter(adapter);
	/*	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String item = (String) parent.getItemAtPosition(position);
					((TextView) parent.getChildAt(0)).setTextColor(0x00000000);

					
					TextView tv = ((TextView) parent.getChildAt(0));
					if(tv != null) tv .setTextColor(Color.WHITE); /* if you want your item to be white
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});*/
        // заголовок
       // spinner.setPrompt("Title");
        // выделяем элемент 
        spinner.setSelection(0);
		
		
		final AuthDialogInterface authInterf = (AuthDialogInterface) this;
		loginBtn = (RelativeLayout) view.findViewById(R.id.fragment_cabinet_login_btn);
		loginBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mFragmentNavigation.showDialog(AuthDialogFragment.newInstance(authInterf));
				}			
		});
		
		pb = (ProgressBar) view.findViewById(R.id.cabinet_pb);
		wv = (WebView) view.findViewById(R.id.cabinet_webview);
		wv.setWebViewClient(new MyCabinetWebViewClient());

		wv.getSettings().setJavaScriptEnabled(true);

		mFragmentNavigation.get(APIHelper.getData("CabinetFragment.getProfilePage", "{}"));
		return view;
	}
	private class MyCabinetWebViewClient extends WebViewClient {
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
			List<String > whiteList = new ArrayList();
			whiteList.add("https://ad.admitad.");
			whiteList.add("https://www.chitai-gorod.ru/profile/orders/");		
			//whiteList.add("https://securepayments.sberbank.ru");

			for(String wl : whiteList)
				if( uri.toString ().startsWith(wl)){
					view.loadUrl(uri.toString());
					return false;
				}
			return true;
		}

		@SuppressWarnings("deprecation")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			Uri uri = Uri.parse(url);

			/*
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

			 String docty = file.contains("css/") ? "text/css" : "text/html";
			 ByteArrayInputStream EMPTY = new ByteArrayInputStream(tContents.getBytes());
			 return new WebResourceResponse(docty, "utf-8", EMPTY);					

			 } catch (IOException e) {
			 // Handle exceptions here
			 }
			 }*/
			//String buff = Utils.getJSByTag((AppCompatActivity)getApplicationContext(), isMyRes(uri));
			if(isMyRes(uri) != null){
				InputStream gg = null;
				try
				{
					gg = view.getContext().getAssets().open(isMyRes(uri));
				}
				catch (IOException e)
				{}

				String docty = isMyRes(uri).contains("css/") ? "text/css" : "text/html";


				return new WebResourceResponse(
					docty, "utf-8", gg);				

			}else{
				return super.shouldInterceptRequest(view, url);
			}
		}



		@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {	
			Uri uri = request.getUrl();

			/*
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

			 String docty = file.contains("css/") ? "text/css" : "text/html";
			 ByteArrayInputStream EMPTY = new ByteArrayInputStream(tContents.getBytes());
			 return new WebResourceResponse(docty, "utf-8", EMPTY);					

			 } catch (IOException e) {
			 // Handle exceptions here
			 }
			 }*/

			//Stringbuff = Utils.getJSByTag((AppCompatActivity)getApplicationContext(), isMyRes(uri));
			if(isMyRes(uri) != null){
				InputStream gg = null;
				try
				{
					gg = getActivity() .getAssets().open(isMyRes(uri));
				}
				catch (IOException e)
				{}

				String docty = isMyRes(uri).contains("css/") ? "text/css" : "text/html";


				return new WebResourceResponse(
					docty, "utf-8", gg);				
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


		private String isMyRes(Uri uri){
			if(uri.getPath().contains("style.min.css")){
				return "css/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
			}
			if(uri.getPath().contains("new-preview.css")){
				return "css/"+ uri.getPathSegments().get(uri.getPathSegments().size() - 1);
			}
			if(uri.toString().contains(".css")){
				return "css/new-preview.css";
			}

			return null;
		}
	}

}
