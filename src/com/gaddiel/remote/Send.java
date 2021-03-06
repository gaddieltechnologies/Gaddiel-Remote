package com.gaddiel.remote;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Send extends Service {
	Handler m_handler;
	Runnable m_handlerTask ;
	Boolean checker;	
	String phonno;
	String body;
	String locations;
	String addressText;
	long timeval;
	double lat;
	double lng;
	String newtime;
	protected static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}	
	@Override
	public int onStartCommand( Intent intents, int flags, int startId)
	{
	      super.onStartCommand(intents, flags, startId);     
		LocationManager locationManager;
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	   // String provider = locationManager.getBestProvider(criteria, true);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
	    Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);	    
	    updateWithNewLocation(location);	    
	   // locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);         
	    Bundle extras = intents.getExtras();
	        phonno =extras.getString("phonno").toString();
	       body = extras.getString("body").toString();
	       if(body.equals("#min")||body.equals("#sec")||body.equals("#hrs")){
	    	   sendSMS(phonno, locations);
	       }
	       if(body.equals("gaddielTrack#Stop")){
	           android.os.Process.killProcess(android.os.Process.myPid());
	            }
	       
	       if(body.startsWith("#min")){
         	  //char bod=body.charAt(4);
         	  //char bod1=body.charAt(5);
         	  //String s=new StringBuilder().append(bod).append(bod1).toString();
         	  //long num=Long.parseLong(s);
	    	   if(body.equals("#min")){
	    		   sendSMS(phonno, locations);
	    	   }
	    	   else{
         	  
         	 String numberOnly= body.replaceAll("[^0-9]", "");
         	long num=Long.parseLong(numberOnly);
         	  
            timeval=num*60000;   }               
           }                
           else if(body.startsWith("#hrs")){
         	 // char bod=body.charAt(4);
         	 // char bod1=body.charAt(5);
         	 // String s=new StringBuilder().append(bod).append(bod1).toString();
         	 // long num=Long.parseLong(s);
            
        	   String numberOnly= body.replaceAll("[^0-9]", "");
            	long num=Long.parseLong(numberOnly);
        	   
        	   timeval=num*3600*1000;
           
           }                 
           else if(body.startsWith("#day")){
         	 // char bod=body.charAt(4);
         	 // char bod1=body.charAt(5);
         	 // String s=new StringBuilder().append(bod).append(bod1).toString();
         	 // long num=Long.parseLong(s); 
            
        	   String numberOnly= body.replaceAll("[^0-9]", "");
            	long num=Long.parseLong(numberOnly);
        	   
        	   timeval=num*24*3600*1000;
           }
           
           else if(body.startsWith("#sec")){
         	 // char bod=body.charAt(4);
         	 // char bod1=body.charAt(5);
         	 // String s=new StringBuilder().append(bod).append(bod1).toString();
         	 // long num=Long.parseLong(s);
         	  
         	 String numberOnly= body.replaceAll("[^0-9]", "");
          	long num=Long.parseLong(numberOnly);
              timeval=num*1000;
             }
           
           else if(body.startsWith("#mon")){
         	 // char bod=body.charAt(4);
         	 // char bod1=body.charAt(5);
         	 // String s=new StringBuilder().append(bod).append(bod1).toString();
         	 // long num=Long.parseLong(s);
            
        	   String numberOnly= body.replaceAll("[^0-9]", "");
            	long num=Long.parseLong(numberOnly);
        	   
        	   timeval=num*24*3600*1000*30;
           }
           
           else if(body.startsWith("#yrs")){
         	 // char bod=body.charAt(4);
         	 // char bod1=body.charAt(5);
         	 // String s=new StringBuilder().append(bod).append(bod1).toString();
         	 // long num=Long.parseLong(s);
         	  
        	   String numberOnly= body.replaceAll("[^0-9]", "");
            	long num=Long.parseLong(numberOnly);
        	   
        	   timeval=num*24*3600*1000*365;
           }
	       
	       
	       else if(body.equals("gaddielTrack#E5min")){
	                       timeval=60000;                  
	                      }                
	                      else if(body.equals("gaddielTrack#E10min")){
	                       timeval=300000;
	                      
	                      }                 
	                      else if(body.equals("gaddielTrack#E15min")){
	                       timeval=900000;
	                      }
	                      
	                      else if(body.equals("gaddielTrack#E30min")){
	                         timeval=1800000;
	                        }
	                      
	                      else if(body.equals("gaddielTrack#Hrs")){
	                       timeval=3600000;
	                      }
	                      
	                      else if(body.equals("gaddielTrack#Dys")){
	                       timeval=86400000;
	                      }
	                      
	                      else if(body.equals("gaddielTrack#Wkys")){
	                       timeval=604800000;
	                      }
	                      
	                      else if(body.equals("gaddielTrack#Mnths")){
	                     
	                       timeval=2592000000L;
	                      }
	                      
	                      else if(body.equals("gaddielTrack#Yrs")){
	                       timeval=31536000000L;
	                       
	                      }   
	                 
	                      else{
	                       return 0;
	                      }             
	             m_handler = new Handler();   
	             
	             m_handlerTask = new Runnable()
	             {  
	                @Override 
	                public void run() {  

	                 m_handler.postDelayed(m_handlerTask, timeval);   
	                 /*LocationManager locationManager;
	         	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	         	   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
	                 Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
	                 updateWithNewLocation(location);*/
	               sendSMS(phonno, locations);
	                        }           
	                }; 
	                m_handlerTask.run();	 
	                return START_REDELIVER_INTENT;	                
	       }
	    private void sendSMS(String phoneNumber, String message)
	    {
	    String SENT = "SMS_SENT";
	    String DELIVERED = "SMS_DELIVERED";
	    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
	    new Intent(SENT), 0);
	    PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);
	    //---when the SMS has been sent---
	    registerReceiver(new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent arg1) {
	    switch (getResultCode())
	    {
	    case Activity.RESULT_OK:
	    Toast.makeText(getBaseContext(), "SMS sent",
	    Toast.LENGTH_SHORT).show();
	    break;
	    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	    Toast.makeText(getBaseContext(), "Generic failure",
	    Toast.LENGTH_SHORT).show();
	    break;
	    case SmsManager.RESULT_ERROR_NO_SERVICE:
	    Toast.makeText(getBaseContext(), "No service",
	    Toast.LENGTH_SHORT).show();
	    break;
	    case SmsManager.RESULT_ERROR_NULL_PDU:
	    Toast.makeText(getBaseContext(), "Null PDU",
	    Toast.LENGTH_SHORT).show();
	    break;
	    case SmsManager.RESULT_ERROR_RADIO_OFF:
	    Toast.makeText(getBaseContext(), "Radio off",
	    Toast.LENGTH_SHORT).show();
	    break;
	    }
	    }
	    }, new IntentFilter(SENT));
	    //---when the SMS has been delivered---
	    registerReceiver(new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent arg1) {
	    switch (getResultCode())
	    {
	    case Activity.RESULT_OK:
	    Toast.makeText(getBaseContext(), "SMS delivered",
	    Toast.LENGTH_SHORT).show();
	    break;
	    case Activity.RESULT_CANCELED:
	    Toast.makeText(getBaseContext(), "SMS not delivered",
	    Toast.LENGTH_SHORT).show();
	    break;
	    }
	    }
	    }, new IntentFilter(DELIVERED));
	    SmsManager sms = SmsManager.getDefault();
	   /*LocationManager locationManager;
 	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
 	   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
         Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
         updateWithNewLocation(location);*/
	    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	    }
	    
	    private final LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	        	lat = location.getLatitude();
    			lng = location.getLongitude();
    			
	          updateWithNewLocation(location);
	        }
	         
	        public void onProviderDisabled(String provider){
	          updateWithNewLocation(null);
	        }

	        public void onProviderEnabled(String provider){}

	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	      };
	    private void updateWithNewLocation(Location location) {
	        
	    	LocationManager locationManager;
		    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

	            // getting GPS status
	            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	            // getting network status
	            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	            if (isNetworkEnabled) {
	            	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	            	String latLongString;
	            	Geocoder geocoder = new Geocoder(Send.this, Locale.getDefault());
	            	
	            	if (locationManager != null) {
	            		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	            		List<Address> addresses = null;
	            		
	            		if (location != null) {
	            			try{
	            			lat = location.getLatitude();
	            			lng = location.getLongitude();
	            			//latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
	            			addresses = geocoder.getFromLocation(lat, lng, 1);
	            			SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss", Locale.US);
	            			newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
	            			}
	            			catch (IOException e1) {
	                            Log.e("LocationSampleActivity",
	                                    "IO Exception in getFromLocation()");
	                            e1.printStackTrace();
	                            
	                            } catch (IllegalArgumentException e2) {
	                            // Error message to post in the log
	                            String errorString = "Illegal arguments " +
	                                    Double.toString(location.getLatitude()) +
	                                    " , " +
	                                    Double.toString(location.getLongitude()) +
	                                    " passed to address service";
	                            Log.e("LocationSampleActivity", errorString);
	                            e2.printStackTrace();
	                            }
	            			catch (NullPointerException e3) {
	            				e3.printStackTrace();
	            				locations="Request Remote to Turn on the Internet or Wait For Location or Send Command Again";
	            			}
	                    	
	                    	if (addresses != null && addresses.size() > 0) {
	                            // Get the first address
	                            Address address = addresses.get(0);
	                            /*
	                             * Format the first line of address (if available),
	                             * city, and country name.
	                             */
	                            addressText = String.format(
	                                    "%s, %s, %s",
	                                    // If there's a street address, add it
	                                    address.getMaxAddressLineIndex() > 0 ?
	                                            address.getAddressLine(0) : "",
	                                    // Locality is usually a city
	                                    address.getLocality(),
	                                    // The country of the address
	                                    address.getCountryName());
	                            // Return the text
	                            
	                        }
	                    	else{
	                    		addressText="No Address was found. Request Remote to turn on Internet";
	                    	}
	                    //	http://maps.google.com/map/?q=loc:;10.7680206;78.684641;Apr 08.2014;2.50.56 PM;Sundar nagar,Trichy.
	                        	//latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
	                    	latLongString="http://maps.google.com/maps/?q=loc:;"+lat+";"+lng+";"+newtime+";"+addressText; 
	            		} /*else {
	            			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*60*1,10, locationListener );
	                        Log.d("GPS Enabled", "GPS Enabled");
	                        
	                        if (locationManager != null) {location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                        
	                        if (location != null) {
	                        	
	                            	lat = location.getLatitude();
	                            	lng = location.getLongitude();
	                            	
		                        	latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
		                    	//latLongString="http://maps.google.com/maps/?q=loc:"+lat+","+lng+addressText; 
	                        	
	                            	//latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
	                        	//latLongString="http://maps.google.com/maps/?q=loc:"+lat+","+lng+addressText;    
	                        }*/
	                            else {
	                  	          latLongString = "No location found. Request Remote to turn on the internet"+";"+newtime; 
	                  	        }
	                        
	                            locations=latLongString;
	                        }
	                    
	                 
	            		}
	        
	      //  myLocationText.setText("My Current Position is:\n" + latLongString);  
	            		
	            		
	        
	            
	        
	            else if (isGPSEnabled) {
	        	
	            	Toast.makeText(getBaseContext(), "hitting at gps enabled", Toast.LENGTH_SHORT).show(); 
	            	String latLongString;
               
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener );
                    Log.d("GPS Enabled", "GPS Enabled");
                    Geocoder geocoder = new Geocoder(Send.this, Locale.getDefault());
                    if (locationManager != null) {location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    List<Address> addresses = null;
                    if (location != null) {
                    	
                    	try{
	            			lat = location.getLatitude();
	            			lng = location.getLongitude();
	            			//latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
	            			addresses = geocoder.getFromLocation(lat, lng, 1);
	            			SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");
	            			newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
	            			}
	            			catch (IOException e1) {
	                            Log.e("LocationSampleActivity",
	                                    "IO Exception in getFromLocation()");
	                            e1.printStackTrace();
	                            
	                            } catch (IllegalArgumentException e2) {
	                            // Error message to post in the log
	                            String errorString = "Illegal arguments " +
	                                    Double.toString(location.getLatitude()) +
	                                    " , " +
	                                    Double.toString(location.getLongitude()) +
	                                    " passed to address service";
	                            Log.e("LocationSampleActivity", errorString);
	                            e2.printStackTrace();
	                            }
	            			catch (NullPointerException e3) {
	            				e3.printStackTrace();
	            				locations="Request Remote to Turn on the Internet or Wait For Location or Send Command Again";
	            			}
	                    	
	                    	if (addresses != null && addresses.size() > 0) {
	                            // Get the first address
	                            Address address = addresses.get(0);
	                            /*
	                             * Format the first line of address (if available),
	                             * city, and country name.
	                             */
	                            addressText = String.format(
	                                    "%s, %s, %s",
	                                    // If there's a street address, add it
	                                    address.getMaxAddressLineIndex() > 0 ?
	                                            address.getAddressLine(0) : "",
	                                    // Locality is usually a city
	                                    address.getLocality(),
	                                    // The country of the address
	                                    address.getCountryName());
	                            // Return the text
	                            
	                        }
	                    	else{
	                    		addressText="No Address Found. Pls Request Remote to turn on Internet";
	                    	}
	                        	//latLongString = "http://maps.google.com/maps/?q=loc:"+lat+","+lng;
	                    	latLongString="http://maps.google.com/maps/?q=loc:;"+lat+";"+lng+";"+newtime+";"+addressText; 
	            		} 
                        else {
                        	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");
	            			newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
              	          latLongString = "No location found"+newtime; 
              	        }
                    
                        locations=latLongString;
                    }
                
               
	        }
	            else{
	            	String latLongString;
	            	latLongString = "No location found"+";"+newtime;
	            	locations=latLongString;
	            }
	        
	}
	    
	     public void onReceive1(Context context, Intent intent) {                
	        Intent i = new Intent(context, IncomingSms.class); 
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startService(i);       
	        
	    }
}
