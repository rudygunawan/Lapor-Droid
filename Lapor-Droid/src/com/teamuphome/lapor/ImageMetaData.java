package com.teamuphome.lapor;


public class ImageMetaData {

	

	private String caption;

	private String blobKey;

	private String longitude;

	public String getLongitude() {

		return longitude;

	}

	public void setLongitude(String longitude) {

		this.longitude = longitude;

	}

	public String getLatitude() {

		return latitude;

	}

	public void setLatitude(String latitude) {

		this.latitude = latitude;

	}



	private String latitude;

	

	public String getBlobKey() {

		return blobKey;

	}

	public void setBlobKey(String blobKey) {

		this.blobKey = blobKey;

	}

	public String getCaption() {

		return caption;

	}

	public void setCaption(String caption) {

		this.caption = caption;

	}

	public String getComment() {

		return comment;

	}

	public void setComment(String comment) {

		this.comment = comment;

	}

	public String getDescription() {

		return description;

	}

	public void setDescription(String description) {

		this.description = description;

	}

	

	private String comment;

	private String description;

	private String thumbUrl;

	public String getThumbUrl() {

		return thumbUrl;

	}

	public void setThumbUrl(String thumbUrl) {

		this.thumbUrl = thumbUrl;

	}

	public String getPreviewUrl() {

		return previewUrl;

	}

	public void setPreviewUrl(String previewUrl) {

		this.previewUrl = previewUrl;

	}



	private String previewUrl;



}

