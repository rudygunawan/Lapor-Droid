package com.teamuphome.lapor;

import java.net.URL;

import android.os.AsyncTask;

public class DownloadAsyncTask extends  AsyncTask<String, Integer, Long>
{

	@Override
	protected Long doInBackground(String... params) {
		// TODO Auto-generated method stub
		int count = params.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) 
        {
            //ImageView view = PictureUtil.downloadFile(params[i]);
        	
        	//totalSize += Downloader.downloadFile(urls[i]);
            //publishProgress((int) ((i / (float) count) * 100));
        }
        return totalSize;
	
	}

}
