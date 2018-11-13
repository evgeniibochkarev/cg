package ru.chitaigorod.android.entities;
import org.json.*;

public class EntryProductCart
{
	private JSONObject product;
	private Boolean mChecked;
	
	public EntryProductCart(JSONObject it){
		product = it;
		mChecked = false;
	}
	
	public JSONObject toJSON(){
		return product;
	}
	public boolean isChecked(){
		return mChecked;
	}
	public void setChecked(Boolean val){
		mChecked = val;
	}
	public String getId() throws JSONException{
		return product.getString("ID");
	}
	
	public String getName() throws JSONException{
		return product.getString("NAME");
	}
	
	public String getProductId() throws JSONException{
		return product.getString("PRODUCT_ID");
	}
	public int getQuantity() throws JSONException{
		return product.getInt("QUANTITY");
	}
	public void setQuantity(int q){
		try
		{
			product.put("QUANTITY", q);
		}
		catch (JSONException e)
		{}
	}
	public Boolean getDelay() throws JSONException{
		return product.getString("DELAY").equals("Y");
	}
	public Boolean getCanBuy() throws JSONException{
		return product.getString("CAN_BUY").equals("Y");
	}
	public String getPrice() throws JSONException{
		return product.getString("PRICE");
	}
	public String getWeight() throws JSONException{
		return product.getString("WEIGHT");
	}
	public int getMaxQuantity() throws JSONException{
		return product.getInt("MAX_QUANTITY");
	}
	public String getAuthor() throws JSONException{
		return product.getString("AUTHOR");
	}
	public Boolean getPredzakaz() throws JSONException{
		return product.getString("PREDZAKAZ").equals("Y");
	}
	public String getPic() throws JSONException{
		return product.getString("DETAIL_PICTURE_SRC");
	}
	
	public Double getTotalItemPriceFormatted() throws JSONException, NumberFormatException{
		return getQuantity()* Double.valueOf(getPrice());
	}
	
	
}


/*
"ID":"34908439",
"NAME":"Золотая воровка",
"PRODUCT_PROVIDER_CLASS":"CCatalogProductProvider",
"CALLBACK_FUNC":null,
"MODULE":"catalog",
"PRODUCT_ID":"444683",
"PRODUCT_PRICE_ID":"310508",
"QUANTITY":3,
"DELAY":"N",
"CAN_BUY":"Y",
"CURRENCY":"RUB",
"SUBSCRIBE":"N",
"TYPE":null,
"SET_PARENT_ID":null,
"PRICE":"93.00",
"WEIGHT":"110.00",
"DISCOUNT_NAME":null,
"DISCOUNT_VALUE":null,
"DISCOUNT_COUPON":null,
"DISCOUNT_PRICE":"0.00",
"PRODUCT_XML_ID":"2360585",
"DETAIL_PAGE_URL":"/catalog/book/444683/",
"QUANTITY_DETAIL":{"1":{"QUANTITY":1,"DAYS":1},"2":{"QUANTITY":9,"DAYS":1},
"7":{"QUANTITY":8,"DAYS":7}},
"MAX_QUANTITY":18,
"CHECKED":false,
"DETAIL_PICTURE":"6572153",
"AUTHOR":"Катаев Н.",
"PREDZAKAZ":"N",
"DETAIL_PICTURE_SRC":"https://img-gorod.ru/upload/iblock/0a8/140_224/0a8472456c27e14e85ca013b2ed5b394.jpg",
"CATALOG":{"IBLOCK_ID":30,"XML_ID":"2360585","CODE":"","SECTION_ID":[9657,9685,9688],

*/
