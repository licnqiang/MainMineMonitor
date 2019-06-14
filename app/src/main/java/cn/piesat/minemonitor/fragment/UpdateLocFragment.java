package cn.piesat.minemonitor.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.HomeActivity;
import cn.piesat.minemonitor.MainActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.ContentListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateLocFragment extends Fragment implements View.OnClickListener {


    private List<String> titles;
    private Intent intent;
private SharedPreferences sper;
    private SharedPreferences.Editor edit;

    public UpdateLocFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_map_loc, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton save = view.findViewById(R.id.btn_save);
        LinearLayout back = view.findViewById(R.id.ll_back);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        titles = new ArrayList<>();
        titles = ((HomeActivity) activity).getTitles();//通过强转成宿主activity，就可以获取到传递过来的数据
//        Toast.makeText(activity, ""+ titles.get(0)+ titles.get(1), Toast.LENGTH_SHORT).show();
        sper = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                edit = sper.edit();
                //通过editor对象写入数据
                edit.putString("UpdateLoc", "loc");
                //提交数据存入到xml文件中
                edit.commit();
                startActivity(new Intent(getContext(), ContentListActivity.class));
                CustomSQLTools s = new CustomSQLTools();
                s.updateXYNO(sper.getString("LocNo","Null"),getContext(),sper.getString("demX",titles.get(0)),sper.getString("demY",titles.get(1)));
                getActivity().onBackPressed();//销毁自己
                break;
            case R.id.ll_back:
                intent =new Intent(getContext(), ContentListActivity.class);
                startActivity(intent);
                getActivity().onBackPressed();//销毁自己

                break;
        }
    }
}
