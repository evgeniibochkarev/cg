package ru.chitaigorod.android.view;
import ru.chitaigorod.android.utils.*;
import android.view.*;
import android.os.*;
import ru.chitaigorod.android.*;
import android.support.annotation.*;
import android.widget.Toast;

public class SearchFragment extends BaseFragment
{

	public static final String TAG = "SearchFragment";
	@Override
	public boolean onActivityBackPress()
	{
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
			Toast.makeText(getActivity(), "search is load", Toast.LENGTH_SHORT).show();
			//wrap.get("javascript:console.log('MAGIC jgfh');");
			// TODO: Implement this method
		}	
	}
}
