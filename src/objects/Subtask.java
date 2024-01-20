package objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description) {
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
                "id=" + this.getTaskId() +
                ", epicId=" + this.getEpicId() +
                ", name=" + this.getName() + " " +
                ", description=" + this.getDescription() + " " +
                ", status=" + this.getStatus() +
                '}';
    }

    @Override
    public Object clone()
    {
        Subtask subTask = (Subtask) super.clone();
        return subTask;
    }
}
