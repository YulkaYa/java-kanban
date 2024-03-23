package service;

import history.HistoryManager;
import manager.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {
    private LocalDateTime start = LocalDateTime.parse("23:12:13 23.12.2009", Managers.formatter);
    private long durationForNextTask = 20;
    private long duration = 5;

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    protected void managersCreatesInitializedAndReadyToWorkObjectsOfManagers() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        int subtask1id = taskManager.createSubTask(subtask1, epic1);

        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        int task1id = taskManager.createTask(task1);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getHistory();
        assertEquals(3, taskManager.getHistory().size(), "Неверное количество задач в истории.");
        assertEquals(epic1, taskManager.getHistory().get(0), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals(subtask1, taskManager.getHistory().get(1), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, taskManager.getHistory().get(2), "История просмотров не совпадает с" +
                " порядком просмотра.");
    }

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    protected void managersCreatesInitializedAndReadyToWorkObjectsOfHistoryManagers() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        epic1.setTaskId(1);
        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask1.setTaskId(2);
        Task task1 = new Task("таск1", "таск1 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        task1.setTaskId(3);

        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(task1);
        assertEquals(3, historyManager.getHistory().size(), "Неверное количество задач в " +
                "истории.");
        assertEquals(epic1, historyManager.getHistory().get(0), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(subtask1, historyManager.getHistory().get(1), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, historyManager.getHistory().get(2), "История не совпадает с " +
                "порядком просмотра.");
    }
}
