package mobi.jzcx.android.chongmi.ui.main.serve;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.ConfirmJoinObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.GroupRequest;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.RongLianObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.adapter.AccountCenterPetAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.GroupRequestImageAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetCenterActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.VPagerImageDetailsActivity;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class GroupRequestActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	static GroupRequest groupRequest;
	SimpleDraweeView userImg;
	TextView nameTV;
	TextView reasonTV;
	ListView petList;
	Button acceptBtn;
	ImageView sexImg;
	AccountCenterPetAdapter mPetAdapter;
	PercentLinearLayout userLL;
	HorizontalListView imageListview;
	GroupRequestImageAdapter imageAdapter;

	public static void startActivity(Context context, GroupRequest groupRequest) {
		ActivityUtils.startActivity(context, GroupRequestActivity.class);
		GroupRequestActivity.groupRequest = groupRequest;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouprequest);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		initTitleBar();

		PercentLinearLayout accountLL = (PercentLinearLayout) findViewById(R.id.request_accountLL);
		imageListview = (HorizontalListView) findViewById(R.id.request_accountimg);
		userLL = (PercentLinearLayout) findViewById(R.id.request_userLL);
		userImg = (SimpleDraweeView) findViewById(R.id.request_userimage);
		nameTV = (TextView) findViewById(R.id.request_username);
		reasonTV = (TextView) findViewById(R.id.request_reason);
		petList = (ListView) findViewById(R.id.request_petlist);
		acceptBtn = (Button) findViewById(R.id.request_acceptBtn);
		sexImg = (ImageView) findViewById(R.id.request_usersex);
		if (!CommonTextUtils.isEmpty(groupRequest.getAccount().getIcoUrl())) {
			FrescoHelper.displayImageview(userImg,
					groupRequest.getAccount().getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
					R.drawable.avatar_default_image, getResources(), true);
		}
		if (!CommonTextUtils.isEmpty(groupRequest.getAccount().getNickName())) {
			nameTV.setText(groupRequest.getAccount().getNickName());
		} else {
			nameTV.setText("");
		}
		if (!CommonTextUtils.isEmpty(groupRequest.getContext())) {
			reasonTV.setText(groupRequest.getContext());
		} else {
			reasonTV.setText("");
		}
		if (!CommonTextUtils.isEmpty(groupRequest.getAccount().getGender())) {
			if (groupRequest.getAccount().getGender().equals("0")) {
				sexImg.setImageResource(R.drawable.sex_lady);
			} else {
				sexImg.setImageResource(R.drawable.sex_man);
			}
		} else {
			sexImg.setImageResource(R.drawable.sex_null);
		}

		imageAdapter = new GroupRequestImageAdapter(this);
		imageListview.setAdapter(imageAdapter);
		imageListview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				AccountCenterActivity.startActivity(mActivity, groupRequest.getMemberId());
				return false;
			}
		});

		accountLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountCenterActivity.startActivity(mActivity, groupRequest.getMemberId());
			}
		});

		mPetAdapter = new AccountCenterPetAdapter(this);
		petList.setAdapter(mPetAdapter);
		petList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PetObject petObj = mPetAdapter.getItem(position);
				GetPetObject getpetObj = new GetPetObject();
				getpetObj.setMeberId(groupRequest.getMemberId());
				getpetObj.setPetId(petObj.getPetId());
				PetCenterActivity.startActivity(mActivity, getpetObj);
			}
		});
		// userLL.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// AccountCenterActivity.startActivity(mActivity,
		// groupRequest.getMemberId());
		// }
		// });
		userImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (groupRequest != null) {
					ArrayList<String> urlList = new ArrayList<String>();
					urlList.add(groupRequest.getAccount().getIcoUrl());
					VPagerImageDetailsActivity.startActivity(mActivity, urlList, 0);
				}
			}
		});
		acceptBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfirmJoinObject joinObj = new ConfirmJoinObject();
				joinObj.setAccept(true);
				joinObj.setActivityId(groupRequest.getActivityId());
				joinObj.setRequestId(groupRequest.getRequestId());
				sendMessage(YSMSG.REQ_CONFIRMJOIN, 0, 0, joinObj);
				showWaitingDialog();
			}
		});
	}

	private void initData() {
		sendMessage(YSMSG.REQ_GETMEBERINFO, 0, 0, groupRequest.getMemberId());
		if (groupRequest.isIsJoin()) {
			acceptBtn.setEnabled(false);
		} else {
			acceptBtn.setEnabled(true);
		}
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.grouprequest_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GroupRequestActivity.this.finish();
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_CONFIRMJOIN :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (groupRequest != null) {
						// GroupRequestDao requestDao = new GroupRequestDao();
						// groupRequest.setAccept(true);
						// requestDao.update(groupRequest);
					}
					acceptBtn.setEnabled(false);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_GETMEBERINFO :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					// "Member":{"MemberId":10,"NickName":"王主任","IcoUrl":"http://assets.cm.mc2015.co//CM.App/2015/12/17/17/b6179418-7ee8-44c7-ac43-1fbc74d7ffb9.jpg","Birthday":"1994-12-01","Gender":"1","LocateAddress":null,"LastLocateAddress":null,"Pets":[{"PetId":19,"AnimalSpecies":null,"CategoryId":71,"CategoryName":"冰岛牧羊犬","Gender":"1","Name":"我是一只猫","Birthday":"2009-12-17","IcoUrl":"http://assets.cm.mc2015.co//CM.App/2015/12/17/17/c3617748-f490-450f-8e87-91006d961237.jpg","Description":"��������������x_x��","MemberId":10},{"PetId":14,"AnimalSpecies":null,"CategoryId":8,"CategoryName":"伯曼猫","Gender":"1","Name":"我的一只狗","Birthday":"2014-12-15","IcoUrl":"http://assets.cm.mc2015.co//CM.App/2015/12/15/14/549750a0-e41f-4b61-8993-2b7cee16666e.jpg","Description":"我的一只狗啊啊","MemberId":10}],"FollowsCount":9,"FansCount":14,"IsFollowed":true},"ImMember":{"uuid":"f9eda53a-9fbb-11e5-b940-83aa73fb5995","type":"user","created":1449806540547,"modified":1449806540547,"username":"13316566333","activated":true,"device_token":"","nickname":"","password":"7d14344e5c02233a073eea5a0a5301e8","MemberId":10},"PictureShow":["http://assets.cm.mc2015.co//CM.App/2015/12/19/18/36b425f1-668f-4253-95ad-634334673f97.jpg","http://assets.cm.mc2015.co//CM.App/2015/12/17/17/a561c08b-4e31-4cf0-89e3-c924a51a86a1.jpg","http://assets.cm.mc2015.co//CM.App/2015/12/15/17/8089c48e-b75c-4367-b547-be6650862fd7.jpg","http://assets.cm.mc2015.co//CM.App/2015/12/15/16/fe392801-ff9f-474a-8b01-47eb61a68fb3.jpg"]}
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						AccountDetailObject detailObj = OJMFactory.createOJM().fromJson(json.getString("Member"),
								AccountDetailObject.class);

						JSONArray PictureShow = json.getJSONArray("PictureShow");
						ArrayList<String> photos = new ArrayList<String>();
						for (int j = 0; j < PictureShow.length(); j++) {
							photos.add(PictureShow.getString(j));
						}
						detailObj.setPhotos(photos);
						JSONArray array = json.getJSONObject("Member").getJSONArray("Pets");

						ArrayList<PetObject> petList = new ArrayList<PetObject>();
						int length = array.length();
						if (length > 3) {
							length = 3;
						}
						for (int i = 0; i < length; i++) {
							PetObject petObj = OJMFactory.createOJM().fromJson(array.getString(i), PetObject.class);
							petList.add(petObj);
						}
						detailObj.setPets(petList);
						RongLianObject rlObj = OJMFactory.createOJM().fromJson(json.getString("ImMember"),
								RongLianObject.class);
						detailObj.setRLObj(rlObj);
						mPetAdapter.setData(petList);
						imageAdapter.setData(photos);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
		}
	}

}
