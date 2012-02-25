
package com.teamuphome.lapor;



import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URI;

import java.net.URL;

import java.util.ArrayList;

import java.util.List;



import org.apache.http.HttpResponse;

import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.HttpMultipartMode;

import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.entity.mime.content.ByteArrayBody;

import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;

import org.apache.http.protocol.HTTP;

import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

import org.json.JSONArray;

import org.json.JSONObject;



import android.content.res.Resources;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.util.Log;



public class PictureUtil {

	

	//private static String SERVER_URL = "10.0.1.7:8888";//"photodb-ae.appspot.com";

	

	private static String SERVER_URL = "photodb-ae.appspot.com";

	

	private static String UPLOAD_GET_URL = "http://"+SERVER_URL+"/upload";

	private static String UPDATE_URL = "http://"+SERVER_URL+"/update";

	private static String GET_ALL_IMAGES_URL = "http://"+SERVER_URL+"/rest/images";

	

	

	public static Bitmap downloadFile(String fileUrl){

        URL myFileUrl =null;          

        try {

        	

//        	if(thumbnail) {

//        		fileUrl = fileUrl +"/resized?width=100&height=100";

//        	}

        	

             myFileUrl= new URL(fileUrl);

        } catch (MalformedURLException e) {

             // TODO Auto-generated catch block

             e.printStackTrace();

        }

        

        HttpURLConnection conn = null;

        

        try {

        	 conn = (HttpURLConnection)myFileUrl.openConnection();

             conn.setDoInput(true);

             conn.connect();

             InputStream is = conn.getInputStream();

             

             Bitmap bmImg = BitmapFactory.decodeStream(is);

             return bmImg;

        } catch (IOException e) {

             // TODO Auto-generated catch block

             e.printStackTrace();

        }finally{

        	

        	if(conn!=null) {

        		conn.disconnect();

        	}        	

        	

        }

        return null;

   }

	

	public static List<ImageMetaData> getAllImages() {

		

		Log.v("getAllImagesDownloadURL", "getAllImages start");

		

		List metadata =new ArrayList();

		

		try {

		

		String str = executeGet(GET_ALL_IMAGES_URL);

		

		JSONArray array = new JSONArray(str);

		

		//image = new ImageMetaData[array.length()];

		

		for(int i=0;i<array.length();i++) {

			//imageViewList[i] = new ImageView(view.getContext());

			

			JSONObject obj = (JSONObject)array.getJSONObject(i);

			

			String blobKey = obj.getString("blobKey");

			

			String downloadURL = GET_ALL_IMAGES_URL+"/"+blobKey;

			

			ImageMetaData image = new ImageMetaData();

			image.setBlobKey(blobKey);

			image.setCaption(obj.getString("description"));

			image.setThumbUrl(obj.getString("thumbUrl"));

			image.setDescription(obj.getString("comment"));

			image.setPreviewUrl(obj.getString("previewUrl"));

			image.setLatitude(obj.getString("gpsLat"));

			image.setLongitude(obj.getString("gpsLong"));

			

			

			metadata.add(image);

			

			Log.v("getAllImages","downloadURL = "+downloadURL);

			

			

		}

		

		Log.v("getAllImagesDownloadURL", "no of images "+array.length());

		

		}catch(Exception e) {

			Log.e("getAllImagesDownloadURL",e.getMessage());

		}

		

		return metadata;

	}

	

	public static String executeGet(String url) throws Exception {

		

		BufferedReader in = null;

		StringBuffer sb = new StringBuffer("");

		

		HttpClient httpclient = new DefaultHttpClient();

		HttpGet request = new HttpGet();

        request.setURI(new URI(url));  		

		

		HttpResponse response = httpclient.execute(request);



				

		

		try {

		

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	        String line = "";

	        String NL = System.getProperty("line.separator");

	        while ((line = in.readLine()) != null) {

	            sb.append(line);

	        }

	        

			

		}catch(Exception e) {

			Log.v("Exception",e.getMessage());

		}finally {

			

			if(in !=null) {

				in.close();

			}

			

					

			httpclient.getConnectionManager().shutdown();

			

		}

        

        

        return sb.toString();



	}

	

	

	public static void uploadImage(Bitmap bm,byte ba[],String caption,String description,double longitude,double latitude) throws Exception {

		

		InputStream is=null;

		ByteArrayOutputStream bao =null;

		HttpClient httpclient = new DefaultHttpClient();

		

		try {

			

			

		bao = new ByteArrayOutputStream();



		bm.compress(Bitmap.CompressFormat.JPEG, 75, bao);



		if(ba ==null) {

			ba = bao.toByteArray();

		}

		

		//byte [] ba = bao.toByteArray();



//		String ba1=Base64.encodeToString(ba,Base64.DEFAULT);

//

//		ArrayList<NameValuePair> nameValuePairs = new

//

//		ArrayList<NameValuePair>();

//

//		nameValuePairs.add(new BasicNameValuePair("image",ba1));



		

		String url = getUploadURL();	

		Log.v("executeMultipartPost", "UPLOAD URL"+url);

		

		

		url = url.replace("hmoniaga1:8888",SERVER_URL);

		

		Log.v("executeMultipartPost", "URL NOW"+url);

		

		

		ByteArrayBody bab = new ByteArrayBody(ba,caption+".jpg");



		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		reqEntity.addPart("image", bab);

		 		         

				

		HttpPost httppost = new HttpPost(url);



		httppost.setEntity(reqEntity);



		HttpResponse response = httpclient.execute(httppost);

		

		Log.v("Log.v(TA","Execute is done");

		

		

		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = "";

        StringBuffer sb = new StringBuffer();

        while ((line = in.readLine()) != null) {

            sb.append(line);

        }

        

        Log.v("uploadImages","upload images is done "+sb.toString());

			

        JSONObject array = new JSONObject(sb.toString());

		String blobKey = array.getString("blobKey");

		

		Log.v("uploadImages","upload images is done");

 			

		if(blobKey !=null) {

			Log.v("uploadImages","blobKey returned = "+blobKey);

			

			String updateURL = UPDATE_URL;

			

			httppost = new HttpPost(UPDATE_URL);

			

//			reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

//			reqEntity.addPart("blobKey", new StringBody(blobKey));

//			reqEntity.addPart("description", new StringBody(caption));

//			reqEntity.addPart("comment", new StringBody(description));		

//			reqEntity.addPart("gpsLong", new StringBody(new Double(longitude == 0 ? 0.0 : longitude).toString()));		

//			reqEntity.addPart("gpsLang", new StringBody(new Double(latitude == 0 ? 0.0 : latitude).toString()));

			

			List <NameValuePair> nvps = new ArrayList <NameValuePair>();

			nvps.add(new BasicNameValuePair("blobKey", ""+blobKey));

			nvps.add(new BasicNameValuePair("description", ""+caption));

			nvps.add(new BasicNameValuePair("comment", ""+description));

			

			nvps.add(new BasicNameValuePair("gpsLong", ""+new Double(longitude == 0 ? 0.0 : longitude).toString()));

			nvps.add(new BasicNameValuePair("gpsLat", ""+new Double(latitude == 0 ? 0.0 : latitude).toString()));

			

			//httppost.setEntity(reqEntity);

			httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));



			response = httpclient.execute(httppost);

			

			Log.v("uploadImages","Update is done");

			

		}

		

		

			

		}catch(Exception e){



			Log.e("executeMultipartPost", e.getMessage());



		}finally {

			

			try {

			

				if(is!=null) {

					is.close();

				}

			

				if(bao!=null) {

					bao.close();

				}

				

				httpclient.getConnectionManager().shutdown();

			

			}catch(Exception e) {

				

			}

			

			

		}



		

	}

	

	public static boolean testUpload(Resources resource,int imageID) {

		

					

		 Bitmap bm = BitmapFactory.decodeResource(resource,

				 imageID);

		 TiffOutputSet outputSet = null;

		 

		 try {

			 

//			 String path = Environment.getExternalStorageDirectory().toString();

//			 

//			 File file = new File(path,"myFile.jpg");

//			 File dest = new File(path,"myFileUpdated.jpg");

//			 FileOutputStream bos = new FileOutputStream(file);

//			 bm.compress(CompressFormat.JPEG, 100, bos); //Bitmap object is your image

//			 //byte[] data = bos.toByteArray();

//			 

//			 IImageMetadata metadata = Sanselan.getMetadata(file);

//

//			 JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

//			 

//			 if (null != jpegMetadata)

//			 {

//				 TiffImageMetadata exif = jpegMetadata.getExif();

//

//				 		if (null != exif)

//					   				{

//					   					// TiffImageMetadata class is immutable (read-only).

//					   					// TiffOutputSet class represents the Exif data to write.

//					   					//

//					   					// Usually, we want to update existing Exif metadata by

//					   					// changing

//					   					// the values of a few fields, or adding a field.

//					   					// In these cases, it is easiest to use getOutputSet() to

//					   					// start with a "copy" of the fields read from the image.

//					  					outputSet = exif.getOutputSet();

//					  				}

//

//				 

//			 }

//			 

//			 if (null == outputSet)

//				 		outputSet = new TiffOutputSet();

//

//			 	TiffOutputField aperture = TiffOutputField.create(

//						TiffConstants.EXIF_TAG_APERTURE_VALUE,

//						outputSet.byteOrder, new Double(0.3));

//			 	

//			 	TiffOutputDirectory exifDirectory = outputSet

//						.getOrCreateExifDirectory();

//				// make sure to remove old value if present (this method will

//				// not fail if the tag does not exist).

//				exifDirectory

//					.removeField(TiffConstants.EXIF_TAG_APERTURE_VALUE);

//				exifDirectory.add(aperture);

//

//				

//		

//			 if (null != outputSet)

//			 {

//			     ExifRewriter ER = new ExifRewriter();

//			     Log.v("===>","Updting EXIF");

//			     ER.updateExifMetadataLossless(file, new FileOutputStream(dest), outputSet);

//			 }

//			 Log.v("===>","Before Update 44444---");

//			 IImageMetadata metadata2 = Sanselan.getMetadata(data);

//			 JpegImageMetadata jpegMetadata2 = (JpegImageMetadata) metadata;

//			 TiffImageMetadata exif2 = jpegMetadata2.getExif();

			// Log.v("===>","EXIF ATTR ="+exif2.findField(new TagInfo("Image Description")));

//

//			 

//			 

//			 

//			 String path = Environment.getExternalStorageDirectory().toString();

//			 OutputStream fOut = null;

//			 Log.v("===>",path);

//			 

//			 File file = null;

//			 

//			 try {

//				 file = new File(path, "rudy"+imageID+".jpg");

//				 fOut = new FileOutputStream(file);

//			 }catch(java.io.FileNotFoundException io) {

//				 file = new File(Environment.getDownloadCacheDirectory().toString(), "rudy"+imageID+".jpg");

//				 fOut = new FileOutputStream(file);

//			 }

//			 

//			 

//			 

//			

//

//			 

//						 

//			 FileOutputStream out = new FileOutputStream(file);

//			 bm.compress(Bitmap.CompressFormat.JPEG, 75, out);

//			 

//			 ExifInterface newexif = new ExifInterface(file.getAbsolutePath());

//			 newexif.setAttribute("Image Description","This is harry");

//			 newexif.setAttribute("UserComment","This is willy comment");

//			 newexif.saveAttributes();

//			 

//			 

//			 IImageMetadata metadata = Sanselan.getMetadata(file);

//			 JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

//			 if (jpegMetadata != null) {

//				 TiffImageMetadata m = jpegMetadata.getExif();

//				 TiffField t = m.findField(ExifTagConstants.EXIF_TAG_USER_COMMENT);

//			 

//				 m.add("Image Description","This is harry");

//				 m.add("UserComment","This is willy comment");

//				 

//				 ExifRewriter ER = new ExifRewriter();

//				 Log.v("===>","updating extif");

//				 ByteArrayOutputStream bao = new ByteArrayOutputStream();

//				 

//				 ER.updateExifMetadataLossless(file,bao,m.getOutputSet());

//			 }

//			 

			// bm = BitmapFactory.decodeFile(dest.getAbsolutePath());

			 Log.v("===>","Testt Upload start");

			 uploadImage(bm,null,"Willy Capiton","Willy Desc",1.1231,12313.223);

			 Log.v("===>","Testt Upload finished ");

			

			 

//			 ExifInterface lalaif = new ExifInterface(file.getAbsolutePath());

//			 Log.v("lalaif====>","mAMA==>"+lalaif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));

//			 Log.v("lalaif====>","mAMAsss==>"+lalaif.getAttribute("Image Description"));

			 

		 }catch(Exception e) {

			 Log.e("log_tag", "testUpload "+e.toString());

		 }

		 

//		 ImageView imageView = new ImageView(view.getContext());

//

//	     imageView.setImageResource(R.drawable.icon);

//	     imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));

//	     imageView.setScaleType(ImageView.ScaleType.FIT_XY);

	        

	        

	     return true;

	        

	}

	



	public static boolean uploadPicture(Bitmap image,String caption,String description,double longitude,double latitude)throws Exception {

		

//		if(data == null) {

//			Log.v("uploadPictures","data is NULL");

//		}

		

		//Bitmap image = (Bitmap) data.getExtras().get("data");

		if(image == null) {

			Log.v("uploadPictures","thumbnail is NULL");

		}

				

		

		uploadImage(image,null,caption,description,longitude,latitude);

		//thumbnail.get

//		InputStream is=null;

//		ByteArrayOutputStream bao =null;

//		

//		try{

//			

//			bao = new ByteArrayOutputStream();

//

//			thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, bao);

//

//			byte [] ba = bao.toByteArray();

//

//			String ba1=Base64.encodeToString(ba,Base64.DEFAULT);

//

//			ArrayList<NameValuePair> nameValuePairs = new

//

//			ArrayList<NameValuePair>();

//

//			nameValuePairs.add(new BasicNameValuePair("image",ba1));

//

//			HttpClient httpclient = new DefaultHttpClient();

//

//			HttpPost httppost = new HttpPost("http://172.31.51.18:8888/");

//

//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//

//			HttpResponse response = httpclient.execute(httppost);

//			

//			HttpEntity entity = response.getEntity();

//			

//			is = entity.getContent();

//

//			}catch(Exception e){

//

//			Log.e("log_tag", "Error in http connection "+e.toString());

//

//			}finally {

//				

//				try {

//				

//					if(is!=null) {

//						is.close();

//					}

//				

//					if(bao!=null) {

//						bao.close();

//					}

//				

//				}catch(Exception e) {

//					

//				}

//				

//				

//			}

//

//		

//		//ImageView image = (ImageView) findViewById(R.id.photoResultView);  

//		//image.setImageBitmap(thumbnail);

//		

		return true;

	}

	

	public static String getUploadURL() throws Exception{

		Log.v("Log.v(TA", "getUploadURL start");

		

		

		String str = executeGet(UPLOAD_GET_URL);

		

		JSONObject obj = new JSONObject(str);

		

		if(obj !=null) {

			return obj.getString("url");

		}else {

			return "";

		}

	        

	    

		

//		BufferedReader in = null;

//		StringBuffer sb = new StringBuffer("");

//		

//		HttpClient httpclient = new DefaultHttpClient();

//		HttpGet request = new HttpGet();

//        request.setURI(new URI(UPLOAD_GET_URL));  		

//		

//		HttpResponse response = httpclient.execute(request);



				

//		

//		try {

//		

//			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

//	        String line = "";

//	        String NL = System.getProperty("line.separator");

//	        while ((line = in.readLine()) != null) {

//	            sb.append(line);

//	            Log.v("Log.v(TA", "s tart 44 "+line);

//	    		

//	        }

//	       

//	        

//			

//		}catch(Exception e) {

//			Log.v("Exception",e.getMessage());

//		}finally {

//			

//			if(in !=null) {

//				in.close();

//			}

//			

//					

//			httpclient.getConnectionManager().shutdown();

//			

//		}

        

        

      //  return sb.toString();



		

	}

	

}

