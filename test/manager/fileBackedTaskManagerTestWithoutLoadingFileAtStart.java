package manager;

import enums.TaskStatus;
import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class fileBackedTaskManagerTestWithoutLoadingFileAtStart {
    @Test
    // Проверка загрузки и сохранения пустого файла и что все менеджеры проинициализированы корректно
    protected void fileBackedTaskManagerTestCorrectInitializationOfManagersFromEmptyFile() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        HashMap<Integer, Task> tasks = fileBackedTaskManager.getMapOfTasks();
        HashMap<Integer, Subtask> subtasks = fileBackedTaskManager.getMapOfSubtasks();
        HashMap<Integer, Epic> epics = fileBackedTaskManager.getMapOfEpics();
        HistoryManager historyManager = fileBackedTaskManager.getHistoryManager();
        assertEquals(true, tasks.isEmpty());
        assertEquals(true, subtasks.isEmpty());
        assertEquals(true, epics.isEmpty());
        assertEquals(true, historyManager.getHistory().isEmpty());
    }

    // Проверка сохранения пустого менеджера в файл
    @Test
    protected void fileBackedTaskManagerTestWithoutLoadingFileAtStart() {
        File tempFile = null;
        tempFile = new File("src/resources/tempFile.csv");
        try {
            tempFile.delete();
            FileBackedTaskManager fileBackedTaskManagerToSaveEmptyFile = new FileBackedTaskManager(tempFile);
            fileBackedTaskManagerToSaveEmptyFile.save();
            assertEquals(true, tempFile.exists());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    protected void createTasksAndCheckFile() throws IOException {
        File fileToSave = File.createTempFile("test", ".csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(fileToSave);

        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        Epic epic3 = new Epic("эпик3", "эпик3 дескрипшн");
        Epic epic4 = new Epic("эпик4", "эпик4 дескрипшн");
        int epic1Id = fileBackedTaskManager.createEpic(epic1);
        int epic2Id = fileBackedTaskManager.createEpic(epic2);
        int epic3Id = fileBackedTaskManager.createEpic(epic3);
        int epic4Id = fileBackedTaskManager.createEpic(epic4);

        LocalDateTime start = LocalDateTime.parse("23:12:13 23.12.2009", Managers.formatter);
        long durationForNextTask = 20;
        final LocalDateTime startWithDuration = start.plusMinutes(durationForNextTask += 20);
        long duration = 5;
        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        Subtask subtask3 = new Subtask("сабтаск3", "сабтаск3 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask4 = new Subtask("сабтаск4", "сабтаск4 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask5 = new Subtask("сабтаск5", "сабтаск5 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask6 = new Subtask("сабтаск6", "сабтаск6 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask7 = new Subtask("сабтаск7", "сабтаск7 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask8 = new Subtask("сабтаск8", "сабтаск8 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        int subtask1id = fileBackedTaskManager.createSubTask(subtask1, epic1);
        int subtask2id = fileBackedTaskManager.createSubTask(subtask2, epic1);
        int subtask3id = fileBackedTaskManager.createSubTask(subtask3, epic1);
        int subtask4id = fileBackedTaskManager.createSubTask(subtask4, epic1);
        int subtask5id = fileBackedTaskManager.createSubTask(subtask5, epic2);
        int subtask6id = fileBackedTaskManager.createSubTask(subtask6, epic2);
        int subtask7id = fileBackedTaskManager.createSubTask(subtask7, epic2);

        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        Task task2 = new Task("таск2", "таск2 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        Task task3 = new Task("таск3", "таск3 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        Task task4 = new Task("таск4", "таск4 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        int task1id = fileBackedTaskManager.createTask(task1);
        int task2id = fileBackedTaskManager.createTask(task2);
        int task3id = fileBackedTaskManager.createTask(task3);
        int task4id = fileBackedTaskManager.createTask(task4);

        fileBackedTaskManager.getAllTasks();
        fileBackedTaskManager.getAllSubTasks();
        fileBackedTaskManager.getAllEpics();
        FileBackedTaskManager fileBackedTaskManagerToCheckSavedFile = FileBackedTaskManager.loadFromFile(fileToSave);

        HashMap<Integer, Task> tasks = fileBackedTaskManagerToCheckSavedFile.getMapOfTasks();
        HashMap<Integer, Epic> epics = fileBackedTaskManagerToCheckSavedFile.getMapOfEpics();
        HashMap<Integer, Subtask> subtasks = fileBackedTaskManagerToCheckSavedFile.getMapOfSubtasks();
        List<Task> history = fileBackedTaskManagerToCheckSavedFile.getHistory();
        // Проверим, что размеры мап и истории совпадают со списком из файла
        assertEquals(4, tasks.size());
        assertEquals(4, epics.size());
        assertEquals(7, subtasks.size());
        assertEquals(15, history.size());
        // Проверим поля 1 задачи из каждой мапы
        assertEquals("таск1", tasks.get(12).getName());
        assertEquals("таск1 дескрипшн", tasks.get(12).getDescription());
        assertEquals(TaskStatus.NEW, tasks.get(12).getStatus());

        assertEquals("сабтаск1", subtasks.get(5).getName());
        assertEquals("сабтаск1 для экпика1", subtasks.get(5).getDescription());
        assertEquals(TaskStatus.NEW, subtasks.get(5).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(6).getStatus());
        assertEquals(1, subtasks.get(5).getEpicId());

        assertEquals("эпик1", epics.get(1).getName());
        assertEquals("эпик1 дескрипшн", epics.get(1).getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(1).getStatus());
        assertEquals(TaskStatus.NEW, epics.get(3).getStatus());
        assertEquals(1, subtasks.get(5).getEpicId());
        assertEquals(15, fileBackedTaskManager.getTaskId());
    }
}
