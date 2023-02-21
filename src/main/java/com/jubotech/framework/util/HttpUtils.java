package com.jubotech.framework.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSON;

public class HttpUtils {

	private static String doGet(String url) {
		// 构建HttpClient实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 设置请求超时时间(毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build();
		CloseableHttpResponse response = null;
		try {

			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);

			response = client.execute(httpGet);
			// 判断状态码
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// 获取body的内容
				HttpEntity entity = response.getEntity();
				String string = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
				return string;
			}
			return null;
		} catch (Exception e) {
			System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
			}
		}
		return null;
	}

	private static String doPost(String url) {
		// 构建HttpClient实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 设置请求超时时间(毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build();
		// 指定GET请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
			// 判断状态码
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// 获取body的内容
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
			}
			return null;
		} catch (Exception e) {

			System.out.println(e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
			}
		}
		return null;
	}

	private static String doPost(String url, Map<String, Object> map) {
		// 构建HttpClient实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 设置请求超时时间(毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpPost.setConfig(requestConfig);
		// 构建body传输
		StringEntity stringEntity = new StringEntity(JSON.toJSONString(map), StandardCharsets.UTF_8);
		httpPost.setEntity(stringEntity);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
			// 判断状态码
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// 获取body的内容
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
			}
			return null;
		} catch (Exception e) {

			System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
			}
		}
		return null;
	}

	private static String doPost(String url, String string) {
		// 构建HttpClient实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 设置请求超时时间(毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpPost.setConfig(requestConfig);
		// 构建body传输
		StringEntity stringEntity = new StringEntity(string, StandardCharsets.UTF_8);
		httpPost.setEntity(stringEntity);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
			// 判断状态码
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// 获取body的内容
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
			}
			return null;
		} catch (Exception e) {

			System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("异常URL【" + url + "】\n 异常" + e.getMessage());
			}
		}
		return null;
	}

	public static String sendGet(String getUrl, Map<String, Object> paraMap) {
		String stringParams = getStringParams(paraMap);
		getUrl = getUrl.contains("?") ? getUrl + stringParams : getUrl + "?" + stringParams;
		return doGet(getUrl);
	}

	public static String sendPost(String getUrl, Map<String, Object> paraMap) {
		String stringParams = getStringParams(paraMap);
		getUrl = getUrl.contains("?") ? getUrl + stringParams : getUrl + "?" + stringParams;
		return doPost(getUrl);
	}

	public static String sendPostBody(String getUrl, Map<String, Object> paraMap) {
		return doPost(getUrl, paraMap);
	}

	public static String sendPostBody(String getUrl, String string) {
		return doPost(getUrl, string);
	}

	private static String getStringParams(Map<String, Object> paraMap) {
		if (paraMap == null) {
			paraMap = new LinkedHashMap<>();
		}

		StringBuilder builder = new StringBuilder();

		paraMap.forEach((key, value) -> {
			builder.append(key);
			builder.append("=");
			builder.append(value);
			builder.append("&");
		});
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public static String sendGet(String getUrl, String params) {
		getUrl = getUrl.contains("?") ? getUrl + params : getUrl + "?" + params;
		return doGet(getUrl);
	}

	public static String sendPost(String getUrl, String params) {
		getUrl = getUrl.contains("?") ? getUrl + params : getUrl + "?" + params;
		return doPost(getUrl);
	}

	public static String sendGet(String getUrl) {
		getUrl = getUrl.contains("?") ? getUrl : getUrl + "?";
		return doGet(getUrl);
	}

	public static String sendPost(String getUrl) {
		getUrl = getUrl.contains("?") ? getUrl : getUrl + "?";
		return doPost(getUrl);
	}

}
