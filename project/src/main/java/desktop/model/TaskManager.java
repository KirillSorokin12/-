package desktop.model;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private String url = "jdbc:postgresql://localhost:5432/oop";
    private String user = "postgres"; // Имя пользователя
    private String password = "OOP1234"; // Пароль

    public TaskManager() {
        // Инициализация соединения
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String login) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/oop", "postgres", "OOP1234");
            String check = "SELECT EXISTS (SELECT 1 FROM public.users WHERE login = ?)";
            PreparedStatement Check = conn.prepareStatement(check);
            Check.setString(1, login);
            ResultSet rs = Check.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getBoolean(1);
                if (exists) {
                    System.out.println("Вы успешно зашли");
                } else {
                    // Добавление нового пользователя
                    String insert = "INSERT INTO public.users (login) VALUES (?)";
                    PreparedStatement Insert = conn.prepareStatement(insert);
                    Insert.setString(1, login);

                    int rowsAffected = Insert.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Пользователь добавлен");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений
        }
    }

    public static int getUserIdByLogin(String login) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/oop", "postgres", "OOP1234");

        String query = "SELECT id FROM public.users WHERE login = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Если пользователь не найден
    }

    public static void addTask(int user_id, String message, int importance) {
        String sql = "INSERT INTO messages (user_id, message, importance) VALUES (?, ?, ?)";
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/oop", "postgres", "OOP1234");
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, user_id);
                pstmt.setString(2, message);
                pstmt.setInt(3, importance);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Задание успешно добавлено!");
                } else {
                    System.out.println("Не удалось добавить задание.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при добавлении задания: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении задания: " + e.getMessage());
        }
    }

    public static void readTasks(Integer userId, boolean together, JTextArea textArea) throws SQLException {
        Statement stmt;
        ResultSet rs;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/oop", "postgres", "OOP1234");
            // Создаем объект Statement для выполнения запросов
            stmt = conn.createStatement();

            // Формируем SQL-запрос в зависимости от переданных параметров
            String query;
            if (!together) {
                // Если передан userId и together=false, выводим только сообщения текущего пользователя
                query = "SELECT messages.message FROM users INNER JOIN messages ON users.id = messages.user_id WHERE users.id = " + userId;
            } else {
                // В остальных случаях выводим все сообщения всех пользователей
                query = "SELECT messages.message FROM users INNER JOIN messages ON users.id = messages.user_id";
            }

            // Выполняем запрос и получаем результат
            rs = stmt.executeQuery(query);

            // Сброс текста в JTextArea для обновления
            textArea.setText("");

            // Проходим по всем строкам результата и добавляем сообщения в текстовое поле
            while (rs.next()) {
                String message = rs.getString("message");
                textArea.append(message + "\n"); // Добавление сообщения в текстовое поле с переносом строки
            }

            // Закрываем объекты ResultSet и Statement после использования
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close(); // Закрытие соединения с базой данных
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM messages.message";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(rs.getString("message"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}