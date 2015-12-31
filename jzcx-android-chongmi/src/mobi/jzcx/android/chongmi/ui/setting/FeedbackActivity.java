package mobi.jzcx.android.chongmi.ui.setting;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
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
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends BaseExActivity implements TextWatcher {
	protected TitleBarHolder mTitleBar = null;
	private TextView numTv;
	EditText feedback;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, FeedbackActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		initTitleBar();
		numTv = (TextView) findViewById(R.id.feedback_textview);
		feedback = (EditText) findViewById(R.id.feedback_edit);
		feedback.addTextChangedListener(this);

		numTv.setText(String.format(getString(R.string.feedback_textnum), "0"));
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.feedback_title));
		mTitleBar.mRightTv.setText(getString(R.string.feedback_righttext));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FeedbackActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (feedback.getText().length() == 0) {
					YSToast.showToast(mActivity, getString(R.string.toast_feedback_notnull));
					return;
				}
				if (feedback.getText().length() < 10) {
					YSToast.showToast(mActivity, getString(R.string.toast_feedback_fammter_error));
					return;
				}
				sendMessage(YSMSG.REQ_FEEDBACK, 0, 0, feedback.getText().toString());
				showWaitingDialog();
			}
		});
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 500) {
			YSToast.showToast(mActivity, getString(R.string.toast_feedback_fammter_error));
		}
		String length = String.valueOf(s.length());
		numTv.setText(String.format(getString(R.string.feedback_textnum), length));

	}

	@Override
	public void handleMessage(Message msg) {
		dismissWaitingDialog();
		if (msg.what == YSMSG.RESP_FEEDBACK) {
			if (msg.arg1 == 200) {
				FeedbackOverActivity.startActivity(mActivity);
				finish();
			} else {
				YSToast.showToast(mActivity, String.valueOf(msg.obj));
			}
		}
	}

}
