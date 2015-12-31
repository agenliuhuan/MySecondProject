package mobi.jzcx.android.chongmi.ui.main.mine;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SexActivity extends BaseExActivity {

	protected TitleBarHolder mTitleBar;
	ImageView manimg;
	ImageView womanimg;
	boolean isBoy = true;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, SexActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sex);

		initView();
		initData();
	}

	private void initData() {
		RelativeLayout manRL = (RelativeLayout) findViewById(R.id.sexmanLL);
		RelativeLayout womanRL = (RelativeLayout) findViewById(R.id.sexwomanLL);
		manimg = (ImageView) findViewById(R.id.sex_manImg);
		womanimg = (ImageView) findViewById(R.id.sex_womanImg);
		manRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				manimg.setVisibility(View.VISIBLE);
				womanimg.setVisibility(View.INVISIBLE);
				isBoy = true;
				if (CoreModel.getInstance().getMyself().getGender().equals("1")) {
					mTitleBar.mRightTv.setEnabled(false);
				} else {
					mTitleBar.mRightTv.setEnabled(true);
				}
			}
		});
		womanRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				manimg.setVisibility(View.INVISIBLE);
				womanimg.setVisibility(View.VISIBLE);
				isBoy = false;
				if (CoreModel.getInstance().getMyself().getGender().equals("0")) {
					mTitleBar.mRightTv.setEnabled(false);
				} else {
					mTitleBar.mRightTv.setEnabled(true);
				}
			}
		});
		if (!CommonTextUtils.isEmpty(CoreModel.getInstance().getMyself().getGender())) {
			if (CoreModel.getInstance().getMyself().getGender().equals("1")) {
				manimg.setVisibility(View.VISIBLE);
				womanimg.setVisibility(View.INVISIBLE);
				isBoy = true;
			} else {
				manimg.setVisibility(View.INVISIBLE);
				womanimg.setVisibility(View.VISIBLE);
				isBoy = false;
			}
			mTitleBar.mRightTv.setEnabled(false);
		} else {
			mTitleBar.mRightTv.setEnabled(true);
		}
	}

	private void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.sex_ttb_title);
		mTitleBar.mRightTv.setText(R.string.sex_title_righttext);
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				complete();
			}
		});

	}

	private void complete() {
		UserObject user = new UserObject();
		if (isBoy) {
			user.setGender("1");
			Constant.Usersex = 1;
		} else {
			user.setGender("0");
			Constant.Usersex = 0;
		}
		sendMessage(YSMSG.REQ_UPDATE_INFO, 0, 0, user);
		SexActivity.this.finish();
	}

	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case YSMSG.RESP_UPDATE_INFO: {
	// if (msg.arg1 == 200) {
	// SexActivity.this.finish();
	// if (isBoy) {
	// CoreModel.getInstance().getMyself().setGender("1");
	// } else {
	// CoreModel.getInstance().getMyself().setGender("0");
	// }
	// } else {
	// YSToast.showToast(mActivity, String.valueOf(msg.obj));
	// }
	// }
	// break;
	// }
	// }
}
