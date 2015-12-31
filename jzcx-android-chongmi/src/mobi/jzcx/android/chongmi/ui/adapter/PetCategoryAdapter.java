package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.PetStyleObject;
import mobi.jzcx.android.chongmi.sdk.fresco.FrescoHelper;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class PetCategoryAdapter extends BaseAdapter implements SectionIndexer {
	private List<PetStyleObject> list = null;
	private Context mContext;

	public PetCategoryAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<PetStyleObject>();
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<PetStyleObject> data) {
		if (list != null) {
			list.clear();
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public PetStyleObject getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final PetStyleObject mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.listitem_petcategory, null);
			viewHolder.name = (TextView) view.findViewById(R.id.itemcategory_name);
			viewHolder.toptitle = (TextView) view.findViewById(R.id.itemcategory_toptitle);
			viewHolder.img = (SimpleDraweeView) view.findViewById(R.id.itemcategory_image);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.toptitle.setVisibility(View.VISIBLE);
			viewHolder.toptitle.setText(mContent.getSpell());
		} else {
			viewHolder.toptitle.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(this.list.get(position).getName())) {
			viewHolder.name.setText(this.list.get(position).getName());
		}
		if (!CommonTextUtils.isEmpty(this.list.get(position).getIcoUrl())) {
			FrescoHelper.displayImageview(viewHolder.img,
					this.list.get(position).getIcoUrl() + CommonUtils.getAvatarSize(mContext),
					R.drawable.avatar_default_image, mContext.getResources(), true);
		}
		return view;

	}

	final static class ViewHolder {
		TextView toptitle;
		TextView name;
		SimpleDraweeView img;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSpell().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSpell();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
