package com.zq.dynamicphoto.bean;

import java.util.Map;

public class Result {
	
	/** 服务器返回的结果码  **/
	private int resultCode = -1;
	
	/** 结果描述  **/
	private String resultInfo;
	
	/** 结果数据 **/
	private String data;
	
	public Map<String, Object> map;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
