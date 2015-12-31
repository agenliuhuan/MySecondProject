package mobi.jzcx.android.chongmi.mode;

import mobi.jzcx.android.chongmi.biz.bo.ActivityBiz;
import mobi.jzcx.android.chongmi.biz.bo.SystemBiz;
import mobi.jzcx.android.chongmi.biz.bo.UserBiz;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.core.mvc.BaseController;
import mobi.jzcx.android.core.mvc.BaseModel;
import android.os.Message;

public class CoreModel extends BaseModel {
	private static final String TAG = CoreModel.class.getSimpleName();
	private static CoreModel mCoreModel = null;
	private UserObject myself = null;

	private String voiceURL;

	@Override
	public boolean handleMessage(Message msg) {
		UserBiz.handleMessage(msg);
		SystemBiz.handleMessage(msg);
		ActivityBiz.handleMessage(msg);
		return true;
	}

	private void init() {

	}

	public void release() {
		mCoreModel = null;
	}

	/**
	 * 获取模型单例
	 */
	public static CoreModel getInstance() {
		if (null == mCoreModel) {
			mCoreModel = new CoreModel();
			BaseController.getInstance().setModel(mCoreModel);
		}

		return mCoreModel;
	}

	/**
	 * 私有构造方法
	 */
	private CoreModel() {
		init();
	}

	public UserObject getMyself() {
		return myself;
	}

	public void setMyself(UserObject myself) {
		this.myself = myself;
	}

	public String getMyMeberId() {
		if (myself != null) {
			return myself.getMemberId();
		}
		return "";
	}

	public String getVoiceURL() {
		return voiceURL;
	}

	public void setVoiceURL(String voiceURL) {
		this.voiceURL = voiceURL;
	}

}
