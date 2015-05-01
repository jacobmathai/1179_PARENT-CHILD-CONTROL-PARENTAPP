package com.think.parentapp_util;

import android.telephony.SmsManager;

public class SendTextMessage {

	public void send(String mobile) {
		// TODO Auto-generated method stub
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(mobile, null, AppConstants.TEXT_MESSAGE, null,
				null);
	}

}
