package mobi.jzcx.android.chongmi.ui.main.serve;

import mobi.jzcx.android.chongmi.biz.vo.GroupRequest;

public interface RequestClickListener {
	public void acceptClick(GroupRequest groupRequest);
	
	public void deleteClick(GroupRequest groupRequest);
	
	public void mainClick(GroupRequest groupRequest);
}
