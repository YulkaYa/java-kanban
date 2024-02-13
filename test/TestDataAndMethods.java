import manager.InMemoryTaskManager;
import manager.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;
import enums.TaskStatus;
import java.util.List;

public class TestDataAndMethods {

    private static TaskManager generateTasksForTest() {

        TaskManager manager = new InMemoryTaskManager();

        Epic epic1 = new Epic("эпик1", "эпик1 дескрипшн");
        Epic epic2 = new Epic("эпик2", "эпик2 дескрипшн");
        Epic epic3 = new Epic("эпик3", "эпик3 дескрипшн");
        Epic epic4 = new Epic("эпик4", "эпик4 дескрипшн");
        int epic1Id = manager.createEpic(epic1);
        int epic2Id = manager.createEpic(epic2);
        int epic3Id = manager.createEpic(epic3);
        int epic4Id = manager.createEpic(epic4);

        Subtask subtask1 = new Subtask("сабтаск1", "сабтаск1 для экпика1");
        Subtask subtask2 = new Subtask("сабтаск2", "сабтаск2 для экпика1");
        Subtask subtask3 = new Subtask("сабтаск3", "сабтаск3 для экпика1");
        Subtask subtask4 = new Subtask("сабтаск4", "сабтаск4 для экпика1");
        Subtask subtask5 = new Subtask("сабтаск5", "сабтаск5 для экпика2");
        Subtask subtask6 = new Subtask("сабтаск6", "сабтаск6 для экпика2");
        Subtask subtask7 = new Subtask("сабтаск7", "сабтаск7 для экпика2");
        Subtask subtask8 = new Subtask("сабтаск8", "сабтаск8 для экпика2");
        int subtask1id = manager.createSubTask(subtask1, epic1);
        int subtask2id = manager.createSubTask(subtask2, epic1);
        int subtask3id = manager.createSubTask(subtask3, epic1);
        int subtask4id = manager.createSubTask(subtask4, epic1);
        int subtask5id = manager.createSubTask(subtask5, epic2);
        int subtask6id = manager.createSubTask(subtask6, epic2);
        int subtask7id = manager.createSubTask(subtask7, epic2);
        int subtask8id = manager.createSubTask(subtask7, epic2);

        Task task1 = new Task("таск1", "таск1 дескрипшн");
        Task task2 = new Task("таск2", "таск2 дескрипшн");
        Task task3 = new Task("таск3", "таск3 дескрипшн");
        Task task4 = new Task("таск4", "таск4 дескрипшн");
        int task1id = manager.createTask(task1);
        int task2id = manager.createTask(task2);
        int task3id = manager.createTask(task3);
        int task4id = manager.createTask(task4);
        return manager;
    }


    public static void testAll()  {
        TaskManager manager = generateTasksForTest();
        System.out.println("history"  + manager.getHistory());
        System.out.println("1--------------------");
        List<Task> tasks = manager.getAllTasks();
        System.out.println("history"  + manager.getHistory());
        System.out.println("2--------------------");
        List<Epic> epics = manager.getAllEpics();
        System.out.println("history"  + manager.getHistory());
        System.out.println("3--------------------");
        List<Subtask> subtasks = manager.getAllSubTasks();
        System.out.println("history"  + manager.getHistory());
        System.out.println("4--------------------");
        manager.getTaskById(tasks.get(1).getTaskId());
        System.out.println("history"  + manager.getHistory());
        System.out.println("5--------------------");
        manager.getTaskById(epics.get(1).getTaskId());
        System.out.println("history"  + manager.getHistory());
        System.out.println("6--------------------");
        manager.getTaskById(subtasks.get(1).getTaskId());
        System.out.println("history"  + manager.getHistory());
        System.out.println("7--------------------");
        manager.removeById(subtasks.get(1).getTaskId());
        System.out.println("history"  + manager.getHistory());
        System.out.println("8--------------------");
        manager.getTaskById(epics.get(1).getTaskId());
        System.out.println("history"  + manager.getHistory());

        System.out.println("9--------------------");
        System.out.println("проверяем updateTask для эпика");

        Epic epicNew = new Epic("эпик345435", "новый эпик345435"); // создадим новый объект эпик
        epicNew.setTaskId(1); // установим ид нового эпика = 1 (сейчас этот ид у эпика "эпик1")

        manager.updateTask(epicNew); // обновим объект эпика1 объектом epicNew с таким же ид

        Subtask subtaskNew = new Subtask("субтаскнью", "дескрипшн");
        subtaskNew.setStatus(TaskStatus.IN_PROGRESS);
        manager.createSubTask(subtaskNew, epicNew);
        System.out.println("10--------------------");
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        Subtask subtaskNew1 = new Subtask("сабтаск8", "сабтаск8 для замены сабтаски 8");
        subtaskNew1.setEpicId(1); // заменим в новой сабтаске epicId
        subtaskNew1.setTaskId(8); // укажем уже существующий id сабтаски

        Subtask subtaskNew2 = new Subtask("сабтаск9", "сабтаск9 для замены сабтаски c id = 9");
        subtaskNew2.setTaskId(9); // укажем уже существующий id сабтаски
        System.out.println("проверяем updateTask для сабтаски");
        manager.updateTask(subtaskNew1); // проверим обновление сабтаски
        manager.updateTask(subtaskNew2);

        System.out.println("11--------------------");
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("12--------------------");

        // проверяем удаление таск, сабтаски, эпика
        System.out.println("проверяем removeById для эпика");
        manager.removeById(1);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("13--------------------");
        System.out.println("проверяем removeById для сабтаски");
        manager.removeById(12);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("14--------------------");
        System.out.println("проверяем removeById для таски");
        manager.removeById(13);
        manager.printAllTasks();  // распечатаем таски

        System.out.println("15--------------------");
        System.out.println("проверяем обновление статуса для эпика'");
        manager.getTaskById(9).setStatus(TaskStatus.IN_PROGRESS); // меняем статус одной из сабтаск на IN_PROGRESS
        manager.updateEpicStatus(2);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("16--------------------");
        manager.getTaskById(9).setStatus(TaskStatus.NEW); // меняем статус сабтаски снова на New
        manager.updateEpicStatus(2);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("17--------------------");
        manager.getTaskById(9).setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE для эпика
        manager.getTaskById(10).setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE  для эпика
        manager.getTaskById(11).setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE  для эпика
        manager.updateEpicStatus(2);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("18--------------------");
        System.out.println("проверяем удаление всех сабтаск'");
        manager.removeAllSubTasks();
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски
        System.out.println("history"  + manager.getHistory());

        System.out.println("19--------------------");
        System.out.println("проверяем удаление всех таск'");
        manager.removeAllTasks();
        manager.printAllTasks();  // распечатаем таски
        System.out.println("history"  + manager.getHistory());
        System.out.println("20--------------------");
        System.out.println("history"  + manager.getHistory());
        System.out.println("21--------------------");
        System.out.println("проверяем удаление всех эпиков'");
        manager.removeAllEpics();
        System.out.println("history"  + manager.getHistory());
    }

    public static void testHistory()  {
        TaskManager manager = generateTasksForTest();
        manager.removeById(3);
        System.out.println(manager.getHistory());
        System.out.println("1--------------------");
        List<Task> tasks = manager.getAllTasks();
        System.out.println(manager.getHistory());
        System.out.println("2--------------------");
        List<Epic> epics = manager.getAllEpics();
        System.out.println(manager.getHistory());
        System.out.println("3--------------------");
        List<Subtask> subtasks = manager.getAllSubTasks();
        System.out.println(manager.getHistory());
        System.out.println("4--------------------");
        manager.getTaskById(13);
        System.out.println(manager.getHistory());
        System.out.println("5--------------------");
        manager.getTaskById(2);
        System.out.println(manager.getHistory());
        System.out.println("6--------------------");
        manager.getTaskById(6);
        System.out.println(manager.getHistory());
        System.out.println("7--------------------");
        manager.getTaskById(2);
        System.out.println(manager.getHistory());
        System.out.println("8--------------------");
        manager.removeById(6);
        System.out.println(manager.getHistory());
        System.out.println("9--------------------");
        manager.getTaskById(1);
        System.out.println(manager.getHistory());
        System.out.println("10--------------------");
        manager.removeAllSubTasks();
        System.out.println(manager.getHistory());
        System.out.println("11--------------------");
        manager.getTaskById(16);
        System.out.println(manager.getHistory());
        System.out.println("12--------------------");
        manager.removeById(1);
        System.out.println(manager.getHistory());
        System.out.println("13--------------------");
        manager.removeById(15);
        System.out.println(manager.getHistory());
        System.out.println("14--------------------");
        manager.removeAllSubTasks();
        System.out.println(manager.getHistory());
        System.out.println("11--------------------");
        manager.getTaskById(14);
        System.out.println(manager.getHistory());
        System.out.println("15--------------------");
        manager.removeAllTasks();
        System.out.println(manager.getHistory());
        System.out.println("16--------------------");
        manager.removeAllEpics();
        System.out.println(manager.getHistory());
    }
}
