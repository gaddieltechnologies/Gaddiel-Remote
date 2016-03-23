package com.gaddiel.remote;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	static final int TIME_DIALOG_ID = 999;
    long timeval;
    String phonno="";
    String body="";
    Intent i=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);            
    }
	public void onReceive(Context context, Intent intent)
    {
    	 String str = "";
    
         if ((intent.getAction() != null) && (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")))
         {
        Bundle pudsBundle = intent.getExtras();
 	     Object[] pdus = (Object[]) pudsBundle.get("pdus");
 	     SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);   
 	 
 	     phonno=messages.getOriginatingAddress();
 	     body=messages.getMessageBody();     
 	    
           Toast.makeText(context, body, Toast.LENGTH_SHORT).show();  

 	     Intent intents = new Intent(context, IncomingSms.class);	             
           //Bundle bundle=new Bundle();
           //bundle.putString("phonno", phonno);
           //bundle.putString("body", body);
          //intent.putExtra("phonno",phonno);
         // intent.putExtra("body",body);
           //intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          // intents.putExtras(bundle);         
           context.startActivity(intents);
 	    
         /* if(body.equals("gaddielTrack#Stop")){
        	   android.os.Process.killProcess(android.os.Process.myPid());
           	
             }        */
    }
    }
}
