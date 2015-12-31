package mobi.jzcx.android.chongmi.sdk.im.view.chatrow;

import java.io.File;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.im.mode.EaseImageCache;
import mobi.jzcx.android.chongmi.sdk.im.ui.EaseShowBigImageActivity;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseCommonUtils;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseImageUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;

public class EaseChatRowImage extends EaseChatRowFile {

	protected ImageView imageView;
	private ImageMessageBody imgBody;

	public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
	}

	@Override
	protected void onInflatView() {
		inflater.inflate(message.direct == EMMessage.Direct.RECEIVE
				? R.layout.ease_row_received_picture
				: R.layout.ease_row_sent_picture, this);
	}

	@Override
	protected void onFindViewById() {
		percentageView = (TextView) findViewById(R.id.percentage);
		imageView = (ImageView) findViewById(R.id.image);
	}

	@Override
	protected void onSetUpView() {
		imgBody = (ImageMessageBody) message.getBody();
		// 接收方向的消息
		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (message.status == EMMessage.Status.INPROGRESS) {
				imageView.setImageResource(R.drawable.image_default_image);
				setMessageReceiveCallback();
			} else {
				progressBar.setVisibility(View.GONE);
				percentageView.setVisibility(View.GONE);
				imageView.setImageResource(R.drawable.image_default_image);
				if (imgBody.getLocalUrl() != null) {
					// String filePath = imgBody.getLocalUrl();
					String remotePath = imgBody.getRemoteUrl();
					String filePath = EaseImageUtils.getImagePath(remotePath);
					String thumbRemoteUrl = imgBody.getThumbnailUrl();
					String thumbnailPath = EaseImageUtils.getThumbnailImagePath(thumbRemoteUrl);
					showImageView(thumbnailPath, imageView, filePath, message);
				}
			}
			return;
		}

		String filePath = imgBody.getLocalUrl();
		if (filePath != null) {
			showImageView(EaseImageUtils.getThumbnailImagePath(filePath), imageView, filePath, message);
		}
		handleSendMessage();
	}

	@Override
	protected void onUpdateView() {
		super.onUpdateView();
	}

	@Override
	protected void onBubbleClick() {
		Intent intent = new Intent(context, EaseShowBigImageActivity.class);
		File file = new File(imgBody.getLocalUrl());
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);
			intent.putExtra("uri", uri);
		} else {
			// The local full size pic does not exist yet.
			// ShowBigImage needs to download it from the server
			// first
			intent.putExtra("secret", imgBody.getSecret());
			intent.putExtra("remotepath", imgBody.getRemoteUrl());
		}
		if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
				&& message.getChatType() != ChatType.GroupChat) {
			try {
				EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
				message.isAcked = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		context.startActivity(intent);
	}

	/**
	 * load image into image view
	 * 
	 * @param thumbernailPath
	 * @param iv
	 * @param position
	 * @return the image exists or not
	 */
	private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath,
			final EMMessage message) {
		// first check if the thumbnail image already loaded into cache
		Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
		if (bitmap != null) {
			// thumbnail image is already loaded, reuse the drawable
			iv.setImageBitmap(bitmap);
			setWidthAndHeight(iv, bitmap);
			return true;
		} else {
			new AsyncTask<Object, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Object... args) {
					File file = new File(thumbernailPath);
					if (file.exists()) {
						return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
					} else {
						if (message.direct == EMMessage.Direct.SEND) {
							return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
						} else {
							return null;
						}
					}
				}

				protected void onPostExecute(Bitmap image) {
					if (image != null) {
						iv.setImageBitmap(image);
						setWidthAndHeight(iv, image);
						EaseImageCache.getInstance().put(thumbernailPath, image);
					} else {
						if (message.status == EMMessage.Status.FAIL) {
							if (EaseCommonUtils.isNetWorkConnected(activity)) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										EMChatManager.getInstance().asyncFetchMessage(message);
									}
								}).start();
							}
						}

					}
				}
			}.execute();

			return true;
		}
	}

	private void setWidthAndHeight(ImageView iv, Bitmap bitmap) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
		int screenWidth = CommonUtils.getScreenWidth(context);
		if (bitmap.getWidth() > bitmap.getHeight()) {
			params.width = screenWidth * 25 / 100;
			params.height = screenWidth * 15 / 100;
		} else {
			params.width = screenWidth * 15 / 100;
			params.height = screenWidth * 25 / 100;
		}
		iv.setLayoutParams(params);
	}
}
