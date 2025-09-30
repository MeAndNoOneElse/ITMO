package client;

import support.Console;

import java.util.Scanner;

public class ConsoleImpl implements Console {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String readln() {
        return scanner.nextLine().trim();
    }
}
