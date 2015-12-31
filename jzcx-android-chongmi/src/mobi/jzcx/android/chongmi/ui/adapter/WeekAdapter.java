package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class WeekAdapter extends BaseAdapter {
	ArrayList<Date> mDateList;
	Context mContext;

	public WeekAdapter(Context context) {
		this.mContext = context;
		mDateList = new ArrayList<Date>();
	}

	public int getCount() {
		return mDateList.size();
	}

	public Date getItem(int position) {
		return mDateList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateList(ArrayList<Date> data) {
		if (mDateList != null) {
			mDateList.clear();
			mDateList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_week, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Date date = getItem(position);
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			holder.textview.setText(String.valueOf(day));
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview;

		public ViewHolder(View view) {
			this.textview = (TextView) view.findViewById(R.id.week_textview);
		}
	}

}