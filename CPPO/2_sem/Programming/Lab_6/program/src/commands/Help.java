package commands;

import managers.CommandManager;
import support.Console;
import support.ExecutionResponse;

public class Help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "справка", false, 1, 0);
        this.console = console;
        this.commandManager = commandManager;
    }

    public ExecutionResponse execute(String[] text) {
        String start = "Ожидается такой формат: \"команда аргумент аргумент\" возможные аргументы указаны в скобках.\n" +
                "Созданы следующие команды:\n";
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        commandManager.getCommandMap().forEach((name, command) ->
                sb.append(String.format("%s: %s%n", name, command.getDescription())));
        return new ExecutionResponse(true, sb.toString());

    }
}
