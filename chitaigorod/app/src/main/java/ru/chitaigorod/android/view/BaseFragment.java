package ru.chitaigorod.android.view;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import ru.chitaigorod.android.utils.*;
import java.sql.*;

/**
 * Created by ari on 8/16/16.
 */
public abstract class BaseFragment extends Fragment {


    private BaseFragmentCallbacks mCallbacks;

    public interface BaseFragmentCallbacks{
        void onAttachSearchViewToDrawer(FloatingSearchView searchView);
		//void showFragment(Fragment fragment, String tag);
		void onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
		
        if (context instanceof BaseFragmentCallbacks) {
            mCallbacks = (BaseFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
									   + " must implement BaseExampleFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

	protected void backPressed(){
		if(mCallbacks != null){
            mCallbacks.onBackPressed();
        }
	}
    protected void attachSearchViewActivityDrawer(FloatingSearchView searchView){
        if(mCallbacks != null){
            mCallbacks.onAttachSearchViewToDrawer(searchView);
        }
    }
		

    public abstract boolean onActivityBackPress();
}
