package desktop.view;

import desktop.controller.TaskController;
import desktop.model.Task;
import desktop.model.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StartButtonWindow extends JFrame {
    String username;
    private TaskController taskController;
    private DefaultListModel<String> taskListModel;

    public StartButtonWindow() {
        taskListModel = new DefaultListModel<>();
        taskController = new TaskController(this);
        setTitle("Daily Task Book");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton registrationButton = new JButton("Регистрация");
        registrationButton.addActionListener(e -> registrationWindow());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1; // Переместим вниз для второй кнопки
        panel.add(registrationButton, gbc); // Добавляем регистрацию кнопки на ту же панель

        add(panel); // Добавляем панель на фрейм
        setVisible(true); // Включаем видимость фрейма
    }
    private void registrationWindow() {
        JFrame registrationFrame = new JFrame("Регистрация");
        registrationFrame.setSize(400, 300);
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registrationFrame.setLocationRelativeTo(null);

        // Создаем панель для входных данных
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Поля для ввода
        JLabel usernameLabel = new JLabel("Имя пользователя:");
        JTextField usernameField = new JTextField();

        // Кнопка регистрации
        JButton registerButton = new JButton("Зарегистрироваться");
        registerButton.addActionListener(e -> {
            username = usernameField.getText();

            // Здесь можно выполнять действия, например, проверка, сохранение и т.д.
            if (!username.isEmpty()) {
                JOptionPane.showMessageDialog(registrationFrame, "Регистрация успешна!\nПользователь: " + username);
                TaskManager.addUser(username);
                registrationFrame.dispose();  // Закрыть окно регистрации
                showTaskSelectionWindow();
                // Можно также обновить основное окно после регистрации, если нужно
                // Например, перезагрузить основной интерфейс или показать сообщение
            } else {
                JOptionPane.showMessageDialog(registrationFrame, "Пожалуйста, заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Добавляем элементы на панель
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(registerButton);

        registrationFrame.add(panel); // Добавляем панель на фрейм
        registrationFrame.setVisible(true); // Показываем окно регистрации
    }


    private void showTaskSelectionWindow() {
        JFrame selectTask = new JFrame("Select Task");
        selectTask.setSize(800, 600);
        selectTask.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        selectTask.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 8));
        JButton addPersonalTasks = new JButton("Добавить свои задачи");
        JButton currentTasks = new JButton("Текущие задачи");
        JButton addSharedTasks = new JButton("Добавить совместные задачи");
        JButton sharedTasks = new JButton("Совместные задачи");

        buttonPanel.add(addPersonalTasks);
        buttonPanel.add(currentTasks);
        buttonPanel.add(addSharedTasks);
        buttonPanel.add(sharedTasks);

        currentTasks.addActionListener(e -> showCurrentTasks());

        addSharedTasks.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Введите заголовок задачи");
            String description = JOptionPane.showInputDialog("Введите описание задачи");
            if (title != null && description != null) {
                try {
                    taskController.addTask(title, description);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    TaskManager.addTask(TaskManager.getUserIdByLogin(username),description, 1);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addPersonalTasks.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Введите заголовок задачи");
            String description = JOptionPane.showInputDialog("Введите описание задачи");
            if (title != null && description != null) {
                try {
                    taskController.addTask(title, description);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        selectTask.add(buttonPanel);
        selectTask.setVisible(true);
    }

//    public void refreshTaskList() {
//        List<Task> tasks = taskController.getTasks(); // Получаем задачи
//        taskListModel.clear();
//        for (Task task : tasks) {
//            taskListModel.addElement(task.getTitle() + ": " + task.getDescription());
//        }
//    }

    private void showCurrentTasks() {
        JFrame tasksFrame = new JFrame("Текущие задачи");
        tasksFrame.setSize(700, 500);

        tasksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tasksFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);


        tasksFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);


        try {
            TaskManager.readTasks(TaskManager.getUserIdByLogin(username), false, textArea);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tasksFrame.setVisible(true);
    }


    public String getUsername(){
        return username;
    }
}