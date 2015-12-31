package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportClickListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class SystemNewsAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<SystemNoticeObject> noticeList;
	ReportClickListener reportClick;

	public SystemNewsAdapter(Context context) {
		this.mContext = context;
		noticeList = new ArrayList<SystemNoticeObject>();
	}

	public void updateList(List<SystemNoticeObject> data) {
		if (noticeList != null) {
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
	public SystemNoticeObject getItem(int position) {
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
		viewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_systemnews, parent, false);
			holder.contentTv = (TextView) convertView.findViewById(R.id.systemnews_Item_content);
			holder.userimg = (SimpleDraweeView) convertView.findViewById(R.id.systemnews_Item_userimg);
			holder.timeTv = (TextView) convertView.findViewById(R.id.systemnews_Item_time);
			holder.voicenameTv = (TextView) convertView.findViewById(R.id.systemnews_Item_name);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		SystemNoticeObject noticeObj = getItem(position);
		if (noticeObj != null) {
			if (!CommonTextUtils.isEmpty(noticeObj.getTitle())) {
				holder.voicenameTv.setText(noticeObj.getTitle());
			} else {
				holder.voicenameTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(noticeObj.getCreateTime())) {
				holder.timeTv.setText(CommonUtils.getTimeSamp(mContext, noticeObj.getCreateTime()));
			} else {
				holder.timeTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(noticeObj.getContext())) {
				holder.contentTv.setText(noticeObj.getContext());
			} else {
				holder.contentTv.setText("");
			}
		}
		return convertView;
	}

	class viewHolder {
		TextView contentTv;
		SimpleDraweeView userimg;
		TextView timeTv;
		TextView voicenameTv;
	}

	class deleteClick implements OnClickListener {
		SystemNoticeObject noticeObj;

		deleteClick(SystemNoticeObject noticeObj) {
			this.noticeObj = noticeObj;
		}

		@Override
		public void onClick(View v) {
			if (reportClick != null) {
				reportClick.systemNewsDelClick(noticeObj);
			}
		}
	}

	class mainClick implements OnClickListener {
		SystemNoticeObject noticeObj;

		mainClick(SystemNoticeObject noticeObj) {
			this.noticeObj = noticeObj;
		}

		@Override
		public void onClick(View v) {
			if (reportClick != null) {
				reportClick.systemNewsMainClick(noticeObj);
			}
		}
	}
}
