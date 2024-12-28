import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQLConnect {
    private static final String URL = "jdbc:postgresql://localhost:5432/test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "OOP1234";

    public static void main(String[] args) {
        try {
            // Подключение к базе данных
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Подключение установлено!");

            // Создание объекта Statement для выполнения SQL-запросов
            Statement statement = connection.createStatement();

            // Выполнение запроса и получение результата
            ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table_name");

            // Обработка результата
            while (resultSet.next()) {
                System.out.println(resultSet.getString("your_column_name"));
            }

            // Закрытие ресурсов
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
            e.printStackTrace();
        }
    }
}
