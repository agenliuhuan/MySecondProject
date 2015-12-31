package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.Calendar;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.view.calendar.CalendarDay;
import mobi.jzcx.android.chongmi.view.calendar.MaterialCalendarView;
import mobi.jzcx.android.chongmi.view.calendar.OnDateSelectedListener;
import mobi.jzcx.android.chongmi.view.calendar.OnMonthChangedListener;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HardwareCalendarActivity extends BaseExActivity implements OnDateSelectedListener, OnMonthChangedListener {

	public static void startActivity(Context context) {
		ActivityUtils.startActivity(context, HardwareCalendarActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		initView();
	}
	private void initView() {
		TextView leftTV = (TextView) findViewById(R.id.calendar_title_lefttext);
		ImageView calendar_centerImg = (ImageView) findViewById(R.id.calendar_centerimg);
		TextView calendar_centerTv = (TextView) findViewById(R.id.weektrend_title_center);
		MaterialCalendarView cardpager = (MaterialCalendarView) findViewById(R.id.calendar_cardpager);
		cardpager.setOnDateChangedListener(this);
		cardpager.setOnMonthChangedListener(this);
		cardpager.setMaximumDate(Calendar.getInstance());
		cardpager.setPagingEnabled(true);
		leftTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HardwareCalendarActivity.this.finish();
			}
		});
	}

	@Override
	public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
		HardwareCalendarActivity.this.finish();
		Constant.selectDate = date.getDate();
	}

	@Override
	public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

	}
}
