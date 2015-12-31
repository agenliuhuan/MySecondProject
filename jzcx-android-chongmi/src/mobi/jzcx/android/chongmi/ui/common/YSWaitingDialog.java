package mobi.jzcx.android.chongmi.ui.common;

import android.content.Context;
import android.widget.TextView;
import mobi.jzcx.android.chongmi.R;

/**
 * 转菊花的等待框-setProHintStr设置菊花下方的提示字符串
 * 
 * @author admin
 * 
 */
public class YSWaitingDialog extends YSBaseDialog {

	public static final String TAG = YSWaitingDialog.class.getSimpleName();
	private TextView mProHint;

	public YSWaitingDialog(Context context) {
		super(context, R.style.unified_loading_dialog);
		setContentView(R.layout.comm_loading_view);
		setCanceledOnTouchOutside(false);
		// setCancelable(false);
		initUI();
	}

	/**
	 * 设置菊花底部的提示字符串
	 * 
	 * @param hintString
	 */
	public void setProHintStr(String hintString) {
		mProHint.setText(hintString);
	}

	private void initUI() {
		mProHint = (TextView) findViewById(R.id.unified_loading_view_text);
	}

}
