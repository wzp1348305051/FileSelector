package com.wzp.fileselector.activity;

import org.json.JSONArray;
import org.json.JSONException;

import com.wzp.fileselector.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private MainActivity instance = this;
	private final int REQUEST = 100;
	private TextView tvContent;
	private Button btnSelect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLayout();
	}

	private void initLayout() {
		setContentView(R.layout.activity_main);
		
		tvContent = (TextView) findViewById(R.id.tv_main_content);
		btnSelect = (Button) findViewById(R.id.btn_main_select);
		
		initEvent();
	}

	private void initEvent() {
		btnSelect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_select:
			FileHomeActivity.actionStart(instance, Environment.getDataDirectory().getAbsolutePath(), REQUEST, "files");
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			switch (requestCode) {
			case REQUEST:
				try {
					JSONArray array = new JSONArray(data.getStringExtra("files"));
					StringBuilder builder = new StringBuilder();
					int length = array.length();
					for (int i = 0; i < length; i++) {
						builder.append(array.get(i)).append("\r\n");
					}
					tvContent.setText(builder.toString());
				} catch (JSONException e) {
					
				}
				break;
			}
			break;
		case Activity.RESULT_CANCELED:
			break;
		}
	}
}
