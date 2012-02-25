package com.teamuphome.lapor;

import java.util.HashMap;
import java.util.Map;

import org.apache.sanselan.common.ImageMetadata;

import android.graphics.Bitmap;

public class PowerUpCacheManager {

	private static Map <String, Bitmap> bitMapCache = new HashMap<String,Bitmap>();

	public static Map<String, Bitmap> getBitMapCache() 
	{
		return bitMapCache;
	}

	public static void setBitMapCache(Map<String, Bitmap> bitMapCache) 
	{
		PowerUpCacheManager.bitMapCache = bitMapCache;
	}
	
	
	private static Map<Integer, ImageMetaData> metadataCache = new HashMap<Integer,ImageMetaData>();

	public static Map<Integer, ImageMetaData> getMetadataCache() {
		return metadataCache;
	}

	public static void setMetadataCache(Map<Integer, ImageMetaData> metadataCache) {
		PowerUpCacheManager.metadataCache = metadataCache;
	}
	
}
