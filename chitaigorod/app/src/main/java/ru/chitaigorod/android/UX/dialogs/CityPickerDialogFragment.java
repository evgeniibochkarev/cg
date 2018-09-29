package ru.chitaigorod.android.UX.dialogs;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.arlib.floatingsearchview.*;
import com.arlib.floatingsearchview.suggestions.*;
import com.arlib.floatingsearchview.suggestions.model.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.utils.*;

import android.support.v4.app.DialogFragment;
import ru.chitaigorod.android.R;


public class CityPickerDialogFragment extends BaseDialogFragment 
{
	private class Suggestion implements SearchSuggestion
	{

		@Override
		public void writeToParcel(Parcel p1, int p2)
		{
			// TODO: Implement this method
		}
		

		private String mName;
		private String mId;
		
		public Suggestion(String suggestion, String id) {
			this.mName = suggestion.toLowerCase();
			this.mId = id;
		}
/*
		public ColorSuggestion(Parcel source) {
			this.mColorName = source.readString();
			this.mIsHistory = source.readInt() != 0;
		}
*/
		

		@Override
		public String getBody() {
			return mName;
		}

		public String getId(){
			return this.mId;
		}
		

		@Override
		public int describeContents() {
			return 0;
		}

				
	}
	@Override
	public void APIResponse(JSONObject json)
	{
		try
		{
			JSONArray data = json.getJSONArray("data");
			List<Suggestion> result = new ArrayList<>();
			for(int i = 0; i < data.length(); i++){
				String name = data.getJSONArray(i).getString(0);
				String id = data.getJSONArray(i).getString(1);
				result.add(new Suggestion(name, id));
			}
			mSearchView.hideProgress();
			mSearchView.swapSuggestions(result);
		}
		catch (JSONException e)
		{}
		// TODO: Implement this method
	}


    
    FloatingSearchView mSearchView;

	public static CityPickerDialogFragment newInstance( ) {
        CityPickerDialogFragment dialogFragment = new CityPickerDialogFragment();
        return dialogFragment;
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
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_city_picker, container, false);

		mSearchView = (FloatingSearchView) view.findViewById(R.id.dialog_city_picker_search_view);
		
		// prepareFilterRecycler(view);
		/*
		 Button btnApply = view.findViewById(R.id.filter_btn_apply);
		 btnApply.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		 //String filterUrl = buildFilterUrl();
		 //filterDialogInterface.onFilterSelected(filterUrl);
		 dismiss();
		 }
		 });

		 Button btnCancel = view.findViewById(R.id.filter_btn_cancel);
		 btnCancel.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		 // Clear all selected values
		 if (filterData != null) {
		 for (FilterType filterType : filterData.getFilters()) {
		 switch (filterType.getType()) {
		 case DeserializerFilters.FILTER_TYPE_RANGE:
		 ((FilterTypeRange) filterType).setSelectedMin(-1);
		 ((FilterTypeRange) filterType).setSelectedMax(-1);
		 break;
		 case DeserializerFilters.FILTER_TYPE_COLOR:
		 ((FilterTypeColor) filterType).setSelectedValue(null);
		 break;
		 case DeserializerFilters.FILTER_TYPE_SELECT:
		 ((FilterTypeSelect) filterType).setSelectedValue(null);
		 break;
		 }
		 }
		 }
		 filterDialogInterface.onFilterCancelled();
		 dismiss();
		 }
		 });*/
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		super.onViewCreated(view, savedInstanceState);
		setupSearchView();
	}
	
	
	private void setupSearchView(){

		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
				@Override
				public void onSearchTextChanged(String p1, String p2)
				{
					mSearchView.showProgress();
					JSONObject data = new JSONObject();
					try
					{
						data.put("query", p2);
						mFragmentNavigation.get(APIHelper.getData( "city.getDataSuggCity", data));
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
					JSONObject data = new JSONObject();
					
					try
					{
						data.put("cityId",sg.getId());
						data.put("cityName", sg.getBody());
					}
					catch (JSONException e)
					{}

					mFragmentNavigation.get(APIHelper.getData( "city.setCity", data));
					dismiss();
				}

				@Override
				public void onSearchAction(String p1)
				{
					// TODO: Implement this method
				}
				
		});	
	}
}
