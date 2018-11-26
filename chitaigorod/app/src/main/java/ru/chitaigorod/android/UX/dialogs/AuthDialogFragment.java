package ru.chitaigorod.android.UX.dialogs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.utils.*;

import android.support.v4.app.DialogFragment;
import ru.chitaigorod.android.interfaces.*;

public class AuthDialogFragment extends BaseDialogFragment 
{
	ProgressDialog pd;
	
	LinearLayout formAuth;
	LinearLayout formReg;
	
	TextInputLayout loginFormAuth;
	TextInputLayout passwordFormAuth;
	
	TextView btnGotoRegFormAuth;
	Button btnLoginFormAuth;
	
	Button btnRegFormReg;

	private TextInputLayout nameFormReg;

	private TextInputLayout passwordFormReg;

	private TextInputLayout loginFormReg;
	
	private AuthDialogInterface mInterface;
	
	@Override
	public void APIResponse(JSONObject json)
	{
		//Strng lol = "";
		try
		{
			String method = json.getString("method");
			JSONObject data = json.getJSONObject("data");
			if(method.equals("AuthDialog.auth")){
				if(pd != null) pd.dismiss();
				
				if(data.getBoolean("result") ){
					if(mInterface != null)
						mInterface.success();
					dismiss();
				}else{
					AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
					
					adb.setMessage("Неверный логин или пароль");
					adb.setPositiveButton("OK", null);
					//dialog = adb.create();
					adb.show();
					
				}
			}else if(method.equals("AuthDialog.reg")){
				if(pd != null) pd.dismiss();
				if(!data.isNull( "USERID") ){
					if(mInterface != null)
						mInterface.success();
					dismiss();
				}
				if(!data.isNull("EMAIL") ){
					AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

					adb.setMessage(data.getString("EMAIL"));
					adb.setPositiveButton("OK", null);
					//dialog = adb.create();
					adb.show();
					loginFormReg.setErrorEnabled(true);
					loginFormReg.setError("проверьте правильность");
				}	
				if(!data.isNull("LOGIN") ){
					loginFormReg.setErrorEnabled(true);
					loginFormReg.setError("проверьте правильность");
				}
				if(!data.isNull("PASSWORD") ){
					passwordFormReg.setErrorEnabled(true);	
					passwordFormReg.setError("проверьте правильность");
				}
				if(!data.isNull("NAME") ){
					nameFormReg.setErrorEnabled(true);	
					nameFormReg.setError("проверьте правильность");
				}
			}
		}
		catch (JSONException e)
		{}

		
		
		// TODO: Implement this method
	}



	public static AuthDialogFragment newInstance(AuthDialogInterface _interface){
		AuthDialogFragment frag = new AuthDialogFragment();
		frag.mInterface = _interface;
		
		return frag;
	}
    

	public static AuthDialogFragment newInstance( ) {
        AuthDialogFragment dialogFragment = new AuthDialogFragment();
        return dialogFragment;
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
        View view = inflater.inflate(R.layout.dialog_auth, container, false);

		formAuth = (LinearLayout) view.findViewById(R.id.login_base_form);
		formReg = (LinearLayout) view.findViewById(R.id.login_registration_form);
		loginFormAuth = (TextInputLayout) view.findViewById(R.id.login_email_email_wrapper);
		passwordFormAuth = (TextInputLayout) view.findViewById(R.id.login_email_password_wrapper);
		btnLoginFormAuth = (Button) view.findViewById(R.id.login_email_confirm);
		btnGotoRegFormAuth = (TextView) view.findViewById(R.id.login_form_registration_btn);
		
		nameFormReg = (TextInputLayout) view.findViewById(R.id.login_registration_name_wrapper);
		loginFormReg = (TextInputLayout) view.findViewById(R.id.login_registration_email_wrapper);
		passwordFormReg = (TextInputLayout) view.findViewById(R.id.login_registration_password_wrapper);
		
		
		btnLoginFormAuth.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					pd = new ProgressDialog(getActivity());
					
					pd.setMessage("Авторизация");
					
					
					pd.show();
					mFragmentNavigation.get( APIHelper.getData("AuthDialog.auth", "{'USER_LOGIN': '"+loginFormAuth.getEditText().getText()+"', 'USER_PASSWORD':'"+passwordFormAuth.getEditText().getText()+"'}"));
				}			
		});
		btnGotoRegFormAuth.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					showRegForm();
				}
				
			
		});
		
		
		ImageButton btnCloseFormReg = (ImageButton) view.findViewById(R.id.login_registration_close_button);
		btnCloseFormReg.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					showLoginForm();
				}	
		});
		
		btnRegFormReg = (Button) view.findViewById(R.id.login_registration_confirm);
		btnRegFormReg.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					pd = new ProgressDialog(getActivity());
					pd.setMessage("Регистрация");
					pd.show();
					
					mFragmentNavigation.get( APIHelper.getData("AuthDialog.reg", "{'REGISTER_NAME':'"+nameFormReg.getEditText().getText()+"', 'REGISTER_LOGIN': '"+loginFormReg.getEditText().getText()+"', 'REGISTER_PASSWORD':'"+passwordFormReg.getEditText().getText()+"'}"));
					
				}
				
			
		});
		// prepareFilterRecycler(view);
		/*
		 Button btnApply = view.findViewById(R.id.filter_btn_apply);
		 btnApply.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		 //String filterUrl = buildFilterUrl();
		 //filterDialogInterface.onFilterSelected(filterUrl);
		 dismiss();
		 }
		 });

		 Button btnCancel = view.findViewById(R.id.filter_btn_cancel);
		 btnCancel.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		 // Clear all selected values
		 if (filterData != null) {
		 for (FilterType filterType : filterData.getFilters()) {
		 switch (filterType.getType()) {
		 case DeserializerFilters.FILTER_TYPE_RANGE:
		 ((FilterTypeRange) filterType).setSelectedMin(-1);
		 ((FilterTypeRange) filterType).setSelectedMax(-1);
		 break;
		 case DeserializerFilters.FILTER_TYPE_COLOR:
		 ((FilterTypeColor) filterType).setSelectedValue(null);
		 break;
		 case DeserializerFilters.FILTER_TYPE_SELECT:
		 ((FilterTypeSelect) filterType).setSelectedValue(null);
		 break;
		 }
		 }
		 }
		 filterDialogInterface.onFilterCancelled();
		 dismiss();
		 }
		 });*/
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		super.onViewCreated(view, savedInstanceState);
		
	}

	private void hideSoftKeyboard() {
        if (getActivity() != null && getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
	
	private void showLoginForm(){
		formReg.setVisibility(View.GONE);
		formAuth.setVisibility(View.VISIBLE);
		
		hideSoftKeyboard();
	}
	private void showRegForm(){
		formAuth.setVisibility(View.GONE);
		formReg.setVisibility(View.VISIBLE);
		
		hideSoftKeyboard();
	}
}
