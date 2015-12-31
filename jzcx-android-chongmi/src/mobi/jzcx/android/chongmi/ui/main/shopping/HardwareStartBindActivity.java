package mobi.jzcx.android.chongmi.ui.main.shopping;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;

public class HardwareStartBindActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, HardwareStartBindActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hardwarestartbind);
		initView();
	}

	private void initView() {
		initTitleBar();
		Button startbindBtn = (Button) findViewById(R.id.startbind_bindbtn);
		startbindBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HardwarePetListActivity.startActivity(mActivity);
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.hardware_startbind_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HardwareStartBindActivity.this.finish();
			}
		});
	}
}
