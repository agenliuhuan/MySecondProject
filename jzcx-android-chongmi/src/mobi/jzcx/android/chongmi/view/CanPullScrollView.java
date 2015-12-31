package mobi.jzcx.android.chongmi.view;

import mobi.jzcx.android.core.view.pulltorefresh.Pullable;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class CanPullScrollView extends ScrollView implements Pullable {
	boolean canPullDown = true;
	boolean canPullUp = true;

	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}

	public CanPullScrollView(Context context) {
		super(context);
	}

	public CanPullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CanPullScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (canPullUp) {
			View contentView = getChildAt(0);
			if (contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
				return true;
			}
			return false;

		} else {
			return false;
		}

	}
}
