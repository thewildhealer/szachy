package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

import java.awt.*;

public class Pawn extends Piece {
    public Pawn(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner);
    }

    @Override
    public boolean isValidMove(int x, int y) {
        return pieceMovement(x,y);
    }

    // TODO: calosc do poprawy raczej
    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        // pierwsza tura, dwa ruchy
        if (board.getTurn() == 1 && y == coord.y + 2 && x == coord.x && board.peek(x, y) == null && board.peek(x, y - 1) == null)
            return true;
        // pozostałe rundy do przodu
        if (y == coord.y + 1 && x == coord.x && board.peek(x, y) == null) return true;
        // bicie na prawo
        if (y == coord.y + 1 && x == coord.x + 1 && board.peek(x + 1, y) != null) return true;
        // bicie na lewo
        if (y == coord.y + 1 && x == coord.x - 1 && board.peek(x - 1, y) != null) return true;
        return false;
    }

    @Override
    public Color getColor() {
        return Color.red;
    }
}
