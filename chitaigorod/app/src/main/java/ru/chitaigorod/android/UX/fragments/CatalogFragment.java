package ru.chitaigorod.android.UX.fragments;
import android.os.*;
import android.view.*;
import org.json.*;
import ru.chitaigorod.android.R;

import java.util.*;

public class CatalogFragment extends BaseFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
	}


	public static String TAG = "CatalogFragment";
	public static CatalogFragment newInstance(){
		CatalogFragment frag = new CatalogFragment();
		return frag;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.main_page_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		super.onViewCreated(view, savedInstanceState);
	}
	

	
	
}
