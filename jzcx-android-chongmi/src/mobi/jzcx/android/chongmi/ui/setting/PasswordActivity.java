package mobi.jzcx.android.chongmi.ui.setting;

import java.util.Timer;
import java.util.TimerTask;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
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

public class PasswordActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;

	EditText mPassword = null;
	EditText mNewPassword = null;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, PasswordActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		initView();
	}

	void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.password_ttb_title);
		mTitleBar.mRightTv.setText(R.string.password_title_righttext);
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mPassword.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_changepsd_oldpsdnull);
					return;
				}
				if (mNewPassword.getText().length() == 0) {
					YSToast.showToast(getApplicationContext(), R.string.toast_changepsd_newpsdnull);
					return;
				}
				if (mPassword.getText().length() < 6) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_min_error);
					return;
				}
				if (mNewPassword.getText().length() < 6) {
					YSToast.showToast(getApplicationContext(), R.string.toast_password_min_error);
					return;
				}
				complete();
			}
		});

		mPassword = (EditText) this.findViewById(R.id.edt_password_old);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mPassword, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 300);

		mPassword.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
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

		mNewPassword = (EditText) this.findViewById(R.id.edt_password_new);
		mNewPassword.addTextChangedListener(new TextWatcher() {
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

	private void complete() {
		UserObject user = new UserObject();
		user.setNewPsd(mNewPassword.getText().toString());
		user.setOldPsd(mPassword.getText().toString());
		sendMessage(YSMSG.REQ_RESETPSD, 0, 0, user);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_RESETPSD : {
				if (msg.arg1 == 200) {
					// 成功
					App.getInstance().clearActivity();
					CoreModel.getInstance().setMyself(null);
					LoginActivity.startActivity(mActivity, PreferencesUtils.getLoginPhone());
					YSToast.showToast(this, R.string.toast_password_modify_succeed);
				} else {
					// 失败
					YSToast.showToast(this, String.valueOf(msg.obj));
				}
			}
				break;
		}
	}

}