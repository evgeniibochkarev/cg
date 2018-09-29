package ru.chitaigorod.android.UX.fragments;



import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import com.arlib.floatingsearchview.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.custom_view.*;

import ru.chitaigorod.android.R;

	/**
	 * Created by f22labs on 07/03/17.
	 */

	public abstract class BaseFragment extends Fragment {

	
		FragmentNavigation mFragmentNavigation;

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

		}


		@Override
		public void onAttach(Context context) {
			super.onAttach(context);
			if (context instanceof FragmentNavigation) {
				mFragmentNavigation = (FragmentNavigation) context;
			}
		}

		public interface FragmentNavigation {
			void pushFragment(Fragment fragment);
			void showDialog(DialogFragment frg);
			void get(String str);
			void setCartBadge(int count);
		}

		
		public void APIResponse(JSONObject json){
			try
			{
				if (json.getString("method").equals("getDataCart"))
				{
					JSONArray data = json.getJSONObject("data").getJSONObject("results").getJSONArray("basket");
					mFragmentNavigation.setCartBadge(data.length());
				}
			}
			catch (JSONException e)
			{}
		}

		

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			super.onViewCreated(view, savedInstanceState);
		}


	}
