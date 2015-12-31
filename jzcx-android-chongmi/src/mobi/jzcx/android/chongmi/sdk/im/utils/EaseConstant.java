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
package mobi.jzcx.android.chongmi.sdk.im.utils;

public class EaseConstant {
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

	public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
	public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
	// /**
	// * app自带的动态表情，直接去resource里获取图片
	// */
	// public static final String MESSAGE_ATTR_BIG_EXPRESSION_ICON =
	// "em_big_expression_icon";
	// /**
	// * 动态下载的表情图片，需要知道表情url
	// */
	// public static final String MESSAGE_ATTR_BIG_EXPRESSION_URL =
	// "em_big_expression_url";

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	public static final String EXTRA_CHAT_TYPE = "chatType";
	public static final String EXTRA_USER_IMID = "userId";

	public static final String EXTRA_GROUP_NAME = "groupName";
	public static final String EXTRA_GROUP_AVATAR = "groupIcoUrl";
	public static final String EXTRA_MEMBER_ID = "memberid";
	public static final String EXTRA_MEMBER_NAME = "memberName";
	public static final String EXTRA_MEMBER_AVATAR = "memberIcoUrl";

	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String ACCOUNT_CONFLICT = "conflict";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
	public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
}
