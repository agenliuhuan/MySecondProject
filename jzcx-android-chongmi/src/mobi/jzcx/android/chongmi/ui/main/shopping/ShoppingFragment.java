package mobi.jzcx.android.chongmi.ui.main.shopping;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExFragment;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.answer.QuestionListActivity;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShoppingFragment extends BaseExFragment {
	View rootView;
	TextView updateTV;
	static ShoppingFragment fragment;
	PetBindObject petbindObj;

	public static ShoppingFragment getInstance() {
		if (fragment == null) {
			fragment = new ShoppingFragment();
		}
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
		initView();
		return rootView;
	}

	private void initView() {
		PercentRelativeLayout hardwareRL = (PercentRelativeLayout) rootView.findViewById(R.id.hardwareRL);
		updateTV = (TextView) rootView.findViewById(R.id.hardware_updatetv);
		hardwareRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_11));
				sendEmptyMessage(YSMSG.REQ_PETBINDLIST_MAIN);
			}
		});
		PercentRelativeLayout answerRL = (PercentRelativeLayout) rootView.findViewById(R.id.AnswerRL);
		answerRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QuestionListActivity.startActivity(getActivity());
				MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_5));
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_PETBINDLIST_MAIN :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							for (int i = 0; i < array.length(); i++) {
								PetBindObject petbindObj = OJMFactory.createOJM().fromJson(array.getString(i),
										PetBindObject.class);
								if (petbindObj.isIsBind()) {
									this.petbindObj = petbindObj;
								}
							}
							if (petbindObj != null) {
								PetStepsnumActivity.startActivity(getActivity(), this.petbindObj);
							} else {
								HardwareStartBindActivity.startActivity(getActivity());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (java.lang.InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					YSToast.showToast(getActivity(), String.valueOf(msg.obj));
				}
				break;
		}
	}
}