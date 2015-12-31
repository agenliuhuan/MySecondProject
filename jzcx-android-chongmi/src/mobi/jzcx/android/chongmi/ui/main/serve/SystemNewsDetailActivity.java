package mobi.jzcx.android.chongmi.ui.main.serve;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.TitleBarHolder;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SystemNewsDetailActivity extends BaseExActivity {
	protected TitleBarHolder mTitleBar = null;
	static SystemNoticeObject sysNoticeObj;
	TextView timeTv;
	TextView titleTv;
	TextView contentTv;

	public static void startActivity(Context context, SystemNoticeObject sysNoticeObj) {
		ActivityUtils.startActivity(context, SystemNewsDetailActivity.class);
		SystemNewsDetailActivity.sysNoticeObj = sysNoticeObj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sysnewsdetail);
		initView();
		initData();
	}

	private void initData() {
		if (sysNoticeObj != null) {
			timeTv.setText(CommonUtils.getTimeSamp(mActivity, sysNoticeObj.getCreateTime()));
			titleTv.setText(sysNoticeObj.getTitle());
			contentTv.setText(sysNoticeObj.getContext());
		}
	}

	private void initView() {
		initTitleBar();
		timeTv = (TextView) findViewById(R.id.sysdetail_time);
		titleTv = (TextView) findViewById(R.id.sysdetail_title);
		contentTv = (TextView) findViewById(R.id.sysdetail_content);

	}

	private void initTitleBar() {
		mTitleBar = new TitleBarHolder(this);
		mTitleBar.mTitle.setText(getString(R.string.sysnewsdetail_title));
		mTitleBar.titleLeftRL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SystemNewsDetailActivity.this.finish();
			}
		});
	}

}
