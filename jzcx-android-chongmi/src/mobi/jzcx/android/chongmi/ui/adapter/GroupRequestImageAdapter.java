package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

public class GroupRequestImageAdapter extends BaseAdapter {
	ArrayList<String> list;
	Context mContext;

	public GroupRequestImageAdapter(Context context) {
		this.mContext = context;
		list = new ArrayList<String>();

	}

	public void setData(List<String> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		if (list.size() > 4) {
			return 4;
		} else {
			return list.size();
		}
	}

	public String getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_grouprequest_image, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imageurl = getItem(position);
		initView(imageurl, holder);
		return convertView;
	}

	private void initView(String imageurl, ViewHolder holder) {
		if (!CommonTextUtils.isEmpty(imageurl)) {
			FrescoHelper.displayImageview(holder.image, imageurl + CommonUtils.getAvatarSize(mContext),
					R.drawable.avatar_default_image, null, mContext.getResources(), true, 20);
			// FrescoHelper.displayImageview(holder.image, imageurl +
			// CommonUtils.getAvatarSize(mContext),
			// R.drawable.avatar_default_image, mContext.getResources(), false);
		} else {
			Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
			holder.image.setImageURI(uri);
		}
	}

	class ViewHolder {
		SimpleDraweeView image;

		public ViewHolder(View view) {
			this.image = (SimpleDraweeView) view.findViewById(R.id.grouprequest_aimage);
		}
	}

}