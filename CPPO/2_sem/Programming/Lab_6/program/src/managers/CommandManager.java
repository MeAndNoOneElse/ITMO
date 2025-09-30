package managers;

import commands.*;
import support.Console;
import support.ExecutionResponse;
import support.TicketAsker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Anufriev
 * класс для создания экземпляров команд
 */
public class CommandManager {
    public  final HashMap<String, Command> CommandMap = new HashMap<>();

    private final Console console;

    public CommandManager(CollectionManager collectionManager, Console console) {
        this.console = console;
        TicketAsker asker = new TicketAsker(console);
        CommandMap.put("help", new Help(console, this));
        CommandMap.put("exit", new Exit(console));
        CommandMap.put("add", new Add(console, collectionManager));
        CommandMap.put("info", new Info(console, collectionManager));
        CommandMap.put("add_if_max", new AddIfMax(console, collectionManager));
        CommandMap.put("update_id", new Update(console, collectionManager));
        CommandMap.put("remove_by_id", new RemoveById(console, collectionManager));
        CommandMap.put("clear", new Clear(console, collectionManager));
        CommandMap.put("print_unique_price", new PrintUniquePrice(console, collectionManager));
        CommandMap.put("execute_script", new ExecuteScript(console, this));
        CommandMap.put("remove_first", new RemoveFirst(console, collectionManager));
        CommandMap.put("remove_lower", new RemoveLower(console, collectionManager));
        CommandMap.put("remove_all_by_event", new RemoveAllByEvent(console, collectionManager));
        CommandMap.put("remove_any_by_event", new RemoveAnyByEvent(console, collectionManager));
        CommandMap.put("show",  new Show(console, collectionManager));
    }
    public CommandManager( Console console) {
        this.console = console;
        TicketAsker asker = new TicketAsker(console);
        CommandMap.put("help", new Help(console, this));
        CommandMap.put("exit", new Exit(console));
        CommandMap.put("add", new Add(console, null));
        CommandMap.put("info", new Info(console, null));
        CommandMap.put("add_if_max", new AddIfMax(console, null));
        CommandMap.put("update_id", new Update(console, null));
        CommandMap.put("remove_by_id", new RemoveById(console, null));
        CommandMap.put("clear", new Clear(console, null));
        CommandMap.put("print_unique_price", new PrintUniquePrice(console, null));
        CommandMap.put("execute_script", new ExecuteScript(console, this));
        CommandMap.put("remove_first", new RemoveFirst(console, null));
        CommandMap.put("remove_lower", new RemoveLower(console, null));
        CommandMap.put("remove_all_by_event", new RemoveAllByEvent(console, null));
        CommandMap.put("remove_any_by_event", new RemoveAnyByEvent(console, null));
        CommandMap.put("show",  new Show(console, null));
    }
    public Map<String, Command> getCommandMap() {
        return CommandMap;
    }
    public ExecutionResponse execute(String commandLine) {
        if (commandLine.isEmpty()) return new ExecutionResponse(false, "Команда не введена");
        String[] parts = commandLine.trim().split(" ");
        String commandName = parts[0];
        String[] args = parts.length > 1 ? parts[0].split(" ") : new String[0];

        Command command = CommandMap.get(commandName);
        if (command == null) return new ExecutionResponse(false, "Команда '" + commandName + "' не найдена");
        return command.execute(parts);
    }
//    public CommandManager1() {
//        this.console = null;
//        CommandMap.put("help", new Help(console, this));
//        CommandMap.put("exit", new Exit(console));
//        CommandMap.put("add", new Add(console, null));
//        CommandMap.put("info", new Info(console, null));
//        CommandMap.put("add_if_max", new AddIfMax(console, null));
//        CommandMap.put("update_id", new Update(console, null));
//        CommandMap.put("remove_by_id", new RemoveById(console, null));
//        CommandMap.put("clear", new Clear(console, null));
//        CommandMap.put("print_unique_price", new PrintUniquePrice(console, null));
//        CommandMap.put("execute_script", new ExecuteScript(console, this));
//        CommandMap.put("remove_first", new RemoveFirst(console, null));
//        CommandMap.put("remove_lower", new RemoveLower(console, null));
//        CommandMap.put("remove_all_by_event", new RemoveAllByEvent(console, null));
//        CommandMap.put("remove_any_by_event", new RemoveAnyByEvent(console, null));
//        CommandMap.put("show", new Show(console, null));
//
//    }
}
