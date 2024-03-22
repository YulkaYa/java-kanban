package objects;

import enums.TaskStatus;
import enums.TaskTypes;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int taskId;
    protected Duration duration;
    protected LocalDateTime startTime;
    private String name;
    private String description;
    private TaskStatus status = TaskStatus.NEW;
    protected LocalDateTime endTime;

    public Task(String name, String description, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
        this.endTime = this.startTime.plus(this.getDuration());
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStatusFromString(String statusToSet) {
        this.setStatus(TaskStatus.valueOf(statusToSet));
    }

    // Получаем тип задачи в enum
    public TaskTypes getTaskType() {
        TaskTypes typeOfTask = null;
        String typeOfTaskInString = this.getClass().getSimpleName();
        typeOfTask = TaskTypes.valueOf(typeOfTaskInString.toUpperCase());
        return typeOfTask;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public String toStringWithoutFieldNames() {
        return String.format("%n%s, %s, %s, %s, %s, %s, %s, %s",
                this.getTaskId(),
                this.getTaskType().getName(),
                this.getName(),
                this.getStatus(),
                this.getDescription(),
                this.getStartTime().format(Managers.formatter),
                this.getDuration().toMinutes(),
                this.getEndTime().format(Managers.formatter));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public String toString() {
        return String.format("%n%s: %s, %s, %s, %s, %s, %s, %s, ",
                this.getTaskType().getName(),
                "id=" + this.taskId,
                "name=" + this.getName(),
                "description=" + this.description,
                "status=" + this.getStatus(),
                "StartTime=" + this.getStartTime().format(Managers.formatter),
                "Duration=" + this.getDuration().toMinutes(),
                "EndTime=" + this.getEndTime().format(Managers.formatter));
    }
}
