package mobi.jzcx.android.chongmi.ui.group;

import java.util.ArrayList;
import java.util.List;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class EventDidianActivity extends BaseExActivity implements OnItemClickListener, OnGetPoiSearchResultListener {

	protected TitleBarHolder mTitleBar;
	PoiSearch mPoiSearch;
	LocationAdapter adapter = null;
	ArrayList<Object> list = new ArrayList<Object>();
	private EditText keyWorldsView = null;
	View emptyview;
	ListView listview;

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, EventDidianActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_dd);

		initView();
		initData();
	}

	private void initData() {
		keyWorldsView = (EditText) findViewById(R.id.edt_event_location);
		emptyview = findViewById(R.id.locationemptyLL);
		listview = (ListView) findViewById(R.id.location_list);
		adapter = new LocationAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.length() < 0) {
					return;
				}
				if (arg0.length() == 0) {
					emptyview.setVisibility(View.GONE);
					listview.setVisibility(View.GONE);
				}
				String city = "";
				if (App.getInstance().getLnglat() != null) {
					city = App.getInstance().getLnglat().getCity();
				}
				mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city).keyword(arg0.toString()).pageNum(10));
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

			}
		});
		keyWorldsView.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(keyWorldsView, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.createevent_locationtext));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = adapter.getItem(position);
		if (obj instanceof PoiInfo) {
			PoiInfo info = (PoiInfo) obj;
			LngLatObject lObj = new LngLatObject();
			if (!TextUtils.isEmpty(info.address + info.name)) {
				lObj.setAdress(info.address + info.name);
			}
			if (info.location != null) {
				lObj.setLat(String.valueOf(info.location.latitude));
			}
			if (info.location != null) {
				lObj.setLng(String.valueOf(info.location.longitude));
			}
			Constant.eventlocation = lObj;
			finish();
		}
		if (obj instanceof PoiAddrInfo) {
			PoiAddrInfo info = (PoiAddrInfo) obj;
			LngLatObject lObj = new LngLatObject();
			if (!TextUtils.isEmpty(info.address + info.name)) {
				lObj.setAdress(info.address + info.name);
			}
			if (info.location != null) {
				lObj.setLat(String.valueOf(info.location.latitude));
			}
			if (info.location != null) {
				lObj.setLng(String.valueOf(info.location.longitude));
			}
			Constant.eventlocation = lObj;
			finish();
		}
	}
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {

	}

	@Override
	public void onGetPoiResult(PoiResult result) {

		if (keyWorldsView.length() == 0) {
			emptyview.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			list.clear();

			if (result.isHasAddrInfo()) {
				List<PoiAddrInfo> addrList = result.getAllAddr();
				list.addAll(addrList);
			}

			List<PoiInfo> poi = result.getAllPoi();
			list.addAll(poi);
			if (list.size() > 0) {
				emptyview.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
			} else {
				emptyview.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
			}
			adapter.setData(list);
		} else {
			emptyview.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}

	}
}
