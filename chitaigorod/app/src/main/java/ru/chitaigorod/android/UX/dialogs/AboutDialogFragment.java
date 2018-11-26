package ru.chitaigorod.android.UX.dialogs;
import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;

import android.support.v4.app.DialogFragment;

public class AboutDialogFragment extends BaseDialogFragment
{

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
	}

	public static AboutDialogFragment newInstance(){
		AboutDialogFragment frag = new AboutDialogFragment();
		
		return frag;
	}
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }

	@Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_about, container, false);

		TextView tv =  (TextView) view.findViewById(R.id.dialog_about_textview);
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			tv.setText(Html.fromHtml(getResources().getString(R.string.text_about),Html.FROM_HTML_MODE_LEGACY));
		} else {
			tv.setText(Html.fromHtml(getResources().getString(R.string.text_about)));
		}
		
		
		return view;
	}
	/*
	 	
	*/
}
