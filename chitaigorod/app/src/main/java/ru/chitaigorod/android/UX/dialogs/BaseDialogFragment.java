package ru.chitaigorod.android.UX.dialogs;


import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import org.json.*;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public abstract class BaseDialogFragment extends DialogFragment
 {

	
	FragmentNavigation mFragmentNavigation;

	

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof FragmentNavigation) {
			mFragmentNavigation = (FragmentNavigation) context;
		}
	}

	public interface FragmentNavigation {
		void pushFragment(Fragment fragment);
		void showDialog(DialogFragment frg);
		void get(String str);
	}

	public abstract void APIResponse(JSONObject json);
	
	}
