package mobi.jzcx.android.chongmi.ui.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
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

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public class RecommendAdapter extends BaseAdapter {
	ArrayList<AccountObject> list;
	ArrayList<String> stringlist;
	Context mContext;

	public RecommendAdapter(Context context) {
		this.mContext = context;
		list = new ArrayList<AccountObject>();
		stringlist = new ArrayList<String>();
	}

	public void setSelect(ArrayList<String> select) {
		if (stringlist != null) {
			stringlist.clear();
			stringlist.addAll(select);
		}
		notifyDataSetChanged();
	}

	public void setData(ArrayList<AccountObject> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size();
	}

	public AccountObject getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_recommend, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AccountObject accountObj = getItem(position);
		initView(accountObj, holder);
		return convertView;
	}

	private void initView(AccountObject accountObj, ViewHolder holder) {
		if (accountObj != null) {
			if (!CommonTextUtils.isEmpty(accountObj.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.avatar,
						accountObj.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
						mContext.getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				holder.avatar.setImageURI(uri);
			}

			if (!CommonTextUtils.isEmpty(accountObj.getNickName())) {
				holder.nameTv.setText(accountObj.getNickName());
			} else {
				holder.nameTv.setText("");
			}
			
			if (!CommonTextUtils.isEmpty(accountObj.getLastLocateAddress())) {
				holder.cityTv.setText(accountObj.getLastLocateAddress());
			} else {
				holder.cityTv.setText("");
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
			// holder.cityTv.setText(distance +
			// mContext.getString(R.string.homepage_distance_text));
			// } else {
			// holder.cityTv.setText("");
			// }
			if (!CommonTextUtils.isEmpty(accountObj.getGender())) {
				if (accountObj.getGender().equals("0")) {
					holder.sexImage.setImageResource(R.drawable.sex_lady);
				} else {
					holder.sexImage.setImageResource(R.drawable.sex_man);
				}
			} else {
				holder.sexImage.setImageResource(R.drawable.sex_null);
			}

			if (stringlist.contains(accountObj.getMemberId())) {
				holder.attentionCheck.setImageResource(R.drawable.attention_select);
			} else {
				holder.attentionCheck.setImageResource(R.drawable.attention_noselect);
			}
			int width = CommonUtils.getScreenWidth(mContext);
			LayoutParams params = (LayoutParams) holder.listPet.getLayoutParams();
			params.height = width * 722 / 10000;
			holder.listPet.setLayoutParams(params);

			PetAdapter petadapter = new PetAdapter(mContext);
			holder.listPet.setAdapter(petadapter);
			petadapter.updateList(accountObj.getPetList(), accountObj.getMemberId());

		}

	}

	class ViewHolder {
		SimpleDraweeView avatar;
		TextView nameTv;
		TextView cityTv;
		ImageView attentionCheck;
		HorizontalListView listPet;
		ImageView sexImage;
		PetAdapter petAdapter;

		public ViewHolder(View view) {
			this.avatar = (SimpleDraweeView) view.findViewById(R.id.recommend_avatar);
			this.nameTv = (TextView) view.findViewById(R.id.recommend_name);
			this.cityTv = (TextView) view.findViewById(R.id.recommend_city);
			this.listPet = (HorizontalListView) view.findViewById(R.id.recommend_pet);
			this.sexImage = (ImageView) view.findViewById(R.id.recommend_sex);
			this.attentionCheck = (ImageView) view.findViewById(R.id.recommend_select);
		}
	}

}