package mobi.jzcx.android.chongmi.ui.main.mine;

import java.util.ArrayList;
import java.util.Collections;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.PetStyleObject;
import mobi.jzcx.android.chongmi.ui.adapter.PetCategoryAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.view.CharacterParser;
import mobi.jzcx.android.chongmi.view.PinyinComparator;
import mobi.jzcx.android.chongmi.view.SideBar;
import mobi.jzcx.android.chongmi.view.SideBar.OnTouchingLetterChangedListener;
import mobi.jzcx.android.core.async.TinyAsyncTask;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PetCategorysActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar;
	View topview;
	LinearLayout serchLL;
	TextView cancelTv;
	EditText edit;
	static String url = "";
	ArrayList<PetStyleObject> objList;
	ArrayList<PetStyleObject> filterList;
	PetCategoryAdapter adapter;
	ListView listview;
	PinyinComparator pinyinComparator;
	CharacterParser characterParser;
	PercentRelativeLayout listRL;
	TextView noresultTv;
	SideBar sidebar;

	public static void startActivity(Context context, String url) {
		ActivityUtils.startActivity(context, PetCategorysActivity.class);
		PetCategorysActivity.url = url;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petcategorys);

		initView();
		initData();
	}

	private void initData() {
		sendMessage(YSMSG.REQ_GETPETCATEGORIES, 0, 0, url);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETPETCATEGORIES :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						//
						String result = (String) msg.obj;
						try {
							objList.clear();
							JSONObject json = new JSONObject(result);
							json = json.getJSONObject("Result");
							JSONArray array = json.getJSONArray("List");
							for (int i = 0; i < array.length(); i++) {
								PetStyleObject styleObj = OJMFactory.createOJM().fromJson(array.getString(i),
										PetStyleObject.class);
								objList.add(styleObj);
							}
							Collections.sort(objList, pinyinComparator);
							adapter.updateListView(objList);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				break;
		}
	}

	private void initView() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(R.string.petcategory_ttb_title);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PetCategorysActivity.this.finish();
				PetStyleActivity.startActivity(mActivity);
			}
		});
		serchLL = (LinearLayout) findViewById(R.id.petcategorys_serchLL);
		topview = findViewById(R.id.petcategorys_title);
		cancelTv = (TextView) findViewById(R.id.petcategorys_canceleditTv);
		edit = (EditText) findViewById(R.id.petcategorys_edit);
		listRL = (PercentRelativeLayout) findViewById(R.id.petcategorys_listRL);
		noresultTv = (TextView) findViewById(R.id.petcategorys_noresultTV);
		serchLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topview.setVisibility(View.GONE);
				serchLL.setVisibility(View.GONE);
				listRL.setVisibility(View.GONE);
				sidebar.setVisibility(View.GONE);
				cancelTv.setVisibility(View.VISIBLE);
				edit.setVisibility(View.VISIBLE);
				edit.requestFocus();
				showkeyboard();
			}
		});
		cancelTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				cancelTv.setVisibility(View.GONE);
				adapter.updateListView(objList);
				edit.setText("");
				edit.clearFocus();
				edit.setVisibility(View.GONE);
				noresultTv.setVisibility(View.GONE);
				sidebar.setVisibility(View.VISIBLE);
				listRL.setVisibility(View.VISIBLE);
				topview.setVisibility(View.VISIBLE);
				serchLL.setVisibility(View.VISIBLE);
			}
		});
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		objList = new ArrayList<PetStyleObject>();
		filterList = new ArrayList<PetStyleObject>();
		listview = (ListView) findViewById(R.id.petcategorys_listview);
		adapter = new PetCategoryAdapter(mActivity);
		listview.setAdapter(adapter);

		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});

		sidebar = (SideBar) findViewById(R.id.sidebar);
		TextView dialog = (TextView) findViewById(R.id.dialog);
		sidebar.setTextView(dialog);

		// 设置右侧触摸监听
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listview.setSelection(position);
				}
			}
		});
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				LogUtils.i("firstVisibleItem", firstVisibleItem + "");
				LogUtils.i("visibleItemCount", visibleItemCount + "");
				LogUtils.i("totalItemCount", totalItemCount + "");
				if (adapter.getCount() > 0) {
					String cur = adapter.getItem(firstVisibleItem).getSpell();
					sidebar.setChooseItem(cur);
				}
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Constant.petStyle = adapter.getItem(position).getCategoryId();
				Constant.petCategory = adapter.getItem(position).getName();
				finish();
			}
		});

		// 根据输入框输入值的改变来过滤搜索
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				filterData(s.toString());
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
			if (edit.isShown()) {
				hideSoftKeyboard();
				cancelTv.setVisibility(View.GONE);
				adapter.updateListView(objList);
				edit.setText("");
				edit.clearFocus();
				edit.setVisibility(View.GONE);
				noresultTv.setVisibility(View.GONE);
				sidebar.setVisibility(View.VISIBLE);
				listRL.setVisibility(View.VISIBLE);
				topview.setVisibility(View.VISIBLE);
				serchLL.setVisibility(View.VISIBLE);
				return true;
			} else {
				PetCategorysActivity.this.finish();
				PetStyleActivity.startActivity(mActivity);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showkeyboard() {
		InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void filterData(final String filterStr) {
		if (CommonTextUtils.isEmpty(filterStr)) {
			// adapter.updateListView(objList);
		} else {
			TinyAsyncTask<Object> asyncTask = new TinyAsyncTask<Object>() {
				@Override
				protected Object doInBackground() throws Exception {
					filterList.clear();
					for (PetStyleObject sortModel : objList) {
						String name = sortModel.getName();
						if (characterParser.getSelling(name).toUpperCase().contains(filterStr.toString().toUpperCase())
								|| name.contains(filterStr)) {
							filterList.add(sortModel);
						}
					}
					Collections.sort(filterList, pinyinComparator);
					return filterList;
				}

				@Override
				protected void onPostExecuteSafely(Object result, Exception e) throws Exception {
					if (filterList.size() > 0) {
						listRL.setVisibility(View.VISIBLE);
						noresultTv.setVisibility(View.GONE);
					} else {
						listRL.setVisibility(View.GONE);
						noresultTv.setVisibility(View.VISIBLE);
					}
					adapter.updateListView(filterList);
				}
			};
			asyncTask.execute();

		}
	}

}
