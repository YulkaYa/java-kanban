package enums;


public enum TaskTypes {
    TASK("Task"), EPIC("Epic"), SUBTASK("Subtask");

    private String type;

    TaskTypes(String type) {
        this.type = type;
    }

    public String getName() {
        return name();
    }

    public String getType() {
        return type;
    }
}
