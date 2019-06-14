package cn.piesat.minemonitor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.piesat.minemonitor.entitys.RegisteredEntity;
import cn.piesat.minemonitor.util.DbUtil;
import cn.piesat.minemonitor.util.ToastUtil;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_user_name)
    EditText et_user_name;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_registered)
    Button btn_registered;
    private String userName;
    private String password;
    private DbUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);
        ininView();

    }

    private void ininView() {
        et_password.setText("");
        et_user_name.setText("");
        btn_registered.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registered:
               /* userName = et_user_name.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
                    RegisteredEntity entity = new RegisteredEntity();
                    entity.setName(userName);
                    entity.setPassword(password);
                    try {
                        dbUtil = DbUtil.getInstance();
                        dbUtil.openDb(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int data = dbUtil.saveUser(entity);
                   *//* if(data2.equals("-1")){

                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }else{
                        ToastUtil.show(this,"注册失败,请重新注册 ");
                    }*//*

                    if (data==1){
                        ToastUtil.show(this,"注册成功");
                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }else  if (data==-1)
                    {
                        ToastUtil.show(this,"用户名已存在");
                    }
                    else
                    {
                  }
                }*/
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        et_user_name.setText("");
        et_password.setText("");
    }
}
