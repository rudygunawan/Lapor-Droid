package com.teamuphome.lapor;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //List<String> urlList;
    List<ImageMetaData> imageMetadataList;
    
    Map<String,ImageView> views;
    
    
    public ImageAdapter(Context c, List<ImageMetaData> urlList) {
        mContext = c;
        this.imageMetadataList = urlList;
    }

    public int getCount() 
    {
        if(imageMetadataList!=null && imageMetadataList.size()>0)
        {
        	return imageMetadataList.size();
        }else
        {
        	return 0;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(1, 1, 1, 1);
            
            
            //imageView.setM
            //imageView.setLayoutParams(params)
            //LayoutParam layoutParam = new LayoutParam();
            
        } else 
        {
            imageView = (ImageView) convertView;
        }
        
        Map<String,Bitmap> cache = PowerUpCacheManager.getBitMapCache();
        //String currentURL = imageMetadataList.get(position);
        ImageMetaData imageMetadata = imageMetadataList.get(position);
        String currentURL = imageMetadata.getThumbUrl();
        
        if(cache.containsKey(imageMetadata.getThumbUrl()))
        {
        	imageView.setImageBitmap((Bitmap)cache.get(currentURL));
        }else
        {
        	ImageLoader imageLoader = new ImageLoader(imageView,position);
        	imageLoader.execute(currentURL);
        	
        	//Bitmap bitmap = PictureUtil.downloadFile(currentURL);
        	//imageView.setImageBitmap(bitmap);
        	//cache.put(currentURL,bitmap);
        }
        
    	PowerUpCacheManager.getMetadataCache().put(Integer.valueOf(position), imageMetadata);
        
        return imageView;
    }
    
    private class ImageLoader extends AsyncTask<String, Void, Bitmap> 
    {
    	//private String url;
    	private final WeakReference<ImageView> imageViewReference;
    	
    	public ImageLoader(ImageView imageView,int position)
    	{
    		 imageViewReference = new WeakReference<ImageView>(imageView);
    	}
    	
    	//views = new HashMap<Integer, ImageView>(
    	  @Override
    	  protected Bitmap doInBackground(String... param) {

    	   // get the file that was passed from the bundle..
    	   String fileURL = param[0];
    	   	
    	   Bitmap bitmap = PictureUtil.downloadFile(fileURL);
    	   Map<String,Bitmap> cache = PowerUpCacheManager.getBitMapCache();
    	   cache.put(fileURL, bitmap); 
    	   return bitmap;
    	  }

    	  @Override
    	  protected void onPostExecute(Bitmap result) 
    	  {
    		  if (imageViewReference != null) 
    		  {
    			      ImageView imageView = imageViewReference.get();
    			      if (imageView != null) 
    			      {
    			         imageView.setImageBitmap(result);
    			      }
    		   }
    	  }

    }


    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3
    };
}