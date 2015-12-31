package mobi.jzcx.android.chongmi.ui.main.answer;

import com.facebook.drawee.view.SimpleDraweeView;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AnswerObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AnswerDetailActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	static AnswerObject answerObj;
	SimpleDraweeView userAvatar;
	TextView nameTV;
	TextView timeTV;
	TextView contentTV;

	public static void startActivity(Context context, AnswerObject answerObj) {
		ActivityUtils.startActivity(context, AnswerDetailActivity.class);
		AnswerDetailActivity.answerObj = answerObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answerdetail);
		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
		userAvatar = (SimpleDraweeView) findViewById(R.id.answerdetail_userAvatar);
		nameTV = (TextView) findViewById(R.id.answerdetail_userName);
		timeTV = (TextView) findViewById(R.id.answerdetail_Time);
		contentTV = (TextView) findViewById(R.id.answerdetail_content);
		userAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (answerObj.getIcoUrl().equals("doctor")) {
					AnswerDoctorActivity.startActivity(mActivity);
				} else {
					AccountCenterActivity.startActivity(mActivity, answerObj.getMemberId());
				}
			}
		});
		nameTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (answerObj.getNickName().equals("doctor")) {
					AnswerDoctorActivity.startActivity(mActivity);
				} else {
					AccountCenterActivity.startActivity(mActivity, answerObj.getMemberId());
				}
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.answerdetail_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerDetailActivity.this.finish();
			}
		});

	}

	private void initData() {
		if (answerObj != null) {
			if (!CommonTextUtils.isEmpty(answerObj.getIcoUrl())) {
				if (answerObj.getIcoUrl().equals("doctor")) {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.question_official_avatar);
					userAvatar.setImageURI(uri);
				} else {
					FrescoHelper.displayImageview(userAvatar,
							answerObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
							R.drawable.avatar_default_image, mActivity.getResources(), true);
				}
			}
			if (!CommonTextUtils.isEmpty(answerObj.getNickName())) {
				if (answerObj.getNickName().equals("doctor")) {
					nameTV.setText(R.string.main_questionofficial_name);
				} else {
					nameTV.setText(answerObj.getNickName());
				}
			} else {
				nameTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(answerObj.getCreateTime())) {
				timeTV.setText(answerObj.getCreateTime());
			} else {
				timeTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(answerObj.getAnswerText())) {
				contentTV.setText(answerObj.getAnswerText());
			} else {
				contentTV.setText("");
			}
		}
	}
}
