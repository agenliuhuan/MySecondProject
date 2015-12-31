package mobi.jzcx.android.chongmi.biz.bo;

import java.io.File;
import java.io.FileNotFoundException;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.AddCommonObject;
import mobi.jzcx.android.chongmi.biz.vo.AddQuestionObject;
import mobi.jzcx.android.chongmi.biz.vo.AnswerQuestionObject;
import mobi.jzcx.android.chongmi.biz.vo.BindPetObject;
import mobi.jzcx.android.chongmi.biz.vo.CheckCommentObject;
import mobi.jzcx.android.chongmi.biz.vo.ConfirmJoinObject;
import mobi.jzcx.android.chongmi.biz.vo.EventObject;
import mobi.jzcx.android.chongmi.biz.vo.GetActivityObject;
import mobi.jzcx.android.chongmi.biz.vo.GetFansObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMeberBlogsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMonthReportObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMoreCommentsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetMoreLikeMebersObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetBlogsObject;
import mobi.jzcx.android.chongmi.biz.vo.GetPetObject;
import mobi.jzcx.android.chongmi.biz.vo.GetWeekReportObject;
import mobi.jzcx.android.chongmi.biz.vo.JoinGroupObject;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.biz.vo.MicroBlogObject;
import mobi.jzcx.android.chongmi.biz.vo.PetObject;
import mobi.jzcx.android.chongmi.biz.vo.RemoveCommentObject;
import mobi.jzcx.android.chongmi.biz.vo.ReportObject;
import mobi.jzcx.android.chongmi.biz.vo.ResponseObject;
import mobi.jzcx.android.chongmi.biz.vo.RongLianObject;
import mobi.jzcx.android.chongmi.biz.vo.UpdatePetIconObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.client.ActivityClient;
import mobi.jzcx.android.chongmi.client.ApiClient;
import mobi.jzcx.android.chongmi.db.DatabaseManager;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.core.mvc.BaseBiz;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.handler.AsyncHttpResponseHandler;
import mobi.jzcx.android.core.net.http.request.RequestParams;
import mobi.jzcx.android.core.net.ojm.OJMFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;

/**
 * 活动相关请求
 */
public class ActivityBiz extends BaseBiz {

	private static void getRecommendMebers(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GET_RECOMMENDMEBERS;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(index));
		// LngLatObject lnglat = App.getInstance().getLnglat();
		// if (lnglat != null) {
		// params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		// }
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETRECOMMENDMEBERS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETRECOMMENDMEBERS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void attention(String attention, String token) {
		String url = ApiClient.API_URL + ActivityClient.ATTENTION;
		RequestParams params = new RequestParams();
		params.put("followedMemberIds", attention);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_ATTENTION);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_ATTENTION);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void updateLocation(LngLatObject lnglat, String token) {
		String url = ApiClient.API_URL + ActivityClient.UPDATE_LOCATION;
		RequestParams params = new RequestParams();
		params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				LogUtils.i("response", response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				LogUtils.i("response", response);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getlocalActivity(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYLOCALACTIVITY;
		RequestParams params = new RequestParams();
		LngLatObject lnglat = App.getInstance().getLnglat();
		if (lnglat != null) {
			params.put("locateAddress", lnglat.getLng() + "," + lnglat.getLat());
		}
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GET_LOCALACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GET_LOCALACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);

	}

	private static void getlocalActivityNew() {
		String url = ApiClient.API_URL + ActivityClient.GET_MYLOCALACTIVITY_NEW;
		RequestParams params = new RequestParams();

		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				LogUtils.i("response", response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				LogUtils.i("response", response);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	private static void getlocalActivityMore(String token, GetActivityObject getmore) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYLOCALACTIVITY_MORE;
		RequestParams params = new RequestParams();
		if (getmore.getLnglatObj() != null) {
			params.put("LngLat", getmore.getLnglatObj().getLng() + "," + getmore.getLnglatObj().getLat());
		}
		params.put("pageIndex", String.valueOf(getmore.getPageIndex()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GET_LOCALACTIVITY_MORE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GET_LOCALACTIVITY_MORE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void createActivity(String token, EventObject eventObj) {
		String url = ApiClient.API_URL + ActivityClient.CREATE_ACTIVITY;
		RequestParams params = new RequestParams();
		params.put("title", eventObj.getTitle());
		params.put("beginTime", eventObj.getBeginTime());
		params.put("address", eventObj.getAddress());
		params.put("locateAddress", eventObj.getLnglat());
		params.put("maxUserCount", String.valueOf(eventObj.getMaxMemberCount()));
		params.put("intro", eventObj.getIntro());
		try {
			File file = new File(eventObj.getIcoUrl());
			params.put("profile_picture", file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CREATEACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CREATEACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void uploadActivityIcon(String token, final String activityid, final String filepath) {
		String url = ApiClient.API_URL + ActivityClient.CREATE_UPDATEACTIVITYICON;
		RequestParams params = new RequestParams();
		try {
			File file = new File(filepath);
			params.put("profile_picture", file);
			params.put("ActivityId", activityid);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_UPDATEACTIVITYICON);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_UPDATEACTIVITYICON);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void joinActivity(String token, String activityid, String reason) {
		String url = ApiClient.API_URL + ActivityClient.JOINACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		params.put("reason", reason);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_JOINACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_JOINACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void dissmisActivity(String token, String activityid) {
		String url = ApiClient.API_URL + ActivityClient.DISSMISMYACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_DISSMISACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_DISSMISACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void detailDissmisActivity(String token, String activityid) {
		String url = ApiClient.API_URL + ActivityClient.DISSMISMYACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_DETAIL_DISSMISACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_DETAIL_DISSMISACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMebers(String token, String activityid) {
		String url = ApiClient.API_URL + ActivityClient.GETACTIVITYMEBERS;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMEBERS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMEBERS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void logoutActivity(String token, String activityid) {
		String url = ApiClient.API_URL + ActivityClient.LOGOUTACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_EXITACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_EXITACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void detailLogoutActivity(String token, String activityid) {
		String url = ApiClient.API_URL + ActivityClient.LOGOUTACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", activityid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_DETAIL_EXITACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_DETAIL_EXITACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void confirmJoinActivity(String token, ConfirmJoinObject obj) {
		String url = ApiClient.API_URL + ActivityClient.CONFIRMJOINACTIVITY;
		RequestParams params = new RequestParams();
		params.put("activityId", obj.getActivityId());
		params.put("accept", String.valueOf(obj.isAccept()));
		params.put("requestId", obj.getRequestId());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CONFIRMJOIN);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CONFIRMJOIN);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getmyActivity(String token) {
		String url = ApiClient.API_URL + ActivityClient.GETMYACTIVITY;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void getMyFollowCount(String token) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYFOLLOWEDCOUNT;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYFOLLOWCOUNT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYFOLLOWCOUNT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void getMyFollow(String token, GetFansObject getObj) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYFOLLOWED;
		RequestParams params = new RequestParams();
		params.put("pageIndex", getObj.getPageindex());
		params.put("memberid", getObj.getMeberId());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYFOLLOW);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYFOLLOW);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyFansCount(String token) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYFANSCOUNT;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYFANSCOUNT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYFANSCOUNT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void getMyFans(String token, GetFansObject getObj) {
		String url = ApiClient.API_URL + ActivityClient.GET_MYFANS;
		RequestParams params = new RequestParams();
		params.put("pageIndex", getObj.getPageindex());
		params.put("memberid", getObj.getMeberId());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYFANS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYFANS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void followUser(String token, String userid) {
		String url = ApiClient.API_URL + ActivityClient.FOLLOW_BYID;
		RequestParams params = new RequestParams();
		params.put("followedMemberId", userid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_FOLLOW);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_FOLLOW);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void cancelFollow(String token, String userid) {
		String url = ApiClient.API_URL + ActivityClient.FOLLOW_CANCEL;
		RequestParams params = new RequestParams();
		params.put("followedMemberId", userid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CANCELFOLLOW);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CANCELFOLLOW);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyPets(String token) {
		String url = ApiClient.API_URL + ActivityClient.GETMYPETS;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYPETS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYPETS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void getPetById(String token, GetPetObject getpetObj) {
		String url = ApiClient.API_URL + ActivityClient.GETPET;
		RequestParams params = new RequestParams();
		if (!CommonTextUtils.isEmpty(getpetObj.getMeberId())) {
			params.put("memberid", getpetObj.getMeberId());
		}
		if (!CommonTextUtils.isEmpty(getpetObj.getPetId())) {
			params.put("petid", getpetObj.getPetId());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETPET);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETPET);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void updatePetIcon(String token, UpdatePetIconObject updateObj) {
		String url = ApiClient.API_URL + ActivityClient.UPDATEPETICON;
		RequestParams params = new RequestParams();
		try {
			File file = new File(updateObj.getImagepath());
			params.put("profile_picture", file);
			params.put("petid", updateObj.getPetid());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_UPDATEPETICON);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_UPDATEPETICON);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void addPet(String token, PetObject petObj) {
		String url = ApiClient.API_URL + ActivityClient.ADDPET;
		RequestParams params = new RequestParams();
		params.put("CategoryId", petObj.getCategoryId());
		params.put("Gender", petObj.getGender());
		params.put("Name", petObj.getName());
		params.put("Birthday", petObj.getBirthday());
		params.put("Description", petObj.getDescription());
		if (!TextUtils.isEmpty(petObj.getIcoUrl())) {
			try {
				File file = new File(petObj.getIcoUrl());
				params.put("profile_picture", file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}

		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_ADDPET);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_ADDPET);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void editPet(String token, PetObject petObj) {
		String url = ApiClient.API_URL + ActivityClient.EDITPET;
		RequestParams params = new RequestParams();
		params.put("petid", petObj.getPetId());
		params.put("CategoryId", petObj.getCategoryId());
		params.put("Gender", petObj.getGender());
		params.put("Name", petObj.getName());
		params.put("Birthday", petObj.getBirthday());
		params.put("Description", petObj.getDescription());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_EDITPET);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_EDITPET);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void removePet(String token, String petId) {
		String url = ApiClient.API_URL + ActivityClient.REMOVEPET;
		RequestParams params = new RequestParams();
		params.put("petid", petId);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REMOVEPET);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_REMOVEPET);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyDynamics(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GETMYDYNAMIC;
		RequestParams params = new RequestParams();
		LngLatObject lnglat = App.getInstance().getLnglat();
		if (lnglat != null) {
			params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		}

		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYDYNAMIC);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYDYNAMIC);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyDynamicsMore(String token, LngLatObject lnglat, String time) {
		String url = ApiClient.API_URL + ActivityClient.GETMYDYNAMICMORE;
		RequestParams params = new RequestParams();
		params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		params.put("minTime", time);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYDYNAMICMORE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYDYNAMICMORE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyDynamicsNotLogin(LngLatObject lnglat) {
		String url = ApiClient.API_URL + ActivityClient.GETMYDYNAMIC_NOTLOGIN;
		RequestParams params = new RequestParams();
		if (lnglat != null) {
			params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYDYNAMIC);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYDYNAMIC);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	private static void getMyDynamicsMoreNotLogin(LngLatObject lnglat, String time) {
		String url = ApiClient.API_URL + ActivityClient.GETMYDYNAMICMORE_NOTLOGIN;
		RequestParams params = new RequestParams();
		params.put("LngLat", lnglat.getLng() + "," + lnglat.getLat());
		params.put("minTime", time);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYDYNAMICMORE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYDYNAMICMORE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	private static void getMyLikes(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GETMYLIKES;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYLIKES);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYLIKES);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyLikesMore(String token, String time) {
		String url = ApiClient.API_URL + ActivityClient.GETMYLIKESMORE;
		RequestParams params = new RequestParams();
		params.put("minTime", time);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYLIKESMORE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYLIKESMORE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getAllCommentsById(String token, GetMoreCommentsObject getmoreObj) {
		String url = ApiClient.API_URL + ActivityClient.GET_COMMENTSBYID;
		RequestParams params = new RequestParams();
		params.put("microblogid", getmoreObj.getMeberid());
		params.put("pageIndex", getmoreObj.getIndex());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETALLCOMMENTS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETALLCOMMENTS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void addCommentsToBlog(String token, AddCommonObject addCommonObj) {
		String url = ApiClient.API_URL + ActivityClient.ADDCOMMENT;
		RequestParams params = new RequestParams();
		params.put("microblogid", addCommonObj.getId());
		if (addCommonObj.isVoice()) {
			try {
				File file = new File(addCommonObj.getVoicePath());
				params.put("profile_picture", file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} else {
			params.put("text", addCommonObj.getText());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_ADDCOMMENT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_ADDCOMMENT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void replyComments(String token, AddCommonObject addCommonObj) {
		String url = ApiClient.API_URL + ActivityClient.REPLYCOMMENT;
		RequestParams params = new RequestParams();
		params.put("microblogid", addCommonObj.getId());
		if (addCommonObj.isVoice()) {
			try {
				File file = new File(addCommonObj.getVoicePath());
				params.put("profile_picture", file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} else {
			params.put("text", addCommonObj.getText());
		}
		if (!CommonTextUtils.isEmpty(addCommonObj.getCommentId())) {
			params.put("replyCommentId", addCommonObj.getCommentId());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REPLYCOMMENT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_REPLYCOMMENT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyMicroblogs(String token, GetMeberBlogsObject getBlogsObj) {
		String url = ApiClient.API_URL + ActivityClient.GETMYBLOGS;
		RequestParams params = new RequestParams();
		params.put("MemberID", getBlogsObj.getMeberId());
		params.put("pageIndex", getBlogsObj.getIndex());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMYBLOGS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMYBLOGS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMyMicroblogById(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.GETBLOGBYID;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETBLOGBYID);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETBLOGBYID);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void mainlikeMicroblogById(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_LIKE;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_MAINBLOGLIKE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_MAINBLOGLIKE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void maincancellikeMicroblogById(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_CANCELLIKE;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_MAINBLOGCANCELLIKE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_MAINBLOGCANCELLIKE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void detaillikeMicroblogById(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_LIKE;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_DETAILBLOGLIKE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_DETAILBLOGLIKE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void detailcancellikeMicroblogById(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_CANCELLIKE;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_DETAILBLOGCANCELLIKE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_DETAILBLOGCANCELLIKE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void addMicroblog(String token, MicroBlogObject mObject) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_ADD;
		RequestParams params = new RequestParams();
		int size = mObject.getPhotos().size();
		for (int i = 0; i < size; i++) {
			try {
				String filepath = mObject.getPhotos().get(i);
				if (filepath.startsWith("content://")) {
					String path = CommonUtils.getPath(Uri.parse(filepath));
					File file = new File(path);
					if (file.exists()) {
						params.put("profile_picture" + i, file);// profile_picture
					}
				} else {
					File file = new File(filepath);
					if (file.exists()) {
						params.put("profile_picture" + i, file);
					}
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		params.put("PetId", mObject.getPetId());
		if (mObject.getLnglat() != null) {
			params.put("LngLat", mObject.getLnglat().getLng() + "," + mObject.getLnglat().getLat());
		}
		params.put("Text", mObject.getText());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_BLOGADD);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_BLOGADD);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getlikeCount(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_GETLIKECOUNT;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_BLOGGETLIKECOUNT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_BLOGGETLIKECOUNT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getlikeMebers(String token, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_GETLIKEMEBERS;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_BLOG_GETLIKEMEBERS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_BLOG_GETLIKEMEBERS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getlikeMeberPagelist(String token, GetMoreLikeMebersObject getmoreObj) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_GETLIKEMEBERPAGELIST;
		RequestParams params = new RequestParams();
		params.put("microblogId", getmoreObj.getId());
		params.put("pageIndex", String.valueOf(getmoreObj.getIndex()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_BLOG_GETLIKEMEBERSMORE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_BLOG_GETLIKEMEBERSMORE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getMeberInfoByid(String token, String userid) {
		String url = ApiClient.API_URL + ActivityClient.GetMeberInfo;
		RequestParams params = new RequestParams();
		params.put("MemberID", userid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETMEBERINFO);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETMEBERINFO);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getCommentNotice(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GetCommentNotice;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETCommentNOTICE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETCommentNOTICE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getSysNotice(String token, int index) {
		String url = ApiClient.API_URL + ActivityClient.GetSysNotice;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETSysNOTICE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETSysNOTICE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void setNoticeRead(String token, String idlist) {
		String url = ApiClient.API_URL + ActivityClient.SetNoticeRead;
		RequestParams params = new RequestParams();
		params.put("noticeids", idlist);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_SETNOTICEREAD);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_SETNOTICEREAD);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void getRLMeber(String token) {
		String url = ApiClient.API_URL + ActivityClient.GetRLMeber;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETRONGLIANMEBER);
				try {
					// "Info":{"MemberId":"55f6593a580e0b0f24404c0f","SubToken":"8f335582fff6ba7a74205f3dfb09e9dc","SubAccountSid":"60fd84ea5aa011e58a2aac853d9d52fd","VoipAccount":"88959500000064","VoipPwd":"MAo3Wo0F"}
					ResponseObject responseObj = OJMFactory.createOJM().fromJson(response, ResponseObject.class);
					if (responseObj.getErrorCode() == 0) {
						String result = responseObj.getResult();
						JSONObject json = new JSONObject(result);
						RongLianObject ronglianObj = OJMFactory.createOJM().fromJson(json.getString("Info"),
								RongLianObject.class);
						insertMyselfTocontact();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETRONGLIANMEBER);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void registegetRLMeber(String token) {
		String url = ApiClient.API_URL + ActivityClient.GetRLMeber;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {

				try {
					// "Info":{"MemberId":"55f6593a580e0b0f24404c0f","SubToken":"8f335582fff6ba7a74205f3dfb09e9dc","SubAccountSid":"60fd84ea5aa011e58a2aac853d9d52fd","VoipAccount":"88959500000064","VoipPwd":"MAo3Wo0F"}
					ResponseObject responseObj = OJMFactory.createOJM().fromJson(response, ResponseObject.class);
					if (responseObj.getErrorCode() == 0) {
						String result = responseObj.getResult();
						JSONObject json = new JSONObject(result);
						RongLianObject ronglianObj = OJMFactory.createOJM().fromJson(json.getString("Info"),
								RongLianObject.class);
						UserObject myselef = new UserObject();
						myselef.setMemberId(ronglianObj.getMemberId());
						CoreModel.getInstance().setMyself(myselef);

						insertMyselfTocontact();
						// DatabaseManager.getInstance().initHelper(ronglianObj.getMemberId());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, String response) {

			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void insertMyselfTocontact() {

	}

	private static void getPetBlogs(String token, GetPetBlogsObject getObj) {
		String url = ApiClient.API_URL + ActivityClient.GETPETBLOGS;
		RequestParams params = new RequestParams();
		params.put("petId", getObj.getPetId());
		params.put("pageIndex", String.valueOf(getObj.getPageIndex()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETPETBLOGS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETPETBLOGS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void searchActivity(String token, String search, int index) {
		String url = ApiClient.API_URL + ActivityClient.SEARCHACTIVITY;
		RequestParams params = new RequestParams();
		LngLatObject lnglat = App.getInstance().getLnglat();
		if (lnglat != null) {
			params.put("locateAddress", lnglat.getLng() + "," + lnglat.getLat());
		}
		params.put("searchKey", search);
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_SEARCHACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_SEARCHACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void feedback(String socialToken, String text) {
		String url = ApiClient.API_URL + ActivityClient.AddFeedBack;
		RequestParams params = new RequestParams();
		params.put("context", text);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_FEEDBACK);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_FEEDBACK);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void report(String socialToken, ReportObject reportObj) {
		String url = ApiClient.API_URL + ActivityClient.Report;
		RequestParams params = new RequestParams();
		if (!CommonTextUtils.isEmpty(reportObj.getActivityId())) {
			params.put("activityId", reportObj.getActivityId());
		}
		if (!CommonTextUtils.isEmpty(reportObj.getCommentMemberId())) {
			params.put("commentId", reportObj.getCommentMemberId());
		}
		if (!CommonTextUtils.isEmpty(reportObj.getMicroblogId())) {
			params.put("microblogId", reportObj.getMicroblogId());
		}
		if (!CommonTextUtils.isEmpty(reportObj.getReason())) {
			params.put("reason", reportObj.getReason());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REPORT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_REPORT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void getaddactivityMsg(String socialToken, int index) {
		String url = ApiClient.API_URL + ActivityClient.GetAddActivityNotice;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETADDACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETADDACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void getactivityByIMid(String socialToken, String imid) {
		String url = ApiClient.API_URL + ActivityClient.GETACTIVITYBYIMID;
		RequestParams params = new RequestParams();
		params.put("imGroupId", imid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETACTIVITYBYIMID);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETACTIVITYBYIMID);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void removeMicroblog(String socialToken, String id) {
		String url = ApiClient.API_URL + ActivityClient.BLOG_REMOVE;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REMOVEMICROBLOG);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_REMOVEMICROBLOG);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void removeComment(String socialToken, RemoveCommentObject obj) {
		String url = ApiClient.API_URL + ActivityClient.REMOVECOMMENT;
		RequestParams params = new RequestParams();
		if (!CommonTextUtils.isEmpty(obj.getMicroblogid())) {
			params.put("microblogid", obj.getMicroblogid());
		}
		if (!CommonTextUtils.isEmpty(obj.getCommentid())) {
			params.put("commentid", obj.getCommentid());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REMOVECOMMENT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_REMOVECOMMENT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void checkMicroblog(String socialToken, String id) {
		String url = ApiClient.API_URL + ActivityClient.CheckMicroblogExists;
		RequestParams params = new RequestParams();
		params.put("microblogid", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CHECKMICROBLOGEXISTS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CHECKMICROBLOGEXISTS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void checkComment(String socialToken, CheckCommentObject obj) {
		String url = ApiClient.API_URL + ActivityClient.CheckCommentExists;
		RequestParams params = new RequestParams();
		if (!CommonTextUtils.isEmpty(obj.getMicroblogid())) {
			params.put("microblogid", obj.getMicroblogid());
		}
		if (!CommonTextUtils.isEmpty(obj.getCommentid())) {
			params.put("commentid", obj.getCommentid());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CHECKCOMMENTEXISTS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CHECKCOMMENTEXISTS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void searchMac(String socialToken, String mac) {
		String url = ApiClient.API_URL + ActivityClient.searchMac;
		RequestParams params = new RequestParams();
		params.put("macList", mac);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_SEARCHMAC);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_SEARCHMAC);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void bindPetCollar(String socialToken, BindPetObject bindpet) {
		String url = ApiClient.API_URL + ActivityClient.bindPetCollar;
		RequestParams params = new RequestParams();
		params.put("petId", String.valueOf(bindpet.getPetId()));
		params.put("petCollarId", String.valueOf(bindpet.getPetCollarId()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_BINDPETCOLLAR);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_BINDPETCOLLAR);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void unbindPetCollar(String socialToken, BindPetObject bindpet) {
		String url = ApiClient.API_URL + ActivityClient.unbindPetCollar;
		RequestParams params = new RequestParams();
		params.put("petId", String.valueOf(bindpet.getPetId()));
		params.put("petCollarId", String.valueOf(bindpet.getPetCollarId()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_UNBINDPETCOLLAR);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_UNBINDPETCOLLAR);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void petBindList(String socialToken) {
		String url = ApiClient.API_URL + ActivityClient.petBindList;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_PETBINDLIST);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_PETBINDLIST);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), null, responseHandler);
	}

	private static void petBindListMain(String socialToken) {
		String url = ApiClient.API_URL + ActivityClient.petBindList;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_PETBINDLIST_MAIN);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_PETBINDLIST_MAIN);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), null, responseHandler);
	}
	private static void writeActivity(String socialToken, String activity, int petid) {
		String url = ApiClient.API_URL + ActivityClient.writeActivity;
		RequestParams params = new RequestParams();
		params.put("petId", String.valueOf(petid));
		params.put("activitys", activity);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_WRITEACTIVITY);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_WRITEACTIVITY);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}
	private static void weekReport(String socialToken, GetWeekReportObject getreport) {
		String url = ApiClient.API_URL + ActivityClient.weekReport;
		RequestParams params = new RequestParams();
		params.put("petId", getreport.getPetid());
		params.put("beginTime", String.valueOf(getreport.getBeginTime()));
		params.put("endTime", String.valueOf(getreport.getEndTime()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_WEEKREPORT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_WEEKREPORT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void oneweekReport(String socialToken, GetWeekReportObject getreport) {
		String url = ApiClient.API_URL + ActivityClient.weekReport;
		RequestParams params = new RequestParams();
		params.put("petId", getreport.getPetid());
		params.put("beginTime", String.valueOf(getreport.getBeginTime()));
		params.put("endTime", String.valueOf(getreport.getEndTime()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_ONEWEEKREPORT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_ONEWEEKREPORT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void monthReport(String socialToken, GetMonthReportObject getreport) {
		String url = ApiClient.API_URL + ActivityClient.monthReport;
		RequestParams params = new RequestParams();
		params.put("petId", String.valueOf(getreport.getPetid()));
		params.put("year", String.valueOf(getreport.getYear()));
		params.put("month", String.valueOf(getreport.getMonth()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_MONTHREPORT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_MONTHREPORT);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}
	private static void noticeRemove(String socialToken, String noticeid) {
		String url = ApiClient.API_URL + ActivityClient.NoticeRemove;
		RequestParams params = new RequestParams();
		params.put("noticeMsgId", noticeid);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_NOTICEREMOVE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_NOTICEREMOVE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void noticeRemoveAll(String socialToken, String noticetype) {
		String url = ApiClient.API_URL + ActivityClient.NoticeAllRemove;
		RequestParams params = new RequestParams();
		params.put("types", noticetype);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_NOTICEREMOVEALL);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_NOTICEREMOVEALL);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void questionAdd(String socialToken, AddQuestionObject obj) {
		String url = ApiClient.API_URL + ActivityClient.QuestionAdd;
		RequestParams params = new RequestParams();
		if (obj.getPhotos() != null && obj.getPhotos().size() > 0) {
			int size = obj.getPhotos().size();
			for (int i = 0; i < size; i++) {
				try {
					String filepath = obj.getPhotos().get(i);
					if (filepath.startsWith("content://")) {
						String path = CommonUtils.getPath(Uri.parse(filepath));
						File file = new File(path);
						if (file.exists()) {
							params.put("profile_picture" + i, file);// profile_picture
						}
					} else {
						File file = new File(filepath);
						if (file.exists()) {
							params.put("profile_picture" + i, file);
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (!CommonTextUtils.isEmpty(obj.getTitle())) {
			params.put("title", obj.getTitle());
		}
		if (!CommonTextUtils.isEmpty(obj.getContext())) {
			params.put("context", obj.getContext());
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_QuestionAdd);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_QuestionAdd);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void questionList(String socialToken, int pageindex) {
		String url = ApiClient.API_URL + ActivityClient.QuestionQuestionList;
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(pageindex));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_QuestionQuestionList);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_QuestionQuestionList);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void searchQuestionList(String socialToken, int index, String text) {
		String url = ApiClient.API_URL + ActivityClient.QuestionQuestionList;
		RequestParams params = new RequestParams();
		params.put("keywords", text);
		params.put("pageIndex", String.valueOf(index));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_SearchQuestionList);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_SearchQuestionList);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void answerQuestion(String socialToken, AnswerQuestionObject obj) {
		String url = ApiClient.API_URL + ActivityClient.QuestionAnswer;
		RequestParams params = new RequestParams();
		params.put("questionId", obj.getQuestionId());
		params.put("answerText", obj.getAnswerText());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_QuestionAnswer);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_QuestionAnswer);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void answerList(String socialToken, int questionId, int pageIndex) {
		String url = ApiClient.API_URL + ActivityClient.QuestionAnswerList;
		RequestParams params = new RequestParams();
		params.put("questionId", String.valueOf(questionId));
		params.put("pageIndex", String.valueOf(pageIndex));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_QuestionAnswerList);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_QuestionAnswerList);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	public static void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.REQ_GETRECOMMENDMEBERS : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getRecommendMebers(socialToken, msg.arg1);
				}

			}
				break;
			case YSMSG.REQ_ATTENTION : {
				String atten = String.valueOf(msg.obj);
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(atten)) {
					attention(atten, socialToken);
				}
			}
				break;
			case YSMSG.REQ_UPDATE_LOCATION :
				if (msg.obj != null) {
					LngLatObject lnglat = (LngLatObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						updateLocation(lnglat, socialToken);
					}
				}
				break;
			case YSMSG.REQ_GET_LOCALACTIVITY : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getlocalActivity(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_GET_LOCALACTIVITY_MORE :
				if (msg.obj != null) {
					GetActivityObject getmore = (GetActivityObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getlocalActivityMore(socialToken, getmore);
					}

				}
				break;
			case YSMSG.REQ_GET_LOCALACTIVITY_NEW :
				getlocalActivityNew();
				break;
			case YSMSG.REQ_CREATEACTIVITY :
				if (msg.obj != null) {
					EventObject eventObj = (EventObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						createActivity(socialToken, eventObj);
					}

				}
				break;
			case YSMSG.REQ_UPDATEACTIVITYICON :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					UserObject myself = CoreModel.getInstance().getMyself();
					if (myself != null) {
						// uploadActivityIcon(socialToken, activityId,
						// FileUtils.ACTIVITY_ICON);
					}

				}
				break;
			case YSMSG.REQ_JOINACTIVITY :
				if (msg.obj != null) {
					JoinGroupObject jobj = (JoinGroupObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						joinActivity(socialToken, jobj.getActId(), jobj.getReason());
					}
				}
				break;
			case YSMSG.REQ_GETMYACTIVITY : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getmyActivity(socialToken);
				}
			}
				break;
			case YSMSG.REQ_DISSMISACTIVITY :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						dissmisActivity(socialToken, activityId);
					}
				}
				break;
			case YSMSG.REQ_EXITACTIVITY :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						logoutActivity(socialToken, activityId);
					}
				}
				break;
			case YSMSG.REQ_DETAIL_DISSMISACTIVITY :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						detailDissmisActivity(socialToken, activityId);
					}
				}
				break;
			case YSMSG.REQ_DETAIL_EXITACTIVITY :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						detailLogoutActivity(socialToken, activityId);
					}
				}
				break;
			case YSMSG.REQ_GETMEBERS :
				if (msg.obj != null) {
					String activityId = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(activityId)) {
						getMebers(socialToken, activityId);
					}
				}
				break;
			case YSMSG.REQ_CONFIRMJOIN :
				if (msg.obj != null) {
					ConfirmJoinObject obj = (ConfirmJoinObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						confirmJoinActivity(socialToken, obj);
					}
				}
				break;

			case YSMSG.REQ_GETMYFOLLOWCOUNT : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getMyFollowCount(socialToken);
				}
			}
				break;
			case YSMSG.REQ_GETMYFOLLOW : {
				if (msg.obj != null) {
					GetFansObject getObj = (GetFansObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyFollow(socialToken, getObj);
					}
				}
			}
				break;
			case YSMSG.REQ_GETMYFANSCOUNT : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getMyFansCount(socialToken);
				}
			}
				break;
			case YSMSG.REQ_GETMYFANS : {
				if (msg.obj != null) {
					GetFansObject getObj = (GetFansObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyFans(socialToken, getObj);
					}
				}
			}
				break;
			case YSMSG.REQ_FOLLOW : {
				if (msg.obj != null) {
					String userid = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						followUser(socialToken, userid);
					}
				}
			}
				break;
			case YSMSG.REQ_CANCELFOLLOW : {
				if (msg.obj != null) {
					String userid = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						cancelFollow(socialToken, userid);
					}
				}
			}
				break;
			case YSMSG.REQ_GETALLCOMMENTS : {
				if (msg.obj != null) {
					GetMoreCommentsObject getmoreObj = (GetMoreCommentsObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getAllCommentsById(socialToken, getmoreObj);
					}
				}
			}
				break;
			case YSMSG.REQ_ADDCOMMENT : {
				if (msg.obj != null) {
					AddCommonObject obj = (AddCommonObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						addCommentsToBlog(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_REPLYCOMMENT : {
				if (msg.obj != null) {
					AddCommonObject obj = (AddCommonObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						replyComments(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_GETMYBLOGS : {
				if (msg.obj != null) {
					GetMeberBlogsObject getBlogsObj = (GetMeberBlogsObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyMicroblogs(socialToken, getBlogsObj);
					}
				}
			}
				break;
			case YSMSG.REQ_GETBLOGBYID : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyMicroblogById(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_MAINBLOGLIKE : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						mainlikeMicroblogById(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_MAINBLOGCANCELLIKE : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						maincancellikeMicroblogById(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_DETAILBLOGLIKE : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						detaillikeMicroblogById(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_DETAILBLOGCANCELLIKE : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						detailcancellikeMicroblogById(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_BLOGADD : {
				if (msg.obj != null) {
					MicroBlogObject mObject = (MicroBlogObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						addMicroblog(socialToken, mObject);
					}
				}
			}
				break;
			case YSMSG.REQ_BLOGGETLIKECOUNT : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getlikeCount(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_BLOG_GETLIKEMEBERS : {
				if (msg.obj != null) {
					String id = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getlikeMebers(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_BLOG_GETLIKEMEBERSMORE : {
				if (msg.obj != null) {
					GetMoreLikeMebersObject getmoreObj = (GetMoreLikeMebersObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getlikeMeberPagelist(socialToken, getmoreObj);
					}
				}
			}
				break;
			case YSMSG.REQ_GETMYDYNAMIC : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getMyDynamics(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_GETMYDYNAMICMORE : {
				if (msg.obj != null) {
					String moreTime = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyDynamicsMore(socialToken, App.getInstance().getLnglat(), moreTime);
					}
				}
			}
				break;
			case YSMSG.REQ_GETMYLIKES : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getMyLikes(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_GETMYLIKESMORE : {
				if (msg.obj != null) {
					String moreTime = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMyLikesMore(socialToken, moreTime);
					}
				}
			}
				break;

			case YSMSG.REQ_GETMYPETS : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getMyPets(socialToken);
				}
			}
				break;
			case YSMSG.REQ_GETPET : {
				if (msg.obj != null) {
					GetPetObject getpetObj = (GetPetObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getPetById(socialToken, getpetObj);
					}
				}
			}
				break;
			case YSMSG.REQ_UPDATEPETICON : {
				if (msg.obj != null) {
					UpdatePetIconObject updateObj = (UpdatePetIconObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						updatePetIcon(socialToken, updateObj);
					}
				}
			}
				break;
			case YSMSG.REQ_ADDPET : {
				if (msg.obj != null) {
					PetObject petObj = (PetObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						addPet(socialToken, petObj);
					}
				}
			}
				break;
			case YSMSG.REQ_EDITPET : {
				if (msg.obj != null) {
					PetObject petObj = (PetObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						editPet(socialToken, petObj);
					}
				}
			}
				break;
			case YSMSG.REQ_REMOVEPET : {
				if (msg.obj != null) {
					String petid = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						removePet(socialToken, petid);
					}
				}
			}
				break;
			case YSMSG.REQ_GETMEBERINFO : {
				if (msg.obj != null) {
					String userid = (String) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getMeberInfoByid(socialToken, userid);
					}
				}
			}
				break;
			case YSMSG.REQ_GETCommentNOTICE : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getCommentNotice(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_GETSysNOTICE : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getSysNotice(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_SETNOTICEREAD : {
				if (msg.obj != null) {
					String noticeid = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						setNoticeRead(socialToken, noticeid);
					}
				}
			}
				break;
			case YSMSG.REQ_GETRONGLIANMEBER : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getRLMeber(socialToken);
				}
			}
				break;
			case YSMSG.REQ_REGISTE_GETRONGLIANMEBER : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					registegetRLMeber(socialToken);
				}
			}
				break;
			case YSMSG.REQ_GETPETBLOGS : {
				if (msg.obj != null) {
					GetPetBlogsObject getObj = (GetPetBlogsObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getPetBlogs(socialToken, getObj);
					}
				}
			}
				break;
			case YSMSG.REQ_SEARCHACTIVITY : {
				if (msg.obj != null) {
					String search = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(search)) {
						searchActivity(socialToken, search, msg.arg1);
					}
				}
			}
				break;
			case YSMSG.REQ_REPORT : {
				if (msg.obj != null) {
					ReportObject report = (ReportObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && report != null) {
						report(socialToken, report);
					}
				}
			}
				break;
			case YSMSG.REQ_FEEDBACK : {
				if (msg.obj != null) {
					String text = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(text)) {
						feedback(socialToken, text);
					}
				}
			}
				break;
			case YSMSG.REQ_GETADDACTIVITY : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					getaddactivityMsg(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_GETACTIVITYBYIMID : {
				if (msg.obj != null) {
					String imId = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(imId)) {
						getactivityByIMid(socialToken, imId);
					}
				}

			}
				break;
			case YSMSG.REQ_REMOVEMICROBLOG : {
				if (msg.obj != null) {
					String id = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(id)) {
						removeMicroblog(socialToken, id);
					}
				}

			}
				break;
			case YSMSG.REQ_REMOVECOMMENT : {
				if (msg.obj != null) {
					RemoveCommentObject obj = (RemoveCommentObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && obj != null) {
						removeComment(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_CHECKMICROBLOGEXISTS : {
				if (msg.obj != null) {
					String id = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(id)) {
						checkMicroblog(socialToken, id);
					}
				}
			}
				break;
			case YSMSG.REQ_CHECKCOMMENTEXISTS : {
				if (msg.obj != null) {
					CheckCommentObject obj = (CheckCommentObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && obj != null) {
						checkComment(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_SEARCHMAC : {
				if (msg.obj != null) {
					String macList = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						searchMac(socialToken, macList);
					}
				}
			}
				break;
			case YSMSG.REQ_PETBINDLIST : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					petBindList(socialToken);
				}
			}
				break;
			case YSMSG.REQ_PETBINDLIST_MAIN : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					petBindListMain(socialToken);
				}
			}
				break;
			case YSMSG.REQ_BINDPETCOLLAR : {
				if (msg.obj != null) {
					BindPetObject bindpetObj = (BindPetObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						bindPetCollar(socialToken, bindpetObj);
					}
				}
			}
				break;
			case YSMSG.REQ_UNBINDPETCOLLAR : {
				if (msg.obj != null) {
					BindPetObject bindpetObj = (BindPetObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						unbindPetCollar(socialToken, bindpetObj);
					}
				}
			}
				break;
			case YSMSG.REQ_WRITEACTIVITY : {
				if (msg.obj != null) {
					String activitys = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						writeActivity(socialToken, activitys, msg.arg1);
					}
				}
			}
				break;
			case YSMSG.REQ_WEEKREPORT : {
				if (msg.obj != null) {
					GetWeekReportObject getReport = (GetWeekReportObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						weekReport(socialToken, getReport);
					}
				}
			}
				break;
			case YSMSG.REQ_ONEWEEKREPORT : {
				if (msg.obj != null) {
					GetWeekReportObject getReport = (GetWeekReportObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						oneweekReport(socialToken, getReport);
					}
				}
			}
				break;
			case YSMSG.REQ_MONTHREPORT : {
				if (msg.obj != null) {
					GetMonthReportObject getReport = (GetMonthReportObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						monthReport(socialToken, getReport);
					}
				}
			}
				break;
			case YSMSG.REQ_NOTICEREMOVE : {
				if (msg.obj != null) {
					String msgid = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(msgid)) {
						noticeRemove(socialToken, msgid);
					}
				}
			}
				break;
			case YSMSG.REQ_NOTICEREMOVEALL : {
				if (msg.obj != null) {
					String msgid = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(msgid)) {
						noticeRemoveAll(socialToken, msgid);
					}
				}
			}
				break;
			case YSMSG.REQ_QuestionAdd : {
				if (msg.obj != null) {
					AddQuestionObject obj = (AddQuestionObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && obj != null) {
						questionAdd(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_QuestionAnswer : {
				if (msg.obj != null) {
					AnswerQuestionObject obj = (AnswerQuestionObject) msg.obj;
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && obj != null) {
						answerQuestion(socialToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_QuestionAnswerList : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					answerList(socialToken, msg.arg1, msg.arg2);
				}
			}
				break;
			case YSMSG.REQ_QuestionQuestionList : {
				String socialToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(socialToken)) {
					questionList(socialToken, msg.arg1);
				}
			}
				break;
			case YSMSG.REQ_SearchQuestionList : {
				if (msg.obj != null) {
					String text = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken) && !CommonTextUtils.isEmpty(text)) {
						searchQuestionList(socialToken, msg.arg1, text);
					}
				}

			}
				break;
			default :
				break;
		}
	}

}
