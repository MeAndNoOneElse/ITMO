package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class RemoveById extends Command {
    private CollectionManager collectionManager;
    private Console console;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id", "(id) удалить элемент по id", false, 2, 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        long id = Long.parseLong(text[1]);
        if (collectionManager.getById(id) == null)
            return new ExecutionResponse(false, "Элемент с id " + id + " не найден");
        collectionManager.removeById(id);
        return new ExecutionResponse(true, "Элемент с id " + id + " успешно удален");
    }
}
