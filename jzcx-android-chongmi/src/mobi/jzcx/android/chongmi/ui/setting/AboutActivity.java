package mobi.jzcx.android.chongmi.ui.setting;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class AboutActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, AboutActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.about_ttb_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

	private void initData() {
		WebView webview = (WebView) findViewById(R.id.about_webview);
		webview.loadUrl("file:///android_asset/about.html");
	}
}
