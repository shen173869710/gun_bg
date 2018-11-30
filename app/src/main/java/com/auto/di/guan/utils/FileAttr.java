package com.auto.di.guan.utils;

public class FileAttr {
	
	/*
	 * 文件名由一下三个字段组成 fileType-filetime-fileVersion.log
	 * fileSize 代表文件的大小
	 */
	private String fileType;
	private String fileTime;
	private String fileVersion;
	private long fileSize;
	private int fileNum;
	
	public FileAttr(String fileType, String fileTime, String fileVersion,
			long fileSize) {
		super();
		this.fileType = fileType;
		this.fileTime = fileTime;
		this.fileVersion = fileVersion;
		this.fileSize = fileSize;
		this.fileNum = Integer.valueOf(fileTime)+Integer.valueOf((fileVersion));
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public String getFileVersion() {
		return String.format("%03d", Integer.valueOf(fileVersion));
	}
	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileName() {
		return fileType+"-"+fileTime+"-"+fileVersion+".log";
	}
	
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	
}
