package si.szachy;

import si.szachy.pieces.Piece;

import java.util.ArrayList;
import java.util.Random;

// TODO: napisac logike AI
public class PlayerAI {
    private ArrayList<Piece> playerPieces;
    private int playerTeam;
    private Chessboard board;
    PlayerAI(Chessboard b, int team) {
        playerPieces = new ArrayList<>();
        playerTeam = team;
        board = b;
        for(Piece p : board.getPieces()) {
            if(p.getOwner() == playerTeam)
                playerPieces.add(p);
        }
    }
    public int getPlayerTeam() {
        return playerTeam;
    }
    private void updateList() {
        ArrayList<Piece> deadPieces = new ArrayList<>();
        for(Piece p : playerPieces) {
            if(p.alive == false)
                deadPieces.add(p);
        }
        for(Piece p : deadPieces)
            playerPieces.remove(deadPieces);
    }
    public void performRandomMove() {
        Piece selectedPiece = null;
        ArrayList<Coordinate> coords = new ArrayList<>();
        int validMoves = 0;
        updateList();
        int size = playerPieces.size();
        Random generator = new Random();
        while(validMoves <= 0) {
            int i = generator.nextInt(size);
            selectedPiece = playerPieces.get(i);
            coords = selectedPiece.getAllValidMoves();
            validMoves = coords.size();
        }
        int i = generator.nextInt(validMoves);
        Coordinate c = coords.get(i);
        if (board.peek(c) != null && board.peek(c).getOwner() != selectedPiece.getOwner()) {
            board.peek(c).die();
        }
        selectedPiece.setCoord(c);
    }
}
