package mobi.jzcx.android.chongmi.ui.main.answer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;

public class AnswerDoctorActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, AnswerDoctorActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answerdoctor);
		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.officialdoctor_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerDoctorActivity.this.finish();
			}
		});
	}

	private void initData() {

	}
}
