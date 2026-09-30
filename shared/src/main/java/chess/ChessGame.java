package chess;

import java.awt.color.ICC_ColorSpace;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        if (piece == null) {
            return null;
        }
        ChessBoard ogBoard = board;
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
            board = new ChessBoard(ogBoard);
            board.makeMove(move);
            if (!isInCheck(color)){
                valid.add(move);
            }
        }
        board = ogBoard;
        return valid;
    }

    private ChessPosition findKing (TeamColor teamColor) {
        for (int col = 1; col <= 8; col++){
            for (int row = 1; row <= 8; row++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return pos;
                }
            }
        }
        return null;
    }

    private Collection<ChessPosition> findTeam(TeamColor teamColor) {
        List<ChessPosition> positions = new ArrayList<>();
        for (int col = 1; col <= 8; col++){
            for (int row = 1; row <= 8; row++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition pos = move.getStartPosition();
        ChessPiece piece = board.getPiece(pos);
        Collection<ChessMove> valid = validMoves(pos);
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Wrong player's turn");
        }
        if (!valid.contains(move)){
            throw new InvalidMoveException("Illegal Move");
        }
        board.makeMove(move);
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        TeamColor enemyColor = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK){
            enemyColor = TeamColor.WHITE;
        }
        Collection<ChessPosition> enemyPos = findTeam(enemyColor);
        for (ChessPosition pos : enemyPos) {
            ChessPiece piece = board.getPiece(pos);
            Collection<ChessMove> moves = piece.pieceMoves(board, pos);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
