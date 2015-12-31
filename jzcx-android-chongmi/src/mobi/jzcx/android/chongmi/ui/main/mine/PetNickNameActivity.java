package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
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
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class PetNickNameActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	static PetObject pet;
	EditText mNickName = null;
	String nickname;

	public static void startActivity(Context context, PetObject pet) {
		ActivityUtils.startActivity(context, PetNickNameActivity.class);
		PetNickNameActivity.pet = pet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petnickname);

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
					YSToast.showToast(PetNickNameActivity.this, R.string.toast_petnickname_format_error);
				}
			}
		});

		mNickName = (EditText) this.findViewById(R.id.edt_petnickname);
		// if (null != getIntent()) {
		// nickname = getIntent().getStringExtra("nickname");
		// if (!TextUtils.isEmpty(nickname)) {
		// mNickName.setText(nickname);
		// CharSequence text = mNickName.getText();
		// if (text instanceof Spannable) {
		// Spannable spanText = (Spannable) text;
		// Selection.setSelection(spanText, text.length());
		// }
		// }
		// }
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mNickName, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 300);

		// InputFilter inputFilter = new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source, int start, int end,
		// Spanned dest, int dstart, int dend) {
		// int sourceLen = source.toString().length();
		// if (dstart + sourceLen > 6) {
		// YSToast.showToast(getApplicationContext(),
		// getString(R.string.toast_nickname_max_error));
		// return "";
		// }
		// if (source.length() < 1 && (dend - dstart >= 1)) {
		// return dest.subSequence(dstart, dend - 1);
		// }
		// return source;
		// }
		// };
		// mNickName.setFilters(new InputFilter[]{inputFilter});

		mNickName.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.toString().trim().length() > 1) {
					if (null != mTitleBar) {
						if (temp.toString().trim().equals(nickname)) {
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
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_petname_lengtherror));
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
		if (pet != null && !CommonTextUtils.isEmpty(pet.getName())) {
			nickname = pet.getName();
			mNickName.setText(pet.getName());
			mNickName.setSelection(pet.getName().length());
		}
	}

	private void complete() {
		if (CommonTextUtils.isEmpty(mNickName.getText().toString())) {
			YSToast.showToast(mActivity, getString(R.string.toast_petname_notnull));
			return;
		}
		// pet.setName(mNickName.getText().toString());
		// sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		Constant.petName = mNickName.getText().toString().trim();
		PetNickNameActivity.this.finish();
	}

	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case YSMSG.RESP_EDITPET : {
	// if (msg.arg1 == 200) {
	// YSToast.showToast(mActivity, R.string.toast_editpetname_success);
	// hideSoftKeyboard();
	// EditPetActivity.pet = pet;
	// PetNickNameActivity.this.finish();
	// } else {
	// YSToast.showToast(mActivity, R.string.toast_editpetname_failed);
	// }
	// }
	// break;
	// }
	// }
}
