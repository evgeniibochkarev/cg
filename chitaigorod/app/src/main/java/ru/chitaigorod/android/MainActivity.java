package ru.chitaigorod.android;


import android.os.*;
import android.support.v7.app.AppCompatActivity;
import ru.chitaigorod.android.view.*;
import com.arlib.floatingsearchview.*;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import java.util.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import ru.chitaigorod.android.utils.*;

public class MainActivity extends AppCompatActivity implements BaseFragment.BaseFragmentCallbacks
{


	@Override
	public void onAttachSearchViewToDrawer(FloatingSearchView searchView)
	{
		searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
	}
	
	public final String Tag = "MainPage";
	private DrawerLayout mDrawerLayout;
	private AppCompatActivity activity;
   
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		activity = this;
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		Utils.switchFragment(R.id.fragment_container,  new ItemListCatalogFragment("https://www.chitai-gorod.ru/catalog/books/9646/"), this, ItemListCatalogFragment.TAG);
		Utils.switchFragment(R.id.fragment_container,  new ItemListCatalogFragment("https://www.chitai-gorod.ru/catalog/books/9646/"), this, ItemListCatalogFragment.TAG);
		
	}
	

    @Override
    public void onBackPressed() {

		super.onBackPressed();
		/*
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if(f instanceof BaseFragment) {
			if (((BaseFragment) f).onActivityBackPress()) {
				//currentFragment.onDetach();
				super.onBackPressed();
			}
		}*/
    
	        
    }

	
	
	
	
}
