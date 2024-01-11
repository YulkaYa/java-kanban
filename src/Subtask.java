import java.util.ArrayList;

public class Subtask extends Task {
    private int epicId;

    Subtask(String name, String description) {
        super(name, description);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "\n Subtask{" +
                "id=" + taskId +
                ", epicId=" + epicId +
                ", name=" + name + " " +
                ", description=" + description + " " +
                ", status=" + status +
                '}';
    }
}
