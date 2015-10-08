package com.esprit.pim.smartsuitecase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {

	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	
	private String message = "";
	private String senderNum ="";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                     
                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
 
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                     
 
                   // Show Alert
                    /*int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, 
                                 "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();*/
                    abortBroadcast();
                } 
                
                Intent intent2 = new Intent(context, TrackingActivity.class);
                intent2.setFlags(intent2.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("msg", message);
                context.startActivity(intent2); // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
	}
	
	
	
	 public static SmsMessage[] getMessagesFromIntent(Intent intent) {
	        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
	        byte[][] pduObjs = new byte[messages.length][];

	        for (int i = 0; i < messages.length; i++) {
	            pduObjs[i] = (byte[]) messages[i];
	        }
	        byte[][] pdus = new byte[pduObjs.length][];
	        int pduCount = pdus.length;
	        SmsMessage[] msgs = new SmsMessage[pduCount];
	        for (int i = 0; i < pduCount; i++) {
	            pdus[i] = pduObjs[i];
	            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
	        }
	        return msgs;
	    }

}
