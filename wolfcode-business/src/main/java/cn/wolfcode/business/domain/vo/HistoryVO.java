package cn.wolfcode.business.domain.vo;

public class HistoryVO {
    private String taskName;
    private String startTime;
    private String endTime;
    private String comment;
    private String durationInMillis;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(String durationInMillis) {
        this.durationInMillis = durationInMillis;
    }
}