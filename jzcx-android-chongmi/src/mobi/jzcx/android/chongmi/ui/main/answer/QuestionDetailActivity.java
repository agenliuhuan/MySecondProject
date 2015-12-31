package mobi.jzcx.android.chongmi.ui.main.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AnswerObject;
import mobi.jzcx.android.chongmi.biz.vo.AnswerQuestionObject;
import mobi.jzcx.android.chongmi.biz.vo.QuestionObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.adapter.AnswerListAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.QuestionDetailImageAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.VPagerImageDetailsActivity;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.WidgetUtil;
import mobi.jzcx.android.chongmi.view.CanPullScrollView;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class QuestionDetailActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	static QuestionObject questionObj;
	PullToRefreshLayout questiondetailRefresh;
	CanPullScrollView scrollView;
	SimpleDraweeView UserAvatar;
	TextView UserName;
	TextView TimeTV;
	TextView titleTV;
	TextView contentTV;
	TextView commentNumTV;
	TextView officialTimeTV;
	TextView officialContentTV;
	GridView imageGrid;
	QuestionDetailImageAdapter gridImageAdapter;

	ListView answerListView;

	AnswerListAdapter answerAdapter;
	ArrayList<AnswerObject> answerList;

	int pageIndex;

	public static void startActivity(Context context, QuestionObject questionObj) {
		ActivityUtils.startActivity(context, QuestionDetailActivity.class);
		QuestionDetailActivity.questionObj = questionObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questiondetail);
		initView();

	}

	private void initView() {
		initTitleBar();
		initInput();

		questiondetailRefresh = (PullToRefreshLayout) findViewById(R.id.questiondetail_refresh);
		scrollView = (CanPullScrollView) findViewById(R.id.questiondetail_scrollview);
		UserAvatar = (SimpleDraweeView) findViewById(R.id.questiondetail_useravatar);
		UserName = (TextView) findViewById(R.id.questiondetail_username);
		TimeTV = (TextView) findViewById(R.id.questiondetail_time);
		titleTV = (TextView) findViewById(R.id.questiondetail_title);
		contentTV = (TextView) findViewById(R.id.questiondetail_context);
		commentNumTV = (TextView) findViewById(R.id.questiondetail_commentNum);
		officialTimeTV = (TextView) findViewById(R.id.questiondetail_officialTime);
		officialContentTV = (TextView) findViewById(R.id.questiondetail_officialContent);

		imageGrid = (GridView) findViewById(R.id.questiondetail_grid);
		gridImageAdapter = new QuestionDetailImageAdapter(mActivity);
		imageGrid.setAdapter(gridImageAdapter);
		imageGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VPagerImageDetailsActivity.startActivity(mActivity, gridImageAdapter.getmList(), position);
			}
		});

		answerListView = (ListView) findViewById(R.id.questiondetail_answerList);
		answerAdapter = new AnswerListAdapter(mActivity);
		answerListView.setAdapter(answerAdapter);

		scrollView.setCanPullDown(false);
		questiondetailRefresh.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

			}

			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				sendMessage(YSMSG.REQ_QuestionAnswerList, questionObj.getQuestionId(), pageIndex, null);
			}
		});
		answerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AnswerObject answerObj = (AnswerObject) parent.getAdapter().getItem(position);
				AnswerDetailActivity.startActivity(mActivity, answerObj);
			}
		});
		UserAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountCenterActivity.startActivity(mActivity, questionObj.getMemberId());
			}
		});
		UserName.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountCenterActivity.startActivity(mActivity, questionObj.getMemberId());
			}
		});
		ImageView officialAvatar = (ImageView) findViewById(R.id.questiondetail_officialAvatar);
		TextView officialName = (TextView) findViewById(R.id.questiondetail_officialName);
		PercentLinearLayout doctorLL = (PercentLinearLayout) findViewById(R.id.questiondetail_doctorLL);
		doctorLL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerObject answerObj = new AnswerObject();
				answerObj.setIcoUrl("doctor");
				answerObj.setNickName("doctor");
				answerObj.setCreateTime(questionObj.getAnswerTime());
				answerObj.setAnswerText(questionObj.getOfficialAnswer());
				AnswerDetailActivity.startActivity(mActivity, answerObj);
			}
		});
		officialAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerDoctorActivity.startActivity(mActivity);
			}
		});
		officialName.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerDoctorActivity.startActivity(mActivity);
			}
		});
	}

	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		if (questionObj != null) {
			if (!CommonTextUtils.isEmpty(questionObj.getIcoUrl())) {
				FrescoHelper.displayImageview(UserAvatar,
						questionObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
						R.drawable.avatar_default_image, mActivity.getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(questionObj.getNickName())) {
				UserName.setText(questionObj.getNickName());
			} else {
				UserName.setText("");
			}
			if (!CommonTextUtils.isEmpty(questionObj.getCreateTime())) {
				TimeTV.setText(questionObj.getCreateTime());
			} else {
				TimeTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(questionObj.getTitle())) {
				titleTV.setText(questionObj.getTitle());
			} else {
				titleTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(questionObj.getContext())) {
				contentTV.setText(questionObj.getContext());
			} else {
				contentTV.setVisibility(View.GONE);
			}
			// if (!CommonTextUtils.isEmpty(questionObj.getAnswerCount())) {
			// commentNumTV.setText(questionObj.getAnswerCount());
			// } else {
			// commentNumTV.setText("0");
			// }
			if (!CommonTextUtils.isEmpty(questionObj.getAnswerTime())) {
				officialTimeTV.setText(questionObj.getAnswerTime());
			} else {
				officialTimeTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(questionObj.getOfficialAnswer())) {
				// officialContentTV.setText(questionObj.getOfficialAnswer());
				WidgetUtil.handleTextView(officialContentTV, questionObj.getOfficialAnswer());
			} else {
				officialContentTV.setText("");
			}
			if (!CommonTextUtils.isEmpty(questionObj.getImgs())) {
				String[] urls = questionObj.getImgs().split(",");
				List<String> urllist = Arrays.asList(urls);
				gridImageAdapter.updateList(urllist);
				imageGrid.setVisibility(View.VISIBLE);
			} else {
				imageGrid.setVisibility(View.GONE);
			}

			answerList = new ArrayList<AnswerObject>();
			pageIndex = 1;
			sendMessage(YSMSG.REQ_QuestionAnswerList, questionObj.getQuestionId(), pageIndex, null);
		}
	}
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_QuestionAnswerList :
				if (msg.arg1 == 200) {
					questiondetailRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (msg.obj != null) {
						/*
						 * "PageIndex":1,"PageCount":1,"PageSize":10,"HasMore":false
						 * ,"List":[]"RecordCount":-1
						 */

						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							int RecordCount = json.getInt("RecordCount");
							commentNumTV.setText(String.valueOf(RecordCount));
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								scrollView.setCanPullUp(true);
							} else {
								scrollView.setCanPullUp(false);
							}
							if (pageIndex == 1) {
								answerList.clear();
							}
							pageIndex++;
							for (int i = 0; i < array.length(); i++) {
								AnswerObject questionObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AnswerObject.class);
								answerList.add(questionObj);
							}
							answerAdapter.setData(answerList);
							CommonUtils.setListViewHeightBasedOnChildren(answerListView);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					scrollView.setCanPullUp(false);
					questiondetailRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
			case YSMSG.RESP_QuestionAnswer :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					editText.setText("");
					hideSoftKeyboard();
					pageIndex = 1;
					sendMessage(YSMSG.REQ_QuestionAnswerList, questionObj.getQuestionId(), pageIndex, null);
					YSToast.showToast(mActivity, getString(R.string.toast_answer_success));
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;

			default :
				break;
		}
	}
	private EditText editText;
	private Button sendBtn;
	private void initInput() {
		PercentRelativeLayout inputRL = (PercentRelativeLayout) findViewById(R.id.questiondetail_inputRL);
		editText = (EditText) findViewById(R.id.questiondetail_edit);
		sendBtn = (Button) findViewById(R.id.questiondetail_sendtext);
		sendBtn.setEnabled(false);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}

			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnswerQuestionObject answerQuestion = new AnswerQuestionObject();
				answerQuestion.setQuestionId(String.valueOf(questionObj.getQuestionId()));
				answerQuestion.setAnswerText(editText.getText().toString());
				sendMessage(YSMSG.REQ_QuestionAnswer, 0, 0, answerQuestion);
				showWaitingDialog();
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.main_questiondetail));
		// mTitleBar.mRightImg.setImageResource(R.drawable.question_shareimg);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				QuestionDetailActivity.this.finish();
			}
		});
	}

}
