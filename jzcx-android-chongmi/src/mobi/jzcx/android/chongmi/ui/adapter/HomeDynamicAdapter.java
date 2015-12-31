package mobi.jzcx.android.chongmi.ui.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.DecoratorViewPager;
import mobi.jzcx.android.chongmi.ui.main.homepage.DynamicClickListener;
import mobi.jzcx.android.chongmi.ui.main.homepage.TVPageIndicator;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.view.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public class HomeDynamicAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	private Context mContext;

	ArrayList<DynamicObject> mList;

	public HomeDynamicAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<DynamicObject>();
	}

	public void setData(ArrayList<DynamicObject> data) {
		if (mList != null) {
			mList.clear();
			mList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public void removeDynamic(String dynamicId) {
		if (!CommonTextUtils.isEmpty(dynamicId)) {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getMicroblogId().equals(dynamicId)) {
						mList.remove(i);
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	public void setLike(String id) {
		if (!CommonTextUtils.isEmpty(id)) {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getMicroblogId().equals(id)) {
						mList.get(i).setIsLiked(true);
						int likeCount = Integer.valueOf(mList.get(i).getLikeCount());
						likeCount++;
						mList.get(i).setLikeCount(String.valueOf(likeCount));
						AccountObject myself = getMyself();
						if (myself != null) {
							if (i != -1) {
								mList.get(i).getlMembers().add(0, myself);
							}
						}
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	public void setCancelFollowed(String memberId) {
		if (!CommonTextUtils.isEmpty(memberId)) {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getAccountObj().getMemberId().equals(memberId)) {
						mList.get(i).getAccountObj().setIsFollowed(false);
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	public void setFollowed(String memberId) {
		if (!CommonTextUtils.isEmpty(memberId)) {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getAccountObj().getMemberId().equals(memberId)) {
						mList.get(i).getAccountObj().setIsFollowed(true);
					}
				}
			}
			notifyDataSetChanged();
		}

	}

	public void setCancelLike(String id) {
		if (!CommonTextUtils.isEmpty(id)) {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getMicroblogId().equals(id)) {
						mList.get(i).setIsLiked(false);
						int likeCount = Integer.valueOf(mList.get(i).getLikeCount());
						likeCount--;
						mList.get(i).setLikeCount(String.valueOf(likeCount));
						AccountObject myself = getMyself();
						if (myself != null) {
							int meberindex = getindexByMeberid(i, myself.getMemberId());
							if (meberindex != -1) {
								mList.get(i).getlMembers().remove(meberindex);
							}
						}
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	private int getindexByMeberid(int index, String meberid) {
		for (int i = 0; i < mList.get(index).getlMembers().size(); i++) {
			AccountObject account = mList.get(index).getlMembers().get(i);
			if (account.getMemberId().equals(meberid)) {
				return i;
			}
		}
		return -1;
	}

	public AccountObject getMyself() {
		AccountObject account;
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			account = new AccountObject();
			account.setMemberId(myself.getMemberId());
			account.setIcoUrl(myself.getIcoUrl());
			return account;
		}
		return null;
	}

	DynamicClickListener clickListener = null;

	public DynamicClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(DynamicClickListener clickListener) {
		this.clickListener = clickListener;
	}

	private void initHeaderView(final HeaderHolder headerHolder, DynamicObject obj) {
		if (!CommonTextUtils.isEmpty(obj.getAccountObj().getIcoUrl())) {
			FrescoHelper.displayImageview(headerHolder.userimg, obj.getAccountObj().getIcoUrl(),
					R.drawable.avatar_default_image, mContext.getResources(), true);
		} else {
			Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
			headerHolder.userimg.setImageURI(uri);
		}
		headerHolder.username.setOnClickListener(new headerImageClick(obj));
		headerHolder.userimg.setOnClickListener(new headerImageClick(obj));
		if (!CommonTextUtils.isEmpty(obj.getAccountObj().getNickName())) {
			headerHolder.username.setText(obj.getAccountObj().getNickName());
		} else {
			headerHolder.username.setText("");
		}
		if (!CommonTextUtils.isEmpty(obj.getAccountObj().getGender())) {
			if (obj.getAccountObj().getGender().equals("0")) {
				headerHolder.sexImage.setImageResource(R.drawable.sex_lady);
			} else {
				headerHolder.sexImage.setImageResource(R.drawable.sex_man);
			}
		} else {
			headerHolder.sexImage.setImageResource(R.drawable.sex_null);
		}
		if (!CommonTextUtils.isEmpty(obj.getLastLocateAddress())) {
			headerHolder.distanceTv.setText(obj.getLastLocateAddress());
		} else {
			headerHolder.distanceTv.setText("");
		}

		// LngLatObject mylnglat = App.getInstance().getLnglat();
		// if (obj.getAdressObject() != null && mylnglat != null) {
		// LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()),
		// Double.valueOf(mylnglat.getLng()));
		// LatLng cp = new
		// LatLng(Double.valueOf(obj.getAdressObject().getLat()),
		// Double.valueOf(obj.getAdressObject()
		// .getLng()));
		// NumberFormat format = NumberFormat.getNumberInstance();
		// format.setMaximumFractionDigits(2);
		// String distance = format.format(DistanceUtil.getDistance(cp, mp) /
		// 1000);
		// headerHolder.distanceTv.setText(distance +
		// mContext.getString(R.string.homepage_distance_text));
		// } else {
		// headerHolder.distanceTv.setText("");
		// }

		UserObject myself = CoreModel.getInstance().getMyself();
		if (!CommonTextUtils.isEmpty(obj.getAccountObj().getMemberId()) && myself != null) {
			if (myself.getMemberId().equals(obj.getAccountObj().getMemberId())) {
				headerHolder.followImage.setVisibility(View.GONE);
			} else {
				if (obj.getAccountObj().isIsFollowed()) {
					headerHolder.followImage.setVisibility(View.GONE);
				} else {
					headerHolder.followImage.setVisibility(View.VISIBLE);
				}
			}
		}
		headerHolder.followImage.setOnClickListener(new headerFollowImageClick(obj));
	}

	private void initView(final ViewHolder viewholder, DynamicObject obj) {
		if (obj.getPetPhotos() != null && obj.getPetPhotos().size() != 0) {
			HomeViewPagerAdapter imageAdapter = new HomeViewPagerAdapter(mContext, addShowView(obj));
			viewholder.dynamicViewPager.setAdapter(imageAdapter);
			viewholder.dynamicIndicator.setPaperCount(obj.getPetPhotos().size());
			viewholder.dynamicIndicator.setViewPager(viewholder.dynamicViewPager);
			viewholder.dynamicIndicator.setOnClickListener(new viewPaperClick(obj));
			viewholder.dynamicViewPager.setVisibility(View.VISIBLE);
		} else {
			viewholder.dynamicViewPager.setVisibility(View.INVISIBLE);
		}

		PetObject pet = obj.getPet();
		if (pet != null) {
			if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
				FrescoHelper.displayImageview(viewholder.dynamicPetImg,
						pet.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
						mContext.getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				viewholder.dynamicPetImg.setImageURI(uri);
			}
			viewholder.dynamicPetImg.setOnClickListener(new petClick(obj));
			viewholder.dynamicPetSexImg.setOnClickListener(new petClick(obj));
			viewholder.dynamicPetType.setOnClickListener(new petClick(obj));
			viewholder.dynamicPetName.setOnClickListener(new petClick(obj));
			if (pet.getGender().equals("0")) {
				viewholder.dynamicPetSexImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pet_bitch));
			} else {
				viewholder.dynamicPetSexImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pet_dog));
			}
			if (!CommonTextUtils.isEmpty(pet.getName())) {
				viewholder.dynamicPetName.setText(pet.getName());
			} else {
				viewholder.dynamicPetName.setText("");
			}

			if (!CommonTextUtils.isEmpty(pet.getCategoryName())) {
				viewholder.dynamicPetType.setText(pet.getCategoryName());
			} else {
				viewholder.dynamicPetType.setText("");
			}
		}

		if (!CommonTextUtils.isEmpty(obj.getCreateTime())) {
			viewholder.dynamicSendtime.setText(CommonUtils.getTimeSamp(mContext, obj.getCreateTime()));
		} else {
			viewholder.dynamicSendtime.setText("");
		}
		if (!CommonTextUtils.isEmpty(obj.getText())) {
			viewholder.dynamicContent.setText(obj.getText());
			viewholder.dynamicContent.setVisibility(View.VISIBLE);
		} else {
			viewholder.dynamicContent.setVisibility(View.GONE);
		}
		if (!CommonTextUtils.isEmpty(obj.getLikeCount()) && !obj.getLikeCount().equals("0")) {
			viewholder.moreZanTv.setText(obj.getLikeCount());
			viewholder.ZanText.setText(obj.getLikeCount());
		} else {
			viewholder.moreZanTv.setText(mContext.getString(R.string.dynamic_zan));
			viewholder.ZanText.setText(mContext.getString(R.string.dynamic_zan));
		}
		viewholder.moreZanTv.setOnClickListener(new moreZanClick(obj));
		viewholder.dynamicDianZanRL.setOnClickListener(new dianZanOrNotClick(obj));
		if (obj.isIsLiked()) {
			viewholder.ZanImage.setImageResource(R.drawable.icon_iszan);
		} else {
			viewholder.ZanImage.setImageResource(R.drawable.icon_notzan);
		}

		if (!CommonTextUtils.isEmpty(obj.getCommentCount()) && !obj.getCommentCount().equals("0")) {
			viewholder.commentNumTv.setText(obj.getCommentCount());
		} else {
			viewholder.commentNumTv.setText(mContext.getString(R.string.dynamic_comment));
		}
		viewholder.commentLL.setOnClickListener(new commentLLClick(obj));

		if (!CommonTextUtils.isEmpty(obj.getLookCount()) && !obj.getLookCount().equals("0")) {
			viewholder.readedNumTv.setText(obj.getLookCount());
		} else {
			viewholder.readedNumTv.setText(mContext.getString(R.string.dynamic_readed));
		}
		viewholder.shareMoreImage.setOnClickListener(new shareMoreClick(obj));
		if (obj.getlMembers() != null && obj.getlMembers().size() != 0) {
			HomeZanDynamicAdapter adapter = new HomeZanDynamicAdapter(mContext);
			viewholder.listZanUser.setAdapter(adapter);
			adapter.updateList(obj.getlMembers());
			viewholder.zanListRL.setVisibility(View.VISIBLE);
		} else {
			viewholder.zanListRL.setVisibility(View.GONE);
		}

		viewholder.listZanUser.setOnTouchListener(new zanListTouch(obj));
	}

	private ArrayList<View> addShowView(DynamicObject obj) {
		ArrayList<View> mListViews = new ArrayList<View>();
		ArrayList<String> urls = obj.getPetPhotos();
		int width = CommonUtils.getScreenWidth(mContext);
		if (urls != null && urls.size() > 0) {
			// ImageView view = new ImageView(mContext);
			// view.setScaleType(ScaleType.CENTER_CROP);
			SimpleDraweeView imgaeView = new SimpleDraweeView(mContext);
			LayoutParams params = new LayoutParams(width, width);
			params.width = width;
			params.height = width;
			imgaeView.setLayoutParams(params);
			if (!CommonTextUtils.isEmpty(urls.get(0))) {
				FrescoHelper.displayImageview(imgaeView, urls.get(0) + CommonUtils.getImageSize(mContext),
						R.drawable.image_default_image, mContext.getResources());
				// ImageLoaderHelper.displayImage(urls.get(0), view,
				// R.drawable.image_default_image, false);
			}
			mListViews.add(imgaeView);
			imgaeView.setOnClickListener(new mainClick(obj));
		} else {
			return null;
		}
		return mListViews;
	}

	class HeaderHolder {
		SimpleDraweeView userimg;
		TextView username;
		TextView distanceTv;
		ImageView followImage;
		ImageView sexImage;

		public HeaderHolder(View view) {
			this.userimg = (SimpleDraweeView) view.findViewById(R.id.dynamic_userimg);
			this.username = (TextView) view.findViewById(R.id.dynamic_name);
			this.distanceTv = (TextView) view.findViewById(R.id.dynamic_distance);
			this.followImage = (ImageView) view.findViewById(R.id.dynamic_followbtn);
			this.sexImage = (ImageView) view.findViewById(R.id.dynamic_usersex);
		}
	}

	class ViewHolder {
		DecoratorViewPager dynamicViewPager;
		TVPageIndicator dynamicIndicator;
		SimpleDraweeView dynamicPetImg;
		ImageView dynamicPetSexImg;
		TextView dynamicPetName;
		TextView dynamicPetType;
		TextView dynamicSendtime;
		TextView dynamicContent;
		TextView moreZanTv;
		PercentLinearLayout dynamicDianZanRL;
		ImageView ZanImage;
		TextView ZanText;
		PercentLinearLayout commentLL;
		TextView commentNumTv;
		TextView readedNumTv;
		ImageView shareMoreImage;
		HorizontalListView listZanUser;
		PercentRelativeLayout zanListRL;

		public ViewHolder(View view) {
			this.dynamicIndicator = (TVPageIndicator) view.findViewById(R.id.dynamic_pageIndicator);
			this.dynamicViewPager = (DecoratorViewPager) view.findViewById(R.id.dynamic_viewpager);
			this.dynamicPetImg = (SimpleDraweeView) view.findViewById(R.id.dynamic_petimg);
			this.dynamicPetSexImg = (ImageView) view.findViewById(R.id.dynamic_petsex);
			this.dynamicPetName = (TextView) view.findViewById(R.id.dynamic_petname);
			this.dynamicPetType = (TextView) view.findViewById(R.id.dynamic_pettype);
			this.dynamicSendtime = (TextView) view.findViewById(R.id.dynamic_sendtime);
			this.dynamicContent = (TextView) view.findViewById(R.id.dynamic_content);
			this.moreZanTv = (TextView) view.findViewById(R.id.dynamic_zan);
			this.dynamicDianZanRL = (PercentLinearLayout) view.findViewById(R.id.dynamic_zanRL);
			this.ZanImage = (ImageView) view.findViewById(R.id.dynamic_zanimg);
			this.ZanText = (TextView) view.findViewById(R.id.dynamic_zantext);
			this.commentLL = (PercentLinearLayout) view.findViewById(R.id.dynamic_commentRL);
			this.commentNumTv = (TextView) view.findViewById(R.id.dynamic_commentNum);
			this.readedNumTv = (TextView) view.findViewById(R.id.dynamic_readedNum);
			this.listZanUser = (HorizontalListView) view.findViewById(R.id.dynamic_zan_userlist);
			this.shareMoreImage = (ImageView) view.findViewById(R.id.dynamic_sharemoreimage);
			this.zanListRL = (PercentRelativeLayout) view.findViewById(R.id.dynamic_zanlistRL);
		}
	}

	class mainClick implements OnClickListener {
		DynamicObject obj;

		mainClick(DynamicObject obj) {
			this.obj = obj;
		}

		@Override
		public void onClick(View v) {
			LogUtils.i("onClick", "mainClick");
			if (clickListener != null) {
				clickListener.mainClick(obj);
			}
		}
	}

	class headerImageClick implements OnClickListener {
		DynamicObject obj;

		headerImageClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.headerImageClick(obj);
			}
		}
	}

	class headerFollowImageClick implements OnClickListener {
		DynamicObject obj;

		headerFollowImageClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.headerFollowImageClick(obj);
			}
		}
	}

	class userImageClick implements OnClickListener {
		DynamicObject obj;

		userImageClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.userImageClick(obj);
			}
		}
	}

	class userFollowImageClick implements OnClickListener {
		DynamicObject obj;

		userFollowImageClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.userFollowImageClick(obj);
			}
		}
	}

	class viewPaperClick implements OnClickListener {
		DynamicObject obj;

		viewPaperClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.viewPaperClick(obj);
			}
		}
	}

	class petClick implements OnClickListener {
		DynamicObject obj;

		petClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.petClick(obj);
			}
		}
	}

	class moreZanClick implements OnClickListener {
		DynamicObject obj;

		moreZanClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.moreZanClick(obj);
			}
		}
	}

	class dianZanOrNotClick implements OnClickListener {
		DynamicObject obj;

		dianZanOrNotClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.dianZanOrNotClick(obj);
			}
		}
	}

	class commentLLClick implements OnClickListener {
		DynamicObject obj;

		commentLLClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.commentLLClick(obj);
			}
		}
	}

	class zanListTouch implements OnTouchListener {
		DynamicObject obj;
		boolean start = false;

		zanListTouch(DynamicObject obj) {
			this.obj = obj;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				start = true;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				start = false;
				return false;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (start) {
					if (clickListener != null) {
						clickListener.zanlistClick(obj);
					}
				}
			}
			return false;
		}

	}

	class shareMoreClick implements OnClickListener {
		DynamicObject obj;

		shareMoreClick(DynamicObject obj) {
			this.obj = obj;
		}

		public void onClick(View view) {
			if (clickListener != null) {
				clickListener.shareMoreClick(obj);
			}
		}
	}

	public int getCount() {
		return mList.size();
	}

	@Override
	public DynamicObject getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_home_new_child, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DynamicObject obj = getItem(position);
		if (obj != null) {
			initView(holder, obj);
		}
		convertView.setOnClickListener(new mainClick(obj));
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_home_new_group, parent, false);
			holder = new HeaderHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (HeaderHolder) convertView.getTag();
		}
		DynamicObject obj = getItem(position);
		if (obj != null) {
			initHeaderView(holder, obj);
		}
		convertView.setOnClickListener(new mainClick(obj));
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return position;
	}

}