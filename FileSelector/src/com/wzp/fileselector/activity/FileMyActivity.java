package com.wzp.fileselector.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wzp.fileselector.R;
import com.wzp.fileselector.adapter.FileMyListAdapter;
import com.wzp.fileselector.bean.MyFile;
import com.wzp.fileselector.bean.SelectedFiles;
import com.wzp.fileselector.util.FileComparator;
import com.wzp.fileselector.util.FileHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileMyActivity extends Activity implements OnClickListener, OnItemClickListener {
	private FileMyActivity instance = this;
	private List<MyFile> data = new ArrayList<MyFile>();
	private String root;// 待扫描的文件夹路径
	private FileMyListAdapter adapter;
	private ImageView ivBack;
	private TextView tvTitle;
	private TextView tvTip;
	private ListView lvContent;
	private TextView tvTotal;
	private Button btnOK;
	
	/**
	 * 启动活动的入口方法
	 * */
	public static void actionStart(Context context, String root, int REQUEST) {
		Intent intent = new Intent(context, FileMyActivity.class);
		intent.putExtra("root", root);//添加首次加载的根路径
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
		root = intent.getStringExtra("root");
	}
	
	public void initLayout() {
		setContentView(R.layout.activity_file_my);
		
		ivBack = (ImageView) findViewById(R.id.iv_file_my_back);
		tvTitle = (TextView) findViewById(R.id.tv_file_my_title);
		tvTip = (TextView) findViewById(R.id.tv_file_my_tip);
		lvContent = (ListView) findViewById(R.id.lv_file_my_content);
		tvTotal = (TextView) findViewById(R.id.tv_file_my_total);
		btnOK = (Button) findViewById(R.id.btn_file_my_ok);
		
		initEvent();
	}
	
	private void initEvent() {
		ivBack.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		
		initListView();
		refreshTextView();
	}
	
	/**
	 * 初始化ListView
	 * */
	private void initListView() {
		List<File> fileList = getFileList(root);
		if (fileList.size() == 0) {
			tvTip.setVisibility(View.VISIBLE);
		} else {
			Collections.sort(fileList, new FileComparator());//文件排序
			for (File file : fileList) {
				MyFile temp = new MyFile();
				if (SelectedFiles.files.containsKey(file.getAbsolutePath())) {//若文件已选中过，则标记为选中
					temp.checked = true;
				}
				temp.file = file;
				data.add(temp);
			}
			adapter = new FileMyListAdapter(instance, data);
			lvContent.setAdapter(adapter);
			lvContent.setOnItemClickListener(this);
		}
	}
	
	/**
	 * 刷新TextView
	 * */
	private void refreshTextView() {
		if (SelectedFiles.files.size() != 0) {
			tvTitle.setText("已选" + SelectedFiles.files.size() + "个");
			tvTotal.setText("已选" + FileHelper.FormetFileSize(SelectedFiles.totalFileSize));
		} else {
			tvTitle.setText("我的文件");
			tvTotal.setText("");
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_file_my_back:
			onBackPressed();
			break;
		case R.id.btn_file_my_ok:
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ImageView ivChecked = (ImageView) view.findViewById(R.id.iv_file_my_item_checked);
		MyFile file = data.get(position);
		file.checked = !file.checked;
		if (file.checked) {
			ivChecked.setImageResource(R.drawable.ic_checkbox_selected);
			SelectedFiles.files.put(file.file.getAbsolutePath(), file.file);//在全局静态变量中添加文件
			SelectedFiles.totalFileSize += file.file.length();//重新计算文件综合大小
		} else {
			ivChecked.setImageResource(R.drawable.ic_checkbox_normal);
			SelectedFiles.files.remove(file.file.getAbsolutePath());//在全局静态变量中移除文件
			SelectedFiles.totalFileSize -= file.file.length();//重新计算文件综合大小
		}
		refreshTextView();
	}
		
	/**
	 * 循环遍历文件夹获取文件夹下所有文件
	 * */
	private List<File> getFileList(String path) {
		File root = new File(path);
		List<File> files = new ArrayList<File>();
		if (root.exists()) {
			if(!root.isDirectory()){
				if (!root.getName().startsWith(".") && root.length() != 0) {//不添加隐藏文件和空文件
					files.add(root);
				}
		    } else {
		        File[] subFiles = root.listFiles();
		        if (subFiles != null) {
		        	for(File file : subFiles){
			            files.addAll(getFileList(file.getAbsolutePath()));
			        }
		        }
		    }
		}
	    return files;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();//等同于直接finish();
    }
	
}