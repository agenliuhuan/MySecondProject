package mobi.jzcx.android.chongmi.ui.main.serve;

import java.text.NumberFormat;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AccountObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.ui.adapter.PetAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.main.mine.AccountCenterActivity;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public class GroupMebersActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	static String aId;
	GridAdapter adapter = null;
	ArrayList<AccountObject> mebersList;

	public static void startActivity(Context context, String activeId) {
		ActivityUtils.startActivity(context, GroupMebersActivity.class);
		if (!TextUtils.isEmpty(activeId)) {
			GroupMebersActivity.aId = activeId;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupmebers);

		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
		ListView gridView = (ListView) findViewById(R.id.GroupMebers_List);
		adapter = new GridAdapter(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AccountObject groupmeber = adapter.getItem(position);
				AccountCenterActivity.startActivity(mActivity, groupmeber.getMemberId());
			}
		});
	}

	private void initData() {
		mebersList = new ArrayList<AccountObject>();
		sendMessage(YSMSG.REQ_GETMEBERS, 0, 0, aId);
	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.groupmeber_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETMEBERS :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("List");
							mebersList.clear();
							ArrayList<PetObject> pets;
							for (int i = 0; i < array.length(); i++) {
								AccountObject actObj = OJMFactory.createOJM().fromJson(array.getString(i),
										AccountObject.class);
								if (!CommonTextUtils.isEmpty(array.getJSONObject(i).getString("Pets"))) {
									JSONArray petarray = array.getJSONObject(i).getJSONArray("Pets");
									pets = new ArrayList<PetObject>();
									for (int j = 0; j < petarray.length(); j++) {
										PetObject pet = OJMFactory.createOJM().fromJson(petarray.getString(j),
												PetObject.class);
										pets.add(pet);
									}
									actObj.setPetList(pets);
								}
								// LngLatObject LngLatObj = new LngLatObject();
								// JSONObject lnglatJson =
								// array.getJSONObject(i).getJSONObject("LocateAddress");
								// LngLatObj.setLng(lnglatJson.getString("Lng"));
								// LngLatObj.setLat(lnglatJson.getString("Lat"));
								// actObj.setLocateAddress(LngLatObj);
								mebersList.add(actObj);
							}
							adapter.setData(mebersList);
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

	class GridAdapter extends BaseAdapter {
		Context mContext;
		ArrayList<AccountObject> list;

		public GridAdapter(Context context) {
			mContext = context;
			list = new ArrayList<AccountObject>();
		}

		public void setData(ArrayList<AccountObject> data) {
			if (list != null) {
				list.clear();
				list.addAll(data);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public AccountObject getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.groupmeber_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AccountObject meberObj = getItem(position);
			if (meberObj != null) {
				if (!CommonTextUtils.isEmpty(meberObj.getIcoUrl())) {
					FrescoHelper.displayImageview(holder.avatar,
							meberObj.getIcoUrl() + CommonUtils.getAvatarSize(mContext),
							R.drawable.avatar_default_image, mContext.getResources(), true);
				} else {
					Uri uri = Uri.parse("res://mobi.jzcx.android.chongmi/" + R.drawable.avatar_default_image);
					holder.avatar.setImageURI(uri);
				}

				if (!CommonTextUtils.isEmpty(meberObj.getNickName())) {
					holder.nameTv.setText(meberObj.getNickName());
				} else {
					holder.nameTv.setText("");
				}
				if (!CommonTextUtils.isEmpty(meberObj.getLastLocateAddress())) {
					holder.cityTv.setText(meberObj.getLastLocateAddress());
				} else {
					holder.cityTv.setText("");
				}

				// LngLatObject mylnglat = App.getInstance().getLnglat();
				// if (meberObj.getLocateAddress() != null && mylnglat != null)
				// {
				// LatLng mp = new LatLng(Double.valueOf(mylnglat.getLat()),
				// Double.valueOf(mylnglat.getLng()));
				// LatLng cp = new
				// LatLng(Double.valueOf(meberObj.getLocateAddress().getLat()),
				// Double.valueOf(meberObj.getLocateAddress().getLng()));
				// NumberFormat format = NumberFormat.getNumberInstance();
				// format.setMaximumFractionDigits(2);
				// String distance = format.format(DistanceUtil.getDistance(cp,
				// mp) / 1000);
				// holder.cityTv.setText(distance +
				// mContext.getString(R.string.homepage_distance_text));
				// } else {
				// holder.cityTv.setText("");
				// }
				if (!CommonTextUtils.isEmpty(meberObj.getGender())) {
					if (meberObj.getGender().equals("0")) {
						holder.sexImage.setImageResource(R.drawable.sex_lady);
					} else {
						holder.sexImage.setImageResource(R.drawable.sex_man);
					}
				} else {
					holder.sexImage.setImageResource(R.drawable.sex_null);
				}

				int width = CommonUtils.getScreenWidth(mContext);
				LayoutParams params = (LayoutParams) holder.listPet.getLayoutParams();
				params.height = width * 722 / 10000;
				params.width = getWidthByCount(meberObj.getPetList().size());
				holder.listPet.setLayoutParams(params);

				PetAdapter petadapter = new PetAdapter(mContext);
				holder.listPet.setAdapter(petadapter);
				if (meberObj.getPetList() != null && meberObj.getPetList().size() > 0) {
					petadapter.updateList(meberObj.getPetList(), meberObj.getMemberId());
				}

			}
			return convertView;
		}

		private int getWidthByCount(int size) {
			int viewwidth = 0;
			if (size == 0) {
				return viewwidth;
			}
			if (size > 3) {
				size = 3;
			}
			int width = CommonUtils.getScreenWidth(mContext);
			viewwidth = width * 722 / 10000 * size + width * 2 / 100 * (size - 1);
			return viewwidth;
		}

		class ViewHolder {
			SimpleDraweeView avatar;
			TextView nameTv;
			TextView cityTv;
			HorizontalListView listPet;
			ImageView sexImage;

			public ViewHolder(View view) {
				this.avatar = (SimpleDraweeView) view.findViewById(R.id.meberitem_avatar);
				this.nameTv = (TextView) view.findViewById(R.id.meberitem_name);
				this.cityTv = (TextView) view.findViewById(R.id.meberitem_city);
				this.listPet = (HorizontalListView) view.findViewById(R.id.meberitem_pet);
				this.sexImage = (ImageView) view.findViewById(R.id.meberitem_sex);
			}
		}
	}

}
