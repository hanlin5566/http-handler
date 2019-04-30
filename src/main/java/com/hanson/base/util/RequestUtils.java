package com.hanson.base.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hanson.base.response.ResponseCode;
import com.hanson.base.response.ResponseData;
import com.hanson.base.wrapper.request.RequestSnapShot;
import com.hanson.base.wrapper.request.RequestWrapper;

/**
 * Create by hanlin on 2017年11月23日
 **/
public class RequestUtils {
	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);
	private static boolean output = false;

	// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
	public final static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (output) {
			logger.debug("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (output) {
					logger.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (output) {
					logger.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (output) {
					logger.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (output) {
					logger.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (output) {
					logger.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 参数解析,接收RequestWrapper 支持多次读取
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getParam(final HttpServletRequest request) throws IOException {
		String method = request.getMethod();
		JSONObject ret = new JSONObject();
		switch (method) {
		case "POST":
			String body = null;
			if(request instanceof RequestWrapper) {
				body = ((RequestWrapper)request).getRequestParams();
			}else {
				body = StreamUtils.copyToString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
			}
			if(!StringUtils.isEmpty(body)) {
				//如果不是json结构，则用responsedata包装一下,重置内容。
				try {
					ret = JSONObject.parseObject(body);
				} catch (Exception e) {
					logger.warn("request body not json type, url:{},body:{}", request.getRequestURI(),body);
					ResponseData responseData = ResponseData.fail(ResponseCode.RESET_CONTENT);
					responseData.setData(body);
					ret = (JSONObject) JSONObject.toJSON(responseData);
				}
			}
			break;
		case "GET":
			ret = (JSONObject) JSONObject.toJSON(request.getParameterMap());
			break;
		}
		
		return ret;
	}

	/**
	 * header解析
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getHeader(final HttpServletRequest request){
		JSONObject ret = new JSONObject();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			ret.put(key, value);
		}
		return ret;
	}
	
	/**
	 * cookies解析
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getCookies(final HttpServletRequest request){
		JSONObject ret = new JSONObject();
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				ret.put(cookie.getName(), cookie);
			}
		}
        return ret;
	}
	
	public static RequestSnapShot getRequestSnapShot(final HttpServletRequest request,String token, String serviceId) {
		return new RequestSnapShot(request, token, serviceId);
	}
	
	/**
	 * 判断{@link} {@link RequestUtils} getParam 方法返回值，判断body入参是否为JSON结构。
	 * body参数不是重构内容
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static boolean bodyIsJSONType(JSONObject param){
        return param.containsKey("code") && param.getIntValue("code") == ResponseCode.RESET_CONTENT.code();
	}
	
}
