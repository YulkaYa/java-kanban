package history;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {
    LocalDateTime start = LocalDateTime.parse("20:10:10 23.12.2009", Managers.formatter);
    long durationForNextTask = 20;
    long duration = 5;

    @Test
    protected void checkThatRemovedTasksDontStoredInHistory() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask1, epic1);

        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createTask(task1);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "Неверное количество задач в истории.");
        assertEquals(1, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(2, history.get(1).getTaskId(), "Неверый id задач в истории.");
        assertEquals(3, history.get(2).getTaskId(), "Неверый id задач в истории.");

        taskManager.removeById(2);
        history = taskManager.getHistory();

        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertEquals(1, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(3, history.get(1).getTaskId(), "Неверый id задач в истории.");

        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);
        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubTasks();
        history = taskManager.getHistory();

        assertEquals(3, history.size(), "Неверное количество задач в истории.");
        assertEquals(3, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(1, history.get(1).getTaskId(), "Неверый id задач в истории.");
        assertEquals(4, history.get(2).getTaskId(), "Неверый id задач в истории.");
    }

    @Test
    protected void checkThatAfterBulkRemovalOfTasksHistoryAlsoIsCleaned() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        taskManager.createEpic(epic1);
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask1, epic1);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic2);

        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createTask(task1);
        Task task2 = new Task("таск2", "таск2 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createTask(task2);

        taskManager.getAllSubTasks();
        taskManager.getAllEpics();
        taskManager.getAllTasks();
        List<Task> history = taskManager.getHistory();

        assertEquals(6, history.size(), "Неверное количество задач в истории.");
        assertEquals(3, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(4, history.get(1).getTaskId(), "Неверый id задач в истории.");
        assertEquals(1, history.get(2).getTaskId(), "Неверый id задач в истории.");
        assertEquals(2, history.get(3).getTaskId(), "Неверый id задач в истории.");
        assertEquals(5, history.get(4).getTaskId(), "Неверый id задач в истории.");
        assertEquals(6, history.get(5).getTaskId(), "Неверый id задач в истории.");

        taskManager.removeAllSubTasks();
        history = taskManager.getHistory();

        assertEquals(4, history.size(), "Неверное количество задач в истории.");
        assertEquals(1, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(2, history.get(1).getTaskId(), "Неверый id задач в истории.");
        assertEquals(5, history.get(2).getTaskId(), "Неверый id задач в истории.");
        assertEquals(6, history.get(3).getTaskId(), "Неверый id задач в истории.");

        taskManager.removeAllTasks();
        history = taskManager.getHistory();

        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertEquals(1, history.get(0).getTaskId(), "Неверый id задач в истории.");
        assertEquals(2, history.get(1).getTaskId(), "Неверый id задач в истории.");

        taskManager.removeAllEpics();
        history = taskManager.getHistory();

        assertEquals(0, history.size(), "Неверное количество задач в истории.");
    }

    @Test
    protected void checkThatHistoryStoresOnlyLastViewOfObject() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask1, epic1);

        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createTask(task1);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        List<Task> history = taskManager.getHistory();


        assertEquals(3, history.size(), "Неверное количество задач в истории.");
        assertEquals(epic1, history.get(0), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals(subtask1, history.get(1), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, history.get(2), "История просмотров не совпадает с" +
                " порядком просмотра.");

        // Изменим состояние объектов и проверим, что история хранит все просмотры + предыдущее состоянии объектов.
        epic1.setName("1 изменение имени эпика");
        epic1.setDescription("1 изменение эпика");
        subtask1.setName("1 изменение имени сабтаски");
        subtask1.setDescription("1 изменение сабтаски");
        task1.setName("1 изменение имени таски");
        task1.setDescription("1 изменение таски");

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        List<Task> history1 = taskManager.getHistory();

        assertEquals(3, history1.size(), "Неверное количество задач в истории.");
        assertEquals(epic1, history1.get(0), "История просмотров не совпадает " + "с порядком" +
                " просмотра.");
        assertEquals(subtask1, history1.get(1), "История не совпадает с " + "порядком просмотра.");
        assertEquals(task1, history1.get(2), "История просмотров не совпадает с" + " порядком " +
                "просмотра.");

        // Теперь проверяем по полям историю просмотров
        assertEquals("1 изменение имени эпика", history1.get(0).getName(), "История просмотров " +
                "не совпадает с порядком просмотра.");
        assertEquals("1 изменение имени сабтаски", history1.get(1).getName(), "История не " +
                "совпадает с порядком просмотра.");
        assertEquals("1 изменение имени таски", history1.get(2).getName(), "История просмотров " +
                "не совпадает с порядком просмотра.");
        assertEquals("1 изменение эпика", history1.get(0).getDescription(), "История просмотров" +
                " не совпадает с порядком просмотра.");
        assertEquals("1 изменение сабтаски", history1.get(1).getDescription(), "История не " +
                "совпадает с порядком просмотра.");
        assertEquals("1 изменение таски", history1.get(2).getDescription(), "История просмотров" +
                " не совпадает с порядком просмотра.");
    }
}