package managers;


import ticketModuls.Ticket;

import java.util.LinkedList;


public class IdGenerator {
    private long nextId;
    private final LinkedList<Ticket> ticketList;


    public IdGenerator(LinkedList<Ticket> ticketList) {
        this.ticketList = ticketList;
        if (ticketList.isEmpty()) {
            nextId = 1;
        } else {
            nextId = ticketList.stream()
                    .mapToLong(Ticket::getId)
                    .max()
                    .getAsLong() + 1;
        }
        System.out.println("Инициализирован IdGenerator с nextId = " + nextId);
    }


    public long generateId() {
        while (ticketList.stream().anyMatch(h -> h.getId() == nextId)) {
            nextId++;
        }
        long id = nextId;
        nextId++;
        System.out.println("Сгенерирован новый id: " + id);
        return id;
    }
}