package mobi.jzcx.android.chongmi.view.swipe;

import mobi.jzcx.android.core.percent.PercentLinearLayout;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by foul on 14/12/8.
 */
public class SwipeViewHolder {
	public HorizontalScrollView hSView;
	public LinearLayout viewContainer;
	public SimpleDraweeView userimg;
	public TextView voicenameTv;
	public TextView timeTv;
	public TextView contentTv;
	public PercentRelativeLayout voiceRL;
	public PercentLinearLayout replyLL;
	public TextView replyNameTV;
	public ImageView voiceImg;
	public TextView voiceText;
	public ImageView deleteImg;
	public SimpleDraweeView detailimg;
}
