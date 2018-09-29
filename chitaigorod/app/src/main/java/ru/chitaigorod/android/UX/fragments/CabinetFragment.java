package ru.chitaigorod.android.UX.fragments;
import android.support.v4.app.*;
import org.json.*;

public class CabinetFragment extends BaseFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
	}

	public static CabinetFragment newInstance(){
		CabinetFragment frag = new CabinetFragment();
		return frag;
	}
}
