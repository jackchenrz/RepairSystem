package com.czvv.repairsystemmobile.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.czvv.repairsystemmobile.base.BaseFragment;

public class ComFragmentAdapter extends FragmentStatePagerAdapter {

	protected List<BaseFragment> items;
	
	public ComFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	public ComFragmentAdapter(FragmentManager fm,List<BaseFragment> items) {
		super(fm);
		this.items = items;
	}

	@Override
	public Fragment getItem(int position) {
		return items.get(position);
	}

	@Override
	public int getCount() {
		return items.size();
	}
}
