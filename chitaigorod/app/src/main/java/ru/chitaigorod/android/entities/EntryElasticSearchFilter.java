package ru.chitaigorod.android.entities;
import java.util.*;

public class EntryElasticSearchFilter
{
	private EntryCategory mCategory;
	private String mAuthor;
	
	
	//private HashMap<String, Object> obj;
	
	public EntryElasticSearchFilter(){
		/*obj = new HashMap<String, Object>();
		obj.put("author","not_set");
		obj.put("seria","not_set");
		obj.put("category", null);
		obj.put("year","not_set");*/
		mCategory = null;
		mAuthor = "not_set";
	}
	
	public EntryElasticSearchFilter(EntryCategory ec, String author){
		//obj = new HashMap<String, Object>();
		//obj.putAll(hm);.
		mCategory = ec;
		mAuthor = author;
	}
	
	public void setAuthor(String str){
		mAuthor = str;
	}
	public String getAuthor(){
		return mAuthor;
	}
	
	public EntryCategory getCategory(){
		return mCategory ;
	}
	public void setCategory(EntryCategory ec){
		mCategory = ec;
	}
	
}
