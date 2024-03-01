package service;

import manager.*;
import history.*;

import java.io.File;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("src/resources/tasks.csv" ));
    }
}
