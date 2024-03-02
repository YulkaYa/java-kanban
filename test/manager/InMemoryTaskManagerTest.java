package manager;

import enums.TaskStatus;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    static Epic epic1;
    static Subtask subtask1;
    static Task task1;
    static int epic1Id;
    static int subtask1id;
    static int task1id;

    @BeforeEach
    private void generateTasksForTest() {
        taskManager = new InMemoryTaskManager();

        epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        epic1Id = taskManager.createEpic(epic1);

        subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        subtask1id = taskManager.createSubTask(subtask1, epic1);

        task1 = new Task("таск1", "таск1 дескрипшн");
        task1id = taskManager.createTask(task1);
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id (Task);
    @Test
    void addNewTaskAndCheckEqualityById() {
        // Создаем задачи с одинаковым id
        Task taskWithSameId = new Task("taskWithSameId", " description of taskWithSameId");
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
    void addNewSubTaskAndCheckEqualityById() {
        Subtask taskWithSameId = new Subtask("taskWithSameId", " description of taskWithSameId");
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
    void addNewEpicAndCheckEqualityById() {
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
    void cantAddEpicInEpicAsSubtaskTest() {
        Subtask subtask2 = new Subtask("subtask", "subtask description in cantAddEpicInEpicAsSubtaskTest");

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
    void cantUpdateSubtaskIfItAbsentInMapTest() {

        Subtask subtask2 = new Subtask("subtask2", "cantUpdateSubtaskIfItAbsentInMapTest");
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
    void tasksAreCreatedWithNewId() {
        taskManager.removeAllSubTasks();
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        Epic epic2 = new Epic("epic2", "tasksAreCreatedWithNewId");
        Subtask subtask2 = new Subtask("subtask2", "tasksAreCreatedWithNewId");
        Task task2 = new Task("task2", "tasksAreCreatedWithNewId");
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
    void cantUpdateSubtaskIfEpicidIsSameAsSomeSubtask() {
        Subtask subtask2 = new Subtask("subtask", "tasksAreCreatedWithNewId");

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
    void inMemoryTaskManagerAddsTasksOfDifferentTypesAndCanFindThem() {
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
    void removeAllEpicsAndTasksAndSubTasks() {

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
    void checkStatusOfEpic() {
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
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
    void checkRemoveOfSubtaskAndUpdateOfEpicStatus() {
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
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
        assertEquals(TaskStatus.IN_PROGRESS, subtasks.get(1).getStatus(), "Неверный статус сабтаски");

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
    void checkRemoveOfEpicAndSubTasksAfterItsRemove() {
        Epic epic2 = new Epic("эпик2", "эпика2 дескрипшн");
        taskManager.createEpic(epic2);
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
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
    void checkThatWeCanGetSubtasksObjectsByEpic() {
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
        taskManager.createSubTask(subtask2, epic1);

        List<Subtask> subtasksByepic1 = taskManager.getSubTasksByEpic(epic1Id);

        assertEquals(2, subtasksByepic1.size(), "Неверное количество сабтаск.");
        assertEquals(subtask1, subtasksByepic1.get(0), "Неверное содержимое сабтаск.");
        assertEquals(subtask2, subtasksByepic1.get(1), "Неверное содержимое сабтаск.");

    }

    @Test
        // Внутри эпиков не должно оставаться неактуальных id подзадач.
    void checkRemoveOfIdSubtasksFromEpicsAfterSubtaskBeingRemoved() {
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
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
    void checkIfSubtaskIsUpdatedAndNewEpicIsNotEqualWithOld() {
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        int epic2Id = taskManager.createEpic(epic2);

        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика2");
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
    void checkIfEpicIsUpdated() {
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
}