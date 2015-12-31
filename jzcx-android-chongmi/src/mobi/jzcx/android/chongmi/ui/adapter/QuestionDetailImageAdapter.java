package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.homepage.ImageDelListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;

public class QuestionDetailImageAdapter extends BaseAdapter {
	ArrayList<String> mList;
	Context mContext;
	ImageDelListener imageDellistener;

	public void setImageDellistener(ImageDelListener imageDellistener) {
		this.imageDellistener = imageDellistener;
	}

	public QuestionDetailImageAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<String>();
	}

	public ArrayList<String> getmList() {
		return mList;
	}

	public void updateList(List<String> list) {
		if (mList != null) {
			mList.clear();
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return mList.size();
	}

	public String getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_petdiaryimage, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int width = CommonUtils.getScreenWidth(mContext);
		LayoutParams params = (LayoutParams) holder.image.getLayoutParams();
		params.width = width * 21 / 100;
		params.height = width * 21 / 100;
		holder.image.setLayoutParams(params);
		String uriPath = getItem(position);
		if (!CommonTextUtils.isEmpty(uriPath)) {
			FrescoHelper.displayImageview(holder.image, uriPath, 0, null, mContext.getResources(), true, 10.0f);
		}
		holder.delImg.setVisibility(View.GONE);
		return convertView;
	}

	class delclick implements OnClickListener {
		String filepath;

		delclick(String filepath) {
			this.filepath = filepath;
		}

		@Override
		public void onClick(View v) {
			LogUtils.i("PetDiaryPictureAdapter", "delclick");
			if (imageDellistener != null) {
				imageDellistener.imageDelClick(filepath);
			}
		}

	}

	class ViewHolder {
		SimpleDraweeView image;
		ImageView delImg;

		public ViewHolder(View view) {
			this.image = (SimpleDraweeView) view.findViewById(R.id.petdiaryimg_image);
			this.delImg = (ImageView) view.findViewById(R.id.petdiaryimg_del);
		}
	}

}
