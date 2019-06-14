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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.HomeActivity;
import cn.piesat.minemonitor.MainActivity;
import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.home.AddMapActivity;
import cn.piesat.minemonitor.home.ContentListActivity;

/**
 * A simple {@link Fragment} subclass.
 * 图斑定位
 */
public class MapLocFragment extends Fragment implements View.OnClickListener {


    private List<String> titles;
    private Intent intent;
    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    public MapLocFragment() {
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
        sper = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        titles = new ArrayList<>();
        titles = ((HomeActivity) activity).getTitles();//通过强转成宿主activity，就可以获取到传递过来的数据
//        Toast.makeText(activity, ""+ titles.get(0)+ titles.get(1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:

                if (!sper.getString("AddTBBH","null").equals("null")){
                    Intent intent = new Intent(getActivity(), AddMapActivity.class);
                    intent.putExtra("AddX", sper.getString("demX","null"));
                    intent.putExtra("AddY", sper.getString("demY","null"));
                    getActivity().setResult(12,intent);
                    getActivity().onBackPressed();
                }else{
                    startActivity(new Intent(getContext(), ContentListActivity.class));
                    getActivity().onBackPressed();//销毁自己
                }

                break;
            case R.id.ll_back:
                intent =new Intent(getContext(), ContentListActivity.class);
                startActivity(intent);
                getActivity().onBackPressed();//销毁自己

                break;
        }
    }
}
