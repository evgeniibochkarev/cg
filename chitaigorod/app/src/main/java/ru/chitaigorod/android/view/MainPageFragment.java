package ru.chitaigorod.android.view;
import android.view.*;
import android.os.*;
import android.support.annotation.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.utils.*;
import android.util.*;
import android.widget.*;
import android.view.View.*;


public class MainPageFragment extends BaseFragment
{

	final String TAG = "MainPageFragment";
	@Override
	public boolean onActivityBackPress()
	{
		// TODO: Implement this method
		return false;
	}
	private WrapperHTML wrap;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
		wrap = new WrapperHTML(getActivity(), TAG);
		
		wrap.setListener(new Parser());
		
		wrap.get("https://www.chitai-gorod.ru/");
		
		
    }
	
	private class Parser implements WrapperHTML.WrapperListener
	{
		@Override
		public void onPageSuccess(String html)
		{
			Toast.makeText(getActivity(), "is load", Toast.LENGTH_SHORT).show();
			//wrap.get("javascript:console.log('MAGIC jgfh');");
			// TODO: Implement this method
		}	
	}
}
