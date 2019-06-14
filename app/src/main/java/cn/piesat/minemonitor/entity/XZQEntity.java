package cn.piesat.minemonitor.entity;

import java.io.Serializable;

/**
 * Created by yjl on 2018/4/10.
 * TB_XZQ_CODE行政区实体类
 */

public class XZQEntity implements Serializable {
    /**
     * (a).	PROV_CODE：行政市级编码
     */
    private String xzs;
    /**
     * (b).	PROV_NAME：行政区域名称
     */
    private String xzqName;
    /**
     * (c).	PROV_CODE：行政县级编码
     */
    private String xzx;

    public String getXzs() {
        return xzs;
    }

    public void setXzs(String xzs) {
        this.xzs = xzs;
    }

    public String getXzqName() {
        return xzqName;
    }

    public void setXzqName(String xzqName) {
        this.xzqName = xzqName;
    }

    public String getXzx() {
        return xzx;
    }

    public void setXzx(String xzx) {
        this.xzx = xzx;
    }
}
