package ru.chitaigorod.android.UX.adapters;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.entities.*;
import java.util.*;
import java.net.*;
import android.content.*;
import ru.chitaigorod.android.interfaces.*;
import android.view.View.*;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.CatViewHolder>
{
	private EntryCategory mCat ;
	private HashMap filter;
	private LayoutInflater layoutInflater;
	
	private final Context context;
    private final CategoryPickerDialogInterface categoryRecyclerInterface;
    

    /**
     * Creates an adapter that handles a list of product items.
     *
     * @param context                   activity context.
     * @param categoryRecyclerInterface listener indicating events that occurred.
     */
    public CategoryDialogAdapter(Context context, CategoryPickerDialogInterface categoryRecyclerInterface) {
        this.context = context;
		mCat = EntryCategory.getCatalog();
        this.categoryRecyclerInterface = categoryRecyclerInterface;
    }

    public void setCategory(EntryCategory ec){
		mCat = ec;
		notifyDataSetChanged();
	}
	public void setFilter(EntryCategory _ec, HashMap fi){	
	
		EntryCategory tt = three(_ec, fi) ;
		if(tt.getSubCategories().size() == 0) tt = EntryCategory.getCatalog();
		setCategory(tt);
	}
	private EntryCategory three(EntryCategory et,HashMap fi){
		EntryCategory temp = new EntryCategory();
		
		for(EntryCategory i : et.getSubCategories()){
			EntryCategory th = three(i, fi);
			EntryCategory n = new EntryCategory(i.getId(), i.getName());
		
			if(th.getSubCategories().size() > 0){		
				n.addSubCategory(th.getSubCategories());
			}
			
		
			
			if(fi.containsKey(n.getId()) || n.getSubCategories().size() > 0)
				temp.addSubCategory(n);
			
		}
		
		
		
		/*for(EntryCategory i : et.getSubCategories()){
			if( fi.containsKey( i.getId()) || three(i,fi).getSubCategories().size() > 0){
				EntryCategory ne = new EntryCategory(i.getId(), i.getName());
				
				EntryCategory th = three(i, fi);
				
				if(th.getSubCategories().size() > 0)
					ne.addSubCategory(th.getSubCategories());
				temp.addSubCategory(ne);
			
			}
						
		}
		/*
		for(EntryCategory i : et.getSubCategories()){
			if(three(i, fi).getSubCategories().size() > 0 || fi.containsKey(i.getId())){
				for(EntryCategory j : i.getSubCategories()){
					temp.addSubCategory(three(j, fi));
				}
				
			}
		}*/
		return temp;
	}
	
	
	@Override
	public CategoryDialogAdapter.CatViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(p1.getContext());

        View view = layoutInflater.inflate(R.layout.card_category_dialog, p1, false);
        return new CatViewHolder(view, categoryRecyclerInterface);
		
	}

	@Override
	public void onBindViewHolder(CategoryDialogAdapter.CatViewHolder holder, int id)
	{
		holder.mainCat.setVisibility(View.GONE);
		holder.subCat.setVisibility(View.GONE);
		int count = getItemCount();
		int j = 0;
		for(EntryCategory item : mCat.getSubCategories()){
			
			if(j == id ){
				holder.bindContent(this, item, categoryRecyclerInterface);
				if(item.getSubCategories().size() != 0){				
					holder.mainCat.setVisibility(View.VISIBLE);
					holder.mainCat.setText(item.getName());	
				}else{
					holder.subCat.setVisibility(View.VISIBLE);
					holder.subCat.setText(item.getName());		
				}
				j++;
			}else{
				j++;
				for(EntryCategory subItem : item.getSubCategories()){
					if(j == id ){	
						holder.bindContent(this,  subItem, categoryRecyclerInterface);
						
						holder.subCat.setVisibility(View.VISIBLE);
						holder.subCat.setText(subItem.getName());		
					}
					j++;
				}
			}
			
		}
		
		
		//holder.productOldPrice.setVisibility(View.GONE);
		
	}

	@Override
	public int getItemCount()
	{
		int count = mCat.getSubCategories().size();
		for(EntryCategory i : mCat.getSubCategories()){
			count = count + i.getSubCategories().size();
		}
		// TODO: Implement this method
		return count;
	}
	

	
	public static class CatViewHolder extends RecyclerView.ViewHolder {      
        CardView cv;
        TextView mainCat;
        RadioButton subCat;
       
	
		
        CatViewHolder(View itemView, final CategoryPickerDialogInterface categoryRecyclerInterface ) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.dialog_category_picker_rv);
            mainCat = (TextView)itemView.findViewById(R.id.card_category_dialog_main_cat);
            subCat = (RadioButton)itemView.findViewById(R.id.card_category_dialog_sub_cat);
         	
        }
		public void bindContent(final CategoryDialogAdapter ad, final EntryCategory ec, final CategoryPickerDialogInterface categoryRecyclerInterface) {
					
			OnClickListener listener = new OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						if(ec.getSubCategories().size() == 0){

							categoryRecyclerInterface.onSelect(ec);
						}else{
							ad.setCategory(ec);
						}
					}
				};
			//mainCat.setOnClickListener(listener);
			subCat.setOnClickListener(listener);
        }
		
    }
}
	
	
	
	
