package ru.chitaigorod.android.UX.fragments;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.dialogs.*;

public class InfoFragment extends BaseFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
	}

	public static InfoFragment newInstance(){
		InfoFragment frag = new InfoFragment();
		return frag;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		
		RelativeLayout rlPhone = (RelativeLayout) view.findViewById(R.id.fragment_info_phone);
		rlPhone.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+78004448444")));
				}						
		});
		
		RelativeLayout rlAbout = (RelativeLayout) view.findViewById(R.id.fragment_info_about);
		rlAbout.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mFragmentNavigation.showDialog(AboutDialogFragment.newInstance());
				}						
			});
		
		
		return view;
	}
}
