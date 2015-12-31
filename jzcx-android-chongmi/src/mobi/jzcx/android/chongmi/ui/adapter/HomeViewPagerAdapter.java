package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HomeViewPagerAdapter extends PagerAdapter {
	Context mContext;
	ArrayList<View> mListViews;

	public HomeViewPagerAdapter(Context context, ArrayList<View> list) {
		this.mContext = context;
		mListViews = list;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		((ViewPager) collection).addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView(mListViews.get(position));
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