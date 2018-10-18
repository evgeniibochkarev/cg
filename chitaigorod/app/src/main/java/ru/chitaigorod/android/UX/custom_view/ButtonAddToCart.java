package ru.chitaigorod.android.UX.custom_view;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.fragments.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.utils.*;

public class ButtonAddToCart extends RelativeLayout
{
	LayoutInflater mInflater;

	private RelativeLayout mMainView;
	private TextView mButtonText;
	private ProgressBar mProgress;
	private String mStatus;
	private ImageView mImg;
	private Item_book mItem;
	public ButtonAddToCart(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		init(); 

	}
	public ButtonAddToCart(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);
		init(); 
	}
	public ButtonAddToCart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		init(); 
	}

	public void init()
	{
		View v = mInflater.inflate(R.layout.custom_view_button_add_to_cart, this, true);
		mMainView = (RelativeLayout) v.findViewById(R.id.product_add_to_cart_layout);
		mButtonText = (TextView) v.findViewById(R.id.product_add_to_cart_text);
		mProgress = (ProgressBar) v.findViewById(R.id.product_add_to_cart_progress);
		mProgress.getIndeterminateDrawable().setColorFilter(getResources()
														.getColor(R.color.white),PorterDuff.Mode.SRC_IN);
		mImg = (ImageView) v.findViewById(R.id.product_add_to_cart_image);
	}
	
	public void setOnClick(Item_book item, final BaseFragment.FragmentNavigation frn){
		mItem = item;
		final JSONObject param = new JSONObject();
		try
		{
			param.put("id", item.getBookId());
			param.put("bookId", item.getBID());
			param.put("section_id", item.geIblSecId());
		}
		catch (JSONException e)
		{}
				
		
		mMainView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(mStatus.equals("BUY")){
						showProgress();
						frn.get( APIHelper.getData("item.addToBasket", param));
						
					}
					
				}
				
			
		});
			
	
	}
	
	private void showProgress(){
		mProgress.setVisibility(View.VISIBLE);
		mImg.setVisibility(View.GONE);
	}
	private void hideProgress(){
		mImg.setVisibility(View.VISIBLE);
		mProgress.setVisibility(View.GONE);
	}
	
	
	public void setStatus(String status){
		mStatus = status;
		hideProgress();
		if(status.equals("SOON")){
			mButtonText.setText("Ожидается");
			//mButtonText.setVisibility(View.VISIBLE);
		} else if(status.equals("PREORDER")){
			mButtonText.setText("Предзаказ");
			//holder.productStatus.setVisibility(View.VISIBLE);	 
			//.holder.productPrice.setVisibility(View.VISIBLE);
		}else if(status.equals("IN_BUSKET")){
			mButtonText.setText("В корзине");	
			mMainView.setBackgroundResource(R.drawable.button_green_action_ripple);
			//holder.productStatus.setVisibility(View.VISIBLE);
			//holder.productPrice.setVisibility(View.VISIBLE);
		}else if(status.equals("BUY")){			
			mButtonText.setText("В корзину");
			mMainView.setBackgroundResource(R.drawable.button_primary_action_ripple);
			// holder.productStatus.setVisibility(View.VISIBLE);
			//holder.productPrice.setVisibility(View.VISIBLE);
		}else if(status.equals("WHERE_BUY")){
			mButtonText.setText("Где купить?");
			//holder.productStatus.setVisibility(View.GONE);
			//holder.productPrice.setVisibility(View.VISIBLE);
		}else if(status.equals("NOT_AVAILABLE")){
			mButtonText.setText("Нет в наличии");
			mMainView.setBackgroundResource(R.drawable.button_secondary_action_ripple);
			//holder.productStatus.setVisibility(View.VISIBLE);
		}
		
	}


}
