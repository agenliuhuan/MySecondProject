package mobi.jzcx.android.chongmi.ui.common;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 公用TitleBar 辅助初始Titlebar的各个子控件。
 * 
 * @author
 * 
 */
public class TitleBarHolder {
	public PercentRelativeLayout titleRL;
	public PercentRelativeLayout titleLeftRL;
	public PercentRelativeLayout titleRightRL;
	public ImageView mLeftImg;
	public TextView mTitle;
	public TextView mRightTv;
	public ImageView mRightImg;

	public TitleBarHolder(final Activity activity) {
		titleRL = (PercentRelativeLayout) activity.findViewById(R.id.title_all);
		titleLeftRL = (PercentRelativeLayout) activity.findViewById(R.id.title_leftRL);
		titleRightRL = (PercentRelativeLayout) activity.findViewById(R.id.title_rightRL);
		mLeftImg = (ImageView) activity.findViewById(R.id.leftbtn);
		mTitle = (TextView) activity.findViewById(R.id.txt_title_center);
		mRightTv = (TextView) activity.findViewById(R.id.rightText);
		mRightImg = (ImageView) activity.findViewById(R.id.rightbtn);
		if (null != titleLeftRL) {
			titleLeftRL.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					activity.finish();
//					activity.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
				}
			});
		}
	}

	public TitleBarHolder(final Activity act, View group) {
		titleRL = (PercentRelativeLayout) group.findViewById(R.id.title_all);
		titleLeftRL = (PercentRelativeLayout) group.findViewById(R.id.title_leftRL);
		titleRightRL = (PercentRelativeLayout) group.findViewById(R.id.title_rightRL);
		mLeftImg = (ImageView) group.findViewById(R.id.leftbtn);
		mTitle = (TextView) group.findViewById(R.id.txt_title_center);
		mRightTv = (TextView) group.findViewById(R.id.rightText);
		mRightImg = (ImageView) group.findViewById(R.id.rightbtn);
		if (null != titleLeftRL) {
			titleLeftRL.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					act.finish();
//					act.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
				}
			});
		}
	}

}
