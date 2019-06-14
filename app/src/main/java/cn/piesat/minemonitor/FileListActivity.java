package cn.piesat.minemonitor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.piesat.minemonitor.adapter.MyFileAdapter;
import cn.piesat.minemonitor.entitys.FileEntity;
import cn.piesat.minemonitor.util.PermisionUtils;

public class FileListActivity extends BaseActivity implements View.OnClickListener {


    private ListView mListView;
    private Button btnComfirm;
    private MyFileAdapter mAdapter;
    private Context mContext;
    private File currentFile;
    String sdRootPath;

    private ArrayList<FileEntity> mList;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist);
        PermisionUtils.verifyStoragePermissions(this);
        openFile();
        mContext = this;
        mList = new ArrayList<>();
    }


    private void openFile() {
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        if(mAdapter ==null){
                            mAdapter = new MyFileAdapter(getApplication(), mList);
                            mListView.setAdapter(mAdapter);
                        }else{
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                      }
                    }
                  };
        sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        currentFile = new File(sdRootPath);
        initView();
        getData(sdRootPath);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView1);
        btnComfirm = (Button) findViewById(R.id.button1);
        btnComfirm.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final FileEntity entity = mList.get(position);
                if(entity.getFileType() == FileEntity.Type.FLODER){
                    currentFile = new File(entity.getFilePath());
                    getData(entity.getFilePath());
                }else if(entity.getFileType() == FileEntity.Type.FILE){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, entity.getFilePath()+"  "+entity.getFileName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
     }

    private void getData(final String path) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                findAllFiles(path);
              }
        }.start();
    }
    /**
     * 查找path地址下所有文件
     * @param path
     */
    public void findAllFiles(String path) {
        mList.clear();

        if(path ==null ||path.equals("")){
            return;
        }
        File fatherFile = new File(path);
        File[] files = fatherFile.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                FileEntity entity = new FileEntity();
                boolean isDirectory = files[i].isDirectory();
                if(isDirectory ==true){
                    entity.setFileType(FileEntity.Type.FLODER);
                }else{
                    entity.setFileType(FileEntity.Type.FILE);
                }
                entity.setFileName(files[i].getName().toString());
                entity.setFilePath(files[i].getAbsolutePath());
                entity.setFileSize(files[i].length()+"");
                mList.add(entity);
            }
        }
        handler.sendEmptyMessage(1);

    }

    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed...");
        if(sdRootPath.equals(currentFile.getAbsolutePath())){
            System.out.println("已经到了根目录...");
            return ;
        }

        String parentPath = currentFile.getParent();
        currentFile = new File(parentPath);
        getData(parentPath);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                setResult(100);
                finish();
                break;

            default:
                break;
        }

    }
}
