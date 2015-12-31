package mobi.jzcx.android.chongmi.ui.group;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;

public class LocationAdapter extends BaseAdapter {
	private Context context;
	private List<Object> mInfos;// 过滤后的item

	public LocationAdapter(Context context) {
		this.context = context;
		this.mInfos = new ArrayList<Object>();
	}

	public void setData(List<Object> data) {
		if (mInfos != null) {
			mInfos.clear();
			mInfos.addAll(data);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_location, null);
			holder.nametv = (TextView) convertView.findViewById(R.id.location_name);
			holder.desctv = (TextView) convertView.findViewById(R.id.location_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Object obj = mInfos.get(position);
		if (obj instanceof PoiInfo) {
			PoiInfo info = (PoiInfo) obj;
			if (!TextUtils.isEmpty(info.name)) {
				holder.nametv.setText(info.name);
			}
			if (!TextUtils.isEmpty(info.address)) {
				holder.desctv.setText(info.address);
			}
		}
		if (obj instanceof PoiAddrInfo) {
			PoiAddrInfo addrinfo = (PoiAddrInfo) obj;
			if (!TextUtils.isEmpty(addrinfo.name)) {
				holder.nametv.setText(addrinfo.name);
			}
			if (!TextUtils.isEmpty(addrinfo.address)) {
				holder.desctv.setText(addrinfo.address);
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView nametv;
		TextView desctv;
	}

}
