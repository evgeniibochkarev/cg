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
		showFragment(new ItemListCatalogFragment("https://www.chitai-gorod.ru/catalog/books/9646/"), ItemListCatalogFragment.TAG);
		showFragment(new ItemListCatalogFragment("https://www.chitai-gorod.ru/catalog/books/9645/"), ItemListCatalogFragment.TAG);
	}
	

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
		//List fragments = getSupportFragmentManager().getFragments();
		
        //BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if(f instanceof BaseFragment) {
			if (((BaseFragment) f).onActivityBackPress()) {
				//currentFragment.onDetach();
				super.onBackPressed();
			}
		}
    
		//FragmentManager fm = getSupportFragmentManager();
		//fm.popBackStack(null, BaseFragment);
        
    }

	String mSelectedTag;
	@Override
	public void showFragment(Fragment frag, String tag) {
		String oldTag = mSelectedTag;
		mSelectedTag = tag;
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		final Fragment oldFragment = fm.findFragmentByTag(oldTag);
		final Fragment fragment = fm.findFragmentByTag(tag);
	
		if (oldFragment != null && !tag.equals(oldTag)) {
			ft.detach(oldFragment);
		}

		if (fragment == null) {
			ft.addToBackStack(tag)
				.replace(R.id.fragment_container, frag, tag);
		} else {
			if (fragment.isDetached()) {
				ft.attach(fragment);
			}
		}
		ft.commit();
	}
	/*
	private Fragment getContentFragment(String tag) {
		Fragment fragment = null;
		if (MainPageFragment.TAG.equals(tag)) {
			fragment = new MainPageFragment();
		} else if (ItemListCatalogFragment.TAG.equals(tag)) {
			fragment = new ItemListCatalogFragment();
		}
		return fragment;
	}*/
	/*
	@Override
	public void showFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
			.beginTransaction()
			.addToBackStack(tag)
			.add(R.id.fragment_container, fragment).commit();
    }*/
	
	
}
