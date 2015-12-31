package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.DayStepObject;
import mobi.jzcx.android.chongmi.biz.vo.GetWeekReportObject;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.biz.vo.WeekStepsObject;
import mobi.jzcx.android.chongmi.ui.adapter.WeekAdapter;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.Constant;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.chongmi.view.HorizontalListView;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class PetStepsnumActivity extends BaseExActivity {
	static PetBindObject petbindObj;

	LineChart stepsLineChart;
	PercentRelativeLayout hasUpdateRL;
	PercentRelativeLayout notUpdateRL;
	HorizontalListView weekListView;
	WeekAdapter weekAdapter;
	ArrayList<WeekStepsObject> weekStepsList;
	WeekStepsObject curWeekStepObj;
	Date curDate;
	TextView stepsTV;

	public static void startActivity(Context context, PetBindObject petbindObj) {
		ActivityUtils.startActivity(context, PetStepsnumActivity.class);
		PetStepsnumActivity.petbindObj = petbindObj;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petstepsnum);
		initView();
		Constant.selectDate = null;
		weekStepsList = new ArrayList<WeekStepsObject>();
	}
	private void initView() {
		hasUpdateRL = (PercentRelativeLayout) findViewById(R.id.hasUpdateRL);
		notUpdateRL = (PercentRelativeLayout) findViewById(R.id.notUpdateRL);
		ImageView notUpdateImg = (ImageView) findViewById(R.id.petstepsnum_notupdateImg);
		notUpdateImg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UpdateStepsActivity.startActivity(mActivity, petbindObj);
			}
		});

		PercentRelativeLayout leftRL = (PercentRelativeLayout) findViewById(R.id.petstepsnum_title_leftRL);
		ImageView petimage = (ImageView) findViewById(R.id.petstepsnum_centerimg);
		TextView petname = (TextView) findViewById(R.id.petstepsnum_title_center);
		TextView rightTv = (TextView) findViewById(R.id.petstepsnum_title_righttext);
		rightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HardwarePetListActivity.startActivity(mActivity);
			}
		});

		TextView todayTV = (TextView) findViewById(R.id.petstepsnum_today);

		ImageView calendarImg = (ImageView) findViewById(R.id.petstepsnum_calendar);

		if (!CommonTextUtils.isEmpty(petbindObj.getIcoUrl())) {
			ImageLoaderHelper.displayAvatar(petbindObj.getIcoUrl() + CommonUtils.getAvatarSize(mActivity), petimage,
					R.drawable.avatar_default_image);
		}
		if (!CommonTextUtils.isEmpty(petbindObj.getName())) {
			petname.setText(petbindObj.getName());
		}

		calendarImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HardwareCalendarActivity.startActivity(mActivity);
			}
		});
		leftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PetStepsnumActivity.this.finish();
			}
		});
		weekListView = (HorizontalListView) findViewById(R.id.petstepsnum_weeklist);
		weekAdapter = new WeekAdapter(mActivity);
		weekListView.setAdapter(weekAdapter);

		Date startDay = CommonUtils.getMondayOfThisWeek(new Date());
		ArrayList<Date> days = getWeekDays(startDay);
		weekAdapter.updateList(days);

		stepsTV = (TextView) findViewById(R.id.petstepsnum_setps);
		ImageView previousTV = (ImageView) findViewById(R.id.petstepsnum_previous);
		ImageView nextTV = (ImageView) findViewById(R.id.petstepsnum_next);
		ImageView shareTV = (ImageView) findViewById(R.id.petstepsnum_share);
		ImageView updateTV = (ImageView) findViewById(R.id.petstepsnum_update);
		updateTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UpdateStepsActivity.startActivity(mActivity, petbindObj);
			}
		});
		TextView weekStepsnum = (TextView) findViewById(R.id.petstepsnum_weekTrendTV);
		weekStepsnum.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StepsnumWeekTrendActivity.startActivity(mActivity, petbindObj, curDate);
			}
		});

		stepsLineChart = (LineChart) findViewById(R.id.petstepsnum_stepsLineChart);

		initLineChart();

		if (petbindObj != null) {
			curDate = new Date();
			GetWeekReportObject getReport = new GetWeekReportObject();
			Date beginDate = CommonUtils.getMondayOfThisWeek(curDate);
			Date endDate = CommonUtils.getSundayOfThisWeek(curDate);
			getReport.setPetid(petbindObj.getPetId());
			getReport.setBeginTime(CommonUtils.getTime(beginDate));
			getReport.setEndTime(CommonUtils.getTime(endDate));
			sendMessage(YSMSG.REQ_ONEWEEKREPORT, 0, 0, getReport);
		}

		weekListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				curDate = (Date) parent.getAdapter().getItem(position);
				getDaySteps();
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.selectDate != null) {
			curDate = Constant.selectDate;
			Date startDay = CommonUtils.getMondayOfThisWeek(Constant.selectDate);
			ArrayList<Date> days = getWeekDays(startDay);
			weekAdapter.updateList(days);

			if (petbindObj != null) {
				GetWeekReportObject getReport = new GetWeekReportObject();
				Date beginDate = CommonUtils.getMondayOfThisWeek(Constant.selectDate);
				Date endDate = CommonUtils.getSundayOfThisWeek(Constant.selectDate);
				getReport.setPetid(petbindObj.getPetId());
				getReport.setBeginTime(CommonUtils.getTime(beginDate));
				getReport.setEndTime(CommonUtils.getTime(endDate));

				sendMessage(YSMSG.REQ_ONEWEEKREPORT, 0, 0, getReport);
			}
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_ONEWEEKREPORT :
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						/*
						 * "List":[{"Date":"2015-12-17 00:00:00","List":[{
						 * "DateDay" : "2015-12-17 00:00:00","Hours":12,"PetId"
						 * :1,"Step":70, "CreateTime" :"2015-12-19 16:40:12",
						 * "UpdateTime":"2015-12-19 16:40:12" }
						 * ,{"DateDay":"2015-12-17 00:00:00"
						 * ,"Hours":18,"PetId":
						 * 1,"Step":86,"CreateTime":"2015-12-19 16:40:12",
						 * "UpdateTime":"2015-12-19 16:40:12"}]
						 */
						String result = (String) msg.obj;
						try {
							JSONObject jsonobj = new JSONObject(result);
							JSONArray array = jsonobj.getJSONArray("List");
							weekStepsList.clear();
							JSONObject weekObj;
							JSONArray dayArray;
							for (int i = 0; i < array.length(); i++) {
								weekObj = array.getJSONObject(i);
								WeekStepsObject weekStepObj = new WeekStepsObject();
								weekStepObj.setDate(weekObj.getString("Date"));
								dayArray = weekObj.getJSONArray("List");
								ArrayList<DayStepObject> dayList = new ArrayList<DayStepObject>();
								for (int j = 0; j < dayArray.length(); j++) {
									DayStepObject dayObj = OJMFactory.createOJM().fromJson(dayArray.getString(j),
											DayStepObject.class);
									dayList.add(dayObj);
								}
								weekStepObj.setDayStepList(dayList);
								weekStepsList.add(weekStepObj);
							}
							getDaySteps();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					YSToast.showToast(mActivity, String.valueOf(msg.obj));
				}

				break;
			default :
				break;
		}
	}

	private void getDaySteps() {
		if (weekStepsList != null && weekStepsList.size() > 0) {
			for (WeekStepsObject weekStepObj : weekStepsList) {
				Date date = CommonUtils.getCreateDate(weekStepObj.getDate());
				if (CommonUtils.isSameDate(curDate, date)) {
					hasUpdateRL.setVisibility(View.VISIBLE);
					notUpdateRL.setVisibility(View.GONE);
					setData(weekStepObj);
				}
			}
		} else {
			if (CommonUtils.isSameDate(curDate, new Date())) {
				hasUpdateRL.setVisibility(View.GONE);
				notUpdateRL.setVisibility(View.VISIBLE);
			} else {
				hasUpdateRL.setVisibility(View.VISIBLE);
				notUpdateRL.setVisibility(View.GONE);

			}
		}
	}

	private void setData(WeekStepsObject weekStepObj) {
		stepsLineChart.setData(generateDataLine(weekStepObj));
		stepsLineChart.animateX(1000);
		int dayAllSteps = getAllStep(weekStepObj);
		stepsTV.setText(String.valueOf(dayAllSteps));
	}

	private LineData generateDataLine(WeekStepsObject weekStepObj) {
		ArrayList<Entry> e1 = new ArrayList<Entry>();
		ArrayList<DayStepObject> dayStepsList = weekStepObj.getDayStepList();
		if (dayStepsList == null || dayStepsList.size() == 0) {
			for (int i = 0; i < 4; i++) {
				e1.add(new Entry(0, i));
			}
		} else {
			for (int i = 0; i < 4; i++) {
				e1.add(new Entry(getStepByIndex(dayStepsList, i), i));
			}
		}

		LineDataSet d1 = new LineDataSet(e1, "New DataSet");
		d1.setLineWidth(2.5f);
		d1.setCircleSize(4.5f);
		d1.setHighLightColor(Color.rgb(244, 117, 117));
		d1.setDrawValues(true);

		ArrayList<String> xvalus = new ArrayList<String>();
		xvalus.add("06:00");
		xvalus.add("12:00");
		xvalus.add("18:00");
		xvalus.add("00:00");

		LineData cd = new LineData(xvalus, d1);
		return cd;
	}

	private int getStepByIndex(ArrayList<DayStepObject> dayStepsList, int i) {
		int step = 0;
		for (DayStepObject dayStepObj : dayStepsList) {
			if (i == 0 && dayStepObj.getHours() == 6) {
				step = dayStepObj.getStep();
			}
			if (i == 1 && dayStepObj.getHours() == 12) {
				step = dayStepObj.getStep();
			}
			if (i == 2 && dayStepObj.getHours() == 18) {
				step = dayStepObj.getStep();
			}
			if (i == 3 && dayStepObj.getHours() == 24) {
				step = dayStepObj.getStep();
			}
		}
		return step;
	}

	private int getAllStep(WeekStepsObject weekStepObj) {
		int step = 0;
		for (DayStepObject dayStepObj : weekStepObj.getDayStepList()) {
			step += dayStepObj.getStep();
		}
		return step;
	}

	private void initLineChart() {

		// no description text
		stepsLineChart.setDescription("");
		stepsLineChart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable touch gestures
		stepsLineChart.setTouchEnabled(false);

		stepsLineChart.setDragDecelerationFrictionCoef(0.9f);

		// enable scaling and dragging
		stepsLineChart.setDragEnabled(true);
		stepsLineChart.setScaleEnabled(true);
		stepsLineChart.setDrawGridBackground(false);
		stepsLineChart.setHighlightPerDragEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		stepsLineChart.setPinchZoom(true);

		// set an alternative background color
		stepsLineChart.setBackgroundColor(Color.TRANSPARENT);

		// add data
		// stepsLineChart.setData(generateDataLine());

		// stepsLineChart.animateX(1000);

		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");

		// get the legend (only possible after setting data)
		Legend l = stepsLineChart.getLegend();

		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
		// l.setTypeface(tf);
		l.setTextSize(11f);
		l.setTextColor(Color.BLACK);
		l.setPosition(LegendPosition.BELOW_CHART_LEFT);
		// l.setYOffset(11f);

		XAxis xAxis = stepsLineChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		// xAxis.setTypeface(tf);
		xAxis.setTextSize(12f);
		xAxis.setTextColor(Color.BLACK);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);
		xAxis.setSpaceBetweenLabels(1);

		YAxis leftAxis = stepsLineChart.getAxisLeft();
		leftAxis.setEnabled(false);
		// leftAxis.setTextColor(ColorTemplate.getHoloBlue());
		// leftAxis.setAxisMaxValue(200f);
		// leftAxis.setDrawGridLines(true);

		YAxis rightAxis = stepsLineChart.getAxisRight();
		rightAxis.setEnabled(false);
		// rightAxis.setTypeface(tf);
		// rightAxis.setTextColor(Color.RED);
		// rightAxis.setAxisMaxValue(900);
		// rightAxis.setStartAtZero(false);
		// rightAxis.setAxisMinValue(-200);
		// rightAxis.setDrawGridLines(false);
	}

	public ArrayList<Date> getWeekDays(Date date) {
		ArrayList<Date> days = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		days.add(date);
		for (int i = 0; i < 6; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date weekdate = calendar.getTime();
			days.add(weekdate);
		}
		return days;
	}

}
