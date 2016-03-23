package com.gaddiel.remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends WakefulBroadcastReceiver {
	private static final String TAG = "Message recieved";
	
	protected static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    
	public static String trigger_message="";
	
	static final int TIME_DIALOG_ID = 999;
	
	@Override
    public void onReceive(Context context, Intent intent)
    {
		
		
    	 String str = "";
         String phonno="";
         String body="";
         Bundle pudsBundle = intent.getExtras();
 	     Object[] pdus = (Object[]) pudsBundle.get("pdus");
 	     SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]); 
 	    Log.i(TAG,  messages.getMessageBody());
 	     phonno=messages.getOriginatingAddress();
 	     body=messages.getMessageBody(); 
 	    Toast.makeText(context, "hitting at income", Toast.LENGTH_SHORT).show(); 
 	    abortBroadcast();
 	     
 	     if((body.startsWith("#"))){
 	    	
 	   
 	    	Intent intents = new Intent(context, Send.class);
            Toast.makeText(context, "hitting at incoming sms", Toast.LENGTH_SHORT).show(); 
 	     
           
           //bundle.putString("phonno", phonno);
           //bundle.putString("body", body);
        intents.putExtra("phonno",phonno);
      intents.putExtra("body",body);

      intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           
           
           //context.startService(intents);
      startWakefulService(context,intents);
            }
 	     
 	     
          
 	    if(body.equals("gaddielTrack#Stop")){
 	    	//abortBroadcast();
 	    	Intent intents = new Intent(context, Send.class);
 	    	
 	    	Toast.makeText(context, "incoming sms no", Toast.LENGTH_LONG).show(); 
 	    	Toast.makeText(context, "incoming sms body", Toast.LENGTH_LONG).show();
 	    	
 	    	
 	 	   
 	 	    
 	            // Toast.makeText(context, body, Toast.LENGTH_SHORT).show(); 
 	 	     
 	           
 	          // bundle.putString("phonno", phonno);
 	          // bundle.putString("body", body);
 	       intents.putExtra("phonno",phonno);
 	       intents.putExtra("body",body);

 	      intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	     completeWakefulIntent(intents);
 	           
 	           
 	           context.stopService(intents);
 	           
 	           
 	          final Handler handler = new Handler();
  	         handler.postDelayed(new Runnable() {
  	           @Override
  	           public void run() {
  	             //Do something after 100ms
  	        	  android.os.Process.killProcess(android.os.Process.myPid());
  	           }
  	         }, 10000);
 	          
 	    	
 	    	
 	    }
 	     
 	    
    }
	 
    
	
	 public void onReceive1(Context context, Intent intent) {                
	        Intent i = new Intent(context, IncomingSms.class); 
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startService(i);       
	        
	    }

}
