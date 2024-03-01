
# java-kanban
Repository for homework project.

## Финальный проект спринта 4,5

### Краткое описание задачи:
Написать бэкенд для трекера задач (Задача, Подзадача, Эпик).

### Список файлов:

| Пакет        |Файл | Описание                                                                     |
|--------------|--|------------------------------------------------------------------------------|
| -            |README.md  | описание проекта                                                             |
| -            |Main.java | -                                                                            || enums        |TaskStatus.java | Список возможных статусов задач                                              |
| manager      |TaskManager.java | Интерфейс менеджеров задач                                                   |
| manager      |InMemoryTaskManager.java | реализация TaskManager для менеджера, хранящего задачи в оперативной памяти  |
| manager      |CSVTaskFormatter.java | реализация TaskManager для менеджера, хранящего задачи в оперативной памяти  |
| objects      | Task.java | Класс задач с типом "Задача"                                                 |
| objects      |Subtask.java  | Класс задач с типом "Подзадач"                                               |
| objects      |objects.Epic.java | Класс задач с типом "Эпик"                                                   |
| service      |Managers.java  | утилитарный класс для создания менеджеров задач и истории                    |
| history      |HistoryManager.java | Интерфейс менеджеров истории просмотров задач                                |
| history      |InMemoryHistoryManager.java | Реализация HistoryManager - хранение истории просмотров в оперативной памяти |
| test.manager |InMemoryTaskManagerTest.java | Данные и тесты для проверки работы менеджера задач     |
| test.service |ManagersTest.java | Данные и тесты для проверки менеджера истории          |
| test         |TestDataAndMethods.java | Данные и тесты для проверки проекта (для запуска вручную)                    |



### Автор:
Юлия А.