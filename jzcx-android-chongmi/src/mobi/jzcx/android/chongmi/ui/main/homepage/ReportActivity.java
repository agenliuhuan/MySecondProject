package mobi.jzcx.android.chongmi.ui.main.homepage;

import java.util.ArrayList;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ReportObject;
import mobi.jzcx.android.chongmi.ui.adapter.ReportAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ReportActivity extends BaseExActivity {
	static ReportObject reportObj;
	TitleBarHolder mTitleBar;
	ReportAdapter reportAdapter;
	int curposition = -1;

	public static void startActivity(Context context, ReportObject cObj) {
		ActivityUtils.startActivity(context, ReportActivity.class);
		ReportActivity.reportObj = cObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		initView();
		initData();
	}

	private void initView() {
		initTitleBar();
		ListView listview = (ListView) findViewById(R.id.reportListview);
		reportAdapter = new ReportAdapter(mActivity);
		listview.setAdapter(reportAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				curposition = position;
				reportAdapter.setSelect(position);
				reportObj.setReason(reportAdapter.getItem(position));
			}
		});

	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.report_ttb_title));
		mTitleBar.mRightTv.setText(R.string.report_ttb_righttv);
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReportActivity.this.finish();
			}
		});
		mTitleBar.mRightTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (curposition == -1) {
					YSToast.showToast(mActivity, getString(R.string.toast_report_notnull));
				} else {
					showWaitingDialog();
					sendMessage(YSMSG.REQ_REPORT, 0, 0, reportObj);
				}
			}
		});

	}

	@Override
	public void handleMessage(Message msg) {
		dismissWaitingDialog();
		if (msg.what == YSMSG.RESP_REPORT) {
			if (msg.arg1 == 200) {
				finish();
				ReportOverActivity.startActivity(mActivity);
			} else {
				YSToast.showToast(mActivity, String.valueOf(msg.obj));
			}
		}
	}

	private void initData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getString(R.string.report_item1));
		list.add(getString(R.string.report_item2));
		list.add(getString(R.string.report_item3));
		list.add(getString(R.string.report_item4));
		list.add(getString(R.string.report_item5));
		list.add(getString(R.string.report_other));
		reportAdapter.updateList(list);
	}
}
