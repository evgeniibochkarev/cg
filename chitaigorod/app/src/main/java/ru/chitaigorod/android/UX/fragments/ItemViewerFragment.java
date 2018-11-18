package ru.chitaigorod.android.UX.fragments;

import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.squareup.picasso.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.UX.custom_view.*;
import ru.chitaigorod.android.UX.dialogs.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.utils.*;

import android.support.v7.widget.Toolbar;

public class ItemViewerFragment extends BaseFragment
{
	private Item_book item;
	private ProgressBar progressView;

    // Fields referencing complex screen layouts.
    private View layoutEmpty;
    private RelativeLayout productContainer;
    private ScrollView contentScrollLayout;

	private ButtonAddToCart productAddToCart;
    // Fields referencing product related views.
    private ResizableImageViewHeight productMainImage;
	private TextView productName;
	private TextView productAuthor;
    private TextView productPriceDiscount;
    private TextView productPrice;
    private TextView productDiscription;
    private TextView productPriceDiscountPercent;

	private RecyclerView productPropRecycler;
	private RecyclerView productImagesRecycler;
	private ArrayList<String> productImagesUrls;
	private ProductImagesRecyclerAdapter productImagesAdapter;

	private String id;

	private Toolbar toolbar;
    /**
     * Refers to the displayed product.
     */
   
    /**
     * Refers to a user-selected product variant
     */
	
	
	public static ItemViewerFragment newInstance(String id){
		ItemViewerFragment itemViewer = new ItemViewerFragment();
		
		itemViewer.id = id;
		//bundle.putSerializable("elastic_filter", (new EntryElasticSearchFilter()).getHashMap());
		
		return itemViewer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.item_vewer_fragment, container, false);
		
		toolbar = (Toolbar)view.findViewById(R.id.fragment_itemViewer_toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		
		toolbar.setNavigationOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					getActivity().onBackPressed();
				}
			});

		
		progressView = (ProgressBar) view.findViewById(R.id.product_progress);

        productContainer = (RelativeLayout) view.findViewById(R.id.product_container);
        layoutEmpty = view.findViewById(R.id.product_empty_layout);
        contentScrollLayout = (ScrollView) view.findViewById(R.id.product_scroll_layout);

		productAddToCart = (ButtonAddToCart) view.findViewById(R.id.product_add_to_cart_button);
        productName = (TextView) view.findViewById(R.id.product_name);
        productAuthor = (TextView) view.findViewById(R.id.product_author);	
		productPriceDiscountPercent = (TextView) view.findViewById(R.id.product_price_discount_percent);
        productPriceDiscount = (TextView) view.findViewById(R.id.product_price_discount);
        productPrice = (TextView) view.findViewById(R.id.product_price);
        productDiscription = (TextView) view.findViewById(R.id.product_info);
		productImagesUrls = new ArrayList<String>();
		productMainImage = (ResizableImageViewHeight) view.findViewById(R.id.product_main_image_view);
		
		productPropRecycler = (RecyclerView) view.findViewById(R.id.product_prop_info_recycler_view);
		
		
		getProduct();
		prepareProductImagesLayout(view);
		return view;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		super.onViewCreated(view, savedInstanceState);
		
		
	}

	
	@Override
	public void APIResponse(JSONObject json)
	{
		super.APIResponse(json);
		try
		{
		String method = json.getString("method");
			if(method.equals("ItemViewerFragment_getProduct")){
				progressView.setVisibility(View.GONE);

				if(item == null)
					item = new Item_book(json.getJSONObject("item"));

				productName.setText(json.getJSONObject("data").getString("name"));
				toolbar.setTitle(productName.getText());
				productAuthor.setText(item.getAuthor());	
				productDiscription.setText(item.getDesc());

				ProductPropRecyclerAdapter productPropAdapter = new ProductPropRecyclerAdapter(json.getJSONObject("data").getJSONArray("prop"));
				productPropRecycler.setHasFixedSize(true);

				LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
				mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
				productPropRecycler.setLayoutManager(mLayoutManager);

				// Disabled nested scrolling since Parent scrollview will scroll the content.
				productPropRecycler.setNestedScrollingEnabled(false);

				productPropRecycler.setAdapter(productPropAdapter);

				JSONArray photos = json.getJSONObject("data").getJSONArray("photos");
				for(int i = 0; i < photos.length(); i++){
					productImagesUrls.add(photos.getString(i));
					productImagesAdapter.addLast(photos.getString(i));
				}
				if(productImagesUrls.size() < 2){
					getActivity().findViewById(R.id.itemvewerfragmentTextView_countphoto).setVisibility(View.GONE);

				}
				Picasso.get()
					.load(productImagesUrls.get(0))
					.fit().centerInside()
					.placeholder(R.drawable.placeholder_loading)
					.error(R.drawable.placeholder_error)
					.into(productMainImage);





				contentScrollLayout.setVisibility(View.VISIBLE);
			}
	
			if(method.equals("ItemViewerFragment_getOtherData")){
				item = new Item_book(json.getJSONObject("item"));
				
			JSONObject oData = json.getJSONObject("data").getJSONObject(item.getBookId());
			 
			productAddToCart.setOnClick(item, mFragmentNavigation);
			
			 Double price = oData.getDouble("price");
			 Double oPrice = oData.getDouble("old_price");
			 
			String btnStatus = oData.getString("button_status");
			
			productAddToCart.setStatus(btnStatus);
			 
			productPrice.setText(oData.getString("price")+" ₽");
			 if((oPrice - price ) > .1){

				 productPriceDiscount.setText(oData.getString("old_price"));
				 productPriceDiscount.setPaintFlags(productPriceDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);				 
			 	productPriceDiscountPercent.setText("-" +  (int)(100 - (price*100)/oPrice) + "℅");
			 }else{
			 	productPriceDiscountPercent.setVisibility(View.GONE);
			 	productPriceDiscount.setVisibility(View.GONE);
			 } 
		}
			
		
		
		}
		catch (JSONException e)
		{}
	}
	
	
	
	private void getProduct(){
		
		/*
		JSONObject god = new JSONObject();
		try
		{
			god.put("id", item.getBookId());
			god.put("bookId", item.getBID());
			god.put("section_id", item.geIblSecId());
		}
		catch (JSONException e)
		{}
		//mFragmentNavigation.get(APIHelper.getData("item.getOtherData", god.toString()));
		*/
		mFragmentNavigation.get(APIHelper.getData("item.ItemViewerFragment_getProduct", id));
	}
	
	
	private void prepareProductImagesLayout(View view) {
        productImagesRecycler = (RecyclerView) view.findViewById(R.id.product_images_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productImagesRecycler.setLayoutManager(linearLayoutManager);
        productImagesAdapter = new ProductImagesRecyclerAdapter(getActivity(), new ProductImagesRecyclerInterface() {
				@Override
				public void onImageSelected(View v, int position) {
					ProductImagesDialogFragment imagesDialog = ProductImagesDialogFragment.newInstance(productImagesUrls, position);
					if (imagesDialog != null)
						imagesDialog.show(getFragmentManager(), ProductImagesDialogFragment.class.getSimpleName());
					else {
						//Timber.e("%s Called with empty image list", ProductImagesDialogFragment.class.getSimpleName());
					}
				}
			});
        productImagesRecycler.setAdapter(productImagesAdapter);

        ViewGroup.LayoutParams params = productImagesRecycler.getLayoutParams();
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        // For small screen even smaller images.
        if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            params.height = (int) (dm.heightPixels * 0.2);
        } else {
            params.height = (int) (dm.heightPixels * 0.28);
        }

    }
	
	
}
