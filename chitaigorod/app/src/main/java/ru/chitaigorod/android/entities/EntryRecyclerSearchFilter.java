package ru.chitaigorod.android.entities;

import java.util.*;

public class EntryRecyclerSearchFilter
{
	private Boolean available;
	private Double maxPrice;
	private Double minPrice;
	
	public EntryRecyclerSearchFilter(){
		available = true;
		maxPrice = 0.0;
		minPrice = 0.0;
		/*obj = new HashMap<String, String>();
		obj.put("available", "1");
		 /*obj.put("seria","not_set");
		obj.put("category","not_set");
		obj.put("year","not_set");*/
	}

	public EntryRecyclerSearchFilter(HashMap hm){
		//HashMap obj = new HashMap<String, String>();
		if(hm.containsKey("available"))
			available = (Boolean) hm.get("available");
		if(hm.containsKey("max_price"))
			maxPrice = (Double) hm.get("max_price");
		if(hm.containsKey("min_price"))
			minPrice = (Double) hm.get("min_price");
	}

	public void setAvailable(Boolean b){
		available = b;
	}
	public Boolean getAvailable(){
		return available;
	}
	public void setMaxPrice(Double d){
		maxPrice = d;
	}
	public Double getMaxPrice(){
		return maxPrice;
	}
	public void setMinPrice(Double d){
		minPrice = d;
	}
	public Double getMinPrice(){
		return minPrice;
	}
	
	
	/*public void setMaxPrice(Double d){
	 maxPrice = d;
	 }
	 public Double getMaxPrice(){
	 return maxPrice;
	 }
	public void setSeria(String str){
		obj.put("seria",str);
	}
	public void setCategory(String str){
		obj.put("category",str);
	}
	public void setYear(String str){
		obj.put("year",str);
	}*/
	
	public HashMap getHashMap(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("available", available);
		hm.put("max_price", maxPrice);
		hm.put("min_price", maxPrice);
		return hm;
	}
}
