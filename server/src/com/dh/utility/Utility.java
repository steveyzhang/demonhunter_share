package com.dh.utility;

public class Utility {
	public static String getFullPath(String string) {
		return new String( GlobalContext.basePath + GlobalContext.templatePath + string);
	}

}
