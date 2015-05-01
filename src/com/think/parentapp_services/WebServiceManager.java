package com.think.parentapp_services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.smartparenting.beans.CallBean;
import com.smartparenting.beans.SMSBean;
import com.smartparenting.beans.URLBean;
import com.think.parentapp_util.AppConstants;
import com.think.parentapp_util.AppVariables;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unchecked")
public class WebServiceManager {

	String SOAP_ACTION = "";
	Context context;

	public WebServiceManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public String loginCheck(String username, String password) {
		// TODO Auto-generated method stub
		String METHOD_NAME = "loginCheck";
		String response = "";

		Log.d("TAG", "Inside login");
		SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;

		SoapObject object = new SoapObject(AppConstants.NAME_SPACE, METHOD_NAME);
		object.addProperty("username", username);
		object.addProperty("password", password);

		response = upload(object);
		Log.d("TAG", response);
		return response;
	}

	public String retreiveIMEI() {
		String METHOD_NAME = "retreiveIMEI";
		String response = "";

		Log.d("TAG", "Inside login");
		SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;

		SoapObject object = new SoapObject(AppConstants.NAME_SPACE, METHOD_NAME);
		object.addProperty("username", AppVariables.username);
		response = upload(object);
		Log.d("TAG", response);
		return response;
	}

	public ArrayList<CallBean> retreiveCalls(String imei) {
		ArrayList<CallBean> callLog = new ArrayList<CallBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveCalls";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));
			object.addProperty("date", date);

			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				callLog = ((ArrayList<CallBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return null;
		}
		return callLog;
	}

	public ArrayList<SMSBean> retreiveSMS(String imei) {
		ArrayList<SMSBean> smsLog = new ArrayList<SMSBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveSMS";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));
			object.addProperty("date", date);

			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				smsLog = ((ArrayList<SMSBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return smsLog;
	}

	public ArrayList<URLBean> retreiveURL(String imei) {
		ArrayList<URLBean> urlLog = new ArrayList<URLBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveURL";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));
			object.addProperty("date", date);

			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				urlLog = ((ArrayList<URLBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlLog;
	}

	public byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	public String upload(SoapObject object) {
		String response = "";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(object);
		HttpTransportSE transport = new HttpTransportSE(AppConstants.URL);
		try {
			transport.call(SOAP_ACTION, envelope);
			SoapPrimitive primitive = (SoapPrimitive) envelope.getResponse();
			response = primitive.toString();
			return response;

		} catch (IOException e) {

			e.printStackTrace();
			return "false";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return "No Internet";
		}

	}

	

	public ArrayList<CallBean> retreiveCallsByDate(String imei,
			String fromdate, String todate) {
		// TODO Auto-generated method stub
		ArrayList<CallBean> callLog = new ArrayList<CallBean>();
		String response = "";
		try {
			String METHOD_NAME = "retriveFromToCall";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));*/
			object.addProperty("from", fromdate);
			object.addProperty("to", todate);
			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				callLog = ((ArrayList<CallBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return null;
		}
		return callLog;
	}

	public ArrayList<CallBean> retreiveCallsByNumber(String imei,
			String number) {
		// TODO Auto-generated method stub
		ArrayList<CallBean> callLog = new ArrayList<CallBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveCallByNumber";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));*/
			object.addProperty("number", number);
		
			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				callLog = ((ArrayList<CallBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return null;
		}
		return callLog;
	}

	public ArrayList<SMSBean> retreiveSMSbyDate(String imei, String fromdate,
			String todate) {
		// TODO Auto-generated method stub
		ArrayList<SMSBean> smsLog = new ArrayList<SMSBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveFromToSMS";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));*/
			object.addProperty("from", fromdate);
			object.addProperty("to", todate);
			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				smsLog = ((ArrayList<SMSBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return smsLog;
	}

	public ArrayList<SMSBean> retreiveSMSbyNumber(String imei, String number) {
		// TODO Auto-generated method stub
		ArrayList<SMSBean> smsLog = new ArrayList<SMSBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveSMSByNumber";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));*/
			object.addProperty("number", number);
			
			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				smsLog = ((ArrayList<SMSBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return smsLog;
	}

	public ArrayList<URLBean> retreiveURLbyDate(String imei, String fromdate,
			String todate) {
		// TODO Auto-generated method stub
		ArrayList<URLBean> urlLog = new ArrayList<URLBean>();
		String response = "";
		try {
			String METHOD_NAME = "retreiveFromToURL";

			SOAP_ACTION = AppConstants.NAME_SPACE + METHOD_NAME;
			SoapObject object = new SoapObject(AppConstants.NAME_SPACE,
					METHOD_NAME);
			object.addProperty("imei", imei);
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat
					.format(new Date(System.currentTimeMillis()));*/
			object.addProperty("from", fromdate);
			object.addProperty("to", todate);
			response = upload(object);
			byte[] beanBytes = Base64.decode(response, Base64.CRLF);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					beanBytes);
			ObjectInputStream ois;

			ois = new ObjectInputStream(byteArrayInputStream);

			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				urlLog = ((ArrayList<URLBean>) obj);
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlLog;
	}

}
