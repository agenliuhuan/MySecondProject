package mobi.jzcx.android.chongmi.ui.main.mine;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PetSexActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	ImageView manimg;
	ImageView womanimg;
	boolean isBoy = true;
	static PetObject pet;

	public static void startActivity(Context context, PetObject pet) {
		ActivityUtils.startActivity(context, PetSexActivity.class);
		PetSexActivity.pet = pet;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petsex);
		initView();
		initData();
	}

	private void initData() {
		RelativeLayout manRL = (RelativeLayout) findViewById(R.id.pet_sexmanLL);
		RelativeLayout womanRL = (RelativeLayout) findViewById(R.id.pet_sexwomanLL);
		manimg = (ImageView) findViewById(R.id.petsex_manImg);
		womanimg = (ImageView) findViewById(R.id.petsex_womanImg);
		manRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				manimg.setVisibility(View.VISIBLE);
				womanimg.setVisibility(View.INVISIBLE);
				isBoy = true;
				if (pet.getGender().equals("1")) {
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
				if (pet.getGender().equals("0")) {
					mTitleBar.mRightTv.setEnabled(false);
				} else {
					mTitleBar.mRightTv.setEnabled(true);
				}
			}
		});
		if (pet.getGender().equals("1")) {
			manimg.setVisibility(View.VISIBLE);
			womanimg.setVisibility(View.INVISIBLE);
			isBoy = true;
		} else {
			manimg.setVisibility(View.INVISIBLE);
			womanimg.setVisibility(View.VISIBLE);
			isBoy = false;
		}
		mTitleBar.mRightTv.setEnabled(false);
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
		if (isBoy) {
			pet.setGender("1");
			Constant.petSex = "1";
		} else {
			pet.setGender("0");
			Constant.petSex = "0";
		}
//		sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		PetSexActivity.this.finish();
	}

//	public void handleMessage(Message msg) {
//		switch (msg.what) {
//			case YSMSG.RESP_EDITPET : {
//				if (msg.arg1 == 200) {
//					EditPetActivity.pet = pet;
//					
//					YSToast.showToast(mActivity, R.string.toast_editpetsex_success);
//				} else {
//					YSToast.showToast(mActivity, R.string.toast_editpetsex_failed);
//				}
//			}
//				break;
//		}
//	}
}
