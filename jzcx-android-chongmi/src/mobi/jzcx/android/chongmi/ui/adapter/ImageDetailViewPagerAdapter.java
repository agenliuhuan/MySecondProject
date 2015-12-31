package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.main.homepage.ImageDetailListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.core.view.photoview.PhotoView;
import mobi.jzcx.android.core.view.photoview.PhotoViewAttacher.OnViewTapListener;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ImageDetailViewPagerAdapter extends PagerAdapter {
	Context mContext;
	ArrayList<String> mListViews;

	public ImageDetailViewPagerAdapter(Context context, ArrayList<String> list) {
		this.mContext = context;
		mListViews = list;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	ImageDetailListener imageListener;

	public void setImageListener(ImageDetailListener imageListener) {
		this.imageListener = imageListener;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_viewpaper_image, null);
		if (!CommonTextUtils.isEmpty(mListViews.get(position))) {
			PhotoView photoview = (PhotoView) convertView.findViewById(R.id.viewpaper_image);
			ImageLoaderHelper.displayImage(mListViews.get(position) + CommonUtils.getImageSize(mContext), photoview,
					R.drawable.image_default_image, false);
			photoview.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (imageListener != null) {
						imageListener.onLongClick();
					}

					return false;
				}
			});
			photoview.setOnViewTapListener(new OnViewTapListener() {

				@Override
				public void onViewTap(View view, float x, float y) {
					if (imageListener != null) {
						imageListener.onViewTap();
					}

				}
			});
			// PhotoViewAttacher mAttacher = new
			// PhotoViewAttacher(photoview);
			// Matrix matris = new Matrix();
			// matris.setScale(1, 1);
			// mAttacher.setDisplayMatrix(matris);

			// photoview.setOnViewTapListener(new OnViewTapListener() {
			// @Override
			// public void onViewTap(View view, float x, float y) {
			// finish();
			// }
			// });
			// photoview.setOnLongClickListener(new OnLongClickListener() {
			// @Override
			// public boolean onLongClick(View v) {
			// downWindow = new
			// ReportOrDeletePopupWindow(VPagerImageDetailsActivity.this,
			// downOnClick);
			// downWindow.setText(getString(R.string.petdiaryimage_down));
			// downWindow.showAtLocation(VPagerImageDetailsActivity.this.findViewById(R.id.vp_image_details),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			// return false;
			// }
			// });
		}
		return convertView;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_viewpaper_image, null);
		if (!CommonTextUtils.isEmpty(mListViews.get(position))) {
			PhotoView photoview = (PhotoView) convertView.findViewById(R.id.viewpaper_image);
			ImageLoaderHelper.displayImage(mListViews.get(position) + CommonUtils.getImageSize(mContext), photoview,
					R.drawable.image_default_image, false);
			photoview.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (imageListener != null) {
						imageListener.onLongClick();
					}

					return false;
				}
			});
			photoview.setOnViewTapListener(new OnViewTapListener() {

				@Override
				public void onViewTap(View view, float x, float y) {
					if (imageListener != null) {
						imageListener.onViewTap();
					}

				}
			});
			// PhotoViewAttacher mAttacher = new
			// PhotoViewAttacher(photoview);
			// Matrix matris = new Matrix();
			// matris.setScale(1, 1);
			// mAttacher.setDisplayMatrix(matris);

			// photoview.setOnViewTapListener(new OnViewTapListener() {
			// @Override
			// public void onViewTap(View view, float x, float y) {
			// finish();
			// }
			// });
			// photoview.setOnLongClickListener(new OnLongClickListener() {
			// @Override
			// public boolean onLongClick(View v) {
			// downWindow = new
			// ReportOrDeletePopupWindow(VPagerImageDetailsActivity.this,
			// downOnClick);
			// downWindow.setText(getString(R.string.petdiaryimage_down));
			// downWindow.showAtLocation(VPagerImageDetailsActivity.this.findViewById(R.id.vp_image_details),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			// return false;
			// }
			// });
		}
		container.addView(convertView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		return convertView;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewGroup) container).removeView((View) object);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
}