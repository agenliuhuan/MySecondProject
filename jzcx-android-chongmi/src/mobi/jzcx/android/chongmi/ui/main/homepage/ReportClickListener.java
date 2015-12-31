package mobi.jzcx.android.chongmi.ui.main.homepage;

import mobi.jzcx.android.chongmi.biz.vo.CommontObject;
import mobi.jzcx.android.chongmi.biz.vo.NoticeObject;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;

public interface ReportClickListener {
	public void reportClick(CommontObject commentObj);
	
	public void mainClick(CommontObject commentObj);
	
	public void systemNewsMainClick(SystemNoticeObject noticeObj);
	
	public void systemNewsDelClick(SystemNoticeObject noticeObj);
	
	public void commentMainClick(NoticeObject commentObj);
	
	public void commentDelClick(NoticeObject commentObj);
	
	
}
