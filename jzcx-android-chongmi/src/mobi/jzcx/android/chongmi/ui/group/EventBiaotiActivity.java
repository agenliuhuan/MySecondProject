package mobi.jzcx.android.chongmi.ui.group;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class EventBiaotiActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;
	EditText edt;
	int resultCode = 100;
	static String title;

	public static void startActivity(Context context, String title) {
		ActivityUtils.startActivity(context, EventBiaotiActivity.class);
		EventBiaotiActivity.title = title;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_bt);
		initView();

	}

	private void initView() {
		initTitleBar();
		edt = (EditText) findViewById(R.id.edt_event_title);
		if (!CommonTextUtils.isEmpty(title)) {
			edt.setText(title);
			edt.setSelection(title.length());
		}
		edt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.toString().trim().length() == 8) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_eventtitle_max_error));
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

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.createevent_titletext));
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
					YSToast.showToast(EventBiaotiActivity.this, getString(R.string.toast_eventtitle_notnull));
					return;
				}
				if (!checkNameEmpty()) {
					YSToast.showToast(EventBiaotiActivity.this, getString(R.string.toast_eventtitle_max_error));
					return;
				}
				Constant.eventTitle = edt.getText().toString().trim();
				finish();
				// String reg = "^[a-zA-Z0-9_\u4e00-\u9fa5\\s]+$";
				// Pattern p1 = Pattern.compile(reg);
				// Matcher m1 =
				// p1.matcher(edt.getEditableText().toString().trim());
				// if (!m1.matches()) {
				// YSToast.showToast(mActivity,
				// R.string.toast_eventtitle_format_error);
				// return;
				// } else {
				//
				// }
			}
		});
	}

	private boolean checkNameEmpty() {
		return edt != null && edt.getText().toString().trim().length() > 1 && edt.getText().toString().trim().length() < 9;
	}

}
