package mobi.jzcx.android.chongmi.ui.main.serve;

import java.util.ArrayList;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.dao.ImContactDao;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseConstant;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.DialogHelper;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenu;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuCreator;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuItem;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView;
import mobi.jzcx.android.core.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGroupActivity extends BaseExActivity implements View.OnClickListener {
	protected TitleBarHolder mTitleBar = null;
	/** 群组列表 */
	private SwipeMenuListView mswipeMenuListView;
	View emptyView;
	/** 群组列表信息适配器 */
	private GroupAdapter mGroupAdapter;
	ArrayList<ActivityObject> mAllMyActives;
	ActivityObject curActiveObj;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, MyGroupActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mygroup);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView() {
		initTitleBar();

		mswipeMenuListView = (SwipeMenuListView) findViewById(R.id.MyGroup_List);
		mswipeMenuListView.setDrawingCacheEnabled(false);
		mswipeMenuListView.setScrollingCacheEnabled(false);
		emptyView = findViewById(R.id.empty_MyGroup);
		mswipeMenuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mGroupAdapter != null) {
					ActivityObject dGroup = mGroupAdapter.getItem(position);
					if (dGroup != null) {
						ImContactObject contact = new ImContactObject();
						contact.setContactAvatar(dGroup.getIcoUrl());
						contact.setContactId(dGroup.getImGroupId());
						contact.setContactName(dGroup.getTitle());
						contact.setMeberId(dGroup.getImGroupId());
						contact.setGroup(true);
						ImContactDao contactDao = new ImContactDao();
						contactDao.update(contact);

						Intent intent = new Intent(MyGroupActivity.this, ChatActivity.class);
						intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
						intent.putExtra(EaseConstant.EXTRA_USER_IMID, dGroup.getImGroupId());
						intent.putExtra(EaseConstant.EXTRA_GROUP_NAME, dGroup.getTitle());
						intent.putExtra(EaseConstant.EXTRA_GROUP_AVATAR, dGroup.getIcoUrl());
						startActivity(intent);

					}
				}
			}
		});
		mAllMyActives = new ArrayList<ActivityObject>();
		mGroupAdapter = new GroupAdapter(this);
		mswipeMenuListView.setAdapter(mGroupAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				switch (menu.getViewType()) {
					case 0 :
						createMenu1(menu);
						break;
					case 1 :
						createMenu2(menu);
						break;
					case 2 :
						createMenu3(menu);
						break;
				}
			}

			private void createMenu1(SwipeMenu menu) {
				SwipeMenuItem dismissItem = new SwipeMenuItem(getApplicationContext());
				dismissItem.setBackground(R.drawable.dismiss_menubg);
				dismissItem.setWidth(getWidth());
				menu.addMenuItem(dismissItem);
			}

			private void createMenu2(SwipeMenu menu) {
				SwipeMenuItem dismissgrayItem = new SwipeMenuItem(getApplicationContext());
				dismissgrayItem.setBackground(R.drawable.dismissgray_menubg);
				dismissgrayItem.setWidth(getWidth());
				menu.addMenuItem(dismissgrayItem);
			}

			private void createMenu3(SwipeMenu menu) {
				SwipeMenuItem exitItem = new SwipeMenuItem(getApplicationContext());
				exitItem.setBackground(R.drawable.exit_menubg);
				exitItem.setWidth(getWidth());
				menu.addMenuItem(exitItem);
			}
		};
		mswipeMenuListView.setMenuCreator(creator);
		mswipeMenuListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (mGroupAdapter != null) {
					ActivityObject activeObj = mGroupAdapter.getItem(position);
					if (activeObj.isIsOwner()) {
						Date endDate = CommonUtils.getDate(activeObj.getEndTime());
						if (endDate.before(new Date())) {
							showDismissDialog(activeObj);
						}
					} else {
						showExitDialog(activeObj);
					}
				}
				return false;
			}
		});

	}

	private int getWidth() {
		int width = CommonUtils.getScreenWidth(mActivity) * 1778 / 10000;
		return width;
	}

	private void initData() {
		sendEmptyMessage(YSMSG.REQ_GETMYACTIVITY);
		showWaitingDialog();
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.mygroup_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyGroupActivity.this.finish();
			}
		});
	}

	public class GroupAdapter extends BaseAdapter {
		Context mContext;
		ArrayList<ActivityObject> list;

		public GroupAdapter(Context context) {
			this.mContext = context;
			this.list = new ArrayList<ActivityObject>();
		}

		public void setData(ArrayList<ActivityObject> data) {
			if (list != null && data != null) {
				list.clear();
				list.addAll(data);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public ActivityObject getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			viewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				holder = new viewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_newsactive, parent, false);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.itemnewsactive_title);
				holder.imgActive = (ImageView) convertView.findViewById(R.id.itemnewsactive_img);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
			ActivityObject activeObj = getItem(position);
			if (activeObj != null) {
				if (!CommonTextUtils.isEmpty(activeObj.getIcoUrl())) {
					ImageLoaderHelper.displayActivityImage(activeObj.getIcoUrl(), holder.imgActive,
							R.drawable.image_default_image, true, 30);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.image_default_image);
					holder.imgActive.setImageURI(uri);
				}
				if (!CommonTextUtils.isEmpty(activeObj.getTitle())) {
					holder.tvTitle.setText(activeObj.getTitle());
				} else {
					holder.tvTitle.setText("");
				}
			}
			// convertView.setOnClickListener(new contentClick(activeObj));

			return convertView;
		}

		class viewHolder {
			ImageView imgActive;
			TextView tvTitle;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public int getItemViewType(int position) {
			ActivityObject activeObj = getItem(position);
			int type = 1;
			if (activeObj.isIsOwner()) {
				if (!CommonTextUtils.isEmpty(activeObj.getEndTime())) {
					Date endDate = CommonUtils.getDate(activeObj.getEndTime());
					if (endDate.before(new Date())) {
						type = 0;
					} else {
						type = 1;
					}
				} else {
					type = 1;
				}
			} else {
				type = 2;
			}
			return type;
		}
	}

	private void showDismissDialog(final ActivityObject activeObj) {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.dismiss_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						curActiveObj = activeObj;
						sendMessage(YSMSG.REQ_DISSMISACTIVITY, 0, 0, activeObj.getActivityId());
						showWaitingDialog();
					}
				});
	}

	private void showExitDialog(final ActivityObject activeObj) {
		DialogHelper.showTwoDialog(mActivity, false, getString(R.string.exit_content),
				getString(R.string.dialog_btn_cancel), getString(R.string.dialog_btn_confim), true, null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						curActiveObj = activeObj;
						sendMessage(YSMSG.REQ_EXITACTIVITY, 0, 0, activeObj.getActivityId());
						showWaitingDialog();
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMYACTIVITY :
				mswipeMenuListView.setEmptyView(emptyView);
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							mAllMyActives.clear();
							for (int i = 0; i < array.length(); i++) {
								ActivityObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										ActivityObject.class);
								mAllMyActives.add(actObj);
							}
							mGroupAdapter.setData(mAllMyActives);
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
			case YSMSG.RESP_DISSMISACTIVITY :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					imGroupRefresh();
					sendEmptyMessage(YSMSG.REQ_GETMYACTIVITY);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;

			case YSMSG.RESP_EXITACTIVITY :
				dismissWaitingDialog();
				if (msg.arg1 == 200) {
					imGroupRefresh();
					sendEmptyMessage(YSMSG.REQ_GETMYACTIVITY);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
				break;
		}
	}

	private void imGroupRefresh() {
		IMInitHelper.getInstance().asyncFetchGroupsFromServer(null);
	}
}
