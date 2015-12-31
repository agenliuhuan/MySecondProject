package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.GroupRequest;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.serve.RequestClickListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class GroupRequestAdapter extends BaseAdapter {
	ArrayList<GroupRequest> list;
	Context mContext;
	RequestClickListener listener;

	public GroupRequestAdapter(Context context, RequestClickListener l) {
		this.mContext = context;
		list = new ArrayList<GroupRequest>();
		listener = l;
	}

	public void setData(List<GroupRequest> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size();
	}

	public GroupRequest getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_newsim, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GroupRequest accountObj = getItem(position);
		initView(accountObj, holder);
		return convertView;
	}

	private void initView(GroupRequest requesObj, ViewHolder holder) {
		if (requesObj.getAccount() != null) {
			if (!CommonTextUtils.isEmpty(requesObj.getAccount().getIcoUrl())) {
				FrescoHelper.displayImageview(holder.user_avatar,
						requesObj.getAccount().getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			}

			if (!CommonTextUtils.isEmpty(requesObj.getAccount().getNickName())) {
				holder.user_name.setText(requesObj.getAccount().getNickName());
			} else {
				holder.user_name.setText("");
			}
			if (!CommonTextUtils.isEmpty(requesObj.getTitle())) {
				holder.content.setText(requesObj.getTitle());
			} else {
				holder.content.setText("");
			}
			holder.timeTv.setVisibility(View.GONE);
			holder.numTv.setVisibility(View.GONE);
			holder.acceptBtn.setOnClickListener(new acceptClick(requesObj));
		}
	}

	class acceptClick implements OnClickListener {
		GroupRequest obj;

		acceptClick(GroupRequest obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (listener != null) {
				// listener.acceptClick(obj);
			}

		}
	}

	class ViewHolder {
		SimpleDraweeView user_avatar;
		TextView user_name;
		TextView content;
		TextView timeTv;
		TextView numTv;
		ImageView acceptBtn;

		public ViewHolder(View view) {
			this.user_avatar = (SimpleDraweeView) view.findViewById(R.id.newim_Item_userimg);
			this.user_name = (TextView) view.findViewById(R.id.newim_Item_username);
			this.content = (TextView) view.findViewById(R.id.newim_Item_content);
			this.timeTv = (TextView) view.findViewById(R.id.newim_Item_time);
			this.numTv = (TextView) view.findViewById(R.id.newim_Item_numtext);
			this.acceptBtn = (ImageView) view.findViewById(R.id.newim_Item_acceptBtn);
		}
	}

}