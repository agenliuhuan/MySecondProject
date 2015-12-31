package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class HomeZanAdapter extends BaseAdapter {
	ArrayList<AccountObject> accountList;
	Context mContext;

	public HomeZanAdapter(Context context) {
		this.mContext = context;
		accountList = new ArrayList<AccountObject>();
	}

	public int getCount() {
		return accountList.size();
	}

	public AccountObject getItem(int position) {
		return accountList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateList(ArrayList<AccountObject> list) {
		if (accountList != null) {
			accountList.clear();
			accountList.addAll(list);
		}
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_home_zan, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AccountObject accountObj = getItem(position);
		if (accountObj != null) {
			if (!CommonTextUtils.isEmpty(accountObj.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.imgMaster,
						accountObj.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
						mContext.getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				holder.imgMaster.setImageURI(uri);
			}
			if (!CommonTextUtils.isEmpty(accountObj.getNickName())) {
				holder.tvName.setText(accountObj.getNickName());
			} else {
				holder.tvName.setText("");
			}
			if (!CommonTextUtils.isEmpty(accountObj.getGender())) {
				if (accountObj.getGender().equals("0")) {
					holder.imgSex.setImageResource(R.drawable.sex_lady);
				} else {
					holder.imgSex.setImageResource(R.drawable.sex_man);
				}
			} else {
				holder.imgSex.setImageResource(R.drawable.sex_null);
			}

			if (!CommonTextUtils.isEmpty(accountObj.getLastLocateAddress())) {
				holder.tvDistance.setText(accountObj.getLastLocateAddress());
			} else {
				holder.tvDistance.setText("");
			}

			// LngLatObject mylnglat = App.getInstance().getLnglat();
			// if (accountObj.getLocateAddress() != null && mylnglat != null) {
			// LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()),
			// Double.valueOf(mylnglat.getLng()));
			// LatLng cp = new
			// LatLng(Double.valueOf(accountObj.getLocateAddress().getLat()),
			// Double.valueOf(accountObj.getLocateAddress().getLng()));
			// NumberFormat format = NumberFormat.getNumberInstance();
			// format.setMaximumFractionDigits(2);
			// String distance = format.format(DistanceUtil.getDistance(cp, mp)
			// / 1000);
			// holder.tvDistance.setText(distance +
			// mContext.getString(R.string.homepage_distance_text));
			// } else {
			// holder.tvDistance.setText("");
			// }

			int width = CommonUtils.getScreenWidth(mContext);
			LayoutParams params = (LayoutParams) holder.petList.getLayoutParams();
			params.height = width * 722 / 10000;
			params.width = getWidthByCount(accountObj.getPetList().size());
			holder.petList.setLayoutParams(params);

			PetAdapter petadapter = new PetAdapter(mContext);
			holder.petList.setAdapter(petadapter);
			if (accountObj.getPetList() != null && accountObj.getPetList().size() > 0) {
				petadapter.updateList(accountObj.getPetList(), accountObj.getMemberId());
			}
		}
		return convertView;
	}

	private int getWidthByCount(int size) {
		int viewwidth = 0;
		if (size == 0) {
			return viewwidth;
		}
		if (size > 3) {
			size = 3;
		}
		int width = CommonUtils.getScreenWidth(mContext);
		viewwidth = width * 722 / 10000 * size + width * 2 / 100 * (size - 1);
		return viewwidth;
	}

	class ViewHolder {
		SimpleDraweeView imgMaster;
		TextView tvName;
		ImageView imgSex;
		TextView tvDistance;
		HorizontalListView petList;

		public ViewHolder(View view) {
			this.imgMaster = (SimpleDraweeView) view.findViewById(R.id.zan_image);
			this.tvName = (TextView) view.findViewById(R.id.zan_name);
			this.imgSex = (ImageView) view.findViewById(R.id.zan_sex);
			this.tvDistance = (TextView) view.findViewById(R.id.zan_distance);
			this.petList = (HorizontalListView) view.findViewById(R.id.zan_petlist);
		}
	}

}