package service;

import exceptions.ManagerSaveException;
import history.HistoryManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CSVTaskFormatter {

    // Метод возвращает строковое представление задачи для csv
    public static String taskToString(Task task) {
        return task.toStringWithoutFieldNames();
    }

    // Метод возвращает в строчном формате содержимое всех мап с задачами и историю задач
    public static String tasksAndHistoryToString(Map<Integer, Task> tasks, Map<Integer,
            Subtask> subtasks, Map<Integer, Epic> epics, HistoryManager historyManager) {

        StringBuilder builder = new StringBuilder("id,type,name,status,description,epic,subtasks");
        tasks.values().forEach(t -> builder.append(taskToString(t))); // Добавили все таски в StringBuilder
        epics.values().forEach(e -> builder.append(taskToString(e))); // Добавили все эпики в StringBuilder
        subtasks.values().forEach(s -> builder.append(taskToString(s))); // Добавили сабтаски в StringBuilder
        builder.append("\n" + historyToString(historyManager));
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
        try {
            value = value.replaceAll("\n|\r", "");
            // Делим строку на 2 части, 1я-общая инфа от задаче, 2я-сабтаски эпика(если есть).2ю часть далее отбрасываем
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
                default:
                    throw new ManagerSaveException("Ошибка в методе taskFromString");
            }
            taskToReturn.setTaskId(Integer.parseInt(splittedFields[0]));
            taskToReturn.setStatusFromString(splittedFields[3]);
            return taskToReturn;
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка в методе taskFromString");
        }
    }

    // Метод возвращает историю в виде списка задач из истории в строчном формате
    public static List<Task> historyFromString(String[] history) {
        try {
            ArrayList<Task> historyFromCSV = new ArrayList<>();
            for (String tasksInHistory : history) {
                historyFromCSV.add(taskFromString(tasksInHistory));
            }
            return historyFromCSV;
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка в методе historyFromString");
        }
    }
}
