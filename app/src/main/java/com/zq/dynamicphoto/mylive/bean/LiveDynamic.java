package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;
import com.zq.dynamicphoto.bean.Dynamic;

import java.io.Serializable;
import java.util.List;

public class LiveDynamic extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8714940109591387450L;

	
	private Integer id;
	//产品动态Id
	private Integer dynamicId;
	//直播间id
	private Integer liveId;
	//用户id
	private Integer userId;
	//是否显示
	private Integer isShow;
	//创建时间
	private String createTime;
	
	private List<Dynamic> dynamic;
	
	private List<NewLiveRoom> newLiveRoom;

	private String promoteImg;

	public String getPromoteImg() {
		return promoteImg;
	}

	public void setPromoteImg(String promoteImg) {
		this.promoteImg = promoteImg;
	}

	public LiveDynamic() {
		// TODO Auto-generated constructor stub
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
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

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Dynamic> getDynamic() {
		return dynamic;
	}

	public void setDynamic(List<Dynamic> dynamic) {
		this.dynamic = dynamic;
	}

	public List<NewLiveRoom> getNewLiveRoom() {
		return newLiveRoom;
	}

	public void setNewLiveRoom(List<NewLiveRoom> newLiveRoom) {
		this.newLiveRoom = newLiveRoom;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
