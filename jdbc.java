package jdbc;

import java.sql.*;
import java.util.Scanner;

public class jdbc {
    public static void main(String[] args) {
        Scanner obj = new Scanner(System.in); // введи логин
        String login = obj.nextLine();
        Connection con = connect_to_db("oop","postgres","OOP1234");
        addUser(con, login); // добавить текущего пользователя
        addTask(con, getUserIdByLogin(con, login), "message", 1); // добавить задачу
        readTasks(con, getUserIdByLogin(con, login), false); // задачи текущего юзера
        readTasks(con, getUserIdByLogin(con, login), true); // совместные задачи
        // deleteTask(con, getUserIdByLogin(con, login)); // удалить все записи по логину
        // updateRow // хз
    }
    public static Connection connect_to_db(String dbname, String user, String pass)
    {
        Connection con_obj=null;
        String url="jdbc:postgresql://localhost:5432/";

        try
        {
            con_obj= DriverManager.getConnection(url+dbname,user,pass);
            if(con_obj!=null)
            {
                System.out.println("Connection established successfully !");
            }
            else
            {
                System.out.println("Connection failed !!");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return con_obj;
    }

    public static void addUser(Connection con,String login)
    {
        try {
            String check = "SELECT EXISTS (SELECT 1 FROM public.users WHERE login = ?)";
            PreparedStatement Check = con.prepareStatement(check);
            Check.setString(1, login);
            ResultSet rs = Check.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getBoolean(1);
                if (exists) {
                    System.out.println("Вы успешно зашли");
                } else {
                    // Добавление нового пользователя
                    String insert = "INSERT INTO public.users (login) VALUES (?)";
                    PreparedStatement Insert = con.prepareStatement(insert);
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

    public static void addTask(Connection con, int user_id, String message, int importance) {
        String sql = "INSERT INTO messages (user_id, message, importance, together) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            pstmt.setString(2, message);
            pstmt.setInt(3, importance);
            pstmt.setBoolean(4, together);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Задание успешно добавлено!");
            } else {
                System.out.println("Не удалось добавить задание.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении задания: " + e.getMessage());
        }
    }

    public static int getUserIdByLogin(Connection con, String login) {
        String query = "SELECT id FROM public.users WHERE login = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
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

    public static void deleteTask(Connection con, int id)
    {
        Statement stmt;

        try {
            String query="DELETE FROM public.messages WHERE user_id = " + id;
            stmt=con.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Задачи от пользователя были удалены");

        }
        catch (Exception e)
        {
            System.out.println("Exception caught in deleteTable");
        }
    }

    public static void readTasks(Connection con, Integer userId, boolean together) {
        Statement stmt;
        ResultSet rs;

        try {
            // Создаем объект Statement для выполнения запросов
            stmt = con.createStatement();

            // Формируем SQL-запрос в зависимости от переданных параметров
            String query;
            if (userId != null && !together) {
                // Если передан userId и together=false, выводим только сообщения текущего пользователя
                query = "SELECT users.login, messages.message "
                        + "FROM users "
                        + "INNER JOIN messages ON users.id = messages.user_id "
                        + "WHERE users.id = " + userId;
            } else {
                // В остальных случаях выводим все сообщения всех пользователей
                query = "SELECT users.login, messages.message "
                        + "FROM users "
                        + "INNER JOIN messages ON users.id = messages.user_id";
            }

            // Выполняем запрос и получаем результат
            rs = stmt.executeQuery(query);

            // Проходим по всем строкам результата и выводим логин и сообщение
            while (rs.next()) {
                String login = rs.getString("login");
                String message = rs.getString("message");

                System.out.println("Login: " + login + ", Message: " + message);
            }

            // Закрываем объекты ResultSet и Statement после использования
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void updateRow(Connection con, int id ,String new_message)
    {
        Statement stmt;

        try {
            stmt=con.createStatement();
            String query=String.format("update %s set name = '%s' where rollno=%d;");
            stmt.executeUpdate(query);
            System.out.println("Row updated Successfully!");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
