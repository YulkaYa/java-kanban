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
    public String toStringWithoutFieldNames() {
        String epic = super.toStringWithoutFieldNames();
        epic += this.getSubtasks();
        return epic;
    }

    @Override
    public String toString() {
        return "\n " +
                this.getTaskType().getName() +
                "{" +
                "id=" + this.getTaskId() +
                ", name=" + this.getName() + " " +
                ", description=" + this.getDescription() + " " +
                ", status=" + this.getStatus() +
                ", сабтаски=" + this.getSubtasks() +
                '}';
    }

    @Override
    public Object clone() {
        Epic epic = (Epic) super.clone();
        ArrayList<Integer> subtasksToClone = (ArrayList<Integer>) this.getSubtasks().clone();
        epic.setSubtasks(subtasksToClone);
        return epic;
    }
}
