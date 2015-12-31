package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;

public class PetAdapter extends BaseAdapter {
	ArrayList<PetObject> mPetList;
	Context mContext;
	String meberid;

	public PetAdapter(Context context) {
		this.mContext = context;
		mPetList = new ArrayList<PetObject>();
	}

	public int getCount() {
		return mPetList.size();
	}

	public PetObject getItem(int position) {
		return mPetList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateList(ArrayList<PetObject> list, String meberid) {
		if (mPetList != null && !CommonTextUtils.isEmpty(meberid)) {
			this.meberid = meberid;
			mPetList.clear();
			mPetList.addAll(list);
		}
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_recommend_attention_pet, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int width = CommonUtils.getScreenWidth(mContext);
		LayoutParams params = (LayoutParams) holder.imgPet.getLayoutParams();
		params.width = width * 722 / 10000;
		params.height = width * 722 / 10000;
		params.setMargins(0, 0, width * 2 / 100, 0);
		holder.imgPet.setLayoutParams(params);
		PetObject pet = getItem(position);
		if (pet != null) {
			FrescoHelper.displayImageview(holder.imgPet, pet.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
					R.drawable.avatar_default_image, mContext.getResources(), true);
			holder.imgPet.setOnClickListener(new petClick(pet));
		} else {
			Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
			holder.imgPet.setImageURI(uri);
		}

		return convertView;
	}

	class petClick implements OnClickListener {
		PetObject pet;

		petClick(PetObject pet) {
			this.pet = pet;
		}

		@Override
		public void onClick(View v) {
			GetPetObject getPet = new GetPetObject();
			getPet.setMeberId(meberid);
			getPet.setPetId(pet.getPetId());
			PetCenterActivity.startActivity(mContext, getPet);
		}

	}

	class ViewHolder {
		SimpleDraweeView imgPet;

		public ViewHolder(View view) {
			this.imgPet = (SimpleDraweeView) view.findViewById(R.id.img_pet);
		}
	}

}