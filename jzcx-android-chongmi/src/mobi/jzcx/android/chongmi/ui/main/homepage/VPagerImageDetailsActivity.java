package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.adapter.HomeViewPagerAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.ImageDetailViewPagerAdapter;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.chongmi.view.ReportOrDeletePopupWindow;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.client.AsyncHttpClient;
import mobi.jzcx.android.core.net.http.handler.BinaryHttpResponseHandler;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.photoview.PhotoView;
import mobi.jzcx.android.core.view.photoview.PhotoViewAttacher;
import mobi.jzcx.android.core.view.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import mobi.jzcx.android.core.view.photoview.PhotoViewAttacher.OnViewTapListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

public class VPagerImageDetailsActivity extends Activity implements ImageDetailListener {
	private ViewPager mViewPager;
	static ArrayList<String> urlList;
	static int index;
	ReportOrDeletePopupWindow downWindow;

	public static void startActivity(Context context, ArrayList<String> urlList, int curIndex) {
		ActivityUtils.startActivity(context, VPagerImageDetailsActivity.class);
		VPagerImageDetailsActivity.urlList = urlList;
		VPagerImageDetailsActivity.index = curIndex;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vp_image_details);
		initView();
	}

	private void initView() {
		initViewPager();
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.vp_image_details);
		ImageDetailViewPagerAdapter imageAdapter = new ImageDetailViewPagerAdapter(this, urlList);
		mViewPager.setAdapter(imageAdapter);
		imageAdapter.setImageListener(this);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// upddateCount(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mViewPager.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				return false;
			}
		});
		mViewPager.setCurrentItem(index, true);
	}
	private OnClickListener downOnClick = new OnClickListener() {

		public void onClick(View v) {
			downWindow.dismiss();
			switch (v.getId()) {
				case R.id.reportOrDeleteBtn :
					if (urlList != null && mViewPager != null) {
						String url = urlList.get(mViewPager.getCurrentItem())
								+ CommonUtils.getImageSize(VPagerImageDetailsActivity.this);
						if (FileUtils.exists(FileUtils.PICTURE + MD5.md5(url) + FileUtils.JPG)) {
							FileUtils.delFile(FileUtils.PICTURE + MD5.md5(url) + FileUtils.JPG);
						}
						try {
							downloadFile(url, FileUtils.PICTURE + MD5.md5(url) + FileUtils.JPG);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				default :
					break;
			}
		}
	};

	/**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	private void downloadFile(String url, final String fileName) throws Exception {

		AsyncHttpClient client = new AsyncHttpClient();
		// 指定文件类型
		String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
		// 获取二进制数据如图片和其他文件
		client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

			@Override
			public void onSuccess(int statusCode, byte[] binaryData) {

				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

				File file = new File(fileName);
				// 压缩格式
				CompressFormat format = Bitmap.CompressFormat.JPEG;
				// 压缩比例
				int quality = 100;
				try {
					// 若存在则删除
					if (file.exists())
						file.delete();
					// 创建文件
					file.createNewFile();
					//
					OutputStream stream = new FileOutputStream(file);
					// 压缩输出
					bmp.compress(format, quality, stream);
					// 关闭
					stream.close();
					//

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				YSToast.showToast(App.getInstance(), App.getInstance().getString(R.string.toast_imagedown_success)
						+ FileUtils.PICTURE);
				setscan();
			}
			@Override
			@Deprecated
			public void onFailure(Throwable error, byte[] binaryData) {
				// TODO Auto-generated method stub
				super.onFailure(error, binaryData);
			}

		});
	}

	public void setscan() {
		if (hasKitkat()) {
			MediaScannerConnection.scanFile(this, new String[]{FileUtils.PICTURE}, new String[]{"image/*"},
					new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE, uri));
							sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
						}
					});
			scanPhotos(FileUtils.PICTURE, this); // 实际起作用的方法
		} else {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
					+ Environment.getExternalStorageDirectory())));
		}
	}

	public void scanPhotos(String filePath, Context context) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(new File(filePath));
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	public boolean hasKitkat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	private ArrayList<View> addShowView(final ArrayList<String> urls) {
		ArrayList<View> mListViews = new ArrayList<View>();
		if (urls != null && urls.size() > 0) {
			for (int i = 0; i < urls.size(); i++) {
				View convertView = LayoutInflater.from(VPagerImageDetailsActivity.this).inflate(
						R.layout.listitem_viewpaper_image, null);
				if (!CommonTextUtils.isEmpty(urls.get(i))) {
					PhotoView photoview = (PhotoView) convertView.findViewById(R.id.viewpaper_image);
					ImageLoaderHelper.displayImage(urls.get(i) + CommonUtils.getImageSize(this), photoview,
							R.drawable.image_default_image, false);

					// PhotoViewAttacher mAttacher = new
					// PhotoViewAttacher(photoview);
					// Matrix matris = new Matrix();
					// matris.setScale(1, 1);
					// mAttacher.setDisplayMatrix(matris);

					photoview.setOnViewTapListener(new OnViewTapListener() {
						@Override
						public void onViewTap(View view, float x, float y) {
							finish();
						}
					});
					photoview.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							downWindow = new ReportOrDeletePopupWindow(VPagerImageDetailsActivity.this, downOnClick);
							downWindow.setText(getString(R.string.petdiaryimage_down));
							downWindow.showAtLocation(
									VPagerImageDetailsActivity.this.findViewById(R.id.vp_image_details), Gravity.BOTTOM
											| Gravity.CENTER_HORIZONTAL, 0, 0);
							return false;
						}
					});
				}
				mListViews.add(convertView);
			}
		} else {
			return null;
		}
		return mListViews;
	}

	@Override
	public void onViewTap() {
		finish();

	}

	@Override
	public void onLongClick() {
		downWindow = new ReportOrDeletePopupWindow(VPagerImageDetailsActivity.this, downOnClick);
		downWindow.setText(getString(R.string.petdiaryimage_down));
		downWindow.showAtLocation(VPagerImageDetailsActivity.this.findViewById(R.id.vp_image_details), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}
}