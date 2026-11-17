package client;
import chess.ChessGame;
import model.GameData;

import static ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ChessClient {
    private enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private List<GameData> games = new ArrayList<>();

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
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private String eval(String input) {
        try {
            String[] tokens = input.trim().split("\\s+");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch(cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }
            return switch(cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> joinGame(params);
                case "observe" -> observeGame(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String register(String... params) throws ResponseException{
        if (params.length == 3) {
            server.register(params[0], params[1], params[2]);
            state = State.SIGNEDIN;
            return "Registered and logged in as " + params[0] + ".";
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
    }
    private String login(String... params) throws ResponseException{
        if (params.length == 2) {
            server.login(params[0], params[1]);
            state = State.SIGNEDIN;
            return "Logged in as " + params[0] + ".";
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Expected: login <USERNAME> <PASSWORD>");
    }

    private String logout() throws ResponseException{
        server.logout();
        state = State.SIGNEDOUT;
        games = new ArrayList<>();
        return "Logged out.";
    }

    private String createGame(String ... params) throws ResponseException{
        if (params.length >= 1) {
            String gameName = String.join(" ", params);
            int gameID = server.createGame(gameName);
            games = server.listGames();
            for (int i = 0; i < games.size(); i++) {
                if (games.get(i).gameID() == gameID) {
                    return "Created game " + gameName + " (" + i + ").";
                }
            }
            return "Created game " + gameName + ".";
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Expected: create <NAME>");
    }

    private String listGames() throws ResponseException {
        games = server.listGames();
        if (games.isEmpty()) {
            return "There are no current games. Use create <NAME> to start one.";
        }
        var result = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            var game = games.get(i);
            result.append(String.format("%d. %s (white: %s, black: %s)%n",
                    i + 1,
                    game.gameName(),
                    game.whiteUsername() == null ? "-" : game.whiteUsername(),
                    game.blackUsername() == null ? "-" : game.blackUsername()));
        }
        return result.toString();
    }

    private GameData getGameFromString(String numberString) throws ResponseException {
        int number;
        try {
            number = Integer.parseInt(numberString);
        } catch (NumberFormatException ex) {
            throw new ResponseException(ResponseException.Code.ClientError,
                    "The game number must be a positive integer.");
        }
        if (number >= 1 || number < games.size()) {
            return games.get(number - 1);
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Game " +  number + " does not exist. Use list to see all current games.");
    }

    private String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var game = getGameFromString(params[0]);
            ChessGame.TeamColor color;
            switch (params[1].toLowerCase()) {
                case "white" -> color = ChessGame.TeamColor.WHITE;
                case "black" -> color = ChessGame.TeamColor.BLACK;
                default -> throw new ResponseException(ResponseException.Code.ClientError,
                        "The color must be white or black.");
            }
            server.joinGame(game.gameID(), color);
            return "Joined " + game.gameName() + " as " + color + ".";
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Expected: play <NUMBER> <WHITE|BLACK>");
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var game = getGameFromString(params[0]);
            server.joinGame(game.gameID(), null);
            return "Observing " + game.gameName() + ".";
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                "Expected observe <NUMBER>");
    }

    private String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - Create an account
                    login <USERNAME> <PASSWORD> - Login into account
                    quit - Exit program
                    help - List commands
                    """;
        }
        return """
                create <NAME> - Create a game
                list - List all games
                play <NUMBER> <WHITE|BLACK> - Join a game
                observe <NUMBER> - Watch a game
                logout - Sign out
                help - List commands
                """;
    }
}
