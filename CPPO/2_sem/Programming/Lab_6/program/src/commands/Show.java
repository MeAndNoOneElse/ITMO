package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class Show extends Command {
    CollectionManager collectionManager;
    Console console;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "выводит все элементы коллекции", false, 1, 0);

        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        String result = collectionManager.getCollection().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining("\n"));
        return new ExecutionResponse(true, result);
    }
}
