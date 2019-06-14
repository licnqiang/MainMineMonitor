package cn.piesat.minemonitor.entity;

import java.io.Serializable;

/**
 * Created by yjl on 2018/4/9.
 * (1).	野外验证任务信息表TB_DATA_CHECK_TASK
 */

public class TaskEntity implements Serializable {
    /**
     * (a).	TASK_ID：任务单编号
     */
    private String taskNumber;
    /**
     * (b).	TASK_NAME：任务名称
     */
    private String taskName;
    /**
     * (c).	TASK_CREATE_DATE：任务创建时间
     */
    private String taskCreateDate;
    /**
     * (d).	TASK_XZQ：任务工作覆盖的行政区编号
     */
    private String taskXZQ;
    /**
     * (e).	TASK_EXTEND：任务空间范围的shp文件绝对路径
     */
    private String tastExtend;
    /**
     * (f).	TASK_CONETND：任务内容
     */
    private String taskContend;
    /**
     * (g).	TASK_BEGINE_DATE：任务计划开始时间
     */
    private String taskBegingDate;
    /**
     * ((h). TASK_END_DATE：任务计划结束时间
     */
    private String taskEndDate;

    /**
     * (i).	TASK_STATUS：任务状态
     */
    private String state;
    /**
     * (j).	TASK_NOTE：任务备注信息
     */
    private String taskNote;

    public String getHistoryYear() {
        return historyYear;
    }

    public void setHistoryYear(String historyYear) {
        this.historyYear = historyYear;
    }

    /**
     * (j).	TASK_NOTE：历史年份
     */
    private String historyYear;

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCreateDate() {
        return taskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
    }

    public String getTaskXZQ() {
        return taskXZQ;
    }

    public void setTaskXZQ(String taskXZQ) {
        this.taskXZQ = taskXZQ;
    }

    public String getTastExtend() {
        return tastExtend;
    }

    public void setTastExtend(String tastExtend) {
        this.tastExtend = tastExtend;
    }

    public String getTaskContend() {
        return taskContend;
    }

    public void setTaskContend(String taskContend) {
        this.taskContend = taskContend;
    }

    public String getTaskBegingDate() {
        return taskBegingDate;
    }

    public void setTaskBegingDate(String taskBegingDate) {
        this.taskBegingDate = taskBegingDate;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public TaskEntity() {

    }
}
