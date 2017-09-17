package com.imprender.project4spark.service;

import java.util.Arrays;
import java.util.List;

public class EntryUtils {

	public static List<String> getTAgs(String string) {
		if (string.equals("")) {
			return null;
		}
		string = string.replace(" ", "");
		List<String> tags = Arrays.asList(string.split(","));
		for (int i = 0; i < tags.size(); i++) {
			tags.set(i, "@" + tags.get(i));
		}
		return tags;
	}
}
