package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    public void setUp() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(tempFile);
    }
}
