package com.ylw.url2epub.model.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "link_history")
public class LinkHistory {
	@DatabaseField(id = true, unique = false, columnName = "url")
	String url;
	@DatabaseField(columnName = "link")
	String link;
	@DatabaseField(columnName = "save_date")
	String saveDate;

	public LinkHistory() {
		super();
	}

	public LinkHistory(String link, String url) {
		this.url = url;
		this.link = link;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		saveDate = format.format(new Date());
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}

	public String getSaveDate() {
		return this.saveDate;
	}
}