package ru.chitaigorod.android.UX.dialogs;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import com.arlib.floatingsearchview.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.interfaces.*;

import android.support.v4.app.DialogFragment;
import ru.chitaigorod.android.R;
import ru.chitaigorod.android.utils.*;
import java.util.*;
import com.arlib.floatingsearchview.suggestions.model.*;

public class AuthorPickerDialogFragment extends BaseDialogFragment
{
	private String query;
	private AuthorPickerDialogInterface mInterface;
	private FloatingSearchView mSearchView;
	
	public static AuthorPickerDialogFragment newInstance(String query, AuthorPickerDialogInterface _interface) {
        AuthorPickerDialogFragment authorDialogFragment = new AuthorPickerDialogFragment();

   		authorDialogFragment.query = query;
        authorDialogFragment.mInterface = _interface;
        return authorDialogFragment;
    }

    
	@Override
	public void APIResponse(JSONObject json)
	{
		
		try
		{
			String method = json.getString("method");
			if(method.equals("getDataAuthorList")){
				List<Suggestion> result = new ArrayList<>();
				
				JSONArray buck = json.getJSONObject("data").getJSONObject("agg_terms_author_t.raw").getJSONArray("buckets");
				for(int i = 0; i < buck.length(); i++){
					if(((JSONObject)buck.get(i)).getInt("doc_count") > 2)
						result.add(new Suggestion(((JSONObject)buck.get(i)).getString("key")));
				}
				mSearchView.hideProgress();
				mSearchView.setSearchFocused(true);
				mSearchView.swapSuggestions(result);
				
			}
		}
		catch (JSONException e)
		{}
		

			
		
	}
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
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
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_author_picker, container, false);
		
		
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		mSearchView = (FloatingSearchView) view.findViewById(R.id.dialog_author_picker_search_view);
		setupSearchView();
	}
	
	
	private void setupSearchView(){
		JSONObject hm = new JSONObject();
		try
		{
			hm.put("query", query);
			hm.put("method", "query");
			mFragmentNavigation.get(APIHelper.getData("search.getDataAuthorList", hm));
		}
		catch (JSONException e)
		{}


		
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
				@Override
				public void onSearchTextChanged(String p1, String p2)
				{
					mSearchView.showProgress();
					JSONObject data = new JSONObject();
					try
					{
						
						data.put("query", p2);
						data.put("method", "author");
						mFragmentNavigation.get(APIHelper.getData( "search.getDataAuthorList", data));
					}
					catch (JSONException e)
					{}

				}

			});

		mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

				@Override
				public void onSuggestionClicked(SearchSuggestion p1)
				{
					Suggestion sg = (Suggestion) p1;
					mInterface.onAuthorChanged(sg.getBody());
					dismiss();
				}

				@Override
				public void onSearchAction(String p1)
				{
					// TODO: Implement this method
				}

			});	
	
	}
	
	private class Suggestion implements SearchSuggestion
	{

		@Override
		public void writeToParcel(Parcel p1, int p2)
		{
			// TODO: Implement this method
		}

		private String mName;
		
		public Suggestion(String suggestion) {
			this.mName = suggestion;
			
		}


		@Override
		public String getBody() {
			return mName;
		}

		@Override
		public int describeContents() {
			return 0;
		}


	}
}
