package com.teamuphome.lapor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity
{
	String latitude;
	String longitude;
	String thumbUrl;
	String url;
	String caption;
	String description;
	
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.detail);
	    
	    int position = this.getIntent().getExtras().getInt("position");
	    
	    ImageMetaData meta = PowerUpCacheManager.getMetadataCache().get(Integer.valueOf(position));
	    
	    latitude = meta.getLatitude();
	    longitude = meta.getLongitude();
	    
	   // Toast.makeText(this, "position lat"+latitude+":long"+longitude, Toast.LENGTH_LONG);
	    
	    //this.getIntent().getExtras().get("thumbURL");
	    //Button backBtn = (Button) findViewById(R.id.backBtn);
	    TextView titleView = (TextView) findViewById(R.id.title);
	    
	    TextView descView = (TextView) findViewById(R.id.desc);
	    WebView webView = (WebView) findViewById(R.id.webView);
	    
	    caption = meta.getCaption();
	    description = meta.getDescription();//+",with position "+latitude+":"+longitude;
	    url = meta.getPreviewUrl();
	    
	    if(caption!=null && caption!="null")
	    {
	    	titleView.setText(caption);
	    }else
	    {
	    	titleView.setText("No Caption");
	    }
	    
	    if(description!=null && description!="null")
	    {
	    	descView.setText(description);
	    }else
	    {
	    	descView.setText("N/A");
	    }
	    
	    if(longitude!="0"&&longitude!="0.0" && longitude!=null && longitude!="")
	    {
	    	GoogleWebView wv = new GoogleWebView(webView);
	    	wv.renderWebView(meta);
	    	webView.setVisibility(View.VISIBLE);
	    }else
	    {
	    	webView.setVisibility(View.GONE);
	    }
	    
	    ImageView imgView = (ImageView) findViewById(R.id.imageView1);
	   
	    if(description!=null)
	    
	    if(url!=null)
	    {	
	    	Bitmap bitmap = PictureUtil.downloadFile(url);
	    	imgView.setImageBitmap(bitmap);
	    	imgView.setVisibility(View.VISIBLE);
	    }else
	    {
	    	imgView.setVisibility(View.GONE);
	    }
	    
        /*
	    backBtn.setOnClickListener(new View.OnClickListener() 
	    {
			public void onClick(View v) 
			{
				Intent newIntent = new Intent();
				setResult(RESULT_OK, newIntent);
				finish();
			}
	    });	
	    */
	}       
	
	
}
