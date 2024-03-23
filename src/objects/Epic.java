package objects;

import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description) {
        super(name, description, LocalDateTime.parse("00:00:00 01.01.0001", Managers.formatter), 0);
        subtasks = new ArrayList<>();
    }

    public void setStartEndTimeAndDuration(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toStringWithoutFieldNames() {
        return String.format("%n%s, %s, %s, %s, %s, %s, %s, %s, %s",
                this.getTaskId(),
                this.getTaskType().getName(),
                this.getName(),
                this.getStatus(),
                this.getDescription(),
                this.getSubtasks(),
                this.getStartTime().format(Managers.formatter),
                this.getDuration().toMinutes(),
                this.getEndTime().format(Managers.formatter));
    }

    @Override
    public String toString() {
        return String.format("%s, %s, ",
                super.toString(),
                "сабтаски=" + this.getSubtasks());
    }
}
