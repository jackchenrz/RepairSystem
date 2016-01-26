package com.czvv.repairsystemmobile.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.activity.RepairHandlerActivity;
import com.czvv.repairsystemmobile.adapter.GirdViewAdapter;
import com.czvv.repairsystemmobile.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairFragment extends BaseFragment implements OnItemClickListener {

	@ViewInject(R.id.tv_title)
	TextView tvTitle;
	@ViewInject(R.id.gv_items)
	GridView gvItems;
	

	private String[] names = new String[] { "机械设备", "行安设备"};
	private int[] imageIds = new int[] { R.drawable.tech_eqpt, R.drawable.fivet_eqpt};
	private GirdViewAdapter adapter;
	
	@Override
	public View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_update, null);
		ViewUtils.inject(this,view);
		tvTitle.setText("报修");
		
		adapter = new GirdViewAdapter(names) {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = null;
				if(convertView == null){
					view =  View.inflate(getActivity(), R.layout.gird_item_home, null);
				}else{
					view = convertView;
				}
				
				TextView name = (TextView) view.findViewById(R.id.tv_name_grid_item);
				name.setText(names[position]);
				name.setTextColor(getActivity().getResources().getColor(R.color.title));
				
				ImageView image = (ImageView) view.findViewById(R.id.iv_icon_gird_item);
				image.setBackgroundResource(imageIds[position]);
				
				
				return view;
			}
		};
		gvItems.setAdapter(adapter);
		gvItems.setOnItemClickListener(this);
		
		return view;
	}
	

	@Override
	public void initData() {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0: //机械设备
			MApplication.mRepairType = "Tech";
			startActivity(new Intent(getActivity(), RepairHandlerActivity.class));
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case 1: //行安设备
			MApplication.mRepairType = "5T";
			startActivity(new Intent(getActivity(), RepairHandlerActivity.class));
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		}
	}
}
