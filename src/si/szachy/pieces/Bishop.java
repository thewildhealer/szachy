package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

public class Bishop extends Piece {
    public Bishop(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner, 30);
        name = "bishop";
        evaluation = new double[][]  {
                { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
                { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
                { -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
                { -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
                { -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
                { -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
                { -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
                { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}};
        setImage();
    }

    public Bishop(Piece other, Chessboard board) {
        super(other, board);
    }

    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        return Math.abs(x - this.getX()) == Math.abs(y - this.getY());
    }
}
