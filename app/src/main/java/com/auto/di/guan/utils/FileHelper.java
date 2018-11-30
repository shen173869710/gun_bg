package com.auto.di.guan.utils;


import android.content.Context;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.auto.di.guan.MyApplication;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;


public class FileHelper {

	private static String TAG = FileHelper.class.getSimpleName();
	private static final String dirName = "com.1111111111";

	public static void saveToSDCard(String filename, String content)throws Exception {
		File dir = createDir(getStoragePath(MyApplication.getInstance(), false)+File.separator+dirName);
		File file = createFile(dir +File.separator +filename);
		FileOutputStream outStream = new FileOutputStream(file, true);
		outStream.write(content.getBytes());
		outStream.write("\r\n".getBytes());
		outStream.close();
	}

	private static String getStoragePath(Context mContext, boolean is_removale) {
		StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
		Class<?> storageVolumeClazz = null;
		try {
			storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
			Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			Object result = getVolumeList.invoke(mStorageManager);
			final int length = Array.getLength(result);
			for (int i = 0; i < length; i++) {
				Object storageVolumeElement = Array.get(result, i);
				String path = (String) getPath.invoke(storageVolumeElement);
				boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
				if (is_removale == removable) {
					return path;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 *  判断文件是否存在
	 */
	public static boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	
	
    /**
     * 创建目录
     * @param path
     * @return
     */
    public static File createDir(String path)
    {
        File dir = new File(path);
        if(!dir.exists()) 
        {      
          dir.mkdir();
        }
        return dir;
    }
    
    
    /*
     * 创建文件
     */
    public static File createFile(String path) throws IOException 
    {
        File file = new File(path);
        if(!file.exists()) 
        {      
          file.createNewFile();
        }
        return file;
    }

    /**
     * 获取当前目录下面的文件信息 并且对文件进行排序
     *
     * @param fileName 根目录地址
     * @return
     */
    public static LinkedList<FileAttr> getDirFile(Context context, String fileName){

        File file = createDir(getStoragePath(context, false)+File.separator+dirName);
        File[] files = file.listFiles();

        LinkedList<FileAttr> list = new LinkedList<FileAttr>();
        if(files != null && files.length >= 0){
            for(int i=0; i<files.length; i++){
            	String[] str = cutFileName(files[i].getName());
            	if (str == null)
            	{
            		continue;
            	}
            	if (fileName.equals(str[0]))
            	{
            		FileAttr fileAttr = new FileAttr(str[0], str[1], str[2], files[i].length());
            		list.add(fileAttr);
            	}
            }
            ListSort(list);
        }
        return list;
    }


	public static String[] cutFileName(String fileName) {
		String[] str = fileName.split("-");
		if (str.length != 3)
		{
			return null;
		}
		String[] num = str[2].split("\\.");
		str[2] = num[0];
		return str;
	}

	/*
	 *  文件名排序
	 */
	public static void ListSort(LinkedList<FileAttr>fileAttrs) {
		Comparator<FileAttr>comparator = new Comparator<FileAttr>() {
			@Override
			public int compare(FileAttr o1, FileAttr o2) {
				if (Integer.valueOf(o1.getFileTime()) == Integer.valueOf(o2.getFileTime())) {
						
					return Integer.valueOf(o1.getFileVersion()) - Integer.valueOf(o2.getFileVersion());
				}
				else
				{
					return Integer.valueOf(o1.getFileTime()) - Integer.valueOf(o2.getFileTime());
				}
			}
		};
		Collections.sort(fileAttrs, comparator);
	}
	
}
