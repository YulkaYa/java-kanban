package history;

import objects.Task;

import java.util.List;

public interface HistoryManager {

    // Метод помечает задачи как просмотренные
    void add(Task task) throws CloneNotSupportedException;

    // Метод возвращает список просмотренных задач
    List<Task> getHistory();
}
