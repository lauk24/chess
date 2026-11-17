package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    private static final String LIGHT_SQUARE = SET_BG_COLOR_WHITE;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREEN;

    public static String draw(ChessBoard board, boolean isWhite) {
        var result = new StringBuilder();
        int startRank = isWhite ? 8 : 1;
        int rankStep = isWhite? -1 : 1;
        int startFile = isWhite? 1 : 8;
        int fileStep = isWhite? 1 : -1;

        for (int rank = startRank; rank >=1 && rank <= 8; rank += rankStep) {
            for (int file = startFile; file >= 1 && file <= 8; file += fileStep) {
                result.append(square(board, rank, file));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private static String square(ChessBoard board, int rank, int file) {
        boolean isLight = (rank + file) % 2 == 1;
        String background = isLight ? LIGHT_SQUARE : DARK_SQUARE;
        var piece = board.getPiece(new ChessPosition(file, rank));
        return background + getPieceText(piece) + RESET_TEXT_COLOR + RESET_BG_COLOR;
    }

    private static String getPieceText(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        String color = isWhite ? SET_TEXT_COLOR_LIGHT_GREY : SET_TEXT_COLOR_GREEN;
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
}
