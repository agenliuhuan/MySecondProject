package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.PetStyleObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class PetStyleAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<PetStyleObject> objList;

	public PetStyleAdapter(Context context) {
		this.mContext = context;
		objList = new ArrayList<PetStyleObject>();
	}

	public void setData(ArrayList<PetStyleObject> data) {
		if (objList != null) {
			objList.clear();
			objList.addAll(data);
		}
	}

	@Override
	public int getCount() {
		return objList.size();
	}

	@Override
	public PetStyleObject getItem(int position) {
		return objList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_petstyle, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PetStyleObject obj = getItem(position);

		initView(holder, obj);

		return convertView;
	}

	private void initView(ViewHolder holder, PetStyleObject obj) {
		if (!CommonTextUtils.isEmpty(obj.getIcoUrl())) {
			FrescoHelper.displayImageview(holder.imgPet, obj.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
					R.drawable.avatar_default_image, mContext.getResources(), true);
		}
		if (obj.getName() != null && !TextUtils.isEmpty(obj.getName())) {
			holder.namePet.setText(obj.getName());
		}
	}

	class ViewHolder {
		SimpleDraweeView imgPet;
		TextView namePet;

		public ViewHolder(View view) {
			this.imgPet = (SimpleDraweeView) view.findViewById(R.id.petstyle_image);
			this.namePet = (TextView) view.findViewById(R.id.petstyle_name);
		}
	}

}
