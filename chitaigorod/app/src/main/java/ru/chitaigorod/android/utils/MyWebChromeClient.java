package ru.chitaigorod.android.utils;
import android.webkit.*;
import org.json.*;
import android.support.v4.app.*;
import java.util.*;

import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.fragments.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.UX.dialogs.*;
import android.view.*;

public class MyWebChromeClient extends WebChromeClient 
{
	private MainActivity ma;
	
	
	public MyWebChromeClient (MainActivity temp){
		ma = temp;
	}
	
	@Override
	public boolean onConsoleMessage(ConsoleMessage cmsg)
	{
		//Log.d(Tag, cmsg.message());
		if (cmsg.message().startsWith("DEB"))
		{
			String dd = cmsg.message();
		}
		
		if (cmsg.message().startsWith("MAIN"))
		{
			String msg = cmsg.message().substring(4); // strip off prefix
			
			try
			{
				JSONObject resp = new JSONObject(msg);
				
				String method = resp.getString("method");
				if(method.equals("jsIsLoaded")){
					ma.findViewById(R.id.load_app).setVisibility(View.GONE);
				}
				if(method.equals("showCityPickerDialog")){
			
					ma.showDialog(CityPickerDialogFragment.newInstance( ));
				}
			}
			catch (JSONException e)
			{}
		}
		if (cmsg.message().startsWith("MAGIC"))
		{
			String msg = cmsg.message().substring(5); // strip off prefix

			try
			{
				JSONObject resp = new JSONObject(msg);
				//int _ctxId = resp.getInt("ctxId");

				/*
				FragmentManager fm = ma.getSupportFragmentManager();
				
				List fr = fm.getFragments();

				
				for(Fragment _fr : fr){
					if(_fr instanceof BaseFragment)
						if(((BaseFragment)_fr).getId() == _ctxId){
							((BaseFragment)_fr).APIResponse(resp);
						}
					if(_fr instanceof BaseDialogFragment)
						if(((BaseDialogFragment)_fr).getId() == _ctxId){
							((BaseDialogFragment)_fr).APIResponse(resp);
						}
				}*/
				
				BaseDialogFragment bdf = ((BaseDialogFragment)ma.getFragmentController().getCurrentDialogFrag());
				if(bdf != null) bdf.APIResponse(resp);
				((BaseFragment)ma.getFragmentController().getCurrentFrag()).APIResponse(resp);
			}
			catch (JSONException e)
			{}



			//mListener.onPageSuccess(msg);
			/* process HTML */

			return true;
		}

		return false;
		// TODO: Implement this method
		//return super.onConsoleMessage(consoleMessage);
	}






}
