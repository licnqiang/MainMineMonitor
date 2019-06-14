package cn.piesat.minemonitor.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import java.util.List;

import cn.piesat.minemonitor.entitys.ControlMsgEntity;
import cn.piesat.minemonitor.entitys.EventMsgEntity;
import cn.piesat.minemonitor.util.ToastUtil;


public abstract class BaseFragment extends Fragment {

    protected Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutView(), null);
        x.view().inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
        setClickListener();
    }

    protected abstract int getLayoutView();

    protected abstract void initView();

    protected abstract void initData(Bundle bundle);

    protected abstract void setClickListener();
    public void onBackPressed() {
//		activity.finish();
    }
    public void showToast(String msg) {
        ToastUtil.show(getActivity(), msg);
    }

    public abstract void addList(List<ControlMsgEntity> entityList);
    public abstract void addEventList(List<EventMsgEntity> entityList);
    public abstract void showFailedError(String error);
}
