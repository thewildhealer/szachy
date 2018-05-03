package si.szachy.player;

import si.szachy.Chessboard;
import si.szachy.pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;

abstract public class Player implements Serializable {
    protected ArrayList<Piece> playerPieces = new ArrayList<>();
    protected ArrayList<Piece> opponentPieces = new ArrayList<>();
    protected Chessboard board;
    protected int playerTeam;

    public Player(Chessboard board, int playerTeam) {
        this.playerTeam = playerTeam;
        this.board = board;

        for (Piece p : board.getPieces()) {
            if (p.getOwner() == playerTeam)
                playerPieces.add(p);
            else
                opponentPieces.add(p);
        }
    }

    public int getPlayerTeam() {
        return playerTeam;
    }

    protected void updateList() {
        playerPieces.clear();
        opponentPieces.clear();
        for (Piece p : board.getPieces()) {
            if (p.getOwner() == playerTeam)
                playerPieces.add(p);
            else
                opponentPieces.add(p);
        }
        board.updateChessboard();
    }
}
