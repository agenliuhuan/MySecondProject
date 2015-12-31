package mobi.jzcx.android.chongmi.ui.main.serve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.CheckCommentObject;
import mobi.jzcx.android.chongmi.biz.vo.CommontObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.NoticeObject;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;
import mobi.jzcx.android.chongmi.ui.adapter.CommentAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetDiaryDetailsActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportClickListener;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CommentActivity extends BaseExActivity implements ReportClickListener {
	protected TitleBarHolder mTitleBar = null;
	private SwipeMenuListView xlistView;
	private View emptyview;
	ArrayList<NoticeObject> noticeList;
	CommentAdapter adapter;

	NoticeObject curCommentObj;
	boolean hasmore;
	boolean isUpdateIng = false;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, CommentActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
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
							if (msgtype.equals("TextComment") || msgtype.equals("VoiceComment")
									|| msgtype.equals("Like")) {
								pageindex = 1;
								sendMessage(YSMSG.REQ_GETCommentNOTICE, pageindex, 0, null);
								PreferencesUtils.setIsHaveComments(false);
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
		xlistView = (SwipeMenuListView) findViewById(R.id.commentXList);
		adapter = new CommentAdapter(mActivity);
		adapter.setReportClick(this);
		xlistView.setAdapter(adapter);
		emptyview = findViewById(R.id.commentemptyLL);
		noticeList = new ArrayList<NoticeObject>();

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
					NoticeObject noticeObj = adapter.getItem(position);
					sendMessage(YSMSG.REQ_NOTICEREMOVE, 0, 0, noticeObj.getNoticeMsgId());
					curCommentObj = noticeObj;
				}
				return false;
			}
		});
		xlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (adapter != null) {
					NoticeObject commentObj = adapter.getItem(position);
					curCommentObj = commentObj;
					if (curCommentObj.getNoticeMsgType() == 4) {
						sendMessage(YSMSG.REQ_CHECKMICROBLOGEXISTS, 0, 0, commentObj.getMicroblogId());
					} else {
						CheckCommentObject checkObj = new CheckCommentObject();
						checkObj.setCommentid(commentObj.getCommentId());
						checkObj.setMicroblogid(commentObj.getMicroblogId());
						sendMessage(YSMSG.REQ_CHECKCOMMENTEXISTS, 0, 0, checkObj);
					}
				}

			}
		});
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
						sendMessage(YSMSG.REQ_GETCommentNOTICE, pageindex, 0, null);
					}
				}
			}
		});
	}
	private int getWidth() {
		int width = CommonUtils.getScreenWidth(mActivity) * 2264 / 10000;
		return width;
	}

	int pageindex = 1;

	private void initData() {
		showWaitingDialog();
		sendMessage(YSMSG.REQ_GETCommentNOTICE, pageindex, 0, null);
		if (PreferencesUtils.getIsHaveComments()) {
			PreferencesUtils.setIsHaveComments(false);
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETCommentNOTICE :
				xlistView.setEmptyView(emptyview);
				dismissWaitingDialog();
				isUpdateIng = false;
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {

							JSONObject json = new JSONObject(result);

							JSONArray array = json.getJSONArray("List");
							if (pageindex == 1) {
								noticeList.clear();
							}
							hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								pageindex++;
							}
							StringBuilder sb = new StringBuilder();
							int size = array.length();
							for (int i = 0; i < size; i++) {
								NoticeObject noticeObj = OJMFactory.createOJM().fromJson(array.getString(i),
										NoticeObject.class);
								AccountObject account = OJMFactory.createOJM().fromJson(
										array.getJSONObject(i).getString("Member"), AccountObject.class);
								JSONObject extras = array.getJSONObject(i).getJSONObject("ExtrasData");
								noticeObj.setAccount(account);
								noticeObj.setMicroblogId(extras.getString("MicroblogId"));
								noticeObj.setCommentId(extras.getString("CommentId"));
								noticeObj.setMemberId(extras.getString("MemberId"));
								noticeObj.setDetailImg(extras.getString("Photo"));
								if (i == size - 1) {
									sb.append(noticeObj.getNoticeMsgId());
								} else {
									sb.append(noticeObj.getNoticeMsgId() + ",");
								}
								noticeList.add(noticeObj);
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
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_SETNOTICEREAD :
				if (msg.arg1 == 200) {

				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}

				break;
			case YSMSG.RESP_CHECKMICROBLOGEXISTS :
				if (msg.arg1 == 200) {
					DynamicObject obj = new DynamicObject();
					obj.setMicroblogId(curCommentObj.getMicroblogId());
					PetDiaryDetailsActivity.startActivity(mActivity, obj, false, null);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_CHECKCOMMENTEXISTS :
				if (msg.arg1 == 200) {
					DynamicObject obj = new DynamicObject();
					obj.setMicroblogId(curCommentObj.getMicroblogId());
					PetDiaryDetailsActivity.startActivity(mActivity, obj, true, curCommentObj);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_NOTICEREMOVE :
				if (msg.arg1 == 200) {
					if (curCommentObj != null) {
						YSToast.showToast(mActivity, getString(R.string.msg_delete_success));
						noticeList.remove(curCommentObj);
						updateList();
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_NOTICEREMOVEALL :
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.msg_clear_success));
					noticeList.clear();
					updateList();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	private void updateList() {
		// List<NoticeObject> list = commentDao.findAll();
		// Collections.sort(list, COMPARATOR);
		if (noticeList != null) {
			adapter.updateList(noticeList);
			if (noticeList.size() == 0) {
				mTitleBar.mRightTv.setVisibility(View.GONE);
			} else {
				mTitleBar.mRightTv.setVisibility(View.VISIBLE);
			}
		}
	}

	private static final Comparator<NoticeObject> COMPARATOR = new Comparator<NoticeObject>() {
		public int compare(NoticeObject o1, NoticeObject o2) {
			Date date1 = CommonUtils.getCreateDate(o1.getCreateTime());
			Date date2 = CommonUtils.getCreateDate(o2.getCreateTime());
			return date2.compareTo(date1);
		}
	};

	ReportOrDeletePopupWindow reportOrDelWindow;

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.comment_title));
		mTitleBar.mRightTv.setText(getString(R.string.comment_clear));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CommentActivity.this.finish();
			}
		});

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
					sendMessage(YSMSG.REQ_NOTICEREMOVEALL, 0, 0, "0,1,4");
					break;
				default :
					break;
			}
		}
	};

	@Override
	public void reportClick(CommontObject commentObj) {

	}

	@Override
	public void mainClick(CommontObject commentObj) {

	}

	@Override
	public void systemNewsMainClick(SystemNoticeObject noticeObj) {

	}

	@Override
	public void systemNewsDelClick(SystemNoticeObject noticeObj) {

	}

	@Override
	public void commentMainClick(NoticeObject commentObj) {

	}

	@Override
	public void commentDelClick(NoticeObject commentObj) {
		// commentDao.delete(commentObj);
		updateList();
	}

}
