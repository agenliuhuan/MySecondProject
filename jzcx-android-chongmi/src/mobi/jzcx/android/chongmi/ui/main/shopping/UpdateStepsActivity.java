package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class UpdateStepsActivity extends BaseExActivity {
	private final static String TAG = "UpdateStepsActivity";

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothLeService mBluetoothLeService;
	private boolean mConnected = false;
	private String mDeviceName;
	private String mDeviceAddress;

	private BluetoothGattCharacteristic mNotifyCharacteristic;

	private static final int REQUEST_ENABLE_BT = 1;
	private boolean mScanning;
	private static final long SCAN_PERIOD = 1000 * 60;
	static PetBindObject pet;
	TextView statusTV;
	ArrayList<String> F8Steps;

	public static void startActivity(Context context, PetBindObject pet) {
		ActivityUtils.startActivity(context, UpdateStepsActivity.class);
		UpdateStepsActivity.pet = pet;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatesteps);
		initView();
	}
	private void initView() {
		PercentRelativeLayout leftRL = (PercentRelativeLayout) findViewById(R.id.updatesteps_title_leftRL);
		ImageView bluetoothImg = (ImageView) findViewById(R.id.updatesteps_bluetoothimg);
		statusTV = (TextView) findViewById(R.id.updatesteps_statusTV);
		TextView cancelTV = (TextView) findViewById(R.id.updatesteps_cancelTV);

		leftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		cancelTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

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
		F8Steps = new ArrayList<String>();

		Intent gattServiceIntent = new Intent(UpdateStepsActivity.this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
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
		scanLeDevice(true);
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
		unregisterReceiver(mGattUpdateReceiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					statusTV.setText(getString(R.string.petstepsnum_updatenohardware));
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.d(TAG, "petmac" + pet.getMac());
			Log.d(TAG, "mDeviceAddress" + device.getAddress());
			if (pet.getMac().toLowerCase().equals(device.getAddress().replace(":", "").toLowerCase())) {
				if (mScanning) {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					mScanning = false;
				}
				mDeviceName = device.getName();
				mDeviceAddress = device.getAddress();

				mHandler.sendEmptyMessage(1);

			}
		}
	};

	public void handleMessage(Message msg) {
		if (msg.what == 1) {
			statusTV.setText(getString(R.string.petstepsnum_updateing));
			if (mBluetoothLeService != null) {
				final boolean result = mBluetoothLeService.connect(mDeviceAddress);
				Log.d(TAG, "Connect request result=" + result);
			}
		}
	};

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.d(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				Log.d(TAG, "GATT_CONNECTED");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mConnected = false;
				Log.d(TAG, "GATT_DISCONNECTED");
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				Log.d(TAG, "SERVICES_DISCOVERED");
				setBluetoothCurTime();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
				String uuid = intent.getStringExtra(BluetoothLeService.EXTRA_DATA_UUID);
				Log.d(TAG, "EXTRA_DATA:" + data);
				Log.d(TAG, "EXTRA_DATA_UUID:" + uuid);
				if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F3)) {
					writeC0ToF1();
				} else if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F1) && data.trim().equals("C0")) {
					writeB0ToF1();
				} else if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F1) && data.trim().equals("B0")) {
					readF8();
				} else if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F8)) {
					if (F8Steps.contains(data)) {
						sendMessage(YSMSG.REQ_WRITEACTIVITY, Integer.parseInt(pet.getPetId()), 0, getStringFromList());
					} else {
						F8Steps.add(data);
						writeB0ToF1();
					}

				}
			}
		}
	};

	private String getStringFromList() {
		StringBuilder sb = new StringBuilder();
		int length = F8Steps.size();
		for (int i = 0; i < length; i++) {
			String data = F8Steps.get(i);
			if (i == length - 1) {
				sb.append(data.replace(" ", ""));
			} else {
				sb.append(data.replace(" ", "") + ",");
			}
		}
		return sb.toString();
	}

	private void setBluetoothCurTime() {
		String uuid;
		// f3
		List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
		for (BluetoothGattService gattService : gattServices) {
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			uuid = gattService.getUuid().toString();

			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				uuid = gattCharacteristic.getUuid().toString();
				if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F3)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
						if (mNotifyCharacteristic != null) {
							mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
							mNotifyCharacteristic = null;
						}
						mBluetoothLeService.writeCharacteristic(gattCharacteristic, getCurTime());
						LogUtils.d(TAG, "gattService  :  " + uuid + "  writeCharacteristic:" + getCurTime().toString());
					}
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						mNotifyCharacteristic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}

				}

			}
		}
	}

	private String getCurTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		year = year % 100;
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return String.format("%02x", year).toUpperCase() + String.format("%02x", month).toUpperCase()
				+ String.format("%02x", day).toUpperCase() + String.format("%02x", hour).toUpperCase()
				+ String.format("%02x", minute).toUpperCase();
	}

	private void writeC0ToF1() {
		// f1 26 f3
		String uuid;
		// f3
		List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
		for (BluetoothGattService gattService : gattServices) {
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			uuid = gattService.getUuid().toString();
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				uuid = gattCharacteristic.getUuid().toString();
				if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F1)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
						if (mNotifyCharacteristic != null) {
							mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
							mNotifyCharacteristic = null;
						}
						mBluetoothLeService.writeCharacteristic(gattCharacteristic, "C0");
						LogUtils.d(TAG, "gattService  :  " + uuid + "  writeCharacteristic" + "C0");
					}
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						mNotifyCharacteristic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}

				}
			}
		}
	}

	private void writeB0ToF1() {
		// f1 26 f3
		String uuid;
		// f3
		List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
		for (BluetoothGattService gattService : gattServices) {
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			uuid = gattService.getUuid().toString();
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				uuid = gattCharacteristic.getUuid().toString();
				if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F1)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
						if (mNotifyCharacteristic != null) {
							mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
							mNotifyCharacteristic = null;
						}
						mBluetoothLeService.writeCharacteristic(gattCharacteristic, "B0");
						LogUtils.d(TAG, "gattService  :  " + uuid + "  writeCharacteristic" + "B0");
					}
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						mNotifyCharacteristic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}

				}
			}
		}
	}

	private void readF8() {
		// f1 26 f3
		String uuid;
		// f3
		List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
		for (BluetoothGattService gattService : gattServices) {
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			uuid = gattService.getUuid().toString();
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				uuid = gattCharacteristic.getUuid().toString();
				if (uuid.equals(SampleGattAttributes.CLIENT_CHARACTERISTIC_F8)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
						if (mNotifyCharacteristic != null) {
							mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
							mNotifyCharacteristic = null;
						}
						mBluetoothLeService.readCharacteristic(gattCharacteristic);
						LogUtils.d(TAG, "gattService  :  " + uuid + "  readCharacteristic");
					}
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						mNotifyCharacteristic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 从指定数组的copy一个子数组并返回
	 * 
	 * 
	 * 
	 * @param org
	 *            of type byte[] 原数组
	 * 
	 * @param to
	 *            合并一个byte[]
	 * 
	 * @return 合并的数据
	 */

	public static byte[] append(byte[] org, byte[] to) {

		byte[] newByte = new byte[org.length + to.length];

		System.arraycopy(org, 0, newByte, 0, org.length);

		System.arraycopy(to, 0, newByte, org.length, to.length);

		return newByte;

	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}
}
