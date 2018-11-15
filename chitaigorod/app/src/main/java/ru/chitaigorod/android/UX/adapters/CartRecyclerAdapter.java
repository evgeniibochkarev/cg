package ru.chitaigorod.android.UX.adapters;



/**
 * Adapter handling list of cart items.
 */
 /**/

import android.content.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.gildaswise.horizontalcounter.*;
import com.squareup.picasso.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.custom_view.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.listeners.*;

import ru.chitaigorod.android.R;

public class CartRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) 
	{
		// TODO: Implement this method
            ViewHolderProduct viewHolderProduct = (ViewHolderProduct) holder;

            EntryProductCart cartProductItem = getCartProductItem(position);
            viewHolderProduct.bindContent(cartProductItem);

		try
		{
			viewHolderProduct.cartProductName.setText(cartProductItem.getName());
			viewHolderProduct.cartProductPrice.setText("" + cartProductItem.getTotalItemPriceFormatted());
            /*viewHolderProduct.cartProductDetails.setText(context.getString(
			 R.string.format_string_division, cartProductItem.getVariant().getColor().getValue(),
			 cartProductItem.getVariant().getSize().getValue()));*/
            viewHolderProduct.cartProductQuantity.setMaxValue(cartProductItem.getMaxQuantity());
			viewHolderProduct.cartProductQuantity.setValue(cartProductItem.getQuantity());
			
			viewHolderProduct. cartProductCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton p1, boolean p2)
					{
						EntryProductCart newItem = cartProductItems.get(position);
						newItem.setChecked(p2);
						cartProductItems.set(position, newItem);
						sendCurrBasket();
					}			
				});
			viewHolderProduct.cartProductQuantity.setChangeListener(new HorizontalCounterInterface(){
					@Override
					public void onChange(int count)
					{
						EntryProductCart newItem = cartProductItems.get(position);
						newItem.setQuantity(count);
						cartProductItems.set(position, newItem);
						sendCurrBasket();
					}			
			});
            Picasso.get().load(cartProductItem.getPic())
				.fit().centerInside()
				.placeholder(R.drawable.placeholder_loading)
				.error(R.drawable.placeholder_error)
				.into(viewHolderProduct.cartProductImage);
			
		}
		catch (JSONException e)
		{}
		
      
    }
	
	private  void sendCurrBasket(){
		List<EntryProductCart> currBasket = new ArrayList<>();
		String forDel = "";
		for(int i = 0; i < cartProductItems.size(); i++){
			
			EntryProductCart temp = cartProductItems.get(i);
			if(temp.isChecked()){
				currBasket.add(temp);
			}else{
				try
				{
					forDel = forDel + temp.getId() + ",";
				}
				catch (JSONException e)
				{}
			}
		}
		
		cartRecyclerInterface.onBasketChange(currBasket, forDel);
			
	}
	

    private List<EntryProductCart> cartProductItems = new ArrayList<>();
   // private List<EntryProductCart> currItems = new ArrayList<>();
    private final CartRecyclerInterface cartRecyclerInterface;
    private final Context context;
    private LayoutInflater layoutInflater;

    /**
     * Creates an adapter that handles a list of cart items.
     *
     * @param context               activity context.
     * @param cartRecyclerInterface listener indicating events that occurred.
     */
    public CartRecyclerAdapter(Context context, CartRecyclerInterface cartRecyclerInterface) {
        this.context = context;
        this.cartRecyclerInterface = cartRecyclerInterface;
    }

    @Override
    public int getItemCount() {
        return cartProductItems.size();// + cartDiscountItems.size();
    }

    
	public void refreshItems(JSONArray cart) {
        if (cart != null) {
			cartProductItems.clear();
			for(int i = 0; i < cart.length(); i++){
				try
				{
					cartProductItems.add(new EntryProductCart(cart.getJSONObject(i)));
					
				}
				catch (JSONException e)
				{}
			}
          //  cartProductItems.clear();
           // cartDiscountItems.clear();
          //  cartProductItems.addAll(cart.getItems());
          //  cartDiscountItems.addAll(cart.getDiscounts());
            notifyDataSetChanged();
        } 
    }
	
    private EntryProductCart getCartProductItem(int position) {
        return cartProductItems.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

            View view = layoutInflater.inflate(R.layout.list_item_cart_product, parent, false);
            return new ViewHolderProduct(view, cartRecyclerInterface);
        
    }

	/*
    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
//        Timber.d("Bind position: " + position);
        */
    
    public void clearCart() {
        cartProductItems.clear();
       // cartDiscountItems.clear();
        notifyDataSetChanged();
    }


     // Provide a reference to the views for each data item
    public static class ViewHolderProduct extends RecyclerView.ViewHolder {

        ResizableImageView cartProductImage;
        HorizontalCounter cartProductQuantity;
        TextView cartProductName;
        TextView cartProductPrice;
        TextView cartProductDetails;
		CheckBox cartProductCheck;
        EntryProductCart cartProductItem;

        public ViewHolderProduct(View itemView, final CartRecyclerInterface cartRecyclerInterface) {
            super(itemView);
            cartProductImage = (ResizableImageView) itemView.findViewById(R.id.cart_product_image);
            cartProductQuantity =   (HorizontalCounter) itemView.findViewById(R.id.cart_product_set_quantity);
            cartProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
            cartProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
            cartProductDetails = (TextView) itemView.findViewById(R.id.cart_product_details);

			cartProductCheck = (CheckBox) itemView.findViewById(R.id.deleteCheckCartItem);
           
			//View deleteProduct = itemView.findViewById(R.id.cart_product_delete);
           /* deleteProduct.setOnClickListener(new OnSingleClickListener() {
					@Override
					public void onSingleClick(View v) {
						cartRecyclerInterface.onProductDelete(cartProductItem);
					}
				});*/
            /*View updateProduct = itemView.findViewById(R.id.cart_product_update);
            updateProduct.setOnClickListener(new OnSingleClickListener() {
					@Override
					public void onSingleClick(View v) {
						cartRecyclerInterface.onProductUpdate(cartProductItem);
					}
				});*/
			
            itemView.setOnClickListener(new OnSingleClickListener() {
					@Override
					public void onSingleClick(View v) {
						//cartRecyclerInterface.onProductSelect(cartProductItem.getVariant().getProductId());
					}
				});
        }

        public void bindContent(EntryProductCart cartProductItem) {
            this.cartProductItem = cartProductItem;
        }
    }
	}
