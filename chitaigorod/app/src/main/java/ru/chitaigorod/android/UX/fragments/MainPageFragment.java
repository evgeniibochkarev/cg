package ru.chitaigorod.android.UX.fragments;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.interfaces.*;
import android.widget.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.utils.*;

public class MainPageFragment extends BaseFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
		
		try
		{
			String method = json.getString("method");
			JSONArray data = json.getJSONArray("data");
			
			if(method.contains("MainPageFragment.getHtml")){
				rv0Text.setText(((JSONObject)data.get(0)).getString("title"));
				
				
				rv0Adapter.addProducts(((JSONObject)data.get(0)).getJSONArray("body"));
				rv1Adapter.addProducts(((JSONObject)data.get(1)).getJSONArray("body"));	
				rv2Adapter.addProducts(((JSONObject)data.get(2)).getJSONArray("body"));
				rv3Adapter.addProducts(((JSONObject)data.get(3)).getJSONArray("body"));
				
				//String body = ((JSONObject) data.get(0)).getString("body");
				//wv.loadUrl("javascript:document.getElementById('new-book').innerHTML = ' "+body+"'");
			} 
		}
		catch (JSONException e)
		{}
	}
	
	private RecyclerView rv0;
	private AppCompatTextView rv0Text;
    private MainPageRecyclerAdapter rv0Adapter;
	
	private RecyclerView rv1;
	private AppCompatTextView rv1Text;
    private MainPageRecyclerAdapter rv1Adapter;
	
	private RecyclerView rv2;
	private AppCompatTextView rv2Text;
    private MainPageRecyclerAdapter rv2Adapter;
	
	private RecyclerView rv3;
	private AppCompatTextView rv3Text;
    private MainPageRecyclerAdapter rv3Adapter;
	
	
    public static MainPageFragment newInstance(){
		MainPageFragment frag = new MainPageFragment();
		return frag;
	}
	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_page_fragment, container, false);
		
		//mFragmentNavigation.get(APIHelper.getData("MainPageFragment.getHtml", "{}"));
		rv0Text = (AppCompatTextView) view.findViewById(R.id.fragment_main_page_textView_rv0);
		rv1Text = (AppCompatTextView) view.findViewById(R.id.fragment_main_page_textView_rv1);
		rv2Text = (AppCompatTextView) view.findViewById(R.id.fragment_main_page_textView_rv2);
		rv3Text = (AppCompatTextView) view.findViewById(R.id.fragment_main_page_textView_rv3);
		
		
		rv0 = (RecyclerView) view.findViewById(R.id.fragment_main_page_recyclerView0);		
		rv0.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));	
        rv0Adapter = new MainPageRecyclerAdapter(getActivity(), new MainPageRecyclerInterface() {
				@Override
				public void onItemSeleted(String id)
				{
					mFragmentNavigation.pushFragment(ItemViewerFragment.newInstance(id));
				}						
		});
		rv0.setAdapter(rv0Adapter);
		
		
		rv1 = (RecyclerView) view.findViewById(R.id.fragment_main_page_recyclerView1);		
		rv1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));	
        rv1Adapter = new MainPageRecyclerAdapter(getActivity(), new MainPageRecyclerInterface() {
				@Override
				public void onItemSeleted(String id)
				{
					mFragmentNavigation.pushFragment(ItemViewerFragment.newInstance(id));
				}						
			});
		rv1.setAdapter(rv0Adapter);
		
		
		rv2 = (RecyclerView) view.findViewById(R.id.fragment_main_page_recyclerView2);		
		rv2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));	
        rv2Adapter = new MainPageRecyclerAdapter(getActivity(), new MainPageRecyclerInterface() {
				@Override
				public void onItemSeleted(String id)
				{
					mFragmentNavigation.pushFragment(ItemViewerFragment.newInstance(id));
				}						
			});
		rv2.setAdapter(rv0Adapter);
		
		
		rv3 = (RecyclerView) view.findViewById(R.id.fragment_main_page_recyclerView3);		
		rv3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));	
        rv3Adapter = new MainPageRecyclerAdapter(getActivity(), new MainPageRecyclerInterface() {
				@Override
				public void onItemSeleted(String id)
				{
					mFragmentNavigation.pushFragment(ItemViewerFragment.newInstance(id));
				}						
			});
		rv3.setAdapter(rv0Adapter);
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		mFragmentNavigation.get(APIHelper.getData("MainPageFragment.getHtml", "{}"));
		
	}

	
	

    
}
