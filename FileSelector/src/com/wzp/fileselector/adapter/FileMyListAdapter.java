package com.wzp.fileselector.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.wzp.fileselector.R;
import com.wzp.fileselector.bean.MyFile;
import com.wzp.fileselector.util.FileHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileMyListAdapter extends BaseAdapter {
	private List<MyFile> data;
	private LayoutInflater inflater;
	private class ViewHolder {
		ImageView ivChecked;
		ImageView ivPreview;
		TextView tvName;
		TextView tvSize;
		TextView tvDate;
	}
	
	public FileMyListAdapter(Context context, List<MyFile> data) {
		this.data = data;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return (null == data) ? 0 : data.size();
	}

	@Override
	public MyFile getItem(int position) {
		if (data.get(position) != null) {
			return data.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view  = null;
		ViewHolder holder = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.activity_file_my_item, null);
			holder = new ViewHolder();
			holder.ivChecked = (ImageView) view.findViewById(R.id.iv_file_my_item_checked);
			holder.ivPreview = (ImageView) view.findViewById(R.id.iv_file_my_item_preview);
			holder.tvName = (TextView) view.findViewById(R.id.tv_file_my_item_name);
			holder.tvSize = (TextView) view.findViewById(R.id.tv_file_my_item_size);
			holder.tvDate = (TextView) view.findViewById(R.id.tv_file_my_item_date);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		MyFile file = getItem(position);
		
		if (file != null) {
			if (file.checked) {//设置文件是否选择
				holder.ivChecked.setImageResource(R.drawable.ic_checkbox_selected);
			} else {
				holder.ivChecked.setImageResource(R.drawable.ic_checkbox_normal);
			}
			String fileName = file.file.getName();
			holder.ivPreview.setImageResource(FileHelper.getImageBySuffix(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())));
			holder.tvName.setText(fileName);//文件名
			holder.tvSize.setText(FileHelper.FormetFileSize(file.file.length()));//文件大小
			Date date = new Date(file.file.lastModified());
			SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
			holder.tvDate.setText(format.format(date));//文件日期
		}
		
		return view;
	}
	
}
