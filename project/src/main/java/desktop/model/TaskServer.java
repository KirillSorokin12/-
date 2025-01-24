package desktop.model;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskServer {
    private List<Task> tasks; // Хранение задач

    public TaskServer() {
        tasks = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start(); // Обработка каждого клиента в отдельном потоке
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Обработка команды клиента
                    String response = handleClientRequest(inputLine);
                    out.println(response); // Отправка ответа клиенту
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String handleClientRequest(String request) {
            String[] parts = request.split(" ");
            String command = parts[0];
            String response = "";

            switch (command.toLowerCase()) {
                case "add":
                    // Логика для добавления задачи
                    String taskName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    tasks.add(new Task(taskName));
                    response = "Задача добавлена: " + taskName;
                    break;
                case "list":
                    // Логика для отображения всех задач
                    response = "Список задач:\n" + tasks.stream()
                            .map(Task::toString)
                            .collect(Collectors.joining("\n"));
                    break;
                case "remove":
                    // Логика для удаления задачи по ID
                    int taskId = Integer.parseInt(parts[1]);
                    if (taskId < tasks.size()) {
                        Task removedTask = tasks.remove(taskId);
                        response = "Задача удалена: " + removedTask;
                    } else {
                        response = "Задача с таким ID не найдена.";
                    }
                    break;
                default:
                    response = "Неизвестная команда.";
            }
            return response;
        }
    }
}