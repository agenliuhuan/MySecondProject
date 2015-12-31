package mobi.jzcx.android.chongmi.ui.register;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.RegisteObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.DatabaseManager;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.InputUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.core.mvc.BaseController;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

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

public class RegisteActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	private EditText mPhoneNumber = null;
	private EditText mPassword = null;
	private EditText mCheckNumber = null;
	private Button checkBtn;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, RegisteActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registe);
		initView();
	}

	private void initView() {
		initTitleBar();
		mPhoneNumber = (EditText) this.findViewById(R.id.registe_phone);
		mPassword = (EditText) this.findViewById(R.id.registe_password);
		mCheckNumber = (EditText) this.findViewById(R.id.registe_check);
		checkBtn = (Button) findViewById(R.id.registe_resendBtn);
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
		mPassword.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}
				selectionStart = mPassword.getSelectionStart();
				selectionEnd = mPassword.getSelectionEnd();
				if (temp.length() > 16) {
					arg0.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					mPassword.setText(arg0);
					mPassword.setSelection(tempSelection);
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
		mTitleBar.mTitle.setText(R.string.registe_title);
		mTitleBar.mRightImg.setVisibility(View.GONE);
		mTitleBar.mRightTv.setVisibility(View.VISIBLE);
		mTitleBar.mRightTv.setText(R.string.registe_title_righttext);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.closeInput(mActivity);
				// LoginActivity.startActivity(mActivity, "");
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
				nextStup();
			}
		});
	}

	protected void nextStup() {
		RegisteObject registeObj = new RegisteObject();
		String phone = mPhoneNumber.getText().toString();
		String password = mPassword.getText().toString();
		String checknum = mCheckNumber.getText().toString();
		registeObj.setPhone(phone);
		registeObj.setPassword(password);
		registeObj.setCode(checknum);
		sendMessage(YSMSG.REQ_REGISTER, 0, 0, registeObj);
		showWaitingDialog();
	}

	private void sendCheckNum() {
		sendMessage(YSMSG.REQ_REGISTER_SMS, 0, 0, mPhoneNumber.getText().toString());
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

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_REGISTER_SMS : {
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.toast_vcodesend_success));
				} else {
					mTickCount = 0;
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			case YSMSG.RESP_REGISTER : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (App.getInstance().getLnglat() != null) {
						BaseController.getInstance().sendMessage(YSMSG.REQ_UPDATE_LOCATION, 0, 0,
								App.getInstance().getLnglat());
					}
					// "MemberId":15,"Phone":"14628899633","RealName":"","Birthday":"0001-01-01T00:00:00","Gender":"",
					// "NickName":"14628899633","ICOUrl":"","LastLoginTime":"2015-12-08T16:28:59.47",
					// "Token":"9b8ebe4124760cbb3b9412f8c087fdb9","password":"D8578EDF8458CE06FBC5BB76A58C5CA4","username":"14628899633"
					if (msg.obj != null) {
						try {
							String result = (String) msg.obj;
							JSONObject jsonobj = new JSONObject(result);
							String userToken = jsonobj.getString("Token");
							PreferencesUtils.setUserToken(userToken);
							UserObject user = new UserObject();
							user.setMemberId(jsonobj.getString("MemberId"));
							user.setPhone(jsonobj.getString("Phone"));
							user.setBirthday(jsonobj.getString("Birthday"));
							user.setGender(jsonobj.getString("Gender"));
							user.setNickName(jsonobj.getString("NickName"));
							user.setIcoUrl(jsonobj.getString("IcoUrl"));
							String ImPsd = jsonobj.getString("password");
							user.setIMpassword(ImPsd);
							final String ImUserName = jsonobj.getString("username");
							user.setIMusername(ImUserName);
							CoreModel.getInstance().setMyself(user);
							DatabaseManager.getInstance().initHelper(user.getMemberId());
							RegisteActivity.this.finish();
							RegisteAttentionActivity.startActivity(mActivity);
							IMInitHelper.getInstance().init(App.getInstance());
							EMChatManager.getInstance().login(ImUserName, ImPsd, new EMCallBack() {

								@Override
								public void onSuccess() {

									// 登陆成功，保存用户名
									IMInitHelper.getInstance().setCurrentUserName(ImUserName);
									// 注册群组和联系人监听
									IMInitHelper.getInstance().registerGroupAndContactListener();

									EMGroupManager.getInstance().loadAllGroups();
									EMChatManager.getInstance().loadAllConversations();

									IMInitHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

									LogUtils.i("IMInitHelper", "onSuccess");
								}

								@Override
								public void onProgress(int progress, String status) {
								}

								@Override
								public void onError(final int code, final String message) {
									LogUtils.i("IMInitHelper", "onError");
								}
							});
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
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
