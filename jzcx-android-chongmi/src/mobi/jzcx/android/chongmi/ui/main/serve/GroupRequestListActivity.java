package mobi.jzcx.android.chongmi.ui.main.serve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.ConfirmJoinObject;
import mobi.jzcx.android.chongmi.biz.vo.GroupRequest;
import mobi.jzcx.android.chongmi.ui.adapter.GroupRequestListAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.chongmi.view.ReportOrDeletePopupWindow;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenu;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuCreator;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuItem;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class GroupRequestListActivity extends BaseExActivity implements RequestClickListener {
	protected TitleBarHolder mTitleBar = null;
	private SwipeMenuListView xlistView;
	View emptyview;
	GroupRequestListAdapter adapter;
	GroupRequest curObj;
	List<GroupRequest> requestlist;
	boolean hasmore;
	boolean isUpdateIng = false;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, GroupRequestListActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouprequestlist);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		registerMessageReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mMessageReceiver);
	}

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!CommonTextUtils.isEmpty(extras)) {
					try {
						JSONObject extraJson = new JSONObject(extras);
						if (null != extraJson && extraJson.length() > 0) {
							String msgtype = extraJson.getString("MessageType");
							if (msgtype.equals("ActivityAdd")) {
								pageindex = 1;
								sendMessage(YSMSG.REQ_GETADDACTIVITY, pageindex, 0, null);
								PreferencesUtils.setIsHaveRequestMsgs(false);
							}
						}
					} catch (JSONException e) {

					}
				}
			}
		}
	}

	private void initView() {
		initTitleBar();
		xlistView = (SwipeMenuListView) findViewById(R.id.grouprequestList);
		adapter = new GroupRequestListAdapter(mActivity);
		adapter.setRquestClick(this);
		xlistView.setAdapter(adapter);
		emptyview = findViewById(R.id.grouprequestemptyLL);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				deleteItem.setBackground(R.drawable.delete_menubg);
				deleteItem.setWidth(getWidth());
				menu.addMenuItem(deleteItem);
			}
		};
		xlistView.setMenuCreator(creator);
		xlistView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (adapter != null) {
					GroupRequest groupRequest = adapter.getItem(position);
					curObj = groupRequest;
					sendMessage(YSMSG.REQ_NOTICEREMOVE, 0, 0, groupRequest.getNoticeMsgId());
				}
				return false;
			}
		});
		xlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (adapter != null) {
					GroupRequest groupRequest = adapter.getItem(position);
					Date endDate = CommonUtils.getDate(groupRequest.getEndTime());
					Date nowDate = new Date();
					if (endDate.after(nowDate)) {
						GroupRequestActivity.startActivity(mActivity, groupRequest);
					}
				}
			}
		});
		requestlist = new ArrayList<GroupRequest>();

		xlistView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {

					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				LogUtils.i("firstVisibleItem", firstVisibleItem + "");
				LogUtils.i("visibleItemCount", visibleItemCount + "");
				LogUtils.i("totalItemCount", totalItemCount + "");
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (hasmore && !isUpdateIng) {
						isUpdateIng = true;
						sendMessage(YSMSG.REQ_GETADDACTIVITY, pageindex, 0, null);
					}
				}
			}
		});
	}

	private int getWidth() {
		int width = CommonUtils.getScreenWidth(mActivity) * 1778 / 10000;
		return width;
	}
	int pageindex = 1;
	private void initData() {
		showWaitingDialog();
		sendMessage(YSMSG.REQ_GETADDACTIVITY, pageindex, 0, null);
		if (PreferencesUtils.getIsHaveRequestMsgs()) {
			PreferencesUtils.setIsHaveRequestMsgs(false);
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETADDACTIVITY : {
				xlistView.setEmptyView(emptyview);
				dismissWaitingDialog();
				isUpdateIng = false;
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							// StringBuilder sb = new StringBuilder();
							int size = array.length();
							if (pageindex == 1) {
								requestlist.clear();
//								if (size > 0) {
//									xlistView.setVisibility(View.VISIBLE);
//									emptyview.setVisibility(View.GONE);
//								} else {
//									xlistView.setVisibility(View.GONE);
//									emptyview.setVisibility(View.VISIBLE);
//								}
							}
							hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								pageindex++;
							}
							for (int i = 0; i < size; i++) {
								GroupRequest requestObj = OJMFactory.createOJM().fromJson(array.getString(i),
										GroupRequest.class);
								AccountDetailObject account = OJMFactory.createOJM().fromJson(
										array.getJSONObject(i).getString("Member"), AccountDetailObject.class);
								requestObj.setAccount(account);
								requestlist.add(requestObj);
							}
							updateList();
							// sendMessage(YSMSG.REQ_SETNOTICEREAD, 0, 0,
							// sb.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {

				}
			}
				break;
			case YSMSG.RESP_SETNOTICEREAD :
				if (msg.arg1 == 200) {

				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}

				break;
			case YSMSG.RESP_CONFIRMJOIN :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (curObj != null) {
						// requestDao.delGroupRequestByid(curObj.getRequestID());
						curObj.setIsJoin(true);
						updateList();
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_NOTICEREMOVE :
				if (msg.arg1 == 200) {
					if (curObj != null) {
						YSToast.showToast(mActivity, getString(R.string.msg_delete_success));
						requestlist.remove(curObj);
						updateList();
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_NOTICEREMOVEALL :
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.msg_clear_success));
					requestlist.clear();
					updateList();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	ReportOrDeletePopupWindow reportOrDelWindow;

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.grouprequestlist_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GroupRequestListActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setText(getString(R.string.comment_clear));
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportOrDelWindow = new ReportOrDeletePopupWindow(mActivity, reportOrdelOnClick);
				reportOrDelWindow.setText(getString(R.string.clearnews_content));
				reportOrDelWindow.showAtLocation(xlistView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
	}

	private OnClickListener reportOrdelOnClick = new OnClickListener() {

		public void onClick(View v) {
			reportOrDelWindow.dismiss();
			switch (v.getId()) {
				case R.id.reportOrDeleteBtn :
					sendMessage(YSMSG.REQ_NOTICEREMOVEALL, 0, 0, "3");
					break;
				default :
					break;
			}
		}
	};

	private void updateList() {

		if (requestlist != null) {
			adapter.updateList(requestlist);
			if (requestlist.size() == 0) {
				mTitleBar.mRightTv.setVisibility(View.GONE);
			} else {
				mTitleBar.mRightTv.setVisibility(View.VISIBLE);
			}
		}
	}

	private static final Comparator<GroupRequest> COMPARATOR = new Comparator<GroupRequest>() {
		public int compare(GroupRequest o1, GroupRequest o2) {
			Date date1 = CommonUtils.getCreateDate(o1.getCreateTime());
			Date date2 = CommonUtils.getCreateDate(o2.getCreateTime());
			return date2.compareTo(date1);
		}
	};

	@Override
	public void acceptClick(GroupRequest groupRequest) {
		curObj = groupRequest;
		ConfirmJoinObject joinObj = new ConfirmJoinObject();
		joinObj.setAccept(true);
		joinObj.setActivityId(groupRequest.getActivityId());
		joinObj.setRequestId(groupRequest.getRequestId());
		sendMessage(YSMSG.REQ_CONFIRMJOIN, 0, 0, joinObj);
		showWaitingDialog();

	}

	@Override
	public void deleteClick(GroupRequest groupRequest) {

	}

	@Override
	public void mainClick(GroupRequest groupRequest) {

	}
}
