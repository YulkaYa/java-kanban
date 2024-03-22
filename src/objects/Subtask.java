package objects;

import service.Managers;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, LocalDateTime startTime, long duration) {
        super(name, description, startTime, duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toStringWithoutFieldNames() {
        return String.format("%n%s, %s, %s, %s, %s, %s, %s, %s, %s",
                this.getTaskId(),
                this.getTaskType().getName(),
                this.getName(),
                this.getStatus(),
                this.getDescription(),
                this.getEpicId(),
                this.getStartTime().format(Managers.formatter),
                this.getDuration().toMinutes(),
                this.getEndTime().format(Managers.formatter));
    }

    @Override
    public String toString() {
        return String.format("%s, %s, ",
                super.toString(),
                "epicId=" + this.getEpicId());
    }
}
