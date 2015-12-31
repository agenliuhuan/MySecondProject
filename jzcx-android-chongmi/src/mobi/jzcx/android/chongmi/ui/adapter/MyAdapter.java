package mobi.jzcx.android.chongmi.ui.adapter;

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
import mobi.jzcx.android.chongmi.view.swipe.SwipeLayoutAdapter;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class MyAdapter extends SwipeLayoutAdapter<CommontObject> {
	private List<CommontObject> data;
	Context mContext;
	String microblogid;
	String meberid;
	ReportClickListener reportClick;

	public MyAdapter(Context context, int contentViewResourceId, int actionViewResourceId, List<CommontObject> objects) {
		super(context, contentViewResourceId, actionViewResourceId, objects);
		data = new ArrayList<CommontObject>();
		data.clear();
		data.addAll(objects);
		mContext = context;
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

	// 实现setContentView方法
	@Override
	public void setContentView(View contentView, int position, HorizontalScrollView parent) {
		SimpleDraweeView userimg = (SimpleDraweeView) contentView.findViewById(R.id.petdiartycomment_userimg);
		TextView timeTv = (TextView) contentView.findViewById(R.id.petdiartycomment_time);
		TextView contentTv = (TextView) contentView.findViewById(R.id.petdiartycomment_content);
		TextView voicenameTv = (TextView) contentView.findViewById(R.id.petdiartycomment_voice_name);
		PercentRelativeLayout voiceRL = (PercentRelativeLayout) contentView.findViewById(R.id.petdiartycomment_voice);
		ImageView voiceImg = (ImageView) contentView.findViewById(R.id.petdiartycomment_voiceimg);
		TextView voiceText = (TextView) contentView.findViewById(R.id.petdiartycomment_voicetext);
		CommontObject commentObj = (CommontObject) getItem(position);
		if (commentObj != null) {
			AccountObject account = commentObj.getAccount();
			String name = "";
			if (account != null) {
				if (!CommonTextUtils.isEmpty(account.getIcoUrl())) {
					FrescoHelper.displayImageview(userimg, account.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
							R.drawable.avatar_default_image, mContext.getResources(), true);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
					userimg.setImageURI(uri);
				}
				name = account.getNickName();
			}
			if (!CommonTextUtils.isEmpty(commentObj.getTimeStamp())) {
				timeTv.setText(commentObj.getTimeStamp());
			} else {
				timeTv.setText("");
			}
			if (!CommonTextUtils.isEmpty(commentObj.getText())) {
				contentTv.setText(commentObj.getText());
			} else {
				contentTv.setText("");
			}
			// if (!CommonTextUtils.isEmpty(commentObj.getVoiceUrl())) {
			// voiceRL.setVisibility(View.VISIBLE);
			// contentTv.setVisibility(View.GONE);
			// } else {
			//
			// }
			voiceRL.setVisibility(View.GONE);
			contentTv.setVisibility(View.VISIBLE);
			if (!CommonTextUtils.isEmpty(name)) {
				voicenameTv.setText(name);
			} else {
				voicenameTv.setText("");
			}
			userimg.setOnClickListener(new userImgClick(account));

			// String localPath = FileUtils.VOICE +
			// DemoUtils.md5(commentObj.getVoiceUrl()) + ".amr";
			//
			// if (FileUtils.exists(localPath)) {
			// try {
			// long duration = CommonUtils.getAmrDuration(new File(localPath));
			// NumberFormat nf = NumberFormat.getNumberInstance();
			// nf.setMaximumFractionDigits(2);
			// voiceText.setText(nf.format(Double.valueOf(duration / 1000)));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// voiceRL.setOnClickListener(new VoicePlayClickListener(localPath,
			// voiceImg, this, mContext));
			// } else {
			// voiceText.setText("");
			// }
		}
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

	@Override
	public void setActionView(View actionView, final int position, final HorizontalScrollView parent) {
		UserObject myself = CoreModel.getInstance().getMyself();
		CommontObject commentObj = (CommontObject) getItem(position);
		ImageView del = (ImageView) actionView.findViewById(R.id.delete);
		if (myself != null) {
			if (myself.getMemberId().equals(meberid)) {
				del.setBackgroundResource(R.drawable.delete_menubg);
			} else {
				del.setBackgroundResource(R.drawable.report_menubg);
			}
		}
		del.setOnClickListener(new reportClick(commentObj));

	}
}
