package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.GetFansObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.adapter.AttentionAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.view.CanPullListView;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout;
import mobi.jzcx.android.core.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AttentionActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	private CanPullListView attentionListView;
	static boolean getFans = false;
	AttentionAdapter adapter;
	ArrayList<AccountObject> accountList;
	static String meberId;
	PullToRefreshLayout refresh;

	public static void startActivity(Context context, boolean getFans, String meberid) {
		ActivityUtils.startActivity(context, AttentionActivity.class);
		AttentionActivity.getFans = getFans;
		AttentionActivity.meberId = meberid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attention);
		initView();

	}

	private void initView() {
		initTitleBar();
		refresh = (PullToRefreshLayout) findViewById(R.id.refresh_actattention);
		refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				pageIndex++;
				getFansObj.setPageindex(String.valueOf(pageIndex));
				if (getFans) {
					sendMessage(YSMSG.REQ_GETMYFANS, 0, 0, getFansObj);
				} else {
					sendMessage(YSMSG.REQ_GETMYFOLLOW, 0, 0, getFansObj);
				}
			}
		});

		attentionListView = (CanPullListView) findViewById(R.id.lv_actattention);
		attentionListView.setCanPullDown(false);
		adapter = new AttentionAdapter(mActivity);
		attentionListView.setAdapter(adapter);
		attentionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtils.i("position", position + "");
				AccountObject account = adapter.getItem(position);
				if (account != null) {
					AccountCenterActivity.startActivity(mActivity, account.getMemberId());
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	GetFansObject getFansObj;
	String time;
	View fansemptyView;
	View attentionemptyView;
	int pageIndex = 1;

	private void initData() {
		fansemptyView = findViewById(R.id.actfans_emptyLL);
		attentionemptyView = findViewById(R.id.actattention_emptyLL);
		accountList = new ArrayList<AccountObject>();
		getFansObj = new GetFansObject();
		getFansObj.setMeberId(meberId);
		pageIndex = 1;
		getFansObj.setPageindex(String.valueOf(pageIndex));
		if (getFans) {
			sendMessage(YSMSG.REQ_GETMYFANS, 0, 0, getFansObj);
		} else {
			sendMessage(YSMSG.REQ_GETMYFOLLOW, 0, 0, getFansObj);
		}
		showWaitingDialog();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		if (getFans) {
			mTitleBar.mTitle.setText(getString(R.string.attentioned_ttb_title));
		} else {
			mTitleBar.mTitle.setText(getString(R.string.attention_ttb_title));
		}
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AttentionActivity.this.finish();
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMYFANS :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					// ,"HasMore":false,"List":[]
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (hasmore) {
								refresh.setCanPullUp(true);
								attentionListView.setCanPullUp(true);
							} else {
								refresh.setCanPullUp(false);
								attentionListView.setCanPullUp(false);
							}
							if (array.length() == 0) {
								fansemptyView.setVisibility(View.VISIBLE);
								refresh.setVisibility(View.GONE);
							} else {
								fansemptyView.setVisibility(View.GONE);
								refresh.setVisibility(View.VISIBLE);
							}
							refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
							ArrayList<PetObject> pets;
							for (int i = 0; i < array.length(); i++) {
								AccountObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AccountObject.class);
								if (!CommonTextUtils.isEmpty(array.getJSONObject(i).getString("Pets"))) {
									JSONArray petarray = array.getJSONObject(i).getJSONArray("Pets");
									pets = new ArrayList<PetObject>();
									for (int j = 0; j < petarray.length(); j++) {
										PetObject pet = OJMFactory.createOJM().fromJson(petarray.getString(j),
												PetObject.class);
										pets.add(pet);
									}
									actObj.setPetList(pets);
								}

//								LngLatObject LngLatObj = new LngLatObject();
//								JSONObject lnglatJson = array.getJSONObject(i).getJSONObject("LocateAddress");
//								LngLatObj.setLng(lnglatJson.getString("Lng"));
//								LngLatObj.setLat(lnglatJson.getString("Lat"));
//								actObj.setLocateAddress(LngLatObj);

								accountList.add(actObj);
							}
							adapter.setData(accountList);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (java.lang.InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					refresh.setCanPullUp(false);
					attentionListView.setCanPullUp(false);
					refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
			case YSMSG.RESP_GETMYFOLLOW :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							boolean hasmore = json.getBoolean("HasMore");
							if (array.length() == 0) {
								attentionemptyView.setVisibility(View.VISIBLE);
								refresh.setVisibility(View.GONE);
							} else {
								attentionemptyView.setVisibility(View.GONE);
								refresh.setVisibility(View.VISIBLE);
							}
							if (hasmore) {
								refresh.setCanPullUp(true);
								attentionListView.setCanPullUp(true);
							} else {
								refresh.setCanPullUp(false);
								attentionListView.setCanPullUp(false);
							}
							refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
							ArrayList<PetObject> pets;
							for (int i = 0; i < array.length(); i++) {
								AccountObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AccountObject.class);
								if (!CommonTextUtils.isEmpty(array.getJSONObject(i).getString("Pets"))) {
									JSONArray petarray = array.getJSONObject(i).getJSONArray("Pets");
									pets = new ArrayList<PetObject>();
									for (int j = 0; j < petarray.length(); j++) {
										PetObject pet = OJMFactory.createOJM().fromJson(petarray.getString(j),
												PetObject.class);
										pets.add(pet);
									}
									actObj.setPetList(pets);
								}
//								LngLatObject LngLatObj = new LngLatObject();
//								JSONObject lnglatJson = array.getJSONObject(i).getJSONObject("LocateAddress");
//								LngLatObj.setLng(lnglatJson.getString("Lng"));
//								LngLatObj.setLat(lnglatJson.getString("Lat"));
//								actObj.setLocateAddress(LngLatObj);

								accountList.add(actObj);
							}
							adapter.setData(accountList);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (java.lang.InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					refresh.setCanPullUp(false);
					attentionListView.setCanPullUp(false);
					refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
		}
	}

}
