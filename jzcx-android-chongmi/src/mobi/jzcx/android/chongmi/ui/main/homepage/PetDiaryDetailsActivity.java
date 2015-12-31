package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.AddCommonObject;
import mobi.jzcx.android.chongmi.biz.vo.CommontObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMoreCommentsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.NoticeObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.RemoveCommentObject;
import mobi.jzcx.android.chongmi.biz.vo.ReportObject;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseChatPrimaryMenuBase.EaseChatPrimaryMenuListener;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseVoiceRecorderView;
import mobi.jzcx.android.chongmi.sdk.im.view.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import mobi.jzcx.android.chongmi.ui.adapter.DetailZanAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.HomePLDetailsCommentAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.HomeViewPagerAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.VoicePlayClickListener;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DecoratorViewPager;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.chongmi.view.CanPullScrollView;
import mobi.jzcx.android.chongmi.view.CommentListView;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.chongmi.view.ReportOrDeletePopupWindow;
import mobi.jzcx.android.chongmi.view.SelectPicPopupWindow;
import mobi.jzcx.android.chongmi.view.swipe.SwipeOnTouchListener;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.client.AsyncHttpClient;
import mobi.jzcx.android.core.net.http.handler.DownloadHttpResponseHandler;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

/**
 * 宠物日志详情
 */
@SuppressLint("NewApi")
public class PetDiaryDetailsActivity extends BaseExActivity implements OnClickListener, ReportClickListener {
	protected TitleBarHolder mTitleBar = null;
	boolean HasDelete = false;
	TextView HasDeleteTV;

	SimpleDraweeView userimg;
	TextView username;
	ImageView usersex;
	TextView distanceTv;
	ImageView followImage;

	DecoratorViewPager dynamicViewPager;
	TVPageIndicator dynamicIndicator;
	SimpleDraweeView dynamicPetImg;
	ImageView dynamicPetSexImg;
	TextView dynamicPetName;
	TextView dynamicPetType;
	TextView dynamicSendtime;
	TextView dynamicContent;
	TextView moreZanTv;
	PercentLinearLayout dynamicDianZanRL;
	ImageView zanImg;
	TextView zanTv;
	TextView commentNumTv;

	PercentLinearLayout shareMoreLL;
	HorizontalListView listZanUser;

	PercentRelativeLayout inputRL;
	PercentRelativeLayout replyRL;
	TextView replyNameTV;
	ImageView replyCloseImage;

	ListView listview;
	Button keyboardBtn;
	Button voiceBtn;
	Button voicespeakBtn;
	Button sendBtn;
	EditText sendText;

	DetailZanAdapter zanUserAdapter;
	ArrayList<CommontObject> commontObjList;
	HomePLDetailsCommentAdapter commentAdapter;

	PullToRefreshLayout refreshLayout;
	CanPullScrollView scrollview;
	static boolean showkeybord = false;
	static NoticeObject noticeObj;
	static DynamicObject dynamicObj;
	String commentId;

	public static void startActivity(Context context, DynamicObject dynamicObj, boolean showkeybord,
			NoticeObject curCommentObj) {
		ActivityUtils.startActivity(context, PetDiaryDetailsActivity.class);
		PetDiaryDetailsActivity.dynamicObj = dynamicObj;
		PetDiaryDetailsActivity.showkeybord = showkeybord;
		PetDiaryDetailsActivity.noticeObj = curCommentObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petlog_details);
		initView();
		mSetStatusBar = true;
		if (noticeObj != null) {
			replyRL.setVisibility(View.VISIBLE);
			replyNameTV.setText(noticeObj.getAccount().getNickName());
			commentId = noticeObj.getCommentId();
		}
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		commontObjList.clear();
		showWaitingDialog();
		HasDelete = false;
		sendMessage(YSMSG.REQ_GETBLOGBYID, 0, 0, dynamicObj.getMicroblogId());
		MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_12));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (VoicePlayClickListener.isPlaying) {
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}
	}
	int pageindex = 1;

	private void initView() {
		initTitleBar();
		initInput();
		this.HasDeleteTV = (TextView) findViewById(R.id.petdiarty_isDeleteTV);

		this.userimg = (SimpleDraweeView) findViewById(R.id.petdiarty_userimg);
		this.username = (TextView) findViewById(R.id.petdiarty_name);
		this.usersex = (ImageView) findViewById(R.id.petdiarty_sex);
		this.distanceTv = (TextView) findViewById(R.id.petdiarty_distance);
		this.followImage = (ImageView) findViewById(R.id.petdiarty_followbtn);

		this.dynamicIndicator = (TVPageIndicator) findViewById(R.id.petdiarty_pageIndicator);
		this.dynamicViewPager = (DecoratorViewPager) findViewById(R.id.petdiarty_viewpager);
		this.dynamicPetImg = (SimpleDraweeView) findViewById(R.id.petdiarty_petimg);
		this.dynamicPetSexImg = (ImageView) findViewById(R.id.petdiarty_petsex);
		this.dynamicPetName = (TextView) findViewById(R.id.petdiarty_petname);
		this.dynamicPetType = (TextView) findViewById(R.id.petdiarty_pettype);
		this.dynamicSendtime = (TextView) findViewById(R.id.petdiarty_sendtime);
		this.dynamicContent = (TextView) findViewById(R.id.petdiarty_content);
		this.moreZanTv = (TextView) findViewById(R.id.petdiarty_morezanTV);
		this.dynamicDianZanRL = (PercentLinearLayout) findViewById(R.id.petdiarty_zanRL);
		this.zanImg = (ImageView) findViewById(R.id.petdiarty_zanImage);
		this.zanTv = (TextView) findViewById(R.id.petdiarty_zantext);
		this.commentNumTv = (TextView) findViewById(R.id.petdiarty_commentNum);
		this.listZanUser = (HorizontalListView) findViewById(R.id.petdiarty_zan_userlist);
		this.shareMoreLL = (PercentLinearLayout) findViewById(R.id.petdiarty_sharemoreRl);
		this.listview = (ListView) findViewById(R.id.petdiarty_commentList);
		commontObjList = new ArrayList<CommontObject>();
		commentAdapter = new HomePLDetailsCommentAdapter(this);
		commentAdapter.setReportClick(this);
		listview.setAdapter(commentAdapter);

		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_petdiary);
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				GetMoreCommentsObject getmoreObj = new GetMoreCommentsObject();
				getmoreObj.setMeberid(dynamicObj.getMicroblogId());
				pageindex++;
				getmoreObj.setIndex(String.valueOf(pageindex));
				sendMessage(YSMSG.REQ_GETALLCOMMENTS, 0, 0, getmoreObj);
			}
		});
		scrollview = (CanPullScrollView) findViewById(R.id.petdiary_scrollview);

		scrollview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
					return true;
				}
				if (sendText.isShown()) {
					sendText.setText("");
					hideSoftKeyboard();
				}
				showkeybord = false;
				return false;
			}
		});
		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (sendText.isShown()) {
					sendText.setText("");
					hideSoftKeyboard();
				}
				showkeybord = false;
				return false;
			}
		});
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (SwipeOnTouchListener._currentActiveHSV != null) {
			boolean isrange = CommonUtils.inRangeOfView(SwipeOnTouchListener._currentActiveHSV, ev);
			if (isrange) {
				return super.dispatchTouchEvent(ev);
			} else {
				SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
				SwipeOnTouchListener._currentActiveHSV = null;
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	};

	ReportOrDeletePopupWindow reportOrDelWindow;

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.petdiarydetail_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PetDiaryDetailsActivity.this.finish();
			}
		});
		mTitleBar.titleRightRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportOrDelWindow = new ReportOrDeletePopupWindow(PetDiaryDetailsActivity.this, reportOrdelOnClick);
				UserObject myself = CoreModel.getInstance().getMyself();
				if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getMemberId()) && myself != null) {
					if (myself.getMemberId().equals(dynamicObj.getAccountObj().getMemberId())) {
						reportOrDelWindow.setText(getString(R.string.petdiarydetail_delete));
					} else {
						reportOrDelWindow.setText(getString(R.string.petdiarydetail_comment_report));
					}
				}
				reportOrDelWindow.showAtLocation(PetDiaryDetailsActivity.this.findViewById(R.id.petdiarty_mainRL),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	private void initInput() {
		inputRL = (PercentRelativeLayout) findViewById(R.id.petdiarty_inputRL);
		inputRL.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				return false;
			}
		});
		replyRL = (PercentRelativeLayout) findViewById(R.id.petdiarty_replyRL);
		replyNameTV = (TextView) findViewById(R.id.reply_name_text);
		replyCloseImage = (ImageView) findViewById(R.id.reply_closeimg);
		replyCloseImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				replyRL.setVisibility(View.GONE);
				replyNameTV.setText("");
				commentId = "";
			}
		});

		HandlerThread thread = new HandlerThread("ChattingVoiceRecord", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		keyboardBtn = (Button) findViewById(R.id.petdiarty_keyboard);
		voiceBtn = (Button) findViewById(R.id.petdiarty_voice);
		voicespeakBtn = (Button) findViewById(R.id.petdiarty_voicespeak);
		sendBtn = (Button) findViewById(R.id.petdiarty_sendtext);
		sendText = (EditText) findViewById(R.id.petdiarty_edit);
		sendBtn.setEnabled(false);
		keyboardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				sendText.setVisibility(View.VISIBLE);
				keyboardBtn.setVisibility(View.GONE);
				voicespeakBtn.setVisibility(View.GONE);
				voiceBtn.setVisibility(View.VISIBLE);

				if (sendText.getText().toString().trim().length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}
				sendText.requestFocus();
				LogUtils.i("InputMethodManager", "show");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		voiceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				hideSoftKeyboard();
				sendText.setVisibility(View.GONE);
				keyboardBtn.setVisibility(View.VISIBLE);
				voicespeakBtn.setVisibility(View.VISIBLE);
				voiceBtn.setVisibility(View.GONE);
				sendBtn.setEnabled(false);
			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (HasDelete) {
					YSToast.showToast(mActivity, getString(R.string.petdiarydetail_hasdelete));
					return;
				}
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				if (!CommonTextUtils.isEmpty(sendText.getText().toString().trim())) {
					AddCommonObject addcomObj = new AddCommonObject();
					addcomObj.setId(dynamicObj.getMicroblogId());
					addcomObj.setVoice(false);
					addcomObj.setText(sendText.getText().toString().trim());
					if (!CommonTextUtils.isEmpty(commentId)) {
						addcomObj.setCommentId(commentId);
						sendMessage(YSMSG.REQ_REPLYCOMMENT, 0, 0, addcomObj);
					} else {
						sendMessage(YSMSG.REQ_ADDCOMMENT, 0, 0, addcomObj);
					}
					showWaitingDialog();
				}
			}
		});
		sendText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				return false;
			}
		});
		sendText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (null == temp) {
					return;
				}

				if (temp.length() == 100) {
					YSToast.showToast(getApplicationContext(), getString(R.string.toast_comment_max_error));
				}
				if (s.toString().trim().length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}
			}
		});
		voiceRecorderView = (EaseVoiceRecorderView) findViewById(R.id.petdiarty_voice_recorder);
		voicespeakBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (HasDelete) {
					YSToast.showToast(mActivity, getString(R.string.petdiarydetail_hasdelete));
					return false;
				}
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				if (listener != null) {
					return listener.onPressToSpeakBtnTouch(v, event);
				}
				return false;
			}
		});
	}

	EaseVoiceRecorderView voiceRecorderView;

	EaseChatPrimaryMenuListener listener = new EaseChatPrimaryMenuListener() {

		@Override
		public void onToggleVoiceBtnClicked() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onToggleExtendClicked() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onToggleEmojiconClicked() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSendBtnClicked(String content) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
			return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

				@Override
				public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
					AddCommonObject addcomObj = new AddCommonObject();
					addcomObj.setId(dynamicObj.getMicroblogId());
					addcomObj.setVoice(true);
					addcomObj.setVoicePath(voiceFilePath);
					if (!CommonTextUtils.isEmpty(commentId)) {
						addcomObj.setCommentId(commentId);
						sendMessage(YSMSG.REQ_REPLYCOMMENT, 0, 0, addcomObj);
					} else {
						sendMessage(YSMSG.REQ_ADDCOMMENT, 0, 0, addcomObj);
					}
					showWaitingDialog();
				}
			});
		}

		@Override
		public void onEditTextClicked() {

		}
	};

	public long getAvailaleSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize) / 1024 / 1024;// MIB单位
	}

	private void initData() {
		if (dynamicObj != null && dynamicObj.getAccountObj() != null) {

			if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getIcoUrl())) {
				FrescoHelper.displayImageview(userimg,
						dynamicObj.getAccountObj().getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
						R.drawable.avatar_default_image, getResources(), true);
			} else {
				Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
				userimg.setImageURI(uri);
			}

			userimg.setOnClickListener(this);
			username.setOnClickListener(this);
			if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getNickName())) {
				username.setText(dynamicObj.getAccountObj().getNickName());
			} else {
				username.setText("");
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getGender())) {
				if (dynamicObj.getAccountObj().getGender().equals("0")) {
					usersex.setImageResource(R.drawable.sex_lady);
				} else {
					usersex.setImageResource(R.drawable.sex_man);
				}
			} else {
				usersex.setImageResource(R.drawable.sex_null);
			}

			if (!CommonTextUtils.isEmpty(dynamicObj.getLastLocateAddress())) {
				distanceTv.setText(dynamicObj.getLastLocateAddress());
			} else {
				distanceTv.setText("");
			}

			// LngLatObject mylnglat = App.getInstance().getLnglat();
			// if (dynamicObj.getAdressObject() != null && mylnglat != null) {
			// LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()),
			// Double.valueOf(mylnglat.getLng()));
			// LatLng cp = new
			// LatLng(Double.valueOf(dynamicObj.getAdressObject().getLat()),
			// Double.valueOf(dynamicObj
			// .getAdressObject().getLng()));
			// NumberFormat format = NumberFormat.getNumberInstance();
			// format.setMaximumFractionDigits(2);
			// String distance = format.format(DistanceUtil.getDistance(cp, mp)
			// / 1000);
			// distanceTv.setText(distance +
			// getString(R.string.homepage_distance_text));
			// } else {
			// distanceTv.setText("");
			// }

			UserObject myself = CoreModel.getInstance().getMyself();
			if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getMemberId()) && myself != null) {
				if (myself.getMemberId().equals(dynamicObj.getAccountObj().getMemberId())) {
					followImage.setVisibility(View.GONE);
					// mTitleBar.mRightTv.setVisibility(View.VISIBLE);
					// mTitleBar.mRightImg.setVisibility(View.GONE);
					// mTitleBar.mRightTv.setText(getString(R.string.editpet_righttext));
				} else {
					// mTitleBar.mRightTv.setVisibility(View.GONE);
					// mTitleBar.mRightImg.setVisibility(View.VISIBLE);
					// mTitleBar.mRightImg.setImageResource(R.drawable.report_img);
					if (dynamicObj.getAccountObj().isIsFollowed()) {
						followImage.setVisibility(View.GONE);
					} else {
						followImage.setVisibility(View.VISIBLE);
					}
				}
				mTitleBar.mRightTv.setVisibility(View.GONE);
				mTitleBar.mRightImg.setVisibility(View.VISIBLE);
				mTitleBar.mRightImg.setImageResource(R.drawable.report_img);
			}

			followImage.setOnClickListener(this);

			if (dynamicObj.getPetPhotos() != null && dynamicObj.getPetPhotos().size() != 0) {
				HomeViewPagerAdapter imageAdapter = new HomeViewPagerAdapter(this,
						addShowView(dynamicObj.getPetPhotos()));
				dynamicViewPager.setAdapter(imageAdapter);
				dynamicIndicator.setViewPager(dynamicViewPager);
			}

			PetObject pet = dynamicObj.getPet();
			if (pet != null) {
				if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
					FrescoHelper.displayImageview(dynamicPetImg,
							pet.getIcoUrl() + CommonUtils.getAvatarSize(mActivity), R.drawable.avatar_default_image,
							getResources(), true);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
					dynamicPetImg.setImageURI(uri);
				}
				dynamicPetImg.setOnClickListener(this);
				if (pet.getGender().equals("0")) {
					dynamicPetSexImg.setImageDrawable(getResources().getDrawable(R.drawable.pet_bitch));
				} else {
					dynamicPetSexImg.setImageDrawable(getResources().getDrawable(R.drawable.pet_dog));
				}
				if (!CommonTextUtils.isEmpty(pet.getName())) {
					dynamicPetName.setText(pet.getName());
				} else {
					dynamicPetName.setText("");
				}

				if (!CommonTextUtils.isEmpty(pet.getCategoryName())) {
					dynamicPetType.setText(pet.getCategoryName());
				} else {
					dynamicPetType.setText("");
				}
			}
			dynamicPetSexImg.setOnClickListener(this);
			dynamicPetName.setOnClickListener(this);
			dynamicPetType.setOnClickListener(this);
			if (!CommonTextUtils.isEmpty(dynamicObj.getCreateTime())) {
				dynamicSendtime.setText(CommonUtils.getTimeSamp(mActivity, dynamicObj.getCreateTime()));
			} else {
				dynamicSendtime.setText("");
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getText())) {
				dynamicContent.setText(dynamicObj.getText());
			} else {
				dynamicContent.setVisibility(View.GONE);
			}

			if (!CommonTextUtils.isEmpty(dynamicObj.getLikeCount()) && !dynamicObj.getLikeCount().equals("0")) {
				moreZanTv.setText(dynamicObj.getLikeCount());
				this.zanTv.setText(String.valueOf(dynamicObj.getLikeCount()));
			} else {
				moreZanTv.setText(getString(R.string.dynamic_zan));
				this.zanTv.setText(getString(R.string.dynamic_zan));
			}

			moreZanTv.setOnClickListener(this);

			if (dynamicObj.isIsLiked()) {
				zanImg.setImageResource(R.drawable.icon_iszan);
			} else {
				zanImg.setImageResource(R.drawable.icon_notzan);
			}

			dynamicDianZanRL.setOnClickListener(this);
			if (!CommonTextUtils.isEmpty(dynamicObj.getCommentCount()) && !dynamicObj.getCommentCount().equals("0")) {
				commentNumTv.setText(dynamicObj.getCommentCount());
			} else {
				commentNumTv.setText(getString(R.string.dynamic_comment));
			}
			shareMoreLL.setOnClickListener(this);
			PercentRelativeLayout zanlistRL = (PercentRelativeLayout) findViewById(R.id.petdiarty_zanListRL);
			if (dynamicObj.getlMembers() != null && dynamicObj.getlMembers().size() != 0) {
				zanUserAdapter = new DetailZanAdapter(this);
				listZanUser.setAdapter(zanUserAdapter);
				zanUserAdapter.updateList(dynamicObj.getlMembers());
				zanlistRL.setVisibility(View.VISIBLE);
			} else {
				zanlistRL.setVisibility(View.GONE);
			}
		}
		// listview.getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		// public void onGlobalLayout() {
		// if (showkeybord) {
		// showkeybord = false;
		// sendText.requestFocus();
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		// }
		// });
	}

	private ArrayList<View> addShowView(final ArrayList<String> urls) {
		ArrayList<View> mListViews = new ArrayList<View>();

		if (urls != null && urls.size() > 0) {
			for (int i = 0; i < urls.size(); i++) {
				View view = LayoutInflater.from(this).inflate(R.layout.listitem_image, null, false);
				SimpleDraweeView imgaeView = (SimpleDraweeView) view.findViewById(R.id.petdiaryimg_image);
				if (!CommonTextUtils.isEmpty(urls.get(i))) {
					FrescoHelper.displayImageview(imgaeView, urls.get(i) + CommonUtils.getImageSize(this),
							R.drawable.image_default_image, this.getResources());
				}
				mListViews.add(view);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						VPagerImageDetailsActivity.startActivity(mActivity, dynamicObj.getPetPhotos(),
								dynamicViewPager.getCurrentItem());
					}
				});
			}
		} else {
			return null;
		}
		return mListViews;
	}

	@Override
	public void handleMessage(Message msg) {
		if (!dynamicViewPager.isShown()) {
			return;
		}
		switch (msg.what) {
			case YSMSG.RESP_GETBLOGBYID :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							// json = json.getJSONObject("Microblog");
							dynamicObj = OJMFactory.createOJM().fromJson(json.toString(), DynamicObject.class);
							PetObject petObj = OJMFactory.createOJM().fromJson(json.getString("Pet"), PetObject.class);
							dynamicObj.setPet(petObj);

							AccountDetailObject account = OJMFactory.createOJM().fromJson(json.getString("Member"),
									AccountDetailObject.class);
							dynamicObj.setAccountObj(account);
							JSONArray photos = json.getJSONArray("Photos");
							ArrayList<String> ps = new ArrayList<String>();
							for (int j = 0; j < photos.length(); j++) {
								ps.add(photos.getString(j));
							}
							dynamicObj.setPetPhotos(ps);
							LngLatObject LngLatObj = new LngLatObject();
							JSONObject lnglatJson = json.getJSONObject("LocateAddress");
							LngLatObj.setLng(lnglatJson.getString("Lng"));
							LngLatObj.setLat(lnglatJson.getString("Lat"));
							dynamicObj.setAdressObject(LngLatObj);

							JSONArray likemeberJson = json.getJSONArray("LikeMembers");
							ArrayList<AccountObject> likeMebers = new ArrayList<AccountObject>();
							for (int k = 0; k < likemeberJson.length(); k++) {
								AccountObject meberObj = OJMFactory.createOJM().fromJson(likemeberJson.getString(k),
										AccountObject.class);
								likeMebers.add(meberObj);
							}
							dynamicObj.setlMembers(likeMebers);
							GetMoreCommentsObject getmoreObj = new GetMoreCommentsObject();
							getmoreObj.setMeberid(dynamicObj.getMicroblogId());
							pageindex = 1;
							getmoreObj.setIndex(String.valueOf(pageindex));
							initData();
							sendMessage(YSMSG.REQ_GETALLCOMMENTS, 0, 0, getmoreObj);

						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else if (msg.arg1 == 6080101) {
					dismissWaitingDialog();
					this.HasDeleteTV.setVisibility(View.VISIBLE);
					HasDelete = true;
					scrollview.setCanPullUp(false);
				} else {
					dismissWaitingDialog();
				}
				break;
			case YSMSG.RESP_ADDCOMMENT :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					int commentCount = Integer.valueOf(dynamicObj.getCommentCount());
					commentCount++;
					dynamicObj.setCommentCount(String.valueOf(commentCount));
					commentNumTv.setText(dynamicObj.getCommentCount());
					commontObjList.clear();
					pageindex = 1;
					GetMoreCommentsObject getmoreObj = new GetMoreCommentsObject();
					getmoreObj.setMeberid(dynamicObj.getMicroblogId());
					getmoreObj.setIndex(String.valueOf(pageindex));
					sendMessage(YSMSG.REQ_GETALLCOMMENTS, 0, 0, getmoreObj);

					if (sendText.isShown()) {
						sendText.setText("");
						sendText.clearFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(sendText.getWindowToken(), 0);
					}
					if (replyRL.isShown()) {
						replyRL.setVisibility(View.GONE);
						replyNameTV.setText("");
						commentId = "";
						YSToast.showToast(mActivity, getString(R.string.toast_replycomment_success));
					} else {
						YSToast.showToast(mActivity, getString(R.string.toast_addcomment_success));
					}
				} else {
					YSToast.showToast(mActivity, getString(R.string.toast_addcomment_failed));
				}
				break;
			case YSMSG.RESP_REPLYCOMMENT :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					int commentCount = Integer.valueOf(dynamicObj.getCommentCount());
					commentCount++;
					dynamicObj.setCommentCount(String.valueOf(commentCount));
					commentNumTv.setText(dynamicObj.getCommentCount());
					commontObjList.clear();
					pageindex = 1;
					GetMoreCommentsObject getmoreObj = new GetMoreCommentsObject();
					getmoreObj.setMeberid(dynamicObj.getMicroblogId());
					getmoreObj.setIndex(String.valueOf(pageindex));
					sendMessage(YSMSG.REQ_GETALLCOMMENTS, 0, 0, getmoreObj);

					// InputMethodManager imm = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(sendText.getWindowToken(),
					// InputMethodManager.RESULT_HIDDEN);
					// if (imm.isActive()) {
					// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					// InputMethodManager.HIDE_NOT_ALWAYS);
					// }
					if (sendText.isShown()) {
						sendText.setText("");
						sendText.clearFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(sendText.getWindowToken(), 0);
					}

					if (replyRL.isShown()) {
						replyRL.setVisibility(View.GONE);
						replyNameTV.setText("");
						commentId = "";
						YSToast.showToast(mActivity, getString(R.string.toast_replycomment_success));
					}
				} else {
					YSToast.showToast(mActivity, getString(R.string.toast_addcomment_failed));
				}

				break;

			case YSMSG.RESP_REMOVECOMMENT :
				if (msg.arg1 == 200) {
					int commentCount = Integer.valueOf(dynamicObj.getCommentCount());
					commentCount--;
					dynamicObj.setCommentCount(String.valueOf(commentCount));
					commentNumTv.setText(dynamicObj.getCommentCount());
					pageindex = 1;
					commontObjList.clear();
					GetMoreCommentsObject getmoreObj = new GetMoreCommentsObject();
					getmoreObj.setMeberid(dynamicObj.getMicroblogId());
					getmoreObj.setIndex(String.valueOf(pageindex));
					sendMessage(YSMSG.REQ_GETALLCOMMENTS, 0, 0, getmoreObj);
					YSToast.showToast(mActivity, getString(R.string.toast_delcomment_success));
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			// case WHAT_ON_DIMISS_DIALOG :
			// popupWindow.dismiss();
			// //
			// voicespeakBtn.setBackgroundDrawable(ResourceHelper.getDrawableById(getBaseContext(),
			// // R.drawable.voice_rcd_btn_talk_nor));
			// voicespeakBtn.setEnabled(true);
			// break;

			case YSMSG.RESP_GETALLCOMMENTS :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (msg.obj != null) {

						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							if (pageindex == 1) {
								commontObjList.clear();
								// commentAdapter.updateList(commontObjList);
								// listview.removeAllViews();
							}
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								scrollview.setCanPullUp(true);
							} else {
								scrollview.setCanPullUp(false);
							}
							for (int i = 0; i < array.length(); i++) {
								CommontObject commontObj = OJMFactory.createOJM().fromJson(array.getString(i),
										CommontObject.class);
								AccountObject aObj = OJMFactory.createOJM().fromJson(
										array.getJSONObject(i).getString("Member"), AccountObject.class);
								AccountObject replyObj = OJMFactory.createOJM().fromJson(
										array.getJSONObject(i).getString("ReplyMember"), AccountObject.class);
								commontObj.setAccount(aObj);
								commontObj.setReplyAccount(replyObj);
								commontObjList.add(commontObj);
								if (!CommonTextUtils.isEmpty(commontObj.getVoiceUrl())) {
									downloadVoiceFile(commontObj.getVoiceUrl());
								}
							}
							commentAdapter.updateList(commontObjList);
							changcommentListHeight();
							listview.getViewTreeObserver().addOnGlobalLayoutListener(globalListener);
							// mHandler.postDelayed(new Runnable() {
							// @Override
							// public void run() {
							//
							// }
							// }, 300);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else {
						changcommentListHeight();
						listview.getViewTreeObserver().addOnGlobalLayoutListener(globalListener);
						// mHandler.postDelayed(new Runnable() {
						// @Override
						// public void run() {
						//
						// if (showkeybord) {
						// showkeybord = false;
						// sendText.requestFocus();
						// InputMethodManager imm = (InputMethodManager)
						// getSystemService(Context.INPUT_METHOD_SERVICE);
						// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
						// InputMethodManager.HIDE_NOT_ALWAYS);
						// }
						// }
						// }, 300);
					}
				} else {
					refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
			case YSMSG.RESP_CANCELFOLLOW :
				if (msg.arg1 == 200) {
					dynamicObj.setIsFollowed(false);
					followImage.setVisibility(View.VISIBLE);
				}
				break;
			case YSMSG.RESP_FOLLOW :
				if (msg.arg1 == 200) {
					dynamicObj.setIsFollowed(true);
					followImage.setVisibility(View.GONE);
					// sendMessage(YSMSG.RESP_MAIN_FOLLOW, 0, 0,
					// dynamicObj.getMemberID());
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.RESP_MAIN_FOLLOW;
					message.obj = dynamicObj.getAccountObj().getMemberId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
				}
				break;
			case YSMSG.RESP_MAIN_FOLLOW :
				LogUtils.i("RESP_MAIN_FOLLOW", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String meberid = String.valueOf(msg.obj);
					if (meberid.equals(dynamicObj.getMicroblogId())) {
						dynamicObj.setIsFollowed(true);
						followImage.setVisibility(View.GONE);
					}
				}
				break;
			case YSMSG.RESP_MAIN_CANCELFOLLOW :
				LogUtils.i("RESP_MAIN_CANCELFOLLOW", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String meberid = String.valueOf(msg.obj);
					if (meberid.equals(dynamicObj.getMicroblogId())) {
						dynamicObj.setIsFollowed(false);
						followImage.setVisibility(View.VISIBLE);
					}
				}
				break;
			case YSMSG.RESP_DETAILBLOGCANCELLIKE :

				isZanIng = false;
				if (msg.arg1 == 200) {
					dynamicObj.setIsLiked(false);
					zanImg.setImageResource(R.drawable.icon_notzan);
					int likeCount = Integer.valueOf(dynamicObj.getLikeCount());
					likeCount--;
					dynamicObj.setLikeCount(String.valueOf(likeCount));
					AccountObject myself = getMyself();
					if (myself != null) {
						int meberindex = getindexByMeberid(myself.getMemberId());
						if (meberindex != -1) {
							dynamicObj.getlMembers().remove(meberindex);
						}
					}
					initData();
					// sendMessage(YSMSG.RESP_DETAILTOMAINCANCELLIKE, 0, 0,
					// dynamicObj.get_id());
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.RESP_DETAILTOMAINCANCELLIKE;
					message.obj = dynamicObj.getMicroblogId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_DETAILBLOGLIKE :
				isZanIng = false;
				if (msg.arg1 == 200) {
					dynamicObj.setIsLiked(true);
					zanImg.setImageResource(R.drawable.icon_iszan);
					int likeCount = Integer.valueOf(dynamicObj.getLikeCount());
					likeCount++;
					dynamicObj.setLikeCount(String.valueOf(likeCount));
					AccountObject myself = getMyself();
					if (myself != null) {
						dynamicObj.getlMembers().add(0, myself);
					}
					initData();
					// sendMessage(YSMSG.RESP_DETAILTOMAINLIKE, 0, 0,
					// dynamicObj.get_id());
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.RESP_DETAILTOMAINLIKE;
					message.obj = dynamicObj.getMicroblogId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_REMOVEMICROBLOG :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					// Constant.shouldRefreshMain = true;
					// LogUtils.i("HomePageFragment", Constant.shouldRefreshMain
					// + "");
					// sendEmptyMessage(YSMSG.REQ_MAIN_REFRESH);
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.REQ_MAIN_DELETE;
					message.obj = dynamicObj.getMicroblogId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
					mActivity.finish();
					YSToast.showToast(mActivity, getString(R.string.toast_microblogremove_success));
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}
	OnGlobalLayoutListener globalListener = new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			LogUtils.i("globalListener", "onGlobalLayout" + ":" + listview.getHeight());

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (showkeybord) {
						showkeybord = false;
						sendText.requestFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
						listview.getViewTreeObserver().removeOnGlobalLayoutListener(globalListener);
					}
				}
			}, 300);
		}
	};

	private void downloadVoiceFile(String voiceUrl) {
		try {
			String savePath = FileUtils.VOICE + MD5.md5(voiceUrl) + ".amr";
			if (!FileUtils.exists(savePath)) {
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(voiceUrl, new DownloadHttpResponseHandler(savePath) {
					public void onSuccess(String savePath) {
						commentAdapter.notifyDataSetChanged();
					}
				});
			}
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changcommentListHeight() {
		CommonUtils.setListViewHeightBasedOnChildren(listview);
	}

	private int getindexByMeberid(String meberid) {
		for (int i = 0; i < dynamicObj.getlMembers().size(); i++) {
			AccountObject account = dynamicObj.getlMembers().get(i);
			if (account.getMemberId().equals(meberid)) {
				return i;
			}
		}
		return -1;
	}

	public AccountObject getMyself() {
		AccountObject account;
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			account = new AccountObject();
			account.setMemberId(myself.getMemberId());
			account.setIcoUrl(myself.getIcoUrl());
			return account;
		}
		return null;
	}

	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow;

	boolean isZanIng = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.petdiarty_name :
				AccountCenterActivity.startActivity(PetDiaryDetailsActivity.this, dynamicObj.getAccountObj()
						.getMemberId());
				break;
			case R.id.petdiarty_userimg :
				AccountCenterActivity.startActivity(PetDiaryDetailsActivity.this, dynamicObj.getAccountObj()
						.getMemberId());
				break;
			case R.id.petdiarty_followbtn :
				if (!dynamicObj.isIsFollowed()) {
					sendMessage(YSMSG.REQ_FOLLOW, 0, 0, dynamicObj.getAccountObj().getMemberId());
				}
				break;
			case R.id.petdiarty_viewpager :
				break;
			case R.id.petdiarty_petimg :
				GetPetObject getPet = new GetPetObject();
				getPet.setMeberId(dynamicObj.getAccountObj().getMemberId());
				getPet.setPetId(dynamicObj.getPet().getPetId());
				PetCenterActivity.startActivity(mActivity, getPet);
				break;
			case R.id.petdiarty_petname :
				GetPetObject getp = new GetPetObject();
				getp.setMeberId(dynamicObj.getAccountObj().getMemberId());
				getp.setPetId(dynamicObj.getPet().getPetId());
				PetCenterActivity.startActivity(mActivity, getp);
				break;
			case R.id.petdiarty_morezanTV :
				ZanListActivity.startActivity(mActivity, dynamicObj.getMicroblogId());
				break;
			case R.id.petdiarty_zanRL :
				if (HasDelete) {
					YSToast.showToast(mActivity, getString(R.string.petdiarydetail_hasdelete));
					return;
				}
				if (!isZanIng) {
					if (dynamicObj.isIsLiked()) {
						sendMessage(YSMSG.REQ_DETAILBLOGCANCELLIKE, 0, 0, dynamicObj.getMicroblogId());
					} else {
						sendMessage(YSMSG.REQ_DETAILBLOGLIKE, 0, 0, dynamicObj.getMicroblogId());
					}
				}
				break;
			case R.id.petdiarty_sharemoreRl :
				hideSoftKeyboard();
				menuWindow = new SelectPicPopupWindow(PetDiaryDetailsActivity.this, itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(PetDiaryDetailsActivity.this.findViewById(R.id.petdiarty_mainRL),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				break;
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
				case R.id.img_wx :
					break;
				case R.id.img_wb :
					break;
				case R.id.img_friend :
					break;

				default :
					break;
			}
		}
	};

	private OnClickListener reportOrdelOnClick = new OnClickListener() {

		public void onClick(View v) {
			reportOrDelWindow.dismiss();
			switch (v.getId()) {
				case R.id.reportOrDeleteBtn :
					UserObject myself = CoreModel.getInstance().getMyself();
					if (!CommonTextUtils.isEmpty(dynamicObj.getAccountObj().getMemberId()) && myself != null) {
						if (myself.getMemberId().equals(dynamicObj.getAccountObj().getMemberId())) {
							sendMessage(YSMSG.REQ_REMOVEMICROBLOG, 0, 0, dynamicObj.getMicroblogId());
							showWaitingDialog();
						} else {
							ReportObject reportObj = new ReportObject();
							reportObj.setMicroblogId(dynamicObj.getMicroblogId());
							ReportActivity.startActivity(mActivity, reportObj);
						}
					}
					break;
				default :
					break;
			}
		}
	};

	@Override
	public void reportClick(CommontObject commentObj) {
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			if (myself.getMemberId().equals(commentObj.getMemberID())) {
				RemoveCommentObject obj = new RemoveCommentObject();
				obj.setMicroblogid(dynamicObj.getMicroblogId());
				obj.setCommentid(commentObj.getCommentId());
				sendMessage(YSMSG.REQ_REMOVECOMMENT, 0, 0, obj);
			} else {
				ReportObject reportObj = new ReportObject();
				// reportObj.setMicroblogId(dynamicObj.getMicroblogId());
				reportObj.setCommentMemberId(commentObj.getCommentId());
				ReportActivity.startActivity(mActivity, reportObj);
			}
			replyRL.setVisibility(View.GONE);
			replyNameTV.setText("");
			commentId = "";
			if (SwipeOnTouchListener._currentActiveHSV != null) {
				SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
				SwipeOnTouchListener._currentActiveHSV = null;
			}
		}
	}

	@Override
	public void mainClick(CommontObject commentObj) {
		UserObject myself = CoreModel.getInstance().getMyself();
		if (commentObj != null && myself != null) {
			if (!myself.getMemberId().equals(commentObj.getMemberID())) {
				replyRL.setVisibility(View.VISIBLE);
				replyNameTV.setText(commentObj.getAccount().getNickName());
				commentId = commentObj.getCommentId();
				sendText.requestFocus();
				sendText.setFocusable(true);
				sendText.setFocusableInTouchMode(true);
				sendText.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) sendText.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(sendText, 0);
				if (SwipeOnTouchListener._currentActiveHSV != null) {
					SwipeOnTouchListener._currentActiveHSV.smoothScrollTo(0, 0);
					SwipeOnTouchListener._currentActiveHSV = null;
				}
				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// imm.showSoftInput(sendText, InputMethodManager.RESULT_SHOWN);
				// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}
	}

	@Override
	public void systemNewsMainClick(SystemNoticeObject noticeObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void systemNewsDelClick(SystemNoticeObject noticeObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commentMainClick(NoticeObject commentObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commentDelClick(NoticeObject commentObj) {
		// TODO Auto-generated method stub

	}
}