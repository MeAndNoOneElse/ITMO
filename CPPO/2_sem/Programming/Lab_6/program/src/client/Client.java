package client;

import managers.CommandManager;
import support.check.*;
import support.*;

import support.check.exeptions.FileNotFind;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;


public class Client {
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 3000;

    public static void main(String[] args) throws FileNotFind {
        String host = "localhost";
        int port = 8765;
        Scanner scanner = new Scanner(System.in);
        ConsoleImpl console = new ConsoleImpl();
        TicketAsker asker = new TicketAsker(console);


        while (true) {
            try {
                CommandManager cM = new CommandManager( console);
                System.out.print("Введите команду: ");
                String userInput = scanner.nextLine().trim();
                if (userInput.isEmpty()) continue;
                if (userInput.equalsIgnoreCase("exit")) break;
                Chek proverka = new Chek(cM);
                if (!proverka.check(userInput)) {
                    System.out.println("Некорректная команда. Попробуйте ещё раз.");
                    continue;
                }
                String[] tokens = userInput.split(" ");

                String command = tokens[0];
                String[] arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
                Object payload = null;

                if (command.equalsIgnoreCase("add")
                        || command.equalsIgnoreCase("add_if_max")) {
                    payload = asker.askTicket(arguments);
                }
                if (command.equalsIgnoreCase("update_id")){
                    payload = asker.askTicketWithID(arguments);
                }

                Request request = new Request(command, arguments, payload);
                try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port))) {
                    channel.configureBlocking(true);
                    Socket socket = channel.socket();

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    out.writeObject(request);
                    out.flush();

                    Response response = (Response) in.readObject();
                    System.out.println(response.getMessage());

                    in.close();
                    out.close();

                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Сервер временно недоступен или произошла ошибка: " + e.getMessage());
                }

            }catch (RuntimeException  e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }
}