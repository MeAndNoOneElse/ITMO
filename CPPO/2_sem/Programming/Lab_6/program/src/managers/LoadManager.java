package managers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import support.check.Chek;
import support.check.Generation;
import support.check.exeptions.InvalidDate;
import support.check.exeptions.InvalidValueInXML;
import ticketModuls.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class LoadManager implements FileManager{
    private final String fileName;
    public LoadManager() {
        this.fileName = System.getenv("FILE_NAME");
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalStateException("Переменная окружения FILE_NAME не установлена");
        }
    }
    public LinkedList<Ticket> readCollection() throws IOException {
        LinkedList<Ticket> ticketList = new LinkedList<>();
        try (FileReader fileReader = new FileReader(fileName)) {
            // Читаем содержимое файла в строку
            StringBuilder xmlContent = new StringBuilder();
            int character;
            while ((character = fileReader.read()) != -1) {
                xmlContent.append((char) character);
            }
            // Создаем фабрику и парсер для XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Читаем XML-файл
            Document document = builder.parse(new InputSource(new StringReader(xmlContent.toString())));

            // Получаем корневой элемент
            Element root = document.getDocumentElement();

            // Получаем список всех элементов <Ticket...>
            // Мы проходим по всем дочерним элементам корневого элемента,
            // используя getChildNodes().
            NodeList tickets = root.getChildNodes();
//
            // Проходим по каждому элементу <Ticket...>
            int collisionCount = 0;
            for (int i = 0; i < tickets.getLength(); i++) {
                Node ticketNode = tickets.item(i);

                // Проверяем, что это элемент (игнорируем текстовые узлы и другие)
                //ticketNode.getNodeType() — это метод, который возвращает тип текущего узла. Узлы в XML могут быть разных типов, например:
                //Node.ELEMENT_NODE — узел является элементом (например, <Ticket12365478963214587>).
                //Node.TEXT_NODE — узел является текстовым содержимым (например, пробелы или текст между тегами).
                //Node.COMMENT_NODE — узел является комментарием.
                //В данном случае мы проверяем, является ли узел элементом (ELEMENT_NODE).
                // Это нужно, чтобы игнорировать текстовые узлы (например, пробелы или переносы строк),
                // которые могут находиться между элементами.

                if (ticketNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element ticketElement = (Element) ticketNode;
                    try {
                        // Получаем значения <id> и добавляем его в множество LongID
                        long id;
                        if (Chek.id(ticketElement.getElementsByTagName("id").item(0).getTextContent().trim())) {
                            id = Long.parseLong(ticketElement.getElementsByTagName("id").item(0).getTextContent().trim());
                            Generation.LongID.add(id);
                        } else {
                            throw new InvalidValueInXML("Неверное поле id в билете: " + ticketElement);
                        }


                        //получаем имя
                        String name;
                        if (Chek.name(ticketElement.getElementsByTagName("name").item(0).getTextContent().trim())) {
                            name = ticketElement.getElementsByTagName("name").item(0).getTextContent().trim();
                        } else {
                            throw new InvalidValueInXML("Неверное поле name в билете: " + ticketElement);
                        }


                        // получаем цену
                        int price;
                        if (Chek.price(ticketElement.getElementsByTagName("price").item(0).getTextContent().trim())) {
                            price = Integer.parseInt(ticketElement.getElementsByTagName("price").item(0).getTextContent().trim());
                        } else {
                            throw new InvalidValueInXML("Неверное поле price в билете: " + ticketElement);
                        }


                        // получаем комментарии Может быть null
                        String comment = null;
                        if (ticketElement.getElementsByTagName("comment").getLength() > 0) {
                            comment = ticketElement.getElementsByTagName("comment").item(0).getTextContent().trim();
                        }
                        // получаем координаты x y
                        Element coordinatesElement = (Element) ticketElement.getElementsByTagName("coordinates").item(0);
                        double x;
                        if (Chek.coordinateX(coordinatesElement.getElementsByTagName("X").item(0).getTextContent().trim())) {
                            x = Double.parseDouble(coordinatesElement.getElementsByTagName("X").item(0).getTextContent().trim());
                        } else {
                            throw new InvalidValueInXML("Неверное поле coordinateX в билете: " + ticketElement);
                        }

                        float y;
                        if (Chek.coordinateY(coordinatesElement.getElementsByTagName("Y").item(0).getTextContent().trim())) {
                            y = Float.parseFloat(coordinatesElement.getElementsByTagName("Y").item(0).getTextContent().trim());
                        } else {
                            throw new InvalidValueInXML("Неверное поле coordinateY в билете: " + ticketElement);
                        }

                        Coordinates coordinates = new Coordinates(x, y);

                        //Делаем дату  2025-02-10T19:00:00.367928500
                        String strCreateData = ticketElement.getElementsByTagName("creationDate").item(0).getTextContent().trim();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnnnnn");
                        LocalDateTime createData = LocalDateTime.parse(strCreateData, formatter);

                        //Делаем тип билета может быть null
                        TicketType type = null;

                        if (ticketElement.getElementsByTagName("type").getLength() > 0) {
                            if (Chek.ticketType(ticketElement.getElementsByTagName("type").item(0).getTextContent().trim())) {
                                String typeString = ticketElement.getElementsByTagName("type").item(0).getTextContent().trim();
                                type = TicketType.valueOf(typeString); // Преобразуем строку в TicketType
                            } else {
                                throw new InvalidValueInXML("Неверное поле ticketType в билете: " + ticketElement);
                            }

                        }
                        //Делаем ивент (Int id, String name, java.util.Date date, EventType eventType)
                        Event event = null;
                        if (ticketElement.getElementsByTagName("event").getLength() > 0) {

                            Element eventElement = (Element) ticketElement.getElementsByTagName("event").item(0);
                            int idEvent;
                            if (Chek.IDint(eventElement.getElementsByTagName("idEvent").item(0).getTextContent().trim())) {
                                idEvent = Integer.parseInt(eventElement.getElementsByTagName("idEvent").item(0).getTextContent().trim());
                                Generation.IntID.add(idEvent);
                            } else {
                                throw new InvalidValueInXML("Неверное поле idEvent в билете: " + ticketElement);
                            }
                            String nameEvent;
                            if (Chek.name(eventElement.getElementsByTagName("nameEvent").item(0).getTextContent().trim())) {
                                nameEvent = eventElement.getElementsByTagName("nameEvent").item(0).getTextContent().trim();
                            } else {
                                throw new InvalidValueInXML("Неверное поле nameEvent в билете: " + ticketElement);
                            }

                            // Обрабатываем дату события (может быть null) yyyy.MM.dd.HH.mm
                            LocalDateTime date = null;
                            if (eventElement.getElementsByTagName("date").getLength() > 0) {
                                String dateString;
                                if (Chek.dateEventInFile(eventElement.getElementsByTagName("date").item(0).getTextContent().trim())) {
                                    dateString = eventElement.getElementsByTagName("date").item(0).getTextContent().trim();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
                                    // Парсинг строки в Date
                                    date = LocalDateTime.parse(dateString, formatter);
                                }
                            } else {
                                throw new InvalidValueInXML("Неверное поле date в билете: " + ticketElement);
                            }

                            // Обрабатываем тип события (может быть null)
                            EventType eventType = null;
                            if (eventElement.getElementsByTagName("EventType").getLength() > 0) {
                                if (Chek.eventType(eventElement.getElementsByTagName("EventType").item(0).getTextContent().trim())) {
                                    eventType = EventType.fromValue(eventElement.getElementsByTagName("EventType").item(0).getTextContent().trim());
                                } else {
                                    throw new InvalidValueInXML("Неверное поле EventType в билете: " + ticketElement);
                                }

                            }
                            event = new Event(idEvent, nameEvent, date, eventType);
                        }
                        //Создаём Ticket и добавляем его в коллекцию
                        Ticket ticket = new Ticket(id, name, coordinates, createData, price, comment, type, event);
                        ticketList.add(ticket);

                    } catch (Exception e) {
                        collisionCount++;
                        continue;

                    }
                }
            }
            System.out.println("Пропущено " + collisionCount + " билетов.\nЗагрузка произведена из файла: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при чтении файла");
        }
        return ticketList;
    }
    public void writeCollection(LinkedList<Ticket> ticketList) {
        // Создание файла
        String DateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM.dd.HH.mm.ss"));
        String fileName = "LastSave_"+DateTime+".xml";
        // Создание файла
        File file = new File(fileName);
        // Запись
        try (FileWriter writer = new FileWriter(file)){
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            //Основной блок записи пробегаем по полям элемента и форматируем вывод toString
            writer.write("<Tickets>\n");
            for (Ticket ticket: ticketList) {
                writer.write("\t<Ticket>\n");
                writer.write("\t\t<id>"+(ticket.getId())+"</id>\n");
                writer.write("\t\t<name>"+(ticket.getName())+"</name>\n");
                writer.write("\t\t<price>"+(ticket.getPrice())+"</price>\n");
                writer.write("\t\t<coordinates>\n");
                writer.write("\t\t\t<X>"+(ticket.getCoordinates().getX())+"</X>\n");
                writer.write("\t\t\t<Y>"+(ticket.getCoordinates().getY())+"</Y>\n");
                writer.write("\t\t</coordinates>\n");
                writer.write("\t\t<creationDate>"+(ticket.getCreationDate())+"</creationDate>\n");
                if (ticket.getComment() != null) {
                    writer.write( "\t\t<comment>"+ticket.getComment()+"</comment>\n");
                }
                if (ticket.getType() != null) {
                    writer.write( "\t\t<type>"+ticket.getType().toString().replace("{", "").replace("}", "")+"</type>\n");
                }
                if (ticket.getEvent() != null) {
                    writer.write("\t\t<event>\n");
                    writer.write( "\t\t\t<idEvent>"+ticket.getEvent().getIdEvent()+"</idEvent>\n");
                    writer.write( "\t\t\t<nameEvent>"+ticket.getEvent().getNameEvent()+"</nameEvent>\n");
                    writer.write( "\t\t\t<date>"+ticket.getEvent().getDate()+"</date>\n");
                    if (ticket.getEvent().getEventType() != null) {
                        writer.write( "\t\t\t<eventType>"+ticket.getEvent().getEventType().toString().replace("{", "").replace("}", "")+"</eventType>\n");
                    }
                    writer.write("\t\t</event>\n");
                }
                writer.write("\t</Ticket>\n");
            }
            writer.write("</Tickets>");
            writer.flush();
            System.out.println("Билеты успешно записаны в файл.");
        } catch (IOException e) {

            System.out.println("Ошибка при записи в файл: "+ e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при записи в файл", e);
        }
    }

}
