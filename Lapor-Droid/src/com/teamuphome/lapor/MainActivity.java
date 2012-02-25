/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamuphome.lapor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.sanselan.common.ImageMetadata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener
{
   // private boolean mAlternateTitle = false;
    private LocationManager locationManager;
    private String provider;
    private Location lastLocation;
    private static final int CAMERA_PIC_REQUEST = 1337;
    
    //String [] imgURLArray;
    ImageMetaData []imgURLArray;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {  
	    if (requestCode == CAMERA_PIC_REQUEST) {  
	        //Toast.makeText(MainActivity.this, "activity come back", Toast.LENGTH_SHORT).show();
	        try{
	        	//UploadThread uploadThread = new UploadThread(data,this.getApplicationContext());
	        	//uploadThread.start();
	        	Location newLocation = getCurrentLocation();
	        	
		        if(newLocation!=null)
	        	{
	        		lastLocation = newLocation;
	        	}
		        
	        }catch(Exception e)
	        {
	        	Toast.makeText(MainActivity.this, "NOT UPLOADED, reason"+e.getMessage(), Toast.LENGTH_SHORT).show();
	        	e.printStackTrace();
	        }
	        
	        if(resultCode== RESULT_OK)
	        {		
	        	Intent inputIntent = new Intent(this.getApplicationContext(), InputActivity.class);
	        	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
	        	inputIntent.putExtra("data", thumbnail);
	        	startActivity(inputIntent);
	        }
		}  
	}
    
    //public void loadImages(GridView gridView, String url)
    //{
    //	Bitmap bitmap = PictureUtil.downloadFile(url);
    //	ImageView imageView = new ImageView();
    //	imageView.set
    //}
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        setContentView(R.layout.main);
        
        GridView gridview = (GridView) findViewById(R.id.gridView);
        
        List<ImageMetaData> imgURLArray = PictureUtil.getAllImages();
        
        if(imgURLArray!=null && imgURLArray.size()>0)
        {
        	//List<ImageMetaData> meta = new ArrayList<ImageMetaData>();
        	//for(int i =0; i < imgURLArray.length;i++)
        	//{
        	//	meta.add(imgURLArray[i]);
        	//}
        	
        	
        	gridview.setAdapter(new ImageAdapter(this,imgURLArray));
        }else
        {
        	Toast.makeText(MainActivity.this, "img URL Array is null", Toast.LENGTH_SHORT);
        }
        
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
            	Intent detailIntent = new Intent(MainActivity.this.getApplicationContext(), DetailActivity.class);
    	        detailIntent.putExtra("position", position);
            	startActivity(detailIntent);
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    private void clickCamera()
    {
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
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
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                //Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                        }, 1000);
                break;

            case R.id.menu_camera:
               // Toast.makeText(this, "Tapped camera", Toast.LENGTH_SHORT).show();
                
                lastLocation = getCurrentLocation();
                
                if(lastLocation!=null)
                {	
                	clickCamera();
                }
                
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	    private String url;
	    private final WeakReference<ImageView> imageViewReference;

	    public BitmapDownloaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    @Override
	    // Actual download method, run in the task thread
	    protected Bitmap doInBackground(String... params) 
	    {
	         // params comes from the execute() call: params[0] is the url.
	    	 
	    	if(params!=null && params[0]!=null &&params[0].trim()!="")
	    	{
	    		return PictureUtil.downloadFile(params[0]);
	    	}else
	    	{
	    		return null; 
	    	}
	    }

	    @Override
	    // Once the image is downloaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) 
	        {
	            bitmap = null;
	        }

	        if (imageViewReference != null) {
	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) 
	            {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	
}
