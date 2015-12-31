package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImageAdapter extends BaseAdapter {
	ArrayList<String> mList;
	Context mContext;

	public ImageAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<String>();
	}

	public void updateList(ArrayList<String> list) {
		if (mList != null) {
			mList.clear();
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_image, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int width = CommonUtils.getScreenWidth(mContext);
		LayoutParams params = (LayoutParams) holder.image.getLayoutParams();
		params.width = (width - 50) / 4;
		params.height = (width - 50) / 4;
		holder.image.setLayoutParams(params);
		String uriPath = getItem(position);
		if (!CommonTextUtils.isEmpty(uriPath)) {
			FrescoHelper.displayImageview(holder.image, uriPath, 0, null, mContext.getResources(), true, 10.0f);
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView image;

		public ViewHolder(View view) {
			this.image = (SimpleDraweeView) view.findViewById(R.id.petdiaryimg_image);

		}
	}

}
