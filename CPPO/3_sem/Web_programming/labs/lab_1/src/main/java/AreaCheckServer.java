import com.fastcgi.FCGIInterface;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AreaCheckServer {

    public static void main(String[] args) {
        System.err.println("=================================");
        System.err.println("FastCGI сервер запускается...");
        System.err.println("=================================");

        new AreaCheckServer().start();
    }

    public void start() {
        FCGIInterface fcgi = new FCGIInterface();
        int requestNumber = 0;

        System.err.println("FastCGI сервер готов принимать запросы");

        while (fcgi.FCGIaccept() >= 0) {
            requestNumber++;
            System.err.println("\n--- Запрос #" + requestNumber + " ---");

            try {
                // Устанавливаем кодировку для вывода
                System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

                // Создаем обработчик и передаем ему потоки
                RequestHandler handler = new RequestHandler(System.in, System.out);

                // Обрабатываем запрос
                handler.handle();

            } catch (Exception e) {
                System.err.println("[FATAL] Критическая ошибка при обработке запроса: " + e.getMessage());
                e.printStackTrace();

                // Отправляем клиенту ошибку 500
                try {
                    System.out.print("Status: 500 Internal Server Error\r\n");
                    System.out.print("Content-Type: application/json; charset=utf-8\r\n");
                    System.out.print("\r\n");
                    System.out.print("{\"success\":false,\"error\":\"Внутренняя ошибка сервера\"}");
                    System.out.flush();
                } catch (Exception ignored) {
                    // Если даже отправка ошибки не удалась, просто логируем
                    System.err.println("[FATAL] Не удалось отправить ответ об ошибке");
                }
            }
        }

        System.err.println("\nFastCGI сервер завершил работу");
    }
}