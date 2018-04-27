package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

import java.awt.*;

public class Rook extends Piece {
    public Rook(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner);
        name = "rook";
        setImage();
    }

    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        return (x == this.getX() || y  == this.getY());
    }

}
