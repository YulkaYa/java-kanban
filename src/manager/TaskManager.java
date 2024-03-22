package manager;

import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
            с типом Task*/
    int createTask(Task task);

    /*  d. Создание. Сам объект должен передаваться в качестве параметра.
            Создание задачи с типом subtask*/
    int createSubTask(Subtask subtask, Epic epic);

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
        Создание задачи с типом Epic*/
    int createEpic(Epic epic);

    /*a. Получение списка всех задач
        (Task)*/
    List<Task> getAllTasks();

    /*a. Получение списка всех задач
        (Epic)*/
    List<Epic> getAllEpics();

    /*a. Получение списка всех задач
        (SubTask)*/
    List<Subtask> getAllSubTasks();

    /*b. Удаление всех задач
        (Task)*/
    void removeAllTasks();

    /*b. Удаление всех задач
        (Epic)*/
    void removeAllEpics();

    /*b. Удаление всех задач
        (SubTask)*/
    void removeAllSubTasks();

    /*    Дополнительные методы:
        a. Получение списка всех подзадач определённого эпика.*/
    List<Subtask> getSubTasksByEpic(int epicId);

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    // Метод для поиска эпиков по сабтаске(id)
    Epic getEpicBySubtaskId(int subTaskId);

    // метод для расчет статуса эпика
    void updateEpicStatus(int epicId);

    //f. Удаление по идентификатору.
    void removeById(int id);

    // c. Получение по идентификатору (любой тип задачи).
    Task getTaskById(int id);

    void printAllEpics();

    void printAllSubTasks();

    void printAllTasks();

    // Метод для получения истории просмотров задач
    List<Task> getHistory();

    // Метод для полкчения списка задач, приоритезированных по времени выполнения
    Set<Task> getPrioritizedTasks();
}
