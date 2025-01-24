package desktop.controller;

import desktop.model.Task;
import desktop.model.TaskManager;
import desktop.view.StartButtonWindow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
//    private List<Task> tasks;
    private TaskManager taskManager;
    private StartButtonWindow startButtonWindow;

    public TaskController(StartButtonWindow startButtonWindow) {
        this.startButtonWindow = startButtonWindow;
        this.taskManager = new TaskManager();
    }

    public void addTask(String title, String description) throws SQLException {
        Task task = new Task(title, description);
        taskManager.addTask(TaskManager.getUserIdByLogin(startButtonWindow.getUsername()), description, 1);
//        startButtonWindow.refreshTaskList();
    }
//    public List<Task> getTasks() {
//        return tasks;
//    }
}