package ru.chitaigorod.android.UX.fragments;
import android.os.*;
import android.view.*;
import ru.chitaigorod.android.*;

public class DiscountFragment extends BaseFragment
{
	public static String TAG = "DiscountFragment";
	
	public static DiscountFragment newInstance(){
		DiscountFragment frag = new DiscountFragment();
		return frag;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.discount_fragment, container, false);
		return view;
	}
	
}
