package com.ylw.url2epub.net;

public class Ele {
	private String id;
	private String title;
	private String mediaType;

	public Ele() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ele(String id, String title, String mediaType) {
		super();
		this.id = id;
		this.title = title;
		this.mediaType = mediaType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

}
