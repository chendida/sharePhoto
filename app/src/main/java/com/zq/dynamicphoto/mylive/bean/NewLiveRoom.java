package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;
import com.zq.dynamicphoto.bean.Dynamic;

import java.io.Serializable;
import java.util.List;

public class NewLiveRoom extends BaseModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4958314121901537998L;
	//直播标题
	private String title;
	//直播封面
	private String cover;
	//直播间创建时间
	private String createTime;
	//相册用户ID
	private Integer userId;
	//直播账号ID
	private Integer liveId;
	//拉流地址1
	private String play_url1;
	//拉流地址2
	private String play_url2;
	//拉流地址3
	private String play_url3;
	//推流地址
	private String pushURL;
	
	//相册用户ID
	private Integer admireCount;
		
	//相册用户ID
	private Integer appid;
	
	//真实观看人数
	private Integer realWatchNum;
	//观看人数
	private Integer watchNum;
	//观看递进数率
	private Integer watchNumRate;

	//虚拟观看人数
	private Integer popularValue;

	private Integer id;

	//最后一次心跳时间
	private Integer lastUpdateTime;
	
	private List<Dynamic> dynamicList;
	
	private List<LiveDynamic> liveDynamicList;

	private String remarkName;

	public Integer getPopularValue() {
		return popularValue;
	}

	public void setPopularValue(Integer popularValue) {
		this.popularValue = popularValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public String getPlay_url1() {
		return play_url1;
	}

	public void setPlay_url1(String play_url1) {
		this.play_url1 = play_url1;
	}

	public String getPlay_url2() {
		return play_url2;
	}

	public void setPlay_url2(String play_url2) {
		this.play_url2 = play_url2;
	}

	public String getPlay_url3() {
		return play_url3;
	}

	public void setPlay_url3(String play_url3) {
		this.play_url3 = play_url3;
	}

	public Integer getAdmireCount() {
		return admireCount;
	}

	public void setAdmireCount(Integer admireCount) {
		this.admireCount = admireCount;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getPushURL() {
		return pushURL;
	}

	public void setPushURL(String pushURL) {
		this.pushURL = pushURL;
	}

	public Integer getRealWatchNum() {
		return realWatchNum;
	}

	public void setRealWatchNum(Integer realWatchNum) {
		this.realWatchNum = realWatchNum;
	}

	public Integer getWatchNum() {
		return watchNum;
	}

	public void setWatchNum(Integer watchNum) {
		this.watchNum = watchNum;
	}

	public Integer getWatchNumRate() {
		return watchNumRate;
	}

	public void setWatchNumRate(Integer watchNumRate) {
		this.watchNumRate = watchNumRate;
	}

	public Integer getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public List<LiveDynamic> getLiveDynamicList() {
		return liveDynamicList;
	}

	public void setLiveDynamicList(List<LiveDynamic> liveDynamicList) {
		this.liveDynamicList = liveDynamicList;
	}

	public List<Dynamic> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(List<Dynamic> dynamicList) {
		this.dynamicList = dynamicList;
	}
	
	
}
