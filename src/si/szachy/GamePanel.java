package si.szachy;

import si.szachy.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private Chessboard board;
    private Piece selectedPiece;
    private int rectSize;
    private Color chessboardColor = new Color(0xd18b47);
    private Color chessboardSecondaryColor = new Color(0xffce9e);

    public void setBoard(Chessboard board) {
        this.board = board;
    }

    public void setRectSize(int rectSize) {
        this.rectSize = rectSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        generateGrid(g);
        update(board, g);
    }

    private void generateGrid(Graphics g) {
        int x = getWidth() / 10;
        int y = getHeight() / 10;

        g.setColor(chessboardSecondaryColor);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                g.fillRect(i * rectSize, j * rectSize, rectSize, rectSize);
            }
        }

        g.setColor(chessboardColor);

        for (int i = 0; i < x; i += 2) {
            for (int j = 0; j < y; j += 2) {
                g.fillRect(i * rectSize, j * rectSize, rectSize, rectSize);
            }
        }
        for (int i = 1; i < x; i += 2) {
            for (int j = 1; j < y; j += 2) {
                g.fillRect(i * rectSize, j * (rectSize), rectSize, rectSize);
            }
        }
    }

    public void setSelectedPiece(Piece p) {
        this.selectedPiece = p;
    }

    public void drawPieceMovement(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.green);
        int thickness = 4;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(thickness));

        ArrayList<Coordinate> validMoves = selectedPiece.getAllValidMoves();
        for (Coordinate c : validMoves) {
            int i = c.x, j = c.y;
            if (board.peek(i, j) != null && board.peek(i, j).getOwner() != selectedPiece.getOwner())
                g.setColor(Color.red);
            else g.setColor(Color.green);
            g.drawRect(i * rectSize + thickness / 2, j * rectSize + thickness / 2, rectSize - thickness, rectSize - thickness);
        }

        g.setColor(Color.magenta);
        g.drawRect(selectedPiece.getX() * rectSize + thickness / 2, selectedPiece.getY() * rectSize + thickness / 2, rectSize - thickness, rectSize - thickness);
        g2d.setStroke(oldStroke);
    }

    private void update(Chessboard board, Graphics g) {
        int x = board.getWidth();
        int y = board.getHeight();
        board.updateChessboard();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (board.peek(i, j) != null) {
                    g.drawImage(board.peek(i, j).getImage(), i * rectSize, j * rectSize, this);
                }
            }
        }
        if (selectedPiece != null) drawPieceMovement(g);

    }

}
