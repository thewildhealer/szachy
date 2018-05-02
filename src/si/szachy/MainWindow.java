package si.szachy;

import si.szachy.pieces.King;
import si.szachy.pieces.Pawn;
import si.szachy.pieces.Piece;
import si.szachy.pieces.Queen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow extends JFrame implements ActionListener, KeyListener {
    private JPanel panelMain;
    private GamePanel gamePanel;
    private JTextArea comments;
    private JButton zapiszButton;
    private JButton wczytajButton;
    private JTabbedPane tabbedPane1;
    private int width = 8, height = 8;
    private int rectSize = 50;
    int turn = 0;
    private Chessboard board;
    Piece selectedPiece;
    boolean isSelected;
    private boolean rotated = false;
    private JMenuBar menuBar;
    private JMenu menu;
    public MainWindow() {
        super("Konrad Zawora 165115");
        setContentPane(gamePanel);
        menu();
        setJMenuBar(menuBar);
        board = new Chessboard(width, height);
        board.newGame(rotated);

        Dimension mainDim = new Dimension(width * rectSize + 500, height * rectSize + 50);
        Dimension gameDim = new Dimension(width * rectSize, height * rectSize);
        panelMain.setPreferredSize(mainDim);
        gamePanel.setPreferredSize(gameDim);

        panelMain.setFocusable(false);
        gamePanel.setFocusable(false);
        comments.setFocusable(false);
        zapiszButton.setFocusable(false);
        wczytajButton.setFocusable(false);
        tabbedPane1.setFocusable(false);

        gamePanel.setBoard(board);
        gamePanel.setRectSize(rectSize);

        setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        isSelected = false;
        PlayerAI ai = new PlayerAI(board, 1);

        // TODO: refactor tego wielkiego, brzydkiego kodu
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX() / rectSize;
                int y = e.getY() / rectSize;
                if (isSelected) {
                    if (selectedPiece.isValidMove(x, y)) {

                        if(selectedPiece.getClass() == King.class){
                            if(selectedPiece.getX() - 2 == x){
                                //left castling
                                board.peek(0, selectedPiece.getY()).
                                        move(selectedPiece.getX() - 1, selectedPiece.getY());
                            }
                            else if(selectedPiece.getX() + 2 == x){
                                //right castling
                                board.peek(width - 1, selectedPiece.getY()).
                                        move(selectedPiece.getX() + 1, selectedPiece.getY());
                            }
                        }
                        else if(selectedPiece.getClass() == Pawn.class){
                            if(y == board.getWidth() - 1 || y == 0){
                                //Promotion to queen
                                Piece queen = new Queen(board, selectedPiece.getCoord(), selectedPiece.getOwner());
                                board.addPiece(queen);
                                board.removePiece(selectedPiece);
                                selectedPiece = queen;
                            }
                        }

                        if (board.peek(x, y) != null && board.peek(x, y).getOwner() != selectedPiece.getOwner()) {
                            board.peek(x, y).die();
                        }
                        selectedPiece.move(x, y);
                        selectedPiece.didMove = true;
                        isSelected = false;
                        selectedPiece = null;
                        //toggleTurn();
                        toggleTurnAI(ai);
                        repaint();
                    } else {
                        isSelected = false;
                        repaint();
                    }
                } else if (board.peek(x, y) != null && isSelected == false && board.peek(x, y).getOwner() == turn) {
                    selectedPiece = board.peek(x, y);
                    isSelected = true;
                    Graphics g = gamePanel.getGraphics();
                    Graphics2D g2d = (Graphics2D) g;
                    g.setColor(Color.green);
                    Piece p = board.peek(x, y);
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
                    /*
                    if(selectedPiece.getClass() == King.class){
                        if(((King)selectedPiece).leftCastling()){
                            g.setColor(Color.black);
                            g.drawRect((selectedPiece.getX() - 2) * rectSize + thickness / 2, selectedPiece.getY() * rectSize + thickness / 2, rectSize - thickness, rectSize - thickness);
                        }
                        if(((King)selectedPiece).rightCastling()){
                            g.setColor(Color.black);
                            g.drawRect((selectedPiece.getX() + 2) * rectSize + thickness / 2, selectedPiece.getY() * rectSize + thickness / 2, rectSize - thickness, rectSize - thickness);
                        }
                    }
                    */
                    g.setColor(Color.magenta);
                    g.drawRect(selectedPiece.getX() * rectSize + thickness / 2, selectedPiece.getY() * rectSize + thickness / 2, rectSize - thickness, rectSize - thickness);
                    g2d.setStroke(oldStroke);
                } else {
                    isSelected = false;
                    repaint();
                }
            }
        });

        setVisible(true);
    }

    private void toggleTurn() {
        turn = turn == 0 ? 1 : 0;
    }
    private void toggleTurnAI(PlayerAI player) {
        int oldTurn = turn;
        turn = player.getPlayerTeam();
        player.performRandomMove();
        turn = oldTurn;
    }
    // TODO: ogarnac ocb z zaznaczeniami radiobuttonow
    private void menu() {
        JMenu submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        menuBar = new JMenuBar();

        menu = new JMenu("Game");
        submenu = new JMenu("New Game");
        JMenuItem pvp = new JMenuItem("Player vs Player");
        submenu.add(pvp);
        pvp.addActionListener(e -> {
            board.newGame(rotated);
            turn = 0;
            repaint();
        });

        menuItem = new JMenuItem("Player vs AI");
        submenu.add(menuItem);
        menu.add(submenu);

        menuItem = new JMenuItem("AI vs AI");
        submenu.add(menuItem);
        menu.add(submenu);

        menuBar.add(menu);

        menu = new JMenu("AI Difficulty");
        ButtonGroup group2 = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("Easy");
        group2.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Medium");
        rbMenuItem.setSelected(true);
        group2.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Hard");
        group2.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Very Hard");
        group2.add(rbMenuItem);
        menu.add(rbMenuItem);
        menuBar.add(menu);


        menu = new JMenu("Preferences");
        ButtonGroup group3 = new ButtonGroup();
        JRadioButtonMenuItem white = new JRadioButtonMenuItem("White");
        rbMenuItem.setSelected(true);
        group3.add(white);
        menu.add(white);
        white.addActionListener(e -> rotated = false);

        JRadioButtonMenuItem black = new JRadioButtonMenuItem("Black");
        group3.add(black);
        menu.add(black);
        black.addActionListener(e -> rotated = true);
        menuBar.add(menu);


    }

    public void actionPerformed(ActionEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

}