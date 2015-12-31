package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMoreLikeMebersObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.adapter.HomeZanAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.view.CanPullListView;
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

/**
 * @author wei 点赞用户列表
 * 
 */
public class ZanListActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	private CanPullListView mZanList;
	private HomeZanAdapter mZanAdapter;
	private ArrayList<AccountObject> accountList;
	static String mId;

	public static void startActivity(Context context, String mId) {
		ActivityUtils.startActivity(context, ZanListActivity.class);
		ZanListActivity.mId = mId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zan_list);
		initView();
		initData();
	}

	PullToRefreshLayout refresh;

	private void initView() {
		initTitleBar();
		refresh = (PullToRefreshLayout) findViewById(R.id.refresh_zanlist);
		refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				GetMoreLikeMebersObject getmoreObj = new GetMoreLikeMebersObject();
				getmoreObj.setId(mId);
				getmoreObj.setIndex(pageindex);
				sendMessage(YSMSG.REQ_BLOG_GETLIKEMEBERSMORE, 0, 0, getmoreObj);
			}
		});
		mZanList = (CanPullListView) findViewById(R.id.lv_zan);
		mZanList.setCanPullDown(false);
		mZanAdapter = new HomeZanAdapter(this);
		mZanList.setAdapter(mZanAdapter);

		mZanList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AccountObject actObj = mZanAdapter.getItem(position);
				if (actObj != null) {
					AccountCenterActivity.startActivity(mActivity, actObj.getMemberId());
				}
			}
		});
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.zanlist_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZanListActivity.this.finish();
			}
		});
	}

	private void initData() {
		accountList = new ArrayList<AccountObject>();
		GetMoreLikeMebersObject getmoreObj = new GetMoreLikeMebersObject();
		getmoreObj.setId(mId);
		getmoreObj.setIndex(pageindex);
		sendMessage(YSMSG.REQ_BLOG_GETLIKEMEBERSMORE, 0, 0, getmoreObj);
	}

	int pageindex = 1;

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_BLOG_GETLIKEMEBERSMORE :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							boolean hasMore = json.getBoolean("HasMore");
							JSONArray array = json.getJSONArray("List");
							if (hasMore) {
								refresh.setCanPullUp(true);
								mZanList.setCanPullUp(true);
							} else {
								refresh.setCanPullUp(false);
								mZanList.setCanPullUp(false);
							}
							if (pageindex == 1) {
								accountList.clear();
							}
							pageindex++;
							refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
							for (int i = 0; i < array.length(); i++) {
								AccountObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AccountObject.class);
								if (!CommonTextUtils.isEmpty(array.getJSONObject(i).getString("Pets"))) {
									JSONArray petsObj = array.getJSONObject(i).getJSONArray("Pets");
									ArrayList<PetObject> petList = new ArrayList<PetObject>();
									for (int j = 0; j < petsObj.length(); j++) {
										PetObject pet = OJMFactory.createOJM().fromJson(petsObj.getString(j),
												PetObject.class);
										petList.add(pet);
									}
									actObj.setPetList(petList);
								}

								// LngLatObject LngLatObj = new LngLatObject();
								// JSONObject lnglatJson =
								// array.getJSONObject(i).getJSONObject("LocateAddress");
								// LngLatObj.setLng(lnglatJson.getString("Lng"));
								// LngLatObj.setLat(lnglatJson.getString("Lat"));
								// actObj.setLocateAddress(LngLatObj);

								accountList.add(actObj);
							}
							mZanAdapter.updateList(accountList);
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
					mZanList.setCanPullUp(false);
					refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
		}
	}
}