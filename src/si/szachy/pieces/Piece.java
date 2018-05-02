package si.szachy.pieces;

import si.szachy.Chessboard;
import si.szachy.Coordinate;

import java.awt.*;
import java.util.ArrayList;

abstract public class Piece {
    protected Chessboard board;
    protected Coordinate coord;
    protected int owner;
    protected Image image;
    protected String name;
    public boolean didMove = false;
    ArrayList<Coordinate> validMoves;

    public Piece(Chessboard b, Coordinate c, int o) {
        this.board = b;
        this.coord = c;
        this.owner = o;
        validMoves = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isFieldDangerous(int x, int y) {
        ArrayList<Piece> pieces = board.getPieces();
        if (name.equals("king")) return false;
        for (Piece p : pieces) {
            if (p.canReach(x, y))
                return true;
        }
        return false;
    }

    public boolean isInDanger() {
        ArrayList<Piece> pieces = board.getPieces();
        for (Piece p : pieces) {
            if (p.canReach(getX(), getY()))
                return true;
        }
        return false;
    }

    public Image getImage() {
        return image;
    }

    public void die() {
        board.removePiece(this);
    }

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

    // TODO: do poprawy calosc
    // TODO: dodac bicie
    public boolean wouldKingBeInDanger(Coordinate c) {
        Coordinate oldCoord = getCoord();
        this.setCoord(c);
        board.updateChessboard();
        boolean test = board.kings[owner].isInDanger();
        this.setCoord(oldCoord);
        board.updateChessboard();
        return test;
    }

    protected void setImage() {
        Toolkit t = Toolkit.getDefaultToolkit();
        String path = "./src/si/szachy/images/";
        path = path + name + owner + ".png";
        image = t.getImage(path);
    }

    abstract protected boolean pieceMovement(int x, int y);

    public boolean isValidMove(int x, int y) {
        ArrayList<Coordinate> list = getAllValidMoves();
        for (Coordinate c : list) {
            if (c.x == x && c.y == y) return true;
        }
        return false;
    }

    public boolean canReach(int x, int y) {
        if (pieceMovement(x, y) == false) return false;
        int xDir, yDir, xTemp = getX(), yTemp = getY();
        xDir = x - this.getX() > 0 ? 1 : -1;
        xDir = x - this.getX() == 0 ? 0 : xDir;
        yDir = y - this.getY() > 0 ? 1 : -1;
        yDir = y - this.getY() == 0 ? 0 : yDir;

        while (xTemp != x || yTemp != y) {
            xTemp += xDir;
            yTemp += yDir;
            if (pieceMovement(xTemp, yTemp) == false) return false;
            if (board.peek(xTemp, yTemp) != null && (xTemp != x || yTemp != y)) return false;
        }

        if (board.peek(x, y) != null && board.peek(x, y).getOwner() == getOwner()) return false;
        return (xTemp == x && yTemp == y);
    }

    public int getOwner() {
        return owner;
    }

    public void move(int x, int y) {
        setCoord(new Coordinate(x, y));
    }

    public ArrayList<Coordinate> getAllValidMoves() {
        validMoves.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canReach(i, j)) {
                    if (board.kings[owner].isInDanger() == true && wouldKingBeInDanger(new Coordinate(i, j)) == false)
                        validMoves.add(new Coordinate(i, j));
                    else if (board.kings[owner].isInDanger() == false)
                        validMoves.add(new Coordinate(i, j));
                }
            }
        }
        return validMoves;
    }
}
