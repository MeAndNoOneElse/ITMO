package commands;

import managers.CollectionManager;
import support.*;
import ticketModuls.*;


/**
 * @author Andrey Anufriev
 * "класс для реализации add"
 */
public class Add extends Command { // Add Command
    private final CollectionManager collectionManager;
    private final Console console;

    public Add(Console console, CollectionManager collectionManager) {
        super("add", "(имя, цена, комментарий(не обязательно)) добавить новый элемент в коллекцию", true, 3, 1);
        this.collectionManager = collectionManager;
        this.console = console;
    }

    public ExecutionResponse execute(String[] arguments) {
        Ticket ticket = collectionManager.getTicket();
        if (ticket == null)
            return new ExecutionResponse(false, "Элемент не передан от клиента");
        collectionManager.add(ticket);

        return new ExecutionResponse(true, "Элемент успешно добавлен");
    }
//    /**
//     * @param text реализует команду add
//     */
//    public ExecutionResponse execute(String[] text) {
//
//        //генерится id
//        long id = Generation.getLongID();
//        //получаем имя
//        String name = text[1];
//        //получаем цену
//        int price = Integer.parseInt(text[2]);
//        //проверка на наличие комментариия
//        String comment = null;
//        String mode = "";
//        if (text.length == 4) {
//            mode = text[3];
//        }
//        if (text.length == 5) {
//            comment = text[3];
//            mode = text[3];
//        }
//        // генерим дату
//        java.time.LocalDateTime creationDate = Generation.getCreationDate();
//
//        //А для остальных нужен reader
//        //получаем координаты
//        Coordinates coordinates = TicketAsker.askCoordinate();
//        //получаем тип
//        TicketType type = TicketAsker.askTicketType(); //Поле может быть null
//        //получаем мероприятие
//        Event event = TicketAsker.askEvent(); //Может быть null
//
//        //создаём билет
//        Ticket ticket = new Ticket(id, name, coordinates, creationDate, price, comment, type, event);
//        collectionManager.add(ticket);
//
//        return new ExecutionResponse(true, "Элемент успешно добавлен");
//    }
}