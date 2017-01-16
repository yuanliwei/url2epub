package com.ylw.url2epub.model;

public class UrlContent {
	String id;
	String href;
	String mediaType;

	public UrlContent() {
		super();
	}

	public UrlContent(String id, String href, String mediaType) {
		super();
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

}
