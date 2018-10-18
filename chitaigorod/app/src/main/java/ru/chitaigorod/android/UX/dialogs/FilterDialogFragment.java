package ru.chitaigorod.android.UX.dialogs;


import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;

import android.support.v4.app.DialogFragment;
import ru.chitaigorod.android.UX.adapters.*;
import android.support.v7.widget.*;
import android.widget.CompoundButton.*;
import android.text.*;
import android.view.animation.*;

public class FilterDialogFragment extends BaseDialogFragment 
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
	}


    private EntryElasticSearchFilter elasticFilterData;
	private EntryRecyclerSearchFilter recyclerFilterData;
    private FilterDialogInterface filterDialogInterface;
	private String query;
	
	public static FilterDialogFragment newInstance(String query, EntryRecyclerSearchFilter recyclerFilter,EntryElasticSearchFilter elasticFilter, FilterDialogInterface filterDialogInterface) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        if (elasticFilter == null || filterDialogInterface == null) {
           // Timber.e(new RuntimeException(), "Created filterDialog with null parameters.");
            return null;
        }
		filterDialogFragment.query = query;
        filterDialogFragment.elasticFilterData = elasticFilter;
        filterDialogFragment.recyclerFilterData = recyclerFilter;
		filterDialogFragment.filterDialogInterface = filterDialogInterface;
        return filterDialogFragment;
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
           // window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_filter, container, false);
		
		//category
		RelativeLayout categoryRL = (RelativeLayout) view.findViewById(R.id.dialog_fragment_category_rl);
		final TextView categoryText = (TextView) view.findViewById(R.id.dialog_fragment_category_name);
		if(elasticFilterData.getCategory() != null){
			categoryText.setText(elasticFilterData.getCategory().getName());
		}
		categoryRL.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mFragmentNavigation.showDialog(CategoryPickerDialogFragment.newInstance(query, new CategoryPickerDialogInterface(){
														   @Override
														   public void onSelect(EntryCategory eCat)
														   {
															   categoryText.setText(eCat.getName());
															   elasticFilterData.setCategory(eCat);
															   filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
															   
														   }
														   												   												   				
													   }));
				}
			});
		
		// Author
		RelativeLayout authorRL = (RelativeLayout) view.findViewById(R.id.dialog_fragment_author_rl);
		final TextView authorText = (TextView) view.findViewById(R.id.dialog_fragment_author_name);
		if(!elasticFilterData.getAuthor().equals("not_set")){
			authorText.setText(elasticFilterData.getAuthor());
		}
		authorRL.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mFragmentNavigation.showDialog(AuthorPickerDialogFragment.newInstance(query, new AuthorPickerDialogInterface(){
														   @Override
														   public void onAuthorChanged(String author)
														   {
															   if(!author.equals("not_set"))   authorText.setText(author);
															   else authorText.setText("Все");
															   elasticFilterData.setAuthor(author);
															   filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
															    // mFragmentNavigation.showDialog(FilterDialogFragment.newInstance(query, recyclerFilterData, elasticFilterData, filterDialogInterface));
														   }												   				
					}));
				}
		});
		
		SwitchCompat inStock = (SwitchCompat) view.findViewById(R.id.dialog_fragment_switch_compat_in_stock);
		inStock.setChecked(recyclerFilterData.getAvailable());
		inStock.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					recyclerFilterData.setAvailable(p2);
					filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
				}
		});
		
		EditText maxPrice = (EditText) view.findViewById(R.id.dialog_fragment_price_max);
		if(recyclerFilterData.getMaxPrice() > 0.01) maxPrice.setText(recyclerFilterData.getMaxPrice().toString());
		maxPrice.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					if(p1.length() != 0){
						recyclerFilterData.setMaxPrice(Double.parseDouble(p1.toString()));
						
					}else{
						recyclerFilterData.setMaxPrice(0.0);	
						//filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
					}
					filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);	
				}
				@Override
				public void afterTextChanged(Editable p1)
				{					
				}				
		});
		
		EditText minPrice = (EditText) view.findViewById(R.id.dialog_fragment_price_min);
		if(recyclerFilterData.getMinPrice() > 0.01) minPrice.setText(recyclerFilterData.getMinPrice().toString());
		minPrice.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					if(p1.length() != 0){
						recyclerFilterData.setMinPrice(Double.parseDouble(p1.toString()));
					}else{
						recyclerFilterData.setMinPrice(0.0);
							//filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
						}
					filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);	
				}
				@Override
				public void afterTextChanged(Editable p1)
				{					
				}				
			});
		
		
		Button btnApply = (Button)view.findViewById(R.id.filter_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//String filterUrl = buildFilterUrl();
					//filterDialogInterface.onFilterSelected(filterUrl);
					 //filterDialogInterface.onFilterChanged(recyclerFilterData, elasticFilterData);
					dismiss();
				}
			});

        Button btnCancel = (Button) view.findViewById(R.id.filter_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					filterDialogInterface.onFilterChanged(new EntryRecyclerSearchFilter(), new EntryElasticSearchFilter());
					dismiss();
				}
				
				
			
		});
		
				
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

	
	
}


/*
import bf.io.openshop.R;
import bf.io.openshop.entities.filtr.DeserializerFilters;
import bf.io.openshop.entities.filtr.FilterType;
import bf.io.openshop.entities.filtr.FilterTypeColor;
import bf.io.openshop.entities.filtr.FilterTypeRange;
import bf.io.openshop.entities.filtr.FilterTypeSelect;
import bf.io.openshop.entities.filtr.Filters;
import bf.io.openshop.interfaces.FilterDialogInterface;
import bf.io.openshop.utils.RecyclerMarginDecorator;
import bf.io.openshop.UX.adapters.FilterRecyclerAdapter;
import timber.log.Timber;

public class FilterDialogFragment extends DialogFragment implements BaseFragment{

    private Filters filterData;
    private FilterDialogInterface filterDialogInterface;

    public static FilterDialogFragment newInstance(Filters filter, FilterDialogInterface filterDialogInterface) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        if (filter == null || filterDialogInterface == null) {
            Timber.e(new RuntimeException(), "Created filterDialog with null parameters.");
            return null;
        }
        filterDialogFragment.filterData = filter;
        filterDialogFragment.filterDialogInterface = filterDialogInterface;
        return filterDialogFragment;
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
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_filters, container, false);

        prepareFilterRecycler(view);

        Button btnApply = view.findViewById(R.id.filter_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String filterUrl = buildFilterUrl();
					filterDialogInterface.onFilterSelected(filterUrl);
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
			});
        return view;
    }

    private void prepareFilterRecycler(View view) {
        RecyclerView filterRecycler = view.findViewById(R.id.filter_recycler);
        filterRecycler.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.VERTICAL));
        filterRecycler.setItemAnimator(new DefaultItemAnimator());
        filterRecycler.setHasFixedSize(true);
        filterRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        FilterRecyclerAdapter filterRecyclerAdapter = new FilterRecyclerAdapter(getActivity(), filterData);
        filterRecycler.setAdapter(filterRecyclerAdapter);
    }

    private String buildFilterUrl() {
        StringBuilder filterUrl = new StringBuilder();

        for (FilterType filterType : filterData.getFilters()) {
            switch (filterType.getType()) {
                case DeserializerFilters.FILTER_TYPE_COLOR:
                    FilterTypeColor filterTypeColor = (FilterTypeColor) filterType;
                    if (filterTypeColor.getSelectedValue() != null) {
                        filterUrl.append("&").append(filterType.getLabel()).append("=").append(filterTypeColor.getSelectedValue().getId());
                    }
                    break;
                case DeserializerFilters.FILTER_TYPE_SELECT:
                    FilterTypeSelect filterTypeSelect = (FilterTypeSelect) filterType;
                    if (filterTypeSelect.getSelectedValue() != null) {
                        filterUrl.append("&").append(filterType.getLabel()).append("=").append(filterTypeSelect.getSelectedValue().getId());
                    }
                    break;
                case DeserializerFilters.FILTER_TYPE_RANGE:
                    FilterTypeRange filterTypeRange = (FilterTypeRange) filterType;
                    if (filterTypeRange.getSelectedMin() >= 0 && filterTypeRange.getSelectedMax() > 0) {
                        filterUrl.append("&").append(filterType.getLabel()).append("=").append(filterTypeRange.getSelectedMin()).append("|").append(filterTypeRange.getSelectedMax());
                    }
                    break;
                default:
                    Timber.e("Unknown filter type.");
                    break;
            }
        }

        Timber.d("BuildFilterUrl - %s", filterUrl.toString());
        return filterUrl.toString();
    }
	}
	*/
