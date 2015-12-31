package mobi.jzcx.android.chongmi.ui.login;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.LoginObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.DatabaseManager;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.MainActivity;
import mobi.jzcx.android.chongmi.ui.register.RegisteActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseExActivity implements OnClickListener {

	private Button mLoginBtn = null;
	private EditText nameEdit;
	private EditText psdEdit;
	// private CheckBox psdShowOrHide;
	static String phone;

	public static void startActivity(Context context, String phone) {
		ActivityUtils.startActivity(context, LoginActivity.class);
		LoginActivity.phone = phone;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		mSetStatusBar = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTextUtils.isEmpty(phone)) {
			nameEdit.setText(phone);
			psdEdit.requestFocus();
		}
	}

	private void initView() {

		TextView fogotpsdTV = (TextView) this.findViewById(R.id.login_fogot);
		TextView registeTV = (TextView) this.findViewById(R.id.login_regist);
		fogotpsdTV.setOnClickListener(this);
		registeTV.setOnClickListener(this);
		mLoginBtn = (Button) this.findViewById(R.id.login_loginBtn);
		mLoginBtn.setOnClickListener(this);

		nameEdit = (EditText) this.findViewById(R.id.login_nameedit);
		psdEdit = (EditText) this.findViewById(R.id.login_psdedit);

		// psdShowOrHide = (CheckBox)
		// this.findViewById(R.id.login_psdShowOrHide);
		// psdShowOrHide.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// if (psdShowOrHide.isChecked()) {
		// psdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		// Editable etable = psdEdit.getText();
		// Selection.setSelection(etable, etable.length());
		// } else {
		// psdEdit.setInputType(InputType.TYPE_CLASS_TEXT |
		// InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// Editable etable = psdEdit.getText();
		// Selection.setSelection(etable, etable.length());
		// }
		// }
		// });

		nameEdit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}
				selectionStart = nameEdit.getSelectionStart();
				selectionEnd = nameEdit.getSelectionEnd();

				if (temp.length() > 0) {
					mLoginBtn.setEnabled(true);
					if (temp.charAt(0) != '1') {
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
						arg0.clear();
						nameEdit.setText(arg0);

					} else if (temp.length() > 11) {
						arg0.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						nameEdit.setText(arg0);
						nameEdit.setSelection(tempSelection);
						YSToast.showToast(getApplicationContext(), getString(R.string.toast_phone_format_error));
					}
				}
				if (temp.length() == 11) {
					nameEdit.clearFocus();
					psdEdit.requestFocus();
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

		psdEdit.addTextChangedListener(new TextWatcher() {
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_fogot : {
				psdEdit.setText("");
				FogotPsdActivity.startActivity(mActivity, nameEdit.getText().toString());
			}
				break;
			case R.id.login_regist : {
				psdEdit.setText("");
				RegisteActivity.startActivity(mActivity);
			}
				break;
			case R.id.login_loginBtn : {
				if (nameEdit.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_notnull);
					nameEdit.requestFocus();
					return;
				}
				if (nameEdit.getText().length() != 11) {
					YSToast.showToast(getApplicationContext(), R.string.toast_phone_format_error);
					nameEdit.requestFocus();
					return;
				}
				if (psdEdit.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_notnull);
					psdEdit.requestFocus();
					return;
				}
				if (psdEdit.getText().length() < 6) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_min_error);
					return;
				}

				InputUtils.closeInput(mActivity);
				login();
			}
				break;
			default :
				break;
		}

	}

	private void login() {
		LoginObject loginObj = new LoginObject();
		loginObj.setUsername(nameEdit.getText().toString());
		loginObj.setUserpsd(psdEdit.getText().toString());
		loginObj.setDeviceName(CommonUtils.getDeviceName());
		loginObj.setDeviceId(CommonUtils.getDeviceId(this));
		sendMessage(YSMSG.REQ_LOGIN, 0, 0, loginObj);
		showOrUpdateWaitingDialog(getString(R.string.login_waitting_text));
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_LOGIN : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (App.getInstance().getLnglat() != null) {
						BaseController.getInstance().sendMessage(YSMSG.REQ_UPDATE_LOCATION, 0, 0,
								App.getInstance().getLnglat());
					}

					// success
					// "Result":{"Tokens":[{"SubSysID":3,"Token":"2cbxksqfuzezwdlxadztt3r1"},{"SubSysID":0,"Token":"c0081c5c14f19675e0cda3eca56cdd5a"}]},"ErrorCode":0,"ErrorMsg":null
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
							LoginActivity.this.finish();
							MainActivity.startActivity(mActivity);
							DatabaseManager.getInstance().initHelper(user.getMemberId());
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

									LogUtils.i("IMInitHelper", "login onSuccess");
								}

								@Override
								public void onProgress(int progress, String status) {
								}

								@Override
								public void onError(final int code, final String message) {
									LogUtils.i("IMInitHelper", "login onError");
								}
							});
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else if (msg.arg1 == 6061104) {
					DialogHelper.showSuspendedDialog(mActivity, false, true, new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
									+ getString(R.string.dialog_suspend_phone)));
							startActivity(intent);
						}
					});
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			default :
				break;
		}
	}

	private static void insertMyselfTocontact() {

	}

}
