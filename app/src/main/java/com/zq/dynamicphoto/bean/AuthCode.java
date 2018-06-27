package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class AuthCode extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -363180857986576988L;

	private Integer id;
	
	private String mobile;
	
	private String content;
	
	private Long textTime;
	
	private String createTime;
	
	private String imei;
	
	private Integer model;//1绑定手机号，2是重置密码
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTextTime() {
		return textTime;
	}

	public void setTextTime(Long textTime) {
		this.textTime = textTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

}
