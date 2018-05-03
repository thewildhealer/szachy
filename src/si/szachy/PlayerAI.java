package si.szachy;

import org.jetbrains.annotations.NotNull;
import org.omg.CORBA.INTERNAL;
import si.szachy.pieces.Piece;

import java.util.*;

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

    public void performMove() {
        updateList();
        move();
    }

    private void move(){
        tuple<Coordinate, Integer> move;
        Piece toMove = null;
        Coordinate destination = null;
        Integer bestValue = Integer.MIN_VALUE;

        for(Piece p: playerPieces){
            List<Coordinate> possibleMoves = p.getAllValidMoves();
            if(!possibleMoves.isEmpty()) {
                move = findBestMove(possibleMoves);
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

    private tuple<Coordinate, Integer> findBestMove(@NotNull List<Coordinate> possibleMoves){
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


    private Integer minimax(int depth, int playerTeam){
        if(depth == 0)
            return evaluateBoard();

        if(this.playerTeam == playerTeam){

        }

        return 0;
    }
    /*
    private tuple<Coordinate, Integer> minimax(int depth, int playerTeam){
        if(depth == 0)
            return new tuple<>(null, evaluateBoard());

        tuple<Coordinate, Integer> move = null, nextMove = null, bestMove = null;
        Piece toMove = null;
        Coordinate destination = null;
        Integer bestValue = Integer.MIN_VALUE;
        Integer actualValue;

        if(this.playerTeam == playerTeam){
            //Firstly find the best move for each piece
            for(Piece p: playerPieces){
                List<Coordinate> possibleMoves = p.getAllValidMoves();
                if(!possibleMoves.isEmpty()) {

                    //find the best move for p piece
                    move = findBestMove(possibleMoves);
                    if (move.value > bestValue) {
                        toMove = p;
                        destination = move.key;
                        bestValue = move.value;
                    }

                    //make move
                    Piece at = board.peek(move.key);
                    if(at != null) {
                        board.removePiece(at);
                        board.updateChessboard();
                    }

                    //go deeper

                    nextMove = minimax(depth-1, (playerTeam+1)%2);

                    //undo move

                    if(at != null) {
                        board.addPiece(at);
                        board.updateChessboard();
                    }
                }
            }
        }
        else{

        }
    }
    */
}
