package com.wzp.fileselector.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import com.wzp.fileselector.R;
import com.wzp.fileselector.bean.SelectedFiles;
import com.wzp.fileselector.util.SDCardScanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 文件选择器主界面，调用方通过actionStart静态方法调用，在自身onActivityResult中获取选择的所有文件路径，返回的路径为json数组格式
 * */
public class FileHomeActivity extends Activity implements OnClickListener {
	private FileHomeActivity instance = this;
	private List<String> externalPaths;//所有外置存储路径，包括手机自带SD卡以及可插拔TF卡路径
	private int REQUEST;// 请求源，由本活动的调用方提供
	private String resultTag; // 本活动返回给调用方中intent附加参数的标识，由本活动的调用方提供
	private String myFolderPath;
	private TextView tvCancel;
	private RelativeLayout rlMy;
	private RelativeLayout rlMM;
	private RelativeLayout rlBig;
	private RelativeLayout rlSD;
	
	/**
	 * 启动活动的入口方法
	 * params
	 * context:活动调用的上下文环境
	 * myFolderPath:我的文件夹的根路径
	 * REQUEST:startActivityForResult的请求码
	 * resultTag:onActivityResult中intent.putExtra返回的数据别名
	 * */
	public static void actionStart(Context context, String myFolderPath, int REQUEST, String resultTag) {
		Intent intent = new Intent(context, FileHomeActivity.class);
		intent.putExtra("myFolderPath", myFolderPath);
		intent.putExtra("request", REQUEST);
		intent.putExtra("result", resultTag);
		((Activity)context).startActivityForResult(intent, REQUEST);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
		initLayout();
	}
	
	private void initData() {
		Intent intent = getIntent();
		myFolderPath = intent.getStringExtra("myFolderPath");
		REQUEST = intent.getIntExtra("request", 0);
		resultTag = intent.getStringExtra("result");
		externalPaths = SDCardScanner.getExtSDCardPaths();
		initSelectedFiles();
	}
	
	public void initLayout() {
		setContentView(R.layout.activity_file_home);
		
		tvCancel = (TextView) findViewById(R.id.tv_file_home_cancel);
		rlMy = (RelativeLayout) findViewById(R.id.rl_file_home_my);
		rlMM = (RelativeLayout) findViewById(R.id.rl_file_home_mm);
		rlBig = (RelativeLayout) findViewById(R.id.rl_file_home_big);
		rlSD = (RelativeLayout) findViewById(R.id.rl_file_home_sd);
		
		initEvent();
	}
	
	private void initEvent() {
		tvCancel.setOnClickListener(this);
		rlMy.setOnClickListener(this);
		rlMM.setOnClickListener(this);
		rlBig.setOnClickListener(this);
		rlSD.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_file_home_cancel:
			onBackPressed();
			break;
		case R.id.rl_file_home_my:
			FileMyActivity.actionStart(instance, myFolderPath, REQUEST);
			break;
		case R.id.rl_file_home_mm:
			FileFolderActivity.actionStart(instance, Environment.getRootDirectory().getParent(), REQUEST);
			break;
		case R.id.rl_file_home_big:
			if (externalPaths != null && externalPaths.size() > 0) {
				FileFolderActivity.actionStart(instance, externalPaths.get(0), REQUEST);
			} else {
				Toast.makeText(instance, "大容量存储暂不可用！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rl_file_home_sd:
			if (externalPaths != null && externalPaths.size() > 1) {
				FileFolderActivity.actionStart(instance, externalPaths.get(1), REQUEST);
			} else {
				Toast.makeText(instance, "SD卡暂不可用！", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST) {
				//因为所有选择的文件都放在了SelectedFiles全局变量中，所以此处data不会有任何数据
				JSONArray array = new JSONArray();
				for (File file : SelectedFiles.files.values()) {
					array.put(file.getAbsolutePath());
				}
				Intent intent = new Intent();
				intent.putExtra(resultTag, array.toString());
				setResult(Activity.RESULT_OK, intent);
				clearSelectedFiles();
				finish();
			}
		}
	}
	
	/**
	 * 初始化全局变量SelectedFiles中的数据
	 * */
	private void initSelectedFiles() {
		SelectedFiles.files = new HashMap<String, File>();
		SelectedFiles.totalFileSize = 0;
	}
	
	/**
	 * 清空全局变量SelectedFiles中的数据
	 * */
	private void clearSelectedFiles() {
		SelectedFiles.files = null;
		SelectedFiles.totalFileSize = 0;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(Activity.RESULT_CANCELED, intent);
		clearSelectedFiles();
		finish();
    }

}

