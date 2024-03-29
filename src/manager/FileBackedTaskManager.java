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
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Map<Integer, Task> mapOfTasksInFile = getMapOfTasks();
    private final Map<Integer, Subtask> mapOfSubtasksInFile = getMapOfSubtasks();
    private final Map<Integer, Epic> mapOfEpicsInFile = getMapOfEpics();
    private final HistoryManager historyManager = getHistoryManager();

    private File fileName = null;

    public FileBackedTaskManager(File fileName) {
        this.fileName = fileName;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager;
        try {
            fileBackedTaskManager = new FileBackedTaskManager(file);
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
                        fileBackedTaskManager.createTask(taskToAdd);
                        break;
                    }
                    case SUBTASK: {
                        int epicId = ((Subtask) taskToAdd).getEpicId();
                        Epic epicForSubtask = fileBackedTaskManager.mapOfEpicsInFile.get(epicId);
                        fileBackedTaskManager.createSubTask(((Subtask) taskToAdd), epicForSubtask);
                        break;
                    }
                    case EPIC: {
                        fileBackedTaskManager.createEpic((Epic) taskToAdd);
                        break;
                    }
                    default: {
                        throw new ManagerSaveException("Ошибка в методе loadFromFile" + ". Итерация =" + i + " " +
                                taskToAdd);
                    }
                }
            }

            // Восстанавливаем историю из оставшейся части списка строк
            if (indexOfHistory != 0 && tasksAndHistory.length != indexOfHistory) {
                String[] history = Arrays.copyOfRange(tasksAndHistory, indexOfHistory + 1,
                        tasksAndHistory.length);
                List<Task> historyFromCSV = CSVTaskFormatter.historyFromString(history);
                historyFromCSV.forEach(fileBackedTaskManager.historyManager::add);
            }
            fileBackedTaskManager.setTaskId();
            return fileBackedTaskManager;
        } catch (IOException | NullPointerException e) {
            throw new ManagerSaveException("Ошибка в  методе loadFromFile" + e.getMessage());
        }
    }

    @Override
    public int createTask(Task task) {
        int newTaskId = super.createTask(task);
        save();
        return newTaskId;
    }

    @Override
    public int createSubTask(Subtask subtask, Epic epic) {
        int newTaskId = super.createSubTask(subtask, epic);
        save();
        return newTaskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int newTaskId = super.createEpic(epic);
        save();
        return newTaskId;
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

    protected void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(fileName)) {
            String allInfoInCSVFormat = CSVTaskFormatter.tasksAndHistoryToString(mapOfTasksInFile, mapOfSubtasksInFile, mapOfEpicsInFile,
                    historyManager);
            fileWriter.write(allInfoInCSVFormat);
        } catch (IOException ioException) {
            throw new ManagerSaveException("Ошибка в методе save");
        }
    }
}
