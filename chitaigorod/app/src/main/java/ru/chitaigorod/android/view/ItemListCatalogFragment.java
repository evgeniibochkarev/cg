package ru.chitaigorod.android.view;
import android.view.LayoutInflater;
import ru.chitaigorod.android.utils.*;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.support.annotation.*;
import android.widget.Toast;
import java.security.*;
import ru.chitaigorod.android.R;
import ru.chitaigorod.android.view.custom_view.*;
import com.arlib.floatingsearchview.*;

public class ItemListCatalogFragment extends BaseFragment
{
	public final static String TAG = "ItemListCatalogFragment";
	private String url;
	
	public ItemListCatalogFragment(String url){
		this.url = url;
	}
	
	@Override
	public boolean onActivityBackPress()
	{
		if(mHeader.onBackPressed()){
			return true;
		}
		return false;
	}
	private WrapperHTML wrap;
	private ShopAppBar mHeader;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.itemlist_catalog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

				
		wrap = new WrapperHTML(getActivity(), TAG);

		wrap.setListener(new Parser());

		wrap.get(url);

		setupSearchBar(view);
    }
	private void setupSearchBar(View view){
		mHeader = (ShopAppBar) view.findViewById(R.id.my_search_view);

		
		
		mHeader.getSearchView().setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
				@Override
				public void onHomeClicked() {
					backPressed();
					//Log.d(TAG, "onHomeClicked()");
				}
			});
		//attachSearchViewActivityDrawer(mHeader.getSearchView());
		
	}
	@Override
	public void onDetach()
	{
		super.onDetach();
		wrap = null;
	}

	private class Parser implements WrapperHTML.WrapperListener
	{
		@Override
		public void onPageSuccess(String html)
		{
			renderPage(html);		
			//wrap.get("javascript:console.log('MAGIC jgfh');");
			// TODO: Implement this method
		}	
	}

	@Override
	public void onResume()
	{
		super.onResume();
		renderPage(mHTML);
	}
	
	private String mHTML;
	private void renderPage(String html){
		mHTML = html;
		if(mHTML != null && this.isVisible()){
			Toast.makeText(getActivity(), "is load", Toast.LENGTH_SHORT).show();
			mHeader.setCartCount("6");
		}
	}
}
