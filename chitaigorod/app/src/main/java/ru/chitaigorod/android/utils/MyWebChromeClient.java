package ru.chitaigorod.android.utils;
import android.webkit.*;
import org.json.*;
import android.support.v4.app.*;
import java.util.*;

import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.fragments.*;

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

		if (cmsg.message().startsWith("MAGIC"))
		{
			String msg = cmsg.message().substring(5); // strip off prefix

			try
			{
				JSONObject resp = new JSONObject(msg);
				int _ctxId = resp.getInt("ctxId");

				
				FragmentManager fm = ma.getSupportFragmentManager();
				List fr = fm.getFragments();

				for(Fragment _fr : fr){
					if(_fr instanceof BaseFragment)
						if(((BaseFragment)_fr).getId() == _ctxId){
							((BaseFragment)_fr).APIResponse(resp);
						}
				}
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
