package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.shopping.UnbindListener;
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

public class HardwarePetListAdapter extends BaseAdapter {
	ArrayList<PetBindObject> petList;
	Context mContext;

	public HardwarePetListAdapter(Context context) {
		this.mContext = context;
		petList = new ArrayList<PetBindObject>();
	}

	public void setData(ArrayList<PetBindObject> data) {
		if (petList != null) {
			petList.clear();
			petList.addAll(data);
		}
		notifyDataSetChanged();
	}

	UnbindListener ubbindlistener;

	public void setUbbindlistener(UnbindListener ubbindlistener) {
		this.ubbindlistener = ubbindlistener;
	}

	public int getCount() {
		return petList.size();
	}

	public PetBindObject getItem(int position) {
		return petList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_hardware_pet, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PetBindObject pet = getItem(position);
		if (pet != null) {
			if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.petImg, pet.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
						R.drawable.avatar_default_image, mContext.getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(pet.getName())) {
				holder.petName.setText(pet.getName());
			}
			if (pet.isIsBind()) {
				holder.unbindTv.setText(mContext.getString(R.string.hardware_petlist_unbind));
			} else {
				holder.unbindTv.setText(mContext.getString(R.string.hardware_petlist_bind));
			}

			holder.unbindTv.setOnClickListener(new unbindClick(pet));
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView petImg;
		TextView unbindTv;
		TextView petName;

		public ViewHolder(View view) {
			this.petImg = (SimpleDraweeView) view.findViewById(R.id.hardwarepet_image);
			this.petName = (TextView) view.findViewById(R.id.hardwarepet_name);
			this.unbindTv = (TextView) view.findViewById(R.id.hardwarepet_unbindtv);
		}
	}

	class unbindClick implements OnClickListener {
		PetBindObject pet;

		unbindClick(PetBindObject pet) {
			this.pet = pet;
		}

		@Override
		public void onClick(View v) {
			if (ubbindlistener != null) {
				ubbindlistener.unbindClick(pet);
			}
		}
	}

}
