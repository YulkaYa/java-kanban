package history;

import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();

    private class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> prev;

        private Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Указатель на первый элемент списка. Он же first
     */
    private Node<Task> first;

    /**
     * Указатель на последний элемент списка. Он же last
     */
    private Node<Task> last;

    private int size = 0;

    // Метод позволяет добавлять задачу в конец списка истории просмотров задач
    private void linkLast(Task task) {
        // Реализуйте метод
        final Node<Task> oldLast = last;

        final Node<Task> newNode = new Node<>(oldLast, task, null);
        last = newNode;
        if (oldLast == null)
            first = newNode;
        else
            oldLast.next = newNode;
        size++;
    }

    private void removeNode(Node node) {
        Node<Task> oldPrev = node.prev;
        Node<Task> oldNext = node.next;
        if (!(oldNext == null && oldPrev == null)) {
            if (oldPrev == null) {
                oldNext.prev = null;
                first = oldNext;
            } else {
                oldPrev.next = oldNext;
            }

            if (oldNext == null) {
                oldPrev.next = null;
                last = oldPrev;
            } else {
                oldNext.prev = oldPrev;
            }
            node.data = null;
            size--;
        } else {
            node.data = null;
            first = null;
            last = null;
            size--;
        }
    }

    // Метод позволяет собирать все задачи из истории в обычный ArrayList
    private List<Task> getTasks() {
        final List<Task> history = new ArrayList<>();
        Node<Task> node = first;
        for (int i = 0; i < size; i++) {
            history.add(node.data);
            node = node.next;
        }
        return history;
    }

    @Override
    public void add(Task task) {

        // Если в истории уже есть запись с id, то удаляем и перезаписываем ее
        if (historyMap.containsKey(task.getTaskId())) {
            Node<Task> nodeTask = historyMap.get(task.getTaskId());
            removeNode(nodeTask);
        }
        linkLast(task);
        historyMap.put(task.getTaskId(), last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = historyMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            historyMap.remove(id);
        }
    }
}
