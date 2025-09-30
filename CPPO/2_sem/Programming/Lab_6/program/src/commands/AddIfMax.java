package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;
import ticketModuls.*;



/**
 * @author Andrey Anufriev
 * класс для реализации add_if_max
 */
public class AddIfMax extends Command {
    private final CollectionManager collectionManager;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add", "(имя, цена, комментарий(не обязательно)) добавить новый элемент в коллекцию", true, 3, 1);
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        //получаем максимальную цену
        int maxPrice = collectionManager.getMaxPrice();
        //получаем цену
        int price = Integer.parseInt(text[2]);
        if (!(price > maxPrice)) {
            return new ExecutionResponse(false, "Это не максимальная цена");
        }
        Ticket ticket = collectionManager.getTicket();
        if (ticket == null)
            return new ExecutionResponse(false, "Элемент не передан от клиента");

        collectionManager.add(ticket);
        return new ExecutionResponse(true, "Элемент успешно добавлен");
    }
}