package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetBlogsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.adapter.PetCenterAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomListViewEx;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomListViewEx.ListViewScrollListener;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomScrollViewEx;
import mobi.jzcx.android.core.view.pulltozoom.PullToZoomScrollViewEx.ScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.umeng.analytics.MobclickAgent;

public class PetCenterActivity extends BaseExActivity implements PetCenterDynamicClick {
	PercentLinearLayout topRL;
	PercentRelativeLayout leftRL;

	private ImageView bgImage;
	private SimpleDraweeView petimage;
	private SimpleDraweeView accountImg;
	TextView petName;
	TextView petIntro;
	TextView petstyle;
	TextView petbirthday;
	ImageView sexImage;

	static GetPetObject getpetObj;
	String savePath;
	PetObject petObj;
	AccountObject accountObj;

	ArrayList<DynamicObject> petBlogList;
	private PetCenterAdapter mPetAdapter;
	// ListView petBlogsListview;
	PullToZoomListViewEx petBlogsListview;
	View emptyview;
	int mScreenWidth;
	TextView nameTV;
	boolean isUpdateIng = false;

	boolean hasmore;

	int height;

	public static void startActivity(Context context, GetPetObject getpetObj) {
		ActivityUtils.startActivity(context, PetCenterActivity.class);
		PetCenterActivity.getpetObj = getpetObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_center);
		loadViewForCode();
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		// int mScreenHeight = localDisplayMetrics.heightPixels;
		mScreenWidth = localDisplayMetrics.widthPixels;
		// AbsListView.LayoutParams localObject = new
		// AbsListView.LayoutParams(mScreenWidth, (int) (66.1F * (mScreenWidth /
		// 100.0F)));
		// scrollView.setHeaderLayoutParams(localObject);
		initView();
		mSetStatusBar = false;
	}

	int scrollY;

	private void loadViewForCode() {
		petBlogsListview = (PullToZoomListViewEx) findViewById(R.id.petCenter_listview);
		View headView = LayoutInflater.from(this).inflate(R.layout.petcenter_headview, null, false);
		View zoomView = LayoutInflater.from(this).inflate(R.layout.petcenter_zoomview, null, false);
		View contentView = LayoutInflater.from(this).inflate(R.layout.petcenter_contentview, null, false);
		petBlogsListview.setHeaderView(headView);
		petBlogsListview.setZoomView(zoomView);
		// scrollView.setScrollContentView(contentView);
		// scrollView.setOnScrollListener(this);
		petBlogsListview.setScrollListener(new ListViewScrollListener() {
			@Override
			public void scrollTo(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int y) {
				LogUtils.i("firstVisibleItem", firstVisibleItem + "");
				LogUtils.i("visibleItemCount", visibleItemCount + "");
				LogUtils.i("totalItemCount", totalItemCount + "");
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (hasmore && !isUpdateIng) {
						isUpdateIng = true;
						GetPetBlogsObject obj = new GetPetBlogsObject();
						obj.setPageIndex(pageindex);
						obj.setPetId(petObj.getPetId());
						sendMessage(YSMSG.REQ_GETPETBLOGS, pageindex, 0, obj);
					}
				}
				scrollY = y;
				if (y < 0) {
					y = 0;
				}
				LogUtils.i("y", "" + y);
				int top = mScreenWidth * 53 / 100;
				if (y == 0) {
					nameTV.setText("");
					topRL.getBackground().setAlpha(0);
				} else if (y <= top) {
					double fa = (double) y / (double) top;
					double alpha = 255 * fa;
					topRL.getBackground().setAlpha((int) alpha);
					if (petObj != null && !CommonTextUtils.isEmpty(petObj.getName())) {
						nameTV.setText(petObj.getName());
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
		});
	}

	private void initView() {
		topRL = (PercentLinearLayout) findViewById(R.id.petcentertitle_topRL);
		leftRL = (PercentRelativeLayout) findViewById(R.id.petcentertitle_leftRL);
		leftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		bgImage = (ImageView) findViewById(R.id.petcenter_petbg);
		petimage = (SimpleDraweeView) findViewById(R.id.petcenter_petimage);
		accountImg = (SimpleDraweeView) findViewById(R.id.petcenter_accountimage);
		petimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (petObj != null) {
					ArrayList<String> urlList = new ArrayList<String>();
					urlList.add(petObj.getIcoUrl());
					VPagerImageDetailsActivity.startActivity(mActivity, urlList, 0);
				}
			}
		});

		accountImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (accountObj != null) {
					AccountCenterActivity.startActivity(mActivity, accountObj.getMemberId());
				}
			}
		});

		petName = (TextView) findViewById(R.id.petcenter_petname);
		petIntro = (TextView) findViewById(R.id.petcenter_petintro);
		petstyle = (TextView) findViewById(R.id.petcenter_petstyle);
		petbirthday = (TextView) findViewById(R.id.petcenter_petbirthday);
		sexImage = (ImageView) findViewById(R.id.petcenter_petsex);

		// petBlogsListview = (ListView)
		// findViewById(R.id.petcenter_diartyListview);
		mPetAdapter = new PetCenterAdapter(mActivity);
		mPetAdapter.setDynamicClick(this);
		petBlogList = new ArrayList<DynamicObject>();
		petBlogsListview.setAdapter(mPetAdapter);
		// listView.setOnPositionChangedListener(this);
		emptyview = findViewById(R.id.petcenter_emptyRL);
		nameTV = (TextView) findViewById(R.id.petcentertitle_center);
		topRL.getBackground().setAlpha(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onEvent(App.getInstance(), getString(R.string.umeng_feedback_9));
		initData();
//		topRL.getBackground().setAlpha(0);
		if (scrollY < 0) {
			scrollY = 0;
		}
		int top = mScreenWidth * 53 / 100;
		if (scrollY == 0) {
			nameTV.setText("");
			topRL.getBackground().setAlpha(0);
		} else if (scrollY <= top) {
			double fa = (double) scrollY / (double) top;
			double alpha = 255 * fa;
			topRL.getBackground().setAlpha((int) alpha);
			if (petObj != null && !CommonTextUtils.isEmpty(petObj.getName())) {
				nameTV.setText(petObj.getName());
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

	private void initData() {
		if (getpetObj != null) {
			sendMessage(YSMSG.REQ_GETPET, 0, 0, getpetObj);
			showWaitingDialog();
		}
	}

	private void setdata() {
		if (petObj != null) {
			if (!CommonTextUtils.isEmpty(petObj.getName())) {
				petName.setText(petObj.getName());
			}
			if (!CommonTextUtils.isEmpty(petObj.getDescription())) {
				petIntro.setText(petObj.getDescription());
			} else {
				petIntro.setText(getString(R.string.petcenter_nointro));
			}

			if (!CommonTextUtils.isEmpty(petObj.getCategoryName())) {
				petstyle.setText(petObj.getCategoryName());
			}
			if (!CommonTextUtils.isEmpty(petObj.getGender())) {
				sexImage.setVisibility(View.VISIBLE);
				if (petObj.getGender().equals("0")) {
					sexImage.setImageResource(R.drawable.pet_bitch);
				} else {
					sexImage.setImageResource(R.drawable.pet_dog);
				}
			} else {
				sexImage.setVisibility(View.GONE);
			}

			if (!CommonTextUtils.isEmpty(petObj.getBirthday())) {
				petbirthday.setText(CommonUtils.getPetAge(mActivity, petObj.getBirthday()));
			}
			if (!CommonTextUtils.isEmpty(petObj.getIcoUrl())) {
				FrescoHelper.displayImageview(petimage, petObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
						R.drawable.avatar_default_image, getResources(), true);
				showTopImage(petObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity));
			}
		}
		if (accountObj != null) {
			FrescoHelper.displayImageview(accountImg, accountObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity),
					R.drawable.avatar_default_image, getResources(), true);
		}
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

	private void showTopImage(String imageurl) {
		ImageLoaderHelper.displayImage(imageurl, bgImage, R.drawable.image_default_image, processor, false);
	}

	int pageindex = 1;

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETPET :
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						petObj = OJMFactory.createOJM().fromJson(json.getString("Pet"), PetObject.class);
						accountObj = OJMFactory.createOJM().fromJson(json.getString("Member"), AccountObject.class);
						setdata();
						GetPetBlogsObject obj = new GetPetBlogsObject();
						pageindex = 1;
						obj.setPageIndex(pageindex);
						obj.setPetId(petObj.getPetId());
						sendMessage(YSMSG.REQ_GETPETBLOGS, 0, 0, obj);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				break;
			case YSMSG.RESP_GETPETBLOGS :
				dismissWaitingDialog();
				isUpdateIng = false;
				if (msg.arg1 == 200) {
					String result = (String) msg.obj;
					try {
						JSONObject json = new JSONObject(result);
						hasmore = json.getBoolean("HasMore");
						if (pageindex == 1) {
							petBlogList.clear();
						}
						pageindex++;
						JSONArray array = json.getJSONArray("List");
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
								AccountObject meberObj = OJMFactory.createOJM().fromJson(likemeberJson.getString(k),
										AccountObject.class);
								likeMebers.add(meberObj);
							}
							actObj.setlMembers(likeMebers);
							petBlogList.add(actObj);
						}
						mPetAdapter.updateData(petBlogList);
						if (petBlogList.size() == 0) {
							emptyview.setVisibility(View.VISIBLE);
						} else {
							emptyview.setVisibility(View.GONE);
						}
						// CommonUtils.setListViewHeightBasedOnChildren(petBlogsListview);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				break;
			case 0 : {
				ViewGroup.LayoutParams params = petBlogsListview.getLayoutParams();
				params.height = height;
				petBlogsListview.setLayoutParams(params);
			}
				break;
			case YSMSG.RESP_MAINBLOGCANCELLIKE :
				if (ActivityUtils.isActivityForeground(getBaseContext(), this.getClass())) {
					if (msg.arg1 == 200) {
						mPetAdapter.setCancelLike(curDynamicObj.getMicroblogId());
					} else {

					}
					isZaning = false;
				}
				break;
			case YSMSG.RESP_MAINBLOGLIKE :
				if (ActivityUtils.isActivityForeground(getBaseContext(), this.getClass())) {
					if (msg.arg1 == 200) {
						mPetAdapter.setLike(curDynamicObj.getMicroblogId());
					} else {

					}
					isZaning = false;
				}
				break;
		}

	}

	@Override
	public void mianClick(DynamicObject obj) {
		if (obj != null) {
			PetDiaryDetailsActivity.startActivity(this, obj, false, null);
		}
	}

	DynamicObject curDynamicObj;
	boolean isZaning = false;

	@Override
	public void zanOrNotClick(DynamicObject obj) {
		if (!isZaning) {
			isZaning = true;
			curDynamicObj = obj;
			if (obj.isIsLiked()) {
				sendMessage(YSMSG.REQ_MAINBLOGCANCELLIKE, 0, 0, obj.getMicroblogId());
			} else {
				sendMessage(YSMSG.REQ_MAINBLOGLIKE, 0, 0, obj.getMicroblogId());
			}
		}

	}

	@Override
	public void commentClick(DynamicObject obj) {
		if (obj != null) {
			PetDiaryDetailsActivity.startActivity(this, obj, true, null);
		}
	}

}