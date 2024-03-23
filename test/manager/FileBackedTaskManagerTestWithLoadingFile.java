package manager;

import enums.TaskStatus;
import exceptions.ManagerSaveException;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTestWithLoadingFile {
    private LocalDateTime start;
    private long durationForNextTask;
    private long duration;
    private File tempFile;

    @BeforeEach
    protected void generateTasksForTest() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        start = LocalDateTime.parse("23:12:13 23.12.2009", Managers.formatter);
        durationForNextTask = 20;
        duration = 5;
        LocalDateTime t1start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t1end = t1start.plusMinutes(duration);
        LocalDateTime t2start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t2end = t1start.plusMinutes(duration);
        LocalDateTime t3start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t3end = t1start.plusMinutes(duration);
        LocalDateTime t4start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t4end = t1start.plusMinutes(duration);

        LocalDateTime s1Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s1End = s1Start.plusMinutes(duration);
        LocalDateTime s2Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s2End = s2Start.plusMinutes(duration);
        LocalDateTime s3Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s3End = s3Start.plusMinutes(duration);
        LocalDateTime s4Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s4End = s4Start.plusMinutes(duration);
        LocalDateTime s5Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s5End = s5Start.plusMinutes(duration);
        LocalDateTime s6Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s6End = s6Start.plusMinutes(duration);
        LocalDateTime s7Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s7End = s7Start.plusMinutes(duration);

        try (Writer fileWriter = new FileWriter(tempFile)) {
            String allInfoInCSVFormat =
                    "id, type, name, status, description, epic, subtasks, starttime, duration, endtime\n" +
                            "12, TASK, таск1, NEW, таск1 дескрипшн, " + t1start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "13, TASK, таск2, NEW, таск2 дескрипшн, " + t2start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "14, TASK, таск3, NEW, таск3 дескрипшн, " + t3start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "15, TASK, таск4, NEW, таск4 дескрипшн, " + t4start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "1, EPIC, эпик1, NEW, эпик1 дескрипшн, [5, 6, 7, 8]\n" +
                            "2, EPIC, эпик2, NEW, эпик2 дескрипшн, [9, 10, 11]\n" +
                            "3, EPIC, эпик3, NEW, эпик3 дескрипшн, []\n" +
                            "4, EPIC, эпик4, IN_PROGRESS, эпик4 дескрипшн, []\n" +
                            "5, SUBTASK, сабтаск1, NEW, сабтаск1 для экпика1, 1, " + s1Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "6, SUBTASK, сабтаск2, IN_PROGRESS, сабтаск2 для экпика1, 1, "
                            + s2Start.format(Managers.formatter) + ", " + duration + "\n" +
                            "7, SUBTASK, сабтаск3, NEW, сабтаск3 для экпика1, 1, " + s3Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "8, SUBTASK, сабтаск4, NEW, сабтаск4 для экпика1, 1, " + s4Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "9, SUBTASK, сабтаск5, NEW, сабтаск5 для экпика2, 2, " + s5Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "10, SUBTASK, сабтаск6, NEW, сабтаск6 для экпика2, 2, " + s6Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "11, SUBTASK, сабтаск7, NEW, сабтаск7 для экпика2, 2, " + s7Start.format(Managers.formatter)
                            + ", " + duration + "\n" +
                            "\n" +
                            "12, TASK, таск1, NEW, таск1 дескрипшн, " + t1start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "13, TASK, таск2, NEW, таск2 дескрипшн, " + t2start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "14, TASK, таск3, NEW, таск3 дескрипшн, " + t3start.format(Managers.formatter) + ", "
                            + duration + "\n" +
                            "15, TASK, таск4, NEW, таск4 дескрипшн, " + t4start.format(Managers.formatter) + ", "
                            + duration + "\n";
            fileWriter.write(allInfoInCSVFormat);
        }
    }

    @Test
    protected void loadFromFileIfFileIsCorrect() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Map<Integer, Task> tasks = fileBackedTaskManager.getMapOfTasks();
        Map<Integer, Epic> epics = fileBackedTaskManager.getMapOfEpics();
        Map<Integer, Subtask> subtasks = fileBackedTaskManager.getMapOfSubtasks();
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
    protected void checkThatAfterLoadCanWorkWithFileManagerAndCanManageAnotherTasks() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Epic epic = new Epic("эпик", "эпик дескрипшн");
        int epicId = fileBackedTaskManager.createEpic(epic);
        LocalDateTime sStart = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime sEnd = sStart.plusMinutes(duration);
        LocalDateTime tStart = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime tEnd = tStart.plusMinutes(duration);

        Subtask subtask = new Subtask("сабтаск", "сабтаск для экпика", sStart, duration);
        int subtaskId = fileBackedTaskManager.createSubTask(subtask, epic);
        Task task = new Task("таск", "таск дескрипшн", tStart, duration);
        int taskId = fileBackedTaskManager.createTask(task);

        fileBackedTaskManager.getTaskById(epicId);
        fileBackedTaskManager.getTaskById(subtaskId);
        fileBackedTaskManager.getTaskById(taskId);
        Map<Integer, Task> tasks = fileBackedTaskManager.getMapOfTasks();
        Map<Integer, Epic> epics = fileBackedTaskManager.getMapOfEpics();
        Map<Integer, Subtask> subtasks = fileBackedTaskManager.getMapOfSubtasks();
        List<Task> history = fileBackedTaskManager.getHistory();
        Set<Task> prioritizedTasks = fileBackedTaskManager.getPrioritizedTasks();
        // Проверим, что размеры мап и истории совпадают со списком из файла
        assertEquals(5, tasks.size());
        assertEquals(5, epics.size());
        assertEquals(8, subtasks.size());
        assertEquals(7, history.size());
        // Проверим поля 1 задачи из каждой мапы
        assertEquals("таск", tasks.get(taskId).getName());
        assertEquals("таск дескрипшн", tasks.get(taskId).getDescription());
        assertEquals(TaskStatus.NEW, tasks.get(taskId).getStatus());

        assertEquals("сабтаск", subtasks.get(subtaskId).getName());
        assertEquals("сабтаск для экпика", subtasks.get(subtaskId).getDescription());
        assertEquals(TaskStatus.NEW, subtasks.get(subtaskId).getStatus());
        assertEquals(epicId, subtasks.get(subtaskId).getEpicId());

        assertEquals("эпик", epics.get(epicId).getName());
        assertEquals("эпик дескрипшн", epics.get(epicId).getDescription());
        assertEquals(TaskStatus.NEW, epics.get(epicId).getStatus());
        assertEquals(19, fileBackedTaskManager.getTaskId());
        assertEquals(taskId, (history.get(history.size() - 1).getTaskId()));
        assertEquals(13, prioritizedTasks.size());
    }

    @Test
    protected void checkThatIfFileDoesntExistIsThrownException() {
        File tempFileThatDoesntExist = null;
        //FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFileThatDoesntExist);
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFileThatDoesntExist);
        }, "Должно быть исключение типа ManagerSaveException");
    }

    @Test
    protected void checkThatIfTypeOfTaskDoesntExistIsThrownException() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        start = LocalDateTime.parse("23:12:13 23.12.2009", Managers.formatter);
        duration = 5;
        try (Writer fileWriter = new FileWriter(tempFile)) {
            String allInfoInCSVFormat =
                    "id, type, name, status, description, epic, subtasks, starttime, duration, endtime\n" +
                            "12,ТИПКОТОРОГОНЕСУЩЕСТВУЕТ,таск1,NEW,таск1 дескрипшн," + start.format(Managers.formatter) +
                            "," + duration + "\n";
            fileWriter.write(allInfoInCSVFormat);
        }

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile);
        }, "Должно быть исключение типа ManagerSaveException");
    }
}