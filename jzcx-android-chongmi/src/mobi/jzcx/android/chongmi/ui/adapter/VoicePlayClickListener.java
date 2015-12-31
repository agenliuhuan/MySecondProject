/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.jzcx.android.chongmi.ui.adapter;

import java.io.File;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class VoicePlayClickListener implements View.OnClickListener {

	ImageView voiceIconView;

	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;

	Context mContext;
	private BaseAdapter adapter;

	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;
	String voiceurl;

	/**
	 * 
	 * @param message
	 * @param v
	 * @param iv_read_status
	 * @param context
	 * @param activity
	 * @param user
	 * @param chatType
	 */
	public VoicePlayClickListener(String voiceurl, ImageView v, BaseAdapter adapter, Context context) {
		this.voiceurl = voiceurl;
		this.adapter = adapter;
		this.voiceIconView = v;
		this.mContext = context;
	}

	public void stopPlayVoice() {
		voiceAnimation.stop();
		voiceIconView.setBackgroundResource(R.anim.comment_voiceanim);
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
		adapter.notifyDataSetChanged();
	}

	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}
		CoreModel.getInstance().setVoiceURL(filePath);
		AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		audioManager.setMode(AudioManager.MODE_NORMAL);
		audioManager.setSpeakerphoneOn(true);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
					mediaPlayer = null;
					stopPlayVoice(); // stop animation
				}

			});
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					isPlaying = true;
					currentPlayListener = VoicePlayClickListener.this;
					showAnimation();
				}
			});

		} catch (Exception e) {
		}
	}

	// show the voice playing animation
	private void showAnimation() {
		// play voice, and start animation
		voiceIconView.setBackgroundResource(R.anim.comment_voiceanim);
		voiceAnimation = (AnimationDrawable) voiceIconView.getBackground();
		voiceAnimation.start();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (isPlaying) {
			if (CoreModel.getInstance().getVoiceURL() != null
					&& CoreModel.getInstance().getVoiceURL().equals(this.voiceurl)) {
				currentPlayListener.stopPlayVoice();
				return;
			}
			currentPlayListener.stopPlayVoice();
		}
		playVoice(this.voiceurl);
		// if (message.direct == EMMessage.Direct.SEND) {
		// // for sent msg, we will try to play the voice file directly
		//
		// } else {
		// if (message.status == EMMessage.Status.SUCCESS) {
		// File file = new File(voiceBody.getLocalUrl());
		// if (file.exists() && file.isFile())
		// playVoice(voiceBody.getLocalUrl());
		// else
		// System.err.println("file not exist");
		//
		// } else if (message.status == EMMessage.Status.INPROGRESS) {
		// Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT).show();
		// } else if (message.status == EMMessage.Status.FAIL) {
		// Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT).show();
		// new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// EMChatManager.getInstance().asyncFetchMessage(message);
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// super.onPostExecute(result);
		// adapter.notifyDataSetChanged();
		// }
		//
		// }.execute();
		//
		// }
		//
		// }
	}
}