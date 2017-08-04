package slmodule.shenle.com;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Home页 的底部的Viewpager的适配器
 * 
 * @author liuyl
 * 
 */
public class BaseViewPagerFragmentAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> list = new ArrayList<>();

	public BaseViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		if (list!=null){
			this.list.clear();
			this.list.addAll(list);
		}
	}

	public void reLoadAdapter(List<Fragment> list) {
		this.list.clear();
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
