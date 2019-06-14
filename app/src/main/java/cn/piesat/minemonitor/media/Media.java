package cn.piesat.minemonitor.media;


import org.litepal.crud.DataSupport;

/**
 * Created by yjl on 2018/3/16.
 */

public class Media extends DataSupport{
    private int id;
    /**
     * 图斑编号
     */
    private String mapId;
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 图片视频路径
     */
    private String filePath;
    /**
     * 图片视频描述路径
     */
    private String TextPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTextPath() {
        return TextPath;
    }

    public void setTextPath(String textPath) {
        TextPath = textPath;
    }
}
