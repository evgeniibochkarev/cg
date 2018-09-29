package ru.chitaigorod.android.entities;

import java.util.*;

public class EntryRecyclerSearchFilter
{
	private Boolean available;
	public EntryRecyclerSearchFilter(){
		available = true;
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
		
		
	}

	public void setAvailable(Boolean b){
		available = b;
	}
	public Boolean getAvailable(){
		return available;
	}
	/*
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
		
		return hm;
	}
}
