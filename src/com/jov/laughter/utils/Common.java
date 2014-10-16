package com.jov.laughter.utils;

public class Common {

	public static String HTTPURL = "http://joveth.github.io/funny/";
	public static String QIMGURL = "http://joveth.github.io/funny/imgs";
	public static boolean IMGFLAG = false;
	public static boolean isEmpty(String str){
		return str==null||str.trim().length()==0;
	}
	public static String cutLongStr(String content, int length) {
		if (content.length() < length / 2)
			return content;
		int count = 0;
		StringBuffer sb = new StringBuffer();
		String[] ss = content.split("");
		for (int i = 1; i < ss.length; i++) {
			count += ss[i].getBytes().length > 1 ? 2 : 1;
			sb.append(ss[i]);
			if (count >= length)
				break;
		}
		return (sb.toString().length() < content.length()) ? sb.append("...")
				.toString() : content;
	}
	public static boolean isLongStr(String content, int length) {
		if (content.length() < length / 2)
			return false;
		int count = 0;
		StringBuffer sb = new StringBuffer();
		String[] ss = content.split("");
		for (int i = 1; i < ss.length; i++) {
			count += ss[i].getBytes().length > 1 ? 2 : 1;
			sb.append(ss[i]);
			if (count >= length)
				return true;		}
		return false;
	}
}
