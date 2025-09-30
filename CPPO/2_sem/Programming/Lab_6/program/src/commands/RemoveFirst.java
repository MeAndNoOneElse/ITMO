package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class RemoveFirst extends Command {
    private CollectionManager collectionManager;
    private Console console;

    public RemoveFirst(Console console, CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции", false, 1, 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(false, "Коллекция пуста");
        collectionManager.getCollection().remove(0);
        return new ExecutionResponse(true, "Первый элемент успешно удален");
    }
}
