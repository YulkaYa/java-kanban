package service;

import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CSVTaskFormatter {

    // Метод возвращает строковое представление задачи для csv
    public static String taskToString(Task task) {
        return task.toStringWithoutFieldNames();
    }

    // Метод возвращает в строчном формате содержимое всех мап с задачами и историю задач
    public static String tasksAndHistoryToString(HashMap<Integer, Task> tasks, HashMap<Integer, Subtask> subtasks,
                                                 HashMap<Integer, Epic> epics,
                                                 HistoryManager historyManager) {
        StringBuilder builder = new StringBuilder("id,type,name,status,description,epic,subtasks");
        tasks.values().forEach(t -> builder.append(taskToString(t))); // Добавили все таски в StringBuilder
        epics.values().forEach(e -> builder.append(taskToString(e))); // Добавили все эпики в StringBuilder
        subtasks.values().forEach(s -> builder.append(taskToString(s))); // Добавили  сабтаски в StringBuilder
        builder.append("\n" + historyToString(historyManager)); //
        return builder.toString();
    }

    // Метод возвращает содержимое истории задач в csv-формате
    static String historyToString(HistoryManager historyManager) {
        StringBuilder builder = new StringBuilder();
        historyManager.getHistory().forEach(t -> builder.append(taskToString(t)));
        return builder.toString();
    }

    // Метод возвращает объект Task с помощью преобразования строки с данными о задаче
    public static Task taskFromString(String value) {
        value = value.replaceAll("\n|\r", "");
        // Делим строку на 2 части, 1я-общая инфа от задаче, 2я-сабтаски эпика(если есть).2-ю часть далее отбрасываем,
        // т.к. восстанавливать сабтаски будем на основе epicId  в самих сабтасках
        String[] stringsToCreateTask = value.split("\\[");
        String[] splittedFields = stringsToCreateTask[0].split(",");

        String typeInUpperCase = splittedFields[1];
        Task taskToReturn = null;
        switch (typeInUpperCase) {
            case ("EPIC"): {
                taskToReturn = new Epic(splittedFields[2], splittedFields[4]);
                break;
            }
            case ("TASK"): {
                taskToReturn = new Task(splittedFields[2], splittedFields[4]);
                break;
            }
            case ("SUBTASK"): {
                taskToReturn = new Subtask(splittedFields[2], splittedFields[4]);
                ((Subtask) taskToReturn).setEpicId(Integer.parseInt(splittedFields[5]));
                break;
            }
            default: break;
        }
        taskToReturn.setTaskId(Integer.parseInt(splittedFields[0]));
        taskToReturn.setStatusFromString(splittedFields[3]);
        return taskToReturn;
    }

    // Метод возвращает историю в виде списка задач из истории в строчном формате
    public static List<Task> historyFromString(String[] history) {
        ArrayList<Task> historyFromCSV = new ArrayList<>();
        for (String tasksInHistory : history) {
            historyFromCSV.add(taskFromString(tasksInHistory));
        }
        return historyFromCSV;
    }
}