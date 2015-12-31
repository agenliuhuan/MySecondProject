package mobi.jzcx.android.chongmi.ui.group;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EventDescActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;
	EditText edt;
	static String desc;

	public static void startActivity(Context context, String desc) {
		ActivityUtils.startActivity(context, EventDescActivity.class);
		EventDescActivity.desc = desc;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_desc);

		initView();
		initData();
	}

	private void initData() {
		edt = (EditText) findViewById(R.id.edt_event_desc);
		if (!CommonTextUtils.isEmpty(desc)) {
			edt.setText(desc);
			edt.setSelection(desc.length());
		}
		edt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.length() == 100) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_eventdesc_max_error));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				temp = arg0;

			}
		});
		edt.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edt, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void initView() {
		initTitleBar();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.createevent_desctext));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.mRightTv.setText(getString(R.string.createevent_titlerighttext));
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (edt.getText().toString().trim().length() == 0) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventdesc_notnull));
					return;
				}
				if (checkNameEmpty()) {
					Constant.eventDesc = edt.getText().toString().trim();
					finish();
				} else {
					YSToast.showToast(mActivity, getString(R.string.toast_eventdesc_max_error));
				}
			}
		});
	}

	private boolean checkNameEmpty() {
		return edt != null && edt.getText().toString().trim().length() > 9
				&& edt.getText().toString().trim().length() < 101;
	}

}
