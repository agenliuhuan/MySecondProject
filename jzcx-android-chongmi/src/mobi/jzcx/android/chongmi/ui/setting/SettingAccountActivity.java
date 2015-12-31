package mobi.jzcx.android.chongmi.ui.setting;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingAccountActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, SettingAccountActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingaccount);
		initView();

	}

	private void initView() {
		initTitleBar();

		PercentRelativeLayout psdRL = (PercentRelativeLayout) findViewById(R.id.layout_settingaccount_password);
		PercentRelativeLayout phoneRL = (PercentRelativeLayout) findViewById(R.id.layout_settingaccount_clearcache);
		psdRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PasswordActivity.startActivity(mActivity);
			}
		});
		phoneRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePhoneActivity.startActivity(mActivity);
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.settingaccount_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SettingAccountActivity.this.finish();
			}
		});
	}

}
