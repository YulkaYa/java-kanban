package manager;

import enums.TaskStatus;
import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CSVTaskFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    File tempFile;
    FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    private void generateTasksForTest() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        try (Writer fileWriter = new FileWriter(tempFile)) {
            String allInfoInCSVFormat =
                    "id,type,name,status,description,epic,subtasks\n" +
                            "12,TASK,таск1,NEW,таск1 дескрипшн,\n" +
                            "13,TASK,таск2,NEW,таск2 дескрипшн,\n" +
                            "14,TASK,таск3,NEW,таск3 дескрипшн,\n" +
                            "15,TASK,таск4,NEW,таск4 дескрипшн,\n" +
                            "1,EPIC,эпик1,NEW,эпик1 дескрипшн,[5, 6, 7, 8]\n" +
                            "2,EPIC,эпик2,NEW,эпик2 дескрипшн,[9, 10, 11]\n" +
                            "3,EPIC,эпик3,NEW,эпик3 дескрипшн,[]\n" +
                            "4,EPIC,эпик4,IN_PROGRESS,эпик4 дескрипшн,[]\n" +
                            "5,SUBTASK,сабтаск1,NEW,сабтаск1 для экпика1,1\n" +
                            "6,SUBTASK,сабтаск2,IN_PROGRESS,сабтаск2 для экпика1,1\n" +
                            "7,SUBTASK,сабтаск3,NEW,сабтаск3 для экпика1,1\n" +
                            "8,SUBTASK,сабтаск4,NEW,сабтаск4 для экпика1,1\n" +
                            "9,SUBTASK,сабтаск5,NEW,сабтаск5 для экпика2,2\n" +
                            "10,SUBTASK,сабтаск6,NEW,сабтаск6 для экпика2,2\n" +
                            "11,SUBTASK,сабтаск7,NEW,сабтаск7 для экпика2,2\n" +
                            "\n" +
                            "12,TASK,таск1,NEW,таск1 дескрипшн,\n" +
                            "13,TASK,таск2,NEW,таск2 дескрипшн,\n" +
                            "14,TASK,таск3,NEW,таск3 дескрипшн,\n" +
                            "15,TASK,таск4,NEW,таск4 дескрипшн,";
            fileWriter.write(allInfoInCSVFormat);
        }
    }


    @Test
    void loadFromFileIfFileIsCorrect() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        HashMap<Integer, Task> tasks = fileBackedTaskManager.getMapOfTasks();
        HashMap<Integer, Epic> epics = fileBackedTaskManager.getMapOfEpics();
        HashMap<Integer, Subtask> subtasks = fileBackedTaskManager.getMapOfSubtasks();
        List<Task> history = fileBackedTaskManager.getHistory();
        // Проверим, что размеры мап и истории совпадают со списком из файла
        assertEquals(4, tasks.size());
        assertEquals(4, epics.size());
        assertEquals(7, subtasks.size());
        assertEquals(4, history.size());
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
        assertEquals(16, fileBackedTaskManager.getTaskId());
    }

    @Test
    void createTasksAndCheckFile() throws IOException {
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

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        Subtask subtask3 = new Subtask("сабтаск3", "сабтаск3 для экпика1");
        Subtask subtask4 = new Subtask("сабтаск4", "сабтаск4 для экпика1");
        Subtask subtask5 = new Subtask("сабтаск5", "сабтаск5 для экпика2");
        Subtask subtask6 = new Subtask("сабтаск6", "сабтаск6 для экпика2");
        Subtask subtask7 = new Subtask("сабтаск7", "сабтаск7 для экпика2");
        Subtask subtask8 = new Subtask("сабтаск8", "сабтаск8 для экпика2");
        int subtask1id = fileBackedTaskManager.createSubTask(subtask1, epic1);
        int subtask2id = fileBackedTaskManager.createSubTask(subtask2, epic1);
        int subtask3id = fileBackedTaskManager.createSubTask(subtask3, epic1);
        int subtask4id = fileBackedTaskManager.createSubTask(subtask4, epic1);
        int subtask5id = fileBackedTaskManager.createSubTask(subtask5, epic2);
        int subtask6id = fileBackedTaskManager.createSubTask(subtask6, epic2);
        int subtask7id = fileBackedTaskManager.createSubTask(subtask7, epic2);

        Task task1 = new Task("таск1", "таск1 дескрипшн");
        Task task2 = new Task("таск2", "таск2 дескрипшн");
        Task task3 = new Task("таск3", "таск3 дескрипшн");
        Task task4 = new Task("таск4", "таск4 дескрипшн");
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

    @Test
    void checkThatAfterLoadCanWorkWithFileManagerAndCanManageAnotherTasks() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic("эпик", "эпик дескрипшн");
        int epicId = fileBackedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("сабтаск", "сабтаск для экпика");
        int subtaskid = fileBackedTaskManager.createSubTask(subtask, epic);
        Task task = new Task("таск", "таск дескрипшн");
        int taskid = fileBackedTaskManager.createTask(task);

        fileBackedTaskManager.getTaskById(epicId);
        fileBackedTaskManager.getTaskById(subtaskid);
        fileBackedTaskManager.getTaskById(taskid);
        HashMap<Integer, Task> tasks = fileBackedTaskManager.getMapOfTasks();
        HashMap<Integer, Epic> epics = fileBackedTaskManager.getMapOfEpics();
        HashMap<Integer, Subtask> subtasks = fileBackedTaskManager.getMapOfSubtasks();
        List<Task> history = fileBackedTaskManager.getHistory();
        // Проверим, что размеры мап и истории совпадают со списком из файла
        assertEquals(5, tasks.size());
        assertEquals(5, epics.size());
        assertEquals(8, subtasks.size());
        assertEquals(7, history.size());
        // Проверим поля 1 задачи из каждой мапы
        assertEquals("таск", tasks.get(taskid).getName());
        assertEquals("таск дескрипшн", tasks.get(taskid).getDescription());
        assertEquals(TaskStatus.NEW, tasks.get(taskid).getStatus());

        assertEquals("сабтаск", subtasks.get(subtaskid).getName());
        assertEquals("сабтаск для экпика", subtasks.get(subtaskid).getDescription());
        assertEquals(TaskStatus.NEW, subtasks.get(subtaskid).getStatus());
        assertEquals(epicId, subtasks.get(subtaskid).getEpicId());

        assertEquals("эпик", epics.get(epicId).getName());
        assertEquals("эпик дескрипшн", epics.get(epicId).getDescription());
        assertEquals(TaskStatus.NEW, epics.get(epicId).getStatus());
        assertEquals(19, fileBackedTaskManager.getTaskId());

        assertEquals(taskid, (history.get(history.size()-1).getTaskId()));
    }
}