package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;

import java.io.Serializable;

public class LiveUser extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4395460310130883847L;
	
	//单次直播标识
	private Integer newLiveId;
	//直播间ID
	private Integer liveId;
	//用户ID
	private Integer userId;
	//微信唯一标识
	private String unionid;
	//创建时间
	private String createTime;
	
	public LiveUser() {
		// TODO Auto-generated constructor stub
	}

	public String getCreateTime() {
		return createTime;
	}

	public Integer getNewLiveId() {
		return newLiveId;
	}

	public void setNewLiveId(Integer newLiveId) {
		this.newLiveId = newLiveId;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
}
