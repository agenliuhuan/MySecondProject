package mobi.jzcx.android.chongmi.view;

import mobi.jzcx.android.core.mvc.utils.LogUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollview extends ScrollView {
	public VerticalScrollview(Context context) {
		super(context);
	}

	public VerticalScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalScrollview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			LogUtils.i("VerticalScrollview", "onInterceptTouchEvent: DOWN super false");
			super.onTouchEvent(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			return false; // redirect MotionEvents to ourself

		case MotionEvent.ACTION_CANCEL:
			LogUtils.i("VerticalScrollview", "onInterceptTouchEvent: CANCEL super false");
			super.onTouchEvent(ev);
			break;

		case MotionEvent.ACTION_UP:
			LogUtils.i("VerticalScrollview", "onInterceptTouchEvent: UP super false");
			return false;

		default:
			LogUtils.i("VerticalScrollview", "onInterceptTouchEvent: " + action);
			break;
		}

		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		LogUtils.i("VerticalScrollview", "onTouchEvent. action: " + ev.getAction());
		return true;
	}
}
