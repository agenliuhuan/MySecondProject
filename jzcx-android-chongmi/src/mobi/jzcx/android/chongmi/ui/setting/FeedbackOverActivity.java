package mobi.jzcx.android.chongmi.ui.setting;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FeedbackOverActivity extends BaseExActivity {
	TitleBarHolder mTitleBar;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, FeedbackOverActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedbackover);
		initView();
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.feedback_over_title));
		mTitleBar.mRightTv.setText(getString(R.string.feedback_over_righttv));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FeedbackOverActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackOverActivity.this.finish();
			}
		});
	}
}
