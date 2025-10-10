import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestHandler {
    private final InputStream input;
    private final PrintStream output;

    public RequestHandler(InputStream input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    public void handle() {
        try {
            long startTime = System.nanoTime();

            // Проверяем метод запроса
            String requestMethod = System.getProperty("REQUEST_METHOD");
            if (requestMethod == null || !requestMethod.equals("POST")) {
                sendErrorResponse(405, "Method Not Allowed", "Только POST запросы разрешены");
                return;
            }

            // Читаем CONTENT_LENGTH
            String contentLengthStr = System.getProperty("CONTENT_LENGTH");
            int contentLength = 0;
            if (contentLengthStr != null && !contentLengthStr.isEmpty()) {
                try {
                    contentLength = Integer.parseInt(contentLengthStr);
                } catch (NumberFormatException e) {
                    sendErrorResponse(400, "Bad Request", "Некорректный Content-Length");
                    return;
                }
            }

            if (contentLength == 0) {
                sendErrorResponse(400, "Bad Request", "Пустое тело запроса");
                return;
            }

            // Парсим параметры
            Map<String, String> params = parseParams(input, contentLength);
            String xStr = params.get("x");
            String yStr = params.get("y");
            String rStr = params.get("r");

            // Проверяем наличие всех параметров
            if (xStr == null || yStr == null || rStr == null) {
                sendErrorResponse(400, "Bad Request", "Отсутствуют обязательные параметры (x, y, r)");
                return;
            }

            // Валидация
            if (!isValid(xStr, yStr, rStr)) {
                sendErrorResponse(400, "Bad Request", "Некорректные значения параметров");
                return;
            }

            // Вычисления
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            double r = Double.parseDouble(rStr);

            boolean inside = checkHit(x, y, r);
            String result = inside ? "Попадание" : "Промах";

            long execTime = (System.nanoTime() - startTime) / 1_000_000;
            String time = new Date().toString();

            sendSuccessResponse(xStr, yStr, rStr, result, time, execTime);

        } catch (IOException e) {
            logError("IOException при обработке запроса: " + e.getMessage());
            sendErrorResponse(500, "Internal Server Error", "Ошибка при чтении данных");
        } catch (NumberFormatException e) {
            logError("NumberFormatException: " + e.getMessage());
            sendErrorResponse(400, "Bad Request", "Некорректный формат чисел");
        } catch (Exception e) {
            logError("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(500, "Internal Server Error", "Внутренняя ошибка сервера");
        }
    }

    private Map<String, String> parseParams(InputStream in, int contentLength) throws IOException {
        Map<String, String> map = new HashMap<>();

        if (contentLength > 0) {
            byte[] buffer = new byte[contentLength];
            int totalRead = 0;
            while (totalRead < contentLength) {
                int bytesRead = in.read(buffer, totalRead, contentLength - totalRead);
                if (bytesRead == -1) break;
                totalRead += bytesRead;
            }

            String body = new String(buffer, 0, totalRead, "UTF-8");
            logInfo("Получено тело запроса: " + body);

            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    map.put(kv[0], java.net.URLDecoder.decode(kv[1], "UTF-8"));
                }
            }
        }

        return map;
    }

    private boolean isValid(String x, String y, String r) {
        try {
            double xd = Double.parseDouble(x);
            double yd = Double.parseDouble(y);
            double rd = Double.parseDouble(r);

            // Проверка диапазонов согласно заданию
            if (xd < -4 || xd > 4) {
                logInfo("Валидация провалена: X вне диапазона [-4, 4]");
                return false;
            }
            if (yd <= -3 || yd >= 5) {
                logInfo("Валидация провалена: Y вне диапазона (-3, 5)");
                return false;
            }
            if (rd <= 0 || rd > 5) {
                logInfo("Валидация провалена: R вне диапазона (0, 5]");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            logInfo("Валидация провалена: некорректный формат чисел");
            return false;
        }
    }

    private boolean checkHit(double x, double y, double r) {
        // 1. Прямоугольник (нижний левый)
        if (x <= 0 && y <= 0 && x >= -r && y >= -r) {
            return true;
        }

        // 2. Треугольник (нижний правый)
        if (x >= 0 && y <= 0 && y >= -r && x <= r && y >= -x) {
            return true;
        }

        // 3. Четверть круга (верхний левый)
        if (x <= 0 && y >= 0 && (x * x + y * y <= r * r)) {
            return true;
        }

        return false;
    }

    private void sendSuccessResponse(String x, String y, String r,
                                     String result, String time, long execTime) {
        // Формируем JSON как ОДНУ строку
        String json = String.format(
                "{\"x\":\"%s\",\"y\":\"%s\",\"r\":\"%s\",\"result\":\"%s\",\"time\":\"%s\",\"execTimeMs\":%d}",
                escapeJson(x), escapeJson(y), escapeJson(r),
                escapeJson(result), escapeJson(time), execTime
        );

        // Выводим заголовки с правильными переносами
        output.print("Content-Type: application/json\r\n");
        output.print("Content-Length: " + json.getBytes(StandardCharsets.UTF_8).length + "\r\n");
        output.print("\r\n");  // Пустая строка между заголовками и телом

        // Выводим JSON БЕЗ перевода строки
        output.print(json);
        output.flush();
    }



    private void sendErrorResponse(int statusCode, String statusText, String errorMessage) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":false,");
        json.append("\"error\":\"").append(escapeJson(errorMessage)).append("\",");
        json.append("\"statusCode\":").append(statusCode);
        json.append("}");

        output.print("Status: " + statusCode + " " + statusText + "\r\n");
        output.print("Content-Type: application/json; charset=utf-8\r\n");
        output.print("Content-Length: " + json.length() + "\r\n");
        output.print("\r\n");
        output.print(json.toString());
        output.flush();

        logError("Отправлена ошибка " + statusCode + ": " + errorMessage);
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Логирование для отладки
    private void logInfo(String message) {
        System.err.println("[INFO] " + new Date() + " - " + message);
    }

    private void logError(String message) {
        System.err.println("[ERROR] " + new Date() + " - " + message);
    }
}