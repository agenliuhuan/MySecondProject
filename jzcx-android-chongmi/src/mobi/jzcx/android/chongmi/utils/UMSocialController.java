package mobi.jzcx.android.chongmi.utils;

import java.util.Map;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.ui.common.YSToast;
import mobi.jzcx.android.chongmi.ui.main.IShareSuccessListener;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class UMSocialController {
	private static final String TAG = UMSocialController.class.getSimpleName();
	public static final String ACT_GET_USER_INFO = "cn.changl.safe360.android.ui.umsocial.get.user.info";
	public static final String ACT_AUTH_FINISH = "cn.changl.safe360.android.ui.umsocial.auth.finish";
	public static final String ACT_GET_USER_INFO_FINISH = "cn.changl.safe360.android.ui.umsocial.get.user.info.finish";

	private static final String QQ_APP_ID = "1104434109";
	private static final String QQ_APP_KEY = "jHyiWS2aV2VgcZBnfjqUrXEB";
	private static final String WEIXIN_APP_ID = "wx967daebe835fbeac"; // "wx8da4d245fba00a3c";
	private static final String WEIXIN_APP_KEY = "5bb696d9ccd75a38c8a0bfe0675559b3";// "b59a9ead7de0b2a917be347bfd95c387";

	private String UMAppId = "5498cceffd98c52b190001c7";
	private UMSocialService mShareController;
	private UMSocialService mLoginController;

	IShareSuccessListener shareSuccessListener;

	public void setShareSuccessListener(IShareSuccessListener listener) {
		this.shareSuccessListener = listener;
	}

	private Activity mActivity;

	public UMSocialController(Activity activity) {
		this.mActivity = activity;
	}

	public void initShareController() {
		mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");

		mShareController.getConfig().setSsoHandler(new SinaSsoHandler());

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, QQ_APP_ID, QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mActivity, WEIXIN_APP_ID, WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();

		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, WEIXIN_APP_ID, WEIXIN_APP_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		mShareController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN_CIRCLE);
		// mShareController.openShare(mActivity, false);

	}

	public void initLoginController() {
		mLoginController = UMServiceFactory.getUMSocialService("com.umeng.login");
		// mLoginController.getConfig().setSsoHandler(new SinaSsoHandler());

		new SinaSsoHandler().addToSocialSDK();

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, QQ_APP_ID, QQ_APP_KEY);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		UMWXHandler wxHandler = new UMWXHandler(mActivity, WEIXIN_APP_ID, WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();

		mLoginController.getConfig().setSinaCallbackUrl("https://api.weibo.com/oauth2/default.html");
	}

	public UMSocialService getLoginController() {
		return mLoginController;
	}

	public UMSocialService getShareController() {
		return mShareController;
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */

	public void setShareContent() {
		// 配置SSO
		// mShareController.getConfig().setSsoHandler(new SinaSsoHandler());

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
		weixinContent.setTitle("友盟社会化分享组件-微信");
		weixinContent.setTargetUrl("http://www.umeng.com/social");
		weixinContent.setShareMedia(new UMImage(mActivity, R.drawable.ic_launcher));
		mShareController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
		circleMedia.setTitle("友盟社会化分享组件-朋友圈");
		circleMedia.setShareMedia(new UMImage(mActivity, R.drawable.ic_launcher));
		// circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl("http://www.umeng.com/social");
		mShareController.setShareMedia(circleMedia);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-新浪微博。http://www.umeng.com/social");
		sinaContent.setShareImage(new UMImage(mActivity, R.drawable.ic_launcher));
		mShareController.setShareMedia(sinaContent);
	}

	/**
	 * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
	 */
	public void directShare(SHARE_MEDIA mPlatform) {
		mShareController.directShare(mActivity, mPlatform, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				// String showText = "分享成功";
				// if (eCode != StatusCode.ST_CODE_SUCCESSED) {
				// showText = "分享失败"; // [" + eCode + "]";
				// }
				// if (eCode != 40000) {
				//
				// }
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					YSToast.showToast(mActivity, "分享成功");
					if (shareSuccessListener != null) {
						shareSuccessListener.shareSuccess();
					}
				} else {
					YSToast.showToast(mActivity, "分享失败");
				}
			}
		});
	}

	/**
	 * 一键分享到多个已授权平台。</br>
	 */
	public void shareMult() {
		SHARE_MEDIA[] platforms = new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN,
				SHARE_MEDIA.RENREN};
		mShareController.postShareMulti(mActivity, new MulStatusListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
				String showText = "分享结果：" + multiStatus.toString();
				// Toast.makeText(mActivity, showText,
				// Toast.LENGTH_SHORT).show();
			}
		}, platforms);
	}

	/**
	 * 注销本次登录</br>
	 */
	public void logout(final SHARE_MEDIA platform) {
		mLoginController.deleteOauth(mActivity, platform, new SocializeClientListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(int status, SocializeEntity entity) {
				String showText = "解除" + platform.toString() + "平台授权成功";
				if (status != StatusCode.ST_CODE_SUCCESSED) {
					showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
				}
				// YSToast.showToast(mActivity, showText);
			}
		});
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	public void login(final SHARE_MEDIA platform) {
		try {
			mLoginController.doOauthVerify(mActivity, platform, new UMAuthListener() {

				@Override
				public void onStart(SHARE_MEDIA platform) {
					// YSToast.showToast(mActivity, "开始授权");
				}

				@Override
				public void onError(SocializeException e, SHARE_MEDIA platform) {
					notifyLoginResult(UMSocialController.ACT_GET_USER_INFO_FINISH, null, "", "");
					YSToast.showToast(mActivity, "授权出错，" + e.getMessage());
				}

				@Override
				public void onComplete(Bundle value, SHARE_MEDIA platform) {
					LogUtils.d(TAG, "bundle content: " + value.toString());
					String uid = value.getString("uid");
					if (!TextUtils.isEmpty(uid)) {
						notifyLoginResult(UMSocialController.ACT_AUTH_FINISH, null, "", "");

						String strPlatform = "";
						String token = "";
						switch (platform) {
							case QQ : {
								try {
									strPlatform = "qq";
									token = value.getString("access_token");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
								break;
							case WEIXIN : {
								try {
									strPlatform = "wechat";
									token = value.getString("access_token");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
								break;
							case SINA : {
								strPlatform = "sina";
							}
								break;
							default :
								break;
						}

						getUserInfo(platform, token, uid);
					} else {
						YSToast.showToast(mActivity, "授权失败");
						notifyLoginResult(UMSocialController.ACT_GET_USER_INFO_FINISH, null, "", "");
					}
				}

				@Override
				public void onCancel(SHARE_MEDIA platform) {
					notifyLoginResult(UMSocialController.ACT_GET_USER_INFO_FINISH, null, "", "");
					YSToast.showToast(mActivity, "取消授权");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取授权平台的用户信息</br>
	 */
	public void getUserInfo(final SHARE_MEDIA platform, final String token, final String uid) {
		try {
			mLoginController.getPlatformInfo(mActivity, platform, new UMDataListener() {

				@Override
				public void onStart() {
					// YSToast.showToast(mActivity, "获取信息");
				}

				@Override
				public void onComplete(int status, Map<String, Object> info) {
					LogUtils.d(TAG, "user info: " + info.toString());
					// Iterator iter = info.entrySet().iterator();
					// while (iter.hasNext()) {
					// Map.Entry entry = (Map.Entry) iter.next();
					// Object key = entry.getKey();
					// Object val = entry.getValue();
					//
					// LogUtils.d(TAG, "key: " + key + " val: " + val + "\n");
					// }
					String strPlatform = "";
					UserObject user = new UserObject();
					switch (platform) {
						case QQ : {
							try {
								strPlatform = "qq";
								// user.setToken(token);
								user.setThirdUid(uid);
								user.setNickName((String) info.get("screen_name"));
								user.setIcoUrl((String) info.get("profile_image_url"));
								if (((String) info.get("gender")).equals("男")) {
									user.setGender("1");
								} else {
									user.setGender("0");
								}
							} catch (Exception e) {
								user = null;
								e.printStackTrace();
							}
						}
							break;
						case WEIXIN : {
							try {
								strPlatform = "wechat";
								user.setThirdUid(uid);
								user.setNickName((String) info.get("nickname"));
								user.setIcoUrl((String) info.get("headimgurl"));
								if ((Integer) info.get("sex") == 1) {
									user.setGender("1");
								} else {
									user.setGender("0");
								}
							} catch (Exception e) {
								user = null;
								e.printStackTrace();
							}
						}
							break;
						case SINA : {
							try {
								strPlatform = "sina";
								user.setThirdUid(uid);
								user.setNickName((String) info.get("screen_name"));
								user.setIcoUrl((String) info.get("profile_image_url"));
								if ((Integer) info.get("gender") == 1) {
									user.setGender("1");
								} else {
									user.setGender("0");
								}
							} catch (Exception e) {
								user = null;
								e.printStackTrace();
							}
						}
							break;
						default :
							user = null;
							break;
					}
					String error = "";
					if (user == null) {
						error = "获取用户信息失败";
					}
					notifyLoginResult(UMSocialController.ACT_GET_USER_INFO, user, error, strPlatform);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyLoginResult(String action, UserObject user, String error, String platform) {
		Intent intent = new Intent(action);
		if (user != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("UserObject", user);
			bundle.putString("Platform", platform);
			intent.putExtras(bundle);
		} else {
			intent.putExtra("Error", error);
			intent.putExtra("Platform", platform);
		}
		mActivity.sendBroadcast(intent);

	}
}
