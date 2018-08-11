package ru.chitaigorod.android.utils;
import android.net.*;
import java.io.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import ru.chitaigorod.android.view.*;

public class Utils
{

	private static String CURRENT_TAG = null;
	
	public static String router(String url){
		Uri uri = Uri.parse(url);
		if(uri.getPathSegments().size() == 0){
			return "MainPage";
		}
		if(uri.getPathSegments().size() == 3){
			return "ItemListCatalog";
		}
			return "";
	}
	public static String getJSByTag(AppCompatActivity act,String inFile) {
        String tContents = "javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);";

		try {
			InputStream stream = act.getAssets().open(inFile);

			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			tContents = new String(buffer);
		} catch (IOException e) {
			// Handle exceptions here
		}

		return tContents;

	}
	
	
	public static void switchFragment(int id, Fragment fragment,
                                                   FragmentActivity activity, String TAG) {
		CURRENT_TAG = TAG;
		
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();

        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
	}
	
	
	public static Fragment getFragmentByTag(FragmentActivity activity, String tag){
		final FragmentManager fm = activity.getSupportFragmentManager();
		final Fragment fragment = fm.findFragmentByTag(tag);
		
		return fragment;
	}
	
	
	public static void switchContent(int id, String TAG,
                                     FragmentActivity baseActivity) {

        Fragment fragmentToReplace = null;

        FragmentManager fragmentManager = baseActivity
			.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // If our current fragment is null, or the new fragment is different, we
        // need to change our current fragment
        if (CURRENT_TAG == null || !TAG.equals(CURRENT_TAG)) {

                        // Try to find the fragment we are switching to
            Fragment fragment = fragmentManager.findFragmentByTag(TAG);

            // If the new fragment can't be found in the manager, create a new
            // one
            if (fragment == null) {

                if (TAG.equals(MainPageFragment.TAG)) {
                    fragmentToReplace = new MainPageFragment();
                } else if (TAG.equals(ItemListCatalogFragment.TAG)) {
                    fragmentToReplace = new ItemListCatalogFragment("");
                }/* else if (TAG.equals(SETTINGS_FRAGMENT_TAG)) {
                    fragmentToReplace = new SettingsFragment();
                } else if (TAG.equals(CONTACT_US_FRAGMENT)) {
                    fragmentToReplace = new ContactUsFragment();
                } else if (TAG.equals(PRODUCT_OVERVIEW_FRAGMENT_TAG)) {
                    fragmentToReplace = new ProductOverviewFragment();
                } else if (TAG.equals(SHOPPING_LIST_TAG)) {
                    fragmentToReplace = new MyCartFragment();
                }*/

            } else

            {
                if (TAG.equals(MainPageFragment.TAG)) {
                    fragmentToReplace = (MainPageFragment) fragment;
                } else if (TAG.equals(ItemListCatalogFragment.TAG)) {
                    fragmentToReplace = (ItemListCatalogFragment) fragment;
                }/* else if (TAG.equals(PRODUCT_OVERVIEW_FRAGMENT_TAG)) {
                    fragmentToReplace = (ProductOverviewFragment) fragment;
                } else if (TAG.equals(SETTINGS_FRAGMENT_TAG)) {
                    fragmentToReplace = (SettingsFragment) fragment;
                } else if (TAG.equals(CONTACT_US_FRAGMENT)) {
                    fragmentToReplace = (ContactUsFragment) fragment;
                }*/
            }

            CURRENT_TAG = TAG;

            // Replace our current fragment with the one we are changing to
            transaction.replace(id, fragmentToReplace, TAG);
            transaction.commit();

        } else

        {
            // Do nothing since we are already on the fragment being changed to
        }
    }
	
	
}
