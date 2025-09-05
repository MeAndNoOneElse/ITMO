package support;

/**
 * Интерфейс для взаимодействия с консолью.
 */
public interface Console {
    /**
     * Выводит сообщение в консоль.
     * @param message сообщение для вывода
     */
    void println(String message);

    /**
     * Считывает строку из консоли.
     * @return введённая строка или null при конце ввода
     */
    String readln();
}