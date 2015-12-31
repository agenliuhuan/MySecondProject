package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.ui.adapter.HomeActiveAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.HomeDynamicAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExFragment;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.ui.main.MainListener;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.ui.main.serve.GroupInfoActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.view.CanPullListView;
import mobi.jzcx.android.chongmi.view.ClearEditText;
import mobi.jzcx.android.chongmi.view.SelectPicPopupWindow;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import mobi.jzcx.android.core.view.stickylistheaders.StickyListHeadersListView;
import mobi.jzcx.android.core.view.stickylistheaders.StickyListHeadersListView.OnHeaderClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

@SuppressLint("NewApi")
public class HomePageFragment extends BaseExFragment
		implements
			OnClickListener,
			DynamicClickListener,
			OnHeaderClickListener,
			BackListener {
	// 动态
	private PullToRefreshLayout dynamicRefresh;
	private StickyListHeadersListView mDynamicList;
	private HomeDynamicAdapter mDynamicAdapter;
	private ArrayList<DynamicObject> mDynamics;

	// 关注
	private PullToRefreshLayout attentionRefresh;
	private StickyListHeadersListView mAttentionList;
	private HomeDynamicAdapter mAttentionAdapter;
	private ArrayList<DynamicObject> mLikes;
	private PercentLinearLayout noattentionLL;

	// 活动
	private PullToRefreshLayout activeRefresh;
	private CanPullListView mActiveListView;
	private HomeActiveAdapter mActiveAdapter;
	private ArrayList<ActivityObject> mAllActives;
	private ArrayList<ActivityObject> mSearchActives;
	PercentLinearLayout notLoginLL;
	TextView headerView;

	View rootView;
	private LinearLayout llTitleDynamic;
	private LinearLayout llTitleAttention;
	private LinearLayout llTitleActive;

	private LinearLayout llHomeDynamic;
	private RelativeLayout rlHomeAttention;
	private LinearLayout llHomeActive;

	private TextView tvTitleDynamic;
	private TextView tvTitleAttention;
	private TextView tvTitleActive;

	DynamicObject curDynamicObj;

	int dynamicPageIndex = 1;
	int likePageIndex = 1;
	int activityPageIndex = 1;
	int activitySearchPageIndex = 1;

	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	static HomePageFragment fragment;

	public static HomePageFragment getInstance() {
		if (fragment == null) {
			fragment = new HomePageFragment();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		initTitle();
		initDynamicView();
		initAttentionView();
		initActiveView();
		configPlatforms();
		setShareContent();
	}

	private void initTitle() {
		llTitleDynamic = (LinearLayout) rootView.findViewById(R.id.ll_home_title_dynamic);
		llTitleAttention = (LinearLayout) rootView.findViewById(R.id.ll_home_title_attention);
		llTitleActive = (LinearLayout) rootView.findViewById(R.id.ll_home_title_active);

		llHomeDynamic = (LinearLayout) rootView.findViewById(R.id.ll_home_new);
		rlHomeAttention = (RelativeLayout) rootView.findViewById(R.id.rl_home_attention);
		llHomeActive = (LinearLayout) rootView.findViewById(R.id.ll_home_active);

		tvTitleDynamic = (TextView) rootView.findViewById(R.id.tv_dynamic);
		tvTitleAttention = (TextView) rootView.findViewById(R.id.tv_attention);
		tvTitleActive = (TextView) rootView.findViewById(R.id.tv_active);

		llTitleDynamic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llTitleDynamic.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.drawable.layout_title_select_bg));
				llTitleAttention.setBackgroundDrawable(null);
				llTitleActive.setBackgroundDrawable(null);

				tvTitleDynamic.setTextColor(getActivity().getResources().getColor(R.color.comm_title_color));
				tvTitleAttention.setTextColor(getActivity().getResources().getColor(R.color.white));
				tvTitleActive.setTextColor(getActivity().getResources().getColor(R.color.white));

				llHomeDynamic.setVisibility(View.VISIBLE);
				rlHomeAttention.setVisibility(View.GONE);
				llHomeActive.setVisibility(View.GONE);

				setDynamicData();

			}
		});
		llTitleAttention.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llTitleDynamic.setBackgroundDrawable(null);
				llTitleAttention.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.drawable.layout_title_select_bg));
				llTitleActive.setBackgroundDrawable(null);

				tvTitleDynamic.setTextColor(getActivity().getResources().getColor(R.color.white));
				tvTitleAttention.setTextColor(getActivity().getResources().getColor(R.color.comm_title_color));
				tvTitleActive.setTextColor(getActivity().getResources().getColor(R.color.white));

				llHomeDynamic.setVisibility(View.GONE);
				rlHomeAttention.setVisibility(View.VISIBLE);
				llHomeActive.setVisibility(View.GONE);

				setLikesData();
				MobclickAgent.onEvent(getActivity(), getString(R.string.umeng_feedback_2));
			}
		});
		llTitleActive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llTitleDynamic.setBackgroundDrawable(null);
				llTitleAttention.setBackgroundDrawable(null);
				llTitleActive.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.drawable.layout_title_select_bg));

				tvTitleDynamic.setTextColor(getActivity().getResources().getColor(R.color.white));
				tvTitleAttention.setTextColor(getActivity().getResources().getColor(R.color.white));
				tvTitleActive.setTextColor(getActivity().getResources().getColor(R.color.comm_title_color));

				llHomeDynamic.setVisibility(View.GONE);
				rlHomeAttention.setVisibility(View.GONE);
				llHomeActive.setVisibility(View.VISIBLE);

				setActivedata();
				MobclickAgent.onEvent(getActivity(), getString(R.string.umeng_feedback_3));
			}
		});
	}

	private void initDynamicView() {
		dynamicRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_dynamic);
		dynamicRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				dynamicPageIndex = 1;
				sendMessage(YSMSG.REQ_GETMYDYNAMIC, dynamicPageIndex, 0, null);

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				sendMessage(YSMSG.REQ_GETMYDYNAMIC, dynamicPageIndex, 0, null);
			}
		});

		mDynamicList = (StickyListHeadersListView) rootView.findViewById(R.id.lv_dynamic);
		mDynamicList.setOnHeaderClickListener(this);
		mDynamicAdapter = new HomeDynamicAdapter(getActivity());
		mDynamicAdapter.setClickListener(this);
		mDynamicList.setAdapter(mDynamicAdapter);
	}

	private void initAttentionView() {
		attentionRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_attention);
		attentionRefresh.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				likePageIndex = 1;
				sendMessage(YSMSG.REQ_GETMYLIKES, likePageIndex, 0, null);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				sendMessage(YSMSG.REQ_GETMYLIKES, likePageIndex, 0, null);
			}
		});

		mAttentionList = (StickyListHeadersListView) rootView.findViewById(R.id.lv_attention);
		mAttentionList.setOnHeaderClickListener(this);
		mAttentionAdapter = new HomeDynamicAdapter(getActivity());
		mAttentionAdapter.setClickListener(this);
		mAttentionList.setAdapter(mAttentionAdapter);

	}

	PercentLinearLayout titleLL;
	LinearLayout serchLL;
	ClearEditText edittext;
	TextView cancelText;
	TextView noresultTV;

	MainListener mainListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainListener = (MainListener) activity;
	}
	boolean isSearch = false;

	private void initActiveView() {
		activeRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_active);
		activeRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				if (isSearch) {
					activitySearchPageIndex = 1;
					sendMessage(YSMSG.REQ_SEARCHACTIVITY, activitySearchPageIndex, 0, edittext.getText().toString());
				} else {
					activityPageIndex = 1;
					sendMessage(YSMSG.REQ_GET_LOCALACTIVITY, activityPageIndex, 0, null);
				}

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (isSearch) {
					sendMessage(YSMSG.REQ_SEARCHACTIVITY, activitySearchPageIndex, 0, edittext.getText().toString());
				} else {
					sendMessage(YSMSG.REQ_GET_LOCALACTIVITY, activityPageIndex, 0, null);
				}

			}
		});
		headerView = (TextView) rootView.findViewById(R.id.lv_active_headerview);
		mActiveListView = (CanPullListView) rootView.findViewById(R.id.lv_active);
		mActiveAdapter = new HomeActiveAdapter(getActivity());
		mActiveListView.setAdapter(mActiveAdapter);
		mActiveListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				hideSoftKeyboard();
				if (mActiveAdapter != null) {
					ActivityObject activeObj = mActiveAdapter.getItem(position);
					if (activeObj != null) {
						GroupInfoActivity.startActivity(getActivity(), activeObj.getImGroupId());
					}
				}
			}
		});
		mActiveListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});

		noresultTV = (TextView) rootView.findViewById(R.id.lv_active_noresultTV);
		serchLL = (LinearLayout) rootView.findViewById(R.id.activity_serchLL);
		edittext = (ClearEditText) rootView.findViewById(R.id.activity_serchEdit);
		cancelText = (TextView) rootView.findViewById(R.id.activity_serchCancelTv);
		titleLL = (PercentLinearLayout) rootView.findViewById(R.id.homepage_titleLL);
		titleLL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mDynamicList.isShown()) {
					mDynamicList.setSelection(0);
				}
				if (mAttentionList.isShown()) {
					mAttentionList.setSelection(0);
				}
			}
		});

		serchLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				serchLL.setVisibility(View.GONE);
				headerView.setVisibility(View.GONE);
				titleLL.setVisibility(View.GONE);
				edittext.setVisibility(View.VISIBLE);
				mSearchActives.clear();
				mActiveAdapter.setData(mSearchActives);
				cancelText.setVisibility(View.VISIBLE);
				edittext.requestFocus();
				showkeyboard();
				mainListener.startSearch();
				mActiveListView.setCanPullDown(true);
				mActiveListView.setCanPullUp(true);
				isSearch = true;
			}
		});
		edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cancelText.isShown() && s.length() == 0) {
					activeRefresh.setVisibility(View.GONE);
					noresultTV.setVisibility(View.GONE);
				}
				if (!CommonTextUtils.isEmpty(edittext.getText().toString())) {
					activitySearchPageIndex = 1;
					sendMessage(YSMSG.REQ_SEARCHACTIVITY, activitySearchPageIndex, 0, edittext.getText().toString());
				}

			}
		});
		cancelText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				mainListener.cancelSearch();
				noresultTV.setVisibility(View.GONE);
				cancelText.setVisibility(View.GONE);
				edittext.setVisibility(View.GONE);
				activeRefresh.setVisibility(View.VISIBLE);
				headerView.setVisibility(View.VISIBLE);
				mActiveAdapter.setData(mAllActives);
				titleLL.setVisibility(View.VISIBLE);
				serchLL.setVisibility(View.VISIBLE);
				edittext.setText("");
				edittext.clearFocus();
				mActiveListView.setCanPullDown(true);
				mActiveListView.setCanPullUp(true);
				isSearch = false;
			}
		});
	}

	private void showkeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edittext, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			View localView = getActivity().getCurrentFocus();
			if (localView != null && localView.getWindowToken() != null) {
				IBinder windowToken = localView.getWindowToken();
				inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
			}
		}
	}

	protected void setActivedata() {
		if (mAllActives == null) {
			mAllActives = new ArrayList<ActivityObject>();
		}
		mSearchActives = new ArrayList<ActivityObject>();
		if (mAllActives.size() == 0) {
			activityPageIndex = 1;
			sendMessage(YSMSG.REQ_GET_LOCALACTIVITY, activityPageIndex, 0, null);
		}
	}

	Handler hander = new Handler();

	private void setDynamicData() {
		MobclickAgent.onEvent(getActivity(), getString(R.string.umeng_feedback_1));
		if (mDynamics == null) {
			mDynamics = new ArrayList<DynamicObject>();
		}
		if (mDynamics.size() == 0) {
			dynamicPageIndex = 1;
			sendMessage(YSMSG.REQ_GETMYDYNAMIC, dynamicPageIndex, 0, null);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setDynamicData();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void setLikesData() {
		if (mLikes == null) {
			mLikes = new ArrayList<DynamicObject>();
		}
		if (mLikes.size() == 0) {
			likePageIndex = 1;
			sendMessage(YSMSG.REQ_GETMYLIKES, likePageIndex, 0, null);
		}
		noattentionLL = (PercentLinearLayout) rootView.findViewById(R.id.ll_home_no_attentionDynamic);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		LogUtils.i("isVisibleToUser", String.valueOf(isVisibleToUser));
		if (isVisibleToUser == true) {
			LogUtils.i("isVisibleToUser", String.valueOf(isVisibleToUser));
		} else if (isVisibleToUser == false) {

		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_SEARCHACTIVITY :
				if (activeRefresh.isShown()) {
					if (msg.arg1 == 200) {
						activeRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						if (msg.obj != null) {
							String result = (String) msg.obj;
							try {
								JSONObject json = new JSONObject(result);
								JSONArray array = json.getJSONArray("List");
								boolean hasmore = json.getBoolean("HasMore");
								if (hasmore) {
									mActiveListView.setCanPullUp(true);
								} else {
									mActiveListView.setCanPullUp(false);
								}
								if (activitySearchPageIndex == 1) {
									mSearchActives.clear();
								}
								activitySearchPageIndex++;
								LngLatObject LngLatObj;
								for (int i = 0; i < array.length(); i++) {
									ActivityObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
											ActivityObject.class);
									LngLatObj = new LngLatObject();
									JSONObject lnglatJson = array.getJSONObject(i).getJSONObject("LocateAddress");
									LngLatObj.setLng(lnglatJson.getString("Lng"));
									LngLatObj.setLat(lnglatJson.getString("Lat"));
									actObj.setLnglatObj(LngLatObj);
									mSearchActives.add(actObj);
								}
								mActiveAdapter.setData(mSearchActives);
								if (mSearchActives.size() > 0) {
									activeRefresh.setVisibility(View.VISIBLE);
									noresultTV.setVisibility(View.GONE);
								} else {
									activeRefresh.setVisibility(View.GONE);
									noresultTV.setVisibility(View.VISIBLE);
								}
								if (cancelText.isShown() && edittext.getText().length() == 0) {
									activeRefresh.setVisibility(View.GONE);
									noresultTV.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (java.lang.InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					} else {
						activeRefresh.refreshFinish(PullToRefreshLayout.FAIL);
					}
				}
				break;
			case YSMSG.RESP_GET_LOCALACTIVITY :
				if (activeRefresh.isShown()) {
					if (msg.arg1 == 200) {
						activeRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
						if (msg.obj != null) {

							String result = (String) msg.obj;
							try {
								JSONObject json = new JSONObject(result);
								JSONArray array = json.getJSONArray("List");
								boolean hasmore = json.getBoolean("HasMore");
								if (hasmore) {
									mActiveListView.setCanPullUp(true);
								} else {
									mActiveListView.setCanPullUp(false);
								}
								if (activityPageIndex == 1) {
									mAllActives.clear();
								}
								activityPageIndex++;
								JSONObject actjson;
								LngLatObject LngLatObj;
								for (int i = 0; i < array.length(); i++) {
									actjson = array.getJSONObject(i);
									ActivityObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
											ActivityObject.class);
									LngLatObj = new LngLatObject();
									JSONObject lnglatJson = actjson.getJSONObject("LocateAddress");
									LngLatObj.setLng(lnglatJson.getString("Lng"));
									LngLatObj.setLat(lnglatJson.getString("Lat"));
									actObj.setLnglatObj(LngLatObj);
									mAllActives.add(actObj);
								}
								mActiveAdapter.setData(mAllActives);
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (java.lang.InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					} else {
						mActiveListView.setCanPullUp(false);
						activeRefresh.refreshFinish(PullToRefreshLayout.FAIL);
					}
				}

				break;
			case YSMSG.RESP_GETMYDYNAMIC :
				if (dynamicRefresh.isShown()) {
					if (msg.arg1 == 200) {
						dynamicRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
						if (msg.obj != null) {
							String result = (String) msg.obj;

							try {
								JSONObject json = new JSONObject(result);
								JSONArray array = json.getJSONArray("List");
								boolean hasmore = json.getBoolean("HasMore");
								if (hasmore) {
									mDynamicList.setCanPullUp(true);
								} else {
									mDynamicList.setCanPullUp(false);
								}
								if (dynamicPageIndex == 1) {
									mDynamics.clear();
								}
								dynamicPageIndex++;
								JSONObject petjson;
								ArrayList<String> ps;
								LngLatObject LngLatObj;
								ArrayList<AccountObject> likeMebers;
								for (int i = 0; i < array.length(); i++) {
									String dStr = array.getString(i);
									DynamicObject actObj = OJMFactory.createOJM().fromJson(dStr, DynamicObject.class);
									petjson = new JSONObject(dStr);
									PetObject petObj = OJMFactory.createOJM().fromJson(petjson.getString("Pet"),
											PetObject.class);
									actObj.setPet(petObj);

									AccountDetailObject account = OJMFactory.createOJM().fromJson(
											petjson.getString("Member"), AccountDetailObject.class);
									actObj.setAccountObj(account);

									JSONArray photos = petjson.getJSONArray("Photos");
									ps = new ArrayList<String>();
									for (int j = 0; j < photos.length(); j++) {
										ps.add(photos.getString(j));
									}
									actObj.setPetPhotos(ps);
									LngLatObj = new LngLatObject();
									JSONObject lnglatJson = petjson.getJSONObject("LocateAddress");
									LngLatObj.setLng(lnglatJson.getString("Lng"));
									LngLatObj.setLat(lnglatJson.getString("Lat"));
									actObj.setAdressObject(LngLatObj);

									JSONArray likemeberJson = petjson.getJSONArray("LikeMembers");
									likeMebers = new ArrayList<AccountObject>();
									for (int k = 0; k < likemeberJson.length(); k++) {
										AccountObject meberObj = OJMFactory.createOJM().fromJson(
												likemeberJson.getString(k), AccountObject.class);
										likeMebers.add(meberObj);
									}
									actObj.setlMembers(likeMebers);
									mDynamics.add(actObj);
								}
								mDynamicAdapter.setData(mDynamics);
								// mDynamicList.smoothScrollToPosition(0);
								// mDynamicList.stopRefresh();
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (java.lang.InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					} else {
						dynamicRefresh.setCanPullUp(false);
						dynamicRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);

					}
				}

				break;
			case YSMSG.RESP_GETMYLIKES :
				if (attentionRefresh.isShown()) {
					if (msg.arg1 == 200) {
						if (msg.obj != null) {
							attentionRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
							String result = (String) msg.obj;
							try {
								JSONObject json = new JSONObject(result);
								JSONArray array = json.getJSONArray("List");
								boolean hasmore = json.getBoolean("HasMore");
								if (hasmore) {
									mAttentionList.setCanPullUp(true);
								} else {
									mAttentionList.setCanPullUp(false);
								}
								if (likePageIndex == 1) {
									mLikes.clear();
								}
								likePageIndex++;
								JSONObject petjson;
								ArrayList<String> ps;
								LngLatObject LngLatObj;
								ArrayList<AccountObject> likeMebers;
								for (int i = 0; i < array.length(); i++) {
									String dStr = array.getString(i);
									DynamicObject actObj = OJMFactory.createOJM().fromJson(dStr, DynamicObject.class);
									petjson = new JSONObject(dStr);
									PetObject petObj = OJMFactory.createOJM().fromJson(petjson.getString("Pet"),
											PetObject.class);
									actObj.setPet(petObj);

									AccountDetailObject account = OJMFactory.createOJM().fromJson(
											petjson.getString("Member"), AccountDetailObject.class);
									actObj.setAccountObj(account);

									JSONArray photos = petjson.getJSONArray("Photos");
									ps = new ArrayList<String>();
									for (int j = 0; j < photos.length(); j++) {
										ps.add(photos.getString(j));
									}
									actObj.setPetPhotos(ps);
									LngLatObj = new LngLatObject();
									JSONObject lnglatJson = petjson.getJSONObject("LocateAddress");
									LngLatObj.setLng(lnglatJson.getString("Lng"));
									LngLatObj.setLat(lnglatJson.getString("Lat"));
									actObj.setAdressObject(LngLatObj);

									JSONArray likemeberJson = petjson.getJSONArray("LikeMembers");
									likeMebers = new ArrayList<AccountObject>();
									for (int k = 0; k < likemeberJson.length(); k++) {
										AccountObject meberObj = OJMFactory.createOJM().fromJson(
												likemeberJson.getString(k), AccountObject.class);
										likeMebers.add(meberObj);
									}
									actObj.setlMembers(likeMebers);
									mLikes.add(actObj);
								}
								mAttentionAdapter.setData(mLikes);
								if (mLikes.size() > 0) {
									noattentionLL.setVisibility(View.GONE);
									attentionRefresh.setVisibility(View.VISIBLE);
								} else {
									noattentionLL.setVisibility(View.VISIBLE);
									attentionRefresh.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (java.lang.InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					} else {
						attentionRefresh.setCanPullUp(false);
						attentionRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);
					}
				}

				break;
			case YSMSG.RESP_MAINBLOGCANCELLIKE :
				if (ActivityUtils.isActivityForeground(getActivity(), getActivity().getClass())) {
					if (msg.arg1 == 200) {
						if (llHomeDynamic.isShown()) {
							mDynamicAdapter.setCancelLike(curDynamicObj.getMicroblogId());
						} else {
							mAttentionAdapter.setCancelLike(curDynamicObj.getMicroblogId());
						}
					} else {

					}
					isZaning = false;
				}
				break;
			case YSMSG.RESP_MAINBLOGLIKE :
				if (ActivityUtils.isActivityForeground(getActivity(), getActivity().getClass())) {
					if (msg.arg1 == 200) {
						if (llHomeDynamic.isShown()) {
							mDynamicAdapter.setLike(curDynamicObj.getMicroblogId());
						} else {
							mAttentionAdapter.setLike(curDynamicObj.getMicroblogId());
						}
					} else {

					}
					isZaning = false;
				}
				break;
			case YSMSG.RESP_FOLLOW :
				if (msg.arg1 == 200) {
					if (curDynamicObj != null) {
						mDynamicAdapter.setFollowed(curDynamicObj.getAccountObj().getMemberId());
					}
				}
				break;
			case YSMSG.RESP_MAIN_FOLLOW :
				LogUtils.i("RESP_MAIN_FOLLOW", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String meberid = String.valueOf(msg.obj);
					mDynamicAdapter.setFollowed(meberid);
				}
				break;
			case YSMSG.RESP_MAIN_CANCELFOLLOW :
				LogUtils.i("RESP_MAIN_CANCELFOLLOW", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String meberid = String.valueOf(msg.obj);
					mDynamicAdapter.setCancelFollowed(meberid);
				}
				break;
			case YSMSG.RESP_DETAILTOMAINLIKE :
				LogUtils.i("RESP_DETAILTOMAINLIKE", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String id = String.valueOf(msg.obj);
					mDynamicAdapter.setLike(id);
					mAttentionAdapter.setLike(id);
				}
				break;
			case YSMSG.RESP_DETAILTOMAINCANCELLIKE :
				LogUtils.i("RESP_DETAILTOMAINCANCELLIKE", String.valueOf(msg.obj));
				if (msg.obj != null) {
					String id = String.valueOf(msg.obj);
					mDynamicAdapter.setCancelLike(id);
					mAttentionAdapter.setCancelLike(id);
				}
				break;
			case YSMSG.REQ_MAIN_REFRESH :
				LogUtils.i("HomePageFragment", "main_refresh");
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						dynamicPageIndex = 1;
						sendMessage(YSMSG.REQ_GETMYDYNAMIC, dynamicPageIndex, 0, null);
					}
				}, 500);
				break;
			case YSMSG.REQ_MAIN_DELETE :
				final String dynamicId = String.valueOf(msg.obj);
				LogUtils.i("HomePageFragment", "main_delete:  " + dynamicId);
				mDynamicAdapter.removeDynamic(dynamicId);
				mAttentionAdapter.removeDynamic(dynamicId);
				break;
		}
	}

	@Override
	public void headerImageClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			AccountCenterActivity.startActivity(getActivity(), obj.getAccountObj().getMemberId());
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void headerFollowImageClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			if (!obj.isIsFollowed()) {
				curDynamicObj = obj;
				sendMessage(YSMSG.REQ_FOLLOW, 0, 0, obj.getAccountObj().getMemberId());
			}
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void userImageClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			AccountCenterActivity.startActivity(getActivity(), obj.getAccountObj().getMemberId());
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}

	}

	@Override
	public void userFollowImageClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			curDynamicObj = obj;
			if (!obj.isIsFollowed()) {
				sendMessage(YSMSG.REQ_FOLLOW, 0, 0, obj.getAccountObj().getMemberId());
			}
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void viewPaperClick(DynamicObject obj) {
		if (obj != null) {
			VPagerImageDetailsActivity.startActivity(getActivity(), obj.getPetPhotos(), 0);
		}
	}

	@Override
	public void petClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			GetPetObject getPet = new GetPetObject();
			getPet.setMeberId(obj.getAccountObj().getMemberId());
			getPet.setPetId(obj.getPet().getPetId());
			PetCenterActivity.startActivity(getActivity(), getPet);
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void moreZanClick(DynamicObject obj) {
		ZanListActivity.startActivity(getActivity(), obj.getMicroblogId());
	}

	boolean isZaning = false;

	@Override
	public void dianZanOrNotClick(DynamicObject obj) {
		if (!isZaning) {
			curDynamicObj = obj;
			isZaning = true;
			if (obj.isIsLiked()) {
				sendMessage(YSMSG.REQ_MAINBLOGCANCELLIKE, 0, 0, obj.getMicroblogId());
			} else {
				sendMessage(YSMSG.REQ_MAINBLOGLIKE, 0, 0, obj.getMicroblogId());
			}
		}
	}

	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow;

	@Override
	public void shareMoreClick(DynamicObject obj) {
		// // 实例化SelectPicPopupWindow
		// menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
		// // 显示窗口
		// menuWindow.showAtLocation(getActivity().findViewById(R.id.mainRL),
		// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
		// 0, 0); // 设置layout在PopupWindow中显示的位置

		// mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
		// SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
		// mController.openShare(getActivity(), false);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
				case R.id.img_wx :
					directShare(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.img_wb :
					directShare(SHARE_MEDIA.SINA);
					break;
				case R.id.img_friend :
					directShare(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				default :
					break;
			}

		}
	};

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
		weixinContent.setTitle("友盟社会化分享组件-微信");
		weixinContent.setTargetUrl("http://www.umeng.com/social");
		weixinContent.setShareMedia(new UMImage(getActivity(), R.drawable.ic_launcher));
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
		circleMedia.setTitle("友盟社会化分享组件-朋友圈");
		circleMedia.setShareMedia(new UMImage(getActivity(), R.drawable.ic_launcher));
		// circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl("http://www.umeng.com/social");
		mController.setShareMedia(circleMedia);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-新浪微博。http://www.umeng.com/social");
		sinaContent.setShareImage(new UMImage(getActivity(), R.drawable.ic_launcher));
		mController.setShareMedia(sinaContent);
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1104434109";
		String appKey = "jHyiWS2aV2VgcZBnfjqUrXEB";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx8da4d245fba00a3c";
		String appSecret = "b59a9ead7de0b2a917be347bfd95c387";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
	 */
	public void directShare(SHARE_MEDIA mPlatform) {
		mController.directShare(getActivity(), mPlatform, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					YSToast.showToast(getActivity(), getString(R.string.share_success));
				} else {
					YSToast.showToast(getActivity(), getString(R.string.share_fail));
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void mainClick(DynamicObject obj) {
		LogUtils.i("mainClick", "mainClick");
		if (CoreModel.getInstance().getMyself() != null) {
			curDynamicObj = obj;
			PetDiaryDetailsActivity.startActivity(getActivity(), obj, false, null);
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId,
			boolean currentlySticky) {
		if (llHomeDynamic.isShown()) {
			DynamicObject obj = mDynamicAdapter.getItem(itemPosition);
			PetDiaryDetailsActivity.startActivity(getActivity(), obj, false, null);
		} else {
			DynamicObject obj = mAttentionAdapter.getItem(itemPosition);
			PetDiaryDetailsActivity.startActivity(getActivity(), obj, false, null);
		}
	}

	@Override
	public void commentLLClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			curDynamicObj = obj;
			PetDiaryDetailsActivity.startActivity(getActivity(), obj, true, null);
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void zanlistClick(DynamicObject obj) {
		if (CoreModel.getInstance().getMyself() != null) {
			curDynamicObj = obj;
			PetDiaryDetailsActivity.startActivity(getActivity(), obj, false, null);
		} else {
			LoginActivity.startActivity(getActivity(), "");
		}
	}

	@Override
	public void backClick() {
		hideSoftKeyboard();
		mainListener.cancelSearch();
		noresultTV.setVisibility(View.GONE);
		cancelText.setVisibility(View.GONE);
		edittext.setVisibility(View.GONE);
		activeRefresh.setVisibility(View.VISIBLE);
		headerView.setVisibility(View.VISIBLE);
		mActiveAdapter.setData(mAllActives);
		titleLL.setVisibility(View.VISIBLE);
		serchLL.setVisibility(View.VISIBLE);
		edittext.setText("");
		edittext.clearFocus();
		mActiveListView.setCanPullDown(true);
		mActiveListView.setCanPullUp(true);
		isSearch = false;
	}

}