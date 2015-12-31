package mobi.jzcx.android.chongmi.sdk.im.view;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.sdk.im.mode.EaseVoiceRecorder;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseCommonUtils;
import mobi.jzcx.android.chongmi.sdk.im.view.chatrow.EaseChatRowVoicePlayClickListener;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMError;

/**
 * 按住说话录制控件
 * 
 */
public class EaseVoiceRecorderView extends RelativeLayout {
	protected Context context;
	protected LayoutInflater inflater;
	protected Drawable[] micImages;
	protected EaseVoiceRecorder voiceRecorder;

	protected PowerManager.WakeLock wakeLock;
	protected ImageView micImage;
	protected ImageView statusImage;
	protected TextView recordingHint;

	protected Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case YSMSG.VOICE_STRONGTH :
					micImage.setImageDrawable(micImages[msg.arg1]);
					break;
				default :
					break;

			}

		}
	};

	public EaseVoiceRecorderView(Context context) {
		super(context);
		init(context);
	}

	public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);

		micImage = (ImageView) findViewById(R.id.mic_image);
		statusImage = (ImageView) findViewById(R.id.record_status_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);

		voiceRecorder = new EaseVoiceRecorder(micImageHandler);

		// 动画资源文件,用于录制语音时
		micImages = new Drawable[]{getResources().getDrawable(R.drawable.amp1),
				getResources().getDrawable(R.drawable.amp2), getResources().getDrawable(R.drawable.amp3),
				getResources().getDrawable(R.drawable.amp4), getResources().getDrawable(R.drawable.amp5),
				getResources().getDrawable(R.drawable.amp6), getResources().getDrawable(R.drawable.amp7)};

		wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
	}

	/**
	 * 长按说话按钮touch事件
	 * 
	 * @param v
	 * @param event
	 */
	EaseVoiceRecorderCallback recorderCallback;
	public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
		if (recorderCallback != null) {
			this.recorderCallback = recorderCallback;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				try {
					if (EaseChatRowVoicePlayClickListener.isPlaying)
						EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
					v.setPressed(true);
					startRecording();
				} catch (Exception e) {
					v.setPressed(false);
				}
				return true;
			case MotionEvent.ACTION_MOVE :
				if (event.getY() < 0) {
					showReleaseToCancelHint();
				} else {
					if (isSetMax) {
						int length = voiceRecorder.getVoiceLength();
						showMax60Hint(60 - length);
					} else {
						showMoveUpToCancelHint();
					}

				}
				return true;
			case MotionEvent.ACTION_UP :
				v.setPressed(false);
				if (event.getY() < 0) {
					// discard the recorded audio.
					discardRecording();
				} else {
					// stop recording and send voice file
					try {
						int length = stopRecoding();
						if (length > 0) {
							if (recorderCallback != null) {
								recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
							}
						} else if (length == EMError.INVALID_FILE) {
							YSToast.showToast(getContext(), R.string.Recording_without_permission);
						} else {
							if (isRecording()) {
								this.setVisibility(View.VISIBLE);
								statusImage.setImageResource(R.drawable.voice_to_short);
								micImage.setVisibility(View.GONE);
								recordingHint.setText(context.getString(R.string.The_recording_time_is_too_short));
								recordingHint.setBackgroundResource(R.color.transparent);
								mHandler.postDelayed(dismissRun, 1000);
							}

							// recordingHint.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);
							// YSToast.showToast(getContext(),
							// R.string.The_recording_time_is_too_short);
						}
					} catch (Exception e) {
						e.printStackTrace();
						YSToast.showToast(getContext(), R.string.send_failure_please);
					}
				}
				return true;
			default :
				discardRecording();
				return false;
		}
	}
	public interface EaseVoiceRecorderCallback {
		/**
		 * 录音完毕
		 * 
		 * @param voiceFilePath
		 *            录音完毕后的文件路径
		 * @param voiceTimeLength
		 *            录音时长
		 */
		void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
	}

	boolean isSetMax = false;
	Handler mHandler = new Handler();

	Runnable countRun = new Runnable() {
		public void run() {
			int length = voiceRecorder.getVoiceLength();
			LogUtils.i("count", length + "");
			if (length == 60) {
				length = stopRecoding();
				if (recorderCallback != null) {
					recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
				}
				discardRecording();

			} else {
				if (length > 50) {
					isSetMax = true;
					showMax60Hint(60 - length);
				}
				mHandler.postDelayed(countRun, 1000);
			}
		}
	};

	Runnable dismissRun = new Runnable() {
		public void run() {
			stopRecoding();
		}
	};

	public void startRecording() {
		if (!EaseCommonUtils.isExitsSdcard()) {
			YSToast.showToast(getContext(), R.string.Send_voice_need_sdcard_support);
			return;
		}
		try {
			isSetMax = false;
			mHandler.post(countRun);
			wakeLock.acquire();
			this.setVisibility(View.VISIBLE);
			statusImage.setImageResource(R.drawable.voice_rcd_hint);
			micImage.setVisibility(View.VISIBLE);
			recordingHint.setText(context.getString(R.string.move_up_to_cancel));
			recordingHint.setBackgroundColor(Color.TRANSPARENT);
			voiceRecorder.startRecording(context);
		} catch (Exception e) {
			e.printStackTrace();
			if (wakeLock.isHeld())
				wakeLock.release();
			if (voiceRecorder != null)
				voiceRecorder.discardRecording();
			this.setVisibility(View.INVISIBLE);
			YSToast.showToast(context, R.string.recoding_fail);
			return;
		}
	}

	public void showReleaseToCancelHint() {
		statusImage.setImageResource(R.drawable.rcd_cancel_icon);
		micImage.setVisibility(View.GONE);
		recordingHint.setText(context.getString(R.string.release_to_cancel));
		recordingHint.setBackgroundResource(R.drawable.rcd_cancel_bg);
	}

	public void showMoveUpToCancelHint() {
		statusImage.setImageResource(R.drawable.voice_rcd_hint);
		micImage.setVisibility(View.VISIBLE);
		recordingHint.setText(context.getString(R.string.move_up_to_cancel));
		recordingHint.setBackgroundColor(Color.TRANSPARENT);
	}

	public void showMax60Hint(int length) {
		statusImage.setImageResource(R.drawable.voice_rcd_hint);
		micImage.setVisibility(View.VISIBLE);
		recordingHint.setText(String.format(context.getString(R.string.voice_length_text), length));
		recordingHint.setBackgroundResource(R.drawable.rcd_cancel_bg);
	}

	public void discardRecording() {
		mHandler.removeCallbacks(countRun);
		if (wakeLock.isHeld())
			wakeLock.release();
		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				this.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	public int stopRecoding() {
		mHandler.removeCallbacks(countRun);
		this.setVisibility(View.INVISIBLE);
		if (wakeLock.isHeld())
			wakeLock.release();
		return voiceRecorder.stopRecoding();
	}

	public String getVoiceFilePath() {
		return voiceRecorder.getVoiceFilePath();
	}

	public String getVoiceFileName() {
		return voiceRecorder.getVoiceFileName();
	}

	public boolean isRecording() {
		return voiceRecorder.isRecording();
	}

}
