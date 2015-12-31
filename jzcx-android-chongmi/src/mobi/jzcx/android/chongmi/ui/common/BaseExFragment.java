package mobi.jzcx.android.chongmi.ui.common;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.core.mvc.BaseFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;

public class BaseExFragment extends BaseFragment {

	protected boolean mIsFragmentVisible;
	protected boolean mPause = false;
	protected InternalReceiver internalReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIsFragmentVisible = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		mPause = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		mPause = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mIsFragmentVisible = true;
		} else {
			mIsFragmentVisible = false;
		}
	}

	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		// intentfilter.addAction(CASIntent.ACTION_SERVICE_DESTORY);
		// intentfilter.addAction(CASIntent.ACTION_FORCE_DEACTIVE);
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		getActivity().registerReceiver(internalReceiver, intentfilter);
	}

	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getAction() == null) {
				return;
			}
			handleReceiver(context, intent);
		}

	}

	protected void handleReceiver(Context context, Intent intent) {

	}

	private YSWaitingDialog mWaitingDialog;

	public void showWaitingDialog() {
		if (null == mWaitingDialog) {
			mWaitingDialog = new YSWaitingDialog(getActivity());
		}

		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showOrUpdateWaitingDialog(String message) {

		if (null == mWaitingDialog) {
			mWaitingDialog = new YSWaitingDialog(getActivity());
		}

		mWaitingDialog.setProHintStr(message);

		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showOrUpdateWaitingDialog(int resId) {
		showOrUpdateWaitingDialog(getString(resId));
	}

	public void updateWaitingDialogResource(int resId) {
		if (null != mWaitingDialog) {
			ProgressBar pb = (ProgressBar) mWaitingDialog.findViewById(R.id.unified_loading_view_circle);
			if (pb != null) {
				mWaitingDialog.setCanceledOnTouchOutside(true);
				pb.setIndeterminateDrawable(null);
				pb.setBackgroundResource(resId);
			}
		}
	}

	public void dismissWaitingDialog() {

		if (null != mWaitingDialog && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
			mWaitingDialog = null;
		}
	}

	public boolean isWaitingDialogShowing() {
		return null != mWaitingDialog && mWaitingDialog.isShowing();
	}
}
