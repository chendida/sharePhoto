package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class LabelRelation extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -6546489828431635911L;

	private Integer id;
	
	private Integer labelId;
	
	private Integer dynamicId;
	
	private String createTime;
	
	private String updateTime;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
