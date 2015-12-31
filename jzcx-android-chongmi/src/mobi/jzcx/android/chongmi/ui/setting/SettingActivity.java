package mobi.jzcx.android.chongmi.ui.setting;

import java.text.NumberFormat;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.ui.register.RegisteAttentionActivity;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

@SuppressLint("NewApi")
public class SettingActivity extends BaseExActivity implements OnClickListener {
	protected TitleBarHolder mTitleBar = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, SettingActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		initData();

		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
					case UpdateStatus.Yes : // has update
						UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
						break;
					case UpdateStatus.No : // has no update
						// Toast.makeText(mContext, "没有更新",
						// Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi : // none wifi
						// Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新",
						// Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout : // time out
						// Toast.makeText(mContext, "超时",
						// Toast.LENGTH_SHORT).show();
						break;
				}
			}

		});

	}

	private void initView() {
		initTitleBar();
		RelativeLayout passwordRL = (RelativeLayout) findViewById(R.id.layout_setting_password);
		RelativeLayout exitRL = (RelativeLayout) findViewById(R.id.layout_setting_exit);
		RelativeLayout clearcacheRL = (RelativeLayout) findViewById(R.id.layout_setting_clearcache);
		RelativeLayout goodRL = (RelativeLayout) findViewById(R.id.layout_setting_good);
		RelativeLayout feedbackRL = (RelativeLayout) findViewById(R.id.layout_setting_feedback);
		RelativeLayout versionRL = (RelativeLayout) findViewById(R.id.layout_setting_version);
		RelativeLayout aboutRL = (RelativeLayout) findViewById(R.id.layout_setting_about);

		passwordRL.setOnClickListener(this);
		exitRL.setOnClickListener(this);
		clearcacheRL.setOnClickListener(this);
		goodRL.setOnClickListener(this);
		feedbackRL.setOnClickListener(this);
		versionRL.setOnClickListener(this);
		aboutRL.setOnClickListener(this);

		if (CoreModel.getInstance().getMyself() == null) {
			passwordRL.setVisibility(View.GONE);
			exitRL.setVisibility(View.GONE);
		}
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.setting_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SettingActivity.this.finish();
			}
		});
	}

	private void initData() {
		TextView cacheTv = (TextView) findViewById(R.id.tv_cache);
		long size = FileUtils.getFolderSize(FileUtils.COVER);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		String filesize = format.format(size / 1048576);
		cacheTv.setText(filesize + "MB");

	}

	private void logout() {
		showOrUpdateWaitingDialog(getString(R.string.logout_waitting_text));
		sendEmptyMessage(YSMSG.REQ_LOGOUT);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_LOGOUT : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					IMInitHelper.getInstance().logout(false, null);
					App.getInstance().clearActivity();
					CoreModel.getInstance().setMyself(null);
					PreferencesUtils.setUserToken("");
					LoginActivity.startActivity(mActivity, PreferencesUtils.getLoginPhone());
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			default :
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_setting_password :
				SettingAccountActivity.startActivity(mActivity);
				break;
			case R.id.layout_setting_exit :
				showLogoutDialog();
				break;
			case R.id.layout_setting_clearcache :
				// DataCleanManager.cleanApplicationData(mActivity, null);
				showClearDialog();
				break;
			case R.id.layout_setting_good :
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity(intent);
				break;
			case R.id.layout_setting_feedback :
				// FeedbackActivity.startActivity(mActivity);
				FeedbackAgent agent = new FeedbackAgent(mActivity);
				agent.startFeedbackActivity();
				break;
			case R.id.layout_setting_version :
				checkVersion();
				break;
			case R.id.layout_setting_about :
				AboutActivity.startActivity(mActivity);
				// RegisteAttentionActivity.startActivity(mActivity);
				break;
			default :
				break;
		}
	}

	private void showLogoutDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.logout_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						logout();
					}
				});
	}

	private void showClearDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.clearcache_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (FileUtils.clearDirectory(FileUtils.COVER)) {
							initData();
							YSToast.showToast(mActivity, getString(R.string.toast_clearover_text));
						}
					}
				});
	}

	private void checkVersion() {
		UmengUpdateAgent.update(this);
	}

	private void startDownload() {
		DownloadManager downloadManager = (DownloadManager) getSystemService(SettingActivity.DOWNLOAD_SERVICE);
		Request request = new DownloadManager.Request(
				Uri.parse("http://download.geo.drweb.com/pub/drweb/android/light/drweb-900-android-light.apk"));
		request.setMimeType("application/vnd.android.package-archive");
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "drweb.apk");
		downloadManager.enqueue(request);
	}
}
