package mobi.jzcx.android.chongmi.ui.register;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.adapter.RecommendAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.MainActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RegisteAttentionActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	int index = 1;
	private ListView lvAtttion;
	private RecommendAdapter mAdapter;
	ArrayList<AccountObject> accountList;
	ArrayList<String> stringlist;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, RegisteAttentionActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeattention);
		initView();

	}

	private void initView() {
		initTitleBar();
		initList();
	}

	private void initList() {
		lvAtttion = (ListView) findViewById(R.id.attention_list);
		accountList = new ArrayList<AccountObject>();
		stringlist = new ArrayList<String>();
		mAdapter = new RecommendAdapter(this);
		lvAtttion.setAdapter(mAdapter);
		lvAtttion.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AccountObject account = mAdapter.getItem(position);
				String memberId = account.getMemberId();
				if (stringlist.contains(memberId)) {
					stringlist.remove(memberId);
				} else {
					stringlist.add(memberId);
				}
				mAdapter.setSelect(stringlist);
			}
		});
		sendMessage(YSMSG.REQ_GETRECOMMENDMEBERS, index, 0, null);
		showWaitingDialog();
		TextView refreshTV = (TextView) findViewById(R.id.tv_refresh);
		refreshTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stringlist.clear();
				index++;
				sendMessage(YSMSG.REQ_GETRECOMMENDMEBERS, index, 1, null);
				showWaitingDialog();
			}
		});
		Button attentionBtn = (Button) findViewById(R.id.btn_attention);
		attentionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (stringlist.size() != 0) {
					String attention = getattention();
					sendMessage(YSMSG.REQ_ATTENTION, 0, 0, attention);
					showWaitingDialog();
				} else {
					YSToast.showToast(mActivity, getString(R.string.toast_noselectattention_text));
				}
			}
		});
	}

	private String getattention() {
		String str = "";
		for (int i = 0; i < stringlist.size(); i++) {
			if (i == stringlist.size() - 1) {
				str = str + stringlist.get(i);
			} else {
				str = str + stringlist.get(i) + ",";
			}

		}
		return str;
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(mActivity);
		mTitleBar.mTitle.setText(R.string.registe_attention_ttb_title);
		mTitleBar.mRightImg.setVisibility(View.GONE);
		mTitleBar.mRightTv.setVisibility(View.VISIBLE);
		mTitleBar.mRightTv.setText(R.string.title_next_skip);
		mTitleBar.titleLeftRL.setVisibility(View.GONE);
		// mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.startActivity(mActivity);
				finish();
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETRECOMMENDMEBERS : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							accountList.clear();
							ArrayList<PetObject> pets;
							LngLatObject lnglatObj;
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
								lnglatObj = new LngLatObject();
								lnglatObj = OJMFactory.createOJM().fromJson(
										array.getJSONObject(i).getString("LocateAddress"), LngLatObject.class);
								actObj.setLocateAddress(lnglatObj);

								accountList.add(actObj);
							}
							mAdapter.setData(accountList);
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
			case YSMSG.RESP_ATTENTION : {
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					MainActivity.startActivity(mActivity);
					finish();
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
			default :
				break;
		}
	}

}
