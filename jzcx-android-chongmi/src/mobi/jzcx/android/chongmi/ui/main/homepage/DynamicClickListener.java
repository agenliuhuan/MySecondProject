package mobi.jzcx.android.chongmi.ui.main.homepage;

import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;

public interface DynamicClickListener {
	public void headerImageClick(DynamicObject obj);

	public void headerFollowImageClick(DynamicObject obj);

	public void userImageClick(DynamicObject obj);

	public void userFollowImageClick(DynamicObject obj);

	public void viewPaperClick(DynamicObject obj);

	public void petClick(DynamicObject obj);

	public void moreZanClick(DynamicObject obj);
	
	public void commentLLClick(DynamicObject obj);

	public void dianZanOrNotClick(DynamicObject obj);

	public void shareMoreClick(DynamicObject obj);
	
	public void zanlistClick(DynamicObject obj);
	
	public void mainClick(DynamicObject obj);

}
