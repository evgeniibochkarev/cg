package ru.chitaigorod.android.UX.fragments;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.adapters.*;
import ru.chitaigorod.android.UX.dialogs.*;
import ru.chitaigorod.android.entities.*;
import ru.chitaigorod.android.interfaces.*;
import ru.chitaigorod.android.utils.*;

import android.support.v7.widget.Toolbar;

public class CartFragment extends BaseFragment
{

	public static CartFragment newInstance(){
		
		return new CartFragment();
	}

	@Override
	public void APIResponse(JSONObject json)
	{
		// TODO: Implement this method
		super.APIResponse(json);
		
		try
		{
			String method = json.getString("method");
			
			if (method.equals("CartFragment.getBasket"))
			{
				//cart = json.getJSONObject("data
				cartRecyclerAdapter.refreshItems(json.getJSONObject("data").getJSONArray("basket"));
			}
			if(method.equals("CartFragment.showAuth")){
				mFragmentNavigation.showDialog(AuthDialogFragment.newInstance());
			}
			if(method.equals("CartFragment.showOrder")){
				mFragmentNavigation.pushFragment(Order2Fragment.newInstance());
			}
			
		}
		catch (JSONException e)
		{}
		
	}

	
	
	private ProgressDialog progressDialog;

    
    private RecyclerView cartRecycler;
    private CartRecyclerAdapter cartRecyclerAdapter;
	private TextView cartSumText;
    private Button cartBuyBtn;
	private String forDel;
	private List<EntryProductCart> currBasket = new ArrayList<>();
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

		Toolbar tb = (Toolbar) view.findViewById(R.id.fragment_cart_toolbar);
		tb.setTitle("Корзина");
		
		cartSumText = (TextView) view.findViewById(R.id.cart_bottom_bar_text);
        cartBuyBtn = (Button) view.findViewById(R.id.cart_buy_button);
		
		cartBuyBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if(currBasket.size() > 0){
						JSONArray data = new JSONArray();
						for(int i = 0; i< currBasket.size(); i++){
							data.put(currBasket.get(i).toJSON());
							
						}

						JSONObject out = new JSONObject();
						try
						{
							out.put("forDel", forDel);
						}
						catch (JSONException e)
						{}
						try
						{
							out.put("basket", data);
							mFragmentNavigation.get(APIHelper.getData("CartFragment.onGoOrder", out));
						}
						catch (JSONException e)
						{}

					}		

					else{
						Toast.makeText(getActivity(), "Выберите товары для оплаты", Toast.LENGTH_SHORT).show();
					}
				}
		});
		progressDialog = Utils.generateProgressDialog(getActivity(), false);
        prepareCartRecycler(view);

        
/*

        Button order = (Button) view.findViewById(R.id.cart_order);
        order.setOnClickListener(new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					if (getActivity() instanceof MainActivity) {
						//((MainActivity) getActivity()).onOrderCreateSelected();
					}
				}
			});*/

        ((MainActivity) getActivity()).get(APIHelper.getData("CartFragment.getBasket", ""));
        return view;
    }
	
	/*
	private void getCartContent() {
        User user = SettingsMy.getActiveUser();
        if (user != null) {
            String url = String.format(EndPoints.CART, SettingsMy.getActualNonNullShop(getActivity()).getId());

            progressDialog.show();
            GsonRequest<Cart> getCart = new GsonRequest<>(Request.Method.GET, url, null, Cart.class,
				new Response.Listener<Cart>() {
					@Override
					public void onResponse(@NonNull Cart cart) {
						if (progressDialog != null) progressDialog.cancel();

						MainActivity.updateCartCountNotification();
						if (cart.getItems() == null || cart.getItems().size() == 0) {
							setCartVisibility(false);
						} else {
							setCartVisibility(true);
							cartRecyclerAdapter.refreshItems(cart);

							cartItemCountTv.setText(getString(R.string.format_quantity, cart.getProductCount()));
							cartTotalPriceTv.setText(cart.getTotalPriceFormatted());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (progressDialog != null) progressDialog.cancel();
						setCartVisibility(false);
						Timber.e("Get request cart error: %s", error.getMessage());
						MsgUtils.logAndShowErrorMessage(getActivity(), error);
					}
				}, getFragmentManager(), user.getAccessToken());
            getCart.setRetryPolicy(MyApplication.getDefaultRetryPolice());
            getCart.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(getCart, CONST.CART_REQUESTS_TAG);
        } else {
            LoginExpiredDialogFragment loginExpiredDialogFragment = new LoginExpiredDialogFragment();
            loginExpiredDialogFragment.show(getFragmentManager(), "loginExpiredDialogFragment");
        }
    }


    private void setCartVisibility(boolean visible) {
        if (visible) {
            if (emptyCart != null) emptyCart.setVisibility(View.GONE);
            if (cartRecycler != null) cartRecycler.setVisibility(View.VISIBLE);
            if (cartFooter != null) cartFooter.setVisibility(View.VISIBLE);
        } else {
            if (cartRecyclerAdapter != null) cartRecyclerAdapter.cleatCart();
            if (emptyCart != null) emptyCart.setVisibility(View.VISIBLE);
            if (cartRecycler != null) cartRecycler.setVisibility(View.GONE);
            if (cartFooter != null) cartFooter.setVisibility(View.GONE);
        }
    }*/

    private void prepareCartRecycler(View view) {
        this.cartRecycler = (RecyclerView) view.findViewById(R.id.cart_recycler);
       // cartRecycler.addItemDecoration(new RecyclerDividerDecorator(getActivity()));
        cartRecycler.setItemAnimator(new DefaultItemAnimator());
        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerAdapter = new CartRecyclerAdapter(getActivity(), new CartRecyclerInterface() {
				@Override
				public void onBasketChange(List<EntryProductCart> basket, String _forDel)
				{
					forDel = _forDel;
					currBasket = basket;
					Double sum = .0;
					for(int i = 0; i < basket.size(); i++){
						EntryProductCart item = basket.get(i);
						
						try
						{
							sum += item.getQuantity() * Double.valueOf(item.getPrice());
							
						}
						catch (JSONException e)
						{}
						catch (NumberFormatException e)
						{}
					}
					cartBuyBtn.setText("Купить ("+ basket.size()+")");
					cartSumText.setText(String.format("%.2f", sum)  +"₽");
				}		
		});
        cartRecycler.setAdapter(cartRecyclerAdapter);
    }
	
	
}
