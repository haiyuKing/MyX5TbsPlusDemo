package com.why.project.myx5tbsplusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tencent.smtt.sdk.QbSdk;

import tbsplus.tbs.tencent.com.tbsplus.TbsPlus;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//初始化
		QbSdk.initX5Environment(MainActivity.this, null);

		initEvents();
	}

	private void initEvents() {
		findViewById(R.id.btn_openUrl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = "http://www.baidu.com";
				TbsPlus.openUrl(MainActivity.this, url, TbsPlus.eTBSPLUS_SCREENDIR.eTBSPLUS_SCREENDIR_SENSOR);
			}
		});

		findViewById(R.id.btn_openfile).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//请求下面的地址会打开第三方浏览器软件，我这边打开的是QQ浏览器（可能是默认的浏览器）
				/*String url = "http://www.beijing.gov.cn/zhuanti/ggfw/htsfwbxzzt/shxfl/fw/P020150720516332194302.doc";
				TbsPlus.openUrl(MainActivity.this, url, TbsPlus.eTBSPLUS_SCREENDIR.eTBSPLUS_SCREENDIR_SENSOR);*/

				//TBS目前只支持加载本地文件。所以远程文件需要先下载，后用TBS加载文件显示。
				//这里没有演示下载过程，而是直接把上面的文件下载到手机中【下面的路径根据实际情况修改】
				String filePath = "/storage/emulated/0/QQBrowser/文档/P020150720516332194302.doc";
				String fileName = "P020150720516332194302.doc";
				DisplayFileActivity.openDispalyFileActivity(MainActivity.this,filePath,fileName);

			}
		});

		findViewById(R.id.btn_openvideo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//直播，点播视频都可以播放
				String url = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";
				TbsPlus.openUrl(MainActivity.this, url, TbsPlus.eTBSPLUS_SCREENDIR.eTBSPLUS_SCREENDIR_SENSOR);
			}
		});
	}


}
