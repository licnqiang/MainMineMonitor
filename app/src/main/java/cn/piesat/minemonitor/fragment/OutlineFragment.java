package cn.piesat.minemonitor.fragment;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.home.CListActivity;
import cn.piesat.minemonitor.home.ContentListActivity;

/**
 * A simple {@link Fragment} subclass.
 * 图斑详情编辑。
 */
public class OutlineFragment extends Fragment implements View.OnClickListener {

    //顶部title文字
//    private TextView topTitle;
//    private ImageView menu;
    private ImageButton save;
    private ImageButton undo;
    private PopupWindow poplayer;
    private ImageView layer;
    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    public OutlineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_outline, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            save = view.findViewById(R.id.btn_save);
            save.setOnClickListener(this);
            undo = view.findViewById(R.id.btn_undo);
            undo.setOnClickListener(this);
            layer = view.findViewById(R.id.iv_layer);
            layer.setOnClickListener(this);
            initPop();
            sper = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        }
    }

    private void initPop() {
        View popupView3 = getLayoutInflater().inflate(R.layout.pop_layer, null);
        poplayer = new PopupWindow(popupView3, 400, ActionBar.LayoutParams.MATCH_PARENT, true);
        poplayer.setTouchable(true);
        poplayer.setOutsideTouchable(true);
        poplayer.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        poplayer.setAnimationStyle(R.style.PopupAnimationRight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String cx = sper.getString("demX", "null");
                String cy = sper.getString("demY", "null");
                if (cx.equals("null")||cy.equals("null")){
                    Toast.makeText(getContext(), "请重新编辑", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
//                Log.d("Amm", "onClick: "+sper.getString("demX","null"));
//                Log.d("Amm", "onClick: "+sper.getString("demY","null"));
                getActivity().onBackPressed();
                break;
            case R.id.btn_undo:
                Toast.makeText(getContext(), "撤销成功", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;
            case R.id.iv_layer:
                poplayer.showAtLocation(v.findViewById(R.id.iv_layer), Gravity.RIGHT, 0, 0);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
