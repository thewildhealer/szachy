package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

public class King extends Piece {
    public King(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner);
        name = "king";
        setImage();
    }

    // TODO: roszada
    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        if (isFieldDangerous(x, y)) return false;
        return x <= getX() + 1 && y <= getY() + 1 && x >= getX() - 1 && y >= getY() - 1;
    }
}
