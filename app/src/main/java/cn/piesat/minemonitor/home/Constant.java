package cn.piesat.minemonitor.home;

import cn.piesat.minemonitor.mapdata.utils.SpHelper;

/**
 * @author Richie on 2017.07.31
 *         常量类
 */
public final class Constant {
    public static final String[] BOOKS = new String[]{"呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市",
            "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟",};
    public static final String[][] FIGURES = new String[][]{
            {"新城区", "回民区", "玉泉区", "赛罕区", "土默特左旗", "托克托县", "和林格尔县", "清水河县", "武川县"},
            {"东河区", "昆都仑区", "青山区", "石拐区", "白云鄂博矿区", "九原区", "土默特右旗", "固阳县", "达尔罕茂明安联合旗"},
            {"海勃湾区", "海南区", "乌达区"},
            {"红山区", "元宝山区", "松山区", "阿鲁科尔沁旗", "巴林左旗", "巴林右旗", "林西县", "克什克腾旗", "翁牛特旗", "喀喇沁旗", "宁城县", "敖汉旗"},
            {"科尔沁区", "科尔沁左翼中旗", "科尔沁左翼后旗", "开鲁县", "库伦旗", "奈曼旗", "扎鲁特旗", "霍林郭勒市"},
            {"东胜区", "达拉特旗", "准格尔旗", "鄂托克前旗", "鄂托克旗", "杭锦旗", "乌审旗", "伊金霍洛旗"},
            {"海拉尔区", "阿荣旗", "莫力达瓦达斡尔族自治旗", "鄂伦春自治旗", "鄂温克族自治旗", "陈巴尔虎旗", "新巴尔虎左旗", "新巴尔虎右旗", "满洲里市", "牙克石市", "扎兰屯市", "额尔古纳市", "根河市"},
            {"临河区", "五原县", "磴口县", "乌拉特前旗", "乌拉特中旗", "乌拉特后旗", "杭锦后旗"},
            {"集宁区", "卓资县", "化德县", "商都县", "兴和县", "凉城县", "察哈尔右翼前旗", "察哈尔右翼中旗", "察哈尔右翼后旗", "四子王旗", "丰镇市"},
            {"乌兰浩特市", "阿尔山市", "科尔沁右翼前旗", "科尔沁右翼中旗", "扎赉特旗", "突泉县"},
            {"二连浩特市", "锡林浩特市", "阿巴嘎旗", "苏尼特左旗", "苏尼特右旗", "东乌珠穆沁旗", "西乌珠穆沁旗", "太仆寺旗", "镶黄旗", "正镶白旗", "正蓝旗", "多伦县"},
            {"阿拉善左旗", "阿拉善右旗", "额济纳旗"},
    };
    public static final String BOOK_NAME = "book_name";
    public static final String FIGURE_NAME = "figure_name";
    public static final String ksd = "矿山开发占地_井口_点";
    public static final String ksm = "矿山开发占地_面";
    public static final String kszhx = "矿山地质灾害_线";
    public static final String kszhm = "矿山地质灾害_面";
    public static final String kszl = "矿山地质环境治理_面";
    public static final String EXPORT = "已导出";
    public static final String IMPLEMENT = "验证中";
    public static final String VERIFIED = "已验证";
    public static final String UNVERIFIED = "未验证";
    public static final String IN_THE_EXECUTION = "执行中";
    public static final String POINT = "点";
    public static final String LINE = "线";
    public static final String SURFACE = "面";
    public static final String FOR = "对";
    public static final String WRONG = "错";
    public static final String HOLE = "漏";
    public static final String YES = "是";
    public static final String NO = "否";
//    public static final String CURRMAP = "本期🌍解译图斑";
//    public static final String LASTMAP = "往期🌎解译图斑";
    public static final String CURRMAP = "2018期解译图斑";
    public static final String LASTMAP = "2017期解译图斑";
    public static final String B06 = "/B06验证相关数据";
    public static final String B06_1 = "/B06验证相关数据/";
    public static final String PIC = "照片";
    public static final String DIV = "视频";
    public static final String SQLITENAME = "nmmvsqlite.db";

    //轨迹相关语句
    public static final String TRACK_EVENT_PIC = "拍照";
    public static final String TRACK_EVENT_HEARTBEAT = "心跳包";
    public static final String TRACK_EVENT_LOGIN = "登录";
    public static final String TRACK_EVENT_QUIT = "退出";
    public  String USERID = SpHelper.getStringValue("USERNAME");
    public static final String GPS = "GPS";


}
