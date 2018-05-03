package si.szachy;

import si.szachy.pieces.Piece;

import java.util.ArrayList;
import java.util.Random;

// TODO: napisac logike AI
public class PlayerAI {
    private ArrayList<Piece> playerPieces = new ArrayList<>();
    private Chessboard board;
    private int playerTeam;

    PlayerAI(Chessboard board, int playerTeam) {
        this.playerTeam = playerTeam;
        this.board = board;

        for(Piece p : board.getPieces()) {
            if(p.getOwner() == playerTeam)
                playerPieces.add(p);
        }
    }

    private void updateList() {
        ArrayList<Piece> deadPieces = new ArrayList<>();
        for(Piece p : playerPieces) {
            if(!p.isAlive)
                deadPieces.add(p);
        }

        for(Piece p : deadPieces)
            playerPieces.remove(p);
    }

    public int getPlayerTeam() {
        return playerTeam;
    }

    public void performRandomMove() {
        Piece selectedPiece = null;
        ArrayList<Coordinate> coords = new ArrayList<>();
        updateList();
        int size = playerPieces.size();
        Random generator = new Random();
        while(coords.isEmpty()) {
            int i = generator.nextInt(size);
            selectedPiece = playerPieces.get(i);
            coords = selectedPiece.getAllValidMoves();
        }
        int i = generator.nextInt(coords.size());
        Coordinate c = coords.get(i);
        if (board.peek(c) != null && board.peek(c).getOwner() != selectedPiece.getOwner()) {
            board.peek(c).die();
        }
        selectedPiece.setCoord(c);
    }
}
