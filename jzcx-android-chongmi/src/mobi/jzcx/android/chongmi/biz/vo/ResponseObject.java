package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

/**
 * 请求返回信息
 */
public class ResponseObject extends BaseObject {
	// "Result":{"SMSLogID":40126},"ErrorCode":1,"ErrorMsg":"短信方送失败"
	private static final long serialVersionUID = -8685066006435961617L;

	private int ErrorCode;
	private String ErrorMsg;
	private String Result;

	public int getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(int errorCode) {
		ErrorCode = errorCode;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

}
