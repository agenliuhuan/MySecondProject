package mobi.jzcx.android.chongmi.ui.login;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ResetPsdObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.InputUtils;
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
import android.widget.EditText;

public class FogotPsdActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	private EditText mPhoneNumber = null;
	private EditText mPassword = null;
	private EditText mCheckNumber = null;
	private Button checkBtn;
	private int smsId = 0;

	public static void startActivity(Context context, String phone) {
		ActivityUtils.startActivity(context, FogotPsdActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fogot);
		initView();
	}

	private void initView() {
		initTitleBar();
		mPhoneNumber = (EditText) this.findViewById(R.id.edt_fogot_phone);
		mPassword = (EditText) this.findViewById(R.id.edt_fogot_password);
		mCheckNumber = (EditText) this.findViewById(R.id.edt_fogot_check);
		checkBtn = (Button) findViewById(R.id.fogot_resendBtn);
		mPhoneNumber.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}
				selectionStart = mPhoneNumber.getSelectionStart();
				selectionEnd = mPhoneNumber.getSelectionEnd();
				if (temp.length() > 0) {
					if (temp.charAt(0) != '1') {
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
						arg0.clear();
						mPhoneNumber.setText(arg0);

					} else if (temp.length() > 11) {
						arg0.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						mPhoneNumber.setText(arg0);
						mPhoneNumber.setSelection(tempSelection);
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
					}
					if (temp.length() == 11) {
						mPassword.requestFocus();
						checkBtn.setEnabled(true);
					} else {
						checkBtn.setEnabled(false);
					}
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
		mPassword.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.length() == 16) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_password_max_error));
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
		mCheckNumber.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (null == temp) {
					return;
				}
			}
		});
		checkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPhoneNumber.getText().length() != 11) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				if (mPhoneNumber.getText().charAt(0) != '1') {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				sendCheckNum();
			}

		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(mActivity);
		mTitleBar.mTitle.setText(R.string.fogot_title);
		mTitleBar.mRightImg.setVisibility(View.GONE);
		mTitleBar.mRightTv.setVisibility(View.VISIBLE);
		mTitleBar.mRightTv.setText(R.string.fogot_title_righttv);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.closeInput(mActivity);
				finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mPhoneNumber.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_notnull);
					return;
				}
				if (mPassword.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_notnull);
					return;
				}
				if (mCheckNumber.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_check_length_error);
					return;
				}
				if (mPhoneNumber.getText().length() != 11) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				if (mPhoneNumber.getText().charAt(0) != '1') {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					return;
				}
				if (mPassword.getText().length() < 6) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_min_error);
					return;
				}
				// if (mCheckNumber.getText().length() != 6) {
				// YSToast.showToast(getApplicationContext(),
				// R.string.toast_check_length_error);
				// return;
				// }
				changePsd();
			}
		});

	}

	private void changePsd() {
		ResetPsdObject resetPsdObj = new ResetPsdObject();
		resetPsdObj.setNewPsd(mPassword.getText().toString());
		resetPsdObj.setVCode(mCheckNumber.getText().toString());
		resetPsdObj.setPhone(mPhoneNumber.getText().toString());
		resetPsdObj.setSmsid(smsId);
		sendMessage(YSMSG.REQ_FOGOTPSD, 0, 0, resetPsdObj);
		showWaitingDialog();
	}

	private void sendCheckNum() {
		mTickCount = 60;
		handler.post(runnable);
		sendMessage(YSMSG.REQ_FOGOTPSD_VCODE, 0, 0, mPhoneNumber.getText().toString());
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

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_FOGOTPSD_VCODE : {
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						smsId = json.getInt("SMSLogID");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					YSToast.showToast(mActivity, getString(R.string.toast_vcodesend_success));
				} else {
					mTickCount = 0;
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			case YSMSG.RESP_FOGOTPSD : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					mActivity.finish();
					YSToast.showToast(mActivity, R.string.fogot_tip1);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}

			}
				break;
			default :
				break;
		}
	}
}
