package com.think.parentapp_util;

public class AppConstants {

	public static String IP = "172.30.14.125";
	public static String NAME_SPACE = "http://webservice.pcontrol.com/";
	public static String SHARED_PREFERENCES = "Parental_Control";
	public static String URL = "http://" + IP
			+ ":8084/Parental_Control/ParentalControlWebService?wsdl";
	public static String TYPE_OUTGOING = "TYPE_OUTGOING";
	public static String TYPE_RECEIVED = "TYPE_RECEIVED";
	public static String TYPE_REJECTED = "TYPE_REJECTED";
	public static String TYPE_MISSED = "TYPE_MISSED";
	public static String STREAM_URL = "http://" + IP
			+ ":8084/Parental_Control/AudioStreamServlet?path=";

	public static String TEXT_MESSAGE = "text_message";
}

