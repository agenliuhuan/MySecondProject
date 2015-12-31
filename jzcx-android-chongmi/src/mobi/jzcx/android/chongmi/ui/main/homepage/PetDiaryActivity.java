package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.MicroBlogObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.ui.adapter.PetDiaryPetAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.PetDiaryPictureAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;

public class PetDiaryActivity extends BaseExActivity implements ImageDelListener {
	protected TitleBarHolder mTitleBar = null;
	private GridView mGrid;
	private PetDiaryPictureAdapter mAdapter;
	private ArrayList<String> imageList;

	private HorizontalListView petListView;
	private PetDiaryPetAdapter petAdapter;

	private Snapshot mSnapshot = null;

	PetObject petObj;

	EditText edit;
	boolean hasAddImg = true;

	private int Snapshot_Action_Id = 10087;
	private int Photos_Action_Id = 10088;
	private int Clip_Action_Id = 10089;
	static ArrayList<PetObject> petList;

	public static void startActivity(Context context, ArrayList<PetObject> petList) {
		ActivityUtils.startActivity(context, PetDiaryActivity.class);
		PetDiaryActivity.petList = petList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_diary);
		initView();
		imageList = new ArrayList<String>();
		imageList.add("addImage");
		mAdapter.updateList(imageList);
		mAdapter.setImageDellistener(this);

		setGridHeight();
		selectPosition = 0;
	}

	private void initView() {
		initTitleBar();
		initImagePicker();
		mGrid = (GridView) findViewById(R.id.petdiary_imageGrid);
		mAdapter = new PetDiaryPictureAdapter(this);
		mGrid.setAdapter(mAdapter);
		mGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filePath = mAdapter.getItem(position);
				if (filePath.equals("addImage")) {
					hideSoftKeyboard();
					imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
					imagePickerPopview.showAtLocation(findViewById(R.id.petdiary_title), Gravity.CENTER, 0, 0);
				}
				// else {
				// if (!hasAddImg) {
				// imageList.add(imageList.size(), "addImage");
				// hasAddImg = true;
				// }
				// // imageList.remove(filePath);
				// mAdapter.updateList(imageList);
				// }
			}
		});

		petListView = (HorizontalListView) findViewById(R.id.petdiary_hList);
		petAdapter = new PetDiaryPetAdapter(this);
		petListView.setAdapter(petAdapter);
		petListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectPosition = position;
				petObj = petAdapter.getItem(position);
				petAdapter.setSelection(position);
			}
		});

		edit = (EditText) findViewById(R.id.petdiary_edit);
		edit.clearFocus();
		edit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (null == temp) {
					return;
				}
				if (temp.length() == 200) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_petdiary_max_error));
				}

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	int selectPosition;

	private void initData() {
		petAdapter.updateList(petList);
		if (petList.size() > 0) {
			petObj = petAdapter.getItem(selectPosition);
			petAdapter.setSelection(selectPosition);
		}
	}

	private void setGridHeight() {
		LayoutParams params = (LayoutParams) mGrid.getLayoutParams();
		int width = CommonUtils.getScreenWidth(mActivity);
		if (imageList.size() < 5) {
			params.height = width / 4 + 3;
		} else if (imageList.size() < 9) {
			params.height = width / 4 * 2 - 10;
		}
		mGrid.setLayoutParams(params);
		mGrid.setVerticalSpacing(14);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_BLOGADD :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					// Constant.shouldRefreshMain = true;
					// LogUtils.i("HomePageFragment", Constant.shouldRefreshMain
					// + "");
					// sendEmptyMessage(YSMSG.REQ_MAIN_REFRESH);
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.REQ_MAIN_REFRESH;
					CoreModel.getInstance().notifyOutboxHandlers(message);
					hideSoftKeyboard();
					finish();
				} else {
					if (!imageList.contains("addImage")) {
						imageList.add(imageList.size() - 1, "addImage");
					}
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.petdiary_ttb_title));
		mTitleBar.mRightTv.setText(getString(R.string.petdiary_title_righttext));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PetDiaryActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (petObj == null) {
					YSToast.showToast(mActivity, getString(R.string.toast_petdiarty_pet));
					return;
				}
				if (imageList == null || imageList.size() == 1) {
					YSToast.showToast(mActivity, getString(R.string.toast_petdiarty_images));
					return;
				}
				if (imageList.contains("addImage")) {
					imageList.remove("addImage");
				}

				// if (CommonTextUtils.isEmpty(edit.getText().toString())) {
				// YSToast.showToast(mActivity,
				// getString(R.string.toast_petdiarty_text));
				// return;
				// }
				MicroBlogObject mBlogObj = new MicroBlogObject();
				if (App.getInstance().getLnglat() != null) {
					mBlogObj.setLnglat(App.getInstance().getLnglat());
				}
				mBlogObj.setPetId(petObj.getPetId());
				mBlogObj.setPhotos(imageList);
				mBlogObj.setText(edit.getText().toString().trim());
				sendMessage(YSMSG.REQ_BLOGADD, 0, 0, mBlogObj);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Snapshot_Action_Id) {
			String imageFile = "";
			if (null != mSnapshot) {
				DisplayMetrics dm = new DisplayMetrics();
				PetDiaryActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
				String cropPath = strImgPath + fileName + FileUtils.PNG;
				if (ImageUtils.saveBitmapToFile(bitmap, cropPath, Snapshot.AVATAR_PERCENT)) {
					int length = imageList.size();
					imageList.add(length - 1, cropPath);
					if (imageList.size() == 7) {
						// removeAddimg();
						imageList.remove("addImage");
						hasAddImg = false;
					}
					mAdapter.updateList(imageList);
					setGridHeight();
				} else {

				}
				ImageUtils.recycleBitmap(bitmap);
			}
		}
	}

	private void removeAddimg() {
		for (int i = 0; i < imageList.size(); i++) {
			if (imageList.get(i).equals("addImage")) {
				imageList.remove(i);
				hasAddImg = false;
			}
		}
	}

	@Override
	public void imageDelClick(String filepath) {
		LogUtils.i("PetDiaryPictureAdapter", "imageDelClick");
		imageList.remove(filepath);
		if (!imageList.contains("addImage")) {
			imageList.add("addImage");
			hasAddImg = true;
		}
		mAdapter.updateList(imageList);
		mAdapter.notifyDataSetChanged();

	}
}