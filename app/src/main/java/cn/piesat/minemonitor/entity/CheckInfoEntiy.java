package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/4/10.
 * 2.4野外验证信息表TB_DATA_CHECK_INFO
 */

public class CheckInfoEntiy {
    /**
     * 2.4.3.1CHECK_ITEM_ID：野外验证信息编号
     */
    private String checkItemId;
    /**
     * 2.4.3.2CHECK_NO：野外验证作业编号
     */
    private String checkNO;
    /**
     * 2.4.3.3CHECK_DES：描述信息
     */
    private String checkDes;
    /**
     * 2.4.3.4FILE_NO：文件编号
     * 绿色部分为多媒体文件编号
     * “图斑编号”与“多媒体文件编号”之间用下划线“NP”或“NM”或“NV”分割
     * 图斑编号来源是数据库的TB_DATA_CHECK_ITEM表KS_TBBH字段
     * 多媒体文件编号是两位从01开始的自增序列
     */
    private String fileNo;
    /**
     * 2.4.3.5FILE_NAME：文件名称
     * 文件名称的构建规则是：文件编号+文件的实际数据格式扩展名
     */
    private String fileName;
    /**
     * 2.4.3.6FILE_PATH：文件路径
     */
    private String filePath;
    /**
     * 2.4.3.7FILE_CREATE_TIME：拍照时间
     */
    private String fileCreateTime;
    /**
     * 2.4.3.8FILE_CREATE_LOCATION_X：观察点位置经度值
     */
    private String fileCreateLocationX;
    /**
     * 2.4.3.9FILE_CREATE_LOCATION_Y：观察点位置纬度值
     */
    private String fileCreateLocationY;
    /**
     * 2.4.3.10SENSOR_ DIRECTION：镜头指向
     */
    private String sensorDirection;
    /**
     * 2.4.3.11FILE_TYPE：多媒体类型
     */
    private String fileType;
    /**
     * 2.4.3.12FILE_CREATE_LOCATION_NO：观察点编号
     * 生成规则：G+四位行政区编号+三位序号
     */
    private String fileCreateLocationNo;

    public CheckInfoEntiy() {
    }

    public String getCheckItemId() {

        return checkItemId;
    }

    public void setCheckItemId(String checkItemId) {
        this.checkItemId = checkItemId;
    }

    public String getCheckNO() {
        return checkNO;
    }

    public void setCheckNO(String checkNO) {
        this.checkNO = checkNO;
    }

    public String getCheckDes() {
        return checkDes;
    }

    public void setCheckDes(String checkDes) {
        this.checkDes = checkDes;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public String getFileCreateLocationX() {
        return fileCreateLocationX;
    }

    public void setFileCreateLocationX(String fileCreateLocationX) {
        this.fileCreateLocationX = fileCreateLocationX;
    }

    public String getFileCreateLocationY() {
        return fileCreateLocationY;
    }

    public void setFileCreateLocationY(String fileCreateLocationY) {
        this.fileCreateLocationY = fileCreateLocationY;
    }

    public String getSensorDirection() {
        return sensorDirection;
    }

    public void setSensorDirection(String sensorDirection) {
        this.sensorDirection = sensorDirection;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileCreateLocationNo() {
        return fileCreateLocationNo;
    }

    public void setFileCreateLocationNo(String fileCreateLocationNo) {
        this.fileCreateLocationNo = fileCreateLocationNo;
    }


}
