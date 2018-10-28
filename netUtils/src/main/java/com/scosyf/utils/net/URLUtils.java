package com.scosyf.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLUtils {
	private final static Logger log = LoggerFactory.getLogger(URLUtils.class);

	public final static String HTTPS = "https";
	public final static String HTTP = "http";
	public final static String URL_PREFIX = "://";

	/**
	 * 
	 * @param url
	 */
	public static String getUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		try {
			URL tmpUrl = new URL(url);

			StringBuilder urlStr = new StringBuilder();
			urlStr.append(tmpUrl.getProtocol()).append(URL_PREFIX).append(tmpUrl.getHost());
			return urlStr.toString();
		} catch (MalformedURLException e) {
		}
		return null;
	}
	
	public static int getPort(String url){
		if (StringUtils.isBlank(url)) {
			return 80;
		}
		try {
			URL tmpUrl = new URL(url);
			return tmpUrl.getPort() == -1 ? 80 : tmpUrl.getPort();
		} catch (MalformedURLException e) {
		}
		return 80;
	}

	public static String encode(String srcUrl) {
		String encodeUrl = null;
		try {
			encodeUrl = URLEncoder.encode(srcUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("encode exception,srcUrl=" + srcUrl, e);
		}
		return encodeUrl;
	}
	
	public static String decode(String str) {
		String decodeStr = null;
		try {
			decodeStr = URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("decode exception,str=" + str, e);
		}
		return decodeStr;
	}
	
	public static InputStream getInputStreamFromUrl(String url){
		InputStream is = null;
		try {
			if(StringUtils.isBlank(url)) return null;
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			log.error(">>>get inputstream from url=" + url, e);
		}
		return is;
	}
	
	/**
	 * 按行读取文本文件内容，使用时注意文件大小
	 * @param url
	 * @return
	 */
	public static List<String> getLinesFromUrl(String url){
		InputStream is = getInputStreamFromUrl(url);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			log.error(">>>readLine exception", e);
		} finally{
			try {
				br.close();
				is.close();
			} catch (IOException e) {
				log.error(">>>stream close exception", e);
			}
		}
		return lines;
	}
	
}
