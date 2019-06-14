package cn.piesat.minemonitor.entity;

import java.io.Serializable;

/**
 * Created by yjl on 2018/3/7.
 * (3).	验证图斑任务信息表TB_DATA_CHECK_ITEM
 * A.	记录一个图斑的验证等信息
 * B.	与野外验证任务分配表的对应关系为一对多：（TB_DATA_CHECK_JOB:TB_DATA_CHECK_ITEM=1:n）
 * C.	即一条验证分配任务可能对应多个图斑
 */

public class TaskListEntity implements Serializable {

    /**
     * (a).	CHECK_NO：野外验证作业单号
     * 值唯一
     */
    private String checkNo;
    /**
     * (b).	Task_ID：野外验证作业单号
     */
    private String taskNumber;
    /**
     * (c).	JOB_USER_ID：验证作业人员
     */
    private String jobUserId;
    /**
     * (d).	XZQNAME：旗县名称
     */
    private String xzqName;
    /**
     * (e).	XZQCODE：旗县代码
     */
    private String xzqCode;
    /**
     * (f).	CHECK_ADRESS_NAME：验证地点名称
     */
    private String checkAdressName;
    /**
     * (g).	CHECK_ADRESS_COORD_X：验证地点坐标经度
     */
    private String checkAddressCoordX;
    /**
     * (h).	CHECK_ADRESS_COORD_Y：验证地点坐标纬度
     */
    private String checkAddressCoordY;
    /**
     * (i).	CHECK_STATUS：任务的状态
     */
    private String state;
    /**
     * (j).	KS_NAME：矿山名称
     */
    private String mineNumber;
    /**
     * (k).	KS_KCFS：开采方式
     */
    private String ksKCFS;
    /**
     * (l).	KS_KCKZ：开采矿种
     */
    private String mineType;
    /**
     * (m).	KS_DLMC：地类名称
     */
    private String ksDLMC;
    /**
     * (n).	KS_PHLX：破坏类型
     */
    private String ksPHLX;
    /**
     * (o).	KS_YXDX：影响对象
     */
    private String ksYXDX;
    /**
     * (p).	KS_ZLDX：治理对象
     */
    private String ksZLDX;
    /**
     * (q).	KS_TBBH：解译图斑编号
     */
    private String mapNumber;
    /**
     * (r).	KS_FIELDS0：图斑属性
     */
    private String ksFieldso;
    /**
     * (s).	KS_TBCHECK_RES：与解译结果对比
     */
    private String ksTBCheckRES;
    /**
     * (t).	KS_TBMJ：图斑面积
     */
    private String ksTBMJ;
    /**
     * (u).	KS_TBJT：图斑影像截图名称
     */
    private String ksTBJT;
    /**
     * (v).	KS_CENTER_COORD_X：解译图标经纬度坐标经度
     */
    private String ksCenterCoordX;
    /**
     * (w).	KS_CENTER_COORD_Y：解译图标经纬度坐标纬度
     */
    private String ksCenterCoordY;
    /**
     * (x).	RECORED_USER：填表人
     */
    private String RecoredUser;
    /**
     * (y).	CHECK_USERANAME：检查人
     */
    private String CheckUserName;
    /**
     * (z).	    KS_EXTEND_U_L_X：图斑四至坐标左上经度
     */
    private String ksULX;
    /**
     * (aa).    KS_EXTEND_U_L_Y：图斑四至坐标左上纬度
     */
    private String ksULY;
    /**
     * (bb).    KS_EXTEND_U_R_X：图斑四至坐标右上经度
     */
    private String ksURX;
    /**
     * (cc).	KS_EXTEND_U_R_Y：图斑四至坐标右上纬度
     */
    private String ksURY;
    /**
     * (dd).	KS_EXTEND_D_L_X：图斑四至坐标左下经度
     */
    private String ksDLX;
    /**
     * (ee).	KS_EXTEND_D_L_Y：图斑四至坐标坐下纬度
     */
    private String ksDLY;
    /**
     * (ff).	KS_EXTEND_D_R_X：图斑四至坐标右下经度
     */
    private String ksDRX;
    /**
     * (gg).	KS_EXTEND_D_R_Y：图斑四至坐标右下纬度
     */
    private String ksDRY;
    /**
     * (hh).	SHPNAME：图斑所在的SHP图层名称（不带后缀）
     */

    private String shpName;
    /**
     * (ii).	DATATYPE：对应的解译数据类型
     */
    private String dataType;

    private int content;
    /**
     * 2.3.4.36KS_TBCHECK_RESDESC：图斑核查结果描述
     */
    private String des;
    /**
     * 2.3.4.36KS_TBCHECK_RESDESC：图斑核查结果备注
     */
    private String remark;
    /**
     * 2.3.4.36KS_TBCHECK_RESDESC：当新增图斑时变为是
     */
    private String isnew;
    /**
     * 2.3.4.36KS_TBCHECK_RESDESC：图斑旗县编码是否变更
     */
    private String xzqChange;

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getJobUserId() {
        return jobUserId;
    }

    public void setJobUserId(String jobUserId) {
        this.jobUserId = jobUserId;
    }

    public String getXzqName() {
        return xzqName;
    }

    public void setXzqName(String xzqName) {
        this.xzqName = xzqName;
    }

    public String getXzqCode() {
        return xzqCode;
    }

    public void setXzqCode(String xzqCode) {
        this.xzqCode = xzqCode;
    }

    public String getCheckAdressName() {
        return checkAdressName;
    }

    public void setCheckAdressName(String checkAdressName) {
        this.checkAdressName = checkAdressName;
    }

    public String getCheckAddressCoordX() {
        return checkAddressCoordX;
    }

    public void setCheckAddressCoordX(String checkAddressCoordX) {
        this.checkAddressCoordX = checkAddressCoordX;
    }

    public String getCheckAddressCoordY() {
        return checkAddressCoordY;
    }

    public void setCheckAddressCoordY(String checkAddressCoordY) {
        this.checkAddressCoordY = checkAddressCoordY;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMineNumber() {
        return mineNumber;
    }

    public void setMineNumber(String mineNumber) {
        this.mineNumber = mineNumber;
    }

    public String getKsKCFS() {
        return ksKCFS;
    }

    public void setKsKCFS(String ksKCFS) {
        this.ksKCFS = ksKCFS;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public String getKsDLMC() {
        return ksDLMC;
    }

    public void setKsDLMC(String ksDLMC) {
        this.ksDLMC = ksDLMC;
    }

    public String getKsPHLX() {
        return ksPHLX;
    }

    public void setKsPHLX(String ksPHLX) {
        this.ksPHLX = ksPHLX;
    }

    public String getKsYXDX() {
        return ksYXDX;
    }

    public void setKsYXDX(String ksYXDX) {
        this.ksYXDX = ksYXDX;
    }

    public String getKsZLDX() {
        return ksZLDX;
    }

    public void setKsZLDX(String ksZLDX) {
        this.ksZLDX = ksZLDX;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public String getKsFieldso() {
        return ksFieldso;
    }

    public void setKsFieldso(String ksFieldso) {
        this.ksFieldso = ksFieldso;
    }

    public String getKsTBCheckRES() {
        return ksTBCheckRES;
    }

    public void setKsTBCheckRES(String ksTBCheckRES) {
        this.ksTBCheckRES = ksTBCheckRES;
    }

    public String getKsTBMJ() {
        return ksTBMJ;
    }

    public void setKsTBMJ(String ksTBMJ) {
        this.ksTBMJ = ksTBMJ;
    }

    public String getKsTBJT() {
        return ksTBJT;
    }

    public void setKsTBJT(String ksTBJT) {
        this.ksTBJT = ksTBJT;
    }

    public String getKsCenterCoordX() {
        return ksCenterCoordX;
    }

    public void setKsCenterCoordX(String ksCenterCoordX) {
        this.ksCenterCoordX = ksCenterCoordX;
    }

    public String getKsCenterCoordY() {
        return ksCenterCoordY;
    }

    public void setKsCenterCoordY(String ksCenterCoordY) {
        this.ksCenterCoordY = ksCenterCoordY;
    }

    public String getRecoredUser() {
        return RecoredUser;
    }

    public void setRecoredUser(String recoredUser) {
        RecoredUser = recoredUser;
    }

    public String getCheckUserName() {
        return CheckUserName;
    }

    public void setCheckUserName(String checkUserName) {
        CheckUserName = checkUserName;
    }

    public String getKsULX() {
        return ksULX;
    }

    public void setKsULX(String ksULX) {
        this.ksULX = ksULX;
    }

    public String getKsULY() {
        return ksULY;
    }

    public void setKsULY(String ksULY) {
        this.ksULY = ksULY;
    }

    public String getKsURX() {
        return ksURX;
    }

    public void setKsURX(String ksURX) {
        this.ksURX = ksURX;
    }

    public String getKsURY() {
        return ksURY;
    }

    public void setKsURY(String ksURY) {
        this.ksURY = ksURY;
    }

    public String getKsDLX() {
        return ksDLX;
    }

    public void setKsDLX(String ksDLX) {
        this.ksDLX = ksDLX;
    }

    public String getKsDLY() {
        return ksDLY;
    }

    public void setKsDLY(String ksDLY) {
        this.ksDLY = ksDLY;
    }

    public String getKsDRX() {
        return ksDRX;
    }

    public void setKsDRX(String ksDRX) {
        this.ksDRX = ksDRX;
    }

    public String getKsDRY() {
        return ksDRY;
    }

    public void setKsDRY(String ksDRY) {
        this.ksDRY = ksDRY;
    }

    public String getShpName() {
        return shpName;
    }

    public void setShpName(String shpName) {
        this.shpName = shpName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getXzqChange() {
        return xzqChange;
    }

    public void setXzqChange(String xzqChange) {
        this.xzqChange = xzqChange;
    }
}
