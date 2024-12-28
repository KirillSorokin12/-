import java.io.*;
        import java.net.*;
        import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 13405);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String userInput;
            while (true) {
                System.out.print("Введите сообщение (или 'exit' для выхода): ");
                userInput = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                out.println(userInput);
                System.out.println("Ответ от сервера: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
