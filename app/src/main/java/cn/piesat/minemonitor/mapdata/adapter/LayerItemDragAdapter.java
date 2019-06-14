package cn.piesat.minemonitor.mapdata.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.piesat.minemonitor.R;
import cn.piesat.minemonitor.mapdata.widget.PointShapeView;
import pie.core.DatasetType;
import pie.core.Layer;
import pie.core.Style;

/**
 * 作者：wangyi
 * <p>
 * 邮箱：wangyi@piesat.cn
 */
public class LayerItemDragAdapter extends BaseItemDraggableAdapter<Layer, BaseViewHolder> {
    public LayerItemDragAdapter(List<Layer> data) {
        super(R.layout.layer_item_draggable_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Layer item) {
        {
            View itemView = helper.getConvertView();
            PointShapeView shapeView = (PointShapeView) itemView.findViewById(R.id.shape_view);
            ImageView imgView = (ImageView) itemView.findViewById(R.id.iv_layer_visibility);

            helper.addOnClickListener(R.id.iv_layer_visibility);

            helper.setText(R.id.tv_layer_name,item.getName());
            Style style = item.getStyle();
            int type = item.getDataset().getType();
            switch (DatasetType.valueOf(type)){
                case POINT:
                    shapeView.setmPointShape(PointShapeView.PointShape.ROUND);
                    shapeView.setmSolidColor(style.lineColor);
                    break;
                case LINE:
                    shapeView.setmPointShape(PointShapeView.PointShape.LINE);
                    shapeView.setmSolidColor(style.lineColor);
                    break;
                case REGION:
                    shapeView.setmPointShape(PointShapeView.PointShape.SQUARE);
                    shapeView.setmSolidColor(style.fillForeColor);
                    shapeView.setmStrokeColor(style.lineColor);//设置边框色
                    shapeView.setDrawStroke(true);
                    break;
                default:
                    break;
            }

            if(item.isVisible()){
                imgView.setBackgroundResource(R.drawable.ic_eye_on);
            }else{
                imgView.setBackgroundResource(R.drawable.ic_eye_off);
            }
        }

    }
}
