package mobi.jzcx.android.chongmi.ui.group;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.JoinGroupObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class JoinGroupReasonActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	EditText edt;
	static ActivityObject activeObj;

	public static void startActivity(Context context, ActivityObject activeObj) {
		ActivityUtils.startActivity(context, JoinGroupReasonActivity.class);
		if (activeObj != null) {
			JoinGroupReasonActivity.activeObj = activeObj;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_group);
		initView();

	}

	protected void onResume() {
		super.onResume();
		initData();

	}

	private void initData() {
		String hint = getString(R.string.joingroup_hinttext);
		hint = String.format(hint, activeObj.getTitle());
		edt.setHint(hint);
		edt.requestFocus();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(edt, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 300);

	}
	private void initView() {
		initTitleBar();
		edt = (EditText) findViewById(R.id.edt_joinGroup);
		edt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.length() == 50) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_joinreason_max_error));
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
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.joingroup_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.mRightTv.setText(getString(R.string.joingroup_title_righttext));
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (edt.getText().toString().trim().length() == 0) {
					String reason = String.format(getString(R.string.joingroup_hinttext), activeObj.getTitle());
					JoinGroupObject jobj = new JoinGroupObject();
					jobj.setActId(activeObj.getActivityId());
					jobj.setReason(reason);
					sendMessage(YSMSG.REQ_JOINACTIVITY, 0, 0, jobj);
					// YSToast.showToast(mActivity,
					// getString(R.string.toast_joinreason_notnull));
				} else if (edt.getText().toString().trim().length() < 6) {
					YSToast.showToast(mActivity, getString(R.string.toast_joinreason_max_error));
				} else {
					String reason = edt.getText().toString().trim();
					JoinGroupObject jobj = new JoinGroupObject();
					jobj.setActId(activeObj.getActivityId());
					jobj.setReason(reason);
					sendMessage(YSMSG.REQ_JOINACTIVITY, 0, 0, jobj);
				}
			}
		});
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_JOINACTIVITY :
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.toast_joinreason_success));
					JoinGroupReasonActivity.this.finish();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}

				break;
		}
	}
}
