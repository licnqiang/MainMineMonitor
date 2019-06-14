package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/3/21.
 */

public class PicVidEntity {
    public PicVidEntity(String picVidPath, String content) {
        this.picVidPath = picVidPath;
        this.content = content;
    }

    /**
     * pic,vid路径
     */
    private String picVidPath;
    /**
     * 描述
     */
    private String content;

    public String getPicVidPath() {
        return picVidPath;
    }

    public void setPicVidPath(String picVidPath) {
        this.picVidPath = picVidPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
