package server;

import managers.*;
import support.*;
import ticketModuls.Ticket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        int port = 8765;
        FileManager fileManager = new LoadManager();
        CollectionManager collectionManager = new CollectionManager(fileManager);
        CommandManager commandManager = new CommandManager(collectionManager, new DopConsole());

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine().trim();
                switch (line) {
                    case "save" -> {
                        collectionManager.saveCollection();
                        System.out.println("Коллекция сохранена по команде save.");
                    }
                    case "exit" -> {
                        collectionManager.saveCollection();
                        System.out.println("Сервер завершает работу.");
                        System.exit(0);
                    }
                    default -> System.out.println("Неизвестная команда сервера. Используйте: save | exit");
                }
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            collectionManager.saveCollection();
            System.out.println("Коллекция сохранена автоматически при завершении.");
        }));


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Подключение от: " + clientSocket.getRemoteSocketAddress());

                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                    Request request = (Request) in.readObject();
                    String commandName = (request.getCommandName());
                    String[] argsArray = request.getArguments();
                    Object payload = request.getPayload();

                    ExecutionResponse execResult;

                    if ((commandName.equalsIgnoreCase("add")
                            || commandName.equalsIgnoreCase("update_id")
                            || commandName.equalsIgnoreCase("add_if_max"))
                            && payload instanceof Ticket ticket) {
                        collectionManager.setTicket(ticket);

                        execResult = commandManager.execute(
                                commandName + " " + String.join(" ", argsArray)
                        );

                    } else if (commandName.equalsIgnoreCase("execute_script")) {
                        execResult = executeScriptOnServer(argsArray[0], commandManager);
                    } else {
                      //  System.out.println(commandName + " " + String.join(" ", argsArray));
                        execResult = commandManager.execute(
                                commandName + " " + String.join(" ", argsArray)
                        );
                    }


                    Response response = new Response(execResult.isSuccess(), execResult.getMessage(), null);
                    out.writeObject(response);
                    out.flush();

                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println("Ошибка при обработке клиента: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
    private static ExecutionResponse executeScriptOnServer(String path, CommandManager commandManager) {
        File file = new File(path);
        System.out.println(path);
        if (!file.exists() || !file.canRead()) {
            return new ExecutionResponse(false, "Файл скрипта не найден или недоступен для чтения.");
        }

        StringBuilder result = new StringBuilder();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                ExecutionResponse resp = commandManager.execute(line);
                result.append("> ").append(line).append("\n")
                        .append(resp.getMessage()).append("\n");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении скрипта: " + e.getMessage());
        }

        return new ExecutionResponse(true, result.toString());
    }
}
