package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }
            new ChessClient(port).run();
        } catch (NumberFormatException ex) {
            System.out.println("The port must be a number like 8080");
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
