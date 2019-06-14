package cn.piesat.minemonitor.mapdata.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.piesat.minemonitor.R;


/**
 * Created by Administrator on 2017/5/18.
 */

public class EditTextDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnClickListener mListener;
    private EditText m_editText;
    private TextView m_tvTitle;
    public EditTextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setNumValue(int num) {
        m_editText.setText(num + "");
    }
    public void setNumValue(double num) {
        m_editText.setText(num + "");
    }

    public void setStrValue(String value){
        m_editText.setText(value);
    }

    public void setInputType(int type){
        if(m_editText!=null){
            m_editText.setInputType(type);
        }
    }

    public void setTitle(String title){
        if(TextUtils.isEmpty(title)){
            m_tvTitle.setText("请输入");
        }else{
            m_tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                this.dismiss();
                break;
            case R.id.confirm_button:
                this.dismiss();
                if (mListener != null) {
                    String strNum = m_editText.getText().toString();
                    mListener.onClick(strNum);
                }
                break;
        }
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onClick(String num);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(mContext, R.layout.dialog_edittext_confirm, null);
        setContentView(contentView);
        m_editText = (EditText) contentView.findViewById(R.id.edittext);
        m_tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        Button cancel = (Button) contentView.findViewById(R.id.cancel_button);
        Button confirm = (Button) contentView.findViewById(R.id.confirm_button);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }
}
