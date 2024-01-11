package objects;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasks;

    public Epic(String name, String description) {
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
                "id=" + this.getTaskId() +
                ", name=" + this.getName() + " " +
                ", description=" + this.getDescription() + " " +
                ", status=" + this.getStatus() +
                ", сабтаски=" + this.getSubtasks() +
                '}';
    }
}
