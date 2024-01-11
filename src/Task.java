import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status = TaskStatus.NEW;
    protected int taskId;

    Task(String name, String description) {
        this.name = name;
        this.description = description;
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
                "id=" + taskId +
                ", name=" + name + " " +
                ", description=" + description + " " +
                ", status=" + status +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
