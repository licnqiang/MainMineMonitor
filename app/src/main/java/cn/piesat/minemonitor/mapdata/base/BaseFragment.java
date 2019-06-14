package cn.piesat.minemonitor.mapdata.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    Unbinder unbinder;
    public abstract int getLayoutId();
    public abstract void loadingData();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getContext(), getLayoutId(), null);
        unbinder =  ButterKnife.bind(this, rootView);
        loadingData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void toActivity(Class<?> toClsActivity) {
        toActivity(toClsActivity, null);
    }

    public void toActivity(Class<?> toClsActivity, Bundle bundle) {
        Intent intent = new Intent(getContext(), toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void toActivityForResult(Class<?> toClsActivity,int requestCode){
        toActivityForResult(toClsActivity,null,requestCode);
    }

    public void toActivityForResult(Class<?> toClsActivity, Bundle bundle,int requestCode){
        Intent intent = new Intent(getContext(), toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent,requestCode);
    }

    public void setResult(Class<?> toClsActivity,int requestCode){
        toActivityForResult(toClsActivity,null,requestCode);
    }

    public void setResult(Class<?> toClsActivity, Bundle bundle,int resultCode){
        Intent intent = new Intent(getContext(), toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().setResult(resultCode,intent);
    }
}
