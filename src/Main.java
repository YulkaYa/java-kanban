public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();

        Task task1 = new Task("таск1", "таск1 дескрипшн");
        Task task2 = new Task("таск2", "таск2 дескрипшн");
        Task task3 = new Task("таск3", "таск3 дескрипшн");
        Task task4 = new Task("таск4", "таск4 дескрипшн");
        int task1id = manager.createTask(task1);
        int task2id = manager.createTask(task2);
        int task3id = manager.createTask(task3);
        int task4id = manager.createTask(task4);

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
        Subtask subtask4 = new Subtask("сабтаск4", "сабтаск4 для экпика2");
        Subtask subtask5 = new Subtask("сабтаск5", "сабтаск5 для экпика2");
        Subtask subtask6 = new Subtask("сабтаск6", "сабтаск6 для экпика2");
        Subtask subtask7 = new Subtask("сабтаск7", "сабтаск7 для экпика2");
        int subtask1id = manager.createSubTask(subtask1, epic1);
        int subtask2id = manager.createSubTask(subtask2, epic1);
        int subtask3id = manager.createSubTask(subtask3, epic1);
        int subtask4id = manager.createSubTask(subtask4, epic2);
        int subtask5id = manager.createSubTask(subtask5, epic2);
        int subtask6id = manager.createSubTask(subtask6, epic2);
        int subtask7id = manager.createSubTask(subtask7, epic2);

        manager.printAllEpics();
        manager.printAllTasks();
        manager.printAllSubTasks();

        System.out.println("проверяем updateTask для эпика");

        Epic epicNew = new Epic("эпик345435", "новый эпик345435"); // создадим новый объект эпик
        epicNew.setTaskId(5); // установим ид нового эпика = 5 (сейчас этот ид у эпика "эпик1")

        manager.updateTask(epicNew); // обновим объект эпика1 объектом epicNew с таким же ид

        Subtask subtaskNew = new Subtask("субтаскнью", "дескрипшн");
        subtaskNew.setStatus(TaskStatus.IN_PROGRESS);
        manager.createSubTask(subtaskNew, epicNew);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        Subtask subtaskNew1 = new Subtask("сабтаск8", "сабтаск8 для замены сабтаски 16");
        subtaskNew1.setEpicId(7); // заменим в новой сабтаске epicId
        subtaskNew1.setTaskId(16); // укажем уже существующий id сабтаски

        Subtask subtaskNew2 = new Subtask("сабтаск9", "сабтаск9 для замены сабтаски c id = 9");
        subtaskNew1.setTaskId(9); // укажем уже существующий id сабтаски
        System.out.println("проверяем updateTask для сабтаски");
        manager.updateTask(subtaskNew1); // проверим обновление сабтаски
        manager.updateTask(subtaskNew2);

        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        // проверяем удаление таск, сабтаски, эпика
        System.out.println("проверяем removeById для эпика");
        manager.removeById(5);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("проверяем removeById для сабтаски");
        manager.removeById(12);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("проверяем removeById для таски");
        manager.removeById(1);
        manager.printAllTasks();  // распечатаем таски

        System.out.println("проверяем обновление статуса для эпика'");
        subtask5.setStatus(TaskStatus.IN_PROGRESS); // меняем статус одной из сабтаск на IN_PROGRESS
        manager.updateEpicStatus(6);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски
        subtask5.setStatus(TaskStatus.NEW); // меняем статус сабтаски снова на New
        manager.updateEpicStatus(6);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        subtask5.setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE для эпика с bl= 6
        subtask6.setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE  для эпика с bl= 6
        subtask7.setStatus(TaskStatus.DONE); // меняем статус сабтаски  на DONE  для эпика с bl= 6
        manager.updateEpicStatus(6);
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("проверяем удаление всех сабтаск'");
        manager.removeAllSubTasks();
        manager.printAllEpics();  // распечатаем эпики
        manager.printAllSubTasks(); // распечатаем сабтаски

        System.out.println("проверяем удаление всех таск'");
        manager.removeAllTasks();
        manager.printAllTasks();  // распечатаем таски
    }
}
