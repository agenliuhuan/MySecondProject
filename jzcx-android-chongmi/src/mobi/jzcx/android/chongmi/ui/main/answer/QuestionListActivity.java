package mobi.jzcx.android.chongmi.ui.main.answer;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.QuestionObject;
import mobi.jzcx.android.chongmi.ui.adapter.QuestionListAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.view.CanPullListView;
import mobi.jzcx.android.chongmi.view.ClearEditText;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionListActivity extends BaseExActivity {
	PullToRefreshLayout questionListRefresh;
	CanPullListView questionListListView;
	TextView noresultTv;
	TextView cancelTV;
	ClearEditText clearEdit;
	LinearLayout searchLL;
	PercentRelativeLayout editRL;
	PercentRelativeLayout serchRL;
	int pageIndex;
	int serchPageIndex;
	boolean isSearch = false;
	ArrayList<QuestionObject> QuestionList;
	ArrayList<QuestionObject> SearchQuestionList;
	QuestionListAdapter questionAdapter;

	ImageView addQuestionImg;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, QuestionListActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionlist);
		initView();
		initData();
		mSetStatusBar = false;
	}

	private void initView() {
		addQuestionImg = (ImageView) findViewById(R.id.questionlist_addQuestionImg);
		addQuestionImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddQuestionActivity.startActivity(mActivity);
			}
		});

		questionListRefresh = (PullToRefreshLayout) findViewById(R.id.questionlist_refresh);
		questionListListView = (CanPullListView) findViewById(R.id.questionlist_listview);
		questionAdapter = new QuestionListAdapter(mActivity);
		questionListListView.setAdapter(questionAdapter);
		questionListRefresh.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				if (isSearch && !CommonTextUtils.isEmpty(clearEdit.getText().toString())) {
					serchPageIndex = 1;
					sendMessage(YSMSG.REQ_SearchQuestionList, serchPageIndex, 0, clearEdit.getText().toString());
				} else {
					pageIndex = 1;
					sendMessage(YSMSG.REQ_QuestionQuestionList, pageIndex, 0, null);
				}
			}

			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (isSearch && !CommonTextUtils.isEmpty(clearEdit.getText().toString())) {
					sendMessage(YSMSG.REQ_SearchQuestionList, serchPageIndex, 0, clearEdit.getText().toString());
				} else {
					sendMessage(YSMSG.REQ_QuestionQuestionList, pageIndex, 0, null);
				}
			}
		});
		questionListListView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
		noresultTv = (TextView) findViewById(R.id.questionlist_noresultTV);
		cancelTV = (TextView) findViewById(R.id.questionlist_canceleditTv);
		clearEdit = (ClearEditText) findViewById(R.id.questionlist_edit);
		searchLL = (LinearLayout) findViewById(R.id.questionlist_serchLL);
		editRL = (PercentRelativeLayout) findViewById(R.id.questionlist_editRL);
		serchRL = (PercentRelativeLayout) findViewById(R.id.questionlist_serchRL);
		searchLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isSearch = true;
				addQuestionImg.setVisibility(View.GONE);
				serchRL.setVisibility(View.GONE);
				editRL.setVisibility(View.VISIBLE);
				clearEdit.requestFocus();
				showkeyboard();
				questionAdapter.setData(SearchQuestionList);
			}
		});
		cancelTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				isSearch = false;
				clearEdit.setText("");
				clearEdit.clearFocus();
				noresultTv.setVisibility(View.GONE);
				editRL.setVisibility(View.GONE);
				pageIndex = 1;
				sendMessage(YSMSG.REQ_QuestionQuestionList, pageIndex, 0, null);
				questionListRefresh.setVisibility(View.VISIBLE);
				serchRL.setVisibility(View.VISIBLE);
				addQuestionImg.setVisibility(View.VISIBLE);
			}
		});
		clearEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					SearchQuestionList.clear();
					questionAdapter.setData(SearchQuestionList);
					questionListRefresh.setVisibility(View.VISIBLE);
					noresultTv.setVisibility(View.GONE);
				} else {
					serchPageIndex = 1;
					sendMessage(YSMSG.REQ_SearchQuestionList, serchPageIndex, 0, clearEdit.getText().toString());
				}

			}
		});
		questionListListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				QuestionObject questionObj = (QuestionObject) parent.getAdapter().getItem(position);
				if (questionObj != null) {
					MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_6));
					QuestionDetailActivity.startActivity(mActivity, questionObj);
				}
			}
		});
		ImageView backImg = (ImageView) findViewById(R.id.questionlist_backImg);
		backImg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showkeyboard() {
		InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(clearEdit, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void initData() {
		pageIndex = 1;
		sendMessage(YSMSG.REQ_QuestionQuestionList, pageIndex, 0, null);
		QuestionList = new ArrayList<QuestionObject>();
		SearchQuestionList = new ArrayList<QuestionObject>();
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_QuestionQuestionList :
				if (msg.arg1 == 200) {
					questionListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
					if (msg.obj != null) {
						/*
						 * {"PageCount":1,"HasMore":false,"List":[],"PageIndex":1
						 * ,"PageSize":10}
						 */

						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								questionListListView.setCanPullUp(true);
							} else {
								questionListListView.setCanPullUp(false);
							}
							if (pageIndex == 1) {
								QuestionList.clear();
							}
							pageIndex++;
							for (int i = 0; i < array.length(); i++) {
								QuestionObject questionObj = OJMFactory.createOJM().fromJson(array.getString(i),
										QuestionObject.class);
								QuestionList.add(questionObj);
							}
							questionAdapter.setData(QuestionList);
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
					questionListListView.setCanPullUp(false);
					questionListRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
			case YSMSG.RESP_SearchQuestionList :
				if (!isSearch) {
					return;
				}
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						/*
						 * {"PageCount":1,"HasMore":false,"List":[],"PageIndex":1
						 * ,"PageSize":10}
						 */
						questionListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								questionListListView.setCanPullUp(true);
							} else {
								questionListListView.setCanPullUp(false);
							}
							if (serchPageIndex == 1) {
								SearchQuestionList.clear();
							}
							serchPageIndex++;
							for (int i = 0; i < array.length(); i++) {
								QuestionObject questionObj = OJMFactory.createOJM().fromJson(array.getString(i),
										QuestionObject.class);
								SearchQuestionList.add(questionObj);
							}
							if (SearchQuestionList.size() > 0) {
								questionAdapter.setData(SearchQuestionList);
								questionListRefresh.setVisibility(View.VISIBLE);
								noresultTv.setVisibility(View.GONE);
							} else {
								questionListRefresh.setVisibility(View.GONE);
								noresultTv.setVisibility(View.VISIBLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					questionListListView.setCanPullUp(false);
					questionListRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;

			default :
				break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (clearEdit.isShown()) {
				hideSoftKeyboard();
				isSearch = false;
				clearEdit.setText("");
				clearEdit.clearFocus();
				editRL.setVisibility(View.GONE);
				noresultTv.setVisibility(View.GONE);
				serchRL.setVisibility(View.VISIBLE);
				pageIndex = 1;
				sendMessage(YSMSG.REQ_QuestionQuestionList, pageIndex, 0, null);
				questionListRefresh.setVisibility(View.VISIBLE);
				addQuestionImg.setVisibility(View.VISIBLE);
				return true;
			} else {
				mActivity.finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
