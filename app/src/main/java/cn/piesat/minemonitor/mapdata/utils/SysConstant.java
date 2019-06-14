package cn.piesat.minemonitor.mapdata.utils;

import android.graphics.Color;

import pie.core.DimensionMode;
import pie.core.Point2D;
import pie.core.SceneMode;

public interface SysConstant {

	interface MapConfig {
		// 默认地图中心点
		Point2D defaultPoint = new Point2D(76.205, 38.85);
		/** 地图的最小缩放级别 */
		int defaultMinScale = 0;
		//默认地图最大比例尺
		int defaultMaxScale = 0;
		//默认地图比例尺
		int defaultScale = 18;
		//默认的地图名称
		String defaultMapName = "map";
		//默认地的地图模式，二维、三维
		DimensionMode defalutDimensionMode = DimensionMode.D2DMode;
		//默认的地图模式，平面、球面
		SceneMode defalutSceneMode = SceneMode.PlaneMode;
		//默认的地图背景颜色
		int defalutMapBkgColor = Color.WHITE ;
		//默认地图旋转状态
		boolean defalutRotateStatus = false;
		//默认地图是否可俯仰
		boolean defalutPitchStatus = false;
		
		String vectorLayerset = "矢量图层集";
		String baseMapLayerset = "baseMapLayerSet";

		String GEOMETRY_REGION_TAG = "geometry_region_tag";
		String GEOMETRY_LINE_TAG = "geometry_line_tag";
		String GEOMETRY_POINT_TAG = "geometry_point_tag";

		String GPS_RECORD_TAG = "gps_record_tag";

		String MY_LOCATION = "my_location";
	}
	
	interface INTENT_KEY{

	}

	interface PropertyName {
		String JYBH = "解译编号";
		String TBBH= "图斑编号";
		String SLTCJ ="矢量图层集";
		String DTTC ="底图图层";
		String YXTC ="影像图层集";
		String XZQH ="行政区划";
	}

	interface BUNDLE_KEY{
		String KEY_PROJECT_NAME = "key_project_name";
		String KEY_PROJECT = "key_project";
		String KEY_ALL_LAYERSET = "key_all_layerset";
		String KEY_LAYER_JSON = "key_layer_json";
		String KEY_GEOMETRY_ID = "key_geometry_id";
        String KEY_LAYERSET = "key_layerset";
		String KEY_PROJECTPATH = "key_projectpath";
		String KEY_LABELLAYER_JSON="key_labellayer_json";
	}

	interface REQUESTCODE{
		int MAIN_TO_PRO = 0x100;
		int MAIN_TO_LAYERMANAGE = 0x101;
	}
	
	interface SP{
		String themelayervisable="themelayershow";

	}
	public static final String IP = "211.154.196.244";
	// 推送端口
	public static final String UPDATEIP = IP;
	// 更新端口
	public static final String UPDATEPORT = "8899";
	public static final int appid=48;

}
