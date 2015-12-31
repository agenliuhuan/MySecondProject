package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.Timer;
import java.util.TimerTask;

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
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class PetDescActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	static PetObject pet;
	EditText mNickName = null;
	static String desc;

	public static void startActivity(Context context, PetObject pet) {
		ActivityUtils.startActivity(context, PetDescActivity.class);
		PetDescActivity.pet = pet;
		PetDescActivity.desc = "";
	}

	public static void startActivity(Context context, String desc) {
		ActivityUtils.startActivity(context, PetDescActivity.class);
		PetDescActivity.desc = desc;
		PetDescActivity.pet = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petdesc);

		initView();

		if (pet != null && !CommonTextUtils.isEmpty(pet.getDescription())) {
			mNickName.setText(pet.getDescription());
		} else if (!CommonTextUtils.isEmpty(desc)) {
			mNickName.setText(desc);
		} else {
			mNickName.setText("");
		}
		CharSequence text = mNickName.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.petdesc_title);
		mTitleBar.mRightTv.setText(R.string.nickname_title_righttext);
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				complete();
			}
		});

		mNickName = (EditText) this.findViewById(R.id.edt_petdesc);
		// InputFilter inputFilter = new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source, int start, int end,
		// Spanned dest, int dstart, int dend) {
		// int sourceLen = source.toString().length();
		// if (dstart + sourceLen > 30) {
		// YSToast.showToast(getApplicationContext(),
		// getString(R.string.toast_petintro_max_error));
		// return source;
		// }
		// if (source.length() < 1 && (dend - dstart >= 1)) {
		// return dest.subSequence(dstart, dend - 1);
		// }
		// return source;
		// }
		//
		//
		// };
		// mNickName.setFilters(new InputFilter[]{new
		// InputFilter.LengthFilter(30)});

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
				if (temp.length() == 0) {
					if (null != mTitleBar) {
						mTitleBar.mRightTv.setEnabled(true);
					}
					return;
				}

				if (pet != null && temp.toString().equals(pet.getDescription())) {
					if (null != mTitleBar) {
						mTitleBar.mRightTv.setEnabled(false);
					}
				} else if (temp.toString().equals(desc)) {
					if (null != mTitleBar) {
						mTitleBar.mRightTv.setEnabled(false);
					}
				} else {
					if (null != mTitleBar) {
						mTitleBar.mRightTv.setEnabled(true);
					}
				}
				if (temp.length() == 30) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_petintro_max_error));
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
//		if (pet != null) {
//			pet.setDescription(mNickName.getText().toString());
//			sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
//		} else {
//			
//		}
		Constant.petIntro = mNickName.getText().toString().trim();
		hideSoftKeyboard();
		PetDescActivity.this.finish();
	}

//	@Override
//	public void handleMessage(Message msg) {
//		switch (msg.what) {
//			case YSMSG.RESP_EDITPET : {
//				if (msg.arg1 == 200) {
//					hideSoftKeyboard();
//					EditPetActivity.pet = pet;
//					PetDescActivity.this.finish();
//					YSToast.showToast(mActivity, getString(R.string.toast_editpetdesc_success));
//				} else {
//					YSToast.showToast(mActivity, String.valueOf(msg.obj));
//				}
//			}
//				break;
//		}
//	}
}
