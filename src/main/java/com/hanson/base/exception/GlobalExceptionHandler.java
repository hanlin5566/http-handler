package com.hanson.base.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hanson.base.response.ResponseCode;
import com.hanson.base.response.ResponseData;

/**
 * Create by hanlin on 2018年10月11日
 * 对于基本的404/500/401错误页面，统一采取json方式友好提示
 **/
@Deprecated
public class GlobalExceptionHandler implements ErrorController{
	private static final String PATH = "/error";

	@Autowired
	private ErrorAttributes errorAttributes;

	@RequestMapping(value = PATH, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData error(HttpServletResponse resp, HttpServletRequest request) {
		// 错误处理逻辑
		ResponseData ret = buildBody(request,false);
		resp.setStatus(200);
		return ret;
	}


	private ResponseData buildBody(HttpServletRequest request,Boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		Map<String, Object> attr = errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
		Integer status = (Integer) attr.get("status");
		String path = (String) attr.get("path");
		String messageFound = (String) attr.get("message");
		String message = "";
		String trace = "";
		if (!StringUtils.isEmpty(path)) {
			message = String.format("Requested path %s with result %s", path, messageFound);
		}
		ResponseCode code = ResponseCode.INTERNAL_SERVER_ERROR;
		if(includeStackTrace){
			trace = (String) attr.get("trace");
			if (!StringUtils.isEmpty(trace)) {
				message += String.format(" and trace %s", trace);
			}
			switch (status) {
			case 400:
				code = ResponseCode.ERROR_PARAM;
				break;
			case 404:
				code = ResponseCode.RESOURCE_NOT_FOUND;
				break;
			case 500: 
				code = ResponseCode.INTERNAL_SERVER_ERROR;
				break;
			default:
				code = ResponseCode.INTERNAL_SERVER_ERROR;
				break;
			}
		}
		ResponseData fail = ResponseData.fail(code);
		fail.setData(message);
		return fail;
	}


	public String getErrorPath() {
		return null;
	}
}
