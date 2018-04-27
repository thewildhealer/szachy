package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

import java.awt.*;

abstract public class Piece {
    protected Chessboard board;
    protected Coordinate coord;
    protected int owner;
    private Color c;

    public Coordinate getCoord() {
        return coord;
    }
    public int getX() {
        return coord.x;
    }
    public int getY() {
        return coord.y;
    }
    public void setCoord(Coordinate c) {
        this.coord = c;
    }

    public Piece(Chessboard b, Coordinate c, int o) {
        this.board = b;
        this.coord = c;
        this.owner = o;
    }
    abstract protected boolean pieceMovement(int x, int y);
    public boolean isValidMove(int x, int y) {
            if (pieceMovement(x, y) == false) return false;
            int xDir, yDir, xTemp = getX(), yTemp = getY();
            xDir = x-this.getX() > 0? 1 : -1;
            xDir = x-this.getX() == 0? 0 : xDir;
            yDir = y-this.getY() > 0? 1 : -1;
            yDir = y-this.getY() == 0? 0 : yDir;

            while(xTemp != x || yTemp != y) {
                xTemp+= xDir;
                yTemp+= yDir;
                if (pieceMovement(xTemp, yTemp) == false) return false;
                if( board.peek(xTemp,yTemp) != null && (xTemp!=x || yTemp!=y)) return false;
            }

            if(board.peek(x,y) != null && board.peek(x,y).getOwner() == getOwner()) return false;
            return (xTemp==x && yTemp==y);
        }
    abstract public Color getColor();
    public int getOwner() {
        return owner;
    }
}
