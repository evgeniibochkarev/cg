package ru.chitaigorod.android.utils;
import android.net.*;
import java.io.*;
import android.support.v7.app.*;

public class Utils
{
	public static String router(String url){
		Uri uri = Uri.parse(url);
		if(uri.getPathSegments().size() == 0){
			return "MainPage";
		}else{
			return "";
		}
	}
	public static String getJSByTag(AppCompatActivity act,String inFile) {
        String tContents = "javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);";

		try {
			InputStream stream = act.getAssets().open(inFile);

			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			tContents = new String(buffer);
		} catch (IOException e) {
			// Handle exceptions here
		}

		return tContents;

	}
	
}
