package com.ylw.url2epub.utils.ormliteutils;

import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.ylw.url2epub.utils.PropUtils;

public class OrmLiteConnection {
	private static org.apache.commons.logging.Log log = LogFactory
			.getLog(OrmLiteConnection.class);

	// private final static String DATABASE_URL =
	// "jdbc:sqlite:E:/java/gen_logs/testmaven.db";
	private final static String DATABASE_URL = PropUtils.get("db_path");

	private static ConnectionSource connectionSource;

	public static void main(String[] args) {
		connectionSource = getConnection(DATABASE_URL);
	}

	public static ConnectionSource getConnection() {
		return getConnection(DATABASE_URL);
	}

	private static ConnectionSource getConnection(String databaseUrl) {
		if (connectionSource == null) {
			try {
				connectionSource = new JdbcConnectionSource(databaseUrl);
			} catch (SQLException e) {
				log.error("create JdbcConnectionSource error ...", e);
			}
		}
		return connectionSource;
	}
}
