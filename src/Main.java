import enums.TaskStatus;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
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
        Epic epic5 = new Epic("эпик5", "эпик5q дескрипшн");
        int epic1Id = manager.createEpic(epic1);
        int epic2Id = manager.createEpic(epic2);
        int epic3Id = manager.createEpic(epic3);
        int epic4Id = manager.createEpic(epic4);
        int epic5Id = manager.createEpic(epic5);
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
        subtask7.setStatus(TaskStatus.IN_PROGRESS);
manager.updateTask(subtask7);
        manager.printAllEpics();
        manager.printAllTasks();
        manager.printAllSubTasks();
        System.out.println("------------");
        subtask4.setStatus(TaskStatus.DONE);
        subtask5.setStatus(TaskStatus.DONE);
        subtask6.setStatus(TaskStatus.DONE);
        subtask7.setStatus(TaskStatus.DONE);

        manager.updateTask(subtask7);
        manager.printAllEpics();
        manager.printAllTasks();
        manager.printAllSubTasks();

        System.out.println("------------");
        manager.printAllEpics();
        manager.printAllTasks();
        manager.printAllSubTasks();
        System.out.println("------------");
        System.out.println("------------");
        manager.printAllSubTasks();
        subtask6.setDescription("dfsdfsdfsdfsdfsdfsdfsfsdfsfsdfsdf");
        manager.updateTask(subtask6);
        manager.printAllSubTasks();
        System.out.println(subtask7);
        System.out.println(manager.getHistory());


    }

}
