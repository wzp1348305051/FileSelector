package com.wzp.fileselector.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.wzp.fileselector.R;
import com.wzp.fileselector.adapter.FileFolderListAdapter;
import com.wzp.fileselector.bean.MyFile;
import com.wzp.fileselector.bean.SelectedFiles;
import com.wzp.fileselector.util.FileComparator;
import com.wzp.fileselector.util.FileHelper;
import com.wzp.fileselector.util.EmptyFileFilter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileFolderActivity extends Activity implements OnClickListener, OnItemClickListener {
	private FileFolderActivity instance = this;
	private ArrayList<MyFile> data = new ArrayList<MyFile>();
	private FileFolderListAdapter adapter;
	private String root;//进入文件夹时的根路径，用于判断按返回键时何时返回上一界面以及后退到上一文件夹
	private String currentPath;//当前选中的文件夹路径
	private ImageView ivBack;
	private TextView tvTitle;
	private TextView tvPath;
	private TextView tvTip;
	private ListView lvContent;
	private TextView tvTotal;
	private Button btnOK;
	
	/**
	 * 启动活动的入口方法
	 * */
	public static void actionStart(Context context, String root, int REQUEST) {
		Intent intent = new Intent(context, FileFolderActivity.class); 
		intent.putExtra("root", root);//添加首次加载的根路径
		((Activity)context).startActivityForResult(intent, REQUEST);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		root = getIntent().getStringExtra("root");
		currentPath = root;
		initLayout();
	}
	
	public void initLayout() {
		setContentView(R.layout.activity_file_folder);
		
		ivBack = (ImageView) findViewById(R.id.iv_file_folder_back);
		tvTitle = (TextView) findViewById(R.id.tv_file_folder_title);
		tvPath = (TextView) findViewById(R.id.tv_file_folder_path);
		tvTip = (TextView) findViewById(R.id.tv_file_folder_tip);
		lvContent = (ListView) findViewById(R.id.lv_file_folder_content);
		tvTotal = (TextView) findViewById(R.id.tv_file_folder_total);
		btnOK = (Button) findViewById(R.id.btn_file_folder_ok);
		
		initEvent();
	}
	
	private void initEvent() {
		ivBack.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		
		refreshView();
		refreshTextView();
	}
	
	/**
	 * 刷新界面
	 * */
	private void refreshView() {
		//刷新列表
		refreshData();
		if (adapter == null) {
			adapter = new FileFolderListAdapter(instance, data);
			lvContent.setAdapter(adapter);
			lvContent.setOnItemClickListener(this);
		} else {
			adapter.notifyDataSetChanged();
		}
		//刷新tvTip
		if (adapter.getCount() == 0) {
			tvTip.setVisibility(View.VISIBLE);
		} else {
			tvTip.setVisibility(View.INVISIBLE);
		}
		//刷新tvPath
		tvPath.setText(currentPath);
	}
	
	/**
	 * 刷新TextView
	 * */
	private void refreshTextView() {
		if (SelectedFiles.files.size() != 0) {//选中文件
			tvTitle.setText("已选" + SelectedFiles.files.size() + "个");
			tvTotal.setText("已选" + FileHelper.FormetFileSize(SelectedFiles.totalFileSize));
		} else if (currentPath.equals(root)) {//未选中任何文件且位于根目录位置
			tvTitle.setText("全部文件");
			tvTotal.setText("");
		} else {//未选中任何文件且不位于根目录位置
			tvTitle.setText("返回上级");
			tvTotal.setText("");
		}
	}
	
	/**
	 * 文件数据刷新
	 */
	private void refreshData() {
		data.clear();
		List<File> fileList = getFileList(currentPath);
		if (fileList != null) {
			for (File file : fileList) {
				MyFile temp = new MyFile();
				if (SelectedFiles.files.containsKey(file.getAbsolutePath())) {//若文件已选中过，则标记为选中
					temp.checked = true;
				}
				temp.file = file;
				data.add(temp);
			}
		}
	}
	
	/**
	 * 获取当前路径下的文件列表
	 */
	private List<File> getFileList(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] fileArray = file.listFiles(new EmptyFileFilter());//不显示隐藏文件
			if (fileArray != null && fileArray.length != 0) {//不显示空文件夹，此处不做判断，有些情况会崩溃
				List<File> fileList = Arrays.asList(fileArray);//将File[]转换为List<File>,以便调用Collections.sort进行排序
				Collections.sort(fileList, new FileComparator());//文件排序
				return fileList;
			}
		}
		return null;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_file_folder_back:
			onBackPressed();
			break;
		case R.id.btn_file_folder_ok:
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyFile file = data.get(position);
		if (file.file.isDirectory()) {
			currentPath = file.file.getAbsolutePath();
			refreshView();
		} else {
			//ListView局部刷新
			file.checked = !file.checked;
			ImageView ivChecked = (ImageView) view.findViewById(R.id.iv_file_folder_item_checked);
			if (file.checked) {
				ivChecked.setImageResource(R.drawable.ic_checkbox_selected);
				SelectedFiles.files.put(file.file.getAbsolutePath(), file.file);//在全局静态变量中添加文件
				SelectedFiles.totalFileSize += file.file.length();//重新计算文件综合大小
			} else {
				ivChecked.setImageResource(R.drawable.ic_checkbox_normal);
				SelectedFiles.files.remove(file.file.getAbsolutePath());//在全局静态变量中移除文件
				SelectedFiles.totalFileSize -= file.file.length();//重新计算文件综合大小
			}
		}
		refreshTextView();
	}
	
	@Override
	public void onBackPressed() {
		if (!currentPath.equals(root)) {//若未到达根目录，则返回文件上层目录
			File file = new File(currentPath);
			currentPath = file.getParentFile().getAbsolutePath();
			refreshView();
		} else {//到达根目录，直接返回
			super.onBackPressed();//等同于直接finish();
		}
		refreshTextView();
    }
	
}
