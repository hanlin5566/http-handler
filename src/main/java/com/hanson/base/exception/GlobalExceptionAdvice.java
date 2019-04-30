package com.hanson.base.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hanson.base.enums.IResponseCode;
import com.hanson.base.response.ResponseCode;
import com.hanson.base.response.ResponseData;
import com.hanson.base.util.RequestUtils;

/**
 * Create by hanlin on 2018年10月31日
 **/
@RestControllerAdvice
public class GlobalExceptionAdvice {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.application.name}")
	private String appName;
	//默认为false
	@Value("${hj.msg.detail.enabled:false}")
	boolean enabledDetailMsg = false;

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseData handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		ResponseData fail = ResponseData.fail(ResponseCode.ERROR_PARAM);
		logger.error(getPrettyDetailMsg(ResponseCode.ERROR_PARAM),RequestUtils.getRequestSnapShot(request, null, appName),e);
        return fail;
    }
    
    /**
     * 404 - Resource Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseData handleHttpMessageNotFoundException(NoHandlerFoundException e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		ResponseData fail = ResponseData.fail(ResponseCode.RESOURCE_NOT_FOUND);
		logger.error(getPrettyDetailMsg(ResponseCode.RESOURCE_NOT_FOUND),RequestUtils.getRequestSnapShot(request, null, appName),e);
        return fail;
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		ResponseData fail = ResponseData.fail(ResponseCode.METHOD_NOT_ALLOWED);
		logger.error(getPrettyDetailMsg(ResponseCode.METHOD_NOT_ALLOWED),RequestUtils.getRequestSnapShot(request, null, appName),e);
        return fail;
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ResponseData handleException(Exception e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		ResponseData fail = ResponseData.fail(ResponseCode.INTERNAL_SERVER_ERROR);
		if(enabledDetailMsg) {
			fail.setMessage(String.format(ResponseCode.INTERNAL_SERVER_ERROR.detailMsg(), appName,e.getMessage()));
		}
		logger.error(getPrettyDetailMsg(String.format(ResponseCode.INTERNAL_SERVER_ERROR.detailMsg(),appName,e.getMessage())),RequestUtils.getRequestSnapShot(request, null, appName),e);
        return fail;
    }

    /**
     * 服务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public ResponseData ServiceException(ServiceException e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		IResponseCode code = e.getCode();
		ResponseData fail = ResponseData.fail(code);
		if(enabledDetailMsg) {
			fail.setMessage(e.getMessage());
		}
		logger.warn(getPrettyDetailMsg(e.getMessage()),RequestUtils.getRequestSnapShot(request, null, appName),e);
        return fail;
    }
    
    /**
     * 访问层异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ControllerException.class)
    public ResponseData ControllerException(ControllerException e) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	HttpServletRequest request = attributes.getRequest();
    	IResponseCode code = e.getCode();
    	ResponseData fail = ResponseData.fail(code);
    	if(enabledDetailMsg) {
    		fail.setMessage(e.getMessage());
		}
    	logger.warn(getPrettyDetailMsg(e.getMessage()),RequestUtils.getRequestSnapShot(request, null, appName),e);
    	return fail;
    }
    
    private String getPrettyDetailMsg(Object obj) {
    	if(obj instanceof IResponseCode) {
    		return ((IResponseCode)obj).detailMsg()+" [reqeustSnapShot]-->{}";
    	}else if(obj instanceof String) {
    		return obj.toString()+" [reqeustSnapShot]-->{}";
    	}else if(obj instanceof ControllerException) {
    		return ((ControllerException)obj).getMessage()+" [reqeustSnapShot]-->{}";
    	}else if(obj instanceof ServiceException) {
    		return ((ServiceException)obj).getMessage()+" [reqeustSnapShot]-->{}";
    	}
    	return obj.toString();
    }
}
