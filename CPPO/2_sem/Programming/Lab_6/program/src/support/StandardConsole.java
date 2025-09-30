package support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Стандартная реализация консоли для ввода-вывода через System.in и System.out.
 * Поддерживает чтение из файла при выполнении скрипта.
 */
public class StandardConsole implements Console {
    private BufferedReader reader;
    private Scanner scriptScanner;

    public StandardConsole() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.scriptScanner = null; // По умолчанию читаем с клавиатуры
    }

    /**
     * Устанавливает источник ввода для скрипта.
     * @param scanner сканнер для чтения из файла скрипта
     */
    public void setScriptScanner(Scanner scanner) {
        this.scriptScanner = scanner;
    }

    /**
     * Сбрасывает источник ввода на клавиатуру.
     */
    public void clearScriptScanner() {
        this.scriptScanner = null;
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String readln() {
        try {
            if (scriptScanner != null && scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                println("> " + line); // Эхо ввода для отладки
                return line;
            } else {
                String line = reader.readLine();
                return line != null ? line.trim() : null;
            }
        } catch (Exception e) {
            println("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }

    /**
     * Проверяет, выполняется ли сейчас скрипт.
     * @return true, если читаем из скрипта
     */
    public boolean isScriptMode() {
        return scriptScanner != null;
    }
}