package objects;

import enums.TaskStatus;
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
        return "\n Task{" +
                "id=" + this.taskId +
                ", name=" + this.name + " " +
                ", description=" + this.description + " " +
                ", status=" + this.status +
                '}';
    }

    @Override
    public Object clone()
    {
        try {
            Task task = (Task) super.clone();
            return task;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
