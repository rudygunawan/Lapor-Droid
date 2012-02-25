package com.teamuphome.lapor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputActivity extends Activity
{
	Bitmap currBitmap;
	private LocationManager locationManager;
	private String provider;
	private Location lastLocation;
	   
	
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.input);
       currBitmap = (Bitmap)this.getIntent().getExtras().get("data"); 
       Button submit = (Button) findViewById(R.id.submit);
       Button cancel = (Button) findViewById(R.id.cancel);
       
       submit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				EditText title = (EditText) findViewById(R.id.title);
				EditText desc  = (EditText) findViewById(R.id.description);
				
				//Toast.makeText(InputActivity.this, "Tapped submit "+title.getText()+":"+desc.getText(), Toast.LENGTH_SHORT).show();
				   
				try{
					lastLocation = getCurrentLocation();
					
					PictureUtil.uploadPicture(currBitmap,title.getText().toString(),desc.getText().toString(),lastLocation.getLongitude(),lastLocation.getLatitude());
					//PictureUtil.uploadPicture(requestCode, resultCode, data);
					//Toast.makeText(InputActivity.this, "pic uploaded"+lastLocation.getLongitude() + ":"+lastLocation.getLatitude(), Toast.LENGTH_LONG).show();
		        }catch(Exception e)
				{
					Toast.makeText(InputActivity.this, "ERROR:pic NOT uploaded", Toast.LENGTH_SHORT).show();
			        
				}
				
			    Intent newIntent = new Intent();
			    setResult(RESULT_OK, newIntent);
			    finish();
			}
       });
       
       cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				//Toast.makeText(InputActivity.this, "Tapped submit canceled", Toast.LENGTH_SHORT).show();
				Intent newIntent = new Intent();
			    setResult(RESULT_OK, newIntent);
			    finish();
			
			}
      });
       
       
	}
	private Location getCurrentLocation()
    {
    	//Location;
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		
		Location location = locationManager.getLastKnownLocation(provider);
		if(location!=null)
		{
			//Toast.makeText(this, "Location, lat :"+location.getLatitude() + " long:"+location.getLongitude(), Toast.LENGTH_LONG).show();
	        //clickCamera();
		}else
		{
			Toast.makeText(this, "Location cant be determined", Toast.LENGTH_LONG).show();
	    }
		return location;
    
    }
	
}
