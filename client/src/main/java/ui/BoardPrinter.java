package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    private static final String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREEN;
    private static final String BORDER = SET_BG_COLOR_DARK_GREY;

    public static String draw(ChessBoard board, boolean isWhite) {
        var result = new StringBuilder();
        int startRank = isWhite ? 8 : 1;
        int rankStep = isWhite? -1 : 1;
        int startFile = isWhite? 1 : 8;
        int fileStep = isWhite? 1 : -1;

        result.append(labelFile(startFile, fileStep));
        for (int rank = startRank; rank >=1 && rank <= 8; rank += rankStep) {
            result.append(labelRank(rank));
            for (int file = startFile; file >= 1 && file <= 8; file += fileStep) {
                result.append(square(board, rank, file));
            }
            result.append(labelRank(rank)).append("\n");
        }
        result.append(labelFile(startFile, fileStep));
        return result.toString();
    }

    private static String square(ChessBoard board, int rank, int file) {
        boolean isLight = (rank + file) % 2 == 1;
        String background = isLight ? LIGHT_SQUARE : DARK_SQUARE;
        var piece = board.getPiece(new ChessPosition(rank, file));
        return background + getPieceText(piece) + RESET_TEXT_COLOR + RESET_BG_COLOR;
    }

    private static String getPieceText(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        String color = isWhite ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
        String symbol = switch(piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
        return color + symbol;
    }

    private static String labelRank (int rank) {
        return BORDER + " " + rank + " " + RESET_TEXT_COLOR + RESET_BG_COLOR;
    }

    private static String labelFile (int startFile, int fileStep) {
        var row = new StringBuilder(BORDER + "   ");
        for (int file = startFile; file >= 1 && file <= 8; file += fileStep) {
            row.append(" ").append((char) 'a' + file - 1).append(" ");
        }
        row.append("   " + RESET_TEXT_COLOR + RESET_BG_COLOR + "\n");
        return row.toString();
    }
}
