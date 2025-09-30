package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;

public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("help", "справка", false, 1, 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        return new ExecutionResponse(true, collectionManager.getInfo());
    }
}
