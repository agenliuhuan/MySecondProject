package mobi.jzcx.android.chongmi.ui.main.serve;

import java.io.File;
import java.util.List;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.dao.ImContactDao;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.sdk.im.controller.EaseUI;
import mobi.jzcx.android.chongmi.sdk.im.mode.EaseEmojicon;
import mobi.jzcx.android.chongmi.sdk.im.mode.EmojiconExampleGroupData;
import mobi.jzcx.android.chongmi.sdk.im.ui.EaseGroupRemoveListener;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseCommonUtils;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseConstant;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseImageUtils;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseChatExtendMenu.EaseChatExtendMenuItemClickListener;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseChatInputMenu;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseChatInputMenu.ChatInputMenuListener;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseChatMessageList;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseVoiceRecorderView;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import mobi.jzcx.android.chongmi.sdk.im.view.chatrow.EaseCustomChatRowProvider;
import mobi.jzcx.android.chongmi.sdk.im.view.emojicon.EaseEmojiconMenu;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.umeng.analytics.MobclickAgent;

public class ChatActivity extends BaseExActivity implements EMEventListener {
	protected TitleBarHolder mTitleBar = null;
	protected static final String TAG = "ChatActivity";
	protected static final int REQUEST_CODE_MAP = 1;
	protected static final int REQUEST_CODE_CAMERA = 2;
	protected static final int REQUEST_CODE_LOCAL = 3;

	ImContactDao contactDao = new ImContactDao();

	protected Bundle fragmentArgs;
	protected int chatType;
	protected String toChatUsername;
	protected String groupName;
	protected String groupAvatar;

	protected EaseChatMessageList messageList;
	protected EaseChatInputMenu inputMenu;

	protected EMConversation conversation;

	protected InputMethodManager inputManager;
	protected ClipboardManager clipboard;

	protected Handler handler = new Handler();
	protected File cameraFile;
	protected EaseVoiceRecorderView voiceRecorderView;
	protected SwipeRefreshLayout swipeRefreshLayout;
	protected ListView listView;

	protected boolean isloading;
	protected boolean haveMoreData = true;
	protected int pagesize = 20;
	protected GroupListener groupListener;
	protected EMMessage contextMenuMessage;

	static final int ITEM_TAKE_PICTURE = 1;
	static final int ITEM_PICTURE = 2;
	// static final int ITEM_FILE = 3;

	protected int[] itemStrings = {R.string.attach_take_pic, R.string.attach_picture};
	protected int[] itemdrawables = {R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector};
	protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE};
	private EMChatRoomChangeListener chatRoomChangeListener;
	private boolean isMessageListInited;
	protected MyItemClickListener extendMenuItemClickListener;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ease_fragment_chat);
		chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
		// 聊天人或群id
		toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_IMID);
		groupName = getIntent().getExtras().getString(EaseConstant.EXTRA_GROUP_NAME);
		groupAvatar = getIntent().getExtras().getString(EaseConstant.EXTRA_GROUP_AVATAR);
		initView();
		setUpView();
		mSetStatusBar = true;
	}

	/**
	 * init view
	 */
	protected void initView() {
		// 按住说话录音控件
		voiceRecorderView = (EaseVoiceRecorderView) findViewById(R.id.voice_recorder);

		// 消息列表layout
		messageList = (EaseChatMessageList) findViewById(R.id.message_list);
		if (chatType != EaseConstant.CHATTYPE_SINGLE)
			messageList.setShowUserNick(true);
		listView = messageList.getListView();

		extendMenuItemClickListener = new MyItemClickListener();
		inputMenu = (EaseChatInputMenu) findViewById(R.id.input_menu);
		registerExtendMenuItem();
		// init input menu
		inputMenu.init(null);
		inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

			@Override
			public void onSendMessage(String content) {
				sendTextMessage(content);
			}

			@Override
			public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
				return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

					@Override
					public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
						sendVoiceMessage(voiceFilePath, voiceTimeLength);
					}
				});
			}

			@Override
			public void onBigExpressionClicked(EaseEmojicon emojicon) {
				// 发送大表情(动态表情)
				sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
			}
		});

		swipeRefreshLayout = messageList.getSwipeRefreshLayout();
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);

		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 设置属性，监听等
	 */
	ImContactObject contact;

	protected void setUpView() {
		initTitleBar();
		contact = contactDao.getImContactById(toChatUsername);
		if (contact != null && !CommonTextUtils.isEmpty(contact.getContactName())) {
			mTitleBar.mTitle.setText(contact.getContactName());
		} else {
			mTitleBar.mTitle.setText(toChatUsername);
		}

		if (chatType == EaseConstant.CHATTYPE_SINGLE) { // 单聊
			mTitleBar.mRightImg.setVisibility(View.GONE);
		} else {
			mTitleBar.mRightImg.setImageResource(R.drawable.chatting_user);
			if (chatType == EaseConstant.CHATTYPE_GROUP) {
				sendMessage(YSMSG.REQ_GETACTIVITYBYIMID, 0, 0, toChatUsername);
				groupListener = new GroupListener();
				EMGroupManager.getInstance().addGroupChangeListener(groupListener);
			} else {
				onChatRoomViewCreation();
			}
		}

		if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
			onConversationInit();
			onMessageListInit();
		}

		setRefreshLayoutListener();
		((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETACTIVITYBYIMID :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							ActivityObject activeObj = OJMFactory.createOJM().fromJson(json.getString("Activity"),
									ActivityObject.class);
							if (contact != null && !CommonTextUtils.isEmpty(contact.getContactName())) {
								if (activeObj != null) {
									mTitleBar.mTitle.setText(contact.getContactName() + "("
											+ activeObj.getMemberCount() + ")");
								} else {
									mTitleBar.mTitle.setText(contact.getContactName() + "(" + 0 + ")");
								}
							} else {
								if (activeObj != null) {
									mTitleBar.mTitle.setText(toChatUsername + "(" + activeObj.getMemberCount() + ")");
								} else {
									mTitleBar.mTitle.setText(toChatUsername + "(" + 0 + ")");
								}
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

				}
				break;
		}
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChatActivity.this.finish();
			}
		});
		mTitleBar.mRightImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GroupInfoActivity.startActivity(mActivity, toChatUsername);
			}
		});
	}

	/**
	 * 注册底部菜单扩展栏item; 覆盖此方法时如果不覆盖已有item，item的id需大于3
	 */
	protected void registerExtendMenuItem() {
		for (int i = 0; i < itemStrings.length; i++) {
			inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
		}
	}

	protected void onConversationInit() {
		// 获取当前conversation对象
		conversation = EMChatManager.getInstance().getConversation(toChatUsername);
		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();
		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			if (chatType == EaseConstant.CHATTYPE_SINGLE) {
				conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
			} else {
				conversation.loadMoreGroupMsgFromDB(msgId, pagesize - msgCount);
			}
		}

	}

	protected void onMessageListInit() {
		messageList.init(toChatUsername, chatType,
				chatFragmentListener != null ? chatFragmentListener.onSetCustomChatRowProvider() : null);
		// 设置list item里的控件的点击事件
		setListItemClickListener();

		messageList.getListView().setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				inputMenu.hideExtendMenuContainer();
				return false;
			}
		});

		isMessageListInited = true;
	}

	protected void setListItemClickListener() {
		messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
			@Override
			public void onUserAvatarClick(String username) {
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null && username.equals(myself.getIMusername())) {
					AccountCenterActivity.startActivity(mActivity, myself.getMemberId());
				} else {
					ImContactObject obj = contactDao.getImContactById(username);
					if (obj != null) {
						AccountCenterActivity.startActivity(mActivity, obj.getMeberId());
					}
				}
			}

			@Override
			public void onResendClick(final EMMessage message) {

				DialogHelper.showTwoDialog(ChatActivity.this, false, getString(R.string.confirm_resend),
						getString(R.string.cancel), getString(R.string.ok), true, null, new OnClickListener() {

							@Override
							public void onClick(View v) {

								resendMessage(message);

							}
						});
			}

			@Override
			public void onBubbleLongClick(EMMessage message) {
				contextMenuMessage = message;
				if (chatFragmentListener != null) {
					chatFragmentListener.onMessageBubbleLongClick(message);
				}
			}

			@Override
			public boolean onBubbleClick(EMMessage message) {
				if (chatFragmentListener != null) {
					return chatFragmentListener.onMessageBubbleClick(message);
				}
				return false;
			}
		});
	}
	protected void setRefreshLayoutListener() {
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
							List<EMMessage> messages;
							try {
								if (chatType == EaseConstant.CHATTYPE_SINGLE) {
									messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
											pagesize);
								} else {
									messages = conversation.loadMoreGroupMsgFromDB(messageList.getItem(0).getMsgId(),
											pagesize);
								}
							} catch (Exception e1) {
								swipeRefreshLayout.setRefreshing(false);
								return;
							}
							if (messages.size() > 0) {
								messageList.refreshSeekTo(messages.size() - 1);
								if (messages.size() != pagesize) {
									haveMoreData = false;
								}
							} else {
								haveMoreData = false;
							}

							isloading = false;

						} else {
							YSToast.showToast(ChatActivity.this, R.string.no_more_messages);
						}
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 600);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					sendImageMessage(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					sendLocationMessage(latitude, longitude, locationAddress);
				} else {
					YSToast.showToast(ChatActivity.this, R.string.unable_to_get_loaction);
				}

			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_10));
		if (isMessageListInited)
			messageList.refresh();
		EaseUI.getInstance().pushActivity(ChatActivity.this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck});
	}

	@Override
	public void onStop() {
		super.onStop();
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

		// 把此activity 从foreground activity 列表里移除
		EaseUI.getInstance().popActivity(ChatActivity.this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (groupListener != null) {
			EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
		}
		if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
			EMChatManager.getInstance().leaveChatRoom(toChatUsername);
		}

		if (chatRoomChangeListener != null) {
			EMChatManager.getInstance().removeChatRoomChangeListener(chatRoomChangeListener);
		}
	}

	/**
	 * 事件监听,registerEventListener后的回调事件
	 * 
	 * see {@link EMNotifierEvent}
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
			case EventNewMessage :
				// 获取到message
				EMMessage message = (EMMessage) event.getData();

				if (contactDao == null) {
					contactDao = new ImContactDao();
				}
				try {
					if (message.getChatType().equals(ChatType.GroupChat)) {
						ImContactObject group = new ImContactObject();
						String groupName = message.getStringAttribute(EaseConstant.EXTRA_GROUP_NAME);
						String groupAvatar = message.getStringAttribute(EaseConstant.EXTRA_GROUP_AVATAR);
						group.setContactId(message.getTo());
						group.setContactName(groupName);
						group.setContactAvatar(groupAvatar);
						group.setGroup(true);
						contactDao.update(group);
					}
					ImContactObject user = new ImContactObject();
					String memberId = message.getStringAttribute(EaseConstant.EXTRA_MEMBER_ID);
					String memberName = message.getStringAttribute(EaseConstant.EXTRA_MEMBER_NAME);
					String memberAvatar = message.getStringAttribute(EaseConstant.EXTRA_MEMBER_AVATAR);
					user.setContactId(message.getFrom());
					user.setContactName(memberName);
					user.setContactAvatar(memberAvatar);
					user.setMeberId(memberId);
					user.setGroup(false);
					contactDao.update(user);
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = App.getInstance().obtainMessage();
				msg.what = IMInitHelper.MSG_REFRESH;
				msg.obj = message;
				CoreModel.getInstance().notifyOutboxHandlers(msg);

				String username = null;
				// 群组消息
				if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
					username = message.getTo();
				} else {
					// 单聊消息
					username = message.getFrom();
				}

				// 如果是当前会话的消息，刷新聊天页面
				if (username.equals(toChatUsername)) {
					messageList.refreshSelectLast();
					// 声音和震动提示有新消息
					EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
				} else {
					// 如果消息不是和当前聊天ID的消息
					EaseUI.getInstance().getNotifier().onNewMsg(message);
				}

				break;
			case EventDeliveryAck :
			case EventReadAck :
				// 获取到message
				messageList.refresh();
				break;
			case EventOfflineMessage :
				// a list of offline messages
				// List<EMMessage> offlineMessages = (List<EMMessage>)
				// event.getData();
				messageList.refresh();
				break;
			default :
				break;
		}

	}

	public void onBackPressed() {
		if (inputMenu.onBackPressed()) {
			ChatActivity.this.finish();
			if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
				EMChatManager.getInstance().leaveChatRoom(toChatUsername);
			}
		}
	}

	protected void onChatRoomViewCreation() {
		final ProgressDialog pd = ProgressDialog.show(ChatActivity.this, "", "Joining......");
		EMChatManager.getInstance().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

			@Override
			public void onSuccess(final EMChatRoom value) {
				ChatActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (ChatActivity.this.isFinishing() || !toChatUsername.equals(value.getUsername()))
							return;
						pd.dismiss();
						EMChatRoom room = EMChatManager.getInstance().getChatRoom(toChatUsername);
						if (room != null) {
							mTitleBar.mTitle.setText(room.getName());
						} else {
							mTitleBar.mTitle.setText(toChatUsername);
						}
						EMLog.d(TAG, "join room success : " + room.getName());
						addChatRoomChangeListenr();
						onConversationInit();
						onMessageListInit();
					}
				});
			}

			@Override
			public void onError(final int error, String errorMsg) {
				// TODO Auto-generated method stub
				EMLog.d(TAG, "join room failure : " + error);
				ChatActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
					}
				});
				ChatActivity.this.finish();
			}
		});
	}

	protected void addChatRoomChangeListenr() {
		chatRoomChangeListener = new EMChatRoomChangeListener() {

			@Override
			public void onChatRoomDestroyed(String roomId, String roomName) {
				if (roomId.equals(toChatUsername)) {
					showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
					ChatActivity.this.finish();
				}
			}

			@Override
			public void onMemberJoined(String roomId, String participant) {
				showChatroomToast("member : " + participant + " join the room : " + roomId);
			}

			@Override
			public void onMemberExited(String roomId, String roomName, String participant) {
				showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : "
						+ roomName);
			}

			@Override
			public void onMemberKicked(String roomId, String roomName, String participant) {
				if (roomId.equals(toChatUsername)) {
					String curUser = EMChatManager.getInstance().getCurrentUser();
					if (curUser.equals(participant)) {
						EMChatManager.getInstance().leaveChatRoom(toChatUsername);
						ChatActivity.this.finish();
					} else {
						showChatroomToast("member : " + participant + " was kicked from the room : " + roomId
								+ " room name : " + roomName);
					}
				}
			}

		};

		EMChatManager.getInstance().addChatRoomChangeListener(chatRoomChangeListener);
	}

	protected void showChatroomToast(final String toastContent) {
		ChatActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				YSToast.showToast(ChatActivity.this, toastContent);
			}
		});
	}

	/**
	 * 扩展菜单栏item点击事件
	 * 
	 */
	class MyItemClickListener implements EaseChatExtendMenuItemClickListener {

		@Override
		public void onClick(int itemId, View view) {
			if (chatFragmentListener != null) {
				if (chatFragmentListener.onExtendMenuItemClick(itemId, view)) {
					return;
				}
			}
			switch (itemId) {
				case ITEM_TAKE_PICTURE : // 拍照
					selectPicFromCamera();
					break;
				case ITEM_PICTURE :
					selectPicFromLocal(); // 图库选择图片
					break;
				// case ITEM_FILE : // 位置
				// // startActivityForResult(new Intent(ChatActivity.this,
				// // EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
				// break;

				default :
					break;
			}
		}

	}

	// 发送消息方法
	// ==========================================================================
	protected void sendTextMessage(String content) {
		EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
		sendMessage(message);
	}

	protected void sendBigExpressionMessage(String name, String identityCode) {
		EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
		sendMessage(message);
	}

	protected void sendVoiceMessage(String filePath, int length) {
		EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
		sendMessage(message);
	}

	protected void sendImageMessage(String imagePath) {
		EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
		sendMessage(message);
	}

	protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
		EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
		sendMessage(message);
	}

	protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
		EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
		sendMessage(message);
	}

	protected void sendFileMessage(String filePath) {
		EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
		sendMessage(message);
	}

	protected void sendMessage(EMMessage message) {
		if (chatFragmentListener != null) {
			// 设置扩展属性
			chatFragmentListener.onSetMessageAttributes(message);
		}
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == EaseConstant.CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
			if (!CommonTextUtils.isEmpty(groupName)) {
				message.setAttribute(EaseConstant.EXTRA_GROUP_NAME, groupName);
			}
			if (!CommonTextUtils.isEmpty(groupAvatar)) {
				message.setAttribute(EaseConstant.EXTRA_GROUP_AVATAR, groupAvatar);
			}
		} else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			message.setAttribute(EaseConstant.EXTRA_MEMBER_ID, myself.getMemberId());
			message.setAttribute(EaseConstant.EXTRA_MEMBER_NAME, myself.getNickName());
			message.setAttribute(EaseConstant.EXTRA_MEMBER_AVATAR, myself.getIcoUrl());
		}

		// 发送消息
		EMChatManager.getInstance().sendMessage(message, null);
		// 刷新ui
		messageList.refreshSelectLast();
	}

	public void resendMessage(EMMessage message) {
		message.status = EMMessage.Status.CREATE;
		EMChatManager.getInstance().sendMessage(message, null);
		messageList.refresh();
	}

	// ===================================================================================

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	protected void sendPicByUri(Uri selectedImage) {
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
		Cursor cursor = ChatActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				YSToast.showToast(ChatActivity.this, R.string.cant_find_pictures);
				return;
			}
			sendImageMessage(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				YSToast.showToast(ChatActivity.this, R.string.cant_find_pictures);
				return;

			}
			sendImageMessage(file.getAbsolutePath());
		}

	}

	/**
	 * 根据uri发送文件
	 * 
	 * @param uri
	 */
	protected void sendFileByUri(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = null;

			try {
				cursor = ChatActivity.this.getContentResolver().query(uri, filePathColumn, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			YSToast.showToast(ChatActivity.this, R.string.File_does_not_exist);
			return;
		}
		// 大于10M不让发送
		if (file.length() > 10 * 1024 * 1024) {
			YSToast.showToast(ChatActivity.this, R.string.The_file_is_not_greater_than_10_m);
			return;
		}
		sendFileMessage(filePath);
	}

	/**
	 * 照相获取图片
	 */
	protected void selectPicFromCamera() {
		if (!EaseCommonUtils.isExitsSdcard()) {
			YSToast.showToast(ChatActivity.this, R.string.sd_card_does_not_exist);
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), EMChatManager.getInstance().getCurrentUser()
				+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 从图库获取图片
	 */
	protected void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 点击清空聊天记录
	 * 
	 */
	protected void emptyHistory() {
		String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
		DialogHelper.showTwoDialog(ChatActivity.this, false, msg, getString(R.string.cancel), getString(R.string.ok),
				true, null, new OnClickListener() {
					@Override
					public void onClick(View v) {
						EMChatManager.getInstance().clearConversation(toChatUsername);
						messageList.refresh();

					}
				});

	}
	/**
	 * 点击进入群组详情
	 * 
	 */
	protected void toGroupDetails() {
		if (chatType == EaseConstant.CHATTYPE_GROUP) {
			EMGroup group = EMGroupManager.getInstance().getGroup(toChatUsername);
			if (group == null) {
				YSToast.showToast(ChatActivity.this, R.string.gorup_not_found);
				return;
			}
			if (chatFragmentListener != null) {
				chatFragmentListener.onEnterToChatDetails();
			}
		} else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
			if (chatFragmentListener != null) {
				chatFragmentListener.onEnterToChatDetails();
			}
		}
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideKeyboard() {
		if (ChatActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (ChatActivity.this.getCurrentFocus() != null)
				inputManager.hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 转发消息
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
			case TXT :
				if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
					sendBigExpressionMessage(((TextMessageBody) forward_msg.getBody()).getMessage(),
							forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
				} else {
					// 获取消息内容，发送消息
					String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
					sendTextMessage(content);
				}
				break;
			case IMAGE :
				// 发送图片
				String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
				if (filePath != null) {
					File file = new File(filePath);
					if (!file.exists()) {
						// 不存在大图发送缩略图
						filePath = EaseImageUtils.getThumbnailImagePath(filePath);
					}
					sendImageMessage(filePath);
				}
				break;
			default :
				break;
		}

		if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}

	/**
	 * 监测群组解散或者被T事件
	 * 
	 */
	class GroupListener extends EaseGroupRemoveListener {

		@Override
		public void onUserRemoved(final String groupId, String groupName) {
			ChatActivity.this.runOnUiThread(new Runnable() {

				public void run() {
					if (toChatUsername.equals(groupId)) {
						YSToast.showToast(ChatActivity.this, R.string.you_are_group);
						ChatActivity.this.finish();
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, String groupName) {
			// 群组解散正好在此页面，提示群组被解散，并finish此页面
			ChatActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					if (toChatUsername.equals(groupId)) {
						YSToast.showToast(ChatActivity.this, R.string.the_current_group);
						ChatActivity.this.finish();
					}
				}
			});
		}

	}

	protected EaseChatFragmentListener chatFragmentListener;
	public void setChatFragmentListener(EaseChatFragmentListener chatFragmentListener) {
		this.chatFragmentListener = chatFragmentListener;
	}

	public interface EaseChatFragmentListener {
		/**
		 * 设置消息扩展属性
		 */
		void onSetMessageAttributes(EMMessage message);

		/**
		 * 进入会话详情
		 */
		void onEnterToChatDetails();

		/**
		 * 用户头像点击事件
		 * 
		 * @param username
		 */
		void onAvatarClick(String username);

		/**
		 * 消息气泡框点击事件
		 */
		boolean onMessageBubbleClick(EMMessage message);

		/**
		 * 消息气泡框长按事件
		 */
		void onMessageBubbleLongClick(EMMessage message);

		/**
		 * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
		 * 
		 * @param view
		 * @param itemId
		 * @return
		 */
		boolean onExtendMenuItemClick(int itemId, View view);

		/**
		 * 设置自定义chatrow提供者
		 * 
		 * @return
		 */
		EaseCustomChatRowProvider onSetCustomChatRowProvider();
	}
}
