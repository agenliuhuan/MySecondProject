package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.BindPetObject;
import mobi.jzcx.android.chongmi.biz.vo.MyBluetoothDevice;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ScanBluetoothActivity extends BaseExActivity {
	private BluetoothAdapter mBluetoothAdapter;
	private LeDeviceListAdapter mLeDeviceListAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 60 seconds.
	private static final long SCAN_PERIOD = 1000 * 60;
	private boolean mScanning;
	PercentRelativeLayout topRL;
	TextView scaningTV;
	ImageView rotateImg;
	ImageView petImg;
	ListView listview;
	static PetBindObject pet;
	MyBluetoothDevice myDevice;
	ArrayList<BluetoothDevice> deviceList;
	ArrayList<BluetoothDevice> updateList;

	public static void startActivity(Context context, PetBindObject pet) {
		ActivityUtils.startActivity(context, ScanBluetoothActivity.class);
		ScanBluetoothActivity.pet = pet;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanbluetooth);
		initView();
		initData();
		mSetStatusBar = false;
	}

	private void initView() {
		PercentRelativeLayout leftRL = (PercentRelativeLayout) findViewById(R.id.scanbluetooth_title_leftRL);
		leftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ScanBluetoothActivity.this.finish();
			}
		});
		topRL = (PercentRelativeLayout) findViewById(R.id.scanbluetooth_topRL);
		rotateImg = (ImageView) findViewById(R.id.scanbluetooth_rotate);
		petImg = (ImageView) findViewById(R.id.scanbluetooth_petimg);
		listview = (ListView) findViewById(R.id.scanbluetooth_listview);
		scaningTV = (TextView) findViewById(R.id.scanbluetooth_text);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mLeDeviceListAdapter != null) {
					myDevice = mLeDeviceListAdapter.getItem(position);
					if (!myDevice.isIsBind()) {
						BindPetObject bindpetObj = new BindPetObject();
						bindpetObj.setPetId(pet.getPetId());
						bindpetObj.setPetCollarId(myDevice.getPetCollarId());
						sendMessage(YSMSG.REQ_BINDPETCOLLAR, 0, 0, bindpetObj);
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		if (!CommonTextUtils.isEmpty(pet.getIcoUrl())) {
			ImageLoaderHelper.displayAvatar(pet.getIcoUrl() + CommonUtils.getAvatarSize(mActivity), petImg,
					R.drawable.avatar_default_image);
		}
		mLeDeviceListAdapter = new LeDeviceListAdapter();
		listview.setAdapter(mLeDeviceListAdapter);
		scanLeDevice(true);
		mHandler.post(updateRunnable);
	}

	Runnable updateRunnable = new Runnable() {
		@Override
		public void run() {
			if (updateList.size() != 0) {
				sendMessage(YSMSG.REQ_SEARCHMAC, 0, 0, getMacString());
				updateList.clear();
			}
			mHandler.postDelayed(updateRunnable, 10 * 1000);
		}
	};

	public void handleMessage(android.os.Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_SEARCHMAC :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						// "MacList":[{"Mac":"e0c79d65c113","IsBind":false,"PetCollarId":0,"PetName":null}]
						String result = (String) msg.obj;
						try {
							JSONObject json = new JSONObject(result);
							JSONArray array = json.getJSONArray("MacList");
							for (int i = 0; i < array.length(); i++) {
								MyBluetoothDevice myDevice = OJMFactory.createOJM().fromJson(array.getString(i),
										MyBluetoothDevice.class);
								for (BluetoothDevice device : deviceList) {
									if (device.getAddress().replace(":", "").toLowerCase()
											.equals(myDevice.getMac().toLowerCase())) {
										myDevice.setDeviceName(device.getName());
									}
								}
								scaningTV.setText(getString(R.string.hardware_scanbluetooth_scanovertext));
								int width = CommonUtils.getScreenWidth(mActivity);
								int height = CommonUtils.getScreenHeight(mActivity);
								LayoutParams params = (LayoutParams) topRL.getLayoutParams();
								params.height = width;
								topRL.setLayoutParams(params);
								params = (LayoutParams) listview.getLayoutParams();
								params.height = height - width;
								listview.setLayoutParams(params);
								mLeDeviceListAdapter.addDevice(myDevice);
								mLeDeviceListAdapter.notifyDataSetChanged();
							}
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
			case YSMSG.RESP_BINDPETCOLLAR : {
				if (msg.arg1 == 200) {
					YSToast.showToast(mActivity, getString(R.string.device_bindsuccess));
					pet.setPetCollarId(myDevice.getPetCollarId());
					pet.setIsBind(true);
					pet.setMac(myDevice.getMac());
					PetStepsnumActivity.startActivity(mActivity, pet);
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}
			}
				break;
		}

	};

	private String getMacString() {
		StringBuilder sb = new StringBuilder();
		int size = updateList.size();
		for (int i = 0; i < size; i++) {
			BluetoothDevice device = updateList.get(i);
			if (i == size - 1) {
				sb.append(device.getAddress().replace(":", ""));
			} else {
				sb.append(device.getAddress().replace(":", "") + ",");
			}
		}
		return sb.toString();
	}

	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
		mLeDeviceListAdapter.clear();
		mHandler.removeCallbacks(updateRunnable);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initData() {
		deviceList = new ArrayList<BluetoothDevice>();
		updateList = new ArrayList<BluetoothDevice>();
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			YSToast.showToast(mActivity, getString(R.string.ble_not_supported));
			finish();
		}
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			YSToast.showToast(mActivity, getString(R.string.ble_not_supported));
			finish();
			return;
		}
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					rotateImg.clearAnimation();

					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rander_anim);
			rotateImg.startAnimation(operatingAnim);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private class LeDeviceListAdapter extends BaseAdapter {
		private ArrayList<MyBluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;

		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<MyBluetoothDevice>();
			mInflator = ScanBluetoothActivity.this.getLayoutInflater();
		}

		public void addDevice(MyBluetoothDevice device) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}

		public MyBluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		public void clear() {
			mLeDevices.clear();
		}

		@Override
		public int getCount() {
			return mLeDevices.size();
		}

		@Override
		public MyBluetoothDevice getItem(int i) {
			return mLeDevices.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
				viewHolder.deviceBind = (TextView) view.findViewById(R.id.device_bind);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			MyBluetoothDevice device = mLeDevices.get(i);
			final String deviceName = device.getDeviceName();
			if (deviceName != null && deviceName.length() > 0) {
				viewHolder.deviceName.setText(deviceName);
			} else {
				viewHolder.deviceName.setText(R.string.unknown_device);
			}
			if (device.isIsBind()) {
				viewHolder.deviceBind.setText(String.format(getString(R.string.device_isbind), device.getPetName()));
			} else {
				viewHolder.deviceBind.setText(getString(R.string.device_unbind));
			}
			viewHolder.deviceAddress.setText(device.getMac());
			return view;
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!deviceList.contains(device)) {
						deviceList.add(device);
						updateList.add(device);
					}

					// mLeDeviceListAdapter.addDevice(device);
					// mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};

	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		TextView deviceBind;
	}

}
