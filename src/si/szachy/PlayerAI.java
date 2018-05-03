package si.szachy;

import org.jetbrains.annotations.NotNull;
import si.szachy.pieces.Piece;

import java.util.*;

public class PlayerAI {
    private ArrayList<Piece> playerPieces = new ArrayList<>();
    private ArrayList<Piece> oppositorPieces = new ArrayList<>();
    private Chessboard board;
    private int playerTeam;
    private static int DEPTH = 2;

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
            else
                oppositorPieces.add(p);
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

    private tuple<Coordinate, Integer> findBestMove(Piece p, @NotNull List<Coordinate> possibleMoves){
        Integer bestValue = Integer.MIN_VALUE;
        Integer actualValue = 0;
        Coordinate bestMove = possibleMoves.get(0);

        for(Coordinate c : possibleMoves){
            Piece at = board.peek(c);
            Coordinate prev = p.getCoord();
            if(at != null) {
                board.removePiece(at);
                board.setField(at.getX(), at.getY(), null);
                //board.updateChessboard();
            }

            p.move(c.x, c.y);
            board.setField(c.x, c.y, p);
            //board.updateChessboard();

            actualValue = minimax(PlayerAI.DEPTH, playerTeam);

            if(actualValue > bestValue){
                bestMove = c;
                bestValue = actualValue;
            }

            p.move(prev.x, prev.y);
            board.setField(prev.x, prev.y, p);
            board.setField(c.x, c.y, at);
            //board.updateChessboard();

            if(at != null) {
                board.addPiece(at);
                //board.updateChessboard();
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

        tuple<Coordinate, Integer> bestMove = null;
        Piece toMove;
        Integer bestValue = this.playerTeam == playerTeam ? Integer.MIN_VALUE : Integer.MAX_VALUE, nextMoveValue;

            for (Piece p : playerTeam == this.playerTeam ? playerPieces : oppositorPieces) {
                List<Coordinate> possibleMoves = p.getAllValidMoves();

                for (Coordinate destination : possibleMoves) {

                    Piece at = board.peek(destination);
                    Coordinate previousCoords = p.getCoord();

                    if(at != null) {
                        board.removePiece(at);
                        board.updateChessboard();
                    }

                    p.move(destination.x, destination.y);
                    board.updateChessboard();

                    nextMoveValue = minimax(depth - 1, (playerTeam + 1) % 2);
                    bestValue = playerTeam == this.playerTeam ?
                            Math.max(nextMoveValue, bestValue) : Math.min(nextMoveValue, bestValue);

                    p.move(previousCoords.x, previousCoords.y);
                    board.updateChessboard();

                    if(at != null) {
                        board.addPiece(at);
                        board.updateChessboard();
                    }
                }
            }
            return bestValue;
    }

}
