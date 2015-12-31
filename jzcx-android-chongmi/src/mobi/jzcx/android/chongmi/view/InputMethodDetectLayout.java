package mobi.jzcx.android.chongmi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 输入法弹出检测Layout
 * 
 * @author Rocksen
 * 
 */
public class InputMethodDetectLayout extends ScrollView {
	private OnInputMethodListener mListener;

	public interface OnInputMethodListener {
		void OnInputMethodResize(int w, int h, int oldw, int oldh);
	}

	public void setOnInputMethodListener(OnInputMethodListener l) {
		mListener = l;
	}

	public InputMethodDetectLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mListener != null) {
			mListener.OnInputMethodResize(w, h, oldw, oldh);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

	}
}