package mobi.jzcx.android.chongmi.ui.main.homepage;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ReportOverActivity extends BaseExActivity {
	TitleBarHolder mTitleBar;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, ReportOverActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reportover);
		initView();
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.report_overttb_title));
		mTitleBar.mRightTv.setText(R.string.report_overtv);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReportOverActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReportOverActivity.this.finish();
			}
		});

	}
}
