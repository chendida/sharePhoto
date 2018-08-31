package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class DynamicStatic extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -7604169284701346570L;

	private Integer id;
	
	private String json;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
