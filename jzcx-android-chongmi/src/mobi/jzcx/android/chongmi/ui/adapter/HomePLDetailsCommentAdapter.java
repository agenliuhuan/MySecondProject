package mobi.jzcx.android.chongmi.ui.adapter;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.CommontObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportClickListener;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.chongmi.view.swipe.SwipeOnTouchListener;
import mobi.jzcx.android.chongmi.view.swipe.SwipeViewHolder;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class HomePLDetailsCommentAdapter extends BaseAdapter {
	private List<CommontObject> data;
	private int itemWidth = 0;
	Context mContext;
	String microblogid;
	String meberid;
	ReportClickListener reportClick;
	boolean AllClose = true;

	public HomePLDetailsCommentAdapter(Context context) {
		this.mContext = context;
		data = new ArrayList<CommontObject>();
	}

	public int getCount() {
		return data.size();
	}

	public CommontObject getItem(int position) {
		return data.get(position);
	}

	public void closeAllItem() {
		if (!AllClose) {
			AllClose = true;
			notifyDataSetChanged();
		}
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateList(ArrayList<CommontObject> list) {
		if (data != null) {
			data.clear();
			data.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void setMicroblogid(String microblogid) {
		this.microblogid = microblogid;
	}

	public void setMeberid(String meberid) {
		this.meberid = meberid;
	}

	public void setReportClick(ReportClickListener reportClick) {
		this.reportClick = reportClick;
	}

	SwipeViewHolder holder = null;

	public View getView(int position, View convertView, ViewGroup parent) {

		CommontObject commentObj = getItem(position);
		if (commentObj != null) {
			if (convertView == null || convertView.getTag() == null) {
				holder = new SwipeViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_swipe, parent, false);
				holder.hSView = (HorizontalScrollView) convertView.findViewById(R.id.hsv);
				holder.viewContainer = (LinearLayout) convertView.findViewById(R.id.item_view_container);
				View contentView = LayoutInflater.from(mContext).inflate(R.layout.listitem_home_petlog_comment, parent,
						false);
				View actionView = LayoutInflater.from(mContext).inflate(R.layout.item_action, parent, false);
				holder.viewContainer.addView(contentView);
				holder.viewContainer.addView(actionView);

				convertView.setTag(holder);
			} else {
				holder = (SwipeViewHolder) convertView.getTag();
				holder.hSView.scrollTo(0, 0);
			}

			holder.voiceRL = (PercentRelativeLayout) convertView.findViewById(R.id.petdiartycomment_voice);
			holder.voiceImg = (ImageView) convertView.findViewById(R.id.petdiartycomment_voiceimg);
			holder.voiceText = (TextView) convertView.findViewById(R.id.petdiartycomment_voicetext);
			holder.contentTv = (TextView) convertView.findViewById(R.id.petdiartycomment_content);
			holder.userimg = (SimpleDraweeView) convertView.findViewById(R.id.petdiartycomment_userimg);
			holder.timeTv = (TextView) convertView.findViewById(R.id.petdiartycomment_time);
			holder.voicenameTv = (TextView) convertView.findViewById(R.id.petdiartycomment_voice_name);
			holder.replyLL = (PercentLinearLayout) convertView.findViewById(R.id.petdiartycomment_replyLL);
			holder.replyNameTV = (TextView) convertView.findViewById(R.id.petdiartycomment_reply_name);
			holder.deleteImg = (ImageView) convertView.findViewById(R.id.delete);
			AccountObject replyAccount = commentObj.getReplyAccount();

			AccountObject account = commentObj.getAccount();
			if (account != null) {
				if (!CommonTextUtils.isEmpty(account.getIcoUrl())) {
					FrescoHelper.displayImageview(holder.userimg,
							account.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
							mContext.getResources(), true);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
					holder.userimg.setImageURI(uri);
				}
			}
			holder.userimg.setOnClickListener(new userImgClick(account));

			if (!CommonTextUtils.isEmpty(commentObj.getCreateTime())) {
				holder.timeTv.setText(CommonUtils.getTimeSamp(mContext, commentObj.getCreateTime()));
			} else {
				holder.timeTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(account.getNickName())) {
				holder.voicenameTv.setText(account.getNickName());
			} else {
				holder.voicenameTv.setText("");
			}
			UserObject myself = CoreModel.getInstance().getMyself();
			if (myself != null) {
				if (myself.getMemberId().equals(commentObj.getMemberID())) {
					holder.deleteImg.setBackgroundResource(R.drawable.delete_menubg);
				} else {
					holder.deleteImg.setBackgroundResource(R.drawable.report_menubg);
				}
			}
			holder.deleteImg.setOnClickListener(new reportClick(commentObj));
			holder.contentTv.setText("");
			if (!CommonTextUtils.isEmpty(commentObj.getText())) {
				if (replyAccount != null) {
					SpannableStringBuilder builder = new SpannableStringBuilder(
							mContext.getString(R.string.eventdetail_reply) + "   " + replyAccount.getNickName() + ":"
									+ commentObj.getText());
					LogUtils.i("builder", builder.toString());
					ForegroundColorSpan mySpan = new ForegroundColorSpan(Color.parseColor("#286eb3"));
					builder.setSpan(mySpan, 5, 5 + replyAccount.getNickName().length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					holder.contentTv.setText(builder);
				} else {
					holder.contentTv.setText(commentObj.getText());
				}
			} else {
				holder.contentTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(commentObj.getVoiceUrl())) {
				if (replyAccount != null) {
					if (!CommonTextUtils.isEmpty(replyAccount.getNickName())) {
						holder.replyNameTV.setText(replyAccount.getNickName() + ":");
					} else {
						holder.replyNameTV.setText("");
					}
					holder.replyLL.setVisibility(View.VISIBLE);
				} else {
					holder.replyNameTV.setText("");
					holder.replyLL.setVisibility(View.GONE);
				}

				holder.voiceRL.setVisibility(View.VISIBLE);
				holder.contentTv.setVisibility(View.GONE);
				String localPath = FileUtils.VOICE + MD5.md5(commentObj.getVoiceUrl()) + ".amr";
				if (FileUtils.exists(localPath)) {
					try {
						long duration = CommonUtils.getAmrDuration(new File(localPath));

						NumberFormat nf = NumberFormat.getNumberInstance();
						nf.setMaximumFractionDigits(2);
						int d = (int) (duration / 1000);
						if (d == 0) {
							d = 1;
						}
						if (d == 59 || d == 61) {
							d = 60;
						}
						holder.voiceText.setText(nf.format(d));
					} catch (IOException e) {
						e.printStackTrace();
					}
					holder.voiceRL.setOnClickListener(new VoicePlayClickListener(localPath, holder.voiceImg, this,
							mContext));
				} else {
					holder.voiceText.setText("");
				}
			} else {
				holder.voiceRL.setVisibility(View.GONE);
				holder.contentTv.setVisibility(View.VISIBLE);
			}

		}

		// ViewTreeObserver vto = holder.hSView.getViewTreeObserver();
		// vto.addOnGlobalLayoutListener(new
		// ViewTreeObserver.OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// if (itemWidth == 0) {
		// itemWidth = holder.hSView.getMeasuredWidth();
		// notifyDataSetChanged();
		// }
		// }
		// });
		itemWidth = CommonUtils.getScreenWidth(mContext);
		// 设置默认宽度
		ViewGroup.LayoutParams lpContent = holder.viewContainer.getChildAt(0).getLayoutParams();
		lpContent.width = itemWidth;
		// 设置默认宽度
		ViewGroup.LayoutParams lpAction = holder.viewContainer.getChildAt(1).getLayoutParams();
		lpAction.width = itemWidth / 5;
		// 定义item显示的内容区
		// setContentView(viewHolder.viewContainer.getChildAt(0), position,
		// holder.hSView);
		// // 定义item隐藏的操作区域
		// setActionView(viewHolder.viewContainer.getChildAt(1), position,
		// viewHolder.hSView);
		// 设置滑动事件监听
		convertView.setOnTouchListener(new SwipeOnTouchListener());
		holder.viewContainer.setOnClickListener(new mainClick(commentObj));
		return convertView;
	}

	class userImgClick implements OnClickListener {
		AccountObject account;

		userImgClick(AccountObject account) {
			this.account = account;
		}

		@Override
		public void onClick(View v) {
			if (account != null) {
				AccountCenterActivity.startActivity(mContext, account.getMemberId());
			}
		}
	}

	class reportClick implements OnClickListener {
		CommontObject commentObj;

		reportClick(CommontObject commentObj) {
			this.commentObj = commentObj;
		}

		@Override
		public void onClick(View v) {
			if (reportClick != null) {
				reportClick.reportClick(commentObj);
			}
		}
	}

	class mainClick implements OnClickListener {
		CommontObject commentObj;

		mainClick(CommontObject commentObj) {
			this.commentObj = commentObj;
		}

		@Override
		public void onClick(View v) {
			if (reportClick != null) {
				reportClick.mainClick(commentObj);
			}
		}
	}

}
