package cn.piesat.minemonitor.home.minetype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;
import cn.piesat.minemonitor.home.ContentListActivity;

public class MineTypeQueryActivity extends Activity {

    private EditText et_ss;
    private ListView lsv_ss;
    private List<String> list = new ArrayList<String>();
    private MyAdapter adapter = null;
    private int useModule;
    private String belongs;
    private Bundle bundle;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_type_query);
        CustomSQLTools s = new CustomSQLTools();
        useModule = getIntent().getIntExtra("Code", -1);
        switch (useModule) {
            case 500:
                bundle = getIntent().getExtras();
                belongs = bundle.getString("BelongId");
                list = s.getDLLX(MineTypeQueryActivity.this, belongs);
                list.add("无");
                break;
        }
        setViews();// 控件初始化
        setData();// 给listView设置adapter
        setListeners();// 设置监听

    }

    private void setListeners() {
        // 没有进行搜索的时候，也要添加对listView的item单击监听
        setItemClick(list);

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
         */
        et_ss.addTextChangedListener(new TextWatcher() {

            /**
             *
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 数据初始化并设置adapter
     */
    private void setData() {
//        initData();// 初始化数据

        // 这里创建adapter的时候，构造方法参数传了一个接口对象，这很关键，回调接口中的方法来实现对过滤后的数据的获取
        adapter = new MyAdapter(list, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                // 这里可以拿到过滤后数据，所以在这里可以对搜索后的数据进行操作
                Log.e("TAG", "接口回调成功");
                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });
        lsv_ss.setAdapter(adapter);
    }

    /**
     * 给listView添加item的单击事件
     *
     * @param filter_lists 过滤后的数据集
     */
    protected void setItemClick(final List<String> filter_lists) {
        lsv_ss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                // 点击对应的item时，弹出toast提示所点击的内容
//                Toast.makeText(MineTypeQueryActivity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();
                data = filter_lists.get(position);
                Intent intent = new Intent(MineTypeQueryActivity.this, ContentListActivity.class);
                intent.putExtra("DataName", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 控件初始化
     */
    private void setViews() {
        // EditText控件
        et_ss = (EditText) findViewById(R.id.ed_text);
        // ListView控件
        lsv_ss = (ListView) findViewById(R.id.lv_list);
    }
}
