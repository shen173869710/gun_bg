package com.auto.di.guan;

import com.auto.di.guan.utils.Task;

public interface IUrlRequestCallBack {
	  public void urlRequestStart(Task result);
	  public void urlRequestEnd(Task result);
	  public void urlRequestException(Task result);
}
