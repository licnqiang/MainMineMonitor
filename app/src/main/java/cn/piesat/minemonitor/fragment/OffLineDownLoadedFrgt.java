package cn.piesat.minemonitor.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.piesat.minemonitor.OffLineMapActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.adapter.LocalMapAdapter;
import cn.piesat.minemonitor.base.BaseFragment;
import cn.piesat.minemonitor.entitys.ControlMsgEntity;
import cn.piesat.minemonitor.entitys.EventMsgEntity;


public class OffLineDownLoadedFrgt extends BaseFragment {

	@ViewInject(R.id.lv_offline_downloaded)
	private ListView m_lvOffLineDownloaded;

	private OffLineMapActivity offLineMapActivity;
	public LocalMapAdapter lAdapter;

	@Override
	protected int getLayoutView() {
		// TODO Auto-generated method stub
		return R.layout.off_line_downloaded;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		Log.e("frgt", "initView");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("frgt", "onResume");
		super.onResume();
//		lAdapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub

		Log.e("frgt", "onAttach");
		offLineMapActivity = (OffLineMapActivity) activity;
		super.onAttach(activity);
	}

	public OffLineDownLoadedFrgt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void updateView() {
		// getListData();
		lAdapter.notifyDataSetChanged();
	}


	@Override
	protected void initData(Bundle bundle) {
		lAdapter = new LocalMapAdapter(offLineMapActivity);
		updateView();
		m_lvOffLineDownloaded.setAdapter(lAdapter);
	}


	@Override
	protected void setClickListener() {
	}

	@Override
	public void addList(List<ControlMsgEntity> entityList) {

	}

	@Override
	public void addEventList(List<EventMsgEntity> entityList) {

	}

	@Override
	public void showFailedError(String error) {

	}


}
