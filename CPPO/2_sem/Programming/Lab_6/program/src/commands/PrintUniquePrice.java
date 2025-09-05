package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class PrintUniquePrice extends Command {
    Console console;
    CollectionManager collectionManager;

    public PrintUniquePrice(Console console, CollectionManager collectionManager) {
        super("print_unique_price", "вывести уникальные цены", false, 1, 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        String result = collectionManager.getUniquePricesAsString();
        return new ExecutionResponse(true, result);
    }
}
