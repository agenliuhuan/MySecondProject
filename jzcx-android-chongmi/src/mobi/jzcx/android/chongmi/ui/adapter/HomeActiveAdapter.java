package mobi.jzcx.android.chongmi.ui.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class HomeActiveAdapter extends BaseAdapter {
	ArrayList<ActivityObject> mActiveList;
	Context mContext;

	public HomeActiveAdapter(Context context) {
		this.mContext = context;
		mActiveList = new ArrayList<ActivityObject>();
	}

	public int getCount() {
		return mActiveList.size();
	}

	public ActivityObject getItem(int position) {
		return mActiveList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setData(ArrayList<ActivityObject> data) {
		if (mActiveList != null) {
			mActiveList.clear();
			mActiveList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_home_active, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ActivityObject activeObj = mActiveList.get(position);
		if (activeObj != null) {
			if (!CommonTextUtils.isEmpty(activeObj.getIcoUrl())) {
				ImageLoaderHelper.displayActivityImage(activeObj.getIcoUrl(), holder.imgActive,
						R.drawable.image_default_image, true, 30);

			}
			if (!TextUtils.isEmpty(activeObj.getTitle())) {
				holder.tvTitle.setText(activeObj.getTitle());
			}
			if (activeObj.getMaxUserCount() != 0) {
				holder.tvNum.setText(mContext.getString(R.string.active_numtext) + activeObj.getMaxUserCount() + "/"
						+ activeObj.getMemberCount());
			}
			if (activeObj.getLnglatObj() != null) {
				LatLng cp = new LatLng(Double.valueOf(activeObj.getLnglatObj().getLat()), Double.valueOf(activeObj
						.getLnglatObj().getLng()));
				if (App.getInstance().getLnglat() != null) {
					LngLatObject mylnglat = App.getInstance().getLnglat();
					LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()), Double.valueOf(mylnglat.getLng()));
					NumberFormat format = NumberFormat.getNumberInstance();
					format.setMaximumFractionDigits(2);
					String distance = format.format(DistanceUtil.getDistance(cp, mp) / 1000);
					holder.tvRange.setText(distance + mContext.getString(R.string.homepage_distance_text));
				}
			}
			if (!TextUtils.isEmpty(activeObj.getIntro())) {
				holder.tvContent.setText(activeObj.getIntro());
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imgActive;
		TextView tvTitle;
		TextView tvNum;
		TextView tvRange;
		TextView tvContent;

		public ViewHolder(View view) {
			this.imgActive = (ImageView) view.findViewById(R.id.img_active);
			this.tvTitle = (TextView) view.findViewById(R.id.tv_active_title);
			this.tvNum = (TextView) view.findViewById(R.id.tv_active_num);
			this.tvRange = (TextView) view.findViewById(R.id.tv_active_range);
			this.tvContent = (TextView) view.findViewById(R.id.tv_active_content);
		}
	}

}