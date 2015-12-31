package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.adapter.AccountPetAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyPetActivity extends BaseExActivity implements OnClickListener, OnItemClickListener {
	protected TitleBarHolder mTitleBar = null;
	private ListView petlistView;
	AccountPetAdapter petAdapter;
	ArrayList<PetObject> petList;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, MyPetActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypet);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sendEmptyMessage(YSMSG.REQ_GETMYPETS);
	}

	private void initView() {
		initTitleBar();
		petlistView = (ListView) findViewById(R.id.mypet_List);
		petAdapter = new AccountPetAdapter(mActivity);
		petlistView.setAdapter(petAdapter);
		petList = new ArrayList<PetObject>();
		petlistView.setOnItemClickListener(this);
		PercentLinearLayout addpetTV = (PercentLinearLayout) findViewById(R.id.mypet_addpetLL);
		addpetTV.setOnClickListener(this);

	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.mypet_ttb_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyPetActivity.this.finish();
			}
		});
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mypet_addpetLL :
				AddPetActivity.startActivity(mActivity, false);
				break;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMYPETS :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("Pets");
							petList.clear();
							if (!CommonTextUtils.isEmpty(json.getString("Pets"))) {
								for (int i = 0; i < array.length(); i++) {
									PetObject actObj = OJMFactory.createOJM().fromJson(array.getString(i), PetObject.class);
									petList.add(actObj);
								}
							}
							petAdapter.setData(petList);
							setHeightListVeiw(petList.size());
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (petAdapter.getItem(position) != null) {
			PetObject pet = petAdapter.getItem(position);
			EditPetActivity.startActivity(mActivity, pet);
			// GetPetObject getpetObj = new GetPetObject();
			// getpetObj.setMeberId(CoreModel.getInstance().getMyself().getMeberId());
			// getpetObj.setPetId(pet.get_id());
			// PetCenterActivity.startActivity(mActivity, getpetObj);
		}

	}

	private void setHeightListVeiw(int count) {
		int screensize = CommonUtils.getScreenWidth(mActivity);
		ViewGroup.LayoutParams params = petlistView.getLayoutParams();
		params.height = 1920 * screensize / 10000 * count;
		petlistView.setLayoutParams(params);
	}
}
