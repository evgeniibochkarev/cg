package ru.chitaigorod.android.entities;
import java.util.*;
import org.json.*;
import ru.chitaigorod.android.utils.*;
import kotlin.internal.contracts.*;

public class Item_book
{
	private String name;
	private String content;
	private String date;
	private String d_pic_path;
	private String d_pic_name;
	private String p_pic_path;
	private String p_pic_name;
	private String p_text;
	private String bid;
	private String tags;
	private String ibl_id;
	private String ibl_sec_id;
	private String id;
	private String author_t;
	private String author_b;
	private String author;
	private String author_p;
	private String collection;
	private String seria;
	private String year;
	private String publisher;
	private String pages;
	private String isbn;
	private String predzakaz;
	private String skoro;
	private String only_us;
	private String new_m;
	private String _new;
	private String best;
	private String recommend;
	private String best_m;
	private String school_disciplin;
	private String school_class;
	
	private HashMap<String, Object> data;
	private JSONObject otherProp;
	
	public Item_book(HashMap _data){
		data = new HashMap<String, Object>();
		this.data.putAll(_data);
	}
	
	public Item_book(Object _data){
		//data = new HashMap<String, String>();
		//this.data.putAll(_data);
		//_data.toString()
		try
		{
			HashMap<String, Object> sour =(HashMap<String, Object>) Utils.jsonToMap(((JSONObject)_data).getJSONObject("_source"));
			this.data = sour;
		}
		catch (JSONException e)
		{}
	}
	public String getBookId(){
		return data.get("id").toString();
	}
	public void setOtherProp(JSONObject dat){
		this.otherProp = dat;
	}
	public JSONObject getOtherProp(){
		return otherProp;
	}
	public String getPPicName(){
		return data.get("p_pic_name").toString();
	}
	public String getImgUrl(){
		return "https://img-gorod.ru/upload/" +data.get("p_pic_path").toString() +"/"+data.get("p_pic_name").toString();
	}
	public String getName(){
		return  data.get("name").toString();
	}
	public String getAuthor(){
		return  data.get("author").toString();
	}
	
	public String getIblId(){
		return data.get("ibl_id").toString();
		/*if(data.containsKey("ibl_id")){
			String hh = (String)data.get("ibl_id");
			if( hh != null && !hh.isEmpty()){
				return data.get("ibl_id");
			}
		}
		return "";
		/*
		if(data.get("ibl_id") == "30"){
			return "30";
		}else{
			return "";
		}*/
		
		//return data.get("ibl_id");// == null) ? return "" : return tt;
	}
}
