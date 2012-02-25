package com.teamuphome.lapor;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UploadThread extends Thread
{
	Intent intent;
	Context context;
	
	public UploadThread(Intent intent, Context context)
	{
		this.intent  = intent;
		this.context = context;
	}
	
	public void run() {
        System.out.println("Hello from a thread!");
        if(intent!=null)
        {	
        	try
        	{
        		//PictureUtil.uploadPicture(intent);
        		Toast.makeText(context, "pic uploaded", Toast.LENGTH_SHORT);
        	}catch(Exception e)
        	{
        		Toast.makeText(context, "pic upload fail", Toast.LENGTH_SHORT);
        	}
        }else
        {
        	Toast.makeText(context, "pic upload fail", Toast.LENGTH_SHORT);
        }
    }
}
