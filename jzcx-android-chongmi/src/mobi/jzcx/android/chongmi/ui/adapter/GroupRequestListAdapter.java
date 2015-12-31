package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.GroupRequest;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.serve.RequestClickListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class GroupRequestListAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<GroupRequest> noticeList;
	RequestClickListener requestClick;

	public GroupRequestListAdapter(Context context) {
		this.mContext = context;
		noticeList = new ArrayList<GroupRequest>();
	}

	public void updateList(List<GroupRequest> data) {
		if (noticeList != null && data != null) {
			noticeList.clear();
			noticeList.addAll(data);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public GroupRequest getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setRquestClick(RequestClickListener requestClick) {
		this.requestClick = requestClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_grouprequest, parent, false);
			holder.contentTv = (TextView) convertView.findViewById(R.id.groupRequest_content);
			holder.userimg = (SimpleDraweeView) convertView.findViewById(R.id.groupRequest_userimg);
			holder.voiceImg = (ImageView) convertView.findViewById(R.id.groupRequest_acceptBtn);
			holder.voiceText = (TextView) convertView.findViewById(R.id.groupRequest_acceptedTV);
			holder.timeTv = (TextView) convertView.findViewById(R.id.groupRequest_time);
			holder.voicenameTv = (TextView) convertView.findViewById(R.id.groupRequest_username);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		GroupRequest groupRequest = getItem(position);
		if (groupRequest != null) {
			if (!CommonTextUtils.isEmpty(groupRequest.getTitle())) {
				holder.contentTv.setText(groupRequest.getTitle());
			} else {
				holder.contentTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(groupRequest.getCreateTime())) {
				holder.timeTv.setText(CommonUtils.getTimeSamp(mContext, groupRequest.getCreateTime()));
			} else {
				holder.timeTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(groupRequest.getAccount().getIcoUrl())) {
				FrescoHelper.displayImageview(holder.userimg,
						groupRequest.getAccount().getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				holder.userimg.setImageURI(uri);
			}
			if (!CommonTextUtils.isEmpty(groupRequest.getAccount().getNickName())) {
				holder.voicenameTv.setText(groupRequest.getAccount().getNickName());
			} else {
				holder.voicenameTv.setText("");
			}

			if (!CommonTextUtils.isEmpty(groupRequest.getEndTime())) {
				Date endDate = CommonUtils.getDate(groupRequest.getEndTime());
				Date nowDate = new Date();
				if (endDate.before(nowDate)) {
					holder.voiceText.setText(mContext.getString(R.string.grouprequestlist_isend));
					holder.voiceImg.setVisibility(View.GONE);
					holder.voiceText.setVisibility(View.VISIBLE);
				} else {
					if (groupRequest.isIsJoin()) {
						holder.voiceText.setText(mContext.getString(R.string.grouprequestlist_accepted));
						holder.voiceImg.setVisibility(View.GONE);
						holder.voiceText.setVisibility(View.VISIBLE);
					} else {
						holder.voiceImg.setVisibility(View.VISIBLE);
						holder.voiceText.setVisibility(View.GONE);
					}
				}
			}

			holder.voiceImg.setOnClickListener(new acceptClick(groupRequest));
			// holder.deleteImg.setBackgroundResource(R.drawable.delete_menubg);
			// holder.deleteImg.setOnClickListener(new
			// deleteClick(groupRequest));
			// holder.viewContainer.setOnClickListener(new
			// mainClick(groupRequest));
		}
		// int itemWidth = CommonUtils.getScreenWidth(mContext);
		// ViewGroup.LayoutParams lpContent =
		// holder.viewContainer.getChildAt(0).getLayoutParams();
		// lpContent.width = itemWidth;
		//
		// ViewGroup.LayoutParams lpAction =
		// holder.viewContainer.getChildAt(1).getLayoutParams();
		// lpAction.width = itemWidth / 5;
		//
		// convertView.setOnTouchListener(new SwipeOnTouchListener());
		return convertView;
	}

	class viewHolder {
		TextView contentTv;
		SimpleDraweeView userimg;
		ImageView voiceImg;
		TextView voiceText;
		TextView timeTv;
		TextView voicenameTv;
	}

	class deleteClick implements OnClickListener {
		GroupRequest groupRequest;

		deleteClick(GroupRequest groupRequest) {
			this.groupRequest = groupRequest;
		}

		@Override
		public void onClick(View v) {
			if (requestClick != null) {
				requestClick.deleteClick(groupRequest);
			}
		}
	}

	class mainClick implements OnClickListener {
		GroupRequest groupRequest;

		mainClick(GroupRequest groupRequest) {
			this.groupRequest = groupRequest;
		}

		@Override
		public void onClick(View v) {
			if (requestClick != null) {
				requestClick.mainClick(groupRequest);
			}
		}
	}

	class acceptClick implements OnClickListener {
		GroupRequest groupRequest;

		acceptClick(GroupRequest groupRequest) {
			this.groupRequest = groupRequest;
		}

		@Override
		public void onClick(View v) {
			if (requestClick != null) {
				requestClick.acceptClick(groupRequest);
			}
		}
	}

}
