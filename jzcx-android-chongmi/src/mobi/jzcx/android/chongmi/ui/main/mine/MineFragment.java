package mobi.jzcx.android.chongmi.ui.main.mine;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExFragment;
import mobi.jzcx.android.chongmi.ui.login.LoginActivity;
import mobi.jzcx.android.chongmi.ui.register.RegisteOverActivity;
import mobi.jzcx.android.chongmi.ui.setting.SettingActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MineFragment extends BaseExFragment implements OnClickListener {
	private View rootView;
	private SimpleDraweeView userimg;
	private TextView nicknameTV;
	private TextView attentionnum;
	private TextView attentionednum;

	static MineFragment fragment;

	public static MineFragment getInstance() {
		if (fragment == null) {
			fragment = new MineFragment();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_mine, container, false);
		initView();
		return rootView;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			initData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {

		userimg = (SimpleDraweeView) rootView.findViewById(R.id.Mine_userimg);
		nicknameTV = (TextView) rootView.findViewById(R.id.Mine_username);
		attentionnum = (TextView) rootView.findViewById(R.id.attention_num);
		attentionednum = (TextView) rootView.findViewById(R.id.attentioned_num);

		RelativeLayout accountRL = (RelativeLayout) rootView.findViewById(R.id.Mine_accountRL);
		LinearLayout attentionLL = (LinearLayout) rootView.findViewById(R.id.Mine_attentionRL);
		LinearLayout attentionedLL = (LinearLayout) rootView.findViewById(R.id.Mine_attentionedRL);
		RelativeLayout mypetRL = (RelativeLayout) rootView.findViewById(R.id.Mine_MyPetRL);
		RelativeLayout settingRL = (RelativeLayout) rootView.findViewById(R.id.Mine_SettingRL);
		userimg.setOnClickListener(this);
		attentionLL.setOnClickListener(this);
		attentionedLL.setOnClickListener(this);
		mypetRL.setOnClickListener(this);
		settingRL.setOnClickListener(this);
		accountRL.setOnClickListener(this);
	}

	private void initData() {
		UserObject myself = CoreModel.getInstance().getMyself();
		if (myself != null) {
			sendMessage(YSMSG.REQ_GETMEBERINFO, 0, 0, myself.getMemberId());
		}

	}

	private void setData(AccountDetailObject myself) {
		if (myself != null) {
			if (!CommonTextUtils.isEmpty(myself.getIcoUrl())) {
				FrescoHelper.displayImageview(userimg, myself.getIcoUrl() + CommonUtils.getAvatarSize(getActivity()),
						R.drawable.avatar_default_image, getActivity().getResources(), true);
			}
			if (!CommonTextUtils.isEmpty(myself.getNickName())) {
				nicknameTV.setText(myself.getNickName());
			}
			attentionnum.setText(String.valueOf(myself.getFollowsCount()));
			attentionednum.setText(String.valueOf(myself.getFansCount()));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.Mine_attentionRL : {
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null) {
					AttentionActivity.startActivity(getActivity(), false, myself.getMemberId());
				} else {
					LoginActivity.startActivity(getActivity(), "");
				}
			}
				break;
			case R.id.Mine_attentionedRL : {
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null) {
					AttentionActivity.startActivity(getActivity(), true, myself.getMemberId());
				} else {
					LoginActivity.startActivity(getActivity(), "");
				}
			}
				break;
			case R.id.Mine_MyPetRL : {
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null) {
					MyPetActivity.startActivity(getActivity());
				} else {
					LoginActivity.startActivity(getActivity(), "");
				}
			}
				break;
			case R.id.Mine_SettingRL : {
				SettingActivity.startActivity(getActivity());
				// RegisteAttentionActivity.startActivity(getActivity());
			}
				break;
			case R.id.Mine_accountRL : {
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null) {
					if (CommonTextUtils.isEmpty(myself.getBirthday())) {
						RegisteOverActivity.startActivity(getActivity());
					} else {
						AccountActivity.startActivity(getActivity());
					}
				} else {
					LoginActivity.startActivity(getActivity(), "");
				}

			}
				break;
			case R.id.Mine_userimg :
				UserObject myself = CoreModel.getInstance().getMyself();
				if (myself != null) {
					AccountCenterActivity.startActivity(getActivity(), myself.getMemberId());
				} else {
					LoginActivity.startActivity(getActivity(), "");
				}

				break;
			default :
				break;
		}

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMEBERINFO :
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						AccountDetailObject detailObj = OJMFactory.createOJM().fromJson(json.getString("Member"),
								AccountDetailObject.class);
						UserObject myself = CoreModel.getInstance().getMyself();
						if (myself != null && detailObj != null && detailObj.getMemberId().equals(myself.getMemberId())) {
							if (!CommonTextUtils.isEmpty(detailObj.getIcoUrl())) {
								CoreModel.getInstance().getMyself().setIcoUrl(detailObj.getIcoUrl());
							}
							if (!CommonTextUtils.isEmpty(detailObj.getBirthday())) {
								CoreModel.getInstance().getMyself().setBirthday(detailObj.getBirthday());
							}
							if (!CommonTextUtils.isEmpty(detailObj.getNickName())) {
								CoreModel.getInstance().getMyself().setNickName(detailObj.getNickName());
							}
							if (!CommonTextUtils.isEmpty(detailObj.getGender())) {
								CoreModel.getInstance().getMyself().setGender(detailObj.getGender());
							}
							setData(detailObj);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					}
				}
				break;
		}
	}

}
