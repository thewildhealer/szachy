package si.szachy;

import si.szachy.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Chessboard {
    private ArrayList<Piece> pieces;
    private ArrayList<Piece>[] playersPieces;
    private Piece[] board;
    private final int width = 8, height = 8;
    public Piece[] kings = new Piece[2];

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public List<Piece> getPieces(int owner) {
        return new ArrayList<>(playersPieces[owner]);
    }

    public void removePiece(Piece p) {
        pieces.remove(p);
        playersPieces[p.getOwner()].remove(p);
    }

    public void wake(Piece p) {
        p.isAlive = true;
        pieces.add(p);
        playersPieces[p.getOwner()].add(p);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Chessboard() {
        playersPieces = new ArrayList[2];
        playersPieces[0] = new ArrayList<>();
        playersPieces[1] = new ArrayList<>();
        initializeChessboard();
    }

    public void addPiece(Piece p) {
        pieces.add(p);
        playersPieces[p.getOwner()].add(p);
        if (pieces.size() > 1) {
            int i = pieces.size() - 1;
            while (pieces.get(i - 1).getOwner() < pieces.get(i).getOwner()) {
                Collections.swap(pieces, i - 1, i);
                if (i > 1) i--;
                else break;
            }
        }
        if (p.getClass() == King.class)
            kings[p.getOwner()] = p;

    }

    public void setField(int x, int y, Piece p) {
        board[x * width + y] = p;
    }

    public void updateChessboard() {
        Arrays.fill(board, null);
        for (Piece p : pieces) {
            setField(p.getX(), p.getY(), p);
        }
    }

    private void initializeChessboard() {
        pieces = new ArrayList<>();
        board = new Piece[width * height];
        for (int i = 0; i < width * height; i++)
            board[i] = null;
    }

    public Piece peek(Coordinate c) {
        return peek(c.x, c.y);
    }

    public Piece peek(int x, int y) {
        return board[x * width + y];
    }

    public void newGame(boolean rotated) {
        pieces.clear();
        for (int i = 0; i < 8 * 8; i++)
            board[i] = null;

        int ownerLower = rotated ? 1 : 0;
        int ownerUpper = rotated ? 0 : 1;

        // player 1
        for (int i = 0; i < 8; i++)
            this.addPiece(new Pawn(this, new Coordinate(i, 1), ownerUpper, rotated));
        this.addPiece(new Rook(this, new Coordinate(0, 0), ownerUpper));
        this.addPiece(new Rook(this, new Coordinate(7, 0), ownerUpper));
        this.addPiece(new Knight(this, new Coordinate(1, 0), ownerUpper));
        this.addPiece(new Knight(this, new Coordinate(6, 0), ownerUpper));
        this.addPiece(new Bishop(this, new Coordinate(2, 0), ownerUpper));
        this.addPiece(new Bishop(this, new Coordinate(5, 0), ownerUpper));
        this.addPiece(new Queen(this, new Coordinate(3, 0), ownerUpper));
        this.addPiece(new King(this, new Coordinate(4, 0), ownerUpper));

        // player 0
        for (int i = 0; i < 8; i++)
            this.addPiece(new Pawn(this, new Coordinate(i, 6), ownerLower, rotated));
        this.addPiece(new Rook(this, new Coordinate(0, 7), ownerLower));
        this.addPiece(new Rook(this, new Coordinate(7, 7), ownerLower));
        this.addPiece(new Knight(this, new Coordinate(1, 7), ownerLower));
        this.addPiece(new Knight(this, new Coordinate(6, 7), ownerLower));
        this.addPiece(new Bishop(this, new Coordinate(2, 7), ownerLower));
        this.addPiece(new Bishop(this, new Coordinate(5, 7), ownerLower));
        this.addPiece(new Queen(this, new Coordinate(3, 7), ownerLower));
        this.addPiece(new King(this, new Coordinate(4, 7), ownerLower));
    }
}
