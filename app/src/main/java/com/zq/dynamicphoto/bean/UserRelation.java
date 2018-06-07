package com.zq.dynamicphoto.bean;


import java.io.Serializable;

public class UserRelation extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1117545825079416776L;

	private Integer id;
	
	private Integer iuserId;//获取相册动态时传的本人id
	
	private Integer uuserId;
	
	private String createTime;

	private Integer relationType;	//映射类型1关注别人2别人关注你

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}

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

}
