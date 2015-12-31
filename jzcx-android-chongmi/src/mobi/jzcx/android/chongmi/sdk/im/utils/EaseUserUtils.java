package mobi.jzcx.android.chongmi.sdk.im.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.sdk.im.controller.EaseUI;
import mobi.jzcx.android.chongmi.sdk.im.controller.EaseUI.EaseUserProfileProvider;
import mobi.jzcx.android.chongmi.sdk.im.mode.EaseUser;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public class EaseUserUtils {

	static EaseUserProfileProvider userProvider;

	static {
		userProvider = EaseUI.getInstance().getUserProfileProvider();
	}

	/**
	 * 根据username获取相应user
	 * 
	 * @param username
	 * @return
	 */
	public static EaseUser getUserInfo(String username) {
		if (userProvider != null)
			return userProvider.getUser(username);

		return null;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username, ImageView imageView) {
		EaseUser user = getUserInfo(username);
		if (user != null && user.getAvatar() != null) {
			try {
				int avatarResId = Integer.parseInt(user.getAvatar());
				Glide.with(context).load(avatarResId).into(imageView);
			} catch (Exception e) {
				// 正常的string路径
				Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL)
						.placeholder(R.drawable.avatar_default_image).into(imageView);
			}
		} else {
			Glide.with(context).load(R.drawable.avatar_default_image).into(imageView);
		}
	}

	/**
	 * 设置用户昵称
	 */
	public static void setUserNick(String username, TextView textView) {
		if (textView != null) {
			EaseUser user = getUserInfo(username);
			if (user != null && user.getNick() != null) {
				textView.setText(user.getNick());
			} else {
				textView.setText(username);
			}
		}
	}

}
