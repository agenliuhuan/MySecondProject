package mobi.jzcx.android.chongmi.biz;

/**
 * 消息基类
 */
public class YSMSG {

	/**
	 * 语音音量
	 */
	public static final int VOICE_STRONGTH = 99;

	private static final int MSG_BASE = 100;

	/**
	 * 用户注册
	 */
	public static final int REQ_REGISTER = MSG_BASE + 1;
	public static final int RESP_REGISTER = MSG_BASE + 2;

	/**
	 * 用户注册短信验证码
	 */
	public static final int REQ_REGISTER_SMS = MSG_BASE + 3;
	public static final int RESP_REGISTER_SMS = MSG_BASE + 4;

	/**
	 * 用户登录
	 */
	public static final int REQ_LOGIN = MSG_BASE + 5;
	public static final int RESP_LOGIN = MSG_BASE + 6;

	/**
	 * 忘记密码短信验证码
	 */
	public static final int REQ_FOGOTPSD_VCODE = MSG_BASE + 7;
	public static final int RESP_FOGOTPSD_VCODE = MSG_BASE + 8;

	/**
	 * 忘记密码
	 */
	public static final int REQ_FOGOTPSD = MSG_BASE + 9;
	public static final int RESP_FOGOTPSD = MSG_BASE + 10;

	/**
	 * 个人资料读取
	 */
	public static final int REQ_GETUSERINFO = MSG_BASE + 11;
	public static final int RESP_GETUSERINFO = MSG_BASE + 12;

	/**
	 * 修改个人资料
	 */
	public static final int REQ_UPDATE_INFO = MSG_BASE + 15;
	public static final int RESP_UPDATE_INFO = MSG_BASE + 16;

	/**
	 * 退出登录
	 */
	public static final int REQ_LOGOUT = MSG_BASE + 17;
	public static final int RESP_LOGOUT = MSG_BASE + 18;

	/**
	 * 密码重设
	 */
	public static final int REQ_RESETPSD = MSG_BASE + 19;
	public static final int RESP_RESETPSD = MSG_BASE + 20;

	/**
	 * 上传当前位置
	 */
	public static final int REQ_UPDATE_LOCATION = MSG_BASE + 21;
	public static final int RESP_UPDATE_LOCATION = MSG_BASE + 22;

	/**
	 * 首次获取附近活动
	 */
	public static final int REQ_GET_LOCALACTIVITY = MSG_BASE + 23;
	public static final int RESP_GET_LOCALACTIVITY = MSG_BASE + 24;

	/**
	 * 获取附近活动更多
	 */
	public static final int REQ_GET_LOCALACTIVITY_MORE = MSG_BASE + 25;
	public static final int RESP_GET_LOCALACTIVITY_MORE = MSG_BASE + 26;

	/**
	 * 获取附近活动最新
	 */
	public static final int REQ_GET_LOCALACTIVITY_NEW = MSG_BASE + 27;
	public static final int RESP_GET_LOCALACTIVITY_NEW = MSG_BASE + 28;

	/**
	 * 创建活动
	 */
	public static final int REQ_CREATEACTIVITY = MSG_BASE + 29;
	public static final int RESP_CREATEACTIVITY = MSG_BASE + 30;

	/**
	 * 上传活动图片
	 */
	public static final int REQ_UPDATEACTIVITYICON = MSG_BASE + 31;
	public static final int RESP_UPDATEACTIVITYICON = MSG_BASE + 32;

	/**
	 * 加入活动
	 */
	public static final int REQ_JOINACTIVITY = MSG_BASE + 33;
	public static final int RESP_JOINACTIVITY = MSG_BASE + 34;

	/**
	 * 解散活动
	 */
	public static final int REQ_DISSMISACTIVITY = MSG_BASE + 35;
	public static final int RESP_DISSMISACTIVITY = MSG_BASE + 36;

	/**
	 * 退出活动
	 */
	public static final int REQ_EXITACTIVITY = MSG_BASE + 37;
	public static final int RESP_EXITACTIVITY = MSG_BASE + 38;

	/**
	 * 获取群成员
	 */
	public static final int REQ_GETMEBERS = MSG_BASE + 39;
	public static final int RESP_GETMEBERS = MSG_BASE + 40;

	/**
	 * 确认他人加入活动
	 */
	public static final int REQ_CONFIRMJOIN = MSG_BASE + 41;
	public static final int RESP_CONFIRMJOIN = MSG_BASE + 42;

	/**
	 * 获取我的活动
	 */
	public static final int REQ_GETMYACTIVITY = MSG_BASE + 43;
	public static final int RESP_GETMYACTIVITY = MSG_BASE + 44;

	/**
	 * 读取我关注的人数量
	 */
	public static final int REQ_GETMYFOLLOWCOUNT = MSG_BASE + 45;
	public static final int RESP_GETMYFOLLOWCOUNT = MSG_BASE + 46;

	/**
	 * 读取我关注的人
	 */
	public static final int REQ_GETMYFOLLOW = MSG_BASE + 47;
	public static final int RESP_GETMYFOLLOW = MSG_BASE + 48;

	/**
	 * 我的粉丝数量
	 */
	public static final int REQ_GETMYFANSCOUNT = MSG_BASE + 49;
	public static final int RESP_GETMYFANSCOUNT = MSG_BASE + 50;

	/**
	 * 读取我的粉丝
	 */
	public static final int REQ_GETMYFANS = MSG_BASE + 51;
	public static final int RESP_GETMYFANS = MSG_BASE + 52;

	/**
	 * 关注
	 */
	public static final int REQ_FOLLOW = MSG_BASE + 53;
	public static final int RESP_FOLLOW = MSG_BASE + 54;

	/**
	 * 取消关注
	 */
	public static final int REQ_CANCELFOLLOW = MSG_BASE + 55;
	public static final int RESP_CANCELFOLLOW = MSG_BASE + 56;

	/**
	 * 获取所有评论
	 */
	public static final int REQ_GETALLCOMMENTS = MSG_BASE + 57;
	public static final int RESP_GETALLCOMMENTS = MSG_BASE + 58;

	/**
	 * 添加评论
	 */
	public static final int REQ_ADDCOMMENT = MSG_BASE + 59;
	public static final int RESP_ADDCOMMENT = MSG_BASE + 60;

	/**
	 * 读取我的所有的博客
	 */
	public static final int REQ_GETMYBLOGS = MSG_BASE + 61;
	public static final int RESP_GETMYBLOGS = MSG_BASE + 62;

	/**
	 * 读取微博
	 */
	public static final int REQ_GETBLOGBYID = MSG_BASE + 63;
	public static final int RESP_GETBLOGBYID = MSG_BASE + 64;

	/**
	 * 发布一条新的微博
	 */
	public static final int REQ_BLOGADD = MSG_BASE + 67;
	public static final int RESP_BLOGADD = MSG_BASE + 68;

	/**
	 * 读取指定Id微博赞的数量
	 */
	public static final int REQ_BLOGGETLIKECOUNT = MSG_BASE + 69;
	public static final int RESP_BLOGGETLIKECOUNT = MSG_BASE + 70;

	/**
	 * 宠物分类
	 */
	public static final int REQ_GETPETCATEGORIES = MSG_BASE + 75;
	public static final int RESP_GETPETCATEGORIES = MSG_BASE + 76;

	/**
	 * 我的宠物列表
	 */
	public static final int REQ_GETMYPETS = MSG_BASE + 77;
	public static final int RESP_GETMYPETS = MSG_BASE + 78;

	/**
	 * 获取指定宠物信息
	 */
	public static final int REQ_GETPET = MSG_BASE + 79;
	public static final int RESP_GETPET = MSG_BASE + 80;

	/**
	 * 上传宠物图片
	 */
	public static final int REQ_UPDATEPETICON = MSG_BASE + 81;
	public static final int RESP_UPDATEPETICON = MSG_BASE + 82;

	/**
	 * 添加宠物
	 */
	public static final int REQ_ADDPET = MSG_BASE + 83;
	public static final int RESP_ADDPET = MSG_BASE + 84;

	/**
	 * 编辑宠物
	 */
	public static final int REQ_EDITPET = MSG_BASE + 85;
	public static final int RESP_EDITPET = MSG_BASE + 86;

	/**
	 * 删除宠物
	 */
	public static final int REQ_REMOVEPET = MSG_BASE + 87;
	public static final int RESP_REMOVEPET = MSG_BASE + 88;

	/**
	 * 我的动态
	 */
	public static final int REQ_GETMYDYNAMIC = MSG_BASE + 73;
	public static final int RESP_GETMYDYNAMIC = MSG_BASE + 74;

	/**
	 * 我的动态更多
	 */
	public static final int REQ_GETMYDYNAMICMORE = MSG_BASE + 89;
	public static final int RESP_GETMYDYNAMICMORE = MSG_BASE + 90;

	/**
	 * 我的关注动态
	 */
	public static final int REQ_GETMYLIKES = MSG_BASE + 91;
	public static final int RESP_GETMYLIKES = MSG_BASE + 92;

	/**
	 * 我的关注动态更多
	 */
	public static final int REQ_GETMYLIKESMORE = MSG_BASE + 93;
	public static final int RESP_GETMYLIKESMORE = MSG_BASE + 94;

	/**
	 * 获取人物信息
	 */
	public static final int REQ_GETMEBERINFO = MSG_BASE + 97;
	public static final int RESP_GETMEBERINFO = MSG_BASE + 98;

	/**
	 * 设置消息已读
	 */
	public static final int REQ_SETNOTICEREAD = MSG_BASE + 101;
	public static final int RESP_SETNOTICEREAD = MSG_BASE + 102;

	/**
	 * 获取荣联信息
	 */
	public static final int REQ_GETRONGLIANMEBER = MSG_BASE + 103;
	public static final int RESP_GETRONGLIANMEBER = MSG_BASE + 104;

	/**
	 * 读取赞博客的人
	 */
	public static final int REQ_BLOG_GETLIKEMEBERS = MSG_BASE + 71;
	public static final int RESP_BLOG_GETLIKEMEBERS = MSG_BASE + 72;

	/**
	 * 读取更多赞博客的人
	 */
	public static final int REQ_BLOG_GETLIKEMEBERSMORE = MSG_BASE + 105;
	public static final int RESP_BLOG_GETLIKEMEBERSMORE = MSG_BASE + 106;

	/**
	 * 读取宠物博客
	 */
	public static final int REQ_GETPETBLOGS = MSG_BASE + 107;
	public static final int RESP_GETPETBLOGS = MSG_BASE + 108;

	/**
	 * 读取关注人列表
	 */
	public static final int REQ_GETRECOMMENDMEBERS = MSG_BASE + 109;
	public static final int RESP_GETRECOMMENDMEBERS = MSG_BASE + 110;

	/**
	 * 批量关注
	 */
	public static final int REQ_ATTENTION = MSG_BASE + 111;
	public static final int RESP_ATTENTION = MSG_BASE + 112;

	/**
	 * 搜索活动
	 */
	public static final int REQ_SEARCHACTIVITY = MSG_BASE + 113;
	public static final int RESP_SEARCHACTIVITY = MSG_BASE + 114;

	/**
	 * 用户举报消息
	 */
	public static final int REQ_REPORT = MSG_BASE + 115;
	public static final int RESP_REPORT = MSG_BASE + 116;

	/**
	 * 用户反馈信息
	 */
	public static final int REQ_FEEDBACK = MSG_BASE + 117;
	public static final int RESP_FEEDBACK = MSG_BASE + 118;

	/**
	 * 通知主界面关注
	 */
	public static final int RESP_MAIN_FOLLOW = MSG_BASE + 119;
	/**
	 * 通知主界面取消关注
	 */
	public static final int RESP_MAIN_CANCELFOLLOW = MSG_BASE + 120;

	/**
	 * 获取请求加入群组消息
	 */
	public static final int REQ_GETADDACTIVITY = MSG_BASE + 121;
	public static final int RESP_GETADDACTIVITY = MSG_BASE + 122;

	/**
	 * 读取评论消息
	 */
	public static final int REQ_GETCommentNOTICE = MSG_BASE + 99;
	public static final int RESP_GETCommentNOTICE = MSG_BASE + 100;

	/**
	 * 读取系统消息
	 */
	public static final int REQ_GETSysNOTICE = MSG_BASE + 123;
	public static final int RESP_GETSysNOTICE = MSG_BASE + 124;

	/**
	 * 通过IM获取活动信息
	 */
	public static final int REQ_GETACTIVITYBYIMID = MSG_BASE + 125;
	public static final int RESP_GETACTIVITYBYIMID = MSG_BASE + 126;

	/**
	 * 删除说说
	 */
	public static final int REQ_REMOVEMICROBLOG = MSG_BASE + 127;
	public static final int RESP_REMOVEMICROBLOG = MSG_BASE + 128;

	/**
	 * 删除评论
	 */
	public static final int REQ_REMOVECOMMENT = MSG_BASE + 129;
	public static final int RESP_REMOVECOMMENT = MSG_BASE + 130;

	/**
	 * 主页赞
	 */
	public static final int REQ_MAINBLOGLIKE = MSG_BASE + 65;
	public static final int RESP_MAINBLOGLIKE = MSG_BASE + 66;

	/**
	 * 主页取消赞
	 */
	public static final int REQ_MAINBLOGCANCELLIKE = MSG_BASE + 95;
	public static final int RESP_MAINBLOGCANCELLIKE = MSG_BASE + 96;

	/**
	 * 详情赞
	 */
	public static final int REQ_DETAILBLOGLIKE = MSG_BASE + 131;
	public static final int RESP_DETAILBLOGLIKE = MSG_BASE + 132;

	/**
	 * 详情取消赞
	 */
	public static final int REQ_DETAILBLOGCANCELLIKE = MSG_BASE + 133;
	public static final int RESP_DETAILBLOGCANCELLIKE = MSG_BASE + 134;

	/**
	 * 用户注册完登录
	 */
	public static final int REQ_REGISTE_LOGIN = MSG_BASE + 138;
	public static final int RESP_REGISTE_LOGIN = MSG_BASE + 139;

	/**
	 * 通知主页赞
	 */
	public static final int RESP_DETAILTOMAINLIKE = MSG_BASE + 135;

	/**
	 * 通知主页取消赞
	 */
	public static final int RESP_DETAILTOMAINCANCELLIKE = MSG_BASE + 136;

	/**
	 * 获取荣联信息
	 */
	public static final int REQ_REGISTE_GETRONGLIANMEBER = MSG_BASE + 140;
	public static final int RESP_REGISTE_GETRONGLIANMEBER = MSG_BASE + 141;

	/**
	 * 检查动态是否存在
	 */
	public static final int REQ_CHECKMICROBLOGEXISTS = MSG_BASE + 142;
	public static final int RESP_CHECKMICROBLOGEXISTS = MSG_BASE + 143;

	/**
	 * 检查评论是否存在
	 */
	public static final int REQ_CHECKCOMMENTEXISTS = MSG_BASE + 144;
	public static final int RESP_CHECKCOMMENTEXISTS = MSG_BASE + 145;

	/**
	 * detail解散活动
	 */
	public static final int REQ_DETAIL_DISSMISACTIVITY = MSG_BASE + 146;
	public static final int RESP_DETAIL_DISSMISACTIVITY = MSG_BASE + 147;

	/**
	 * 退出活动
	 */
	public static final int REQ_DETAIL_EXITACTIVITY = MSG_BASE + 148;
	public static final int RESP_DETAIL_EXITACTIVITY = MSG_BASE + 149;

	public static final int REQ_MAIN_REFRESH = MSG_BASE + 150;

	public static final int REQ_MAIN_DELETE = MSG_BASE + 151;

	public static final int RESP_OUTOFLINE = MSG_BASE + 152;

	/**
	 * 更换手机号
	 */
	public static final int REQ_CHANGEPHONE = MSG_BASE + 153;
	public static final int RESP_CHANGEPHONE = MSG_BASE + 154;

	/**
	 * 更换手机号发短信
	 */
	public static final int REQ_CHANGEPHONESMS = MSG_BASE + 155;
	public static final int RESP_CHANGEPHONESMS = MSG_BASE + 156;

	/**
	 * MAC匹配
	 */
	public static final int REQ_SEARCHMAC = MSG_BASE + 157;
	public static final int RESP_SEARCHMAC = MSG_BASE + 158;

	/**
	 * 已绑定的宠物
	 */
	public static final int REQ_PETBINDLIST = MSG_BASE + 159;
	public static final int RESP_PETBINDLIST = MSG_BASE + 160;

	/**
	 * 绑定宠物
	 */
	public static final int REQ_BINDPETCOLLAR = MSG_BASE + 161;
	public static final int RESP_BINDPETCOLLAR = MSG_BASE + 162;

	/**
	 * 向服务端传递信息
	 */
	public static final int REQ_WRITEACTIVITY = MSG_BASE + 163;
	public static final int RESP_WRITEACTIVITY = MSG_BASE + 164;

	/**
	 * 获取周的信息
	 */
	public static final int REQ_WEEKREPORT = MSG_BASE + 165;
	public static final int RESP_WEEKREPORT = MSG_BASE + 166;

	/**
	 * 获取月的信息
	 */
	public static final int REQ_MONTHREPORT = MSG_BASE + 167;
	public static final int RESP_MONTHREPORT = MSG_BASE + 168;

	/**
	 * 已绑定的宠物主页
	 */
	public static final int REQ_PETBINDLIST_MAIN = MSG_BASE + 169;
	public static final int RESP_PETBINDLIST_MAIN = MSG_BASE + 170;

	/**
	 * 回复评论
	 */
	public static final int REQ_REPLYCOMMENT = MSG_BASE + 171;
	public static final int RESP_REPLYCOMMENT = MSG_BASE + 172;

	/**
	 * 解绑宠物
	 */
	public static final int REQ_UNBINDPETCOLLAR = MSG_BASE + 173;
	public static final int RESP_UNBINDPETCOLLAR = MSG_BASE + 174;

	/**
	 * 消息删除
	 */
	public static final int REQ_NOTICEREMOVE = MSG_BASE + 175;
	public static final int RESP_NOTICEREMOVE = MSG_BASE + 176;

	/**
	 * 消息全部删除
	 */
	public static final int REQ_NOTICEREMOVEALL = MSG_BASE + 177;
	public static final int RESP_NOTICEREMOVEALL = MSG_BASE + 178;

	/**
	 * 问问题
	 */
	public static final int REQ_QuestionAdd = MSG_BASE + 179;
	public static final int RESP_QuestionAdd = MSG_BASE + 180;

	/**
	 * 回答问题
	 */
	public static final int REQ_QuestionAnswer = MSG_BASE + 181;
	public static final int RESP_QuestionAnswer = MSG_BASE + 182;

	/**
	 * 回答列表
	 */
	public static final int REQ_QuestionAnswerList = MSG_BASE + 183;
	public static final int RESP_QuestionAnswerList = MSG_BASE + 184;

	/**
	 * 问题列表
	 */
	public static final int REQ_QuestionQuestionList = MSG_BASE + 185;
	public static final int RESP_QuestionQuestionList = MSG_BASE + 186;

	/**
	 * 搜索问题
	 */
	public static final int REQ_SearchQuestionList = MSG_BASE + 187;
	public static final int RESP_SearchQuestionList = MSG_BASE + 188;

	/**
	 * 获取周的信息
	 */
	public static final int REQ_ONEWEEKREPORT = MSG_BASE + 189;
	public static final int RESP_ONEWEEKREPORT = MSG_BASE + 190;

}
