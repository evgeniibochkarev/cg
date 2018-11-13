package ru.chitaigorod.android.UX.custom_view;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.interfaces.*;

public class HorizontalCounter extends RelativeLayout
{
	LayoutInflater mInflater;

	private RelativeLayout mMainView;
	private ImageButton mButtonMinus;
	private ImageButton mButtonPlus;
	private ProgressBar mProgress;
	private TextView mValue;
	private HorizontalCounterInterface mInteface;
	private int value = 0;
	private int maxValue = 0;
	private int minValue = 0;
	
	public HorizontalCounter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		init(); 

	}
	public HorizontalCounter(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);
		init(); 
	}
	public HorizontalCounter(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		init(); 
	}
	public void setChangeListener(HorizontalCounterInterface intef){
		mInteface = intef;
	}
	public void init()
	{
		View v = mInflater.inflate(R.layout.custom_view_horizontal_counter, this, true);
		mMainView = (RelativeLayout) v.findViewById(R.id.custom_view_horizontal_counter_rl);
		
		mValue = (TextView) v.findViewById(R.id.custom_view_horizontal_counter_value);
		
		mButtonMinus = (ImageButton) v.findViewById(R.id.custom_view_horizontal_counter_minus);
		mButtonMinus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					decr();
					if(mInteface != null){
						mInteface.onChange(getVal());
					}
				}
				
			
		});
		
		mButtonPlus = (ImageButton) v.findViewById(R.id.custom_view_horizontal_counter_plus);
		mButtonPlus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					incr();
					if(mInteface != null){
						mInteface.onChange(getVal());
					}
				}
				
			
		});
		/*mProgress = (ProgressBar) v.findViewById(R.id.product_add_to_cart_progress);
		mProgress.getIndeterminateDrawable().setColorFilter(getResources()
															.getColor(R.color.white),PorterDuff.Mode.SRC_IN);
		
					*/										
									
				//mImg = (ImageView) v.findViewById(R.id.product_add_to_cart_image);
	}
	public int getVal(){
		return value;
	}
	public void setValue(int val){
		value = val;
		mValue.setText(""+ val);
	}
	public void setMaxValue(int val){
		maxValue = val;
	}
	public void setMinValue(int val){
		minValue = val;
	}
	
	private void incr(){
		if(maxValue > value)
			setValue(value + 1);
	}
	private void decr(){
		if(minValue < value)
			setValue(value - 1);
	}
}
