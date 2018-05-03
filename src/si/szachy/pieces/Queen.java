package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

public class Queen extends Piece {
    public Queen(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner, 90);
        name = "queen";
        evaluation = new double[][] {
                { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
                { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
                { -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
                { -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
                {  0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
                { -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
                { -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
                { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}};
        setImage();
    }

    public Queen(Piece other, Chessboard board) {
        super(other, board);
    }

    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        return ((Math.abs(x - this.getX()) == Math.abs(y - this.getY())) || (x == this.getX() || y  == this.getY()));
    }
}
