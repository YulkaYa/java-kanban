package service;

import history.HistoryManager;
import manager.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    void managersCreatesInitializedAndReadyToWorkObjectsOfManagers() throws CloneNotSupportedException {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        int subtask1id = taskManager.createSubTask(subtask1, epic1);

        Task task1 = new Task("таск1", "таск1 дескрипшн");
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
    void managersCreatesInitializedAndReadyToWorkObjectsOfHistoryManagers() throws CloneNotSupportedException {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        Task task1 = new Task("таск1", "таск1 дескрипшн");

        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(task1);
        assertEquals(3, historyManager.getHistory().size(), "Неверное количество задач в истории.");
        assertEquals(epic1, historyManager.getHistory().get(0), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(subtask1, historyManager.getHistory().get(1), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, historyManager.getHistory().get(2), "История не совпадает с " +
                "порядком просмотра.");
    }

    @Test
    void checkThatHistoryStoresPreviousStateOfObject() throws CloneNotSupportedException {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        int subtask1id = taskManager.createSubTask(subtask1, epic1);

        Task task1 = new Task("таск1", "таск1 дескрипшн");
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
        assertEquals(6, taskManager.getHistory().size(), "Неверное количество задач в истории.");
        assertEquals(epic1, taskManager.getHistory().get(0), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals(subtask1, taskManager.getHistory().get(1), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, taskManager.getHistory().get(2), "История просмотров не совпадает с" +
                " порядком просмотра.");
        assertEquals(epic1, taskManager.getHistory().get(3), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals(subtask1, taskManager.getHistory().get(4), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals(task1, taskManager.getHistory().get(5), "История просмотров не совпадает с" +
                " порядком просмотра.");

        // Теперь проверяем по полям историю просмотров
        assertEquals("эпик1", taskManager.getHistory().get(0).getName(), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals("сабтаск1", taskManager.getHistory().get(1).getName(), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals("таск1", taskManager.getHistory().get(2).getName(), "История просмотров не совпадает с" +
                " порядком просмотра.");
        assertEquals("1 изменение имени эпика", taskManager.getHistory().get(3).getName(), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals("1 изменение имени сабтаски", taskManager.getHistory().get(4).getName(), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals("1 изменение имени таски", taskManager.getHistory().get(5).getName(), "История просмотров не совпадает с" +
                " порядком просмотра.");
        assertEquals("1 изменение эпика", taskManager.getHistory().get(3).getDescription(), "История просмотров не совпадает " +
                "с порядком просмотра.");
        assertEquals("1 изменение сабтаски", taskManager.getHistory().get(4).getDescription(), "История не совпадает с " +
                "порядком просмотра.");
        assertEquals("1 изменение таски", taskManager.getHistory().get(5).getDescription(), "История просмотров не совпадает с" +
                " порядком просмотра.");
    }
}