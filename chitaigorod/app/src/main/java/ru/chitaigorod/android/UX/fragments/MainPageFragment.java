package ru.chitaigorod.android.UX.fragments;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.utils.*;
import android.widget.*;
import android.view.View.*;
import java.util.*;
import ru.chitaigorod.android.entities.*;

public class MainPageFragment extends BaseFragment {

    
    public static MainPageFragment newInstance(){
		MainPageFragment frag = new MainPageFragment();
		return frag;
	}
	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_page_fragment, container, false);
		
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		Button btn = (Button) view.findViewById(R.id.mainpagefragmentButtonGoToSearch);
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (mFragmentNavigation != null) {
						SearchFilter filter = new SearchFilter();
						
						mFragmentNavigation.pushFragment(SearchFragment.newInstance("воровка", filter.getFilter()));

					}
				}
				
			
		});
	}

	
	

    private void init() {
        //binder.button.setOnClickListener(v -> FragmentUtils.sendActionToActivity(ACTION_DASHBOARD, currentTab, true, fragmentInteractionCallback));
    }
}
