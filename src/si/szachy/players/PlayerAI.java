package si.szachy.players;

import org.jetbrains.annotations.NotNull;
import si.szachy.Chessboard;
import si.szachy.Coordinate;
import si.szachy.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class PlayerAI extends Player {
    private final int DEPTH;
    public ArrayList<tuple<Coordinate, Piece>> moves = new ArrayList<>();
    public int counter = 0;
    private int moveCount;

    public PlayerAI(Chessboard board, int playerTeam) {
        super(board, playerTeam);
        DEPTH = 2;
    }

    public PlayerAI(Chessboard board, int depth, int playerTeam) {
        super(board, playerTeam);
        DEPTH = depth;
    }

    private boolean preventLooping(tuple<Coordinate, Piece> m) {
        int threshold = 1;
        int counter = 0;
        for (tuple<Coordinate, Piece> t : moves) {
            boolean positionTest = t.key.getX() == m.key.getX() && t.key.getY() == m.key.getY();
            boolean pieceTest = t.value == m.value;
            if (positionTest && pieceTest) counter++;
        }
        return counter >= threshold;
    }

    public void performMove() {
        updateList();
        move();
    }

    // TODO: to tez jest malo optymalne ale nie chce mi sie myslec
    public int getMoveCount() {
        updateList();
        int count = 0;
        for (Piece p : playerPieces)
            count += p.getAllValidMoves().size();
        return count;
    }

    private void move() {
        tuple<Coordinate, Double> move = null;
        Piece toMove = null;
        Coordinate destination = null;
        Double bestValue = -999999.0;
        tuple<Coordinate, Piece> test = new tuple<>(null, null);
        if (moves.size() > 20) moves.clear();
        for (Piece p : playerPieces) {
            List<Coordinate> possibleMoves = p.getAllValidMoves();
            if (!possibleMoves.isEmpty()) {
                move = findBestMove(p, possibleMoves);
                if (move.value > bestValue) {
                    test.key = move.key;
                    test.value = p;
                    if (!preventLooping(test)) {
                        toMove = p;
                        destination = move.key;
                        bestValue = move.value;
                    } else bestValue -= 10;
                }
            }
        }
        //To niżej można przerzucić do jakiejś oddzielnej funckji
        counter = 0;
        if (toMove != null) {
            tuple<Coordinate, Piece> mov = new tuple<>(destination, toMove);
            moves.add(mov);
            if (board.peek(destination) != null && board.peek(destination).getOwner() != toMove.getOwner()) {
                board.peek(destination).die();
            }
            toMove.setCoord(destination);
        }
    }

    private tuple<Coordinate, Double> findBestMove(Piece p, @NotNull List<Coordinate> possibleMoves) {
        Double bestValue = -999999.0;
        Double actualValue = 0.0;
        Coordinate bestMove = possibleMoves.get(0);

        for (Coordinate c : possibleMoves) {
            Piece at = board.peek(c);
            Coordinate prev = p.getCoord();

            if (at != null) {
                at.die();
            }

            p.setCoord(c);
            board.setField(c.getX(), c.getY(), p);
            board.setField(prev.x, prev.y, null);

            actualValue = minimax(DEPTH, (playerTeam + 1) % 2, -999999.0, 999999.0);

            if (actualValue > bestValue) {
                bestMove = c;
                bestValue = actualValue;
            }

            p.setCoord(prev);
            if (at != null) {
                at.isAlive = true;
                board.addPiece(at);
            }
            board.setField(c.getX(), c.getY(), at);
            board.setField(prev.x, prev.y, p);
        }

        return new tuple<>(bestMove, bestValue);
    }

    private double evaluateBoard() {
        double value = 0;
        for (Piece p : board.getPieces()) {
            if (p.getOwner() == playerTeam)
                value += p.getValue();
            else
                value -= p.getValue();
        }

        return value;
    }

    private Double minimax(int depth, int playerTeam, double alfa, double beta) {
        counter++;
        if (depth == 0)
            return evaluateBoard();

        tuple<Coordinate, Double> bestMove = null;
        Piece toMove;
        Double bestValue = this.playerTeam == playerTeam ? -999999.0 : 999999.0, nextMoveValue;

        for (Piece p : playerTeam == this.playerTeam ? playerPieces : oppositorPieces) {
            if (p.isAlive) {
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
                    if (playerTeam == this.playerTeam) {
                        bestValue = Math.max(bestValue, nextMoveValue);
                        alfa = Math.max(bestValue, alfa);
                    } else {
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

    private class tuple<K, V> {
        K key;
        V value;

        tuple(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
