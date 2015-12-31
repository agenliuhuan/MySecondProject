package mobi.jzcx.android.chongmi.ui.group;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.EventObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.OnTabActivityResultListener;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pickerview.OptionsPopupWindow;
import mobi.jzcx.android.core.view.pickerview.OptionsPopupWindow.OnOptionsSelectListener;
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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class CreateEventActivity extends BaseExActivity implements OnClickListener, OnTabActivityResultListener {

	protected TitleBarHolder mTitleBar;

	Snapshot mSnapshot;

	private int Snapshot_Action_Id = 10087;
	private int Photos_Action_Id = 10088;
	private int Clip_Action_Id = 10089;

	private PercentRelativeLayout seticonRL;
	private PercentRelativeLayout creat_event_title, creat_event_date, creat_event_location, creat_event_mebernum,
			creat_event_desc;
	private TextView creat_event_title_edt, creat_event_date_edt, creat_event_location_edt, creat_event_mebernum_edt,
			creat_event_desc_edt;
	private SimpleDraweeView seticonImg;
	private TimePopupWindow timePW;
	private OptionsPopupWindow optionPW;
	EventObject eventObj;
	boolean updateIcon = false;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, CreateEventActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_event);
		initView();
		Constant.eventTitle = "";
		Constant.eventlocation = null;
		Constant.eventDesc = "";
	}

	@Override
	protected void onResume() {
		super.onResume();
		initdata();
	}

	private void initdata() {
		if (!TextUtils.isEmpty(Constant.eventTitle)) {
			creat_event_title_edt.setText(Constant.eventTitle);
			eventObj.setTitle(Constant.eventTitle);
		}
		if (Constant.eventlocation != null) {
			creat_event_location_edt.setText(Constant.eventlocation.getAdress());
			eventObj.setAddress(Constant.eventlocation.getAdress());
			eventObj.setLnglat(Constant.eventlocation.getLng() + "," + Constant.eventlocation.getLat());
		}

		if (!TextUtils.isEmpty(Constant.eventDesc)) {
			creat_event_desc_edt.setText(Constant.eventDesc);
			eventObj.setIntro(Constant.eventDesc);
		}

	}

	private void initView() {
		initTitleBar();
		initImagePicker();
		seticonRL = (PercentRelativeLayout) findViewById(R.id.creat_event_seticonRL);
		seticonImg = (SimpleDraweeView) findViewById(R.id.creat_event_seticon_image);
		seticonRL.setOnClickListener(this);

		creat_event_title = (PercentRelativeLayout) findViewById(R.id.creat_event_titleRL);
		creat_event_date = (PercentRelativeLayout) findViewById(R.id.creat_event_dateRL);
		creat_event_location = (PercentRelativeLayout) findViewById(R.id.creat_event_locationRL);
		creat_event_mebernum = (PercentRelativeLayout) findViewById(R.id.creat_event_mebernumRL);
		creat_event_desc = (PercentRelativeLayout) findViewById(R.id.creat_event_descRL);

		creat_event_title_edt = (TextView) findViewById(R.id.creat_event_title_edt);
		creat_event_date_edt = (TextView) findViewById(R.id.creat_event_date_edt);
		creat_event_location_edt = (TextView) findViewById(R.id.creat_event_location_edt);
		creat_event_mebernum_edt = (TextView) findViewById(R.id.creat_event_mebernum_edt);
		creat_event_desc_edt = (TextView) findViewById(R.id.creat_event_desc_edt);

		creat_event_title.setOnClickListener(this);
		creat_event_date.setOnClickListener(this);
		creat_event_location.setOnClickListener(this);
		creat_event_mebernum.setOnClickListener(this);
		creat_event_desc.setOnClickListener(this);

		timePW = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		timePW.setRange(year - 100, year + 100);
		timePW.setCyclic(true);
		timePW.setOnTimeSelectListener(new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date) {
				Date now = new Date();
				Calendar calender = Calendar.getInstance();
				calender.setTime(now);
				calender.add(calender.DATE, 30);
				Date nextmonth = calender.getTime();
				if (date.before(now)) {
					YSToast.showToast(mActivity, getString(R.string.toast_datebefore_text));
				} else if (date.after(nextmonth)) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventdateafter_text));
				} else {
					String time = CommonUtils.getTime(date);
					creat_event_date_edt.setText(time);
					eventObj.setBeginTime(time);
				}
			}
		});
		eventObj = new EventObject();

		optionPW = new OptionsPopupWindow(this);
		ArrayList<String> numArray = new ArrayList<String>();
		for (int i = 2; i < 31; i++) {
			numArray.add(i + getString(R.string.ren));
		}
		optionPW.setPicker(numArray);
		optionPW.setCyclic(true);
		optionPW.setOnoptionsSelectListener(new OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				creat_event_mebernum_edt.setText(String.valueOf(options1 + 2));
				eventObj.setMaxMemberCount(options1 + 2);
			}
		});
	}
	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.mRightTv.setText(getString(R.string.createevent_title_right));
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (CommonTextUtils.isEmpty(eventObj.getIcoUrl())) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventicon_notnull));
					return;
				}
				if (CommonTextUtils.isEmpty(eventObj.getTitle())) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventtitle_notnull));
					return;
				}
				if (CommonTextUtils.isEmpty(eventObj.getBeginTime())) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventbegintime_notnull));
					return;
				}
				if (CommonTextUtils.isEmpty(eventObj.getLnglat())) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventadress_notnull));
					return;
				}
				if (eventObj.getMaxMemberCount() < 2 || eventObj.getMaxMemberCount() > 30) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventnum_notnull));
					return;
				}
				if (CommonTextUtils.isEmpty(eventObj.getIntro())) {
					YSToast.showToast(mActivity, getString(R.string.toast_eventdesc_notnull));
					return;
				}
				sendMessage(YSMSG.REQ_CREATEACTIVITY, 0, 0, eventObj);
				showWaitingDialog();
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
		if (requestCode == Snapshot_Action_Id) {
			String imageFile = "";
			if (null != mSnapshot) {
				DisplayMetrics dm = new DisplayMetrics();
				CreateEventActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

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
				intent.putExtra("outputX", Snapshot.AVATAR_WIDTH_HEIGHT);
				intent.putExtra("outputY", Snapshot.AVATAR_WIDTH_HEIGHT);
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
				intent.putExtra("outputX", Snapshot.AVATAR_WIDTH_HEIGHT);
				intent.putExtra("outputY", Snapshot.AVATAR_WIDTH_HEIGHT);
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
					updateIcon = true;
					FrescoHelper.displayImageview(seticonImg, "file://" + cropPath, 0, getResources(), true);
					eventObj.setIcoUrl(cropPath);
				}
				ImageUtils.recycleBitmap(bitmap);
			}
		}
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_CREATEACTIVITY :
				dismissWaitingDialog();
				// "ActivityId":"55efe640ccac1e26e0102aae","ImGroupId":"g8895951"
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							Constant.eventDesc = "";
							Constant.eventlocation = null;
							Constant.eventTitle = "";
							JSONObject json = new JSONObject(result);
							String ActivityId = json.getString("ActivityId");
							YSToast.showToast(mActivity, getString(R.string.toast_createevent_success));
							// if (updateIcon) {
							// sendMessage(YSMSG.REQ_UPDATEACTIVITYICON, 0, 0,
							// ActivityId);
							// }
							mActivity.finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_UPDATEACTIVITYICON :
				if (msg.arg1 == 200) {
					// File iconFile = new File(FileUtils.ACTIVITY_ICON);
					// if (iconFile.exists()) {
					// iconFile.delete();
					// }
					// finish();
				}
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
			case R.id.creat_event_seticonRL :
				imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
				imagePickerPopview.showAtLocation(findViewById(R.id.createvent_title), Gravity.CENTER, 0, 0);
				break;
			case R.id.creat_event_titleRL :
				EventBiaotiActivity.startActivity(mActivity, Constant.eventTitle);
				break;
			case R.id.creat_event_dateRL :

				Date now = new Date();
				Calendar calender = Calendar.getInstance();
				calender.setTime(now);
				calender.add(calender.DATE, 1);
				Date tomorrow = calender.getTime();
				timePW.showAtLocation(creat_event_date, Gravity.BOTTOM, 0, 0, tomorrow);
				break;
			case R.id.creat_event_locationRL :
				startActivity(new Intent(mActivity, EventDidianActivity.class));
				break;
			case R.id.creat_event_mebernumRL :
				// startActivity(new Intent(mActivity, EventNumActivity.class));
				optionPW.showAtLocation(creat_event_date, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.creat_event_descRL :
				EventDescActivity.startActivity(mActivity, eventObj.getIntro());
				break;

			default :
				break;
		}
	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {

	}

}
