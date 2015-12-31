package mobi.jzcx.android.chongmi.view;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.core.percent.PercentLinearLayout;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class ImagePickerPopupWindow extends PopupWindow {

	private Button snapshotBtn, albumBtn, cancelBtn;
	private View mMenuView;

	public ImagePickerPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.comm_image_picker, null);
		snapshotBtn = (Button) mMenuView.findViewById(R.id.btn_image_picker_snapshot);
		albumBtn = (Button) mMenuView.findViewById(R.id.btn_image_picker_album);
		cancelBtn = (Button) mMenuView.findViewById(R.id.btn_image_picker_cancel);
		// 设置按钮监听
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		snapshotBtn.setOnClickListener(itemsOnClick);
		albumBtn.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x20000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				dismiss();
				return true;
			}
		});
	}

}
