package mobi.jzcx.android.chongmi.ui.main.mine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.UpdatePetIconObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.MyAnimationUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.OnTimeSelectListener;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class EditPetActivity extends BaseExActivity implements OnClickListener {
	protected TitleBarHolder mTitleBar = null;
	private Snapshot mSnapshot = null;

	private int Snapshot_Action_Id = 10087;
	private int Photos_Action_Id = 10088;
	private int Clip_Action_Id = 10089;

	static PetObject pet;
	private TimePopupWindow timePW;

	SimpleDraweeView petImg;
	TextView nicknameTv;
	TextView sexTv;
	TextView styleTv;
	TextView birthdayTv;
	TextView introTv;

	public static void startActivity(Context context, PetObject pet) {
		ActivityUtils.startActivity(context, EditPetActivity.class);
		EditPetActivity.pet = pet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editpet);
		initView();
		initData();

		Constant.petName = "";
		Constant.petSex = "";
		Constant.petCategory = "";
		Constant.petStyle = "";
		Constant.petIntro = "";
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTextUtils.isEmpty(Constant.petName)) {
			nicknameTv.setText(Constant.petName);
			pet.setName(Constant.petName);
			Constant.petName = "";
			sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		}
		if (!CommonTextUtils.isEmpty(Constant.petSex)) {
			if (pet.getGender().equals("0")) {
				sexTv.setText("MM");
			} else {
				sexTv.setText("GG");
			}
			pet.setGender(Constant.petSex);
			Constant.petSex = "";
			sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		}
		if (!CommonTextUtils.isEmpty(Constant.petCategory)) {
			pet.setCategoryId(Constant.petStyle);
			styleTv.setText(Constant.petCategory);
			Constant.petCategory = "";
			Constant.petStyle = "";
			sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		}
		if (!CommonTextUtils.isEmpty(Constant.petIntro)) {
			introTv.setText(Constant.petIntro);
			pet.setDescription(Constant.petIntro);
			Constant.petIntro = "";
			sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
		}
	}

	private void initView() {
		initTitleBar();
		initImagePicker();
		PercentRelativeLayout avatarRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_avatar);
		PercentRelativeLayout nameRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_nickname);
		PercentRelativeLayout sexRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_sex);
		PercentRelativeLayout styleRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_style);
		PercentRelativeLayout birthdayRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_birthday);
		PercentRelativeLayout introRL = (PercentRelativeLayout) findViewById(R.id.layout_editpet_desc);

		petImg = (SimpleDraweeView) findViewById(R.id.img_editpet_image);
		nicknameTv = (TextView) findViewById(R.id.tv_editpet_nickname);
		sexTv = (TextView) findViewById(R.id.tv_editpet_sex);
		styleTv = (TextView) findViewById(R.id.tv_editpet_style);
		birthdayTv = (TextView) findViewById(R.id.tv_editpet_birthday);
		introTv = (TextView) findViewById(R.id.tv_editpet_intro);

		avatarRL.setOnClickListener(this);
		nameRL.setOnClickListener(this);
		sexRL.setOnClickListener(this);
		styleRL.setOnClickListener(this);
		birthdayRL.setOnClickListener(this);
		introRL.setOnClickListener(this);

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
					String birthday = CommonUtils.getTime(date);
					pet.setBirthday(birthday);
					birthdayTv.setText(birthday);
					sendMessage(YSMSG.REQ_EDITPET, 0, 0, pet);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		timePW.dismiss();
	}

	private void initData() {
		if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
			FrescoHelper.displayImageview(petImg, pet.getIcoUrl(), R.drawable.avatar_default_image, getResources(),
					true);
		}
		if (!CommonTextUtils.isEmpty(pet.getName())) {
			nicknameTv.setText(pet.getName());
		}
		if (!CommonTextUtils.isEmpty(pet.getGender())) {
			if (pet.getGender().equals("0")) {
				sexTv.setText("MM");
			} else {
				sexTv.setText("GG");
			}
		}
		if (!CommonTextUtils.isEmpty(pet.getCategoryName())) {
			styleTv.setText(pet.getCategoryName());
		}
		if (!CommonTextUtils.isEmpty(pet.getBirthday())) {
			birthdayTv.setText(pet.getBirthday());
		}
		if (!CommonTextUtils.isEmpty(pet.getDescription())) {
			introTv.setText(pet.getDescription());
		}
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.editpet_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditPetActivity.this.finish();
			}
		});
		// mTitleBar.mRightTv.setText(getString(R.string.editpet_righttext));
		// mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// sendMessage(YSMSG.REQ_REMOVEPET, 0, 0, pet.get_id());
		// }
		// });
	}

	private void initImagePicker() {
		mSnapshot = new Snapshot();
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
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Snapshot_Action_Id) {
			String imageFile = "";
			if (null != mSnapshot) {
				DisplayMetrics dm = new DisplayMetrics();
				EditPetActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

				// imageFile = mSnapshot.onActivityResultProc(dm.widthPixels >
				// dm.heightPixels ? dm.widthPixels
				// : dm.heightPixels);
				imageFile = mSnapshot.getmImgPath1();
			}
			if (FileUtils.exists(imageFile)) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				File temp = new File(imageFile);
				intent.setDataAndType(Uri.fromFile(temp), "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", Snapshot.AVATAR_PERCENT);
				intent.putExtra("outputY", Snapshot.AVATAR_PERCENT);
				intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
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
				intent.putExtra("outputX", Snapshot.AVATAR_PERCENT);
				intent.putExtra("outputY", Snapshot.AVATAR_PERCENT);
				intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
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

	private void setAvatar(String avatar) {
		UpdatePetIconObject updateObj = new UpdatePetIconObject();
		updateObj.setPetid(pet.getPetId());
		updateObj.setImagepath(avatar);
		sendMessage(YSMSG.REQ_UPDATEPETICON, 0, 0, updateObj);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_UPDATEPETICON :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							String imagePath = json.getString("ImgUrl");
							FrescoHelper.displayImageview(petImg, imagePath, R.drawable.avatar_default_image,
									getResources(), true);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					YSToast.showToast(mActivity, R.string.toast_editpeticon_success);
				} else {
					YSToast.showToast(mActivity, R.string.toast_editpeticon_failed);
				}
				break;
			case YSMSG.RESP_EDITPET :
				if (msg.arg1 == 200) {
					// initData();
					YSToast.showToast(mActivity, getString(R.string.toast_success));
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_REMOVEPET :
				if (msg.arg1 == 200) {
					mActivity.finish();
					YSToast.showToast(mActivity, R.string.toast_delpet_success);
				} else {
					YSToast.showToast(mActivity, R.string.toast_delpet_failed);
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_editpet_avatar :
				imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
				imagePickerPopview.showAtLocation(findViewById(R.id.layout_editpet_avatar), Gravity.CENTER, 0, 0);
				break;
			case R.id.layout_editpet_nickname :
				PetNickNameActivity.startActivity(mActivity, pet);
				break;
			case R.id.layout_editpet_sex :
				PetSexActivity.startActivity(mActivity, pet);
				break;
			case R.id.layout_editpet_style :
				PetStyleActivity.startActivity(mActivity);
				break;
			case R.id.layout_editpet_birthday :
				timePW.showAtLocation(birthdayTv, Gravity.BOTTOM, 0, 0, CommonUtils.getDate(pet.getBirthday()));
				break;
			case R.id.layout_editpet_desc :
				PetDescActivity.startActivity(mActivity, pet);
				break;
			default :
				break;
		}
	}

}
