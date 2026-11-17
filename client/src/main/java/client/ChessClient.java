package client;
import static ui.EscapeSequences.*;
import java.util.Scanner;

public class ChessClient {
    private enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(int port) {
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Welcome to chess! Type help to view commands");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e){
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>>" + SET_TEXT_COLOR_GREEN);
    }

    private String eval(String input) {
        try {

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
}
