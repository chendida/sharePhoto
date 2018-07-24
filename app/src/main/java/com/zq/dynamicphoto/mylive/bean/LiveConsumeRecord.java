package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;

import java.io.Serializable;

public class LiveConsumeRecord extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8339608331412083400L;

	//直播间用户id
	private Integer userId;
	//当前直播间id
	private Integer newLiveId ;
	//在线人数
	private Integer peopleNum;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//消耗的币
	private Integer consumeCoin;

	private String cover;

	private String title;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LiveConsumeRecord() {
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getNewLiveId() {
		return newLiveId;
	}
	public void setNewLiveId(Integer newLiveId) {
		this.newLiveId = newLiveId;
	}
	public Integer getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(Integer peopleNum) {
		this.peopleNum = peopleNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getConsumeCoin() {
		return consumeCoin;
	}
	public void setConsumeCoin(Integer consumeCoin) {
		this.consumeCoin = consumeCoin;
	}

}
