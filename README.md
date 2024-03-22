
# java-kanban
Repository for homework project.

## Финальный проект спринта 4,5

### Краткое описание задачи:
Написать бэкенд для трекера задач (Задача, Подзадача, Эпик).

### Список файлов:

| Пакет        | Файл                                                     | Описание                                                                                         |
|--------------|----------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| -            | README.md                                                | описание проекта                                                                                 |
| -            | Main.java                                                | -                                                                                                || enums        |TaskStatus.java | Список возможных статусов задач                                              |
| manager      | TaskManager.java                                         | Интерфейс менеджеров задач                                                                       |
| manager      | FileBackedTaskManager.java                               | реализация TaskManager для менеджера, хранящего задачи в файле                                   |
| manager      | InMemoryTaskManager.java                                 | реализация TaskManager для менеджера, хранящего задачи в оперативной памяти                      |
| objects      | Task.java                                                | Класс задач с типом "Задача"                                                                     |
| objects      | Subtask.java                                             | Класс задач с типом "Подзадач"                                                                   |
| objects      | objects.Epic.java                                        | Класс задач с типом "Эпик"                                                                       |
| service      | Managers.java                                            | утилитарный класс для создания менеджеров задач и истории                                        |
| service      | CSVTaskFormatter.java                                    | класс хранит методы для обработки csv файла с задачами                                           |
| history      | HistoryManager.java                                      | Интерфейс менеджеров истории просмотров задач                                                    |
| history      | InMemoryHistoryManager.java                              | Реализация HistoryManager - хранение истории просмотров в оперативной памяти                     |
| test.manager | TaskManagerTest.java                           | Тесты на все методы из TaskManager                                                               |
| test.manager | InMemoryTaskManagerTest.java                             | Создание инстанса  InMemoryTaskManager для каждого метода из TaskManagerTest.java                                 |
| test.manager | FileBackedTaskManagerTest.java.                          | Создание инстанса  FileBackedTaskManager для каждого метода из TaskManagerTest.java     |
| test.manager | FileBackedTaskManagerTestWithLoadingFile.java                           | Данные и тесты для проверки работы c файлами менеджера задач (если файл заранее сгенерирован)                                            |
| test.manager | fileBackedTaskManagerTestWithoutLoadingFileAtStart.java. | Данные и тесты для проверки работы c файлами менеджера задач (если файл заранее НЕ сгенерирован) |
| test.service | ManagersTest.java                                        | Данные и тесты для проверки менеджера истории                                                    |
| enum         | TaskStatus.java                                          | список статусов задач                                                                            |
| enum         | TaskTypes.java                                           | список типов задач                                                                               |
| exceptions   | ManagerSaveException.java                                | исключение, генерируемое при обработке файловым менеджером                                       |
| resources         |                                               | папка resources для сохранения и загрузки файла с задачами                                       |

### Автор:
Юлия А.