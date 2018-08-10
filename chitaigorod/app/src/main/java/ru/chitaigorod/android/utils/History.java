package ru.chitaigorod.android.utils;
import java.util.*;

public class History
{
	ArrayList<Obj> his;
	public History(){
		his = new ArrayList<Obj>();
	}
	public void add(String tag, String url){
		for(int i = 0; i < his.size(); i++){
			if(his.get(i).tag == tag){
				his.remove(i);
			}
		}
		
		his.add(new Obj(tag, url));
	}
	
	public String goBack(){
		his.remove(his.size()-1);
		return his.get((his.size()-1)).tag;
	}
	
	private class Obj{
		String tag;
		String url;
		public Obj(String tag, String url){
			this.tag = tag;
			this.url = url;
		}
		
		public String tag(){
			return tag;
		}
		public String url(){
			return url;
		}
	}
}
