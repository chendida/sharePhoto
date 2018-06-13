package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class DynamicVideo extends BaseModel implements Serializable {

	private static final long serialVersionUID = -2079939139102022803L;
	
	private Integer id;
	
	private Integer dynamicId;
	
	private String videoURL;//视频地址
	
	private String videoCover;//视频缩略图

	private String downloadVideoURL;//视频下载地址

	public String getDownloadVideoURL() {
		return downloadVideoURL;
	}

	public void setDownloadVideoURL(String downloadVideoURL) {
		this.downloadVideoURL = downloadVideoURL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

}
