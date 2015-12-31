package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetCenterDynamicClick;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetDiaryDetailsActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PetCenterAdapter extends BaseAdapter {

	private Context mContext;
	private List<DynamicObject> list;
	private LayoutInflater inflater;

	public PetCenterAdapter(Context context) {
		this.mContext = context;
		list = new ArrayList<DynamicObject>();
		inflater = LayoutInflater.from(mContext);
	}

	PetCenterDynamicClick dynamicClick = null;

	public void setDynamicClick(PetCenterDynamicClick dynamicClick) {
		this.dynamicClick = dynamicClick;
	}

	public void updateData(ArrayList<DynamicObject> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	public void setCancelLike(String id) {
		if (!CommonTextUtils.isEmpty(id)) {
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getMicroblogId().equals(id)) {
						list.get(i).setIsLiked(false);
						int likeCount = Integer.valueOf(list.get(i).getLikeCount());
						likeCount--;
						list.get(i).setLikeCount(String.valueOf(likeCount));
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	public void setLike(String id) {
		if (!CommonTextUtils.isEmpty(id)) {
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getMicroblogId().equals(id)) {
						list.get(i).setIsLiked(true);
						int likeCount = Integer.valueOf(list.get(i).getLikeCount());
						likeCount++;
						list.get(i).setLikeCount(String.valueOf(likeCount));
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public DynamicObject getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		// if (convertView == null) {
		//
		// convertView = inflater.inflate(R.layout.listitem_pet_center_img1,
		// null);
		// viewHolder = new ViewHolder();
		//
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }
		DynamicObject dynamicObj = getItem(position);
		if (dynamicObj != null) {
			if (dynamicObj.getPetPhotos() != null && dynamicObj.getPetPhotos().size() != 0) {
				if (dynamicObj.getPetPhotos().size() == 1) {
					convertView = inflater.inflate(R.layout.listitem_pet_center_img1, null);
					viewHolder.contentImg1 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg1);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(0) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg1, R.drawable.image_default_image, false);
				} else if (dynamicObj.getPetPhotos().size() == 2) {
					convertView = inflater.inflate(R.layout.listitem_pet_center_img2, null);
					viewHolder.contentImg1 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg1);
					viewHolder.contentImg2 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg2);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(0) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg1, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(1) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg2, R.drawable.image_default_image, false);
				} else if (dynamicObj.getPetPhotos().size() == 3) {
					convertView = inflater.inflate(R.layout.listitem_pet_center_img3, null);
					viewHolder.contentImg1 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg1);
					viewHolder.contentImg2 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg2);
					viewHolder.contentImg3 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg3);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(0) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg1, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(1) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg2, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(2) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg3, R.drawable.image_default_image, false);
				} else if (dynamicObj.getPetPhotos().size() >= 4) {
					convertView = inflater.inflate(R.layout.listitem_pet_center_img4, null);
					viewHolder.contentImg1 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg1);
					viewHolder.contentImg2 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg2);
					viewHolder.contentImg3 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg3);
					viewHolder.contentImg4 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg4);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(0) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg1, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(1) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg2, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(2) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg3, R.drawable.image_default_image, false);
					ImageLoaderHelper.displayImage(
							dynamicObj.getPetPhotos().get(3) + CommonUtils.getImageSize(mContext),
							viewHolder.contentImg4, R.drawable.image_default_image, false);
				} else {
					convertView = inflater.inflate(R.layout.listitem_pet_center_img1, null);
					viewHolder.contentImg1 = (ImageView) convertView.findViewById(R.id.petcenteritem_contentimg1);
					viewHolder.contentImg1.setVisibility(View.GONE);
				}
				convertView.setTag(viewHolder);
				viewHolder.dayTV = (TextView) convertView.findViewById(R.id.petcenteritem_dayTV);
				viewHolder.monthTV = (TextView) convertView.findViewById(R.id.petcenteritem_monthTV);
				viewHolder.contentText = (TextView) convertView.findViewById(R.id.petcenteritem_contenttext);
				viewHolder.zanLL = (LinearLayout) convertView.findViewById(R.id.petcenteritem_zanLL);
				viewHolder.zanImage = (ImageView) convertView.findViewById(R.id.petcenteritem_zanimg);
				viewHolder.zanText = (TextView) convertView.findViewById(R.id.petcenteritem_zantext);
				viewHolder.commentLL = (LinearLayout) convertView.findViewById(R.id.petcenteritem_commentRL);
				viewHolder.commentText = (TextView) convertView.findViewById(R.id.petcenteritem_commentNum);
			}

			if (!CommonTextUtils.isEmpty(dynamicObj.getText())) {

				viewHolder.contentText.setText(dynamicObj.getText().trim());
				viewHolder.contentText.setVisibility(View.VISIBLE);
			} else {
				viewHolder.contentText.setVisibility(View.GONE);
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getCreateTime())) {
				Date date = CommonUtils.getCreateDate(dynamicObj.getCreateTime());
				Calendar ca = Calendar.getInstance();
				ca.setTime(date);
				int day = ca.get(Calendar.DAY_OF_MONTH);
				int month = ca.get(Calendar.MONTH);
				viewHolder.dayTV.setText(String.valueOf(day));
				String[] months = mContext.getResources().getStringArray(R.array.months);
				viewHolder.monthTV.setText(String.valueOf(months[month]));
			} else {
				viewHolder.dayTV.setText("");
				viewHolder.monthTV.setText("");
			}

			if (dynamicObj.isIsLiked()) {
				viewHolder.zanImage.setImageResource(R.drawable.icon_iszan);
			} else {
				viewHolder.zanImage.setImageResource(R.drawable.icon_notzan);
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getLikeCount()) && !dynamicObj.getLikeCount().equals("0")) {
				viewHolder.zanText.setText(dynamicObj.getLikeCount());
			} else {
				viewHolder.zanText.setText(mContext.getString(R.string.dynamic_zan));
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getCommentCount()) && !dynamicObj.getCommentCount().equals("0")) {
				viewHolder.commentText.setText(dynamicObj.getCommentCount());
			} else {
				viewHolder.commentText.setText(mContext.getString(R.string.dynamic_comment));
			}
			convertView.setOnClickListener(new mainClick(dynamicObj));
			viewHolder.commentLL.setOnClickListener(new commentLLClick(dynamicObj));
			viewHolder.zanLL.setOnClickListener(new dianZanOrNotClick(dynamicObj));
		}
		return convertView;
	}

	class mainTouch implements OnTouchListener {
		DynamicObject dynamicObj;

		mainTouch(DynamicObject dynamicObj) {
			this.dynamicObj = dynamicObj;
		}
		boolean isMove = false;
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				isMove = true;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (!isMove) {
					PetDiaryDetailsActivity.startActivity(mContext, dynamicObj, false, null);
				}
			}

			return false;
		}
	}

	class mainClick implements OnClickListener {
		DynamicObject dynamicObj;

		mainClick(DynamicObject dynamicObj) {
			this.dynamicObj = dynamicObj;
		}

		@Override
		public void onClick(View v) {
			if (dynamicClick != null) {
				dynamicClick.mianClick(dynamicObj);
			}
		}

	}

	class dianZanOrNotClick implements OnClickListener {
		DynamicObject obj;

		dianZanOrNotClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (dynamicClick != null) {
				dynamicClick.zanOrNotClick(obj);
			}
		}
	}

	class commentLLClick implements OnClickListener {
		DynamicObject obj;

		commentLLClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (dynamicClick != null) {
				dynamicClick.commentClick(obj);
			}
		}
	}

	private void setGridHeight(GridView grid, int count) {
		LayoutParams params = (LayoutParams) grid.getLayoutParams();
		int width = CommonUtils.getScreenWidth(mContext);
		if (count < 4) {
			params.height = (width) / 4 + 20;
		} else if (count < 7) {
			params.height = (width) / 2 + 40;
		}
		grid.setLayoutParams(params);
		grid.setVerticalSpacing(10);
	}

	static class ViewHolder {
		public TextView dayTV;
		public TextView monthTV;
		public TextView contentText;
		public ImageView contentImg1;
		public ImageView contentImg2;
		public ImageView contentImg3;
		public ImageView contentImg4;
		public LinearLayout zanLL;
		public ImageView zanImage;
		public TextView zanText;
		public LinearLayout commentLL;
		public TextView commentText;
	}
}