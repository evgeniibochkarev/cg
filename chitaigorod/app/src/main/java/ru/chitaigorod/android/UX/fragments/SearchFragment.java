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
import android.support.v7.widget.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.utils.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.UX.dialogs.*;
import android.support.v4.app.*;
import android.graphics.*;

public class SearchFragment extends BaseFragment
{	
	public static String TAG = "SearchFragment";
	
	private int page;
	private String query;
	private EntryElasticSearchFilter elasticFilter;
	private EntryRecyclerSearchFilter recyclerFilter;
	private View loadMoreProgress;
	private View emptyContentView;
	
	private FloatingSearchView mSearchView;
	private RecyclerView productsRecycler;
    private GridLayoutManager productsRecyclerLayoutManager;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private EndlessRecyclerScrollListener endlessRecyclerScrollListener;
	
	public static SearchFragment newInstance(){
		Bundle bundle = new Bundle();
		bundle.putString("query", "");
		//bundle.putSerializable("elastic_filter", (new EntryElasticSearchFilter()).getHashMap());
		bundle.putSerializable("recycler_filter", (new EntryRecyclerSearchFilter()).getHashMap());
		
		SearchFragment frg = new SearchFragment();
		frg.setArguments(bundle);

		return frg;
	}
	public static SearchFragment newInstance(String query, EntryRecyclerSearchFilter rFilter, EntryElasticSearchFilter eFilter){
		Bundle bundle = new Bundle();
		bundle.putString("query", query);
		//bundle.putSerializable("elastic_filter", eFilter.getHashMap());
		bundle.putSerializable("recycler_filter", rFilter.getHashMap());
		SearchFragment frg = new SearchFragment();
		frg.setArguments(bundle);

		return frg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		query = "";
		if(savedInstanceState == null){
			Bundle bundle = getArguments();
			page = 0;
			query = bundle.getString("query");
			elasticFilter = new EntryElasticSearchFilter();//new EntryElasticSearchFilter((HashMap)bundle.getSerializable("elastic_filter"));
			recyclerFilter = new EntryRecyclerSearchFilter((HashMap)bundle.getSerializable("recycler_filter"));
		}else{
			query = savedInstanceState.getString("query");
			elasticFilter = new EntryElasticSearchFilter();//new EntryElasticSearchFilter( (HashMap)savedInstanceState.getSerializable("elastic_filter"));
			recyclerFilter = new EntryRecyclerSearchFilter((HashMap)savedInstanceState.getSerializable("recycler_filter"));
			page = savedInstanceState.getInt("page", 0);
		}
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		this.emptyContentView = view.findViewById(R.id.category_products_empty);
        this.loadMoreProgress = view.findViewById(R.id.category_load_more_progress);
        this.mSearchView = (FloatingSearchView) view.findViewById(R.id.my_search_view);
		this.productsRecycler = (RecyclerView)view.findViewById(R.id.category_products_recycler);
        
        productsRecycler.setItemAnimator(new DefaultItemAnimator());
       // productsRecycler.setHasFixedSize(true);
        
	   
	   
	   
        productsRecyclerLayoutManager = new GridLayoutManager(getActivity(), 2);
        productsRecycler.setLayoutManager(productsRecyclerLayoutManager);
        endlessRecyclerScrollListener = new EndlessRecyclerScrollListener(productsRecyclerLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
               	page = currentPage;
				onGoSearch();    
            }
        };
        productsRecycler.addOnScrollListener(endlessRecyclerScrollListener);
        //productsRecyclerAdapter = new ProductsRecyclerAdapter();
		prepareRecyclerAdapter();
		productsRecycler.setAdapter(productsRecyclerAdapter);
		
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		

		
		
		setupSearchView();
		page = 0;
		onGoSearch();
	}
	
	private void setupSearchView(){
		mSearchView.setMenuItemIconColor(Color.parseColor("#FC6621"));
		mSearchView.setSearchText(query);
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

				@Override
				public void onSearchTextChanged(String p1, String p2)
				{
					endlessRecyclerScrollListener.clean();
					productsRecyclerAdapter.clear();
					query = p2;
					page = 0;
					//endlessRecyclerScrollListener.resetLoading();
					onGoSearch();
				}
				
		});
		/*
		mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
				@Override
				public void onHomeClicked() {
					((MainActivity) getActivity()).onBackPressed();
					//Log.d(TAG, "onHomeClicked()");
				}
			});*/
		mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
				@Override
				public void onActionMenuItemSelected(MenuItem item) {
					if (item.getItemId() == R.id.action_search_filter) {
						mFragmentNavigation.showDialog(FilterDialogFragment.newInstance(query,recyclerFilter,elasticFilter, new FilterDialogInterface(){
															   @Override
															   public void onFilterChanged(EntryRecyclerSearchFilter rFilter, EntryElasticSearchFilter eFilter)
															   {
																   recyclerFilter = rFilter;
																   elasticFilter = eFilter;
																   page = 0;
																   endlessRecyclerScrollListener.clean();
																   productsRecyclerAdapter.clear();
																   
																   
																   onGoSearch();														   }						
						}));
					}

				}
			});
		
	}
	
	private void onGoSearch(){
		mSearchView.showProgress();
		
		((MainActivity) getActivity()).get(APIHelper.getData("cart.getDataCart", new JSONObject()));
		JSONObject param = new JSONObject();
		try
		{
			param.putOpt("query", query);
			param.put("page", page);
		
			JSONObject jsonObject = new JSONObject();
			/*
			HashMap it = elasticFilter.getHashMap();
			
			for(Map.Entry entry : it.entrySet()) {
				String key =(String) entry.getKey();
				String value =(String) entry.getValue();
				jsonObject.put(key, value);
			}*/
			jsonObject.put("author", elasticFilter.getAuthor());
			if( elasticFilter.getCategory() != null) jsonObject.put("category", elasticFilter.getCategory().getId());
	
			param.putOpt("filter", jsonObject);
			
			((MainActivity) getActivity()).get(APIHelper.getData("search.getDataSearch", param));
		}
		catch (JSONException e)
		{}
		
		
	}
	
	
	
	@Override
	public void APIResponse(JSONObject json)
	{
		
		super.APIResponse(json);
		try
		{
			if (json.getString("method").equals("getDataSearch"))
			{
				mSearchView.hideProgress();
				
				JSONArray jsonArray = json.getJSONObject("data").getJSONObject("hits").getJSONArray("hits");
				
				ArrayList<Item_book> dataList = new ArrayList<Item_book>();     
				
				if (jsonArray != null) { 
					int len = jsonArray.length();
					for (int i=0;i<len;i++){ 
						Item_book ib = new Item_book(jsonArray.get(i));
						//(HashMap)Utils.toList( (jsonArray.get(i).toString() ));
						//String iblId = ib . getIblId();
						if(ib.getIblId().equals("30"))
							dataList.add(ib);
						
					} 
				} 
				if(dataList.size() > 0){
					//endlessRecyclerScrollListener.resetLoading();
					productsRecyclerAdapter.addProducts(dataList);
				}
			}
			if(json.getString("method").equals("getOtherData")){
				JSONObject jsonObject = json.getJSONObject("data");
				
				
				productsRecyclerAdapter.setOtherProp(jsonObject, recyclerFilter, elasticFilter);	
			}
		}
		catch (JSONException e)
		{}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		//outState.putSerializable("elastic_filter", elasticFilter.getHashMap());
		outState.putSerializable("recycler_filter", recyclerFilter.getHashMap());
		outState.putInt("page", this.page);
		outState.putString("query", this.query);
	
	}

	

	
	private void prepareRecyclerAdapter() {
        productsRecyclerAdapter = new ProductsRecyclerAdapter(getActivity(), new CategoryRecyclerInterface() {
				@Override
				public void onProductSelected(View caller, Item_book product) {
					mFragmentNavigation.pushFragment(ItemViewerFragment.newInstance(product.getBookId()));
				}
			});
    }
	public boolean onActivityBackPress()
	{
		// TODO: Implement this method
		return false;
	}
	
	
}
