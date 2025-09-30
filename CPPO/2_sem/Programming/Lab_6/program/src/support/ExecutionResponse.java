package support;

/**
 * Результат выполнения команды
 */
public class ExecutionResponse {
    private final boolean success;
    private final String message;

    /**
     * Конструктор результата выполнения команды
     * @param success флаг успешности выполнения
     * @param message сообщение с результатом
     */
    public ExecutionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Проверяет, успешно ли выполнена команда
     * @return true, если команда выполнена успешно
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Возвращает сообщение с результатом выполнения
     * @return сообщение
     */
    public String getMessage() {
        return message;
    }
}