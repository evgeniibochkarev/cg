package ru.chitaigorod.android.UX.dialogs;



import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.utils.*;

import android.support.v4.app.DialogFragment;

public class CategoryPickerDialogFragment extends BaseDialogFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		try
		{
			String method = json.getString("method");
			if(method.equals("getDataCategoryList")){
				
				HashMap<Integer, Boolean> result = new HashMap<>();

				JSONArray buck = json.getJSONObject("data").getJSONObject("agg_terms_ibl_sec_id").getJSONArray("buckets");
				for(int i = 0; i < buck.length(); i++){
					if(((JSONObject)buck.get(i)).getInt("doc_count") > 2)
						result.put(((JSONObject)buck.get(i)).getInt("key"), true);
				}
			
				mAdapter.setFilter(EntryCategory.getCatalog(),result);
						
			}
		}
		catch (JSONException e)
		{}
	}
	
	private String query;
	private CategoryPickerDialogInterface mInterface;
	private CategoryDialogAdapter mAdapter;
	private EntryCategory mTreeCategories;

	public static CategoryPickerDialogFragment newInstance(String query, CategoryPickerDialogInterface _interface) {
        CategoryPickerDialogFragment categoryDialogFragment = new CategoryPickerDialogFragment();

   		categoryDialogFragment.query = query;
        categoryDialogFragment.mInterface = _interface;
        return categoryDialogFragment;
    }
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullscreen);
    }

	@Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
			//window.setGravity(Gravity.RIGHT);
            //window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_category_picker, container, false);
		Toolbar toolbar = (Toolbar)view.findViewById(R.id.dialog_category_picker_toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		toolbar.setNavigationOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					dismiss();
				}
		});
		
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initCategories();
		
		RecyclerView rv = (RecyclerView)view.findViewById(R.id.dialog_category_picker_rv);
		
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		rv.setLayoutManager(llm);
		
		
		final HashMap fi = new HashMap<Integer, Boolean>();

				
		mAdapter = new CategoryDialogAdapter(getActivity(), new CategoryPickerDialogInterface(){
				@Override
				public void onSelect(EntryCategory eCat)
				{
					mInterface.onSelect(eCat);
					dismiss();
					//Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
				}
		});
		//mAdapter.setCategory(EntryCategory.getCatalog());
		rv.setAdapter(mAdapter);
		
	}
	
	
	
	
	private void initCategories(){
		JSONObject hm = new JSONObject();
		try
		{
			hm.put("query", query);
			
			mFragmentNavigation.get(APIHelper.getData("search.getDataCategoryList", hm));
		}
		catch (JSONException e)
		{}
		
	}
}
