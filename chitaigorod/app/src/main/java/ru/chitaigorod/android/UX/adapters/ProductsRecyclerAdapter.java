package ru.chitaigorod.android.UX.adapters;

import android.content.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.custom_view.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;
import com.squareup.picasso.*;
import org.json.*;
import android.graphics.*;
import android.net.*;


/**
 * Adapter handling list of product items.
 */
public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final CategoryRecyclerInterface categoryRecyclerInterface;
    private List<Item_book> products = new ArrayList<>();
    private LayoutInflater layoutInflater;

    
    /**
     * Creates an adapter that handles a list of product items.
     *
     * @param context                   activity context.
     * @param categoryRecyclerInterface listener indicating events that occurred.
     */
    public ProductsRecyclerAdapter(Context context, CategoryRecyclerInterface categoryRecyclerInterface) {
        this.context = context;
        this.categoryRecyclerInterface = categoryRecyclerInterface;

      
    }

    private Item_book getItem(int position) {
        return products.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addProducts(List<Item_book> productList) {
        int st = products.size() ;
		products.addAll(productList);
		
		notifyItemRangeInserted(products.size(), products.size() - st);
		
		
		/*
		if(products.size() > st){
			notifyItemRangeInserted(st , productList.size()-st );
		}else{
			//notifyDataSetChanged();
			notifyItemRangeRemoved(0, st);
			notifyItemRangeInserted(0, products.size());
			
		}
        //notifyDataSetChanged();*/
    }

	public void setOtherProp(JSONObject jsonObject, EntryRecyclerSearchFilter rFilter, EntryElasticSearchFilter eFilter){
		try
		{
		
			Iterator<String> keysStr = jsonObject.keys();
			while(keysStr.hasNext()) {
				String key = keysStr.next();
				JSONObject value = jsonObject.getJSONObject(key);
				
				for(int i = 0; i < products.size(); i++){
					if (products.get(i) .getBookId().equals( value.get("id").toString()))
					{
						products.get(i).setOtherProp(value);
					
						if(filter(products.get(i), rFilter, eFilter)){
							notifyItemChanged(i);
						}else{
							products.remove(i);
							notifyItemRemoved(i);
						}
					}
				}
			}
			
		}
		catch (JSONException e)
		{}
	}
	private Boolean filter(Item_book item, EntryRecyclerSearchFilter rF, EntryElasticSearchFilter eF){
		try
		{
			if(rF.getAvailable()){
				String status = item.getOtherProp().getString("status");	
				if(!status.equals("IN_STOCK") ){
					return false;
				}
			}
			if(rF.getMinPrice() > 0.01)
				if( rF.getMinPrice() >= item.getOtherProp().getDouble("price") )
					return false;
				
			if(rF.getMaxPrice() > 0.01)
				if( rF.getMaxPrice() <= item.getOtherProp().getDouble("price"))
					return false;
			
			
		}
		catch (JSONException e)
		{}

		
		
		return true;
	}
	
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ProductsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view, categoryRecyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item_book product = getItem(position);
        holder.bindContent(product);
		holder.productPrice.setVisibility(View.GONE);
		holder.productOldPrice.setVisibility(View.GONE);
		holder.productDiscount.setVisibility(View.GONE);
		holder.productStatus.setVisibility(View.GONE);
        // - replace the contents of the view with that element
        /*holder.productNameTV.setText(holder.product.getName());

        if (loadHighRes && product.getMainImageHighRes() != null) {
            Picasso.with(context).load(product.getMainImageHighRes())
				.fit().centerInside()
				.placeholder(R.drawable.placeholder_loading)
				.error(R.drawable.placeholder_error)
				.into(holder.productImage);
        } else {*/
		 holder.productName.setText(holder.product.getName());
         holder.productAuthor.setText(holder.product.getAuthor())  ;
		 
		 
		 Picasso.get().load(holder.product.getImgUrl())
				.fit().centerInside()
				.placeholder(R.drawable.placeholder_loading)
				.error(R.drawable.placeholder_error)
				.into(holder.productImage);
				
		 if(holder.product.getOtherProp() !=  null){
			 
			 try
			 {
				 String status = holder.product.getOtherProp().getString("button_status");
				 
				 holder.productPrice.setText(holder.product.getOtherProp().getString("price"));
				 if(!holder.product.getOtherProp().getString("old_price").equals(holder.product.getOtherProp().getString("price"))){
					 holder.productOldPrice.setVisibility(View.VISIBLE);
					 holder.productDiscount.setVisibility(View.VISIBLE);
					 
					holder.productOldPrice.setText(holder.product.getOtherProp().getString("old_price"));
					holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					
					Double q = Double.valueOf( holder.product.getOtherProp().getString("price")) * 100.00 / Double.valueOf( holder.product.getOtherProp().getString("old_price") );     
					holder.productDiscount.setText("Вы экономите: " +(int)(100-  q) +"%");
				}
				 
				 if(status.equals("SOON")){
					 holder.productStatus.setText("Ожидается");
					 holder.productStatus.setVisibility(View.VISIBLE);
				 } else if(status.equals("PREORDER")){
					 holder.productStatus.setText("Предзаказ");
					 //holder.productStatus.setVisibility(View.VISIBLE);	 
					 holder.productPrice.setVisibility(View.VISIBLE);
				 }else if(status.equals("IN_BUSKET")){
					 //holder.productStatus.setText("В корзине");
					 //holder.productStatus.setVisibility(View.VISIBLE);
					 holder.productPrice.setVisibility(View.VISIBLE);
				 }else if(status.equals("BUY")){			
					// holder.productStatus.setText("Есть в наличии");
					// holder.productStatus.setVisibility(View.VISIBLE);
					 
					 holder.productPrice.setVisibility(View.VISIBLE);
				 }else if(status.equals("WHERE_BUY")){
					 holder.productStatus.setVisibility(View.GONE);
					 holder.productPrice.setVisibility(View.VISIBLE);
				 }else if(status.equals("NOT_AVAILABLE")){
					 holder.productStatus.setText("Нет в наличии");
					 holder.productStatus.setVisibility(View.VISIBLE);
				 }
			 }
			 catch (JSONException e)
			 {}
		 }
      /*  }

        // Determine if product is on sale
        double pr = holder.product.getPrice();
        double dis = holder.product.getDiscountPrice();
        if (pr == dis || Math.abs(pr - dis) / Math.max(Math.abs(pr), Math.abs(dis)) < 0.000001) {
            holder.productPriceTV.setVisibility(View.VISIBLE);
            holder.productPriceDiscountTV.setVisibility(View.GONE);
            holder.productPriceTV.setText(holder.product.getPriceFormatted());
            holder.productPriceTV.setPaintFlags(holder.productPriceTV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.productPriceTV.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
        } else {
            holder.productPriceTV.setVisibility(View.VISIBLE);
            holder.productPriceDiscountTV.setVisibility(View.VISIBLE);
            holder.productPriceTV.setText(holder.product.getPriceFormatted());
            holder.productPriceTV.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.productPriceTV.setTextColor(ContextCompat.getColor(context, R.color.textSecondary));
            holder.productPriceDiscountTV.setText(holder.product.getDiscountPriceFormatted());
        }*/
    }

    public void clear() {
		notifyItemRangeRemoved(0, products.size());
        products.clear();
    }

    
    

	

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ResizableImageView productImage;
        public TextView productName;
		public TextView productAuthor;
		public TextView productPrice;
		public TextView productStatus;
		public TextView productOldPrice;
		public TextView productDiscount;
		
        private Item_book product;

        public ViewHolder(View v, final CategoryRecyclerInterface categoryRecyclerInterface) {
            super(v);
			
			productImage = (ResizableImageView) v.findViewById(R.id.product_item_image); 
			productName = (TextView) v.findViewById(R.id.product_item_name);
			productAuthor = (TextView) v.findViewById(R.id.product_item_author);
			productStatus = (TextView) v.findViewById(R.id.product_item_status);
			productPrice = (TextView) v.findViewById(R.id.product_item_price);
			productOldPrice = (TextView) v.findViewById(R.id.product_item_old_price);
			productDiscount = (TextView) v.findViewById(R.id.product_item_discount);
			
           /* productPriceTV = v.findViewById(R.id.product_item_price);
            productPriceDiscountTV = v.findViewById(R.id.product_item_discount);
            productImage = v.findViewById(R.id.product_item_image);
           */
			v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(product.getOtherProp() != null)
							categoryRecyclerInterface.onProductSelected(v, product);
					}
				});
        }

        public void bindContent(Item_book product) {
            this.product = product;
        }
		
		
    }
	}
	
