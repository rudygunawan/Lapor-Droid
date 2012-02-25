package com.teamuphome.lapor;



import android.location.Location;

import android.webkit.WebView;

import android.webkit.WebViewClient;



public class GoogleWebView  {

	

	//private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";

	

	private static final String MAP_URL = "http://photodb-ae.appspot.com/map.jsp";

	

	

	//

	

	private WebView webView;

	

	public GoogleWebView(WebView view) {

		this.webView = view;

	}

	

	public void renderWebView(ImageMetaData location){

		

		final String centerURL = "javascript:initialize(" + 

		location.getLatitude() + "," + 

		location.getLongitude()+ ")";

	    

		//webView = (WebView) findViewById(R.id.webview);

		//view

		webView.getSettings().setJavaScriptEnabled(true);

	    //Wait for the page to load then send the location information

	    webView.setWebViewClient(new WebViewClient(){  

	      @Override  

	      public void onPageFinished(WebView view, String url)  

	      {	

	    	  webView.loadUrl(centerURL);

	      }



	    });

	   

	    

	    webView.loadUrl(MAP_URL);  









	  }

	

}

