package si.szachy;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Chessboard board;
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

    }

}
