package com.auto.di.guan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.entity.ElecEvent;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.PollingUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
public class FragmentTab7 extends BaseFragment {
	private TextView textView;
	private View view;

	private ProgressBar db_progress;
	private TextView db_desc;
	private Button db_load;
	private Button db_unload;
	private TextView title_bar_code;
//	private AsyncQueryHandler mHandler = new AsyncQueryHandler() {
//		@Override
//		protected Handler createHandler(Looper looper) {
//			return super.createHandler(looper);
//		}
//
//		@Override
//		public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
//			super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
//		}
//
//		@Override
//		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//			super.onQueryComplete(token, cookie, cursor);
//		}
//
//		@Override
//		protected void onInsertComplete(int token, Object cookie, Uri uri) {
//			super.onInsertComplete(token, cookie, uri);
//		}
//
//		@Override
//		protected void onUpdateComplete(int token, Object cookie, int result) {
//			super.onUpdateComplete(token, cookie, result);
//		}
//
//		@Override
//		protected void onDeleteComplete(int token, Object cookie, int result) {
//			super.onDeleteComplete(token, cookie, result);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//		}
//	};
//	ContentObserver contentObserver;

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

           String path1 = intent.getDataString();//得到的就有U盘的路径**
			if (path1 != null) {
				Toast.makeText(getActivity(),"usb path "+path1.toString(), Toast.LENGTH_LONG).show();
			}

			if (intent.getAction().equals("Android.intent.action.MEDIA_EJECT") || intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")) {
				Toast.makeText(getActivity(),"usb romove ", Toast.LENGTH_LONG).show();


			}else if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
				String path = intent.getDataString();
				String pathString = path.split("file://")[1];
				Toast.makeText(getActivity(),"pathString= "+pathString, Toast.LENGTH_LONG).show();
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_7, null);
		db_progress = (ProgressBar)view.findViewById(R.id.db_progress) ;
		db_desc = (TextView)view.findViewById(R.id.db_desc);
		db_load = (Button)view.findViewById(R.id.db_load);
		db_unload = (Button)view.findViewById(R.id.db_unload);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addDataScheme("file");
		getActivity().registerReceiver(receiver, filter);
		init();
		db_desc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				String set = getExtSDCard();
				Toast.makeText(getActivity(),"set= "+set, Toast.LENGTH_LONG).show();
			}
		});

		title_bar_code = view.findViewById(R.id.title_bar_code);
		title_bar_code.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
//				PollingUtils.startPollingService(activity, MainActivity.ALERM_TIME);
				EventBus.getDefault().post(new ElecEvent());
			}
		});
		return view;
	}

	private void init() {
//		contentObserver = new ContentObserver(mHandler) {
//			@Override
//			public boolean deliverSelfNotifications() {
//				return super.deliverSelfNotifications();
//			}
//
//			@Override
//			public void onChange(boolean selfChange) {
//				super.onChange(selfChange);
//			}
//
//			@Override
//			public void onChange(boolean selfChange, Uri uri) {
//				super.onChange(selfChange, uri);
//			}
//		};
//
//		db_load.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
//
//		db_unload.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
		db_load.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				String Path = getStoragePath(getActivity(), true);
				Toast.makeText(getActivity(),"Path= "+Path, Toast.LENGTH_LONG).show();
			}
		});

		db_unload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				String[] result = null;
				StorageManager storageManager = (StorageManager)getActivity().getSystemService(Context.STORAGE_SERVICE);
				try {
					Method method = StorageManager.class.getMethod("getVolumePaths");
					method.setAccessible(true);
					try {
						result =(String[])method.invoke(storageManager);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < result.length; i++) {
						Toast.makeText(getActivity(),"result[i]= "+result[i], Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void refreshData() {

	}

	protected static final class QueryArg {
		public Uri uri;
		public String[] projection;
		public String selection;
		public String[] selectionArgs;
		public String orderBy;
		public ContentObserver contentObserver;
		public Runnable runnable;
		public Object cookie;
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

	public static String getExtSDCard(){
		File[] files=new File("/mnt").listFiles();
		String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
		String file;
		for (int i = 0; i < files.length; i++) {
			file=files[i].getAbsolutePath();
			if(!file.equalsIgnoreCase(sdcard)&&file.contains("ext")){
				return file;
			}

		}
		return null;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	};
}
