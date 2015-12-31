package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.core.view.photoview.PhotoView;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class VPagerImageDetailsAdapter extends PagerAdapter {
	Context mContext;
	ArrayList<String> imageurls;

	public VPagerImageDetailsAdapter(Context context) {
		this.mContext = context;
		imageurls = new ArrayList<String>();
	}

	public void updateList(ArrayList<String> data) {
		if (imageurls != null) {
			imageurls.clear();
			imageurls.addAll(data);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imageurls.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photo_view = new PhotoView(mContext);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		photo_view.setLayoutParams(p);
		if (!CommonTextUtils.isEmpty(imageurls.get(position))) {
			loadImage(photo_view, imageurls.get(position));
		}
		container.addView(photo_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return photo_view;
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

	private void loadImage(ImageView simple_image, String url) {
		ImageLoaderHelper.displayImage(url, simple_image);
	}
}