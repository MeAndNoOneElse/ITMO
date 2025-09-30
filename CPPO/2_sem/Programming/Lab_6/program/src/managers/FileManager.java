package managers;


import ticketModuls.Ticket;

import java.util.LinkedList;
import java.util.Vector;

/**
 * интерфейс для работы с файлами
 */
public interface FileManager {
    LinkedList<Ticket> readCollection() throws java.io.IOException;
    void writeCollection(LinkedList<Ticket> ticketList) throws java.io.IOException;
}
