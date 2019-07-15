package com.example.litianci.yiwangtongban.utils;

import com.google.gson.Gson;

public class GsonUtils {

	
	public static  <T> T  json2bean(String result , Class<T> clazz){
		
		Gson gson = new Gson();
		
		T t = gson.fromJson(result, clazz);
		
		return t;
	}


}
