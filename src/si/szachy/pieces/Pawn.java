package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

import java.awt.*;

public class Pawn extends Piece {
    public Pawn(Chessboard board, Coordinate coord, int owner) {
        super(board, coord, owner);
        name = "pawn";
        setImage();
    }

    // TODO: przejrzec to, moga byc tu bugi
    @Override
    protected boolean pieceMovement(int x, int y) {
        if (!coord.isValid(x, y)) return false;
        if (x == this.getX() && this.getY() == y) return false;
        int direction = owner == 0 ? -1 : 1;
        // 2 kroki na poczatek
        if(didMove == false && (getY() == 1 || getY() == 6) && y == getY()+direction*2 && board.peek(x,y) == null) return true;
        // bicie po skosach
        if (y == (getY() + direction) && board.peek(x,y) != null && board.peek(x,y).getOwner() != owner && (x==getX()-1 || x== getX()+1) && x!=getX()) return true;
        // nie bicie po prostej
        if (board.peek(x,y) != null) return false;
        // zwykly ruch
        return (y == (getY() + direction)) && x == getX();
    }

}
