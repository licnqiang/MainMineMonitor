package cn.piesat.minemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.entitys.FileEntity;

/**
 * 作者：wangyi
 * <p>
 * 邮箱：wangyi@piesat.cn
 */
public class MyFileAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FileEntity> mAList;
    private LayoutInflater mInflater;


    public MyFileAdapter(Context mContext, ArrayList<FileEntity> mList) {
        super();
        this.mContext = mContext;
        this.mAList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mAList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(mAList.get(position).getFileType() == FileEntity.Type.FLODER){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        FileEntity entity = mAList.get(position);

        if(convertView == null){
            holder = new ViewHolder();
            switch (type) {
                case 0://folder
                    convertView = mInflater.inflate(R.layout.item_listview, parent, false);
                    holder.iv = (ImageView) convertView.findViewById(R.id.item_imageview);
                    holder.tv = (TextView) convertView.findViewById(R.id.item_textview);
                    break;
                case 1://file
                    convertView = mInflater.inflate(R.layout.item_listview, parent, false);
                    holder.iv = (ImageView) convertView.findViewById(R.id.item_imageview);
                    holder.tv = (TextView) convertView.findViewById(R.id.item_textview);

                    break;

                default:
                    break;

            }
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case 0:
                holder.iv.setImageResource(R.drawable.folder_125);
                holder.tv.setText(entity.getFileName());
                break;
            case 1:
                holder.iv.setImageResource(R.drawable.file);
                holder.tv.setText(entity.getFileName());

                break;

            default:
                break;
        }


        return convertView;
      }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
