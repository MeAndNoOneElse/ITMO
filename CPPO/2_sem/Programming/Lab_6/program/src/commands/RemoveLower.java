package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class RemoveLower extends Command {
    private CollectionManager collectionManager;
    private Console console;

    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower", "(цена) удалить из коллекции все элементы, у которых цена меньше, чем заданная", false, 2, 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        int price = Integer.parseInt(text[1]);
        int initialSize = collectionManager.getCollection().size();
        collectionManager.removeLower(price);
        int removedCount = initialSize - collectionManager.getCollection().size();
        return new ExecutionResponse(true, "Удалено элементов: " + removedCount);
    }
}
