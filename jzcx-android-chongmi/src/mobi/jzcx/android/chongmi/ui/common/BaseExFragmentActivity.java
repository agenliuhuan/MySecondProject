package mobi.jzcx.android.chongmi.ui.common;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.core.mvc.BaseFragmentActivity;
import mobi.jzcx.android.core.utils.SystemBarTintManager;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BaseExFragmentActivity extends BaseFragmentActivity {

	protected boolean mIsFragmentVisible;
	protected boolean mPause = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIsFragmentVisible = true;
		App.getInstance().addActivity(this);

	}

	public void setStatusBarColor(int colorID) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setStatusBarColorLOLLIPOP(colorID);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// tintManager.setNavigationBarTintEnabled(true);
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
		// window.setNavigationBarColor(Color.TRANSPARENT);
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
	public void onResume() {
		super.onResume();
		setStatusBarColor(R.color.comm_title_color);
		mPause = false;
		App.getInstance().setForegroundActivity(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mPause = true;

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
