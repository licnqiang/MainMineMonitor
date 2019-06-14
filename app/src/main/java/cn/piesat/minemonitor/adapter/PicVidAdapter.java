package cn.piesat.minemonitor.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.entity.CheckInfoEntiy;
import cn.piesat.minemonitor.home.contentlist.ShowMediaActivity;
import cn.piesat.minemonitor.media.PlayMediaActivity;

/**
 * Created by yjl on 2018/3/21.
 */

public class PicVidAdapter extends ArrayAdapter<CheckInfoEntiy> implements View.OnClickListener {
    private int resourceId;
    private ShowMediaActivity mContext;
    private InnerItemOnclickListener mListener;
    public PicVidAdapter(@NonNull Context context, int resource, List<CheckInfoEntiy> textViewResourceId) {
        super(context, resource, textViewResourceId);
        resourceId = resource;
        mContext = (ShowMediaActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final CheckInfoEntiy filePath = getItem(position);
        ViewHolder vh;
        convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        vh = new ViewHolder();
        vh.showpic = convertView.findViewById(R.id.iv_show_pic);
        vh.context = convertView.findViewById(R.id.tv_show_content);
        vh.start = convertView.findViewById(R.id.iv_start);
        vh.bjcontent = convertView.findViewById(R.id.bj_content);
        if (filePath.getFilePath().endsWith("mp4")) {
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+filePath.getFilePath();
            Glide
                    .with(getContext())
                    .load(Uri.fromFile(new File(path)))
                    .into(vh.showpic);
//            vh.showpic.setImageBitmap(getVideoThumbnail(path));
            vh.start.setVisibility(View.VISIBLE);
            vh.context.setText(filePath.getCheckDes());
            vh.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PlayMediaActivity.class);
                    intent.putExtra("path", path);
                    getContext().startActivity(intent);
                }
            });
        } else if (filePath.getFilePath().endsWith("jpg")) {
            final String imagePath = "file:///"+Environment.getExternalStorageDirectory().getAbsolutePath()+filePath.getFilePath();
            vh.showpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(Uri.parse(imagePath));
                }
            });
            Glide.with(getContext()).load(imagePath).into(vh.showpic);
            vh.context.setText(filePath.getCheckDes());
        }
        convertView.setTag(vh);
        vh.bjcontent.setTag(position);
        vh.bjcontent.setOnClickListener(this);
        return convertView;
    }
    private Dialog dia;

    public void showDialog(Uri path) {
        dia = new Dialog(getContext(), R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_dialog);
        final ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
//        imageView.setImageURI(path);
        Glide.with(mContext)
                .load(path)
                .into(imageView);
        dia.show();
        Window window = dia.getWindow();
        dia.setCanceledOnTouchOutside(true);//点击可以退出
        android.view.WindowManager.LayoutParams params = dia.getWindow().getAttributes();
        Display display = mContext.getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth());
        params.height = (int) (display.getHeight()*2/3);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
//        GestureImageView gs= new GestureImageView(this);
//        gs.GestureImageViewInit();
        dia.setCanceledOnTouchOutside(true);//设置区域外点击消失
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });


    }
    /**
     * 刷新单个item
     *
     * @param listView
     * @param position
     */
    public void refreshListView(ListView listView, int position) {
        int start = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        for (int i = start, j = last; i <= j; i++) {
            if (position == i) {
                View convertView = listView.getChildAt(position - start);
                if (convertView != null) {
                    getView(position, convertView, listView);
                    break;
                }
            }
        }
    }
    // 获取视频缩略图
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath, new HashMap<String, String>());
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    class ViewHolder {
        ImageView showpic;
        TextView context;
        ImageView start;
        ImageButton bjcontent;
    }
    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
            mListener.itemClick(v);

    }
}
