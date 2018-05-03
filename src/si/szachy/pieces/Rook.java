package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

public class Rook extends Piece {
    public Rook(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner, 50);
        name = "rook";
        
        evaluation = new double[][] {
                {  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
                {  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
                { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
                { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
                { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
                { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
                { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
                {  0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
};
        setImage();
    }

    public Rook(Piece other, Chessboard board) {
        super(other, board);
    }

    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        return (x == this.getX() || y  == this.getY());
    }

}
