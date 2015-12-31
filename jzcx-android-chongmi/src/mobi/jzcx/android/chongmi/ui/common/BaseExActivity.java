package mobi.jzcx.android.chongmi.ui.common;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.core.mvc.BaseActivity;
import mobi.jzcx.android.core.utils.SystemBarTintManager;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

public class BaseExActivity extends BaseActivity {

	private boolean mIsActivity = true;
	protected InternalReceiver internalReceiver;
	private YSWaitingDialog mWaitingDialog;
	protected boolean mSetStatusBar = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);

	}

	public void setStatusBarColor(int colorID) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setStatusBarColorLOLLIPOP(colorID);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(getResources().getColor(colorID));
		}
	}
	@TargetApi(21)
	private void setStatusBarColorLOLLIPOP(int colorID) {
		Window window = getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(getResources().getColor(colorID));
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onResume() {
		super.onResume();

		App.getInstance().setForegroundActivity(this);

		if (!mIsActivity) {
			mIsActivity = true;
		}
		if (mSetStatusBar) {
			setStatusBarColor(R.color.comm_title_color);
		} else {
			setStatusBarColor(R.color.transparent);
		}
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		hideSoftKeyboard();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissWaitingDialog();
	}

	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		// intentfilter.addAction(CASIntent.ACTION_SERVICE_DESTORY);
		// intentfilter.addAction(CASIntent.ACTION_FORCE_DEACTIVE);
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	protected final void unregisterReceiver() {
		unregisterReceiver(internalReceiver);
	}

	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getAction() == null) {
				return;
			}
			handleReceiver(context, intent);
		}

	}

	protected void handleReceiver(Context context, Intent intent) {

	}

	public void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			View localView = this.getCurrentFocus();
			if (localView != null && localView.getWindowToken() != null) {
				IBinder windowToken = localView.getWindowToken();
				inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
			}
		}
	}

	public void showWaitingDialog() {
		if (null == mWaitingDialog) {
			mWaitingDialog = new YSWaitingDialog(this);
		}

		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showOrUpdateWaitingDialog(String message) {

		if (null == mWaitingDialog) {
			mWaitingDialog = new YSWaitingDialog(this);
		}

		mWaitingDialog.setProHintStr(message);

		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showOrUpdateWaitingDialog(int resId) {
		showOrUpdateWaitingDialog(getString(resId));
	}

	public void updateWaitingDialogResource(int resId) {
		if (null != mWaitingDialog) {
			ProgressBar pb = (ProgressBar) mWaitingDialog.findViewById(R.id.unified_loading_view_circle);
			if (pb != null) {
				mWaitingDialog.setCanceledOnTouchOutside(true);
				pb.setIndeterminateDrawable(null);
				pb.setBackgroundResource(resId);
			}
		}
	}

	public void dismissWaitingDialog() {

		if (null != mWaitingDialog && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
			mWaitingDialog = null;
		}
	}

	public boolean isWaitingDialogShowing() {
		return null != mWaitingDialog && mWaitingDialog.isShowing();
	}

	protected UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

}
