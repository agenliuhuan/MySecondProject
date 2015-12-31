package mobi.jzcx.android.chongmi.ui.main.mine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountDetailObject;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMeberBlogsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.RongLianObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.dao.ImContactDao;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseConstant;
import mobi.jzcx.android.chongmi.ui.adapter.AccountCenterPetAdapter;
import mobi.jzcx.android.chongmi.ui.adapter.AccountPetDynamicAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetCenterActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.PetDiaryDetailsActivity;
import mobi.jzcx.android.chongmi.ui.main.homepage.VPagerImageDetailsActivity;
import mobi.jzcx.android.chongmi.ui.main.serve.ChatActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.client.AsyncHttpClient;
import mobi.jzcx.android.core.net.http.handler.BinaryHttpResponseHandler;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomScrollViewEx;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomScrollViewEx.ScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.umeng.analytics.MobclickAgent;

public class AccountCenterActivity extends BaseExActivity implements OnClickListener, ScrollListener {

	private ImageView userImagebg;
	private SimpleDraweeView userImage;

	private TextView username;
	private ImageView usersex;
	private TextView attentionNum;
	private TextView fansNum;
	private PercentLinearLayout messageLL;
	private PercentLinearLayout attentionLL;
	private ImageView attentionimage;
	private TextView attentiontext;
	private PercentLinearLayout topRL;

	private ListView mPetList;
	private AccountCenterPetAdapter mPetAdapter;

	private TextView petTitle;
	private TextView dynamicTitle;
	private TextView morePetTitle;
	private ListView mPetDynamicList;
	private AccountPetDynamicAdapter mPetDynamicAdapter;
	private ArrayList<DynamicObject> mDynamics;
	static String meberid;

	private PercentLinearLayout havepetLL;
	private PercentLinearLayout petemptyLL;

	AccountDetailObject detailObj;
	PullToZoomScrollViewEx scrollView;
	int mScreenWidth;
	boolean hasmore;
	boolean isUpdateIng = false;

	public static void startActivity(Context context, String meberId) {
		ActivityUtils.startActivity(context, AccountCenterActivity.class);
		AccountCenterActivity.meberid = meberId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_center);
		loadViewForCode();
		initView();
		mSetStatusBar = false;
	}

	int sy = 0;

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_8));
		initData();
		// topRL.getBackground().setAlpha(0);
		int toy = mScreenWidth * 661 / 1000;
		if (sy < 0) {
			sy = 0;
		}
		if (sy > toy) {
			sy = toy;
		}

		if (sy <= toy) {
			double fa = (double) sy / (double) toy;
			// double as = 1 - fa;
			double alpha = 255 * fa;

			topRL.getBackground().setAlpha((int) alpha);

			if (sy == toy) {
				nameTV.setText(detailObj.getNickName());
			} else {
				nameTV.setText("");
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			topRL.getBackground().setAlpha(255);
		}
	}

	private void loadViewForCode() {
		scrollView = (PullToZoomScrollViewEx) findViewById(R.id.accountcenter_scroll_view);
		View headView = LayoutInflater.from(this).inflate(R.layout.accountcenter_headview, null, false);
		View zoomView = LayoutInflater.from(this).inflate(R.layout.accountcenter_zoomview, null, false);
		View contentView = LayoutInflater.from(this).inflate(R.layout.accountcenter_contentview, null, false);
		scrollView.setHeaderView(headView);
		scrollView.setZoomView(zoomView);
		scrollView.setScrollContentView(contentView);

		// scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		//
		// @Override
		// public void onGlobalLayout() {
		// // 这一步很重要，使得上面的购买布局和下面的购买布局重合
		// onScroll(y);
		//
		// }
		// });
		scrollView.setOnScrollListener(this);
		// mWindowManager = (WindowManager)
		// getSystemService(Context.WINDOW_SERVICE);
		// if (suspendView == null) {
		// showSuspend();
		// }
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		mScreenWidth = localDisplayMetrics.widthPixels;

	}

	@Override
	public void scrollTo(int scrollY) {
		LogUtils.i("scrollTo", scrollY + "");

		int toy = mScreenWidth * 661 / 1000;
		if (scrollY < 0) {
			scrollY = 0;
		}
		if (scrollY > toy) {
			scrollY = toy;
		}

		if (scrollY <= toy) {
			double fa = (double) scrollY / (double) toy;
			// double as = 1 - fa;
			double alpha = 255 * fa;

			topRL.getBackground().setAlpha((int) alpha);

			if (scrollY == toy) {
				nameTV.setText(detailObj.getNickName());
			} else {
				nameTV.setText("");
			}
		}
		sy = scrollY;
		View view = (View) scrollView.getPullRootView().getChildAt(scrollView.getPullRootView().getChildCount() - 1);
		int d = view.getBottom();
		d -= (scrollView.getPullRootView().getHeight() + scrollView.getPullRootView().getScrollY());
		if (d == 0) {
			if (hasmore && !isUpdateIng) {
				isUpdateIng = true;
				GetMeberBlogsObject getBlogsObj = new GetMeberBlogsObject();
				getBlogsObj.setMeberId(meberid);
				getBlogsObj.setIndex(String.valueOf(blogPageSize));
				sendMessage(YSMSG.REQ_GETMYBLOGS, 0, 0, getBlogsObj);
			}

		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	TextView nameTV;
	private void initView() {
		userImage = (SimpleDraweeView) findViewById(R.id.accountcenter_userimg);
		userImagebg = (ImageView) findViewById(R.id.accountcenter_userimgbg);
		username = (TextView) findViewById(R.id.accountcenter_name);
		usersex = (ImageView) findViewById(R.id.accountcenter_seximg);
		attentionNum = (TextView) findViewById(R.id.accountcenter_attention_num);
		fansNum = (TextView) findViewById(R.id.accountcenter_fansnum);
		messageLL = (PercentLinearLayout) findViewById(R.id.accountcenter_messageLL);
		attentionLL = (PercentLinearLayout) findViewById(R.id.accountcenter_attentionLL);
		attentionimage = (ImageView) findViewById(R.id.accountcenter_attentionimg);
		attentiontext = (TextView) findViewById(R.id.accountcenter_attentiontext);
		petTitle = (TextView) findViewById(R.id.accountcenter_petTitle);
		dynamicTitle = (TextView) findViewById(R.id.accountcenter_dynamicTitle);
		morePetTitle = (TextView) findViewById(R.id.accountcenter_morepet);

		havepetLL = (PercentLinearLayout) findViewById(R.id.accountcenter_havepetLL);
		petemptyLL = (PercentLinearLayout) findViewById(R.id.accountcenter_petEmptyLL);

		initPetList();
		initPetDynamicList();

		topRL = (PercentLinearLayout) findViewById(R.id.accountcenter_title_topll);
		nameTV = (TextView) findViewById(R.id.accountcenter_title_center);
		PercentRelativeLayout leftRL = (PercentRelativeLayout) findViewById(R.id.accountcenter_title_leftRL);
		PercentRelativeLayout attentionRL = (PercentRelativeLayout) findViewById(R.id.accountcenter_attentionRL);
		PercentRelativeLayout fansRL = (PercentRelativeLayout) findViewById(R.id.accountcenter_fansRL);
		userImage.setOnClickListener(this);
		leftRL.setOnClickListener(this);
		attentionRL.setOnClickListener(this);
		fansRL.setOnClickListener(this);
		messageLL.setOnClickListener(this);
		attentionLL.setOnClickListener(this);
		morePetTitle.setOnClickListener(this);

	}

	int blogPageSize = 1;

	private void initData() {
		showWaitingDialog();
		sendMessage(YSMSG.REQ_GETMEBERINFO, 0, 0, meberid);

		GetMeberBlogsObject getBlogsObj = new GetMeberBlogsObject();
		getBlogsObj.setMeberId(meberid);
		blogPageSize = 1;
		getBlogsObj.setIndex(String.valueOf(blogPageSize));
		sendMessage(YSMSG.REQ_GETMYBLOGS, 0, 0, getBlogsObj);
	}

	private void setdata() {
		if (detailObj != null) {
			if (!CommonTextUtils.isEmpty(detailObj.getIcoUrl())) {
				FrescoHelper.displayImageview(userImage, detailObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
						R.drawable.avatar_default_image, getResources(), true);
				showTopImage(detailObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity));
			}
			if (!CommonTextUtils.isEmpty(detailObj.getNickName())) {
				username.setText(detailObj.getNickName());
			}
			if (!CommonTextUtils.isEmpty(detailObj.getGender())) {
				if (detailObj.getGender().equals("0")) {
					usersex.setImageResource(R.drawable.sex_lady);
				} else {
					usersex.setImageResource(R.drawable.sex_man);
				}
			} else {
				usersex.setImageResource(R.drawable.sex_null);
			}

			if (detailObj.isIsFollowed()) {
				attentionLL.setBackgroundResource(R.drawable.layout_account_attentioned_bg);
				attentionimage.setVisibility(View.GONE);
				attentiontext.setText(getString(R.string.account_center_attentioned));
			} else {
				attentionLL.setBackgroundResource(R.drawable.layout_account_attention_bg);
				attentionimage.setVisibility(View.VISIBLE);
				attentiontext.setText(getString(R.string.account_center_attention));
			}

			attentionNum.setText(String.valueOf(detailObj.getFollowsCount()));
			fansNum.setText(String.valueOf(detailObj.getFansCount()));
			mPetAdapter.setData(detailObj.getPets());
			if (detailObj.getPets().size() > 3) {
				morePetTitle.setVisibility(View.VISIBLE);
			} else {
				morePetTitle.setVisibility(View.GONE);
			}

			CommonUtils.setListViewHeightBasedOnChildren(mPetList);
			UserObject myself = CoreModel.getInstance().getMyself();
			PercentLinearLayout actionLL = (PercentLinearLayout) findViewById(R.id.accountcenter_actionLL);
			if (!CommonTextUtils.isEmpty(detailObj.getMemberId()) && myself != null) {
				if (myself.getMemberId().equals(detailObj.getMemberId())) {
					actionLL.setVisibility(View.GONE);
					petTitle.setText(getString(R.string.account_center_mypetnum));
					dynamicTitle.setText(getString(R.string.account_center_mydynamic));
				} else {
					actionLL.setVisibility(View.VISIBLE);
					petTitle.setText(getString(R.string.account_center_petnum));
					dynamicTitle.setText(getString(R.string.account_center_dynamic));
				}
			}
		}
	}

	String savePath;

	private void showTopImage(String imageurl) {
		ImageLoaderHelper.displayImage(imageurl, userImagebg, R.drawable.image_default_image, processor, false);
		// Bitmap bitmap = null;
		// savePath = FileUtils.COVER + detailObj.getMemberId() + FileUtils.JPG;
		// if (FileUtils.exists(savePath)) {
		// ImageLoaderHelper.displayImage(savePath, userImagebg,
		// R.drawable.user_default_image, false);
		// } else {
		//
		// }
	}

	BitmapProcessor processor = new BitmapProcessor() {
		@Override
		public Bitmap process(Bitmap bitmap) {
			if (!FileUtils.exists(savePath)) {
				Bitmap newbitmap = ImageUtils.fastblur(bitmap, 4);
				return newbitmap;
				// if (ImageUtils.saveBitmapToFile(newbitmap, savePath, 90)) {
				//
				// }
			}
			return null;
		}
	};

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMEBERINFO :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						detailObj = OJMFactory.createOJM()
								.fromJson(json.getString("Member"), AccountDetailObject.class);
						JSONArray array = json.getJSONObject("Member").getJSONArray("Pets");
						ArrayList<PetObject> petList = new ArrayList<PetObject>();
						for (int i = 0; i < array.length(); i++) {
							PetObject petObj = OJMFactory.createOJM().fromJson(array.getString(i), PetObject.class);
							petList.add(petObj);
						}
						if (petList.size() == 0) {
							havepetLL.setVisibility(View.GONE);
							petemptyLL.setVisibility(View.VISIBLE);
						} else {
							havepetLL.setVisibility(View.VISIBLE);
							petemptyLL.setVisibility(View.GONE);
						}
						detailObj.setPets(petList);
						RongLianObject rlObj = OJMFactory.createOJM().fromJson(json.getString("ImMember"),
								RongLianObject.class);
						detailObj.setRLObj(rlObj);
						setdata();
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
			case YSMSG.RESP_GETMYBLOGS :
				isUpdateIng = false;
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							hasmore = json.getBoolean("HasMore");

							if (array.length() == 0) {
								emptyView.setVisibility(View.VISIBLE);
								mPetDynamicList.setVisibility(View.GONE);
							} else {
								emptyView.setVisibility(View.GONE);
								mPetDynamicList.setVisibility(View.VISIBLE);
							}
							if (blogPageSize == 1) {
								mDynamics.clear();
							}
							blogPageSize++;

							JSONObject petjson;
							ArrayList<String> ps;
							LngLatObject LngLatObj;
							ArrayList<AccountObject> likeMebers;
							for (int i = 0; i < array.length(); i++) {
								String dStr = array.getString(i);
								DynamicObject actObj = OJMFactory.createOJM().fromJson(dStr, DynamicObject.class);
								petjson = new JSONObject(dStr);
								PetObject petObj = OJMFactory.createOJM().fromJson(petjson.getString("Pet"),
										PetObject.class);
								actObj.setPet(petObj);
								AccountDetailObject account = OJMFactory.createOJM().fromJson(
										petjson.getString("Member"), AccountDetailObject.class);
								actObj.setAccountObj(account);

								JSONArray photos = petjson.getJSONArray("Photos");
								ps = new ArrayList<String>();
								for (int j = 0; j < photos.length(); j++) {
									ps.add(photos.getString(j));
								}
								actObj.setPetPhotos(ps);
								LngLatObj = new LngLatObject();
								JSONObject lnglatJson = petjson.getJSONObject("LocateAddress");
								LngLatObj.setLng(lnglatJson.getString("Lng"));
								LngLatObj.setLat(lnglatJson.getString("Lat"));
								actObj.setAdressObject(LngLatObj);

								JSONArray likemeberJson = petjson.getJSONArray("LikeMembers");
								likeMebers = new ArrayList<AccountObject>();
								for (int k = 0; k < likemeberJson.length(); k++) {
									AccountObject meberObj = OJMFactory.createOJM().fromJson(
											likemeberJson.getString(k), AccountObject.class);
									likeMebers.add(meberObj);
								}
								actObj.setlMembers(likeMebers);
								mDynamics.add(actObj);
							}
							mPetDynamicAdapter.updateList(mDynamics);
							if (mDynamics != null && mDynamics.size() > 0) {
								for (DynamicObject dynamicObj : mDynamics) {
									if (dynamicObj != null && dynamicObj.getPetPhotos() != null
											&& dynamicObj.getPetPhotos().size() > 0) {
										getAllBitmapByURL(dynamicObj.getPetPhotos());
									}
								}
							}
							CommonUtils.setListViewHeightBasedOnChildren(mPetDynamicList);
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
			case YSMSG.RESP_CANCELFOLLOW :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					detailObj.setIsFollowed(false);
					attentionLL.setBackgroundResource(R.drawable.layout_account_attention_bg);
					attentionimage.setVisibility(View.VISIBLE);
					attentiontext.setText(getString(R.string.account_center_attention));
					int fanscount = detailObj.getFansCount();
					fanscount--;
					detailObj.setFansCount(fanscount);
					fansNum.setText(String.valueOf(detailObj.getFansCount()));
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.RESP_MAIN_CANCELFOLLOW;
					message.obj = detailObj.getMemberId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case YSMSG.RESP_FOLLOW :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					detailObj.setIsFollowed(true);
					attentionLL.setBackgroundResource(R.drawable.layout_account_attentioned_bg);
					attentionimage.setVisibility(View.GONE);
					attentiontext.setText(getString(R.string.account_center_attentioned));
					int fanscount = detailObj.getFansCount();
					fanscount++;
					detailObj.setFansCount(fanscount);
					fansNum.setText(String.valueOf(detailObj.getFansCount()));
					Message message = App.getInstance().obtainMessage();
					message.what = YSMSG.RESP_MAIN_FOLLOW;
					message.obj = detailObj.getMemberId();
					CoreModel.getInstance().notifyOutboxHandlers(message);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
			case 1 :
				mPetDynamicAdapter.notifyDataSetChanged();
				break;
		}
	}

	private void getAllBitmapByURL(final ArrayList<String> urls) {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		int count = urls.size();
		if (count > 4) {
			count = 4;
		}

		for (int i = 0; i < count; i++) {
			final String url = urls.get(i);
			int screenWiddth = CommonUtils.getScreenWidth(mActivity);
			final int width = screenWiddth * 1916 / 10000;
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (!FileUtils.exists(FileUtils.COVER + MD5.md5(url) + FileUtils.JPG)) {
						try {
							downloadFile(url + "?width=" + width + "&height=" + width, FileUtils.COVER + MD5.md5(url)
									+ FileUtils.JPG);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mHandler.sendEmptyMessage(1);
				}
			});
		}
	}

	/**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	public static void downloadFile(String url, final String fileName) throws Exception {

		AsyncHttpClient client = new AsyncHttpClient();
		// 指定文件类型
		String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
		// 获取二进制数据如图片和其他文件
		client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

			@Override
			public void onSuccess(int statusCode, byte[] binaryData) {

				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

				File file = new File(fileName);
				// 压缩格式
				CompressFormat format = Bitmap.CompressFormat.JPEG;
				// 压缩比例
				int quality = 100;
				try {
					// 若存在则删除
					if (file.exists())
						file.delete();
					// 创建文件
					file.createNewFile();
					//
					OutputStream stream = new FileOutputStream(file);
					// 压缩输出
					bmp.compress(format, quality, stream);
					// 关闭
					stream.close();
					//

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			@Override
			@Deprecated
			public void onFailure(Throwable error, byte[] binaryData) {
				// TODO Auto-generated method stub
				super.onFailure(error, binaryData);
			}

		});
	}

	private void initPetList() {
		mPetList = (ListView) findViewById(R.id.accountcenter_petlist);
		mPetAdapter = new AccountCenterPetAdapter(this);
		mPetList.setAdapter(mPetAdapter);
		mPetList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GetPetObject getPet = new GetPetObject();
				getPet.setMeberId(detailObj.getMemberId());
				getPet.setPetId(mPetAdapter.getItem(position).getPetId());
				PetCenterActivity.startActivity(mActivity, getPet);
			}
		});
	}

	View emptyView;

	private void initPetDynamicList() {
		mPetDynamicList = (ListView) findViewById(R.id.accountcenter_petdynamiclist);
		mPetDynamicAdapter = new AccountPetDynamicAdapter(this);
		mPetDynamicList.setAdapter(mPetDynamicAdapter);
		mDynamics = new ArrayList<DynamicObject>();
		mPetDynamicList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PetDiaryDetailsActivity.startActivity(mActivity, mPetDynamicAdapter.getItem(position), false, null);
			}
		});
		emptyView = findViewById(R.id.accountcenter_dynamicEmptyLL);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.accountcenter_userimg :
				if (detailObj != null) {
					ArrayList<String> urlList = new ArrayList<String>();
					urlList.add(detailObj.getIcoUrl());
					VPagerImageDetailsActivity.startActivity(mActivity, urlList, 0);
				}
				break;
			case R.id.accountcenter_morepet :
				if (detailObj != null && detailObj.getPets() != null) {
					PetListActivity.startActivity(mActivity, detailObj.getPets(), detailObj.getMemberId());
				}
				break;
			case R.id.accountcenter_title_leftRL :
				AccountCenterActivity.this.finish();
				break;
			case R.id.accountcenter_attentionRL :
				AttentionActivity.startActivity(mActivity, false, meberid);
				break;
			case R.id.accountcenter_fansRL :
				AttentionActivity.startActivity(mActivity, true, meberid);
				break;
			case R.id.accountcenter_messageLL :
				if (detailObj != null && detailObj.getRLObj() != null) {
					ImContactObject contact = new ImContactObject();
					contact.setContactAvatar(detailObj.getIcoUrl());
					contact.setContactId(detailObj.getRLObj().getUsername());
					contact.setContactName(detailObj.getNickName());
					contact.setMeberId(detailObj.getMemberId());
					contact.setGroup(false);
					ImContactDao contactDao = new ImContactDao();
					contactDao.update(contact);

					Intent intent = new Intent(AccountCenterActivity.this, ChatActivity.class);
					intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
					intent.putExtra(EaseConstant.EXTRA_USER_IMID, detailObj.getRLObj().getUsername());
					intent.putExtra(EaseConstant.EXTRA_MEMBER_ID, detailObj.getMemberId());
					intent.putExtra(EaseConstant.EXTRA_MEMBER_NAME, detailObj.getNickName());
					intent.putExtra(EaseConstant.EXTRA_MEMBER_AVATAR, detailObj.getIcoUrl());
					startActivity(intent);
				}
				break;
			case R.id.accountcenter_attentionLL :
				if (detailObj != null) {
					if (detailObj.isIsFollowed()) {
						sendMessage(YSMSG.REQ_CANCELFOLLOW, 0, 0, detailObj.getMemberId());
					} else {
						sendMessage(YSMSG.REQ_FOLLOW, 0, 0, detailObj.getMemberId());
					}
					showWaitingDialog();
				}

				break;
		}

	}

}
