package mobi.jzcx.android.chongmi.ui.main.shopping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.DayStepObject;
import mobi.jzcx.android.chongmi.biz.vo.GetWeekReportObject;
import mobi.jzcx.android.chongmi.biz.vo.PetBindObject;
import mobi.jzcx.android.chongmi.biz.vo.WeekStepsObject;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.net.ojm.OJMFactory;
import mobi.jzcx.android.core.percent.PercentRelativeLayout;
import mobi.jzcx.android.core.utils.ActivityUtils;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class StepsnumWeekTrendActivity extends BaseExActivity {
	static PetBindObject petbindObj;
	ImageView previousImg;
	ImageView nextImg;
	LineChart mChart;
	TextView daystepsTV;
	TextView countstepsTV;
	TextView topname;
	static Date curDate;
	ArrayList<WeekStepsObject> weekStepsList;

	public static void startActivity(Context context, PetBindObject obj, Date date) {
		ActivityUtils.startActivity(context, StepsnumWeekTrendActivity.class);
		StepsnumWeekTrendActivity.petbindObj = obj;
		StepsnumWeekTrendActivity.curDate = date;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weektrend);
		initView();
		weekStepsList = new ArrayList<WeekStepsObject>();
	}
	private void initView() {
		PercentRelativeLayout leftRL = (PercentRelativeLayout) findViewById(R.id.weektrend_title_leftRL);
		topname = (TextView) findViewById(R.id.weektrend_title_center);
		previousImg = (ImageView) findViewById(R.id.weektrend_previousImg);
		nextImg = (ImageView) findViewById(R.id.weektrend_nextImg);
		mChart = (LineChart) findViewById(R.id.weektrend_stepsLineChart);
		daystepsTV = (TextView) findViewById(R.id.weektrend_daystepsTV);
		countstepsTV = (TextView) findViewById(R.id.weektrend_countstepsTV);

		leftRL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StepsnumWeekTrendActivity.this.finish();
			}
		});

		initLineChart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		if (petbindObj != null) {
			GetWeekReportObject getReport = new GetWeekReportObject();
			Date beginDate = CommonUtils.getMondayOfThisWeek(curDate);
			Date endDate = CommonUtils.getSundayOfThisWeek(curDate);
			getReport.setPetid(petbindObj.getPetId());
			getReport.setBeginTime(CommonUtils.getTime(beginDate));
			getReport.setEndTime(CommonUtils.getTime(endDate));
			sendMessage(YSMSG.REQ_WEEKREPORT, 0, 0, getReport);
		}
	}

	private void setData() {
		ArrayList<Date> days = getWeekDays(CommonUtils.getMondayOfThisWeek(curDate));
		mChart.setData(generateDataLine(days));
		mChart.animateX(1000);
		int weekSteps = getWeekAllStep();
		weekSteps = weekSteps / 7;
		daystepsTV.setText(String.valueOf(weekSteps));
	}

	private LineData generateDataLine(ArrayList<Date> days) {
		ArrayList<Entry> e1 = new ArrayList<Entry>();
		if (weekStepsList == null || weekStepsList.size() == 0) {
			for (int i = 0; i < 7; i++) {
				e1.add(new Entry(0, i));
			}
		} else {
			for (int i = 0; i < 7; i++) {
				WeekStepsObject weekObj = getweekStepObjByDay(days.get(i));
				if (weekObj != null) {
					e1.add(new Entry(getAllStep(weekObj), i));
				} else {
					e1.add(new Entry(0, i));
				}

			}
		}

		LineDataSet d1 = new LineDataSet(e1, "New DataSet");
		d1.setLineWidth(2.5f);
		d1.setCircleSize(4.5f);
		d1.setHighLightColor(Color.rgb(244, 117, 117));
		d1.setDrawValues(true);

		ArrayList<String> xvalus = new ArrayList<String>();
		xvalus.add(getString(R.string.weektrend_week1));
		xvalus.add(getString(R.string.weektrend_week2));
		xvalus.add(getString(R.string.weektrend_week3));
		xvalus.add(getString(R.string.weektrend_week4));
		xvalus.add(getString(R.string.weektrend_week5));
		xvalus.add(getString(R.string.weektrend_week6));
		xvalus.add(getString(R.string.weektrend_week7));
		LineData cd = new LineData(xvalus, d1);
		return cd;
	}

	private int getWeekAllStep() {
		int step = 0;
		for (WeekStepsObject weekStepObj : weekStepsList) {
			step += getAllStep(weekStepObj);
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

	private WeekStepsObject getweekStepObjByDay(Date date) {
		for (WeekStepsObject weekStepObj : weekStepsList) {
			Date weekDate = CommonUtils.getCreateDate(weekStepObj.getDate());
			if (CommonUtils.isSameDate(date, weekDate)) {
				return weekStepObj;
			}
		}
		return null;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_WEEKREPORT :
				if (ActivityUtils.isActivityForeground(getBaseContext(), this.getClass())) {
					if (msg.arg1 == 200) {
						if (msg.obj != null) {
							/*
							 * "List":[{"Date":"2015-12-17 00:00:00","List":[{
							 * "DateDay" :
							 * "2015-12-17 00:00:00","Hours":12,"PetId"
							 * :1,"Step":70, "CreateTime"
							 * :"2015-12-19 16:40:12",
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
								setData();
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
				}

				break;
			default :
				break;
		}
	}

	private void initLineChart() {

		// no description text
		mChart.setDescription("");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable touch gestures
		mChart.setTouchEnabled(false);

		mChart.setDragDecelerationFrictionCoef(0.9f);

		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);
		mChart.setHighlightPerDragEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(true);

		// set an alternative background color
		mChart.setBackgroundColor(Color.TRANSPARENT);

		// add data
		// mChart.setData(generateDataLine());

		// mChart.animateX(1000);

		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();

		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
		// l.setTypeface(tf);
		l.setTextSize(11f);
		l.setTextColor(Color.BLACK);
		l.setPosition(LegendPosition.BELOW_CHART_LEFT);
		// l.setYOffset(11f);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		// xAxis.setTypeface(tf);
		xAxis.setTextSize(12f);
		xAxis.setTextColor(Color.BLACK);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);
		xAxis.setSpaceBetweenLabels(1);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setEnabled(false);
		// leftAxis.setTextColor(ColorTemplate.getHoloBlue());
		// leftAxis.setAxisMaxValue(200f);
		// leftAxis.setDrawGridLines(true);

		YAxis rightAxis = mChart.getAxisRight();
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
