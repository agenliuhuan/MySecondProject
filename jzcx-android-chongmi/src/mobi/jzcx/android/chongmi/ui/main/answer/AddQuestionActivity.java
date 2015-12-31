package mobi.jzcx.android.chongmi.ui.main.answer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AddQuestionObject;
import mobi.jzcx.android.chongmi.ui.adapter.QuestionPictureAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.ImageDelListener;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.Snapshot;
import mobi.jzcx.android.chongmi.view.ImagePickerPopupWindow;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

public class AddQuestionActivity extends BaseExActivity implements ImageDelListener {
	protected TitleBarHolder mTitleBar = null;
	EditText titleEdit;
	TextView titleLength;
	EditText contextEdit;
	GridView grid;
	TextView contextLength;
	private QuestionPictureAdapter mAdapter;
	private ArrayList<String> imageList;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, AddQuestionActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addquestion);
		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
		titleEdit = (EditText) findViewById(R.id.addquestion_title);
		titleLength = (TextView) findViewById(R.id.addquestion_titleLength);
		contextEdit = (EditText) findViewById(R.id.addquestion_context);
		grid = (GridView) findViewById(R.id.addquestion_grid);
		contextLength = (TextView) findViewById(R.id.addquestion_contextLength);

		titleEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				titleLength.setText(String.format(getString(R.string.addquestion_title_length), s.length()));
			}
		});
		contextEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				contextLength.setText(String.format(getString(R.string.addquestion_content_length), s.length()));
			}
		});
		ScrollView scrollview = (ScrollView) findViewById(R.id.addquestion_scrollview);
		scrollview.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mRightTv.setText(getString(R.string.addquestion_title_righttext));
		mTitleBar.mLeftImg.setBackgroundResource(R.drawable.addquestion_close);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AddQuestionActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (titleEdit.getText().length() == 0) {
					YSToast.showToast(mActivity, getString(R.string.toast_addquestion_notnull));
					return;
				}
				AddQuestionObject addquestion = new AddQuestionObject();
				addquestion.setTitle(titleEdit.getText().toString());
				if (contextEdit.getText().length() > 0) {
					addquestion.setContext(contextEdit.getText().toString());
				}
				if (imageList.contains("addImage")) {
					imageList.remove("addImage");
				}
				addquestion.setPhotos(imageList);
				showWaitingDialog();
				sendMessage(YSMSG.REQ_QuestionAdd, 0, 0, addquestion);
			}
		});
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_QuestionAdd :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					mActivity.finish();
					YSToast.showToast(mActivity, getString(R.string.toast_addquestion_success));
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			default :
				break;
		}
	};

	Snapshot mSnapshot;
	boolean hasAddImg = true;

	private int Snapshot_Action_Id = 10087;
	private int Photos_Action_Id = 10088;
	private int Clip_Action_Id = 10089;

	private void initData() {
		mSnapshot = new Snapshot();
		mAdapter = new QuestionPictureAdapter(mActivity);
		imageList = new ArrayList<String>();
		imageList.add("addImage");
		mAdapter.updateList(imageList);
		mAdapter.setImageDellistener(this);
		grid.setAdapter(mAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filePath = mAdapter.getItem(position);
				if (filePath.equals("addImage")) {
					imagePickerPopview = new ImagePickerPopupWindow(mActivity, imagePickerOnClick);
					imagePickerPopview.showAtLocation(findViewById(R.id.addquestion_grid), Gravity.CENTER, 0, 0);
				} 
//				else {
//					if (!hasAddImg) {
//						imageList.add(imageList.size(), "addImage");
//						hasAddImg = true;
//					}
//					// imageList.remove(filePath);
//					mAdapter.updateList(imageList);
//				}
			}
		});
		titleLength.setText(String.format(getString(R.string.addquestion_title_length), 0));
		contextLength.setText(String.format(getString(R.string.addquestion_content_length), 0));
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
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Snapshot_Action_Id) {
			String imageFile = "";
			if (null != mSnapshot) {
				DisplayMetrics dm = new DisplayMetrics();
				AddQuestionActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
					int length = imageList.size();
					imageList.add(length - 1, cropPath);
					if (imageList.size() == 5) {
						// removeAddimg();
						imageList.remove("addImage");
						hasAddImg = false;
					}
					mAdapter.updateList(imageList);
				} else {

				}
				ImageUtils.recycleBitmap(bitmap);
			}
		}
	}

	@Override
	public void imageDelClick(String filepath) {
		imageList.remove(filepath);
		if (!imageList.contains("addImage")) {
			imageList.add("addImage");
			hasAddImg = true;
		}
		mAdapter.updateList(imageList);
		mAdapter.notifyDataSetChanged();
	}
}
