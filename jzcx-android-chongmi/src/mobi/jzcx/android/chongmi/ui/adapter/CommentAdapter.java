package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.NoticeObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportClickListener;
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

public class CommentAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<NoticeObject> noticeList;
	ReportClickListener reportClick;

	public CommentAdapter(Context context) {
		this.mContext = context;
		noticeList = new ArrayList<NoticeObject>();
	}

	public void updateList(List<NoticeObject> data) {
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
	public NoticeObject getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setReportClick(ReportClickListener reportClick) {
		this.reportClick = reportClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_comment, parent, false);
			holder.contentTv = (TextView) convertView.findViewById(R.id.comment_Item_content);
			holder.voiceImg = (ImageView) convertView.findViewById(R.id.comment_Item_zan);
			holder.userimg = (SimpleDraweeView) convertView.findViewById(R.id.comment_Item_userimg);
			holder.detailimg = (SimpleDraweeView) convertView.findViewById(R.id.comment_Item_detailimg);
			holder.timeTv = (TextView) convertView.findViewById(R.id.comment_Item_time);
			holder.voicenameTv = (TextView) convertView.findViewById(R.id.comment_Item_username);
			holder.voiceText = (TextView) convertView.findViewById(R.id.comment_Item_distancetext);
			holder.replyNameTV = (TextView) convertView.findViewById(R.id.comment_Item_tip);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		NoticeObject noticeObj = getItem(position);
		if (noticeObj != null) {
			if (noticeObj.getNoticeMsgType() == 0) {
				holder.contentTv.setText(mContext.getString(R.string.comment_isvoice));
				holder.voiceImg.setVisibility(View.GONE);
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.replyNameTV.setVisibility(View.VISIBLE);
			} else if (noticeObj.getNoticeMsgType() == 1) {
				if (!CommonTextUtils.isEmpty(noticeObj.getContext())) {
					holder.contentTv.setText(noticeObj.getContext());
				} else {
					holder.contentTv.setText("");
				}
				holder.voiceImg.setVisibility(View.GONE);
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.replyNameTV.setVisibility(View.VISIBLE);
			} else if (noticeObj.getNoticeMsgType() == 4) {
				holder.voiceImg.setVisibility(View.VISIBLE);
				holder.contentTv.setVisibility(View.GONE);
				holder.replyNameTV.setVisibility(View.GONE);
			} else {
				holder.contentTv.setText("");
				holder.voiceImg.setVisibility(View.GONE);
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.replyNameTV.setVisibility(View.VISIBLE);
			}

			if (!CommonTextUtils.isEmpty(noticeObj.getCreateTime())) {
				holder.timeTv.setText(CommonUtils.getTimeSamp(mContext, noticeObj.getCreateTime()));
			} else {
				holder.timeTv.setText("");
			}
			AccountObject account = noticeObj.getAccount();
			if (account != null) {
				if (!CommonTextUtils.isEmpty(account.getIcoUrl())) {
					FrescoHelper
							.displayImageview(holder.userimg,
									account.getIcoUrl() + CommonUtils.getAvatarSize(mContext), 0,
									mContext.getResources(), true);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
					holder.userimg.setImageURI(uri);
				}
				if (!CommonTextUtils.isEmpty(account.getNickName())) {
					holder.voicenameTv.setText(account.getNickName());
				} else {
					holder.voicenameTv.setText("");
				}
				if (!CommonTextUtils.isEmpty(noticeObj.getAccount().getLastLocateAddress())) {
					holder.voiceText.setText(noticeObj.getAccount().getLastLocateAddress());
				} else {
					holder.voiceText.setText("");
				}
			}

			// LngLatObject mylnglat = App.getInstance().getLnglat();
			// if (!CommonTextUtils.isEmpty(noticeObj.getLng()) &&
			// !CommonTextUtils.isEmpty(noticeObj.getLat())
			// && mylnglat != null) {
			// LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()),
			// Double.valueOf(mylnglat.getLng()));
			// LatLng cp = new LatLng(Double.valueOf(noticeObj.getLat()),
			// Double.valueOf(noticeObj.getLng()));
			// NumberFormat format = NumberFormat.getNumberInstance();
			// format.setMaximumFractionDigits(2);
			// String distance = format.format(DistanceUtil.getDistance(cp, mp)
			// / 1000);
			// holder.voiceText.setText(distance +
			// mContext.getString(R.string.homepage_distance_text));
			// } else {
			//
			// }
			// holder.voiceText.setText("");
			if (!CommonTextUtils.isEmpty(noticeObj.getDetailImg())) {
				FrescoHelper.displayImageview(holder.detailimg,
						noticeObj.getDetailImg() + CommonUtils.getAvatarSize(mContext), R.drawable.image_default_image,
						null, mContext.getResources(), true, 20);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.image_default_image);
				holder.detailimg.setImageURI(uri);
			}
			// convertView.setOnClickListener(new mainClick(noticeObj));
		}

		return convertView;
	}

	class viewHolder {
		TextView contentTv;
		ImageView voiceImg;
		SimpleDraweeView userimg;
		SimpleDraweeView detailimg;
		TextView timeTv;
		TextView voicenameTv;
		TextView voiceText;
		TextView replyNameTV;
	}

	class deleteClick implements OnClickListener {
		NoticeObject noticeObj;

		deleteClick(NoticeObject noticeObj) {
			this.noticeObj = noticeObj;
		}

		@Override
		public void onClick(View v) {

			if (reportClick != null) {
				reportClick.commentDelClick(noticeObj);
			}
		}
	}

	class mainClick implements OnClickListener {
		NoticeObject noticeObj;

		mainClick(NoticeObject noticeObj) {
			this.noticeObj = noticeObj;
		}

		@Override
		public void onClick(View v) {
			if (reportClick != null) {
				reportClick.commentMainClick(noticeObj);
			}
		}
	}

}
