package mobi.jzcx.android.chongmi.ui.main;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExFragmentActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.group.CreateEventActivity;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.BackListener;
import mobi.jzcx.android.chongmi.ui.main.homepage.HomePageFragment;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetDiaryActivity;
import mobi.jzcx.android.chongmi.ui.main.mine.AddPetActivity;
import mobi.jzcx.android.chongmi.ui.main.mine.MineFragment;
import mobi.jzcx.android.chongmi.ui.main.serve.NewsFragment;
import mobi.jzcx.android.chongmi.ui.main.shopping.ShoppingFragment;
import mobi.jzcx.android.chongmi.ui.register.RegisteOverActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.chongmi.view.MainEditPopupWindow;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseExFragmentActivity
		implements
			MainListener,
			OnClickListener,
			NewsFragment.OnUpdateMsgUnreadCountsListener {
	private HomePageFragment homeFragment = null;
	private ShoppingFragment shoppingFragment = null;
	private NewsFragment newsFragment = null;
	private MineFragment mineFragment = null;
	private Fragment mCurFragment = null;

	private PercentRelativeLayout bottomRL;
	private ImageView editImg;
	private PercentLinearLayout homeLL;
	private PercentLinearLayout shoppingLL;
	private PercentRelativeLayout newsLL;
	private PercentLinearLayout mineLL;

	Animation showAnim;
	Animation hideAnim;
	ImageView haveNewsImg;

	MainEditPopupWindow mainEditPopView;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, MainActivity.class);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mCurFragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}
		setContentView(R.layout.activity_main);
		initView();
		initData();
		mActivity = this;
		UmengUpdateAgent.update(this);
		UserObject user = CoreModel.getInstance().getMyself();

	}
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		OnUpdateMsgUnreadCounts();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	private void initView() {
		bottomRL = (PercentRelativeLayout) findViewById(R.id.main_bottomLL);
		haveNewsImg = (ImageView) findViewById(R.id.main_havenews_img);

		homeLL = (PercentLinearLayout) findViewById(R.id.main_bottom_homeLL);
		shoppingLL = (PercentLinearLayout) findViewById(R.id.main_bottom_shoppingLL);
		newsLL = (PercentRelativeLayout) findViewById(R.id.main_bottom_newsLL);
		mineLL = (PercentLinearLayout) findViewById(R.id.main_bottom_mineLL);

		editImg = (ImageView) findViewById(R.id.main_bottom_editimg);

		homeLL.setOnClickListener(this);
		shoppingLL.setOnClickListener(this);
		newsLL.setOnClickListener(this);
		mineLL.setOnClickListener(this);

		editImg.setOnClickListener(this);

		if (homeFragment == null) {
			mCurFragment = homeFragment = new HomePageFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_main_content, mCurFragment).commit();

		showAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.edit_bottom_in);
		hideAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate_between_interface_bottom_in);
	}

	private void initData() {

		registerMessageReceiver();
		// sendEmptyMessage(YSMSG.REQ_TOKEN);
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
		// sendEmptyMessage(YSMSG.REQ_GETADDACTIVITY);
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
							}
							if (msgtype.equals("System")) {
								PreferencesUtils.setIsHaveSysMsgs(true);
							}
							if (msgtype.equals("ActivityAdd")) {
								PreferencesUtils.setIsHaveRequestMsgs(true);
								// sendEmptyMessage(YSMSG.REQ_GETADDACTIVITY);
							}
							OnUpdateMsgUnreadCounts();
						}
					} catch (JSONException e) {

					}
				}
				LogUtils.i("MainActivity", showMsg.toString());
			}
		}
	}
	private ArrayList<PetObject> petList;

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_OUTOFLINE : {
				if (IMInitHelper.getInstance().isLoggedIn()) {
					IMInitHelper.getInstance().logout(true, new EMCallBack() {

						@Override
						public void onSuccess() {
							LogUtils.i("IMInitHelper", "logout onSuccess()");
						}

						@Override
						public void onProgress(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(int arg0, String arg1) {
							LogUtils.i("IMInitHelper", "logout onError()");

						}
					});
				}

				CoreModel.getInstance().setMyself(null);
				PreferencesUtils.setUserToken("");
				showLoginDialog();
			}
				break;
			case YSMSG.RESP_GETMYPETS :
				if (ActivityUtils.isActivityForeground(getBaseContext(), this.getClass())) {
					if (msg.arg1 == 200) {
						if (msg.obj != null) {
							String result = (String) msg.obj;
							try {
								JSONObject json = new JSONObject(result);
								JSONArray array = json.getJSONArray("Pets");
								petList = new ArrayList<PetObject>();
								for (int i = 0; i < array.length(); i++) {
									PetObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
											PetObject.class);
									petList.add(actObj);
								}
								if (petList.size() > 0) {
									PetDiaryActivity.startActivity(mActivity, petList);
								} else {
									showAddPetDialog();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (java.lang.InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			default :
				break;
		}
	}

	private void showAddPetDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.notPetDetail_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), false,
				new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}, new OnClickListener() {
					@Override
					public void onClick(View v) {
						AddPetActivity.startActivity(mActivity, false);
					}
				});
	}

	private void showLoginDialog() {
		DialogHelper.showOneDialog(mActivity, false, getString(R.string.dialog_relogin),
				getString(R.string.dialog_relogin_ok), false, new OnClickListener() {
					@Override
					public void onClick(View v) {
						LoginActivity.startActivity(App.getInstance().getForegroundActivity(),
								PreferencesUtils.getLoginPhone());
						App.getInstance().clearActivity();
					}
				});
	}

	private long firstTime = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
			if (!bottomRL.isShown()) {
				BackListener listener = (BackListener) homeFragment;
				listener.backClick();
				return true;
			}

			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				YSToast.showToast(App.getInstance(), R.string.back_click_text);
				firstTime = secondTime;
				return true;
			} else {
				moveTaskToBack(false);
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void showUpdateDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.notupdateDetail_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), false,
				new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}, new OnClickListener() {
					@Override
					public void onClick(View v) {
						RegisteOverActivity.startActivity(mActivity);
					}
				});
	}

	private OnClickListener editAndPetDiaryOnClick = new OnClickListener() {

		public void onClick(View v) {
			mainEditPopView.dismiss();
			switch (v.getId()) {
				case R.id.main_petdiaryLL :
					sendEmptyMessage(YSMSG.REQ_GETMYPETS);
					break;
				case R.id.main_createventLL :
					if (CommonTextUtils.isEmpty(CoreModel.getInstance().getMyself().getBirthday())) {
						showUpdateDialog();
					} else {
						CreateEventActivity.startActivity(mActivity);
					}
					break;
				default :
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_bottom_homeLL :

				if (curPosition == 0) {
					return;
				}
				setSelection(0);
				if (homeFragment == null) {
					homeFragment = new HomePageFragment();
				}
				switchContent(homeFragment);
				break;

			case R.id.main_bottom_shoppingLL :

				if (curPosition == 1) {
					return;
				}
				setSelection(1);
				if (shoppingFragment == null) {
					shoppingFragment = new ShoppingFragment();
				}
				switchContent(shoppingFragment);
				break;

			case R.id.main_bottom_newsLL :
				MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_7));
				if (curPosition == 2) {
					return;
				}
				setSelection(2);
				if (newsFragment == null) {
					newsFragment = new NewsFragment();
				}
				switchContent(newsFragment);
				break;
			case R.id.main_bottom_mineLL :
				if (curPosition == 3) {
					return;
				}
				setSelection(3);
				if (mineFragment == null) {
					mineFragment = new MineFragment();
				}
				switchContent(mineFragment);
				break;
			case R.id.main_bottom_editimg :
				mainEditPopView = new MainEditPopupWindow(mActivity, editAndPetDiaryOnClick);
				mainEditPopView.showAtLocation(findViewById(R.id.mainRL), Gravity.CENTER, 0, 0);
				break;
			default :
				break;
		}
	}
	int curPosition = 0;

	private void setSelection(int index) {
		curPosition = index;
		ImageView nearlyImg = (ImageView) findViewById(R.id.main_nearly_img);
		ImageView cmImg = (ImageView) findViewById(R.id.main_cm_img);
		ImageView newsImg = (ImageView) findViewById(R.id.main_news_img);
		ImageView mineImg = (ImageView) findViewById(R.id.main_mine_img);

		TextView nearlyText = (TextView) findViewById(R.id.main_nearly_text);
		TextView cmText = (TextView) findViewById(R.id.main_cm_text);
		TextView newsText = (TextView) findViewById(R.id.main_news_text);
		TextView mineText = (TextView) findViewById(R.id.main_mine_text);
		switch (index) {
			case 0 :
				nearlyImg.setBackgroundResource(R.drawable.main_nearly_click);
				nearlyText.setTextColor(getResources().getColor(R.color.comm_title_color));

				cmImg.setBackgroundResource(R.drawable.main_cm_selector);
				cmText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				newsImg.setBackgroundResource(R.drawable.main_news_selector);
				newsText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				mineImg.setBackgroundResource(R.drawable.main_mine_selector);
				mineText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));
				break;
			case 1 :
				nearlyImg.setBackgroundResource(R.drawable.main_nearly_selector);
				nearlyText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				cmImg.setBackgroundResource(R.drawable.main_cm_click);
				cmText.setTextColor(getResources().getColor(R.color.comm_title_color));

				newsImg.setBackgroundResource(R.drawable.main_news_selector);
				newsText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				mineImg.setBackgroundResource(R.drawable.main_mine_selector);
				mineText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));
				break;
			case 2 :
				nearlyImg.setBackgroundResource(R.drawable.main_nearly_selector);
				nearlyText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				cmImg.setBackgroundResource(R.drawable.main_cm_selector);
				cmText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				newsImg.setBackgroundResource(R.drawable.main_news_click);
				newsText.setTextColor(getResources().getColor(R.color.comm_title_color));

				mineImg.setBackgroundResource(R.drawable.main_mine_selector);
				mineText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));
				break;
			case 3 :
				nearlyImg.setBackgroundResource(R.drawable.main_nearly_selector);
				nearlyText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				cmImg.setBackgroundResource(R.drawable.main_cm_selector);
				cmText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				newsImg.setBackgroundResource(R.drawable.main_news_selector);
				newsText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				mineImg.setBackgroundResource(R.drawable.main_mine_click);
				mineText.setTextColor(getResources().getColor(R.color.comm_title_color));
				break;
			default :
				nearlyImg.setBackgroundResource(R.drawable.main_nearly_selector);
				nearlyText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				cmImg.setBackgroundResource(R.drawable.main_cm_selector);
				cmText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				newsImg.setBackgroundResource(R.drawable.main_news_selector);
				newsText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));

				mineImg.setBackgroundResource(R.drawable.main_mine_selector);
				mineText.setTextColor(getResources().getColorStateList(R.drawable.main_bottom_textselector));
				break;
		}
	}

	private void switchContent(Fragment fragment) {
		if (null == fragment) {
			fragment = homeFragment;
		}
		if (null == fragment) {
			fragment = homeFragment = new HomePageFragment();
		}

		Fragment from = mCurFragment;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.translate_between_interface_right_in,
				R.anim.translate_between_interface_left_out, R.anim.translate_between_interface_left_in,
				R.anim.translate_between_interface_right_out);
		if (!fragment.isAdded()) {
			// 隐藏当前的fragment，add下一个到Activity中
			fragmentTransaction.hide(from).add(R.id.layout_main_content, fragment);
		} else {
			// 隐藏当前的fragment，显示下一个
			fragmentTransaction.hide(from).show(fragment);
		}

		// fragmentTransaction.replace(R.id.layout_main_content, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		mCurFragment = fragment;
	}

	@Override
	public void OnUpdateMsgUnreadCounts() {
		// 未读消息
		int unreadCount = EMChatManager.getInstance().getUnreadMsgsCount();
		if (unreadCount > 0 || PreferencesUtils.getIsHaveComments() || PreferencesUtils.getIsHaveSysMsgs()
				|| PreferencesUtils.getIsHaveRequestMsgs()) {
			haveNewsImg.setVisibility(View.VISIBLE);
		} else {
			haveNewsImg.setVisibility(View.GONE);
		}
	}

	@Override
	public void startSearch() {
		editImg.setVisibility(View.GONE);
		bottomRL.setVisibility(View.GONE);
	}

	@Override
	public void cancelSearch() {
		editImg.setVisibility(View.VISIBLE);
		bottomRL.setVisibility(View.VISIBLE);
	}

}
