package com.yunpan;

import java.io.Serializable;

public class CloudFile implements Serializable{
	private static final long serialVersionUID = 1L;
	private String path;
	private boolean isFile;
	private long date;
	
	public CloudFile(String path, boolean isFile) {
		super();
		this.path = path;
		this.isFile = isFile;
		this.date = System.currentTimeMillis();
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	public String getPathKey() {
		return "_"+path;
	}
	
	public long getDate() {
		return date;
	}
	
	
	
	
	

}
