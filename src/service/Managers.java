package service;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import manager.FileBackedTaskManager;
import manager.TaskManager;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class Managers {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("src/resources/tasks.csv"));
    }
}
