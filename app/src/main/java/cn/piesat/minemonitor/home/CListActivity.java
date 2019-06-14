package cn.piesat.minemonitor.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.db.CustomSQLTools;

public class CListActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView list;
    private int useModule;
    List<String> m_dataList;
    private ArrayAdapter adapter;
    private Intent intent;
    private String data;
    private String belongs;
    private Bundle bundle;
    public static final String Belongs = "3005";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clist);
        CustomSQLTools s = new CustomSQLTools();
        useModule = getIntent().getIntExtra("Code", -1);
        list = findViewById(R.id.lv_list);
        m_dataList = new ArrayList<>();
        switch (useModule) {
            case 100:
                //旗县名称
                list.setVisibility(View.GONE);
                initQXMC();
                break;
            case 200:
            case 300:
            case 400:
            case 500:
            case 600:
            case 1002:
            case 1003:
                //地类类型
                //破坏类型
                //治理对象
                //开采矿种
                //开采矿种
                bundle = getIntent().getExtras();
                belongs = bundle.getString("BelongId");
                m_dataList = s.getDLLX(CListActivity.this, belongs);
                break;
            case 201:
                m_dataList.add("井口");
                break;
            case 202:
                m_dataList.add("地裂缝");
                break;
            case 700:
                m_dataList = s.getDLLX(this, Belongs);
                break;
            case 800:
                m_dataList = s.getDLDX(CListActivity.this);
                break;
            case 1000:
                m_dataList = s.Txt();
                break;

        }
        list.setOnItemClickListener(this);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, m_dataList);
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        data = m_dataList.get(position);
        Intent intent = new Intent(this, ContentListActivity.class);
        intent.putExtra("DataName", data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void initQXMC() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_list);
        listView.setVisibility(View.VISIBLE);
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, initGroupData(),
                R.layout.item_expand_group_normal, new String[]{Constant.BOOK_NAME}, new int[]{R.id.label_group_normal},
                initChildData(), R.layout.item_expand_child, new String[]{Constant.FIGURE_NAME}, new int[]{R.id.label_expand_child});
        listView.setAdapter(adapter);

        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });
        //  设置子选项点击监听事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                data = Constant.FIGURES[groupPosition][childPosition];
                intent = new Intent(CListActivity.this, ContentListActivity.class);
                intent.putExtra("DataName", data);
                setResult(RESULT_OK, intent);
                finish();
//                Toast.makeText(CListActivity.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // 构建分组项显示的数据
    private List<Map<String, String>> initGroupData() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        for (int i = 0; i < Constant.BOOKS.length; i++) {
            map = new HashMap<>();
            map.put(Constant.BOOK_NAME, Constant.BOOKS[i]);
            list.add(map);
        }
        return list;
    }

    // 构建子选项显示的数据
    private List<List<Map<String, String>>> initChildData() {
        List<List<Map<String, String>>> list = new ArrayList<>();
        List<Map<String, String>> child;
        Map<String, String> map;
        int row = Constant.FIGURES.length;
        int column = Constant.FIGURES[0].length;
        for (int i = 0; i < row; i++) {
            child = new ArrayList<>();
            if (i == 0) {
                column = 9;
            } else if (i == 1) {
                column = 9;
            } else if (i == 2) {
                column = 3;
            } else if (i == 3) {
                column = 12;
            } else if (i == 4) {
                column = 8;
            } else if (i == 5) {
                column = 8;
            } else if (i == 6) {
                column = 13;
            } else if (i == 7) {
                column = 7;
            } else if (i == 8) {
                column = 11;
            } else if (i == 9) {
                column = 6;
            } else if (i == 10) {
                column = 12;
            } else if (i == 11) {
                column = 3;
            }
            for (int j = 0; j < column; j++) {
                map = new HashMap<>();
                map.put(Constant.FIGURE_NAME, Constant.FIGURES[i][j]);
                child.add(map);
            }
            list.add(child);
        }
        return list;
    }
}

