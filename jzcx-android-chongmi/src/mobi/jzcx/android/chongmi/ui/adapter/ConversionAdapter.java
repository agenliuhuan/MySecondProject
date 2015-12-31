package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.db.dao.ImContactDao;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseCommonUtils;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseSmileUtils;
import mobi.jzcx.android.chongmi.sdk.im.utils.EaseUserUtils;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ConversionAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<EMConversation> list;
	ImContactDao contactDao = new ImContactDao();

	public ConversionAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<EMConversation>();
	}

	public void updateData(List<EMConversation> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public EMConversation getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_newsim, parent, false);
			holder = new viewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		// 获取与此用户/群组的会话
		EMConversation conversation = list.get(position);
		// 获取用户username或者群组groupid
		String username = conversation.getUserName();
		ImContactObject contact = contactDao.getImContactById(username);
		// if (conversation.getType() == EMConversationType.GroupChat) {
		// // 群聊消息，显示群聊头像
		// // holder.user_avatar.setImageResource(R.drawable.ease_group_icon);
		// EMGroup group = EMGroupManager.getInstance().getGroup(username);
		// holder.user_name.setText(group != null ? group.getGroupName() :
		// username);
		// } else if (conversation.getType() == EMConversationType.ChatRoom) {
		// // holder.user_avatar.setImageResource(R.drawable.ease_group_icon);
		// EMChatRoom room = EMChatManager.getInstance().getChatRoom(username);
		// holder.user_name.setText(room != null &&
		// !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
		// } else {
		// EaseUserUtils.setUserAvatar(mContext, username, holder.user_avatar);
		// EaseUserUtils.setUserNick(username, holder.user_name);
		// }
		if (contact != null) {
			if (!CommonTextUtils.isEmpty(contact.getContactName())) {
				holder.user_name.setText(contact.getContactName());
			} else {
				holder.user_name.setText(username);
			}
			if (!CommonTextUtils.isEmpty(contact.getContactAvatar())) {
				ImageLoaderHelper.displayAvatar(contact.getContactAvatar(), holder.user_avatar);
			} else {
				holder.user_avatar.setImageResource(R.drawable.avatar_default_image);
			}
		} else {
			holder.user_avatar.setImageResource(R.drawable.avatar_default_image);
			holder.user_name.setText(username);
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.numTv.setText(String.valueOf(conversation.getUnreadMsgCount()));
			holder.numTv.setVisibility(View.VISIBLE);
		} else {
			holder.numTv.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.content.setText(
					EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(lastMessage, (mContext))),
					BufferType.SPANNABLE);

			holder.timeTv.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
				// holder.msgState.setVisibility(View.VISIBLE);
			} else {
				// holder.msgState.setVisibility(View.GONE);
			}
		}
		holder.acceptBtn.setVisibility(View.GONE);
		return convertView;
	}
	class viewHolder {
		ImageView user_avatar;
		TextView user_name;
		TextView content;
		TextView timeTv;
		TextView numTv;
		ImageView acceptBtn;

		viewHolder(View view) {
			this.user_avatar = (ImageView) view.findViewById(R.id.newim_Item_userimg);
			this.user_name = (TextView) view.findViewById(R.id.newim_Item_username);
			this.content = (TextView) view.findViewById(R.id.newim_Item_content);
			this.timeTv = (TextView) view.findViewById(R.id.newim_Item_time);
			this.numTv = (TextView) view.findViewById(R.id.newim_Item_numtext);
			this.acceptBtn = (ImageView) view.findViewById(R.id.newim_Item_acceptBtn);
		}
	}

}
