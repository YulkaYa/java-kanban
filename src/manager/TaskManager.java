package manager;

import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import enums.TaskStatus;

public class TaskManager {
    private int taskId = 0;

    private final HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> mapOfEpics = new HashMap<>();

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
        с типом Task*/
    public int createTask(Task task) {
        taskId++; // вычисляем id для новой таски
        task.setTaskId(taskId); // присваиваем id таске
        mapOfTasks.put(taskId, task); // сохраняем таску в список всех таск
        return taskId;
    }

    /*  d. Создание. Сам объект должен передаваться в качестве параметра.
        Создание задачи с типом subtask*/
    public int createSubTask(Subtask subtask, Epic epic) {
        taskId++; // вычисляем id для новой сабтаски
        subtask.setTaskId(taskId); // присваиваем id сабтаски
        subtask.setEpicId(epic.getTaskId()); // сохраняем в объект сабтаски id связанного эпика
        epic.getSubtasks().add(taskId); // сохраняем сабтаску в epic
        mapOfSubtasks.put(taskId, subtask); // сохраняем сабтаску в список всех сабтаск
        return taskId;
    }

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
    Создание задачи с типом Epic*/
    public int createEpic(Epic epic) {
        taskId++; // вычисляем id для нового эпика
        epic.setTaskId(taskId); // присваиваем id эпику
        mapOfEpics.put(taskId, epic); // сохраняем эпик в список всех эпиков
        return taskId;
    }

    /*a. Получение списка всех задач
    (Task)*/
    public ArrayList<Task> getAllTasks() {
        if (mapOfTasks == null) {
            return new ArrayList<>();
        }
        return mapOfTasks.values()
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /*a. Получение списка всех задач
    (Epic)*/
    public ArrayList<Epic> getAllEpics() {
        if (mapOfEpics == null) {
            return new ArrayList<>();
        }
        return mapOfEpics.values()
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /*a. Получение списка всех задач
    (SubTask)*/
    public ArrayList<Subtask> getAllSubTasks() {
        if (mapOfSubtasks == null) {
            return new ArrayList<>();
        }
        return mapOfSubtasks.values()
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /*b. Удаление всех задач
    (Task)*/
    public void removeAllTasks() {
        mapOfTasks.clear();
    }

    /*b. Удаление всех задач
    (Epic)*/
    public void removeAllEpics() {
        mapOfEpics.clear(); // очистили список эпиков
        mapOfSubtasks.clear(); // очистили список сабтасок
    }

    /*b. Удаление всех задач
    (SubTask)*/
    public void removeAllSubTasks() {
        mapOfSubtasks.clear(); //
        for (Epic epic : getAllEpics()) { // проходим по списку эпиков и очищаем списки сабтасок в них
            epic.getSubtasks().clear();
            updateEpicStatus(epic.getTaskId());
        }
    }

    /*    Дополнительные методы:
    a. Получение списка всех подзадач определённого эпика.*/
    public ArrayList<Subtask> getSubTasksByEpic(int epicId) {
        ArrayList<Integer> subtasksIdByEpic = mapOfEpics.get(epicId).getSubtasks(); // получили все id сабтаск по эпику
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int idOfSubtask : subtasksIdByEpic) {
            subtasks.add(mapOfSubtasks.get(idOfSubtask)); // сохраняем в новый массив объекты сабтаск из эпика
        }
        return subtasks; // возвращаем список сабтаск из эпика в виде массива объектов Subtask
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        if (task != null) {
            switch (task.getClass().toString()) { // Определяем тип задачи по классу
                case "class Task":
                    if (mapOfTasks.containsValue(task)) {
                        mapOfTasks.put(task.getTaskId(), task);
                    }
                    break;

                case "class Subtask":
                    if (mapOfSubtasks.containsValue(task)) {
                        Subtask newSubtask = (Subtask) task;
                        int subTaskId = newSubtask.getTaskId();

                        // получаем эпик, с которым была связана сабтаска
                        Epic oldEpic = getEpicBySubtaskId(subTaskId);
                        Epic newEpic = (Epic) getTaskById(newSubtask.getEpicId());

                        if (newEpic != null && newEpic.getTaskId() != 0) {
                            // если изменяли epicId, то обновляем сами эпики
                            if (oldEpic != newEpic) {
                                removeById(subTaskId); // удаляем сабтаску по id
                                newEpic.getSubtasks().add(subTaskId); // добавляем сабтаску в новый эпик
                            }
                            // заменяем объект сабтаски  в списке сабтаск новым
                            mapOfSubtasks.put(subTaskId, newSubtask);
                            // обновляем статус эпика
                            updateEpicStatus(newEpic.getTaskId());
                        }
                    }
                    break;

                case "class Epic":
                    if (mapOfEpics.containsValue(task)) {
                        // Получаем объекты нового и старого эпиков
                        Epic newEpic = (Epic) task;
                        Epic oldEpic = (Epic) getTaskById(newEpic.getTaskId());
                        // Получаем списки сабтасок нового и старого эпиков
                        ArrayList<Integer> subtasksNewEpic = newEpic.getSubtasks();
                        ArrayList<Integer> subtasksOldEpic = oldEpic.getSubtasks();

                        // Если список сабтасок  в новом эпике != старому списку, то заменяем список в новом на старый
                        if (!subtasksNewEpic.equals(subtasksOldEpic)) {
                            newEpic.getSubtasks().clear();
                            newEpic.getSubtasks().addAll(subtasksOldEpic);
                        }
                        mapOfEpics.put(task.getTaskId(), newEpic);
                        updateEpicStatus(newEpic.getTaskId());
                    }
                    break;
            }
        }

    }

    // Метод для поиска эпиков по сабтаске(id)
    public Epic getEpicBySubtaskId(int subTaskId) {
        Subtask subtask = (Subtask) getTaskById(subTaskId);
        int epicId = subtask.getEpicId();
        return mapOfEpics.get(epicId);
    }

    // метод для расчет статуса эпика
    public void updateEpicStatus(int epicId) {
        Epic epic = (Epic) getTaskById(epicId);
        ArrayList<Subtask> subTasks = getSubTasksByEpic(epicId);
        ArrayList<TaskStatus> statuses = new ArrayList<>();

        if (subTasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            for (Subtask subTask : subTasks) {
                statuses.add(subTask.getStatus());
            }
            if (statuses.contains(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (statuses.contains(TaskStatus.NEW) && statuses.contains(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (statuses.contains(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
            } else epic.setStatus(TaskStatus.DONE);
        }
    }

    // c. Получение по идентификатору.
    public Task getTaskById(Integer id) {
        if (mapOfEpics.containsKey(id)) {
            return mapOfEpics.get(id);

        } else if (mapOfSubtasks.containsKey(id)) {
            return mapOfSubtasks.get(id);

        } else if (mapOfTasks.containsKey(id)) {
            return mapOfTasks.get(id);

        } else return null;
    }

    //f. Удаление по идентификатору.
    public void removeById(Integer id) {
        Task task = getTaskById(id);
        if (task != null) {

            switch (task.getClass().toString()) { // Определяем тип задачи по классу
                case "class Task":
                    mapOfTasks.remove(id);
                    break;
                case "class Subtask":

                    Epic epic = getEpicBySubtaskId(id); //получаем связанный с сабтаской эпик
                    ArrayList<Integer> idOfSubtasks = epic.getSubtasks(); // получаем список id сабтаск эпика

                    // получаем индекс сабтаски в списке сабтаск в эпике
                    int indexOfIdSubtask = idOfSubtasks.indexOf(id);
                    idOfSubtasks.remove(indexOfIdSubtask); // удаляем id сабтаски из эпика

                    mapOfSubtasks.remove(id); // удаляем сабтаску из списка сабтасок
                    updateEpicStatus(epic.getTaskId()); // обновляем статус эпика
                    break;

                case "class Epic":
                    // удаляем из списка сабтасок все сабтаски, связанные с удаляемым эпиком
                    for (int idOfSubtask : ((Epic) task).getSubtasks()) {
                        mapOfSubtasks.remove(idOfSubtask);
                    }
                    // удаляем сам эпик из списка всех эпиков
                    mapOfEpics.remove(id);
                    break;
            }
        }
    }

    public void printAllEpics() {
        System.out.println(getAllEpics());
    }

    public void printAllSubTasks() {
        System.out.println(getAllSubTasks());
    }

    public void printAllTasks() {
        System.out.println(getAllTasks());
    }
}
