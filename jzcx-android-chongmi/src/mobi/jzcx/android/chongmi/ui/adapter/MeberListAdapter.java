package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;


import com.facebook.drawee.view.SimpleDraweeView;

public class MeberListAdapter extends BaseAdapter {
	ArrayList<AccountObject> mAccountList;
	Context mContext;

	public MeberListAdapter(Context context) {
		this.mContext = context;
		mAccountList = new ArrayList<AccountObject>();
	}

	public int getCount() {
		return mAccountList.size();
	}

	public AccountObject getItem(int position) {
		return mAccountList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateList(ArrayList<AccountObject> data) {
		if (mAccountList != null) {
			mAccountList.clear();
			mAccountList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_meber_image, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int width = CommonUtils.getScreenWidth(mContext);
		LayoutParams params = (LayoutParams) holder.zanuserImage.getLayoutParams();
		params.width = width * 1111 / 10000;
		params.height = width * 1111 / 10000;
		params.setMargins(0, 0, width * 2 / 100, 0);
		holder.zanuserImage.setLayoutParams(params);
		AccountObject accountObj = getItem(position);
		if (accountObj != null) {
			if (!CommonTextUtils.isEmpty(accountObj.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.zanuserImage, accountObj.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				holder.zanuserImage.setImageURI(uri);
			}
//			convertView.setOnClickListener(new zanUserClick(accountObj.get_id()));
		} else {
			convertView.setVisibility(View.GONE);
		}
		return convertView;
	}

	class zanUserClick implements OnClickListener {
		String userid;

		zanUserClick(String userid) {
			this.userid = userid;
		}

		@Override
		public void onClick(View v) {
			AccountCenterActivity.startActivity(mContext, userid);
		}

	}

	class ViewHolder {
		SimpleDraweeView zanuserImage;

		public ViewHolder(View view) {
			this.zanuserImage = (SimpleDraweeView) view.findViewById(R.id.meberListItem_image);
		}
	}
}
