import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasks;

    Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "\n Epic{" +
                "id=" + taskId +
                ", name=" + name + " " +
                ", description=" + description + " " +
                ", status=" + status +
                ", сабтаски=" + getSubtasks() +
                '}' ;
    }
}
