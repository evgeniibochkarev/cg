package ru.chitaigorod.android.UX.adapters;

import android.content.*;
import android.graphics.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.UX.custom_view.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;


public class MainPageRecyclerAdapter extends RecyclerView.Adapter<MainPageRecyclerAdapter.ViewHolder> {

	 private final Context context;
	 private final MainPageRecyclerInterface recyclerInterface;
	 private JSONArray products = new JSONArray();
	 private LayoutInflater layoutInflater;



	 
    public MainPageRecyclerAdapter(Context context, MainPageRecyclerInterface recyclerInterface) {
        this.context = context;
        this.recyclerInterface = recyclerInterface;


    }
/*
    private Item_book getItem(int position) {
        return products.get(position);
    }*/

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.length();
    }

    public void addProducts(JSONArray productList) {
       // int st = products.size() ;
		products = productList;
		 notifyDataSetChanged();
		//notifyItemRangeInserted(products.size(), products.size() - st);	
    }

	
	

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MainPageRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_horizontal_recycler, parent, false);
        return new ViewHolder(view, recyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Item_book product = getItem(position);
  		try{
	   holder.bindContent((JSONObject)products.get(position));
		//holder.productPrice.setVisibility(View.GONE);
		holder.productOldPrice.setVisibility(View.GONE);
		holder.productDiscount.setVisibility(View.GONE);
		holder.productStatus.setVisibility(View.GONE);
        // - replace the contents of the view with that element
        
		holder.productName.setText(holder.product.getString("name"));
		holder.productAuthor.setText(holder.product.getString("author"))  ;


		Picasso.get().load(holder.product.getString("img"))
			.fit().centerInside()
			.placeholder(R.drawable.placeholder_loading)
			.error(R.drawable.placeholder_error)
			.into(holder.productImage);	

			holder.productPrice.setText(holder.product.getString("price"));
			
		
		}
		catch (JSONException e)
		{}

    }

    





    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ResizableImageViewHeight productImage;
        public TextView productName;
		public TextView productAuthor;
		public TextView productPrice;
		public TextView productStatus;
		public TextView productOldPrice;
		public TextView productDiscount;

        private JSONObject product;

        public ViewHolder(View v, final MainPageRecyclerInterface recyclerInterface) {
            super(v);

			productImage = (ResizableImageViewHeight) v.findViewById(R.id.product_item_image); 
			productName = (TextView) v.findViewById(R.id.product_item_name);
			productAuthor = (TextView) v.findViewById(R.id.product_item_author);
			productStatus = (TextView) v.findViewById(R.id.product_item_status);
			productPrice = (TextView) v.findViewById(R.id.product_item_price);
			productOldPrice = (TextView) v.findViewById(R.id.product_item_old_price);
			productDiscount = (TextView) v.findViewById(R.id.product_item_discount);

	
			v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try
						{
							recyclerInterface.onItemSeleted(product.getString("id"));
						}
						catch (JSONException e)
						{}
						/*if(product.getOtherProp() != null)
							categoryRecyclerInterface.onProductSelected(v, product);
					*/}
				});
        }

        public void bindContent(JSONObject product) {
            this.product = product;
        }


    }
	}

