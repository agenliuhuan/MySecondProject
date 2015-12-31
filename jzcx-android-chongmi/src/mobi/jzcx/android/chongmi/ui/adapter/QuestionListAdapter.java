package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.QuestionObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
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

public class QuestionListAdapter extends BaseAdapter {
	ArrayList<QuestionObject> questionList;
	Context mContext;

	public QuestionListAdapter(Context context) {
		this.mContext = context;
		questionList = new ArrayList<QuestionObject>();
	}

	public void setData(ArrayList<QuestionObject> data) {
		if (questionList != null) {
			questionList.clear();
			questionList.addAll(data);
		}
		notifyDataSetChanged();
	}
	public int getCount() {

		return questionList.size();
	}

	public QuestionObject getItem(int position) {
		return questionList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_question, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		QuestionObject question = getItem(position);
		if (question != null) {
			if (!CommonTextUtils.isEmpty(question.getIcoUrl())) {
				FrescoHelper.displayImageview(holder.questionAvatar,
						question.getIcoUrl() + CommonUtils.getAvatarSize(mContext), R.drawable.avatar_default_image,
						mContext.getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(question.getTitle())) {
				holder.questionTitle.setText(question.getTitle());
			} else {
				holder.questionTitle.setText("");
			}
			if (!CommonTextUtils.isEmpty(question.getOfficialAnswer())) {
				holder.questionContext.setText(question.getOfficialAnswer());
			} else {
				holder.questionContext.setText("");
			}
			holder.questionAvatar.setOnClickListener(new AvatarClick(question));
		}
		return convertView;
	}

	class ViewHolder {
		SimpleDraweeView questionAvatar;
		TextView questionTitle;
		TextView questionContext;

		public ViewHolder(View view) {
			this.questionAvatar = (SimpleDraweeView) view.findViewById(R.id.question_avatar);
			this.questionTitle = (TextView) view.findViewById(R.id.question_title);
			this.questionContext = (TextView) view.findViewById(R.id.question_context);
		}
	}

	class AvatarClick implements OnClickListener {
		QuestionObject question;
		AvatarClick(QuestionObject question) {
			this.question = question;
		}

		public void onClick(View v) {
			AccountCenterActivity.startActivity(mContext, question.getMemberId());
		}

	}

}
