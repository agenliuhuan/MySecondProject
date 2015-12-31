package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.BindPetObject;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.ui.adapter.HardwarePetListAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.mine.AddPetActivity;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HardwarePetListActivity extends BaseExActivity implements UnbindListener {
	protected TitleBarHolder mTitleBar = null;
	ListView listview;
	HardwarePetListAdapter petlistAdapter;
	ArrayList<PetBindObject> petList;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, HardwarePetListActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hardwarepetlist);
		initView();
	}

	protected void onResume() {
		super.onResume();
		sendEmptyMessage(YSMSG.REQ_PETBINDLIST);
	}

	private void initView() {
		initTitleBar();
		listview = (ListView) findViewById(R.id.hardwarepetlist_Listview);
		PercentLinearLayout bindLL = (PercentLinearLayout) findViewById(R.id.hardwarepetlist_bindLL);
		bindLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddPetActivity.startActivity(mActivity, false);
			}
		});
		petlistAdapter = new HardwarePetListAdapter(mActivity);
		petList = new ArrayList<PetBindObject>();
		petlistAdapter.setUbbindlistener(this);
		listview.setAdapter(petlistAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (petlistAdapter != null) {
					PetBindObject pet = petlistAdapter.getItem(position);
					if (pet.isIsBind()) {
						PetStepsnumActivity.startActivity(mActivity, pet);
					} else {
						if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
							YSToast.showToast(mActivity, getString(R.string.toast_bluetooth_enable));
						} else {
							ScanBluetoothActivity.startActivity(mActivity, pet);
						}
					}
				}
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_PETBINDLIST :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							petList.clear();
							for (int i = 0; i < array.length(); i++) {
								PetBindObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										PetBindObject.class);
								petList.add(actObj);
							}
							petlistAdapter.setData(petList);
							setHeightListVeiw(petList.size());
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (java.lang.InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_UNBINDPETCOLLAR :
				if (msg.arg1 == 200) {
					sendEmptyMessage(YSMSG.REQ_PETBINDLIST);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	private void setHeightListVeiw(int count) {
		int screensize = CommonUtils.getScreenWidth(mActivity);
		ViewGroup.LayoutParams params = listview.getLayoutParams();
		params.height = 1667 * screensize / 10000 * count;
		listview.setLayoutParams(params);
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.hardware_petlist_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HardwarePetListActivity.this.finish();
			}
		});
	}

	public void unbindClick(PetBindObject pet) {
		if (pet.isIsBind()) {
			BindPetObject bindpetObj = new BindPetObject();
			bindpetObj.setPetId(pet.getPetId());
			bindpetObj.setPetCollarId(pet.getPetCollarId());
			sendMessage(YSMSG.REQ_UNBINDPETCOLLAR, 0, 0, bindpetObj);
		}
	}

}
