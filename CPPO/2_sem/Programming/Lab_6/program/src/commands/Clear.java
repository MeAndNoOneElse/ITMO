package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;


/**
 * @author Andrey Anufriev
 * класс для команды clear
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", false, 1, 0);
        this.collectionManager = collectionManager;
        this.console = console;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        collectionManager.clear();
        return new ExecutionResponse(true, "Коллекция успешно очищена");

    }
}
