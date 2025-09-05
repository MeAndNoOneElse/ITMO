package support.check;

import managers.CommandManager;
import support.check.exeptions.*;
import commands.*;
import ticketModuls.EventType;
import ticketModuls.TicketType;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static support.check.DateEventCheck.isValidDateTime;
import static support.check.Generation.LongID;


public class Chek {
    CommandManager commandManager;
    public Chek(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    public boolean check(String input) throws InvalidCommandExeption, InvalidNameException, InvalidPriceException, MissingFieldException, FileNotFind {
        String[] token = input.split(" ");
        // Проверка на минимальное количество элементов
        if (token.length == 0) {
            throw new MissingFieldException("Введенная строка пуста.");
        }
        String strcommand = token[0];

        if (!commandManager.CommandMap.containsKey(strcommand)) {
            throw new InvalidCommandExeption("Неверная команда: " + strcommand);
        }
        Command command = commandManager.CommandMap.get(strcommand);


        //команды 1+0
        if (command.getMustArg() == 1) {
             if (token.length >= 2) {
                throw new MoreFieldException("Слишком много аргументов");
            }
            return true;
        }
        //команды 2+0
        if (command.getMustArg() == 2) {
            if (token.length < 2) {
                throw new MissingFieldException("Недостаточно аргументов");
            }
            if (token.length >= 3) {
                throw new MoreFieldException("Слишком много аргументов");
            }
            String arg = token[1];

            if (command instanceof ExecuteScript) {
                if (fileName(arg)) {
                    System.out.println(command.getName());
                    return true;
                }
            }
            if (command instanceof RemoveById) {
                if (id(arg)) {
                    return true;
                }
            }
            if (command instanceof RemoveLower) {
                if (price(arg)) {
                    return true;
                }
            }

        }
        //команды 3+1
        if ((command.getMustArg() == 3) & (command.getDopArg() == 1)) {
            if (token.length < 3) {

                throw new MissingFieldException("Недостаточно аргументов");
            }
            if (token.length > 4) {
                throw new MoreFieldException("Слишком много аргументов");
            }
            String name = token[1];
            if (name(name)) {
                String arg2 = token[2];
                if (command instanceof Add || command instanceof AddIfMax) {
                    if (price(arg2)) {
                        return true;
                    }
                }
                if (command instanceof RemoveAllByEvent || command instanceof RemoveAnyByEvent) {
                    if (dateEvent(arg2)) {
                        if (token.length == 4) {
                            String arg3 = token[3];
                            if (eventType(arg3)) {
                                return true;
                            }
                        }

                        return true;
                    }
                }
            }
        }
        //команды 4+1
        if (command instanceof Update) {
            if ((token.length < 4)) {
                throw new MissingFieldException("Недостаточно аргументов");
            }
            if (token.length >= 6) {
                throw new MoreFieldException("Слишком много аргументов");
            }
            String id = token[1];
            String name = token[2];
            String price = token[3];
            if (id(id) & name(name) & price(price)) {
                return true;
            }
        }
        return false;
    }
    private String getDopArg(){
        String dopString = "";

        return dopString;
    }

    //Проверка, что строка ID является числом long>0
    private boolean move(String arg) {
        try {
            int move = Integer.parseInt(arg);
            if (!(move == 1 || move == 2 || move == 3 || move == 4)) {
                throw new InvalidIDExeption("Введёный режим не в том формате (1,2,3,4)");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidIDExeption("Введёный режим не является числом (int)");
        }
    }

    //проверка id
    public static boolean id(String strId) {
        try {
            long id = Long.parseLong(strId);
            if (id <= 0) {
                throw new InvalidIDExeption("Введённый id<=0, а должен быть больше 0");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidIDExeption("Введёный id не является числом (long)");
        }
    }
    private boolean oldId(String strId) {
        if (id(strId) & LongID.contains(Long.parseLong(strId))) {
            return true;
        }
        else {
            throw new InvalidIDExeption("Нет билета с таким id. Чтобы создать новый, можете воспользоваться командой add");
        }
    }

    //проверка существования файла
    private boolean fileName(String fileName) throws FileNotFind {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFind("Не найден файл с таким именем.");
        }
        return true;
    }

    //Проверка, что строка price является числом int>0
    public static boolean price(String strPrice) {
        try {
            if (Integer.parseInt(strPrice) <= 0) {
                throw new InvalidIDExeption("Введённая цена <=0, а должен быть больше 0");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidIDExeption("Введёная price не является числом (int)");
        }
    }

    //проверка на пустоту строки
    public static boolean name(String name) {
        if (name.isEmpty()) {
            throw new InvalidNameException("Имя не может быть пустым");
        }
        return true;
    }

    //Проверка корректности даты мероприятия
    private boolean dateEvent(String strDate) {
        try {
            LocalDateTime eventDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm", Locale.ENGLISH));
            if (!isValidDateTime(eventDate)) {
                throw new InvalidDate("Такой даты не существует");
            }
            // Проверяем, что дата находится в будущем
            LocalDateTime now = LocalDateTime.now();
            if (eventDate.isBefore(now)) {
                throw new InvalidDate("Дата находится в прошлом");
            }
            return true;
        } catch (DateTimeParseException e) {
            throw new InvalidDate("Неверный формат даты (yyyy.MM.dd.HH.mm)");
        }
    }

    //Проверка, существования eventType в Enum
    public static boolean eventType(String str) {
        try {
            EventType.valueOf(str);
            return true;
        } catch (IllegalArgumentException e) {
            throw new InvalideTicketType("Такого типа мероприятия нет");
        }
    }


    // проверка coordinates Double x
    public static boolean coordinateX(String X) {
        try {
            Double.parseDouble(X);
        } catch (NumberFormatException e) {
            throw new InvalidCoordinate("Введёная Координата X не является числом(double)");
        }
        return true;
    }

    // проверка coordinates   float y
    public static boolean coordinateY(String Y) {
        try {
            if (Float.parseFloat(Y) < -121) {
                throw new InvalidCoordinate("Введёная Координата Y должна быть больше -121");
            }
        } catch (NumberFormatException e) {
            throw new InvalidCoordinate("Введёная Координата Y не является числом(float)");
        }
        return true;
    }

    //Проверка, существования ticketType в Enum
    public static boolean ticketType(String str) {
        try {
            TicketType.fromValue(str);
            return true;
        } catch (IllegalArgumentException e) {
            throw new InvalideTicketType("Такого типа билета нет");
        }
    }

    //проверка полей event
    // id
    // date
    // eventType
    //
    //Проверка, что строка ID является числом int>0
    public static boolean IDint(String strId) {
        try {
            int id = Integer.parseInt(strId);
            if (id <= 0) {
                throw new InvalidIDExeption("Введённый id<=0, а должен быть больше 0");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidIDExeption("Введёный id не является числом (int)");
        }
    }
    public static boolean IDLong(String strId) {
        try {
            long id = Long.parseLong(strId);
            if (id <= 0) {
                throw new InvalidIDExeption("Введённый id<=0, а должен быть больше 0");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidIDExeption("Введёный id не является числом (int)");
        }
    }


    //Проверка даты
//    public static boolean dateEvent(String strDate){
//        try {
//            LocalDateTime eventDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH));
//            if (!isValidDateTime(eventDate)) {
//                throw new InvalidDate("Такой даты не существует");
//            }
//            // Проверяем, что дата находится в будущем
//            LocalDateTime now = LocalDateTime.now();
//            if (eventDate.isBefore(now)) {
//                throw new InvalidDate("Дата находится в прошлом");
//            }
//            return true;
//        }catch (DateTimeParseException e){
//            throw new InvalidDate("Неверный формат даты");
//        }
//    }
    //Проверка даты
    public static boolean dateEventInFile(String strDate) {
        try {
            LocalDateTime eventDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm"));
            if (!isValidDateTime(eventDate)) {
                throw new InvalidDate("Такой даты не существует");
            }
            return true;
        } catch (DateTimeParseException e) {
            throw new InvalidDate("Неверный формат даты");
        }
    }
}