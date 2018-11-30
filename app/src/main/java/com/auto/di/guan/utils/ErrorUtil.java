package com.auto.di.guan.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

public class ErrorUtil {
	
	private static final int LOG_TIMER_DELAY = 10 * 1000;
	private static final int LOG_QUEUE_TRIGGER_SIZE = 10;
	
	private  AtomicBoolean sIsTimerStarted = new AtomicBoolean(false);
	private  AtomicBoolean sCanWriteToDisk = new AtomicBoolean(true);
	
	private  Vector<String> sPendingQueue = new Vector<String>();
	private  Vector<String> sWriterQueue = new Vector<String>();
	
	private  List<FileAttr> mLogFiles = new ArrayList<FileAttr>();
	
	private String fileName = "error.txt";
	private int maxNum;
	private long maxLength;
	private Context context;

	private  Handler sHandler;
	{
		if (Looper.myLooper() == null) {
			Looper.prepare();
			sHandler = new Handler();
		} else {
			sHandler = new Handler();
		}
	}


	public   void queueMessage(Context context, String msg) {
		this.context = context;
		startTimer();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis()));
		msg = "\n" + time + " | " + msg;
		sPendingQueue.add(msg);
		writeMessagesInTheQueue();

//		writeSD(msg);
	}

	private  void startTimer() {
		if (sIsTimerStarted.compareAndSet(false, true)) {
			sHandler.post(sDelayRunnable);
		}
	}
	
	/**
	 * 启动一个轮询写入的操作， 
	 * 
	 */
	private  Runnable sDelayRunnable = new Runnable() {
		@Override
		public void run() {
			writeMessagesInTheQueue();
			
			if (sPendingQueue.size() > 0) {
				sHandler.postDelayed(sDelayRunnable, LOG_TIMER_DELAY);
				
			} else {
				
				if (sIsTimerStarted.compareAndSet(true, false)) {
					sHandler.removeCallbacks(sDelayRunnable);
				}
			}
		}
	};
	
	private  void writeMessagesInTheQueue() {

		if (canWriteToDisk() && sCanWriteToDisk.compareAndSet(true, false)) {
			Thread writerThread = new Thread(sWriterRunnable);
			writerThread.start();
		}
	}

	
	private  boolean canWriteToDisk() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	/*
	 *   启动一个文件的写入线程
	 */
	private  Runnable sWriterRunnable = new Runnable() {
		@Override
		public void run() {
			
			copyMessagesFromQueue();
			saveMessagesToDisk();
		}
	};
	
	private  void copyMessagesFromQueue() {
		synchronized (sPendingQueue) {
			Iterator<String> it = sPendingQueue.iterator();
			while (it.hasNext()) {
				sWriterQueue.add(it.next());
			}
			sPendingQueue.clear();
		}
	}
	
	
	/*
	 *  将消息写入文件]
	 *  1. 写入之前 找到可以写的文件
	 *  2. 
	 */
	private  void saveMessagesToDisk() {
		Iterator<String> it = sWriterQueue.iterator();
		
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			builder.append(it.next());
		} 
		/*
		 *   写入文件
		 *   1. 获取当前目录下文件内容
		 */
		if (builder.toString().length() > 0)
		{
			writeSD(builder.toString());
		}
		sWriterQueue.clear();
		sCanWriteToDisk.set(true);
	}

	/*
	 *  写入文件
	 *  1. 如果文件list.siez == 0   创建新文件
	 *  2. 如果文件 0 < list.siez < maxSize;  找到最后修改的文件
	 *  3. 如果文件 lsit siez > maxSize    删除最早的文件， 添加新文件
	 */
	private void writeSD(String msg) {
		LogUtils.e("ErrorUtil", "msg ="+msg);
//		LinkedList<FileAttr> fileAttrs = FileHelper.getDirFile(context,fileName);
//
//		int numSize = fileAttrs.size();
//		String time;
//		String name = null;
//		if (numSize == 0)
//		{
//			FileAttr fileAttr = new FileAttr(fileName, currentTime, String.format("%03d", 0), 0);
//			name = fileAttr.getFileName();
//		}
//		else if ( (numSize > 0) && (numSize < maxNum))
//		{
//			long size = fileAttrs.getLast().getFileSize();
//			time = fileAttrs.getLast().getFileTime();
//			if (size <= maxLength)
//			{
//				if (Integer.valueOf(currentTime) > Integer.valueOf(time))
//				{
//					FileAttr fileAttr = new FileAttr(fileName, currentTime, String.format("%03d", 0), 0);
//					name = fileAttr.getFileName();
//				}
//				else
//				{
//					name = fileAttrs.getLast().getFileName();
//				}
//			}
//			else
//			{
//				if (Integer.valueOf(currentTime) > Integer.valueOf(time))
//				{
//					FileAttr fileAttr = new FileAttr(fileName, currentTime, String.format("%03d", 0), 0);
//					name = fileAttr.getFileName();
//				}
//				else
//				{
//					int ver = Integer.valueOf(fileAttrs.getLast().getFileVersion()) + 1;
//					FileAttr fileAttr = new FileAttr(fileName, currentTime, String.format("%03d", ver), 0);
//					name = fileAttr.getFileName();
//				}
//			}
//		}


		try {
			if (msg != null)
			{
				FileHelper.saveToSDCard(fileName, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
