package com.uwi.idworks.service;

public class System {
	public static String getenv(String key) {
		String value = null;
		if (key.equals("OVERRIDE_SEMESTER"))
		  value = "true";
		else if (key.equals("SEMESTER"))
		  value = "202230";
	
        return value;
    }
}
