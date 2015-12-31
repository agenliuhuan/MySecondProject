package mobi.jzcx.android.chongmi.ui.main.mine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ReportObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.MyAnimationUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.OnTimeSelectListener;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.Type;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AccountActivity extends BaseExActivity implements OnClickListener {
	protected TitleBarHolder mTitleBar = null;

	private Snapshot mSnapshot = null;

	private int Snapshot_Action_Id = 10087;
	private int Photos_Action_Id = 10088;
	private int Clip_Action_Id = 10089;

	private SimpleDraweeView mImage = null;

	private TextView mNicknameTxt;
	private TextView sexTxt;
	private TextView birthDayTxt;
	private String birthday;

	private TimePopupWindow timePW;
	private RelativeLayout mBirthdayLayout;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, AccountActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		initView();
		Constant.UserName = "";
		Constant.Usersex = -1;
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		initTitleBar();
		initImagePicker();

		mImage = (SimpleDraweeView) findViewById(R.id.img_account_image);
		mNicknameTxt = (TextView) findViewById(R.id.tv_account_nickname);
		sexTxt = (TextView) findViewById(R.id.tv_account_sex);
		birthDayTxt = (TextView) findViewById(R.id.tv_account_birthday);

		RelativeLayout mAvatarLayout = (RelativeLayout) findViewById(R.id.layout_account_avatar);
		RelativeLayout mNicknameLayout = (RelativeLayout) findViewById(R.id.layout_account_nickname);
		RelativeLayout mSexLayout = (RelativeLayout) findViewById(R.id.layout_account_sex);
		mBirthdayLayout = (RelativeLayout) findViewById(R.id.layout_account_birthday);

		mAvatarLayout.setOnClickListener(this);
		mNicknameLayout.setOnClickListener(this);
		mSexLayout.setOnClickListener(this);
		mBirthdayLayout.setOnClickListener(this);

		timePW = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		timePW.setRange(year - 100, year + 100);
		timePW.setCyclic(true);
		timePW.setOnTimeSelectListener(new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date) {
				Date now = new Date();
				if (date.after(now)) {
					YSToast.showToast(mActivity, getString(R.string.toast_dateafter_text));
				} else {
					UserObject user = new UserObject();
					birthday = CommonUtils.getTime(date);
					user.setBirthday(birthday);
					birthDayTxt.setText(birthday);
					sendMessage(YSMSG.REQ_UPDATE_INFO, 0, 0, user);
				}
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.account_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountActivity.this.finish();
			}
		});
	}

	private void initImagePicker() {
		mSnapshot = new Snapshot();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == Snapshot_Action_Id) {
				String imageFile = "";
				if (null != mSnapshot) {
					// DisplayMetrics dm = new DisplayMetrics();
					// AccountActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

					imageFile = mSnapshot.getmImgPath1();
				}
				if (FileUtils.exists(imageFile)) {
					Intent intent = new Intent("com.android.camera.action.CROP");
					File temp = new File(imageFile);
					intent.setDataAndType(Uri.fromFile(temp), "image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", Snapshot.AVATAR_WIDTH_HEIGHT);
					intent.putExtra("outputY", Snapshot.AVATAR_WIDTH_HEIGHT);
					intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					intent.putExtra("noFaceDetection", true);
					intent.putExtra("return-data", true);
					intent.putExtra("scale", true);
					intent.putExtra("scaleUpIfNeeded", true);
					startActivityForResult(intent, Clip_Action_Id);
				}
			} else if (requestCode == Photos_Action_Id) {
				if (data != null) {
					Uri uri = (Uri) data.getData();
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(uri, "image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", Snapshot.AVATAR_WIDTH_HEIGHT);
					intent.putExtra("outputY", Snapshot.AVATAR_WIDTH_HEIGHT);
					intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					intent.putExtra("noFaceDetection", true);
					intent.putExtra("return-data", true);
					intent.putExtra("scale", true);
					intent.putExtra("scaleUpIfNeeded", true);
					startActivityForResult(intent, Clip_Action_Id);
				}

			} else if (requestCode == Clip_Action_Id) {
				if (data != null) {
					Bitmap bitmap = data.getExtras().getParcelable("data");
					String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					String strImgPath = FileUtils.COVER;
					File out = new File(strImgPath);
					if (!out.exists()) {
						out.mkdirs();
					}
					String cropPath = strImgPath + fileName + FileUtils.JPG;
					if (ImageUtils.saveBitmapToFile(bitmap, cropPath, Snapshot.AVATAR_PERCENT)) {
						setAvatar(cropPath);

					} else {

					}
					ImageUtils.recycleBitmap(bitmap);
				}
			}
		}
	}

	private void initData() {
		UserObject user = CoreModel.getInstance().getMyself();
		if (user != null) {
			if (!CommonTextUtils.isEmpty(user.getNickName())) {
				mNicknameTxt.setText(user.getNickName());
			}
			if (!CommonTextUtils.isEmpty(user.getGender())) {
				if (user.getGender().equals("0")) {
					sexTxt.setText(getString(R.string.sex_woman));
				} else {
					sexTxt.setText(getString(R.string.sex_man));
				}
			} else {
				sexTxt.setText("");
			}
			if (!CommonTextUtils.isEmpty(user.getBirthday())) {
				birthDayTxt.setText(user.getBirthday());
			}
			if (!CommonTextUtils.isEmpty(user.getIcoUrl())) {
				FrescoHelper.displayImageview(mImage, user.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
						R.drawable.avatar_default_image, getResources(), true);
			}
		}
		if (!CommonTextUtils.isEmpty(Constant.UserName)) {
			mNicknameTxt.setText(Constant.UserName);
		}
		if (Constant.Usersex == 0) {
			sexTxt.setText(getString(R.string.sex_woman));
		} else if (Constant.Usersex == 1) {
			sexTxt.setText(getString(R.string.sex_man));
		}
	}

	private void setAvatar(String filePath) {
		UserObject user = new UserObject();
		user.setIcoUrl(filePath);
		sendMessage(YSMSG.REQ_UPDATE_INFO, 0, 0, user);
		showOrUpdateWaitingDialog(getString(R.string.toast_updateavatar_ing));
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_UPDATE_INFO : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						UserObject myself = OJMFactory.createOJM().fromJson(result, UserObject.class);
						if (!CommonTextUtils.isEmpty(myself.getIcoUrl())) {
							CoreModel.getInstance().getMyself().setIcoUrl(myself.getIcoUrl());
							FrescoHelper.displayImageview(mImage,
									myself.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
									R.drawable.avatar_default_image, getResources(), true);
						}
						if (!CommonTextUtils.isEmpty(myself.getBirthday())) {
							CoreModel.getInstance().getMyself().setBirthday(myself.getBirthday());
						}
						if (!CommonTextUtils.isEmpty(myself.getNickName())) {
							CoreModel.getInstance().getMyself().setNickName(myself.getNickName());
							Constant.UserName = "";
						}
						if (!CommonTextUtils.isEmpty(myself.getGender())) {
							CoreModel.getInstance().getMyself().setGender(myself.getGender());
							Constant.Usersex = -1;
						}
						YSToast.showToast(mActivity, getString(R.string.toast_success));
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

	ImagePickerPopupWindow imagePickerPopview;

	private OnClickListener imagePickerOnClick = new OnClickListener() {

		public void onClick(View v) {
			imagePickerPopview.dismiss();
			switch (v.getId()) {
				case R.id.btn_image_picker_snapshot :
					String strImgPath = FileUtils.COVER;
					File out = new File(strImgPath);
					if (!out.exists()) {
						out.mkdirs();
					}
					String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					String photoFilePath = strImgPath + fileName;
					if (null != mSnapshot) {
						mSnapshot.takePhoto(mActivity, photoFilePath, Snapshot_Action_Id, Snapshot.AVATAR_PERCENT);
					}
					break;
				case R.id.btn_image_picker_album :
					Intent album = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					album.setType("image/*");
					startActivityForResult(album, Photos_Action_Id);
					break;
				default :
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_account_avatar :
				imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
				imagePickerPopview.showAtLocation(findViewById(R.id.layout_account_rootview), Gravity.CENTER, 0, 0);
				break;
			case R.id.layout_account_nickname :
				NicknameActivity.startActivity(AccountActivity.this);
				break;
			case R.id.layout_account_sex :
				SexActivity.startActivity(AccountActivity.this);
				break;
			case R.id.layout_account_birthday :
				UserObject user = CoreModel.getInstance().getMyself();
				Date birthdaydate = null;
				if (user != null) {
					if (!TextUtils.isEmpty(user.getBirthday())) {
						birthdaydate = CommonUtils.getDate(user.getBirthday());
					}
				}
				timePW.showAtLocation(mBirthdayLayout, Gravity.BOTTOM, 0, 0, birthdaydate);
				break;
			default :
				break;
		}
	}
}
