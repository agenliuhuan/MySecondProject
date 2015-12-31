package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<String> noticeList;
	int curposition = -1;

	public ReportAdapter(Context context) {
		this.mContext = context;
		noticeList = new ArrayList<String>();
	}

	public void updateList(ArrayList<String> data) {
		if (noticeList != null) {
			noticeList.clear();
			noticeList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public void setSelect(int position) {
		curposition = position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public String getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_reporttext, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String text = getItem(position);
		if (!CommonTextUtils.isEmpty(text)) {
			holder.textview.setText(text);
		}
		if (curposition == position) {
			holder.selectimg.setVisibility(View.VISIBLE);
		} else {
			holder.selectimg.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {

		TextView textview;
		ImageView selectimg;

		public ViewHolder(View view) {
			this.selectimg = (ImageView) view.findViewById(R.id.reportitem_selectimg);
			this.textview = (TextView) view.findViewById(R.id.reportitem_text);

		}
	}

}
