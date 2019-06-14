package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/4/10.
 * 2.13地类代码信息表TB_DLDM_INFO
 */

public class DldmInfoEntity {
    /**
     * (a).	OBJECTID：记录ID
     */
    private int dlID;
    /**
     * (a).	DAIMAMINGCHENG：地类代码名称
     */
    private String dlName;
    /**
     * (a).	DAIMALEIXING：地类代码类型
     */
    private String dlType;
    /**
     * (a).	DAIMA：地类代码编号
     */
    private String dlCode;

    public DldmInfoEntity() {
    }

    public int getDlID() {

        return dlID;
    }

    public void setDlID(int dlID) {
        this.dlID = dlID;
    }

    public String getDlName() {
        return dlName;
    }

    public void setDlName(String dlName) {
        this.dlName = dlName;
    }

    public String getDlType() {
        return dlType;
    }

    public void setDlType(String dlType) {
        this.dlType = dlType;
    }

    public String getDlCode() {
        return dlCode;
    }

    public void setDlCode(String dlCode) {
        this.dlCode = dlCode;
    }

    public String getDlSanDaLei() {
        return dlSanDaLei;
    }

    public void setDlSanDaLei(String dlSanDaLei) {
        this.dlSanDaLei = dlSanDaLei;
    }

    /**
     * (a).	SANDALEI：三大类
     */
    private String dlSanDaLei;

}
