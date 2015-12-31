package mobi.jzcx.android.chongmi.ui.main.mine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.register.RegisteAttentionActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.InputUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.OnTimeSelectListener;
import mobi.jzcx.android.core.view.pickerview.TimePopupWindow.Type;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddPetActivity extends BaseExActivity implements OnClickListener {
	protected TitleBarHolder mTitleBar;

	private Snapshot mSnapshot = null;

	public int Snapshot_Action_Id = 10087;
	public int Photos_Action_Id = 10088;
	public int Clip_Action_Id = 10089;

	SimpleDraweeView mImage = null;

	EditText mNickName = null;

	boolean mHasAvatar = false;
	private TimePopupWindow timePW;
	TextView styleTv;
	TextView descTv;
	PetObject petObj = null;
	RelativeLayout birthdayRL;
	TextView birthdayTv;

	ImageView GGImage;
	TextView GGTv;
	ImageView MMImage;
	TextView MMTv;
	static boolean isFromRegiste;

	public static void startActivity(Context context, boolean isFromRegiste) {
		ActivityUtils.startActivity(context, AddPetActivity.class);
		AddPetActivity.isFromRegiste = isFromRegiste;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addpet);
		initView();
		petObj = new PetObject();
		Constant.petStyle = "";
		Constant.petCategory = "";
		Constant.petIntro = "";
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(Constant.petCategory)) {
			styleTv.setTextColor(Color.parseColor("#666666"));
			styleTv.setText(Constant.petCategory);
		}
		if (!TextUtils.isEmpty(Constant.petIntro)) {
			descTv.setText(Constant.petIntro);
		} else {
			descTv.setText("");
		}
	}

	private void initView() {
		initTitleBar();

		initImagePicker();

		mImage = (SimpleDraweeView) findViewById(R.id.addpet_image);
		mImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);
				imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
				imagePickerPopview.showAtLocation(findViewById(R.id.addpet_title), Gravity.CENTER, 0, 0);
			}
		});
		mNickName = (EditText) this.findViewById(R.id.addpet_nickname);
		mNickName.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
				}

				if (temp.length() == 10) {
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
		RelativeLayout styleRL = (RelativeLayout) findViewById(R.id.addpet_styleLL);
		RelativeLayout introRL = (RelativeLayout) findViewById(R.id.addpet_introLL);
		styleTv = (TextView) findViewById(R.id.categoryTv);
		descTv = (TextView) findViewById(R.id.edit_pet_intro);
		birthdayRL = (RelativeLayout) findViewById(R.id.addpet_birthdayLL);
		birthdayTv = (TextView) findViewById(R.id.birthdayTv);
		GGImage = (ImageView) findViewById(R.id.addpet_man);
		MMImage = (ImageView) findViewById(R.id.addpet_woman);
		GGTv = (TextView) findViewById(R.id.addpet_mantv);
		MMTv = (TextView) findViewById(R.id.addpet_womantv);
		MMImage.setOnClickListener(this);
		GGImage.setOnClickListener(this);
		styleRL.setOnClickListener(this);
		introRL.setOnClickListener(this);
		birthdayRL.setOnClickListener(this);

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
					petObj.setBirthday(birthday);
					birthdayTv.setText(birthday);
				}
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(mActivity);
		mTitleBar.mTitle.setText(R.string.registeeditpet_title);
		mTitleBar.mRightImg.setVisibility(View.GONE);
		mTitleBar.mRightTv.setVisibility(View.VISIBLE);
		if (isFromRegiste) {
			mTitleBar.titleLeftRL.setVisibility(View.GONE);
		} else {
			mTitleBar.titleLeftRL.setVisibility(View.VISIBLE);
		}
		mTitleBar.mRightTv.setText(R.string.registeover_title_righttext);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.closeInput(mActivity);
				finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mHasAvatar) {
					YSToast.showToast(mActivity, R.string.toast_peticon_notnull);
					return;
				} else {

				}
				if (CommonTextUtils.isEmpty(mNickName.getText().toString())) {
					YSToast.showToast(mActivity, R.string.toast_petname_notnull);
					return;
				} else if (mNickName.getText().toString().trim().length() < 2) {
					YSToast.showToast(mActivity, R.string.toast_petname_lengtherror);
					return;
				} else {
					petObj.setName(mNickName.getText().toString().trim());
				}
				String reg = "^[a-zA-Z0-9_\u4e00-\u9fa5\\s]+$";
				Pattern p1 = Pattern.compile(reg);
				Matcher m1 = p1.matcher(mNickName.getText().toString());
				if (!m1.matches()) {
					YSToast.showToast(mActivity, R.string.toast_petnickname_format_error);
					return;
				}

				if (TextUtils.isEmpty(petObj.getGender())) {
					YSToast.showToast(mActivity, R.string.toast_petsex_notnull);
					return;
				}

				if (TextUtils.isEmpty(Constant.petStyle)) {
					YSToast.showToast(mActivity, R.string.toast_petstyle_notnull);
					return;
				} else {
					petObj.setCategoryId(Constant.petStyle);
				}
				if (TextUtils.isEmpty(petObj.getBirthday())) {
					YSToast.showToast(mActivity, R.string.toast_petbirthday_notnull);
					return;
				}
				if (!TextUtils.isEmpty(Constant.petIntro)) {
					petObj.setDescription(Constant.petIntro);
				}
				sendMessage(YSMSG.REQ_ADDPET, 0, 0, petObj);
				showWaitingDialog();
			}
		});
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == Snapshot_Action_Id) {
				String imageFile = "";
				if (null != mSnapshot) {
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);

					// imageFile = mSnapshot.onActivityResultProc(dm.widthPixels
					// > dm.heightPixels ? dm.widthPixels
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

			} else if (requestCode == Clip_Action_Id) {
				if (data != null && data.getExtras() != null) {
					Bitmap bitmap = data.getExtras().getParcelable("data");
					if (bitmap != null) {
						String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						String strImgPath = FileUtils.COVER;
						File out = new File(strImgPath);
						if (!out.exists()) {
							out.mkdirs();
						}
						String cropPath = strImgPath + fileName + FileUtils.JPG;
						if (ImageUtils.saveBitmapToFile(bitmap, cropPath, Snapshot.AVATAR_PERCENT)) {
							petObj.setIcoUrl(cropPath);
							FrescoHelper.displayImageview(mImage, "file://" + cropPath,
									R.drawable.avatar_default_image, getResources(), true);
							mHasAvatar = true;
						}
						ImageUtils.recycleBitmap(bitmap);
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addpet_styleLL :
				PetStyleActivity.startActivity(mActivity);
				break;
			case R.id.addpet_introLL :
				PetDescActivity.startActivity(mActivity, descTv.getText().toString());
				break;
			case R.id.addpet_birthdayLL :
				timePW.showAtLocation(birthdayRL, Gravity.BOTTOM, 0, 0, new Date());
				break;
			case R.id.addpet_man :
				petObj.setGender("1");
				GGImage.setImageResource(R.drawable.account_pet_gg);
				MMImage.setImageResource(R.drawable.account_pet_default);
				GGTv.setTextColor(Color.parseColor("#666666"));
				MMTv.setTextColor(Color.parseColor("#999999"));
				break;
			case R.id.addpet_woman :
				petObj.setGender("0");
				MMImage.setImageResource(R.drawable.account_pet_mm);
				GGImage.setImageResource(R.drawable.account_pet_default);
				GGTv.setTextColor(Color.parseColor("#999999"));
				MMTv.setTextColor(Color.parseColor("#666666"));
				break;
			default :
				break;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_ADDPET : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (isFromRegiste) {
						RegisteAttentionActivity.startActivity(mActivity);
					}
					finish();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			default :
				break;
		}
	}
}
