package ru.chitaigorod.android.entities;
import java.util.*;

public class SearchFilter
{
	private HashMap<String, String> obj;
	
	public SearchFilter(){
		obj = new HashMap<String, String>();
		obj.put("author","not_set");
		obj.put("seria","not_set");
		obj.put("category","not_set");
		obj.put("year","not_set");
	}
	
	public SearchFilter(HashMap hm){
		obj = new HashMap<String, String>();
		obj.putAll(hm);
	}
	
	public void setAuthor(String str){
		obj.put("author",str);
	}
	public void setSeria(String str){
		obj.put("seria",str);
	}
	public void setCategory(String str){
		obj.put("category",str);
	}
	public void setYear(String str){
		obj.put("year",str);
	}
	public HashMap getFilter(){
		return obj;
	}
}
