package si.szachy;

import org.jetbrains.annotations.NotNull;
import si.szachy.pieces.Piece;

import java.util.*;

// TODO: napisac logike AI
public class PlayerAI {
    private ArrayList<Piece> playerPieces = new ArrayList<>();
    private Chessboard board;
    private int playerTeam;

    private class tuple<K, V>{
        K key;
        V value;

        tuple(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

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

        board.updateChessboard();
    }

    public int getPlayerTeam() {
        return playerTeam;
    }

    //Taka nazwa funkcji, bo nie chciało mi się zmieniać czegoś w kodzie poza tą klasą
    public void performRandomMove() {
        updateList();
        move();
    }
    /*public void performRandomMove() {
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
    }*/

    private void move(){
        tuple<Coordinate, Integer> move;
        Piece toMove = null;
        Coordinate destination = null;
        Integer bestValue = Integer.MIN_VALUE;

        for(Piece p: playerPieces){
            List<Coordinate> possibleMoves = p.getAllValidMoves();
            if(!possibleMoves.isEmpty()) {
                move = findBestMove(p, possibleMoves);
                if (move.value > bestValue) {
                    toMove = p;
                    destination = move.key;
                    bestValue = move.value;
                }
            }
        }

        //To niżej można przerzucić do jakiejś oddzielnej funckji
        if(toMove != null) {
            if (board.peek(destination) != null && board.peek(destination).getOwner() != toMove.getOwner()) {
                board.peek(destination).die();
            }
            toMove.setCoord(destination);
        }
    }

    private tuple<Coordinate, Integer> findBestMove(Piece piece, @NotNull List<Coordinate> possibleMoves){
        Integer bestValue = Integer.MIN_VALUE;
        Integer actualValue = 0;
        Coordinate bestMove = possibleMoves.get(0);

        for(Coordinate c : possibleMoves){
            Piece at = board.peek(c);
            if(at != null) {
                board.removePiece(at);
                board.updateChessboard();
            }
            actualValue = evaluateBoard();
            if(actualValue > bestValue){
                bestMove = c;
                bestValue = actualValue;
            }
            if(at != null) {
                board.addPiece(at);
                board.updateChessboard();
            }
        }

        return new tuple<>(bestMove, bestValue);
    }

    private int evaluateBoard(){
        int value = 0;
        for(Piece p: board.getPieces()){
            if(p.getOwner() == playerTeam)
                value += p.getValue();
            else
                value -= p.getValue();
        }

        return value;
    }


}
