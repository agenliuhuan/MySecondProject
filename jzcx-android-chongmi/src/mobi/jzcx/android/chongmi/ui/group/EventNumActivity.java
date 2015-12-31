package mobi.jzcx.android.chongmi.ui.group;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class EventNumActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;
	EditText edt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_num);

		initView();
		initData();
	}

	private void initData() {
		edt = (EditText) findViewById(R.id.edt_event_num);
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.createevent_mebernumtext));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.mRightTv.setText(getString(R.string.createevent_titlerighttext));
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (checkNameEmpty()) {
					finish();
				} else {
					YSToast.showToast(mActivity, getString(R.string.toast_eventnum_notnull));
				}
			}
		});
	}

	private boolean checkNameEmpty() {
		return edt != null && edt.getText().toString().trim().length() > 0;
	}

}
