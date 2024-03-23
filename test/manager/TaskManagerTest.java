package manager;

import enums.TaskStatus;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    Epic epic1;
    Subtask subtask1;
    Task task1;
    int epic1Id;
    int subtask1id;
    int task1id;
    LocalDateTime start = LocalDateTime.parse("20:10:10 23.12.2009", Managers.formatter);
    long durationForNextTask = 20;
    long duration = 5;
    LocalDateTime sStart;
    LocalDateTime sEnd;
    LocalDateTime tStart;
    LocalDateTime tEnd;

    protected void generateTasksForTest() {
        sStart = start.plusMinutes(durationForNextTask += 20);
        sEnd = sStart.plusMinutes(duration);
        tStart = start.plusMinutes(durationForNextTask += 20);
        tEnd = tStart.plusMinutes(duration);

        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        epic1Id = taskManager.createEpic(epic1);

        subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1", sStart, duration);
        subtask1id = taskManager.createSubTask(subtask1, epic1);

        task1 = new Task("таск1", "таск1 дескрипшн", tStart, duration);
        task1id = taskManager.createTask(task1);
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id (Task);
    @Test
    protected void addNewTaskAndCheckEqualityById() {
        generateTasksForTest();
        LocalDateTime t1Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t1End = t1Start.plusMinutes(duration);
        // Создаем задачи с одинаковым id
        Task taskWithSameId = new Task("taskWithSameId", " description of taskWithSameId",
                t1Start, duration);
        taskWithSameId.setTaskId(task1id);

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        assertNotNull(taskWithSameId, "Задача не найдена.");
        // Проверяем, что таски с одинаковым id равны друг другу, если равен их id
        assertEquals(task1, taskWithSameId, "Задачи не совпадают.");
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id (SubTask);
    @Test
    protected void addNewSubTaskAndCheckEqualityById() {
        generateTasksForTest();
        LocalDateTime s1Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s1End = s1Start.plusMinutes(duration);
        Subtask taskWithSameId = new Subtask("taskWithSameId", " description of taskWithSameId",
                s1Start, duration);
        taskWithSameId.setTaskId(subtask1id);

        List<Subtask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(1, epic1.getSubtasks().size(), "Неверное количество задач в эпике.");
        assertEquals(subtask1, subTasks.get(0), "Задачи не совпадают.");
        // Проверяем, что таски с одинаковым id равны друг другу, если равен их id
        assertNotNull(taskWithSameId, "Задача не найдена.");
        assertEquals(subtask1, taskWithSameId, "Задачи не совпадают.");
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id (Epic);
    @Test
    protected void addNewEpicAndCheckEqualityById() {
        generateTasksForTest();
        Epic taskWithSameId = new Epic("taskWithSameId", " description of taskWithSameId");
        taskWithSameId.setTaskId(epic1Id);

        List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
        // Проверяем, что таски с одинаковым id равны друг другу, если равен их id
        assertNotNull(taskWithSameId, "Задача не найдена.");
        assertEquals(epic1, taskWithSameId, "Задачи не совпадают.");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    protected void cantAddEpicInEpicAsSubtaskTest() {
        generateTasksForTest();
        LocalDateTime s1Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s1End = s1Start.plusMinutes(duration);
        Subtask subtask2 = new Subtask("subtask", "subtask description in " +
                "cantAddEpicInEpicAsSubtaskTest", s1Start, duration);

        subtask2.setTaskId(epic1Id); // Присвоили эпикID эпика сабтаске
        taskManager.createSubTask(subtask2, epic1); // Пробуем добавить сабтаску к эпику

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, epic1.getSubtasks().size(), "Неверное количество сабтаск в эпике");
        assertNotEquals(subtasks.get(1).getTaskId(), epic1Id, "id эпика и сабтаски совпали, то " +
                "есть эпик был сохранен как подзадача самого себя");
        assertEquals(4, subtasks.get(1).getTaskId(), "id сабтаски не был расчитан верно");
        assertEquals(4, subtask2.getTaskId(), "id сабтаски не был расчитан верно");
        assertEquals(4, epics.get(0).getSubtasks().get(1), "id сабтаски не был расчитан верно");
    }

    // проверьте, что объект Subtask с несуществующим id не добавится в список сабтаск
    @Test
    protected void cantUpdateSubtaskIfItAbsentInMapTest() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("subtask2", "cantUpdateSubtaskIfItAbsentInMapTest",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask2.setTaskId(1000); //  установим несуществующий idсабтаски
        taskManager.updateTask(subtask2); // Попытаемся обновить сабтаску

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, epic1.getSubtasks().size(), "Неверное количество сабтаск в эпике");
        assertEquals(2, subtasks.get(0).getTaskId(), "в эпике сохранена сабтаска с " +
                "несуществующим id");
        assertEquals("сабтаск1", subtasks.get(0).getName(), "в эпике сохранена сабтаска с " +
                "несуществующим именем");

        // Изменим в новом объекте сабтаски  поля :  id subtask2= id subtask1 , epicId = 100;
        subtask2.setEpicId(100);
        subtask2.setTaskId(subtask1id);
        taskManager.updateTask(subtask2); // Попытаемся обновить сабтаску

        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();

        // Првоерим, что так как id у сабтаск совпали, то произойдет обновление сабтаски , но epicId
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, epic1.getSubtasks().size(), "Неверное количество сабтаск в эпике");
        assertEquals(2, subtasks.get(0).getTaskId(), "в эпике сохранена сабтаска с " +
                "несуществующим id");
        assertEquals("сабтаск1", subtasks.get(0).getName(), "в эпике сохранена сабтаска с " +
                "несуществующим именем");
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    protected void tasksAreCreatedWithNewId() {
        generateTasksForTest();
        taskManager.removeAllSubTasks();
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        LocalDateTime s2Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime s2End = s2Start.plusMinutes(duration);
        LocalDateTime t2Start = start.plusMinutes(durationForNextTask += 20);
        LocalDateTime t2End = t2Start.plusMinutes(duration);

        Epic epic2 = new Epic("epic2", "tasksAreCreatedWithNewId");
        Subtask subtask2 = new Subtask("subtask2", "tasksAreCreatedWithNewId", s2Start, duration);
        Task task2 = new Task("task2", "tasksAreCreatedWithNewId", t2Start, duration);
        epic2.setTaskId(100);
        subtask2.setTaskId(101);
        task2.setTaskId(102);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subtask2, epic2);
        taskManager.createTask(task2);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        List<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(1, epic2.getSubtasks().size(), "Неверное количество сабтаск в эпике");

        assertEquals(100, epics.get(0).getTaskId(), "эпик сохранен с некорректным id");
        assertEquals(101, subtasks.get(0).getTaskId(), "сабтаска сохранена с некорректным id");
        assertEquals(102, tasks.get(0).getTaskId(), "таска сохранена с некорректным id");
        assertEquals(101, epic2.getSubtasks().get(0), "в эпике сохранена сабтаска с " +
                "несуществующим id");
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком
    @Test
    protected void cantUpdateSubtaskIfEpicidIsSameAsSomeSubtask() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("subtask", "tasksAreCreatedWithNewId",
                start.plusMinutes(durationForNextTask += 20), duration);

        subtask2.setEpicId(subtask1id);
        subtask2.setTaskId(subtask1id);
        taskManager.updateTask(subtask2);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, epic1.getSubtasks().size(), "Неверное количество сабтаск в эпике");

        assertEquals(1, epics.get(0).getTaskId(), "эпик сохранен с некорректным id");
        assertEquals(2, subtasks.get(0).getTaskId(), "сабтаска сохранена с некорректным id");
        assertEquals(2, epic1.getSubtasks().get(0), "в эпике сохранена сабтаска с " +
                "несуществующим id");
        assertEquals(1, subtasks.get(0).getEpicId(), "в эпике сохранена сабтаска с " +
                "несуществующим id");
        assertEquals("сабтаск1", subtasks.get(0).getName(), "в эпике сохранена сабтаска с " +
                "несуществующим id");
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    @Test
    protected void inMemoryTaskManagerAddsTasksOfDifferentTypesAndCanFindThem() {
        generateTasksForTest();
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        List<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, tasks.size(), "Неверное количество таск.");
        assertEquals(1, epic1.getSubtasks().size(), "Неверное количество сабтаск в эпике");

        assertEquals(epic1, taskManager.getTaskById(1), "Эпики не совпали.");
        assertEquals(subtask1, taskManager.getTaskById(2), "Сабтаски не совпали.");
        assertEquals(task1, taskManager.getTaskById(3), "Таски не совпали");

        assertEquals(epic1, epics.get(0), "Эпики не совпали.");
        assertEquals(subtask1, subtasks.get(0), "Сабтаски не совпали.");
        assertEquals(task1, tasks.get(0), "Таски не совпали");
    }

    // Проверяем удаление всех задач и списков после этого
    @Test
    protected void removeAllEpicsAndTasksAndSubTasks() {
        generateTasksForTest();
        taskManager.removeAllSubTasks();
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, tasks.size(), "Неверное количество таск.");

        taskManager.removeAllTasks();
        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();
        tasks = taskManager.getAllTasks();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(0, tasks.size(), "Неверное количество таск.");

        taskManager.removeAllEpics();
        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();
        tasks = taskManager.getAllTasks();
        assertEquals(0, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(0, tasks.size(), "Неверное количество таск.");
    }

    // Проверим, что статус эпика меняется вслед за сабтасками в нем
    @Test
    protected void checkStatusOfEpic() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");

        assertEquals(TaskStatus.NEW, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.NEW, subtasks.get(0).getStatus(), "Неверный статус сабтаски");
        assertEquals(TaskStatus.NEW, subtasks.get(1).getStatus(), "Неверный статус сабтаски");

        // Изменим статус 1-ой сабтаски на IN_PROGRESS -> статус эпика IN_PROGRESS
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(0).getStatus(), "Неверный статус " +
                "сабтаски");
        assertEquals(TaskStatus.NEW, subtasks.get(1).getStatus(), "Неверный статус сабтаски");

        // Изменим статус 2-ой сабтаски на IN_PROGRESS -> статус эпика IN_PROGRESS
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(0).getStatus(), "Неверный статус " +
                "сабтаски");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus(), "Неверный статус " +
                "сабтаски");

        // Изменим статус 1-ой сабтаски на DONE -> статус эпика IN_PROGRESS
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.DONE, subtasks.get(0).getStatus(), "Неверный статус сабтаски");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus(), "Неверный статус " +
                "сабтаски");

        // Изменим статус 2-ой сабтаски на DONE -> статус эпика DONE
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateTask(subtask1);
        assertEquals(TaskStatus.DONE, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.DONE, subtasks.get(0).getStatus(), "Неверный статус сабтаски");
        assertEquals(TaskStatus.DONE, subtasks.get(1).getStatus(), "Неверный статус сабтаски");
    }

    @Test
    protected void checkRemoveOfSubtaskAndUpdateOfEpicStatus() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        // Установили статус сабтаски2 = IN_PROGRESS
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubTask(subtask2, epic1);
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        // Проверили, что при создании сабтаски старый статус сабтаски  не меняется на NEW
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.NEW, subtasks.get(0).getStatus(), "Неверный статус сабтаски");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus(), "Неверный статус " +
                "сабтаски");

        // Изменим статус 1-ой сабтаски на DONE -> статус эпика IN_PROGRESS
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.DONE, subtasks.get(0).getStatus(), "Неверный статус " +
                "сабтаски");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus(), "Неверный статус " +
                "сабтаски");

        // Удалим сабтаску по ID и проверим статус эпика -> NEW,
        // а также проверим, что сабтаска удалена из эпика и списка сабтаск
        taskManager.removeById(subtask1id);

        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, tasks.size(), "Неверное количество таск.");

        assertEquals(epic1, epics.get(0), "Неверное количество эпиков.");
        assertEquals(subtask2, subtasks.get(0), "Неверное количество сабтаск.");
        assertEquals(1, tasks.size(), "Неверное количество таск.");

        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(0).getStatus(), "Неверный статус " +
                "сабтаски");
    }

    // Проверка удаления сабтасок при удалении эпика
    @Test
    protected void checkRemoveOfEpicAndSubTasksAfterItsRemove() {
        generateTasksForTest();
        Epic epic2 = new Epic("эпик2", "эпика2 дескрипшн");
        taskManager.createEpic(epic2);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(0, epics.get(1).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");

        // Удалим 1 эпик и проверим, что 2 эпик остался, а сабтаски 1-го эпика удалены из всех списков
        taskManager.removeById(1);
        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(0, subtasks.size(), "Неверное количество сабтаск.");
    }

    // Проверим, что можем получить все объекты сабтаск эпика, имея его id
    @Test
    protected void checkThatWeCanGetSubtasksObjectsByEpic() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);

        List<Subtask> subtasksByepic1 = taskManager.getSubTasksByEpic(epic1Id);

        assertEquals(2, subtasksByepic1.size(), "Неверное количество сабтаск.");
        assertEquals(subtask1, subtasksByepic1.get(0), "Неверное содержимое сабтаск.");
        assertEquals(subtask2, subtasksByepic1.get(1), "Неверное содержимое сабтаск.");

    }

    @Test
    // Внутри эпиков не должно оставаться неактуальных id подзадач.
    protected void checkRemoveOfIdSubtasksFromEpicsAfterSubtaskBeingRemoved() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски1.");
        assertEquals(4, subtasks.get(1).getTaskId(), "Неверный id сабтаски2.");
        assertEquals(2, epics.get(0).getSubtasks().get(0), "Неверный id сабтаски1. в эпике");
        assertEquals(4, epics.get(0).getSubtasks().get(1), "Неверный id сабтаски2. в эпике");

        // Удалим 1 сабтаску и проверим, что 2 сабтаска осталась
        taskManager.removeById(subtask2.getTaskId());
        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(1, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски1.");
        assertEquals(2, epics.get(0).getSubtasks().get(0), "Неверный id сабтаски1. в эпике");
    }

    @Test
    protected void checkIfSubtaskIsUpdatedAndNewEpicIsNotEqualWithOld() {
        generateTasksForTest();
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        taskManager.createEpic(epic2);

        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика2",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic2);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски.");
        assertEquals(5, subtasks.get(1).getTaskId(), "Неверный id сабтаски.");
        assertEquals(1, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(1, epics.get(1).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, epics.get(0).getSubtasks().get(0), "Неверный id сабтаски. в эпике");
        assertEquals(5, epics.get(1).getSubtasks().get(0), "Неверный id сабтаски. в эпике");

        // Поменяем в сабтаске2 epicid : вместо эпика2 сделаем эпик1

        subtask2.setEpicId(1);
        // Обновим сабтаску2
        taskManager.updateTask(subtask2);

        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();
        // Проверим, что сабтаска2 теперь привязана к эпику1, а из эпика2 удалена
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски.");
        assertEquals(5, subtasks.get(1).getTaskId(), "Неверный id сабтаски.");
        assertEquals(2, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(0, epics.get(1).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, epics.get(0).getSubtasks().get(0), "Неверный id сабтаски. в эпике");
        assertEquals(5, epics.get(0).getSubtasks().get(1), "Неверный id сабтаски. в эпике");
    }

    @Test
    protected void checkIfEpicIsUpdated() {
        generateTasksForTest();
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        epic2.setTaskId(epic1Id);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски.");
        assertEquals(1, epics.get(0).getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, epics.get(0).getSubtasks().get(0), "Неверный id сабтаски. в эпике");

        taskManager.updateTask(epic2);

        epics = taskManager.getAllEpics();
        subtasks = taskManager.getAllSubTasks();

        Epic epicToCheck = epics.get(0);
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(2, subtasks.get(0).getTaskId(), "Неверный id сабтаски.");
        assertEquals(1, epicToCheck.getSubtasks().size(), "Неверное количество сабтаск в эпике.");
        assertEquals(2, epicToCheck.getSubtasks().get(0), "Неверный id сабтаски. в эпике");
        assertEquals(1, epicToCheck.getTaskId(), "Неверный id эпика.");
        assertEquals("эпик2", epicToCheck.getName(), "Неверный name эпика.");
        assertEquals("эпик2 дескрипшн", epicToCheck.getDescription(), "Неверный дескрипшн эпика.");
    }

    // Для расчёта статуса Epic. Граничные условия: a. Все подзадачи со статусом NEW.
    @Test
    protected void checkStatusOfEpicIfAllSubtasksOfStatusNew() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 дескрипшн",
                start.plusMinutes(durationForNextTask += 20), duration);
        taskManager.createSubTask(subtask2, epic1);
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(TaskStatus.NEW, subtasks.get(0).getStatus());
        assertEquals(TaskStatus.NEW, subtasks.get(1).getStatus());
        assertEquals(TaskStatus.NEW, epics.get(0).getStatus());
    }

    // Для расчёта статуса Epic. Граничные условия: b. Все подзадачи со статусом DONE.
    @Test
    protected void checkStatusOfEpicIfAllSubtasksOfStatusDone() {
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubTask(subtask1, epic1);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(TaskStatus.DONE, subtasks.get(0).getStatus());
        assertEquals(TaskStatus.DONE, subtasks.get(1).getStatus());
        assertEquals(TaskStatus.DONE, epics.get(0).getStatus());
    }

    // Для расчёта статуса Epic. Граничные условия: c. Подзадачи со статусами NEW и DONE.
    @Test
    protected void checkStatusOfEpicIfAllSubtasksOfStatusNewAndDone() {
        generateTasksForTest();
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(TaskStatus.NEW, subtasks.get(0).getStatus());
        assertEquals(TaskStatus.DONE, subtasks.get(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus());
    }

    // Для расчёта статуса Epic. Граничные условия: d. Подзадачи со статусом IN_PROGRESS.
    @Test
    protected void checkStatusOfEpicIfAllSubtasksOfStatusInProgress() {
        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1",
                start.plusMinutes(durationForNextTask += 20), duration);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubTask(subtask1, epic1);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(0).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, epics.get(0).getStatus());
    }

    //c. тест на корректность расчёта пересечения интервалов. Если задачи не пересекаются
    @Test
    protected void checkIfSubtaskAndTaskTimeNotClash() {
        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        LocalDateTime s1Start = start;
        LocalDateTime s1End = s1Start.plusMinutes(duration);
        LocalDateTime s2Start = s1End.plusMinutes(1);
        LocalDateTime s2End = s2Start.plusMinutes(duration);
        LocalDateTime t1Start = s2End.plusMinutes(1);
        LocalDateTime t1End = t1Start.plusMinutes(duration);
        LocalDateTime t2Start = t1End.plusMinutes(1);
        LocalDateTime t2End = t2Start.plusMinutes(duration);
        subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1", s1Start, duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1", s2Start, duration);
        task1 = new Task("таск1", "таск1 дескрипшн", t1Start, duration);
        Task task2 = new Task("таск2", "таск2 дескрипшн", t2Start, duration);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask1, epic1);
        taskManager.createSubTask(subtask2, epic1);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Epic> epics = taskManager.getAllEpics();
        List<Task> tasks = taskManager.getAllTasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        LocalDateTime s1StartInManager = subtask1.getStartTime();
        LocalDateTime s2StartInManager = subtask2.getStartTime();
        LocalDateTime s1EndInManager = subtask1.getEndTime();
        LocalDateTime s2EndInManager = subtask2.getEndTime();
        LocalDateTime t1StartInManager = task1.getStartTime();
        LocalDateTime t2StartInManager = task2.getStartTime();
        LocalDateTime t1EndInManager = task1.getEndTime();
        LocalDateTime t2EndInManager = task2.getEndTime();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, tasks.size(), "Неверное количество таск.");
        assertEquals(2, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(epic1.getStartTime(), s1StartInManager);
        assertEquals(epic1.getEndTime(), s2EndInManager);
        assertEquals(epic1.getDuration(), Duration.between(s1StartInManager, s2EndInManager));
        assertEquals(s1Start, s1StartInManager);
        assertEquals(s2Start, s2StartInManager);
        assertEquals(t1Start, t1StartInManager);
        assertEquals(t2Start, t2StartInManager);

        assertEquals(s1End, s1EndInManager);
        assertEquals(s2End, s2EndInManager);
        assertEquals(t1End, t1EndInManager);
        assertEquals(t2End, t2EndInManager);
    }

    //c. тест на корректность расчёта пересечения интервалов. Если задачи пересекаются
    @Test
    protected void checkIfSubtaskAndTaskTimeClash() {
        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1", start, duration);
        task1 = new Task("таск1", "таск1 дескрипшн", start, duration);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask1, epic1);
        taskManager.createTask(task1);


        List<Epic> epics = taskManager.getAllEpics();
        List<Task> tasks = taskManager.getAllTasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        LocalDateTime s1Start = subtask1.getStartTime();
        LocalDateTime s1End = subtask1.getEndTime();
        LocalDateTime t1Start = task1.getStartTime();
        LocalDateTime t1End = task1.getEndTime();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, tasks.size(), "Неверное количество таск.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(epic1.getStartTime(), s1Start);
        assertEquals(epic1.getEndTime(), s1End);
        assertEquals(epic1.getDuration(), Duration.between(s1Start, s1End));
    }

    //c. тест на корректность расчёта пересечения интервалов. Если задачи пересекаются
    @Test
    protected void checkIfSubtasksTimeClash() {
        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1", start, duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1", start, duration);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask1, epic1);
        taskManager.createSubTask(subtask2, epic1);

        List<Epic> epics = taskManager.getAllEpics();
        List<Task> tasks = taskManager.getAllTasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();

        LocalDateTime s1Start = subtask1.getStartTime();
        LocalDateTime s2End = subtask2.getEndTime();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, tasks.size(), "Неверное количество таск.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(epic1.getStartTime(), s1Start);
        assertEquals(epic1.getEndTime(), s2End);
        assertEquals(subtask1.getTaskId(), epic1.getSubtasks().get(0));
    }

    //c. тест на приоритеты задач
    @Test
    protected void checkHowPrioritizedTasks() {
        generateTasksForTest();
        LocalDateTime s2Start = sStart.minusMinutes(100);
        LocalDateTime s2End = s2Start.plusMinutes(duration);
        LocalDateTime s3Start = s2End.plusMinutes(1);
        LocalDateTime s3End = s3Start.plusMinutes(duration);
        LocalDateTime t2Start = sStart.minusMinutes(50);
        LocalDateTime t2End = t2Start.plusMinutes(duration);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1", s2Start, duration);
        Subtask subtask3 = new Subtask("сабтаск3", "сабтаск3 для экпика1", s3Start, duration);
        Task task2 = new Task("таск2", "таск2 дескрипшн", t2Start, duration);
        taskManager.createSubTask(subtask2, epic1);
        taskManager.createSubTask(subtask3, epic1);
        taskManager.createTask(task2);

        List<Epic> epics = taskManager.getAllEpics();
        List<Task> tasks = taskManager.getAllTasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        Iterator iterator = prioritizedTasks.iterator();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, tasks.size(), "Неверное количество таск.");
        assertEquals(3, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(5, prioritizedTasks.size());
        assertEquals(subtask2, (Subtask) iterator.next());
        assertEquals(subtask3, (Subtask) iterator.next());
        assertEquals(task2, iterator.next());
        assertEquals(subtask1, (Subtask) iterator.next());
        assertEquals(task1, iterator.next());
    }

    @Test
    protected void getSubTasksByEpicTestIfEpicDoesntExist() {
        generateTasksForTest();
        List<Subtask> subTasks = taskManager.getSubTasksByEpic(100);
        assertEquals(true, subTasks.isEmpty());
    }

    @Test
    protected void getTaskByIdIfIdDoesntExist() {
        generateTasksForTest();
        Task taskToFind = taskManager.getTaskById(100);
        assertEquals(null, taskToFind);
    }

    @Test
    protected void removeById() {
        generateTasksForTest();
        List<Task> tasks = taskManager.getAllTasks();
        List<Task> history = taskManager.getHistory();
        assertEquals(1, tasks.size());
        assertEquals(1, history.size());

        taskManager.removeById(3);
        tasks = taskManager.getAllTasks();
        history = taskManager.getHistory();
        assertEquals(0, tasks.size());
        assertEquals(0, history.size());
    }

    @Test
    protected void getEpicBySubtaskIdIfIdDoesntExist() {
        generateTasksForTest();
        Epic epicToFind = taskManager.getEpicBySubtaskId(100);
        assertEquals(null, epicToFind);
    }

    @Test
    protected void updateTaskIfTaskExistInMapAndTimeNotClash() {
        generateTasksForTest();
        LocalDateTime t1Start = start.plusMinutes(durationForNextTask += 20);
        Task task2 = new Task("таск2", "таск2 дескрипшн", t1Start, duration);
        task2.setTaskId(task1id);
        taskManager.updateTask(task2);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task1id, taskManager.getAllTasks().get(0).getTaskId());
        assertEquals("таск2", taskManager.getAllTasks().get(0).getName());
        assertEquals("таск2 дескрипшн", taskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    protected void updateTaskIfTaskDoesntExistInMapAndTimeNotClash() {
        generateTasksForTest();
        LocalDateTime t1Start = start.plusMinutes(durationForNextTask += 20);
        Task task2 = new Task("таск2", "таск2 дескрипшн", t1Start, duration);
        task2.setTaskId(100);
        taskManager.updateTask(task2);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task1id, taskManager.getAllTasks().get(0).getTaskId());
        assertEquals("таск1", taskManager.getAllTasks().get(0).getName());
        assertEquals("таск1 дескрипшн", taskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    protected void updateTaskIfTaskExistInMapAndTimeClash() {
        generateTasksForTest();
        Task task2 = new Task("таск2", "таск2 дескрипшн", tStart, duration);
        task2.setTaskId(task1.getTaskId());
        taskManager.updateTask(task2);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task1id, taskManager.getAllTasks().get(0).getTaskId());
        assertEquals("таск2", taskManager.getAllTasks().get(0).getName());
        assertEquals("таск2 дескрипшн", taskManager.getAllTasks().get(0).getDescription());
    }

    @Test
    protected void createEpicIfListOfSubtasksIsNotEmpty() {
        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        epic1.getSubtasks().add(1);
        epic1Id = taskManager.createEpic(epic1);

        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, epic1Id);

    }
}