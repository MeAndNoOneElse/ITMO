package commands;

import support.Console;
import support.ExecutionResponse;

public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "завершение работы(без сохранения в файл)", false, 1, 0);
        this.console = console;
    }

    public ExecutionResponse execute(String[] text) {
        return new ExecutionResponse(false, "Программа завершена");
    }
}
