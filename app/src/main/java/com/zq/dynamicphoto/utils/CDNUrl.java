package com.zq.dynamicphoto.utils;

public class CDNUrl {
	
	public static String toCNDURL(String url){
		return "http://srsoure.redshoping.cn/"+url.substring(53,url.length());
	}
}
