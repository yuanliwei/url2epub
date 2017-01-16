package com.ylw.url2epub.net;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.model.db.LinkHistory;
import com.ylw.url2epub.utils.FileUtil;
import com.ylw.url2epub.utils.digest.MD5;

public class Get {
	private static Log log = LogFactory.getLog(Get.class);
	private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
	private RejectedExecutionHandler rejQune = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			log.error("rej task");
			r.run();
		}
	};
	ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 30, 30, TimeUnit.SECONDS, workQueue, rejQune);

	// ExecutorService e = Executors.newFixedThreadPool(1);

	Map<String, Ele> map = new HashMap<>();
	List<Ele> list = new ArrayList<>();

	String rootDir;
	private CallBack callback;

	public Get(String rootDir) {
		super();
		this.rootDir = rootDir;
	}

	public void getContent(String id, String url, int deep, AtomicInteger atomicInteger) {
		try {
			new URL(url);
		} catch (Exception e) {
			log.warn(url + " not valid url");
			return;
		}
		atomicInteger.incrementAndGet();
		if (map.get(url) != null) {
			int count = atomicInteger.decrementAndGet();
			log.debug("deep:" + deep + " - 1 task count : " + count);
			if (count == 0) {
				callback.onResult(list);
			}
			return;
		}
		Ele ele = new Ele(id, "notitle1", "unknowtype");
		map.put(url, ele);
		get(url, (int code, InputStream in, String type) -> {
			Ele ele1 = new Ele(id, "notitle2", type);
			map.put(url, ele1);
			if (code == 0) {
				list.add(ele1);
				switch (type) {
				case "image/gif":
				case "image/jpeg":
				case "image/png":
				case "text/css":
					saveInputStreamToFile(getPath(ele1), in);
					break;
				case "text/html":
					ele1.setMediaType("application/xhtml+xml");
					String body = getStringFromInputStream(in);
					if (TextUtils.isBlank(body)) {
						break;
					}
					int s1 = url.indexOf("://");
					int s2 = url.indexOf("/", s1 + 3);
					String prefix = url;
					if (s2 > 0) {
						prefix = url.substring(0, s2);
					}
					parseLinks(ele1, prefix, body, deep - 1, atomicInteger);
					break;
				default:
					log.debug("unknow type");
					break;
				}
			}
			int count = atomicInteger.decrementAndGet();
			log.debug("deep:" + deep + " - 2 task count : " + count + " " + executor.getActiveCount());
			if (count == 0/* || executor.getActiveCount() == 0 */) {
				callback.onResult(list);
			}
		});
	}

	private void parseLinks(Ele ele, String prefix, String body, int deep, AtomicInteger atomicInteger) {
		try {
			Parser parser = Parser.createParser(body, "utf-8");
			List<LinkHistory> histories = new ArrayList<>();
			NodeVisitor visitor = new NodeVisitor() {
				@Override
				public void visitTag(Tag tag) {
					String tagName = tag.getTagName();
					String link;
					String id;
					String url;
					String shortLink;
					switch (tagName) {
					case "TITLE":
						TitleTag titleTag = (TitleTag) tag;
						ele.setTitle(titleTag.getTitle());
						break;
					case "LINK":
						link = tag.getAttribute("href");
						if (TextUtils.isBlank(link))
							break;
						id = MD5.md5(link) + ".css";
						tag.setAttribute("href", id);
						url = link;
						if (link.startsWith("/")) {
							url = prefix + link;
						}
						histories.add(new LinkHistory(link, url));
						// TODO post to thread pool
						getContent(id, url, deep, atomicInteger);
						break;
					case "IMG":
						link = tag.getAttribute("src");
						if (TextUtils.isBlank(link))
							break;

						shortLink = link;
						if (link.indexOf("?") > 0)
							shortLink = link.substring(0, link.indexOf("?"));
						try {
							URL url2 = new URL(shortLink);
							shortLink = url2.getPath();
						} catch (MalformedURLException e) {
						}
						if (shortLink.endsWith("/")) {
							shortLink = shortLink.replaceAll("/*$", "");
						}
						if (shortLink.indexOf("/") > 0) {
							shortLink = shortLink.substring(shortLink.lastIndexOf("/"));
						}
						if (shortLink.indexOf(".") > 0 && !shortLink.endsWith(".")) {
							shortLink = shortLink.substring(shortLink.lastIndexOf("."));
						} else {
							shortLink = ".png";
						}
						id = MD5.md5(link) + shortLink;
						tag.setAttribute("src", id);
						url = link;
						if (link.startsWith("/")) {
							url = prefix + link;
						}
						histories.add(new LinkHistory(link, url));
						// TODO post to thread pool
						getContent(id, url, deep, atomicInteger);
						// System.out.println(tagName);
						break;
					case "A":
						if (deep < 0)
							break;
						LinkTag linkTag = (LinkTag) tag;
						link = linkTag.getLink();
						if (TextUtils.isBlank(link))
							break;
						if (link.contains(" "))
							break;
						id = MD5.md5(link) + ".html";
						linkTag.setLink(id);
						url = link;
						if (link.startsWith("/")) {
							url = prefix + link;
						}
						histories.add(new LinkHistory(link, url));
						shortLink = link;
						if (link.indexOf("?") > 0)
							shortLink = link.substring(0, link.indexOf("?"));
						try {
							URL url2 = new URL(shortLink);
							shortLink = url2.getPath();
						} catch (MalformedURLException e) {
						}
						if (shortLink.endsWith("/")) {
							shortLink = shortLink.replaceAll("/*$", "");
						}
						if (shortLink.indexOf("/") > 0)
							shortLink = shortLink.substring(shortLink.lastIndexOf("/"), shortLink.length());
						if (shortLink.contains(".") && !shortLink.endsWith(".")) {
							String suffix = shortLink.split("\\.")[1];
							switch (suffix) {
							case "htm":
							case "html":
								// TODO post to thread pool
								getContent(id, url, deep, atomicInteger);
								break;

							default:
								log.info("ignore link : " + link);
								break;
							}
						} else {
							// TODO post to thread pool
							getContent(id, url, deep, atomicInteger);
						}
						// System.out.println(tagName);
						break;
					case "SCRIPT":
						// System.out.println(tagName);
						break;
					default:
						// System.out.println(tagName);
						break;
					}

					super.visitTag(tag);
				}
			};
			NodeList nodes = parser.parse(null);
			nodes.visitAllNodesWith(visitor);
			String result = nodes.toHtml();
			String savePath = getPath(ele);
			// OrmLiteUtils.saveOrUpdateAll(histories);
			FileUtil.saveFullPathFile(savePath, result);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	void parserLinks(String url, String body, List<UrlContent> contents, int deep, AtomicInteger atomicInteger,
			CallBack callback) {
		if (deep < 0)
			return;
		Pattern pattern = Pattern.compile("<a[^>]*?href=\"([^\"]*?)\"");
		matchBody(url, body, deep, atomicInteger, callback, pattern);
		pattern = Pattern.compile("<img[^>]*?src=\"([^\"]*?)\"");
		matchBody(url, body, deep, atomicInteger, callback, pattern);
		pattern = Pattern.compile("<link[^>]*?href=\"([^\"]*?)\"");
		matchBody(url, body, deep, atomicInteger, callback, pattern);

	}

	private void matchBody(String url, String body, int deep, AtomicInteger atomicInteger, CallBack callback,
			Pattern pattern) {
		Matcher matcher = pattern.matcher(body);
		int s1 = url.indexOf("://");
		int s2 = url.indexOf("/", s1 + 3);
		String prefix = url;
		if (s2 > 0) {
			prefix = url.substring(0, s2);
		}
		String prefix2 = prefix;
		while (matcher.find()) {
			String findUrl = matcher.group(1);
			// log.debug("deep:" + deep + " " + findUrl);
			if (findUrl.contains(".apk"))
				continue;
			if (findUrl.contains(".exe"))
				continue;
			if (findUrl.contains(".zip"))
				continue;
			if (findUrl.contains(".rar"))
				continue;
			if (findUrl.contains(".mp3"))
				continue;
			if (findUrl.contains(".mp4"))
				continue;
			if (findUrl.contains(".avi"))
				continue;
			if (findUrl.contains(".mov"))
				continue;
			executor.execute(new Runnable() {

				@Override
				public void run() {

					if (findUrl.startsWith("http")) {
						getContent(findUrl, findUrl, deep, atomicInteger);
					}
					if (findUrl.startsWith("/")) {
						String s = prefix2 + findUrl;
						// log.debug(s);
						getContent(findUrl, s, deep, atomicInteger);
					}
				}
			});
		}
	}

	private String getStringFromInputStream(InputStream in) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			String body = new String(outputStream.toByteArray());
			return body;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void saveInputStreamToFile(String url, InputStream in) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(url);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(os);
		}
	}

	void get(String url, GETCallBack callBack) {
		executor.execute(new Runnable() {
			public void run() {

				InputStream in = null;
				try {
					URL url2 = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
					connection.setRequestProperty("accept", "*/*");
					connection.setRequestProperty("connection", "Keep-Alive");
					connection.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
					connection.setConnectTimeout(20000);
					connection.connect();
					Map<String, List<String>> map = connection.getHeaderFields();
					JSONObject headers = new JSONObject();
					List<String> status = map.get(null);
					int code = Integer.parseInt(status.get(0).split(" ")[1]);
					if (code != 200) {
						log.warn(status.get(0) + " - " + url);
						callBack.onResult(-1, null, null);
						return;
					}
					for (String key : map.keySet()) {
						// log.debug(key + "--->" + map.get(key));
						headers.put(key, map.get(key));
					}
					String type = map.get("Content-Type").get(0).split(";")[0];
					in = connection.getInputStream();
					callBack.onResult(0, in, type);
				} catch (Exception e) {
					callBack.onResult(-1, null, null);
					log.error(url);
					log.error(e.getMessage(), e);
				} finally {
					IOUtils.close(in);
				}
			}
		});
	}

	String getId(String url) {
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}
		String name = url.substring(url.lastIndexOf("/") + 1);
		if (TextUtils.isBlank(name)) {
			return "html" + url.hashCode() + ".html";
		}

		return name;
	}

	private String getPath(Ele ele) {
		return rootDir + ele.getId();
	}

	public void setCallBack(CallBack callBack) {
		this.callback = callBack;
	}
}
