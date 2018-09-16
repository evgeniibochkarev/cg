package ru.chitaigorod.android.UX.fragments;

import org.json.*;
import android.os.*;
import android.view.*;
import ru.chitaigorod.android.R;
import java.util.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.utils.*;
import com.arlib.floatingsearchview.*;
import java.security.acl.*;

public class SearchFragment extends BaseFragment
{
	public static String TAG = "SearchFragment";
	
	private int page;
	private String lastQuery;
	private SearchFilter filter;
	
	
	
	public static SearchFragment newInstance(String query, HashMap filter){
		Bundle bundle = new Bundle();
		bundle.putString("query", query);
		bundle.putSerializable("filter", filter);
		
		SearchFragment frg = new SearchFragment();
		frg.setArguments(bundle);
		
		return frg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		

		
		if(savedInstanceState == null){
			Bundle bundle = getArguments();
			String query = bundle.getString("query");
			filter = new SearchFilter((HashMap)bundle.getSerializable("filter"));
		}else{
			lastQuery = savedInstanceState.getString("query");
			filter = new SearchFilter( (HashMap)savedInstanceState.getSerializable("filter"));
			page = savedInstanceState.getInt("page", 0);
		}
		setupSearchView();
	}
	
	private void setupSearchView(){
		mSearchView.setSearchText(lastQuery);
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

				@Override
				public void onSearchTextChanged(String p1, String p2)
				{
					lastQuery = p2;
					onGoSearch(p2, filter);
				}
				
		});
		mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
				@Override
				public void onHomeClicked() {
					((MainActivity) getActivity()).onBackPressed();
					//Log.d(TAG, "onHomeClicked()");
				}
			});
		
	}
	
	private void onGoSearch(String query, SearchFilter filter){
		mSearchView.showProgress();
		
		JSONObject param = new JSONObject();
		try
		{
			param.putOpt("query", query);
			param.put("page", page);
		
			JSONObject jsonObject = new JSONObject();
			
			HashMap it = filter.getFilter();
			for(Map.Entry entry : it.entrySet()) {
				String key =(String) entry.getKey();
				String value =(String) entry.getValue();
				jsonObject.put(key, value);
			}
			param.putOpt("filter", jsonObject);
			
			((MainActivity) getActivity()).get(APIHelper.getData(getId(), "getDataSearch", param));
		}
		catch (JSONException e)
		{}
		
		
	}
	
	
	
	@Override
	public void APIResponse(JSONObject json)
	{
		try
		{
			if (json.getString("method").equals("getDataSearch"))
			{
				mSearchView.hideProgress();
			}
		}
		catch (JSONException e)
		{}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("filter", filter.getFilter());
		outState.putInt("page", this.page);
		outState.putString("query", this.lastQuery);
	}

	
	
	public boolean onActivityBackPress()
	{
		// TODO: Implement this method
		return false;
	}
	
	
}
