package managers;


import ticketModuls.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {
    private final LocalDateTime initializationDate;
    private final LinkedList<Ticket> ticketList;
    private final FileManager fileManager;
    private final IdGenerator idGenerator;
    private Ticket ticket =  null;

    public CollectionManager(FileManager fileManager) {
        this.ticketList = new LinkedList<>();
        this.initializationDate = LocalDateTime.now();
        this.fileManager = fileManager;
        this.idGenerator = new IdGenerator(ticketList);
        loadCollection();
    }

    private void loadCollection() {
        try {
            LinkedList<Ticket> loaded = fileManager.readCollection();
            ticketList.addAll(loaded);
            System.out.println("Загружено " + loaded.size() + " элементов из файла:");
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
        }
    }

    public void saveCollection() {
        try {
            fileManager.writeCollection(ticketList);
            System.out.println("Коллекция сохранена в файл. Всего элементов: " + ticketList.size());
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }
    public long generateId() {
        return idGenerator.generateId();
    }

    //для add
    public void add(Ticket ticket) {
        long newId = generateId();
        ticket.setID(newId);
        ticketList.add(ticket);
        System.out.println("Добавлен элемент с id=" + newId + " и именем: " + ticket.getName());
    }

    public int getMaxPrice() {
        return ticketList.stream()
                .mapToInt(Ticket::getPrice)
                .max().getAsInt();
    }

    // для update
    public void addWithId(Ticket ticket) {
        ticketList.add(ticket);
    }

    public void clear() {
        ticketList.clear();
    }

    public void removeById(long id) {
        ticketList.removeIf(h -> h.getId() == id);
    }

    public Ticket getById(long id) {
        return ticketList.stream().filter(h -> h.getId() == id).findFirst().orElse(null);
    }

    public String getInfo() {
        return String.format("Тип: %s, Дата инициализации: %s, Количество элементов: %d",
                ticketList.getClass().getSimpleName(), initializationDate, ticketList.size());
    }

    public String getUniquePricesAsString() {
        return ticketList.stream()
                .map(Ticket::getPrice)       // Получаем поток цен (Integer)
                .distinct()                 // Оставляем уникальные
                .map(String::valueOf)       // Преобразуем в строки
                .collect(Collectors.joining("\n"));  // Соединяем через \n
    }

    public void removeLower(int minPrice) {
        Iterator<Ticket> iterator = ticketList.iterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (ticket.getPrice() < minPrice) {
                iterator.remove(); // Безопасное удаление
            }
        }
    }


    public void removeAllByEvent(Event event) {
        List<Ticket> ticketsToRemove = ticketList.stream()
                .filter(ticket -> ticket.getEvent() != null)  // Проверяем, что event не null
                .filter(ticket -> Objects.equals(event.getNameEvent(), ticket.getEvent().getNameEvent()))
                .filter(ticket -> Objects.equals(event.getDate(), ticket.getEvent().getDate()))
                .filter(ticket -> Objects.equals(event.getEventType(), ticket.getEvent().getEventType()))
                .collect(Collectors.toList());

        if (ticketsToRemove.isEmpty()) {
            System.out.println("Билетов с указанным Event не найдено.");
        } else {
            ticketList.removeAll(ticketsToRemove);
            System.out.println("Удалены билеты с ID: " +
                    ticketsToRemove.stream()
                            .map(Ticket::getId)
                            .map(String::valueOf)
                            .collect(Collectors.joining(", ")));
        }
    }

    public long removeAnyByEvent(Event event) {

        Optional<Ticket> ticketToRemove = ticketList.stream()
                .filter(ticket -> ticket.getEvent() != null)  // Проверяем, что event не null
                .filter(ticket -> Objects.equals(event.getNameEvent(), ticket.getEvent().getNameEvent()))
                .filter(ticket -> Objects.equals(event.getDate(), ticket.getEvent().getDate()))
                .filter(ticket -> Objects.equals(event.getEventType(), ticket.getEvent().getEventType()))
                .findFirst();

        if (ticketToRemove.isPresent()) {
            ticketList.remove(ticketToRemove.get());
            System.out.println("Удалён билет с ID: " + ticketToRemove.get().getId());
            return ticketToRemove.get().getId();
        } else {
            System.out.println("Билетов с указанным Event не найдено.");
        }
        return 0;
    }

    public LinkedList<Ticket> getCollection() {
        return ticketList;
    }

    public String show() {
        return ticketList.stream()
                .sorted(Comparator.comparingDouble(ticket -> {
                    Coordinates coords = ticket.getCoordinates();
                    return Math.sqrt(Math.pow(coords.getX(), 2) + Math.pow(coords.getY(), 2));
                }))
                .map(Ticket::toString) // Преобразуем каждый билет в строку
                .collect(Collectors.joining("\n")); // Объединяем с переносами строк
    }
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
