package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        return switch (piece.getPieceType()){
            case BISHOP -> bishopMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
            case PAWN -> pawnMoves(board, myPosition);
        };
    }

    private static final int[][] diagonalDirection = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
    };
    private static final int[][] straightDirection = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };
    private static final int[][] omniDirection = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };
    private static final int[][] LDirection = {
            {1, 2}, {-1, 2}, {2, 1}, {-2, 1}, {2, -1}, {-2, -1}, {1, -2}, {-1, -2}
    };

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition myPosition, int[][] direction){
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        for (int[] dir : direction) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean isEnd = false;
            while (!isEnd) {
                row += dir[0];
                col += dir[1];
                if (!onBoard(row, col)){
                    isEnd = true;
                    continue;
                }
                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece occupant = board.getPiece(newPos);
                if (occupant == null){
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else if (isEnemy(occupant, piece.getTeamColor())) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                    isEnd = true;
                } else {
                    isEnd = true;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> moveOnce(ChessBoard board, ChessPosition myPosition, int[][] direction) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        for (int[] dir : direction) {
            int row = myPosition.getRow() + dir[0];
            int col = myPosition.getColumn() + dir[1];
            if (onBoard(row, col)){
                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece occupant = board.getPiece(newPos);
                if (occupant == null || isEnemy(occupant, piece.getTeamColor())){
                    moves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return moves;
    }

    private boolean onBoard(int row, int col){
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean isEnemy(ChessPiece occupant, ChessGame.TeamColor myColor) {
        return occupant != null && occupant.getTeamColor() != myColor;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        return slidingMoves(board, myPosition, diagonalDirection);
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        return slidingMoves(board, myPosition, straightDirection);
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        return slidingMoves(board, myPosition, omniDirection);
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        return moveOnce(board, myPosition, LDirection);
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        return moveOnce(board, myPosition, omniDirection);
    }

    private Collection<ChessMove> addPawnMove(ChessPosition from, ChessPosition to, int dir){
        List<ChessMove> moves = new ArrayList<>();
        int row = to.getRow();
        PieceType[] promotedPieces = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
        if ((dir == 1 && row == 8) || dir == -1 && row == 1) {
            for (PieceType type : promotedPieces) {
                moves.add(new ChessMove(from, to, type));
            }
        } else {
            moves.add(new ChessMove(from, to, null));
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        int dir = 1;
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            dir = -1;
        }
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (onBoard(row+dir, col) && board.getPiece(new ChessPosition(row+dir, col)) == null){
            moves.addAll(addPawnMove(myPosition, new ChessPosition(row+dir, col), dir));
            if (((dir == 1 && row == 2) || (dir == -1 && row == 7)) && board.getPiece(new ChessPosition(row+dir*2, col)) == null){
                moves.addAll(addPawnMove(myPosition, new ChessPosition(row+dir*2, col), dir));
            }
        }

        if ((dir == 1 && row == 2) || dir == -1 && row == 7) {
            if (null == board.getPiece(new ChessPosition(row + dir, col)) || null == board.getPiece(new ChessPosition(row + dir*2, col))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + (dir * 2), col), null));
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
