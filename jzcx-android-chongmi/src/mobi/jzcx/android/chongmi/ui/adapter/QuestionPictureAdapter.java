package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.homepage.ImageDelListener;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;

public class QuestionPictureAdapter extends BaseAdapter {
	ArrayList<String> mList;
	Context mContext;
	ImageDelListener imageDellistener;

	public void setImageDellistener(ImageDelListener imageDellistener) {
		this.imageDellistener = imageDellistener;
	}

	public QuestionPictureAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<String>();
	}

	public void updateList(ArrayList<String> list) {
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
		params.width = width * 20 / 100;
		params.height = width * 20 / 100;
		holder.image.setLayoutParams(params);
		String uriPath = getItem(position);
		if (!CommonTextUtils.isEmpty(uriPath)) {
			if (uriPath.equals("addImage")) {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.petdiary_addimg);
				holder.image.setImageURI(uri);
				holder.delImg.setVisibility(View.GONE);
			} else {
				holder.delImg.setVisibility(View.VISIBLE);
				FrescoHelper.displayImageview(holder.image, "file://" + uriPath, 0, null, mContext.getResources(),
						true, 10.0f);
			}
			holder.delImg.setOnClickListener(new delclick(uriPath));
		}

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
