package ru.chitaigorod.android.view.custom_view;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.ViewGroup;
import ru.chitaigorod.android.R;
import android.content.Context;
import android.util.*;
import android.widget.RelativeLayout;
import com.arlib.floatingsearchview.*;
import android.widget.TextView;
import android.content.res.TypedArray;
import java.util.function.*;
import android.support.v7.widget.*;

public class ShopAppBar extends RelativeLayout
{
	private View mMainLayout;
	private FloatingSearchView mSearch;
	private View mCartButton;
	private RecyclerView mSearchResult;
	
	public ShopAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
		mMainLayout = inflate(getContext(), R.layout.shop_app_bar, this);
		
		
		
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShopAppBar);
		Boolean isShowCart = a.hasValue(R.styleable.ShopAppBar_showCart);
		
		if(isShowCart){
			mCartButton = layoutInflater.inflate(R.layout.custom_cart_button,  this, true);
			
			mSearch = new FloatingSearchView(context, attrs);
		
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams ( RelativeLayout.LayoutParams.MATCH_PARENT,
																			  RelativeLayout.LayoutParams.MATCH_PARENT );
			lp.addRule(RelativeLayout.LEFT_OF, R.id.my_custom_cart);
			mSearch.setLayoutParams(lp);
			//layoutInflater.inflate( mSearch.get, this, true);
			this.addView(mSearch);
			
			mSearchResult = (RecyclerView) mMainLayout.findViewById(R.id.search_results_list);
			setupSearchBar();
		}
	}	
	public void showSearchResults(){
		mSearchResult.setVisibility(View.VISIBLE);	
	}
	public Boolean onBackPressed(){
		if(mSearchResult.getVisibility() == View.VISIBLE){
			mSearchResult.setVisibility(View.GONE);
			return false;
		}
		return true;
	}
	private void setupSearchBar(){
		mSearch.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
				@Override
				public void onSearchTextChanged(String p1, String p2)
				{
					
				}
		});
		
		
		
	}
	public void setCartCount(String count){
		TextView tv = (TextView) mMainLayout.findViewById(R.id.badge_cart_textview);
		tv.setText(count);
	}
	
	public FloatingSearchView getSearchView(){
		return mSearch;
	}
	
	
	
}
