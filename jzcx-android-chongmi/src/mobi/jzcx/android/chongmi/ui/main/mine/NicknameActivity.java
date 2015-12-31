package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class NicknameActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;
	String defaultName;
	EditText mNickName = null;
	boolean mHasNickName = false;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, NicknameActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname);

		initView();
		initData();
	}

	void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.nickname_ttb_title);
		mTitleBar.mRightTv.setText(R.string.nickname_title_righttext);
		mTitleBar.mRightTv.setEnabled(false);
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String reg = "^[a-zA-Z0-9_\u4e00-\u9fa5\\s]+$";
				Pattern p1 = Pattern.compile(reg);
				Matcher m1 = p1.matcher(mNickName.getEditableText().toString());
				if (m1.matches()) {
					complete();
				} else {
					YSToast.showToast(NicknameActivity.this, R.string.toast_nickname_format_error);
				}
			}
		});

		mNickName = (EditText) this.findViewById(R.id.edt_nickname_new);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mNickName, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 300);

		mNickName.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.toString().trim().length() > 1) {
					if (null != mTitleBar) {
						if (temp.toString().trim().equals(defaultName)) {
							mTitleBar.mRightTv.setEnabled(false);
						} else {
							mTitleBar.mRightTv.setEnabled(true);
						}
					}
				} else {
					if (null != mTitleBar) {
						mTitleBar.mRightTv.setEnabled(false);
					}
				}
				if (temp.toString().trim().length() == 10) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_nickname_max_error));
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
	private void initData() {
		UserObject user = CoreModel.getInstance().getMyself();
		if (user != null && !TextUtils.isEmpty(user.getNickName())) {
			defaultName = user.getNickName();
			mNickName.setText(user.getNickName());
			mNickName.setSelection(user.getNickName().length());
		}
	}

	private void complete() {
		if (CommonTextUtils.isEmpty(mNickName.getText().toString())) {
			YSToast.showToast(mActivity, getString(R.string.toast_usernickname_notnull));
			return;
		}

		UserObject user = new UserObject();
		user.setNickName(mNickName.getText().toString().trim());
		Constant.UserName = mNickName.getText().toString().trim();
		// 发送请求
		sendMessage(YSMSG.REQ_UPDATE_INFO, 0, 0, user);
		NicknameActivity.this.finish();
		// showWaitingDialog();
	}

	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case YSMSG.RESP_UPDATE_INFO : {
	// dismissWaitingDialog();
	// if (msg.arg1 == 200) {
	// UserObject user = CoreModel.getInstance().getMyself();
	// user.setNickName(mNickName.getText().toString());
	// CoreModel.getInstance().setMyself(user);
	// hideSoftKeyboard();
	// NicknameActivity.this.finish();
	// } else {
	// YSToast.showToast(mActivity, String.valueOf(msg.obj));
	// }
	// }
	// break;
	// }
	// }

}