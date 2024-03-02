package objects;

import enums.TaskStatus;
import enums.TaskTypes;

import java.util.Objects;

public class Task implements Cloneable {
    private int taskId;
    private String name;
    private String description;
    private TaskStatus status = TaskStatus.NEW;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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

    public void setStatusFromString(String status) {
        for (TaskStatus statusEnum : TaskStatus.values()) {
            if (statusEnum.name().equals(status)) {
                this.setStatus(statusEnum);
            }
        }
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
        return String.format("%n%s,%s,%s,%s,%s,",
                this.getTaskId(),
                this.getTaskType().getName(),
                this.getName(),
                this.getStatus(),
                this.getDescription());
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
        return "\n " +
                this.getTaskType().getName() +
                "{" +
                "id=" + this.taskId +
                ", name=" + this.name + " " +
                ", description=" + this.description + " " +
                ", status=" + this.status +
                '}';
    }
}
