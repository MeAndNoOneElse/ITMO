package support;

import managers.CommandManager;

/**
 * Класс для запуска интерактивного режима программы, обрабатывающий команды пользователя через {@link CommandManager}.
 * Предоставляет консольный интерфейс для взаимодействия с коллекцией.
 */
public class Runner {
    private final CommandManager commandManager;
    private final Console console;
    private boolean receivedEOF = false;
    private volatile boolean normalExit = false;

    /**
     * Создаёт новый экземпляр {@link Runner} с указанными менеджером команд и консолью.
     * Настраивает обработку прерывания программы (Ctrl+C) для вывода сообщения о несохранённых данных.
     *
     * @param commandManager менеджер команд для обработки пользовательского ввода
     * @param console консоль для вывода сообщений и чтения ввода пользователя
     */
    public Runner(CommandManager commandManager, Console console) {
        this.commandManager = commandManager;
        this.console = console;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!normalExit) {
                console.println("\nПрограмма прервана (Ctrl+C). Данные не сохранены.");
            }
        }));
    }

    /**
     * Запускает интерактивный режим программы, выводя инструкции и ожидая ввода команд от пользователя.
     * Завершает работу при выполнении команды "exit" или при возникновении ошибки.
     */
    public void interactiveMode() {
        console.println("Добро пожаловать в программу управления коллекцией!");
        console.println("инструкции по управлению в терминале:");
        console.println("- Ctrl+D: Сигнал конца ввода. Программа выведет сообщение и продолжит работу.");
        console.println("- Ctrl+C: Немедленно прерывает программу без сохранения данных.");
        console.println("- Ctrl+Z: Приостанавливает программу и переводит её в фон. Для возобновления введите 'fg'.");
        console.println("- Ctrl+S: Приостанавливает вывод в терминал. Нажмите Ctrl+Q для возобновления.");
        console.println("Введите команду (help для списка команд):");

        while (true) {
            String input = console.readln();
            if (input == null) {
                if (!receivedEOF) {
                    console.println("Для выхода из программы используйте команду exit.");
                    receivedEOF = true;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    console.println("Ошибка: " + e.getMessage());
                }
                continue;
            }
            receivedEOF = false;
            try {
                ExecutionResponse response = commandManager.execute(input);
                console.println(response.getMessage());
                if (!response.isSuccess() && input.equals("exit")) {
                    normalExit = true;
                    break;
                }
            } catch (Exception e) {
                console.println("Произошла ошибка: " + e.getMessage());
                console.println("Введите команду (help для списка команд):");
            }
            console.println("Введите команду (help для списка команд):");
        }
    }
}