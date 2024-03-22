package manager;

import enums.TaskStatus;
import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> mapOfEpics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int taskId = 0;

    protected int getTaskId() {
        return taskId;
    }

    protected int setTaskId() {
        taskId++;
        setTaskId(taskId);
        return taskId;
    }

    protected int setTaskId(int taskIdToAdd) {
        boolean isIdAbsentInAnyMap = false;

        if (taskIdToAdd == 0) { // Если id = 0,то меняем сохраняем новый id из счетчика
            taskIdToAdd = setTaskId();
        }
        while (!isIdAbsentInAnyMap) {
            isIdAbsentInAnyMap = whichMapContainsTask(taskIdToAdd).isEmpty(); // Проверим, есть ли уже такой id в мапах
            if (isIdAbsentInAnyMap) { // Если такого id нет, то не изменяем его
                break;
            } else {
                taskIdToAdd = setTaskId();
            }
        }
        return taskIdToAdd;
    }

    // Методы для получения мап с задачами без изменения истории просмотра
    protected HashMap<Integer, Epic> getMapOfEpics() {
        return mapOfEpics;
    }

    protected HashMap<Integer, Task> getMapOfTasks() {
        return mapOfTasks;
    }

    protected HashMap<Integer, Subtask> getMapOfSubtasks() {
        return mapOfSubtasks;
    }

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
        с типом Task*/
    @Override
    public int createTask(Task task) {
        boolean isTask = task.getClass().getSimpleName().equals("Task");
        boolean isTaskCrossWithAny = checkIfTaskTimeCross(task);
        // Проверяем, что сохраняем объект Task, а не наследника класса, а также, что такой таски нет в мапе
        if (isTask && !isTaskCrossWithAny) {
            int id = setTaskId(task.getTaskId());
            task.setTaskId(id); // присваиваем id таске
            task.setStatus(TaskStatus.NEW);
            mapOfTasks.put(id, task); // сохраняем таску в список всех таск
            prioritizedTasks.add(task);
            return id;
        } else return 0;
    }

    /*  d. Создание. Сам объект должен передаваться в качестве параметра.
        Создание задачи с типом subtask*/
    @Override
    public int createSubTask(Subtask subtask, Epic epic) {
        boolean isEpicInMap = mapOfEpics.containsValue(epic);
        boolean isTaskCrossWithAny = checkIfTaskTimeCross(subtask);
        // Проверяем, что эпик есть в мапе эпиков, а сабтаски еще нет в мапе сабтаск, иначе не добавляем сабтаску в мапу
        if (isEpicInMap && !isTaskCrossWithAny) {
            int id = setTaskId(subtask.getTaskId());
            subtask.setTaskId(id); // присваиваем id сабтаски
            subtask.setEpicId(epic.getTaskId()); // сохраняем в объект сабтаски id связанного эпика
            epic.getSubtasks().add(id); // сохраняем сабтаску в epic
            mapOfSubtasks.put(id, subtask); // сохраняем сабтаску в список всех сабтаск
            prioritizedTasks.add(subtask);
            calculateEpicStartAndEndTime(epic);
            updateEpicStatus(epic.getTaskId()); // Обновляем статус эпика
            return id;
        } else return 0;
    }

    /*    d. Создание. Сам объект должен передаваться в качестве параметра.
    Создание задачи с типом Epic*/
    @Override
    public int createEpic(Epic epic) {
        ArrayList<Integer> subtasksInEpic = epic.getSubtasks();
        // Если эпик не содержит сабтаск и эпика еще нет в мапе эпиков, то сохраняем его, иначе не сохраняем эпик, так
        // как обновление объектов делаем через update
        if (subtasksInEpic.isEmpty()) {
            int id = setTaskId(epic.getTaskId()); // вычисляем id для нового эпика
            epic.setTaskId(id); // присваиваем id эпику
            epic.setStatus(TaskStatus.NEW);
            mapOfEpics.put(id, epic); // сохраняем эпик в список всех эпиков
            calculateEpicStartAndEndTime(epic);
            return id;
        } else {
            return 0;
        }
    }

    /*a. Получение списка всех задач
    (Task)*/
    @Override
    public List<Task> getAllTasks() {
        if (mapOfTasks.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Task> tasks = new ArrayList<>(mapOfTasks.values());
            tasks.forEach(historyManager::add);
            return tasks;
        }
    }

    /*a. Получение списка всех задач
    (Epic)*/
    @Override
    public List<Epic> getAllEpics() {
        if (mapOfEpics.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Epic> epics = new ArrayList<>(mapOfEpics.values());
            epics.forEach(historyManager::add);
            return epics;
        }
    }

    /*a. Получение списка всех задач
    (SubTask)*/
    @Override
    public List<Subtask> getAllSubTasks() {
        if (mapOfSubtasks.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Subtask> subTasks = new ArrayList<>(mapOfSubtasks.values());
            subTasks.forEach(historyManager::add);
            return subTasks;
        }
    }

    /*b. Удаление всех задач
    (Task)*/
    @Override
    public void removeAllTasks() {
        mapOfTasks.keySet().forEach(historyManager::remove);
        mapOfTasks.values().forEach(prioritizedTasks::remove);
        mapOfTasks.clear();
    }

    /*b. Удаление всех задач
    (Epic)*/
    @Override
    public void removeAllEpics() {
        mapOfEpics.keySet().forEach(historyManager::remove);
        mapOfEpics.clear(); // очистили список эпиков
        mapOfSubtasks.keySet().forEach(historyManager::remove);
        mapOfSubtasks.values().forEach(prioritizedTasks::remove);
        mapOfSubtasks.clear(); // очистили список сабтасок
    }

    /*b. Удаление всех задач
    (SubTask)*/
    @Override
    public void removeAllSubTasks() {
        mapOfSubtasks.keySet().forEach(historyManager::remove);
        mapOfSubtasks.values().forEach(prioritizedTasks::remove);
        mapOfSubtasks.clear(); //
        mapOfEpics.values()
                .stream()
                .peek(epic -> epic.getSubtasks().clear())
                .peek(epic -> calculateEpicStartAndEndTime(epic))
                .forEach(epic -> updateEpicStatus(epic.getTaskId()));
    }

    /*    Дополнительные методы:
    a. Получение списка всех подзадач определённого эпика.*/
    @Override
    public List<Subtask> getSubTasksByEpic(int epicId) {  //
        // Проверяем, что такой эпик есть в мапе эпиков
        if (mapOfEpics.containsKey(epicId)) {
            // получили все id сабтаск по эпику
            return mapOfEpics.get(epicId).getSubtasks()
                    .stream()
                    .map(id -> mapOfSubtasks.get(id))
                    .peek(historyManager::add)
                    .collect(Collectors.toList());
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
                    if (mapOfTasks.containsKey(thisTaskId) && !checkIfTaskTimeCross(task)) {
                        mapOfTasks.put(task.getTaskId(), task);
                    }
                    break;

                case "Subtask":
                    // Если сабтаска есть в мапе сабтаск, то обновляем объект
                    if (mapOfSubtasks.containsKey(thisTaskId) && !checkIfTaskTimeCross(task)) {
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
                                prioritizedTasks.add(task);
                            }
                            // заменяем объект сабтаски  в списке сабтаск новым
                            mapOfSubtasks.put(subTaskId, newSubtask);
                            calculateEpicStartAndEndTime(newEpic);
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
                        Epic oldEpic = mapOfEpics.get(thisTaskId);
                        // Получаем списки сабтасок нового и старого эпиков
                        ArrayList<Integer> subtasksNewEpic = newEpic.getSubtasks();
                        ArrayList<Integer> subtasksOldEpic = oldEpic.getSubtasks();

                        // Если список сабтасок  в новом эпике != старому списку, то заменяем список в новом на старый,
                        // так как обновление списка сабтаск производится через обновление самих сабтаск
                        if (!subtasksNewEpic.equals(subtasksOldEpic)) {
                            newEpic.getSubtasks().clear();
                            newEpic.getSubtasks().addAll(subtasksOldEpic);
                        }
                        mapOfEpics.put(thisTaskId, newEpic);
                        calculateEpicStartAndEndTime(newEpic);
                        updateEpicStatus(thisTaskId);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    // Метод для поиска эпиков по сабтаске(id). Без записи в историю просмотра задач
    @Override
    public Epic getEpicBySubtaskId(int subTaskId) {
        if (mapOfSubtasks.containsKey(subTaskId)) {
            // Проверяем, в каком эпике записана данная сабтаска
            return mapOfEpics.values()
                    .stream()
                    .filter(epic -> epic.getSubtasks()
                            .contains(subTaskId)).findFirst().get();

        } else return null;
    }

    // метод для расчета статуса эпика
    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = mapOfEpics.get(epicId);

        // Обновляем эпик, если он существует в мапе эпиков
        if (epic != null) {
            // Получаем список статусов сабтасок эпика
            List<TaskStatus> statuses = new ArrayList<>(mapOfSubtasks.values())
                    .stream().filter(s -> s.getEpicId() == epicId).collect(Collectors.toCollection(
                            ArrayList::new))
                    .stream().map(Task::getStatus).collect(Collectors.toCollection(
                            ArrayList::new));

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
        Map<Integer, ? extends Task> mapOfAnyTasks = whichMapContainsTask(id);
        if (!mapOfAnyTasks.isEmpty()) {
            Task task = mapOfAnyTasks.get(id);
            String className = task.getClass().getSimpleName();

            switch (className) { // Определяем тип задачи по классу
                case "Task":
                    mapOfTasks.remove(id);
                    historyManager.remove(id);
                    prioritizedTasks.remove(task);
                    break;
                case "Subtask":
                    Epic epic = getEpicBySubtaskId(id); //получаем связанный с сабтаской эпик
                    ArrayList<Integer> idOfSubtasks = epic.getSubtasks(); // получаем список id сабтаск эпика

                    // получаем индекс сабтаски в списке сабтаск в эпике
                    int indexOfIdSubtask = idOfSubtasks.indexOf(id);
                    idOfSubtasks.remove(indexOfIdSubtask); // удаляем id сабтаски из эпика

                    mapOfSubtasks.remove(id); // удаляем сабтаску из списка сабтасок
                    historyManager.remove(id); // удаляем сабтаску из истории
                    prioritizedTasks.remove(task);
                    calculateEpicStartAndEndTime(epic);
                    updateEpicStatus(epic.getTaskId()); // обновляем статус эпика
                    break;

                case "Epic":
                    // удаляем из списка сабтасок все сабтаски, связанные с удаляемым эпиком
                    for (int idOfSubtask : ((Epic) task).getSubtasks()) {
                        prioritizedTasks.remove(mapOfSubtasks.get(idOfSubtask));
                        mapOfSubtasks.remove(idOfSubtask);
                        historyManager.remove(idOfSubtask);
                    }
                    // удаляем сам эпик из списка всех эпиков
                    mapOfEpics.remove(id);
                    historyManager.remove(id); // удаляем эпик из истории
                    break;
                default:
                    break;
            }
        }
    }

    // c. Получение по идентификатору (любой тип задачи).
    @Override
    public Task getTaskById(int id) {
        Map<Integer, ? extends Task> map = whichMapContainsTask(id);
        if (!map.isEmpty()) {
            Task task = map.get(id);
            historyManager.add(task);
            return task;
        } else return null;
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

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Метод для поиска мапы, к которой относится задача
    public Map<Integer, ? extends Task> whichMapContainsTask(int id) {
        if (mapOfEpics.containsKey(id)) {
            return mapOfEpics;
        } else if (mapOfTasks.containsKey(id)) {
            return mapOfTasks;
        } else if (mapOfSubtasks.containsKey(id)) {
            return mapOfSubtasks;
        } else return new HashMap<>();
    }

    public void calculateEpicStartAndEndTime(Epic epic) {
        List<Task> sortedSubTasks = prioritizedTasks
                .stream()
                .filter(t -> t.getTaskType().getType().equals("Subtask") &&
                        ((Subtask) t).getEpicId() == epic.getTaskId())
                .collect(Collectors.toList());
        if (!sortedSubTasks.isEmpty()) {
            LocalDateTime startTime = sortedSubTasks.get(0).getStartTime();
            LocalDateTime endTime = sortedSubTasks.get(sortedSubTasks.size() - 1).getEndTime();
            epic.setStartEndTimeAndDuration(startTime, endTime);
        }
    }

    public boolean checkIfTaskTimeCross(Task task) {
        boolean isTimeCross = false;
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        for (Task task1 : prioritizedTasks) {
            LocalDateTime startOfRange = task1.getStartTime();
            LocalDateTime endOfRange = task1.getEndTime();
            LocalDateTime maxOfStart = start.isAfter(startOfRange) ? start : startOfRange;
            LocalDateTime minOfEnd = end.isBefore(endOfRange) ? end : endOfRange;
            long duration = Duration.between(maxOfStart, minOfEnd).toMinutes();
            // Проверяем, если id, время старта, окончания и выполнения совпали, то не считаем это пересеченем
            if (start.isEqual(startOfRange) && end.isEqual(endOfRange) && task.equals(task1)) {
                break;
            }
            if (duration >= 0) {
                isTimeCross = true;
                break;
            }
        }
        return isTimeCross;
    }
}
