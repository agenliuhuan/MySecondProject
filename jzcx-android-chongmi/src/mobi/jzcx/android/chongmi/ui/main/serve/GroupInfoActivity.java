package mobi.jzcx.android.chongmi.ui.main.serve;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.ReportObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.adapter.MeberListAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.group.JoinGroupReasonActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.ReportActivity;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.chongmi.view.ReportOrDeletePopupWindow;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.umeng.analytics.MobclickAgent;

public class GroupInfoActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	static String EXTRA_QUEIT = "";
	static String EXTRA_RELOAD = "";
	ActivityObject activeObj = null;
	HorizontalListView meberListview;
	ArrayList<AccountObject> mebersList;
	MeberListAdapter adapter;
	Button joinOrExitBtn;
	static String IMGroupid;
	// ImageView eventIcon;
	TextView dateTv;
	TextView locationTv;
	TextView mebernumTv;
	TextView introTv;
	TextView ctimeTv;

	public static void startActivity(Context context, String IMGroupid) {
		ActivityUtils.startActivity(context, GroupInfoActivity.class);
		GroupInfoActivity.IMGroupid = IMGroupid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sendMessage(YSMSG.REQ_GETACTIVITYBYIMID, 0, 0, IMGroupid);
		showWaitingDialog();
		MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_4));
	}

	private void initView() {
		initTitleBar();
		PercentRelativeLayout mebersRL = (PercentRelativeLayout) findViewById(R.id.event_detail_member);
		dateTv = (TextView) findViewById(R.id.event_detail_date_edt);
		locationTv = (TextView) findViewById(R.id.event_detail_location_edt);
		mebernumTv = (TextView) findViewById(R.id.event_detail_mebernum_edt);
		introTv = (TextView) findViewById(R.id.event_detail_intro_edt);
		ctimeTv = (TextView) findViewById(R.id.event_detail_createtime_edt);
		joinOrExitBtn = (Button) findViewById(R.id.event_detail_join);
		meberListview = (HorizontalListView) findViewById(R.id.event_detail_meberList);
		adapter = new MeberListAdapter(mActivity);
		mebersList = new ArrayList<AccountObject>();
		meberListview.setAdapter(adapter);

		joinOrExitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CoreModel.getInstance().getMyself() != null) {
					if (activeObj.isIsOwner()) {
						showDismissDialog();
					} else if (activeObj.isIsJoin()) {
						showExitDialog();
					} else {
						JoinGroupReasonActivity.startActivity(mActivity, activeObj);
					}
				} else {

				}
			}
		});
		mebersRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupMebersActivity.startActivity(mActivity, activeObj.getActivityId());
			}
		});
		meberListview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				GroupMebersActivity.startActivity(mActivity, activeObj.getActivityId());
				return false;
			}
		});
	}

	private void showDismissDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.dismiss_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendMessage(YSMSG.REQ_DETAIL_DISSMISACTIVITY, 0, 0, activeObj.getActivityId());
						showWaitingDialog();
					}
				});
	}

	private void showExitDialog() {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.exit_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendMessage(YSMSG.REQ_DETAIL_EXITACTIVITY, 0, 0, activeObj.getActivityId());
						showWaitingDialog();
					}
				});
	}

	private void initData() {
		if (activeObj != null) {
			mTitleBar.mTitle.setText(activeObj.getTitle());
			// if (!TextUtils.isEmpty(activeObj.getICOUrl())) {
			// ImageLoaderHelper.displayImage(activeObj.getICOUrl(), eventIcon);
			// }
			if (!TextUtils.isEmpty(activeObj.getAddress()) && activeObj.getLnglatObj() != null) {
				LatLng cp = new LatLng(Double.valueOf(activeObj.getLnglatObj().getLat()), Double.valueOf(activeObj
						.getLnglatObj().getLng()));
				if (App.getInstance().getLnglat() != null) {
					LngLatObject mylnglat = App.getInstance().getLnglat();
					LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()), Double.valueOf(mylnglat.getLng()));
					NumberFormat format = NumberFormat.getNumberInstance();
					format.setMaximumFractionDigits(2);
					String distance = format.format(DistanceUtil.getDistance(cp, mp) / 1000);
					locationTv.setText(activeObj.getAddress() + " | " + distance
							+ getString(R.string.homepage_distance_text));
				}

			}
			if (!TextUtils.isEmpty(activeObj.getBeginTime())) {
				dateTv.setText(activeObj.getBeginTime());
			}
			if (!TextUtils.isEmpty(activeObj.getCreateTime())) {
				ctimeTv.setText(activeObj.getCreateTime());
			}
			mebernumTv.setText(activeObj.getMaxUserCount() + "/" + activeObj.getMemberCount());
			if (!TextUtils.isEmpty(activeObj.getIntro())) {
				introTv.setText(activeObj.getIntro());
			}
			if (activeObj.isIsOwner()) {
				joinOrExitBtn.setText(getString(R.string.eventdetail_dismissevent));
				Date endDate = CommonUtils.getDate(activeObj.getEndTime());
				if (endDate.before(new Date())) {
					joinOrExitBtn.setEnabled(true);
				} else {
					joinOrExitBtn.setEnabled(false);
				}
			} else if (activeObj.isIsJoin()) {
				joinOrExitBtn.setText(getString(R.string.eventdetail_exitevent));
			} else {
				joinOrExitBtn.setText(getString(R.string.eventdetail_joinevent));
			}

			if (activeObj.isIsOwner()) {
				mTitleBar.titleRightRL.setVisibility(View.GONE);
			} else {
				mTitleBar.titleRightRL.setVisibility(View.VISIBLE);
			}

		}
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETACTIVITYBYIMID :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							activeObj = OJMFactory.createOJM().fromJson(json.getString("Activity"),
									ActivityObject.class);
							LngLatObject lnglat = OJMFactory.createOJM().fromJson(
									json.getJSONObject("Activity").getString("LocateAddress"), LngLatObject.class);
							activeObj.setLnglatObj(lnglat);
							initData();
							sendMessage(YSMSG.REQ_GETMEBERS, 0, 0, activeObj.getActivityId());
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
			case YSMSG.RESP_GETMEBERS :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							mebersList.clear();
							ArrayList<PetObject> pets;
							for (int i = 0; i < array.length(); i++) {
								AccountObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AccountObject.class);
								// if
								// (!CommonTextUtils.isEmpty(array.getJSONObject(i).getString("Pets")))
								// {
								// JSONArray petarray =
								// array.getJSONObject(i).getJSONArray("Pets");
								// pets = new ArrayList<PetObject>();
								// for (int j = 0; j < petarray.length(); j++) {
								// PetObject pet =
								// OJMFactory.createOJM().fromJson(petarray.getString(j),
								// PetObject.class);
								// pets.add(pet);
								// }
								// actObj.setPetList(pets);
								// }
								// LngLatObject LngLatObj = new LngLatObject();
								// JSONObject lnglatJson =
								// array.getJSONObject(i).getJSONObject("LocateAddress");
								// LngLatObj.setLng(lnglatJson.getString("Lng"));
								// LngLatObj.setLat(lnglatJson.getString("Lat"));
								// actObj.setLocateAddress(LngLatObj);
								mebersList.add(actObj);
							}
							adapter.updateList(mebersList);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (java.lang.InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case YSMSG.RESP_DETAIL_DISSMISACTIVITY :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					imGroupRefresh();
					GroupInfoActivity.this.finish();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;

			case YSMSG.RESP_DETAIL_EXITACTIVITY :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					imGroupRefresh();
					GroupInfoActivity.this.finish();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	private void imGroupRefresh() {
		IMInitHelper.getInstance().asyncFetchGroupsFromServer(null);
	}

	ReportOrDeletePopupWindow reportOrDelWindow;

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GroupInfoActivity.this.finish();
			}
		});
		mTitleBar.mRightImg.setImageResource(R.drawable.report_img);
		mTitleBar.titleRightRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reportOrDelWindow = new ReportOrDeletePopupWindow(GroupInfoActivity.this, reportOrdelOnClick);
				reportOrDelWindow.setText(getString(R.string.petdiarydetail_comment_report));
				reportOrDelWindow.showAtLocation(GroupInfoActivity.this.findViewById(R.id.event_detailMainRL),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
	}

	private OnClickListener reportOrdelOnClick = new OnClickListener() {

		public void onClick(View v) {
			reportOrDelWindow.dismiss();
			switch (v.getId()) {
				case R.id.reportOrDeleteBtn :
					ReportObject reportObj = new ReportObject();
					reportObj.setActivityId(activeObj.getActivityId());
					ReportActivity.startActivity(mActivity, reportObj);
					break;
				default :
					break;
			}
		}
	};

}
