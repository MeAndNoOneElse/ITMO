package support;

import support.check.Generation;
import ticketModuls.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static support.check.DateEventCheck.isValidDateTime;


public class TicketAsker {
    private final Console console;

    public TicketAsker(Console console) {
        this.console = console;
    }

    public Ticket askTicket(String[] text) {
        //получаем имя
        String name = text[0];
        //получаем цену
        int price = Integer.parseInt(text[1]);
        //проверка на наличие комментариия
        String comment = null;
        if (text.length == 3) {
            comment = text[2];
        }
        java.time.LocalDateTime creationDate = Generation.getCreationDate();
        Coordinates coordinates = askCoordinates();
        TicketType type = askEnumOrNull(TicketType.class);
        Event event = askEvent();
        ;
        return new Ticket(1, name, coordinates, creationDate, price, comment, type, event);
    }

    public Ticket askTicketWithID(String[] text) {
        long id = Long.parseLong(text[0]);
        System.out.println(id);
        //получаем имя
        String name = text[1];
        //получаем цену
        int price = Integer.parseInt(text[2]);
        //проверка на наличие комментариия
        String comment = null;
        if (text.length == 4) {
            comment = text[3];
        }
        java.time.LocalDateTime creationDate = Generation.getCreationDate();
        Coordinates coordinates = askCoordinates();
        TicketType type = askEnumOrNull(TicketType.class);
        Event event = askEvent();

        return new Ticket(id, name, coordinates, creationDate, price, comment, type, event);
    }

    public Event askEvent() {
        return new Event(Generation.getIntID(), askNonEmptyString("Ошибка: name не может быть пустым. Повторите ввод:"), askDate(), askEnumOrNull(EventType.class));

    }

    public TicketType askTicketType() {
        return askEnumOrNull(TicketType.class);
    }

    private <T extends Enum<T>> T askEnumOrNull(Class<T> enumClass) {
        while (true) {
            console.println("Введите тип");
            String input = console.readln();
            if (input == null) {
                console.println("Введите значение или пустую строку для null:");
                console.println("Доступные значения: " + Arrays.toString(enumClass.getEnumConstants()));
                continue;
            }
            if (input.isEmpty()) return null;
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                console.println("Ошибка: введите одно из указанных значений или пустую строку. Повторите ввод:");
            }
        }
    }

    private String askNonEmptyString(String errorMessage) {
        console.println("Введите название мероприятия:");
        String input = console.readln();
        while (input == null || input.isEmpty()) {
            if (input == null) {
                console.println("Для выхода используйте команду exit.");
            } else {
                console.println(errorMessage);
            }
            console.println("Введите значение:");
            input = console.readln();
        }
        return input;
    }

    private LocalDateTime askDate() {
        LocalDateTime eventDate = null;
        console.println("Введите дату мероприятия в формате yyyy.MM.dd.HH.mm:");
        while (eventDate == null) {
            String dateString = console.readln().trim();
            try {
                // Парсим строку в LocalDateTime
                eventDate = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm"));

                // Проверяем, что дата существует (например, 31 февраля не существует)
                if (!isValidDateTime(eventDate)) {
                    System.out.println("Ошибка: введённая дата не существует. Пожалуйста, введите корректную дату.");
                    eventDate = null; // Сбрасываем дату, чтобы цикл продолжился
                    continue;
                }

                // Проверяем, что дата находится в будущем
                LocalDateTime now = LocalDateTime.now();
                if (eventDate.isBefore(now)) {
                    System.out.println("Ошибка: введённая дата должна быть в будущем. Пожалуйста, введите дату позже текущего времени.");
                    eventDate = null; // Сбрасываем дату, чтобы цикл продолжился
                }

            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: некорректный формат даты. Пожалуйста, введите дату в формате 'yyyy.MM.dd.HH.mm'.");
            }
        }
        console.println("Успешно");
        return eventDate;
    }

    private Long askLong() {
        while (true) {
            String input = console.readln();
            if (input == null) {
                console.println("Для выхода используйте команду exit.");
                console.println("Введите x:");
                return null;
            }
            try {
                return Long.parseLong(input.replace(",", "."));
            } catch (NumberFormatException e) {
                console.println("Ошибка: x должен быть числом с плавающей точкой. Повторите ввод:");
            }
        }
    }

    private Double askDouble() {
        while (true) {
            String input = console.readln();
            if (input == null) {
                console.println("Для выхода используйте команду exit.");
                console.println("Введите x:");
                return null;
            }
            try {
                return Double.parseDouble(input.replace(",", "."));
            } catch (NumberFormatException e) {
                console.println("Ошибка: x должен быть числом с плавающей точкой. Повторите ввод:");
            }
        }
    }

    private Float askFloat() {
        try {
            while (true) {
                String input = console.readln();
                if (input == null) {
                    console.println("Для выхода используйте команду exit.");
                    console.println("Введите y:");
                    return null;
                }
                if (Double.parseDouble(input.replace(",", ".")) > -121) {
                    return Float.parseFloat(input.replace(",", "."));
                } else {
                    console.println("Ошибка: Y должен быть больше -121. Попробуйте ещё раз:");
                }
            }
        } catch (NumberFormatException e) {
            console.println("Ошибка: Y должен быть числом с плавающей точкой. Повторите ввод:");
        }
        return null;
    }

    private Coordinates askCoordinates() {
        while (true) {
            console.println("Введите x:");
            Double x = askDouble();
            if (x == null) continue;

            console.println("Введите y:");
            Float y = askFloat();
            if (y == null) continue;
console.println("Координаты успешно добавлены");
            Coordinates coordinates = new Coordinates(x, y);


            return coordinates;

        }
    }
}

