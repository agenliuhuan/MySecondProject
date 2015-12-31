package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.ui.adapter.AccountPetAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;

public class PetListActivity extends BaseExActivity implements OnItemClickListener {
	static ArrayList<PetObject> list;
	protected TitleBarHolder mTitleBar = null;
	private ListView petlistView;
	AccountPetAdapter petAdapter;
	static String meberId;

	public static void startActivity(Context context, ArrayList<PetObject> petlist, String meberid) {
		ActivityUtils.startActivity(context, PetListActivity.class);
		PetListActivity.list = new ArrayList<PetObject>();
		PetListActivity.list.addAll(petlist);
		PetListActivity.meberId = meberid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petlist);
		initView();
	}

	private void initView() {
		initTitleBar();
		petlistView = (ListView) findViewById(R.id.petList);
		petAdapter = new AccountPetAdapter(mActivity);
		petlistView.setAdapter(petAdapter);
		petAdapter.setData(list);
		petlistView.setOnItemClickListener(this);

	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.petlist_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PetListActivity.this.finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (petAdapter.getItem(position) != null && !CommonTextUtils.isEmpty(meberId)) {
			PetObject pet = petAdapter.getItem(position);
			GetPetObject getpetObj = new GetPetObject();
			getpetObj.setMeberId(meberId);
			getpetObj.setPetId(pet.getPetId());
			PetCenterActivity.startActivity(mActivity, getpetObj);
		}

	}

}
