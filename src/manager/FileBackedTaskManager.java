package manager;

import exceptions.ManagerSaveException;
import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import service.CSVTaskFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final HashMap<Integer, Task> mapOfTasksInFile = getMapOfTasks();
    private final HashMap<Integer, Subtask> mapOfSubtasksInFile = getMapOfSubtasks();
    private final HashMap<Integer, Epic> mapOfEpicsInFile = getMapOfEpics();
    private final HistoryManager historyManager = getHistoryManager();
    private int taskId = getTaskId();
    private File fileName = null;

    public FileBackedTaskManager(File fileName) {
        this.fileName = fileName;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            String fileToString = Files.readString(file.toPath());
            String[] tasksAndHistory = fileToString.split("\n");
            int indexOfHistory = 0; // Поле хранит индекс строки, с которой начинаются строки с историей

            for (int i = 1; i < tasksAndHistory.length; i++) {
                String task = tasksAndHistory[i];
                if (task.length() < 10) { // Проверяем, является строка пустой/разделяющей список задач и историю
                    indexOfHistory = i; // Сохраняем индекс строки
                    break;
                }

                Task taskToAdd = CSVTaskFormatter.taskFromString(task); // Восстанавливаем объект задачи из строки

                // Добавляем задачу в соответствующую мапу
                switch (taskToAdd.getTaskType()) {
                    case TASK: {
                        fileBackedTaskManager.mapOfTasksInFile.put(taskToAdd.getTaskId(), taskToAdd);
                        break;
                    }
                    case SUBTASK: {
                        fileBackedTaskManager.mapOfSubtasksInFile.put(taskToAdd.getTaskId(), (Subtask) taskToAdd);
                        break;
                    }
                    case EPIC: {
                        fileBackedTaskManager.mapOfEpicsInFile.put(taskToAdd.getTaskId(), (Epic) taskToAdd);
                        break;
                    }
                }
            }

            // Восстанавливем историю из оставшейся части списка строк
            if (tasksAndHistory.length - indexOfHistory != 0) {
                String[] history = Arrays.copyOfRange(tasksAndHistory, indexOfHistory + 1,
                        tasksAndHistory.length);
                List<Task> historyFromCSV = CSVTaskFormatter.historyFromString(history);
                historyFromCSV.forEach(fileBackedTaskManager.historyManager::add);
            }
            fileBackedTaskManager.generateTaskId(); // Устанавливаем taskId = максимальному ID из всех мап

            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в  методе loadFromFile");
        }
    }

    @Override
    public int createTask(Task task) {
        taskId = super.createTask(task);
        save();
        return taskId;
    }

    @Override
    public int createSubTask(Subtask subtask, Epic epic) {
        taskId = super.createSubTask(subtask, epic);
        save();
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        taskId = super.createEpic(epic);
        save();
        return taskId;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epics = super.getAllEpics();
        save();
        return epics;
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        List<Subtask> subtasks = super.getAllSubTasks();
        save();
        return subtasks;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public List<Subtask> getSubTasksByEpic(int epicId) {
        List<Subtask> subtasks = super.getSubTasksByEpic(epicId);
        save();
        return subtasks;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void removeById(int id) {
        super.removeById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(fileName)) {
            String allInfoInCSVFormat = CSVTaskFormatter.tasksAndHistoryToString(mapOfTasksInFile, mapOfSubtasksInFile, mapOfEpicsInFile,
                    historyManager);
            fileWriter.write(allInfoInCSVFormat);
        } catch (IOException ioException) {
            throw new ManagerSaveException("Ошибка в методе save");
        }
    }

    // Метод для генерации taskId начиная с уже существующего максимального значения
    private void generateTaskId() {
        int maxId = 0;
        int maxIdInMap = 0;
        if (!mapOfEpicsInFile.isEmpty()) {
            maxIdInMap = Collections.max(mapOfEpicsInFile.keySet());
            if (maxId < maxIdInMap) {
                maxId = maxIdInMap;
            }
        }

        if (!mapOfSubtasksInFile.isEmpty()) {
            maxIdInMap = Collections.max(mapOfSubtasksInFile.keySet());
            if (maxId < maxIdInMap) {
                maxId = maxIdInMap;
            }
        }

        if (!mapOfTasksInFile.isEmpty()) {
            maxIdInMap = Collections.max(mapOfTasksInFile.keySet());
            if (maxId < maxIdInMap) {
                maxId = maxIdInMap;
            }
        }
        taskId = maxId;
        setTaskId(taskId);
    }

}
