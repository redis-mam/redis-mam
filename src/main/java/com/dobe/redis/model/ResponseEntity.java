package com.dobe.redis.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
*  响应消息
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class ResponseEntity<T> implements Serializable {

	private static final long serialVersionUID = -1483013122646260435L;
	public static final int SUCCESS_CODE = 200;
	public static final int ERROR_CODE = -999;
    /**
     * 用于简单的状态返回
     */
    public static final ResponseEntity<?> SUCCESS = new ResponseEntity(SUCCESS_CODE);
    public static final ResponseEntity<?> ERROR = new ResponseEntity<>(ERROR_CODE, "Error");

	/** 响应状态 **/
	private int resStatus;
	/** 响应信息 **/
	private T resMsg;

	/** 响应参数**/
	private Map<String, Object> params = new HashMap<String, Object>();

	public ResponseEntity() { }
	
	public ResponseEntity(int resStatus) {
		this.resStatus = resStatus;
	}

	public ResponseEntity(int resStatus, T resMsg) {
		super();
		this.resStatus = resStatus;
		this.resMsg = resMsg;
	}
	
	public ResponseEntity(int resStatus, T resMsg, Map<String, Object> params) {
		super();
		this.resStatus = resStatus;
		this.resMsg = resMsg;
		this.params = params;
	}

	public int getResStatus() {
		return resStatus;
	}

	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}

	public T getResMsg() {
		return resMsg;
	}

	public void setResMsg(T resMsg) {
		this.resMsg = resMsg;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public ResponseEntity<T> addParam(String key, Object value) {
		this.params.put(key, value);
		return this;
	}
	
	public boolean validSuc() {
		return !(this.resStatus == SUCCESS_CODE);
	}
	
	public static ResponseEntity<?> error(Object msg){
		return new ResponseEntity<>(ERROR_CODE, msg);
	}
	
	public static ResponseEntity<?> success(){
        return new ResponseEntity<>(SUCCESS_CODE);
    }
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
