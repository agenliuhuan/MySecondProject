package mobi.jzcx.android.chongmi.ui.setting;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ChangePhoneObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.chongmi.view.ClearEditText;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChangePhoneActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	TextView oldPhoneTV;
	ClearEditText newphone;
	ClearEditText mCheckNumber;
	Button checkBtn;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, ChangePhoneActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changephone);
		initView();
	}

	private void initView() {
		initTitleBar();
		oldPhoneTV = (TextView) findViewById(R.id.changephone_oldphone);
		newphone = (ClearEditText) findViewById(R.id.changephone_newphone);
		mCheckNumber = (ClearEditText) findViewById(R.id.changephone_check);
		checkBtn = (Button) findViewById(R.id.changephone_resendBtn);
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			if (!CommonTextUtils.isEmpty(myself.getPhone())) {
				oldPhoneTV.setText(myself.getPhone());
			}
		}

		newphone.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				selectionStart = newphone.getSelectionStart();
				selectionEnd = newphone.getSelectionEnd();
				if (temp.length() > 0) {
					if (temp.charAt(0) != '1') {
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
						arg0.clear();
						newphone.setText(arg0);

					} else if (temp.length() > 11) {
						arg0.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						newphone.setText(arg0);
						newphone.setSelection(tempSelection);
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
					}
					if (temp.length() == 11) {
						checkBtn.setEnabled(true);
					} else {
						checkBtn.setEnabled(false);
					}
				} else {
					checkBtn.setEnabled(false);
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
		checkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (newphone.getText().length() != 11) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				if (newphone.getText().charAt(0) != '1') {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}

				sendCheckNum();
			}

		});

	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.changephone_title));
		mTitleBar.mRightTv.setText(R.string.changephone_righttext);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePhoneActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (newphone.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_notnull);
					return;
				}

				if (mCheckNumber.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_check_length_error);
					return;
				}

				if (newphone.getText().length() != 11) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				if (newphone.getText().charAt(0) != '1') {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}

				nextStup();
			}
		});
	}

	protected void nextStup() {
		ChangePhoneObject registeObj = new ChangePhoneObject();
		String phone = newphone.getText().toString();
		String checknum = mCheckNumber.getText().toString();
		registeObj.setPhone(phone);
		registeObj.setCode(checknum);
		sendMessage(YSMSG.REQ_CHANGEPHONE, 0, 0, registeObj);
		showWaitingDialog();
	}

	private void sendCheckNum() {
		sendMessage(YSMSG.REQ_CHANGEPHONESMS, 0, 0, newphone.getText().toString());
		mTickCount = 60;
		handler.post(runnable);
	}

	int mTickCount = 60;
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			Message message = Message.obtain();
			message.what = 1;
			handler.sendMessage(message);
			mTickCount--;
			if (mTickCount >= 0) {
				mHandler.postDelayed(this, 1000);
			}
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (1 == msg.what) {
				if (null != checkBtn) {
					if (mTickCount > 0) {
						String tips = String.format(getString(R.string.fogot_tv_resendcount), mTickCount);
						checkBtn.setText(tips);
						checkBtn.setTextColor(getResources().getColor(R.color.comm_gray));
						checkBtn.setEnabled(false);
					} else {
						checkBtn.setEnabled(true);
						checkBtn.setText(getString(R.string.registe_tv_resend));
						checkBtn.setTextColor(getResources().getColor(R.color.white));
						handler.removeCallbacks(runnable);
					}
				}
			}
		}
	};

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_CHANGEPHONESMS : {
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.toast_vcodesend_success));
				} else {
					mTickCount = 0;
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			case YSMSG.RESP_CHANGEPHONE :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					App.getInstance().clearActivity();
					CoreModel.getInstance().setMyself(null);
					LoginActivity.startActivity(mActivity, newphone.getText().toString());
					YSToast.showToast(this, R.string.toast_phone_modify_succeed);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}

	};

}
