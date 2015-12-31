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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AccountCenterPetAdapter extends BaseAdapter {
	ArrayList<PetObject> petList;
	Context mContext;

	public AccountCenterPetAdapter(Context context) {
		this.mContext = context;
		petList = new ArrayList<PetObject>();
	}

	public void setData(ArrayList<PetObject> data) {
		if (petList != null) {
			petList.clear();
			petList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		if (petList.size() > 3) {
			return 3;
		} else {
			return petList.size();
		}

	}

	public PetObject getItem(int position) {
		return petList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_centerpet, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PetObject pet = getItem(position);
		if (pet != null) {
			if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.petImg, pet.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(pet.getGender())) {
				if (pet.getGender().equals("0")) {
					holder.petSex.setBackgroundResource(R.drawable.pet_bitch);
				} else {
					holder.petSex.setBackgroundResource(R.drawable.pet_dog);
				}
			}
			if (!CommonTextUtils.isEmpty(pet.getName())) {
				holder.petName.setText(pet.getName());
			}

			if (!CommonTextUtils.isEmpty(pet.getCategoryName())) {
				holder.petCategory.setText(pet.getCategoryName());
			}
			if (!CommonTextUtils.isEmpty(pet.getBirthday())) {
				holder.petBirthday.setText(CommonUtils.getPetAge(mContext, pet.getBirthday()));
			}
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView petImg;
		ImageView petSex;
		TextView petName;
		TextView petCategory;
		TextView petBirthday;

		public ViewHolder(View view) {
			this.petImg = (SimpleDraweeView) view.findViewById(R.id.centerpet_image);
			this.petSex = (ImageView) view.findViewById(R.id.centerpet_gender);
			this.petName = (TextView) view.findViewById(R.id.centerpet_name);
			this.petCategory = (TextView) view.findViewById(R.id.centerpet_category);
			this.petBirthday = (TextView) view.findViewById(R.id.centerpet_birthday);
		}
	}

}