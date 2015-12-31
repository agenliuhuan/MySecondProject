package mobi.jzcx.android.chongmi.view;

import mobi.jzcx.android.core.view.pulltorefresh.Pullable;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CanPullListView extends ListView implements Pullable {
	boolean canPullDown = true;
	boolean canPullUp = true;

	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}

	public CanPullListView(Context context) {
		super(context);
	}

	public CanPullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CanPullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (canPullDown) {
			if (getCount() == 0) {
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
				// 滑到ListView的顶部了
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public boolean canPullUp() {
		if (canPullUp) {
			if (getCount() == 0) {
				return false;
			} else if (getLastVisiblePosition() == (getCount() - 1)) {
				// 滑到底部了
				if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
						&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
					return true;
				} else {
					return false;
				}
			} else
				return false;

		} else {
			return false;
		}

	}
}
