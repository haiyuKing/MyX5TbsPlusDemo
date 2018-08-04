package com.why.project.myx5tbsplusdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.sdk.TbsReaderView;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HaiyuKing
 * Used 调用腾讯浏览服务预览文件
 */

public class DisplayFileActivity extends AppCompatActivity{

	private static final String TAG = DisplayFileActivity.class.getSimpleName();

	private TbsReaderView mTbsReaderView;//用于预览文件5-1

	private String filePath = "";
	private String fileName = "";

	public static void openDispalyFileActivity(Context context,String filePath,String fileName){
		Intent intent = new Intent(context,DisplayFileActivity.class);
		intent.putExtra("filepath",filePath);
		intent.putExtra("filename",fileName);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_displayfile);

		initTbsReaderView();//用于预览文件5-2

		Intent intent = getIntent();
		filePath = intent.getStringExtra("filepath");
		fileName = intent.getStringExtra("filename");

		onePermission();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTbsReaderView.onStop();//用于预览文件5-5
	}

	//初始化TbsReaderView 5-3
	private void initTbsReaderView(){
		mTbsReaderView = new TbsReaderView(DisplayFileActivity.this, new TbsReaderView.ReaderCallback(){
			@Override
			public void onCallBackAction(Integer integer, Object o, Object o1) {
				//ReaderCallback 接口提供的方法可以不予处理（目前不知道有什么用途，但是一定要实现这个接口类）
			}
		});
		RelativeLayout rootRl = (RelativeLayout) findViewById(R.id.root_layout);
		rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}
	//预览文件5-4
	/**
	 * filePath :文件路径。格式为 android 本地存储路径格式，例如：/sdcard/Download/xxx.doc. 不支持 file:///格式。暂不支持在线文件。
	 * fileName : 文件的文件名（含后缀）*/
	private void displayFile(String filePath,String fileName) {
		Bundle bundle = new Bundle();
		bundle.putString("filePath", filePath);
		bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
		boolean result = mTbsReaderView.preOpen(parseFormat(fileName), false);
		if (result) {
			mTbsReaderView.openFile(bundle);
		}
	}

	private String parseFormat(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**只有一个运行时权限申请的情况*/
	private void onePermission(){
		RxPermissions rxPermissions = new RxPermissions(DisplayFileActivity.this); // where this is an Activity instance
		rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE) //权限名称，多个权限之间逗号分隔开
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) throws Exception {
						Log.e(TAG, "{accept}granted=" + granted);//执行顺序——1【多个权限的情况，只有所有的权限均允许的情况下granted==true】
						if (granted) { // 在android 6.0之前会默认返回true
							// 已经获取权限
							Toast.makeText(DisplayFileActivity.this, "已经获取权限", Toast.LENGTH_SHORT).show();
						} else {
							// 未获取权限
							Toast.makeText(DisplayFileActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(TAG,"{accept}");//可能是授权异常的情况下的处理
					}
				}, new Action() {
					@Override
					public void run() throws Exception {
						Log.e(TAG,"{run}");//执行顺序——2
						displayFile(filePath,fileName);
					}
				});
	}
}
