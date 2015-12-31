package mobi.jzcx.android.chongmi.ui.register;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.InputUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class RegisteOverActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;

	private Snapshot mSnapshot = null;

	public int Snapshot_Action_Id = 10087;
	public int Photos_Action_Id = 10088;
	public int Clip_Action_Id = 10089;

	SimpleDraweeView mImage = null;

	EditText mNickName = null;

	String sex = "-1";
	String userAvatar;
	TimePopupWindow timePW;
	PercentRelativeLayout birthdayRL;
	TextView birthdayTv;
	boolean setBirthday = false;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, RegisteOverActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeover);
		initView();
	}

	private void initView() {
		initTitleBar();

		initImagePicker();

		mImage = (SimpleDraweeView) findViewById(R.id.registeover_image);
		mImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);
				imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
				imagePickerPopview.showAtLocation(findViewById(R.id.registeover_image), Gravity.CENTER, 0, 0);
			}
		});
		mNickName = (EditText) this.findViewById(R.id.registeover_nickname);
		mNickName.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null == temp) {
					return;
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
		PercentLinearLayout manLL = (PercentLinearLayout) findViewById(R.id.registover_manLL);
		PercentLinearLayout womanLL = (PercentLinearLayout) findViewById(R.id.registover_womanLL);
		final ImageView manImg = (ImageView) findViewById(R.id.registover_manImg);
		final ImageView womanImg = (ImageView) findViewById(R.id.registover_womanImg);

		manLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manImg.setImageResource(R.drawable.account_man);
				womanImg.setImageResource(R.drawable.account_default);
				sex = "1";
			}
		});
		womanLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manImg.setImageResource(R.drawable.account_default);
				womanImg.setImageResource(R.drawable.account_man);
				sex = "0";
			}
		});
		timePW = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		timePW.setRange(year - 100, year + 100);
		timePW.setCyclic(true);
		birthdayRL = (PercentRelativeLayout) findViewById(R.id.birthdayRL);
		birthdayTv = (TextView) findViewById(R.id.birthdayText);
		birthdayRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePW.showAtLocation(birthdayRL, Gravity.BOTTOM, 0, 0, new Date());
			}
		});
		timePW.setOnTimeSelectListener(new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date) {
				Date now = new Date();
				if (date.after(now)) {
					YSToast.showToast(mActivity, getString(R.string.toast_dateafter_text));
				} else {
					setBirthday = true;
					birthdayTv.setText(CommonUtils.getTime(date));
				}
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(mActivity);
		mTitleBar.mTitle.setText(R.string.registeover_title);
		mTitleBar.mRightImg.setVisibility(View.GONE);
		mTitleBar.mRightTv.setVisibility(View.VISIBLE);
		mTitleBar.mRightTv.setText(R.string.registeover_title_righttext);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registeOver();
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
					intent.putExtra("return-data", true);
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
				intent.putExtra("return-data", true);
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

							setAvatar(cropPath);
						}
						ImageUtils.recycleBitmap(bitmap);
					}
				}
			}
		}
	}

	private void registeOver() {
		// 隐藏软键盘
		InputUtils.closeInput(mActivity);

		UserObject user = new UserObject();
		if (CommonTextUtils.isEmpty(userAvatar)) {
			YSToast.showToast(mActivity, getString(R.string.toast_useravatar_notnull));
			return;
		} else {
			user.setIcoUrl(userAvatar);
		}
		if (CommonTextUtils.isEmpty(mNickName.getText().toString())) {
			YSToast.showToast(mActivity, getString(R.string.toast_usernickname_notnull));
			return;
		} else {
			user.setNickName(mNickName.getText().toString());
		}

		String reg = "^[a-zA-Z0-9_\u4e00-\u9fa5\\s]+$";
		Pattern p1 = Pattern.compile(reg);
		Matcher m1 = p1.matcher(mNickName.getText().toString());
		if (!m1.matches()) {
			YSToast.showToast(RegisteOverActivity.this, R.string.toast_nickname_format_error);
			return;
		}

		if (sex.equals("-1")) {
			YSToast.showToast(mActivity, getString(R.string.toast_usersex_notnull));
			return;
		} else {
			user.setGender(sex);
		}

		if (!setBirthday) {
			YSToast.showToast(mActivity, getString(R.string.toast_userbirthday_notnull));
			return;
		} else {
			user.setBirthday(birthdayTv.getText().toString());
		}
		sendMessage(YSMSG.REQ_UPDATE_INFO, 0, 0, user);
		showWaitingDialog();
	}

	private void setAvatar(String filePath) {
		userAvatar = filePath;
		FrescoHelper.displayImageview(mImage, "file://" + filePath, R.drawable.avatar_default_image, getResources(),
				true);
	}

	private String getNickNameByRegister(String phone) {
		return phone.substring(7, 11);
	}

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
						}
						if (!CommonTextUtils.isEmpty(myself.getBirthday())) {
							CoreModel.getInstance().getMyself().setBirthday(myself.getBirthday());
						}
						if (!CommonTextUtils.isEmpty(myself.getNickName())) {
							CoreModel.getInstance().getMyself().setNickName(myself.getNickName());
						}
						if (!CommonTextUtils.isEmpty(myself.getGender())) {
							CoreModel.getInstance().getMyself().setGender(myself.getGender());
						}
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
					RegisteOverActivity.this.finish();
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
