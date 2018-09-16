package ru.chitaigorod.android.utils;
import java.sql.*;
import java.util.*;
import org.json.*;

public class APIHelper
{
	public static String getData(int ctxId, String method,JSONObject param){
		String data = "";
		/*for(Map.Entry<String, String> e : param.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();

			data = data + key + ":'"+value+"',";
		}*/
		
		return "javascript:API.item."+method+"('"+ctxId+"', "+param.toString()+");";
	}
}

