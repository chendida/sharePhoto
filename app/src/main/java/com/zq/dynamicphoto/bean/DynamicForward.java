package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class DynamicForward extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -6880479030215573036L;

	private Integer id;
	
	private Integer iuserId;//本人id
	
	private Integer uuserId;//被转发人id
	
	private Integer idynamicId;
	
	private Integer udynamicId;//被转发动态id
	
	private String createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIuserId() {
		return iuserId;
	}

	public void setIuserId(Integer iuserId) {
		this.iuserId = iuserId;
	}

	public Integer getUuserId() {
		return uuserId;
	}

	public void setUuserId(Integer uuserId) {
		this.uuserId = uuserId;
	}

	public Integer getIdynamicId() {
		return idynamicId;
	}

	public void setIdynamicId(Integer idynamicId) {
		this.idynamicId = idynamicId;
	}

	public Integer getUdynamicId() {
		return udynamicId;
	}

	public void setUdynamicId(Integer udynamicId) {
		this.udynamicId = udynamicId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
