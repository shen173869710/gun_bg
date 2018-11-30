/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.auto.di.guan;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.auto.di.guan.entity.Entiy;

import java.io.IOException;

public class ConsoleActivity extends SerialPortActivity {

	EditText mReception;
	EditText Emission;

	Button rbid;
	Button rgid;

	Button sbid;
	Button sgid;

	Button rs;
	Button kf;
	Button gf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);

//		setTitle("Loopback test");
		mReception = (EditText) findViewById(R.id.EditTextReception);

		Emission = (EditText) findViewById(R.id.EditTextEmission);
		Emission.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int i;
				CharSequence t = v.getText();
				char[] text = new char[t.length()];
				for (i=0; i<t.length(); i++) {
					text[i] = t.charAt(i);
				}
				try {
					mOutputStream.write(new String(text).getBytes());
					mOutputStream.write('\n');
//					mReception.setText(new String(text));
//					Emission.getText().clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		});

		rbid = (Button) findViewById(R.id.rbid);
		rgid = (Button) findViewById(R.id.rgid);

		sbid = (Button) findViewById(R.id.sbid);
		sgid = (Button) findViewById(R.id.sgid);

		rs = (Button) findViewById(R.id.rs);
		kf = (Button) findViewById(R.id.kf);
		gf = (Button) findViewById(R.id.gf);

		rbid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mOutputStream.write(new String("rbid").getBytes());
					mOutputStream.write('\n');
//					mReception.setText(new String(text));
//					Emission.getText().clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		rgid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mOutputStream.write(new String("rgid").getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		rs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					mOutputStream.write(new String("rs 001 001 0 00").getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		kf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showToastLongMsg("开启设备  发送命令"+"kf 002 001 0 01");
//					}
//				});
//				try {
//					String cmd = Entiy.cmdOpen("002", "001","0","01");
//					mOutputStream.write(cmd.getBytes());
//					mOutputStream.write('\n');
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		});

		gf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					mOutputStream.write(new String("gf 002 001 0 00").getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		sbid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mOutputStream.write(new String("sbid001").getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		sgid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mOutputStream.write(new String("sgid001").getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
//		runOnUiThread(new Runnable() {
//			public void run() {
//
//				if (mReception != null) {
//					mReception.append(new String(buffer, 0, size));
//
//
//				}
//			}
//		});

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToastLongMsg("aaaaaaaaaaaaaaa开启设备成功"+new String(buffer, 0, size));

			}
		});


	}

	public void showToastLongMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
