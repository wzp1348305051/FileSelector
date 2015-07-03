package com.wzp.fileselector.util;

import java.text.DecimalFormat;

import com.wzp.fileselector.R;

public class FileHelper {
	
	/**
	 * 根据文件名后缀返回图片资源id
	 * */
	public static int getImageBySuffix(String suffix) {
		int resource = R.drawable.ic_file_default;
		if (suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx")) {
			resource = R.drawable.ic_file_doc;
		} else if (suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")) {
			resource = R.drawable.ic_file_xls;
		} else if (suffix.equalsIgnoreCase("ppt") || suffix.equalsIgnoreCase("pptx")) {
			resource = R.drawable.ic_file_ppt;
		} else if (suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg") || suffix.equalsIgnoreCase("bmp")) {
			resource = R.drawable.ic_file_img;
		} else if (suffix.equalsIgnoreCase("pdf")) {
			resource = R.drawable.ic_file_pdf;
		} else if (suffix.equalsIgnoreCase("txt")) {
			resource = R.drawable.ic_file_txt;
		} else if (suffix.equalsIgnoreCase("zip") || suffix.equalsIgnoreCase("rar")) {
			resource = R.drawable.ic_file_rar;
		} else if (suffix.equalsIgnoreCase("dps")) {
			resource = R.drawable.ic_file_dps;
		} else if (suffix.equalsIgnoreCase("mp3") || suffix.equalsIgnoreCase("wav") || suffix.equalsIgnoreCase("ape") || suffix.equalsIgnoreCase("flac") || suffix.equalsIgnoreCase("wave") || suffix.equalsIgnoreCase("amr") || suffix.equalsIgnoreCase("aac") || suffix.equalsIgnoreCase("mid")) {
			resource = R.drawable.ic_file_music;
		} else if (suffix.equalsIgnoreCase("mp4") || suffix.equalsIgnoreCase("avi") || suffix.equalsIgnoreCase("rvmb") || suffix.equalsIgnoreCase("mkv") || suffix.equalsIgnoreCase("rm") || suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("3gp") || suffix.equalsIgnoreCase("vob") || suffix.equalsIgnoreCase("mpeg") || suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("mov") || suffix.equalsIgnoreCase("flv")) {
			resource = R.drawable.ic_file_video;
		} else if (suffix.equalsIgnoreCase("apk")) {
			resource = R.drawable.ic_file_apk;
		}
		return resource;
	}
	
	/**
	 * 转换文件大小
	 * */
	public static String FormetFileSize(long size) {//转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String result = "";
		if (size < 1024) {
			result = df.format((double) size) + "B";
		} else if (size < 1048576) {
			result = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			result = df.format((double) size / 1048576) + "M";
		} else {
			result = df.format((double) size / 1073741824) + "G";
		}
		return result;
	}
	
}
