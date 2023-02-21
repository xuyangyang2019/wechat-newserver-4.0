package com.jubotech.framework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.util.StringUtils;

public class StringUtil {

	private static final String UNDERLINE = "_";

	/**
	 * 首字母变小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	public static List<String> stringToList(String str, String split) {
		List<String> list = new ArrayList<>();
		if (!StringUtils.isEmpty(str)) {
			String[] s = str.split(split);
			if (null != s && s.length > 0) {
				for (int i = 0; i < s.length; i++) {
					list.add(s[i]);
				}
			} else {
				list.add(str);
			}
		}
		return list;
	}

	public static List<String> stringToList(String str) {
		List<String> list = new ArrayList<>();
		if (!StringUtils.isEmpty(str)) {
			String[] s = str.split(",");
			if (null != s && s.length > 0) {
				for (int i = 0; i < s.length; i++) {
					list.add(s[i]);
				}
			} else {
				list.add(str);
			}
		}
		return list;
	}

	public static String ListToString(List<String> list) {
		String s = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				s = s + list.get(i) + ",";
			}
		}
		if (!StringUtils.isEmpty(s) && s.endsWith(",")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;

	}

	public static List<String> removeRepeat(List<String> list) {
		Set<String> set = new HashSet<>();
		List<String> newList = new ArrayList<String>();
		for (String cd : list) {
			if (set.add(cd)) {
				newList.add(cd);
			}
		}
		return newList;
	}

	public static String camelToUnderline(String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		int len = value.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	public static String getEmoji() {
		List<String> list = new ArrayList<>();
		list.add("[微笑]");
		list.add("[发呆]");
		list.add("[得意]");
		list.add("[害羞]");
		list.add("[调皮]");
		list.add("[呲牙]");
		list.add("[惊讶]");
		list.add("[酷]");
		list.add("[冷汗]");
		list.add("[偷笑]");
		list.add("[愉快]");
		list.add("[白眼]");
		list.add("[憨笑]");
		list.add("[悠闲]");
		list.add("[奋斗]");
		list.add("[抠鼻]");
		list.add("[坏笑]");
		list.add("[阴险]");
		list.add("[西瓜]");
		list.add("[篮球]");
		list.add("[太阳]");
		list.add("[强]");
		list.add("[抱拳]");
		list.add("[胜利]");
		list.add("[转圈]");
		list.add("[跳跳]");
		list.add("[乱舞]");
		list.add("[激动]");
		list.add("[左太极]");
		list.add("[右太极]");
		Random rand = new Random();
		return list.get(rand.nextInt(list.size()));
	}
}
