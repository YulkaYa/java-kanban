
# java-kanban
Repository for homework project.

## Финальный проект спринта 4

### Краткое описание задачи:
Написать бэкенд для трекера задач (Задача, Подзадача, Эпик).

### Список файлов:

| Пакет        |Файл | Описание                                                                     |
|--------------|--|------------------------------------------------------------------------------|
| enums        |TaskStatus.java | Список возможных статусов задач                                              |
| manager      |TaskManager.java | Интерфейс менеджеров задач                                                   |
| manager      |InMemoryTaskManager.java | реализация TaskManager для менеджера, хранящего задачи в оперативной памяти  |
| objects      | Task.java | Класс задач с типом "Задача"                                                 |
| objects      |Subtask.java  | Класс задач с типом "Подзадач"                                               |
| objects      |objects.Epic.java | Класс задач с типом "Эпик"                                                   |
| -            |README.md  | описание проекта                                                             |
| -            |Main.java | -                                                                            |
| service      |Managers.java  | утилитарный класс для создания менеджеров задач и истории                    |
| history      |HistoryManager.java | Интерфейс менеджеров истории просмотров задач                                |
| history      |InMemoryHistoryManager.java | Реализация HistoryManager - хранение истории просмотров в оперативной памяти |
| test.manager |InMemoryTaskManagerTest.java | Данные и тесты для проверки работы менеджера задач     |
| test.manager           |ManagersTest.java | Данные и тесты для проверки менеджера истории          |
| -            |TestDataAndMethods.java | Данные и тесты для проверки проекта (для запуска вручную)                    |



### Автор:
Юлия А.