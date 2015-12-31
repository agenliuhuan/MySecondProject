package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.AnswerObject;
import mobi.jzcx.android.chongmi.biz.vo.QuestionObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.WidgetUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AnswerListAdapter extends BaseAdapter {
	ArrayList<AnswerObject> answerList;
	Context mContext;

	public AnswerListAdapter(Context context) {
		this.mContext = context;
		answerList = new ArrayList<AnswerObject>();
	}

	public void setData(ArrayList<AnswerObject> data) {
		if (answerList != null) {
			answerList.clear();
			answerList.addAll(data);
		}
		notifyDataSetChanged();
	}
	public int getCount() {
		return answerList.size();
	}

	public AnswerObject getItem(int position) {
		return answerList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_answer, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AnswerObject answer = getItem(position);
		if (answer != null) {
			if (!CommonTextUtils.isEmpty(answer.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.answerAvatar,
						answer.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
						mContext.getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(answer.getNickName())) {
				holder.answerName.setText(answer.getNickName());
			} else {
				holder.answerName.setText("");
			}
			if (!CommonTextUtils.isEmpty(answer.getCreateTime())) {
				holder.answerTime.setText(answer.getCreateTime());
			} else {
				holder.answerTime.setText("");
			}
			if (!CommonTextUtils.isEmpty(answer.getAnswerText())) {
				WidgetUtil.handleTextView(holder.answerContext, answer.getAnswerText());
			} else {
				holder.answerContext.setText("");
			}
			holder.answerAvatar.setOnClickListener(new AvatarClick(answer));
			holder.answerName.setOnClickListener(new AvatarClick(answer));
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView answerAvatar;
		TextView answerName;
		TextView answerTime;
		TextView answerContext;

		public ViewHolder(View view) {
			this.answerAvatar = (SimpleDraweeView) view.findViewById(R.id.listitemanswer_useravatar);
			this.answerName = (TextView) view.findViewById(R.id.listitemanswer_username);
			this.answerTime = (TextView) view.findViewById(R.id.listitemanswer_time);
			this.answerContext = (TextView) view.findViewById(R.id.listitemanswer_context);
		}
	}

	class AvatarClick implements OnClickListener {
		AnswerObject answer;
		AvatarClick(AnswerObject answer) {
			this.answer = answer;
		}

		public void onClick(View v) {
			AccountCenterActivity.startActivity(mContext, answer.getMemberId());
		}

	}

}
