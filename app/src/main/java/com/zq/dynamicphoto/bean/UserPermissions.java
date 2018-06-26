package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class UserPermissions extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 322809775662931906L;

	private Integer id;
	
	private Integer iuserId;//本人id
	
	private Integer uuserId;//
	
	private Integer iLabelId;
	
	private Integer permissionsType;// 权限类型1对方看不到我2不看对方
	
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

	public Integer getiLabelId() {
		return iLabelId;
	}

	public void setiLabelId(Integer iLabelId) {
		this.iLabelId = iLabelId;
	}

	public Integer getPermissionsType() {
		return permissionsType;
	}

	public void setPermissionsType(Integer permissionsType) {
		this.permissionsType = permissionsType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
