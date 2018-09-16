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

	public class BaseFragment extends Fragment {

	protected FloatingSearchView mSearchView;
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
			void get(String str);
		}

		public  void APIResponse(JSONObject json){

		};

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			super.onViewCreated(view, savedInstanceState);

			ShopAppBar sab = (ShopAppBar)view.findViewById(R.id.my_search_view);
			if (sab != null){
				mSearchView = sab.getSearchView();

				View cartBtn = sab.getCartButton();
				cartBtn.setOnClickListener( new OnClickListener(){
						@Override
						public void onClick(View p1)
						{
							//((MainActivity)getActivity()).onCartSelected();
						}	
					});
			}
		}


	}
