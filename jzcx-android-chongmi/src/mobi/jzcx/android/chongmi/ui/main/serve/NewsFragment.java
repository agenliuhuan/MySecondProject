package mobi.jzcx.android.chongmi.ui.main.serve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.db.dao.ImContactDao;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseConstant;
import mobi.jzcx.android.chongmi.ui.adapter.ConversionAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExFragment;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.chongmi.view.swipe.SwipeOnTouchListener;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenu;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuCreator;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuItem;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;

public class NewsFragment extends BaseExFragment implements OnClickListener {
	ImContactDao contactDao = new ImContactDao();
	View rootView;
	protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
	SwipeMenuListView listView;
	protected boolean hidden;
	protected boolean isConflict;
	ImageView commentHaveImg;
	ImageView systemHaveImg;
	ImageView requestHaveImg;
	ConversionAdapter mAdapter;

	static NewsFragment fragment;

	OnUpdateMsgUnreadCountsListener mAttachListener;

	public static NewsFragment getInstance() {
		if (fragment == null) {
			fragment = new NewsFragment();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_news, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden && !isConflict) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i("NewsFragment", "onResume");
		if (!hidden) {
			refresh();
		}

		registerMessageReceiver();

		if (PreferencesUtils.getIsHaveComments()) {
			commentHaveImg.setVisibility(View.VISIBLE);
		} else {
			commentHaveImg.setVisibility(View.GONE);
		}
		if (PreferencesUtils.getIsHaveSysMsgs()) {
			systemHaveImg.setVisibility(View.VISIBLE);
		} else {
			systemHaveImg.setVisibility(View.GONE);
		}
		if (PreferencesUtils.getIsHaveRequestMsgs()) {
			requestHaveImg.setVisibility(View.VISIBLE);
		} else {
			requestHaveImg.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mAttachListener = (OnUpdateMsgUnreadCountsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnUpdateMsgUnreadCountsListener");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.i("NewsFragment", "onPause");
		// if (messageChange != null) {
		// IMessageSqlManager.unregisterMsgObserver(messageChange);
		// }
		if (mMessageReceiver != null) {
			getActivity().unregisterReceiver(mMessageReceiver);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

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
		getActivity().registerReceiver(mMessageReceiver, filter);
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
								PreferencesUtils.setIsHaveComments(true);
								commentHaveImg.setVisibility(View.VISIBLE);
							}
							if (msgtype.equals("System")) {
								PreferencesUtils.setIsHaveSysMsgs(true);
								systemHaveImg.setVisibility(View.VISIBLE);
							}
							if (msgtype.equals("ActivityAdd")) {
								PreferencesUtils.setIsHaveRequestMsgs(true);
								requestHaveImg.setVisibility(View.VISIBLE);
							}
						}
					} catch (JSONException e) {

					}
				}
			}
		}
	}

	private void initView() {
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.newslistview_headerview, null);

		PercentRelativeLayout commentLL = (PercentRelativeLayout) headerView.findViewById(R.id.news_commentRL);
		PercentRelativeLayout systemLL = (PercentRelativeLayout) headerView.findViewById(R.id.news_systemRL);
		PercentRelativeLayout mygroupLL = (PercentRelativeLayout) headerView.findViewById(R.id.news_myGroupRL);
		PercentRelativeLayout requestRL = (PercentRelativeLayout) headerView.findViewById(R.id.news_requestRL);

		commentLL.setOnClickListener(this);
		systemLL.setOnClickListener(this);
		mygroupLL.setOnClickListener(this);
		requestRL.setOnClickListener(this);

		commentHaveImg = (ImageView) headerView.findViewById(R.id.news_havecomment_img);
		systemHaveImg = (ImageView) headerView.findViewById(R.id.news_havesystems_img);
		requestHaveImg = (ImageView) headerView.findViewById(R.id.news_haverequest_img);
		listView = (SwipeMenuListView) rootView.findViewById(R.id.news_chatting_lv);
		mAdapter = new ConversionAdapter(getActivity());
		listView.addHeaderView(headerView);
		listView.setAdapter(mAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				deleteItem.setBackground(R.drawable.delete_menubg);
				deleteItem.setWidth(getWidth());
				menu.addMenuItem(deleteItem);
			}
		};

		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				EMConversation conversation = conversationList.get(position);
				if (EMChatManager.getInstance().deleteConversation(conversation.getUserName(), conversation.isGroup(),
						true)) {
					YSToast.showToast(getActivity(), R.string.msg_delete_success);
					refresh();
				}
				return false;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = (EMConversation) parent.getAdapter().getItem(position);
				String username = conversation.getUserName();
				ImContactObject contact = contactDao.getImContactById(username);
				if (username.equals(EMChatManager.getInstance().getCurrentUser())) {
					// Toast.makeText(getActivity(),
					// R.string.Cant_chat_with_yourself, 0).show();
				} else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					if (conversation.isGroup()) {
						if (conversation.getType() == EMConversationType.ChatRoom) {
							// it's group chat
							intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
						} else {
							intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
							if (contact != null) {
								intent.putExtra(EaseConstant.EXTRA_GROUP_NAME, contact.getContactName());
								intent.putExtra(EaseConstant.EXTRA_GROUP_AVATAR, contact.getContactAvatar());
							}
						}
					} else {
						intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
					}
					// it's single chat
					intent.putExtra(EaseConstant.EXTRA_USER_IMID, username);

					startActivity(intent);
				}
			}
		});
	}

	private int getWidth() {
		int width = CommonUtils.getScreenWidth(getActivity()) * 1778 / 10000;
		return width;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.news_commentRL :
				CommentActivity.startActivity(getActivity());
				break;
			case R.id.news_systemRL :
				SysNewsActivity.startActivity(getActivity());
				break;
			case R.id.news_myGroupRL :
				MyGroupActivity.startActivity(getActivity());
				break;
			case R.id.news_requestRL :
				GroupRequestListActivity.startActivity(getActivity());
				break;
			default :
				break;
		}
	}

	public interface OnUpdateMsgUnreadCountsListener {
		void OnUpdateMsgUnreadCounts();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationList());
		mAdapter.updateData(conversationList);
		// setListViewHeightBasedOnChildren(conversationList.size());
		if (mAttachListener != null) {
			mAttachListener.OnUpdateMsgUnreadCounts();
		}
	}

	/**
	 * 获取会话列表
	 * 
	 * @param context
	 * @return +
	 */
	protected List<EMConversation> loadConversationList() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
							conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	private void setListViewHeightBasedOnChildren(int size) {
		int screensize = CommonUtils.getScreenWidth(getActivity());
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = 1778 * screensize / 10000 * size;
		listView.setLayoutParams(params);

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case IMInitHelper.MSG_REFRESH : {
				LogUtils.i("IMInitHelper", "MSG_REFRESH");
				refresh();
			}
				break;
		}

	}

}
