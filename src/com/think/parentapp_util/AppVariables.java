package com.think.parentapp_util;

import java.util.ArrayList;

import com.smartparenting.beans.CallBean;
import com.smartparenting.beans.SMSBean;
import com.smartparenting.beans.URLBean;

public class AppVariables {

	public static ArrayList<CallBean> callLog = null;
	public static ArrayList<SMSBean> smsLog = null;
	public static ArrayList<URLBean> urlLog = null;
	
	public static ArrayList<String> children ;
	public static String username = "";
	public static int TODATE = 0;
}
