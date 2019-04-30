package com.hanson.base.wrapper.request;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hanson.base.response.ResponseCode;
import com.hanson.base.response.ResponseData;
import com.hanson.base.util.RequestUtils;

/**
 * Create by hanlin on 2018年11月1日
 * request快照，包括请求信息
 **/
public class RequestSnapShot {
	/**
	 * 请求地址
	 */
	private String uri;
	/**
	 * 请求者IP
	 */
	private String ip;
	/**
	 * 请求者标识
	 */
	private String token;
	/**
	 * 请求参数
	 */
	private JSONObject param;
	/**
	 * 服务所在ip
	 */
	private String localIp;
	/**
	 * 服务所在端口
	 */
	private int localPort;
	/**
	 * 服务标识
	 */
	private String serviceId;
	/**
	 * header信息
	 */
	private JSONObject header;
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * cookies
	 */
	private JSONObject cooikes;
	
	
	
	public RequestSnapShot(HttpServletRequest request,String token, String serviceId){
		super();
		this.token = token;
		this.serviceId = serviceId;
		this.uri = request.getRequestURI();
		this.ip = RequestUtils.getIpAddress(request);
		try {
			//FIXME:此处request被读取后，是否可以多次读取
			request = new RequestWrapper(request);
			this.param = RequestUtils.getParam(request);
		} catch (Exception e) {
			 ResponseData fail = ResponseData.fail(ResponseCode.ERROR_PARAM);
			this.param =(JSONObject) JSONObject.toJSON(fail);
		}
		this.localIp = request.getLocalAddr();
		this.localPort = request.getLocalPort();
		this.header = RequestUtils.getHeader(request);
		this.method = request.getMethod();
		this.cooikes = RequestUtils.getCookies(request);
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public JSONObject getParam() {
		return param;
	}
	public void setParam(JSONObject param) {
		this.param = param;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public JSONObject getHeader() {
		return header;
	}
	public void setHeader(JSONObject header) {
		this.header = header;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public JSONObject getCooikes() {
		return cooikes;
	}
	public void setCooikes(JSONObject cooikes) {
		this.cooikes = cooikes;
	}
	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
