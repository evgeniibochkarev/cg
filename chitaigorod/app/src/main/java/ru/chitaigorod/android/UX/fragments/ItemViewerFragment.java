package ru.chitaigorod.android.UX.fragments;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.utils.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.UX.dialogs.*;
import java.util.*;
import android.text.*;
import android.graphics.*;
import ru.chitaigorod.android.UX.custom_view.*;

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
    private TextView productName;
	private TextView productAuthor;
    private TextView productPriceDiscount;
    private TextView productPrice;
    private TextView productDiscription;
    private TextView productPriceDiscountPercent;

	
	private RecyclerView productImagesRecycler;
	private ArrayList<String> productImagesUrls;
	private ProductImagesRecyclerAdapter productImagesAdapter;

    /**
     * Refers to the displayed product.
     */
   
    /**
     * Refers to a user-selected product variant
     */
	
	
	public static ItemViewerFragment newInstance(Item_book _item){
		ItemViewerFragment itemViewer = new ItemViewerFragment();
		
		itemViewer.item = _item;
		//bundle.putSerializable("elastic_filter", (new EntryElasticSearchFilter()).getHashMap());
		
		return itemViewer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.item_vewer_fragment, container, false);
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
		
		getProduct();
		prepareProductImagesLayout(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		super.onViewCreated(view, savedInstanceState);
		
		productAddToCart.setOnClick(item, mFragmentNavigation);
	}

	
	@Override
	public void APIResponse(JSONObject json)
	{
		super.APIResponse(json);
		try
		{
		String method = json.getString("method");
	
	
		if(method.equals("getOtherData")){
			
			JSONObject oData = json.getJSONObject("data").getJSONObject(item.getBookId());
			
			
			 Double price = oData.getDouble("price");
			 Double oPrice = oData.getDouble("old_price");
			 
			String btnStatus = oData.getString("button_status");
			
			productAddToCart.setStatus(btnStatus);
			 
			 productPrice.setText(oData.getString("price"));
			 if((oPrice - price ) > .1){
				 //String htmlTaggedString  = "<strike>"+oData.getString("old_price")+"</strike>";
				 //Spanned textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
				 productPriceDiscount.setText(oData.getString("old_price"));
				 productPriceDiscount.setPaintFlags(productPriceDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);				 
			 	productPriceDiscountPercent.setText("-" +  (int)(100 - (price*100)/oPrice) + "℅");
			 }else{
			 	productPriceDiscountPercent.setVisibility(View.GONE);
			 	productPriceDiscount.setVisibility(View.GONE);
			 }
		}
		if(method.equals("ItemViewerFragment_getProduct")){
			progressView.setVisibility(View.GONE);
			
			productName.setText(json.getJSONObject("data").getString("name"));
			productAuthor.setText(json.getJSONObject("data").getString("author"));	
			productDiscription.setText(json.getJSONObject("data").getString("description"));
			
			JSONArray photos = json.getJSONObject("data").getJSONArray("photos");
			for(int i = 0; i < photos.length(); i++){
				productImagesUrls.add(photos.getString(i));
				productImagesAdapter.addLast(photos.getString(i));
			}
			
			
			
			
			contentScrollLayout.setVisibility(View.VISIBLE);
		}
		
		
		
		
		
		}
		catch (JSONException e)
		{}
	}
	
	
	
	private void getProduct(){
		
		
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

		mFragmentNavigation.get(APIHelper.getData("item.ItemViewerFragment_getProduct", god));
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
            params.height = (int) (dm.heightPixels * 0.4);
        } else {
            params.height = (int) (dm.heightPixels * 0.48);
        }

    }
	
	
}
