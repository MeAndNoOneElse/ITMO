package support;

public class DopConsole implements Console {
    @Override
    public void println(String message) {
        // Ничего не выводит — серверная версия
    }

    @Override
    public String readln() {
        return null; // Не используется на сервере
    }
}
