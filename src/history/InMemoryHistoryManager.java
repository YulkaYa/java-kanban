package history;

import objects.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) throws CloneNotSupportedException {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add((Task) task.clone());
    }

    @Override
    public List<Task> getHistory() {
       return history;
    }
}