package si.szachy;
import si.szachy.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;

public class Chessboard {
    private ArrayList<Piece> pieces;
    private Piece[] board;
    private int turn = 1;
    private final int width, height;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Chessboard(int w, int h) {
        this.width = w;
        this.height = h;
        initializeChessboard();
    }

    public void addPiece(Piece p) {
        pieces.add(p);
        if (pieces.size() > 1) {
            int i = pieces.size() - 1;
            while (pieces.get(i - 1).getOwner() < pieces.get(i).getOwner()) {
                Collections.swap(pieces, i - 1, i);
                if (i > 1) i--;
                else break;
            }
        }

    }
    public void setField(int x, int y, Piece p) {
        board[x + y * width] = p;
    }

    public void updateChessboard() {
        for (int i = width * height - 1; i >= 0; i--)
            board[i] = null;
        for (int i = pieces.size() - 1; i >= 0; i--)
            setField(pieces.get(i).getCoord().getX(), pieces.get(i).getCoord().getY(), pieces.get(i));
    }

    private void initializeChessboard() {
        pieces = new ArrayList<>();
        board = new Piece[width * height];
        for (int i = 0; i < width* height; i++)
            board[i] = null;
    }

    public Piece peek(Coordinate c) {
        return peek(c.x, c.y);
    }
    public Piece peek(int x, int y) {
        return board[x + y * width];
    }

    public int getTurn() {
        return turn;
    }
}
