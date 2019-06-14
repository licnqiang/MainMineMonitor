package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/6/13.
 * 1.1.野外验证轨迹信息表TB_DATA_CHECK_TRACK  *
 */

public class CheckTrackEntiy {
    /**
     * 野外验证轨迹信息记录ID
     */
    private String  trackNo ;
    /**
     * 任务ID
     */
    private String  taskJobid ;
    /**
     * 图斑编号
     */
    private String  checkNo ;
    /**
     * 终端编号
     */
    private String  devinceNo ;
    /**
     * 终端位置X
     */
    private String  placeX ;
    /**
     * 终端位置Y
     */
    private String  placeY ;
    /**
     * 事件类型(1.心跳包（1小时存一条（如果没有退出的话）），2.登陆（登陆时存一条），3.拍照（拍照时存一条），4.退出（退出时存一条）)
     */
    private String  eventType ;
    /**
     * 记录时间
     */
    private String  eventTime ;
    /**
     * 当前用户ID
     */
    private String  userId ;
    /**
     * 获取位置类型(GPS,基站，北斗)
     */
    private String  locationType ;
    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getTaskJobid() {
        return taskJobid;
    }

    public void setTaskJobid(String taskJobid) {
        this.taskJobid = taskJobid;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getDevinceNo() {
        return devinceNo;
    }

    public void setDevinceNo(String devinceNo) {
        this.devinceNo = devinceNo;
    }

    public String getPlaceX() {
        return placeX;
    }

    public void setPlaceX(String placeX) {
        this.placeX = placeX;
    }

    public String getPlaceY() {
        return placeY;
    }

    public void setPlaceY(String placeY) {
        this.placeY = placeY;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }




}
