package manager;

import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import enums.TaskStatus;
import service.Managers;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> mapOfEpics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskId = 0;

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
        с типом Task*/
    @Override
    public int createTask(Task task) {
        boolean isTask = task.getClass().getSimpleName().equals("Task");
        boolean isTaskInMap = mapOfTasks.containsValue(task);
        // Проверяем, что сохраняем объект Task, а не наследника класса, а также, что такой таски нет в мапе
        if (isTask && !isTaskInMap) {
            taskId++; // вычисляем id для новой таски
            task.setTaskId(taskId); // присваиваем id таске
            task.setStatus(TaskStatus.NEW);
            mapOfTasks.put(taskId, task); // сохраняем таску в список всех таск
            return taskId;
        } else return 0;
    }

    /*  d. Создание. Сам объект должен передаваться в качестве параметра.
        Создание задачи с типом subtask*/
    @Override
    public int createSubTask(Subtask subtask, Epic epic) {
        boolean isSubTaskInMap = mapOfSubtasks.containsValue(subtask);
        boolean isEpicInMap = mapOfEpics.containsValue(epic);
        // Проверяем, что эпик есть в мапе эпиков, а сабтаски еще нет в мапе сабтаск, иначе не добавляем сабтаску в мапу
        if (isEpicInMap && !isSubTaskInMap) {
            taskId++; // вычисляем id для новой сабтаски
            subtask.setTaskId(taskId); // присваиваем id сабтаски
            subtask.setStatus(TaskStatus.NEW);
            subtask.setEpicId(epic.getTaskId()); // сохраняем в объект сабтаски id связанного эпика
            epic.getSubtasks().add(taskId); // сохраняем сабтаску в epic
            mapOfSubtasks.put(taskId, subtask); // сохраняем сабтаску в список всех сабтаск
            updateEpicStatus(epic.getTaskId()); // Обновляем статус эпика
            return taskId;
        } else return 0;
    }

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
    Создание задачи с типом Epic*/
    @Override
    public int createEpic(Epic epic) { //
        boolean isEpicInMap = mapOfEpics.containsValue(epic);
        ArrayList<Integer> subtasksInEpic = epic.getSubtasks();
        // Если эпик не содержит сабтаск и эпика еще нет в мапе эпиков, то сохраняем его, иначе не сохраняем эпик, так
        // как обновление объектов делаем через update
        if (subtasksInEpic.isEmpty() && !isEpicInMap) {
            taskId++; // вычисляем id для нового эпика
            epic.setTaskId(taskId); // присваиваем id эпику
            epic.setStatus(TaskStatus.NEW);
            mapOfEpics.put(taskId, epic); // сохраняем эпик в список всех эпиков
            return taskId;
        } else {
            return 0;
        }
    }

    /*a. Получение списка всех задач
    (Task)*/
    @Override
    public ArrayList<Task> getAllTasks() {
        if (mapOfTasks == null || mapOfTasks.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Task> tasks = mapOfTasks.values()
                    .stream()
                    .collect(Collectors.toCollection(ArrayList::new));
            tasks.forEach(task -> {
                try {
                    historyManager.add(task);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            });
            return tasks;
        }
    }

    /*a. Получение списка всех задач
    (Epic)*/
    @Override
    public ArrayList<Epic> getAllEpics() {
        if (mapOfEpics == null || mapOfEpics.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Epic> epics = mapOfEpics.values()
                    .stream()
                    .collect(Collectors.toCollection(ArrayList::new));
            epics.forEach(epic -> {
                try {
                    historyManager.add(epic);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            });
            return epics;
        }
    }

    /*a. Получение списка всех задач
    (SubTask)*/
    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        if (mapOfSubtasks == null || mapOfSubtasks.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Subtask> subTasks = mapOfSubtasks.values()
                    .stream()
                    .collect(Collectors.toCollection(ArrayList::new));
            subTasks.forEach(subTask -> {
                try {
                    historyManager.add(subTask);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            });
            return subTasks;
        }
    }

    /*b. Удаление всех задач
    (Task)*/
    @Override
    public void removeAllTasks() {
        mapOfTasks.clear();
    }

    /*b. Удаление всех задач
    (Epic)*/
    @Override
    public void removeAllEpics() {
        mapOfEpics.clear(); // очистили список эпиков
        mapOfSubtasks.clear(); // очистили список сабтасок
    }

    /*b. Удаление всех задач
    (SubTask)*/
    @Override
    public void removeAllSubTasks() {
        mapOfSubtasks.clear(); //
        for (Epic epic : getAllEpics()) { // проходим по списку эпиков и очищаем списки сабтасок в них
            epic.getSubtasks().clear();
            updateEpicStatus(epic.getTaskId());
        }
    }

    /*    Дополнительные методы:
    a. Получение списка всех подзадач определённого эпика.*/
    @Override
    public ArrayList<Subtask> getSubTasksByEpic(int epicId) throws CloneNotSupportedException {  //
        // Проверяем, что такой эпик есть в мапе эпиков
        if (mapOfEpics.containsKey(epicId)) {
            // получили все id сабтаск по эпику
            ArrayList<Integer> subtasksIdByEpic = mapOfEpics.get(epicId).getSubtasks();
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (int idOfSubtask : subtasksIdByEpic) {
                Subtask subtask = mapOfSubtasks.get(idOfSubtask);
                subtasks.add(subtask); // сохраняем в новый массив объекты сабтаск из эпика
                historyManager.add(subtask);
            }

            return subtasks; // возвращаем список сабтаск из эпика в виде массива объектов Subtask
        } else return new ArrayList<>();
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        if (task != null) {
            String className = task.getClass().getSimpleName();
            int thisTaskId = task.getTaskId();
            switch (className) { // Определяем тип задачи по классу
                case "Task":
                    // Если таска есть в мапе таск, то обновляем объект
                    if (mapOfTasks.containsKey(thisTaskId)) {
                        mapOfTasks.put(task.getTaskId(), task);
                    }
                    break;

                case "Subtask":
                    // Если сабтаска есть в мапе сабтаск, то обновляем объект
                    if (mapOfSubtasks.containsKey(thisTaskId)) {
                        Subtask newSubtask = (Subtask) task;
                        int subTaskId = newSubtask.getTaskId();
                        // получаем эпик, с которым была связана сабтаска
                        Epic oldEpic = getEpicBySubtaskId(subTaskId);
                        // получаем эпик, с которым должна быть связана сабтаска
                        Epic newEpic = mapOfEpics.get(newSubtask.getEpicId());

                        // Если сабтаска есть в мапе сабтаск и эпики(старый, новый) существуют, то обновляем сабтаск
                        // и эпики
                        if (newEpic != null && oldEpic != null) {
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

                case "Epic":
                    // Если эпик есть в мапе эпиков, то обновляем объект
                    if (mapOfEpics.containsKey(thisTaskId)) {
                        // Получаем объекты нового и старого эпиков
                        Epic newEpic = (Epic) task;
                        Epic oldEpic = mapOfEpics.get(newEpic.getTaskId());
                        // Получаем списки сабтасок нового и старого эпиков
                        ArrayList<Integer> subtasksNewEpic = newEpic.getSubtasks();
                        ArrayList<Integer> subtasksOldEpic = oldEpic.getSubtasks();

                        // Если список сабтасок  в новом эпике != старому списку, то заменяем список в новом на старый,
                        // так как обновление списка сабтаск производится через обновление самих сабтаск
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


    // Метод для поиска эпиков по сабтаске(id). Без записи в историю просмотра задач
    @Override
    public Epic getEpicBySubtaskId(int subTaskId) {
        Subtask subtask = mapOfSubtasks.get(subTaskId);
        if (mapOfSubtasks.containsKey(subTaskId)) {
            int epicId = subtask.getEpicId();
            return mapOfEpics.get(epicId);
        } else return null;
    }

    // метод для расчета статуса эпика
    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = mapOfEpics.get(epicId);

        // Обновляем эпик, если он существует в мапе эпиков
        if (epic != null) {
            // Получаем список статусов сабтасок эпика
            ArrayList<TaskStatus> statuses = mapOfSubtasks.values()
                    .stream()
                    .collect(Collectors.toCollection(ArrayList::new))

                    .stream()
                    .filter(s -> s.getEpicId() == epicId)
                    .collect(Collectors.toCollection(ArrayList::new))

                    .stream()
                    .map(s -> s.getStatus())
                    .collect(Collectors.toCollection(ArrayList::new));


            if (statuses.isEmpty()) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                if (statuses.contains(TaskStatus.IN_PROGRESS)) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                } else if (statuses.contains(TaskStatus.NEW) && statuses.contains(TaskStatus.DONE)) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                } else if (statuses.contains(TaskStatus.NEW)) {
                    epic.setStatus(TaskStatus.NEW);
                } else epic.setStatus(TaskStatus.DONE);
            }
        }
    }

    //f. Удаление по идентификатору.
    @Override
    public void removeById(int id) {
        HashMap<Integer, ? extends Task> mapOfAnyTasks = whichMapContainsTask(id);
        if (mapOfAnyTasks != null) {
            Task task = mapOfAnyTasks.get(id);
            String className = task.getClass().getSimpleName();

            switch (className) { // Определяем тип задачи по классу
                case "Task":
                    mapOfTasks.remove(id);
                    break;
                case "Subtask":
                    Epic epic = getEpicBySubtaskId(id); //получаем связанный с сабтаской эпик
                    ArrayList<Integer> idOfSubtasks = epic.getSubtasks(); // получаем список id сабтаск эпика

                    // получаем индекс сабтаски в списке сабтаск в эпике
                    int indexOfIdSubtask = idOfSubtasks.indexOf(id);
                    idOfSubtasks.remove(indexOfIdSubtask); // удаляем id сабтаски из эпика

                    mapOfSubtasks.remove(id); // удаляем сабтаску из списка сабтасок
                    updateEpicStatus(epic.getTaskId()); // обновляем статус эпика
                    break;

                case "Epic":
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

    // c. Получение по идентификатору (любой тип задачи).
    @Override
    public Task getTaskById(int id) throws CloneNotSupportedException {
        HashMap<Integer, ? extends Task> map = whichMapContainsTask(id);
        if (map != null) {
            Task task = map.get(id);
            historyManager.add(task);
            return task;
        } else return null;
    }


    // Метод для поиска мапы, к которой относится задача
    public HashMap<Integer, ? extends Task> whichMapContainsTask(int id) {
        if (mapOfEpics.containsKey(id)) {
            return mapOfEpics;
        } else if (mapOfTasks.containsKey(id)) {
            return mapOfTasks;
        } else if (mapOfSubtasks.containsKey(id)) {
            return mapOfSubtasks;
        } else
            return null;
    }

    @Override
    public void printAllEpics() {
        System.out.println(getAllEpics());
    }

    @Override
    public void printAllSubTasks() {
        System.out.println(getAllSubTasks());
    }

    @Override
    public void printAllTasks() {
        System.out.println(getAllTasks());
    }

    // Метод для получения истории просмотров задач
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
