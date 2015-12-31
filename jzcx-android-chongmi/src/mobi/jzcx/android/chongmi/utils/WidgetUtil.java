package mobi.jzcx.android.chongmi.utils;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public class WidgetUtil {
	public static void handleTextView(TextView textView, String textStr) {
		textView.setText(textStr);// 首先要赋值一次，让系统自动处理，产生自动换行
		ViewTreeObserver vto = textView.getViewTreeObserver();
		MyOnGlobalLayoutListener layoutListener = new MyOnGlobalLayoutListener(textView, textStr);
		vto.addOnGlobalLayoutListener(layoutListener);
	}
}

class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {
	private TextView textView;
	private String textValue;

	public MyOnGlobalLayoutListener(TextView textView, String textValue) {
		this.textView = textView;
		this.textValue = textValue;

	}

	@Override
	public void onGlobalLayout() {
		ViewTreeObserver obs = textView.getViewTreeObserver();
		obs.removeGlobalOnLayoutListener(this);
		if (textView.getLineCount() > 10)// 如果一行显示不下而自动换行，所以要在前台文件作修改，去掉singleLine=true，否则该条件不会成立。
		{
			int lineEndIndex = this.textView.getLayout().getLineEnd(9);// 获取被截断的字符长度
			String text = textValue.subSequence(0, lineEndIndex - 6) + "..."
					+ App.getInstance().getString(R.string.questionofficial_hasmore);// 手动加上省略号
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			// style.setSpan(new
			// BackgroundColorSpan(Color.parseColor("#ff6600")), text.length() -
			// 4, text.length(),
			// Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置textview的背景颜色
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff6600")), text.length() - 4, text.length(),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
			textView.setText(style);
		} else {
			textView.setText(textValue);
		}
	}
}
