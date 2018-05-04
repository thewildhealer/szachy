package si.szachy.players;

import org.jetbrains.annotations.NotNull;
import si.szachy.Chessboard;
import si.szachy.Coordinate;
import si.szachy.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlayerAI extends Player {
    private static int DEPTH = 4;

    public PlayerAI(Chessboard board, int playerTeam) {
        super(board, playerTeam);
    }

    public void performMove() {
        updateList();
        move();
    }

    private void move(){
        List<triple<Piece, Coordinate, Future<Double>>> moves = new ArrayList<>();
        Piece toMove = null;
        Coordinate destination = null;
        Double bestValue = -999999.0;

        for(Piece p: playerPieces){
            List<Coordinate> possibleMoves = p.getAllValidMoves();
            if(!possibleMoves.isEmpty()) {
                moves.addAll(findBestMove(p, possibleMoves));
            }
        }

        for(triple<Piece, Coordinate, Future<Double>> move : moves){
            try{
                if(move.ext.get() > bestValue){
                    bestValue = move.ext.get();
                    toMove = move.key;
                    destination = move.value;
                }
            } catch (Exception e) {}
        }

        //To niżej można przerzucić do jakiejś oddzielnej funckji
        if(toMove != null) {
            if (board.peek(destination) != null && board.peek(destination).getOwner() != toMove.getOwner()) {
                board.peek(destination).die();
            }
            toMove.setCoord(destination);
        }
    }

    private List<triple<Piece, Coordinate, Future<Double>>> findBestMove(Piece p, @NotNull List<Coordinate> possibleMoves){
        Double bestValue = -999999.0;
        Double actualValue = 0.0;
        Coordinate bestMove = possibleMoves.get(0);

        List<triple<Piece, Coordinate, Future<Double>>> valuesForeachMove = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(possibleMoves.size());

        for(Coordinate c : possibleMoves){
            Piece at = board.peek(c);
            Coordinate prev = p.getCoord();

            if(at != null){
                at.die();
            }

            p.setCoord(c);
            board.setField(c.getX(), c.getY(), p);
            board.setField(prev.x, prev.y, null);

            Chessboard copyBoard = new Chessboard();
            for(Piece piece : board.getPieces()){
                copyBoard.addPiece(piece.createCopy(copyBoard));
            }
            MiniMax miniMax = new MiniMax(PlayerAI.DEPTH, (playerTeam+1)%2, playerTeam, copyBoard);
            valuesForeachMove.add(new triple<>(p, c, executorService.submit(miniMax)));
            //actualValue = minimax(PlayerAI.DEPTH, (playerTeam+1)%2, -999999.0, 999999.0);

            p.setCoord(prev);
            if(at != null) {
                at.isAlive = true;
                board.addPiece(at);
            }
            board.setField(c.getX(), c.getY(), at);
            board.setField(prev.x, prev.y, p);
        }

        return valuesForeachMove;
    }
/*
    private double evaluateBoard(){
        double value = 0;
        for(Piece p: board.getPieces()){
            if(p.getOwner() == playerTeam)
                value += p.getValue();
            else
                value -= p.getValue();
        }

        return value;
    }


    private Double minimax(int depth, int playerTeam, double alfa, double beta){
        counter++;
        if(depth == 0)
            return evaluateBoard();

        tuple<Coordinate, Double> bestMove = null;
        Piece toMove;
        Double bestValue = this.playerTeam == playerTeam ? -999999.0 : 999999.0, nextMoveValue;

            for (Piece p : playerTeam == this.playerTeam ? playerPieces : oppositorPieces) {
                if(p.isAlive) {
                    List<Coordinate> possibleMoves = p.getAllValidMoves();

                    for (Coordinate destination : possibleMoves) {

                        Piece at = board.peek(destination);
                        Coordinate previousCoords = p.getCoord();

                        if (at != null) {
                            at.die();
                        }

                        p.setCoord(destination);
                        board.setField(destination.getX(), destination.getY(), p);
                        board.setField(previousCoords.x, previousCoords.y, null);
                        //*******************************************************************

                        nextMoveValue = minimax(depth - 1, (playerTeam + 1) % 2, alfa, beta);
                        if(playerTeam == this.playerTeam) {
                            bestValue = Math.max(bestValue, nextMoveValue);
                            alfa = Math.max(bestValue, alfa);
                        }
                        else {
                            bestValue = Math.min(bestValue, nextMoveValue);
                            beta = Math.min(bestValue, beta);
                        }

                        p.setCoord(previousCoords);
                        if (at != null) {
                            at.isAlive = true;
                            board.addPiece(at);
                        }
                        board.setField(destination.getX(), destination.getY(), at);
                        board.setField(p.getX(), p.getY(), p);

                       if (alfa >= beta)
                           return bestValue;
                    }
                }
            }

        return bestValue;
    }
*/
}

class tuple<K, V>{
    K key;
    V value;

    tuple(K key, V value){
        this.key = key;
        this.value = value;
    }
}

class triple<K, V, E>{
    K key;
    V value;
    E ext;
    triple(K key, V value, E ext){
        this.key = key;
        this.value = value;
        this.ext = ext;
    }
}

class MiniMax implements Callable<Double>{

    private Chessboard board;
    private int depth;
    private int actualPlayer;
    private int owner;
    private static final double alpha = -999999.0;
    private static final double beta =  999999.0;

    MiniMax(int depth, int firstPlayer, int owner, Chessboard board){
        this.depth = depth;
        this.board = board;
        actualPlayer = firstPlayer;
        this.owner = owner;
    }


    @Override
    public Double call() {
        return evaluateMoves(depth, alpha, beta, actualPlayer);
    }

    private double evaluateMoves(int depth, double alpha, double beta, int actualPlayer) {
        if (depth == 0)
            return evaluateBoard();

        double minmax = actualPlayer == owner ? MiniMax.alpha : MiniMax.beta;

        for (Piece p : board.getPieces(actualPlayer)) {
            if (p.isAlive) {
                for (Coordinate destination : p.getAllValidMoves()) {
                    Piece opponent = board.peek(destination);
                    Coordinate previousCoords = p.getCoord();

                    if (opponent != null) opponent.die();

                    p.setCoord(destination);
                    board.setField(destination.x, destination.y, p);
                    board.setField(previousCoords.x, previousCoords.y, null);

                    if (actualPlayer == owner) {
                        minmax = Math.max(minmax, evaluateMoves(depth - 1, alpha, beta, (actualPlayer + 1)%2));
                        alpha = Math.max(minmax, alpha);
                    }
                    else {
                        minmax = Math.min(minmax, evaluateMoves(depth - 1, alpha, beta, (actualPlayer + 1)%2));
                        beta = Math.min(minmax, beta);
                    }

                    p.setCoord(previousCoords);

                    if (opponent != null) board.wake(opponent);

                    board.setField(destination.getX(), destination.getY(), opponent);
                    board.setField(p.getX(), p.getY(), p);


                    if (alpha >= beta)
                        return minmax;
                }
            }
        }
        return minmax;
    }

    private double evaluateBoard(){
        double value = 0;
        for(Piece p: board.getPieces()){
            if(p.getOwner() == owner)
                value += p.getValue();
            else
                value -= p.getValue();
        }
        return value;
    }
}
