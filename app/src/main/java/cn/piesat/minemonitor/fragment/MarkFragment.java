package cn.piesat.minemonitor.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.home.VerNoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences sper;
    private SharedPreferences.Editor edit;
    public MarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_mark, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = view.findViewById(R.id.ll_back);
        linearLayout.setOnClickListener(this);
        ImageView imageView = view.findViewById(R.id.img_bj);
        imageView.setOnClickListener(this);
        sper = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                break;
            case R.id.img_bj:
                startActivity(new Intent(getContext(), VerNoActivity.class));
                getActivity().onBackPressed();//销毁自己
                break;
        }
    }
}
