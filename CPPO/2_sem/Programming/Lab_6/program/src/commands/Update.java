package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;
import ticketModuls.Ticket;

public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update_id", "(id, имя, цена, комментарий(не обязательно) обновить элемент по его id", true, 4, 1);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        Ticket ticket = collectionManager.getTicket();
        if (ticket == null)
            return new ExecutionResponse(false, "Элемент не передан от клиента");
        long id = ticket.getId();

        Ticket oldTicket = collectionManager.getById(id);
        if (oldTicket == null)
            return new ExecutionResponse(false, "Элемент с id " + id + " не найден");
        collectionManager.removeById(id);
        Ticket newTicket = collectionManager.getTicket();
        if (newTicket == null)
            return new ExecutionResponse(false, "Новый элемент не передан от клиента");

        newTicket.setID(id);

        collectionManager.addWithId(newTicket);

        return new ExecutionResponse(true, "Элемент с id " + id + " успешно обновлён");

    }
}