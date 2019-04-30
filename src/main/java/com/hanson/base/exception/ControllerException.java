package com.hanson.base.exception;

import com.hanson.base.enums.IResponseCode;

/**
 * Create by hanlin on 2018年11月1日
 **/
public class ControllerException extends RuntimeException {

	private static final long serialVersionUID = 8199321945311887310L;

	private IResponseCode code;
	private Object input = null;

	public ControllerException(IResponseCode code,String message,Throwable cause,Object input){
		super(message,cause);
		this.code = code;
		this.input = input;
	}
	public ControllerException(IResponseCode code,String message,Throwable cause){
		super(message,cause);
        this.code = code;
    }
	public ControllerException(IResponseCode code,String message){
		super(message);
		this.code = code;
	}
	public ControllerException(IResponseCode code){
		super(code.detailMsg());
		this.code = code;
	}
	public ControllerException(IResponseCode code,Throwable cause){
		super(code.detailMsg(),cause);
		this.code = code;
	}
	public IResponseCode getCode() {
		return code;
	}
	public void setCode(IResponseCode code) {
		this.code = code;
	}
	public Object getInput() {
		return input;
	}
	public void setInput(Object input) {
		this.input = input;
	}
}
