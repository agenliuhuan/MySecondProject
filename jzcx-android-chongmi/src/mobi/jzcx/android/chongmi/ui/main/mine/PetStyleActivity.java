package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetStyleObject;
import mobi.jzcx.android.chongmi.ui.adapter.PetStyleAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
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
import android.widget.ListView;

public class PetStyleActivity extends BaseExActivity implements OnItemClickListener {
	protected TitleBarHolder mTitleBar;
	PetStyleAdapter adapter;
	ArrayList<PetStyleObject> objList;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, PetStyleActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petstyle);

		initView();
		initData();
	}

	private void initData() {
		sendEmptyMessage(YSMSG.REQ_GETPETCATEGORIES);
	}

	private void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.petstyle_ttb_title);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PetStyleActivity.this.finish();
			}
		});

		ListView petstyleList = (ListView) findViewById(R.id.petstyle_list);
		adapter = new PetStyleAdapter(mActivity);
		objList = new ArrayList<PetStyleObject>();
		petstyleList.setAdapter(adapter);
		petstyleList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (adapter != null) {
			PetStyleObject styleObj = adapter.getItem(position);
			if (styleObj != null) {
				PetStyleActivity.this.finish();
				PetCategorysActivity.startActivity(mActivity, styleObj.getCategoryId());
			}
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETPETCATEGORIES :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						//
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							json = json.getJSONObject("Result");
							JSONArray array = json.getJSONArray("List");
							for (int i = 0; i < array.length(); i++) {
								PetStyleObject styleObj = OJMFactory.createOJM().fromJson(array.getString(i),
										PetStyleObject.class);
								objList.add(styleObj);
							}
							adapter.setData(objList);
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				break;
		}
	}
}
