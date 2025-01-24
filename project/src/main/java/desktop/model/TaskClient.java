package desktop.model;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TaskClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            String userInput;

            while (true) {
                System.out.println("Введите команду (add/list/remove [id] или exit):");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                out.println(userInput); // Отправка команды серверу
                System.out.println("Сервер: " + in.readLine()); // Получение ответа от сервера
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
