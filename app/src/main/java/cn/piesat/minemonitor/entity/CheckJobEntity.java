package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/4/10.
 * 2.2野外验证任务分配表TB_DATA_CHECK_JOB
 */

public class CheckJobEntity {
    /**
     * 2.2.1.1TASK_JOB_ID：任务分配单号
     */
    private String taskJobID;
    /**
     * 2.2.1.2TASK_ID：任务单编号
     */
    private String taskID;
    /**
     * 2.2.1.3JOB_BEGINE_DATE：任务开始时间
     */
    private String jobBegineDate;
    /**
     * 2.2.1.4JOB_END_DATE：任务结束时间
     */
    private String jobEndDate;
    /**
     * 2.2.1.5JOB_NOTE：任务分配备注
     */
    private String jobNote;
    /**
     * 2.2.1.6USER_ID	：分配作业人员ID
     */
    private String userId;

    public CheckJobEntity() {
    }

    public String getTaskJobID() {
        return taskJobID;
    }

    public void setTaskJobID(String taskJobID) {
        this.taskJobID = taskJobID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getJobBegineDate() {
        return jobBegineDate;
    }

    public void setJobBegineDate(String jobBegineDate) {
        this.jobBegineDate = jobBegineDate;
    }

    public String getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobNote() {
        return jobNote;
    }

    public void setJobNote(String jobNote) {
        this.jobNote = jobNote;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
