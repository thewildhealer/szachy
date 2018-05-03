package si.szachy.players;

import si.szachy.Chessboard;
import si.szachy.pieces.Piece;

import java.util.ArrayList;

abstract public class Player {
    protected ArrayList<Piece> playerPieces = new ArrayList<>();
    protected ArrayList<Piece> oppositorPieces = new ArrayList<>();
    protected Chessboard board;
    protected int playerTeam;

    public Player(Chessboard board, int playerTeam) {
        this.playerTeam = playerTeam;
        this.board = board;

        for (Piece p : board.getPieces()) {
            if (p.getOwner() == playerTeam)
                playerPieces.add(p);
            else
                oppositorPieces.add(p);
        }
    }

    // TODO: TO KONIECZNIE ZMIENIĆ!!! WYWALA SIĘ PRZY NP TWORZENIU NOWEJ GRY LUB COFANIU
    protected void updateList() {
        ArrayList<Piece> deadPieces = new ArrayList<>();
        for (Piece p : playerPieces) {
            if (!p.isAlive)
                deadPieces.add(p);
        }

        for (Piece p : deadPieces)
            playerPieces.remove(p);

        board.updateChessboard();
    }

    public int getPlayerTeam() {
        return playerTeam;
    }

}
