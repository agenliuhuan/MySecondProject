package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class PetDiaryPetAdapter extends BaseAdapter {
	ArrayList<PetObject> mList;
	Context mContext;
	int curpostion = -1;

	public PetDiaryPetAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<PetObject>();
	}

	public void updateList(ArrayList<PetObject> list) {
		if (mList != null) {
			mList.clear();
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void setSelection(int position) {
		curpostion = position;
		notifyDataSetChanged();
	}

	public int getCount() {
		return mList.size();
	}

	public PetObject getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_petdiarypet, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PetObject pet = getItem(position);
		if (pet != null) {
			if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.image, pet.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			}
		}
		if (position == curpostion) {
			holder.addImg.setVisibility(View.VISIBLE);
		} else {
			holder.addImg.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView image;
		ImageView addImg;

		public ViewHolder(View view) {
			this.image = (SimpleDraweeView) view.findViewById(R.id.petdiarypet_image);
			this.addImg = (ImageView) view.findViewById(R.id.petdiarypet_add);
		}
	}

}
