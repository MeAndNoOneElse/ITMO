package commands;

import managers.CollectionManager;
import support.Console;
import support.ExecutionResponse;
import ticketModuls.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RemoveAllByEvent extends Command {
    private CollectionManager collectionManager;
    private Console console;

    public RemoveAllByEvent(Console console, CollectionManager collectionManager) {
        super("remove_all_by_event", "(имя, дата в формате yyyy.MM.DD.hh.mm, тип(не обязательно)) удалить из коллекции все элементы, у которых такое же мероприятие", false, 3, 1);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String[] text) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");
        String name = text[1];
        LocalDateTime date = LocalDateTime.parse(text[2], DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm"));
        EventType eventType = (text.length >= 4 ? EventType.fromValue(text[3]) : null);
        Event event = new Event(1, name, date, eventType);
        int initialSize = collectionManager.getCollection().size();
        collectionManager.removeAllByEvent(event);
        int removedCount = initialSize - collectionManager.getCollection().size();
        return new ExecutionResponse(true, "Удалено элементов: " + removedCount);
    }
}