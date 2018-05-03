package si.szachy;

import si.szachy.pieces.King;
import si.szachy.pieces.Pawn;
import si.szachy.pieces.Piece;
import si.szachy.pieces.Queen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {
    private JPanel panelMain;
    private GamePanel gamePanel;
    private JTextArea comments;
    private JButton zapiszButton;
    private JButton wczytajButton;
    private JTabbedPane tabbedPane1;
    private final int width = 8, height = 8;
    private final int rectSize = 50;
    int turn = 0;
    private Chessboard board;
    Piece selectedPiece;
    boolean isSelected = false;
    private boolean rotated = false;
    private JMenuBar menuBar;
    private JMenu menu;

    public MainWindow() {
        super("SZACHULCE");

        board = new Chessboard();
        board.newGame(rotated);
        PlayerAI ai = new PlayerAI(board, 1);

        initializeWindow();

        // TODO: refactor tego wielkiego, brzydkiego kodu
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX() / rectSize;
                int y = e.getY() / rectSize;
                if (isSelected) {
                    if (selectedPiece.isValidMove(x, y)) {

                        if (selectedPiece.getClass() == King.class) {
                            if (selectedPiece.getX() - 2 == x) {
                                //left castling
                                board.peek(0, selectedPiece.getY()).
                                        move(selectedPiece.getX() - 1, selectedPiece.getY());
                            } else if (selectedPiece.getX() + 2 == x) {
                                //right castling
                                board.peek(width - 1, selectedPiece.getY()).
                                        move(selectedPiece.getX() + 1, selectedPiece.getY());
                            }
                        } else if (selectedPiece.getClass() == Pawn.class) {
                            if (y == board.getWidth() - 1 || y == 0) {
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
                        deselect();
                        toggleTurnAI(ai);
                    } else
                        deselect();
                } else if (board.peek(x, y) != null && !isSelected && board.peek(x, y).getOwner() == turn)
                    selectPiece(board.peek(x, y));
                else
                    deselect();
            }
        });
    }

    private void initializeWindow() {
        setContentPane(gamePanel);
        menu();
        setJMenuBar(menuBar);
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        pack();
        setVisible(true);
    }

    private void deselect() {
        isSelected = false;
        selectedPiece = null;
        gamePanel.setSelectedPiece(null);
        repaint();
    }

    private void selectPiece(Piece p) {
        selectedPiece = p;
        isSelected = true;
        gamePanel.setSelectedPiece(selectedPiece);
        gamePanel.paintImmediately(gamePanel.getVisibleRect());

    }

    private void toggleTurn() {
        turn = turn == 0 ? 1 : 0;
    }

    private void toggleTurnAI(PlayerAI player) {
        board.updateChessboard();
        gamePanel.paintImmediately(gamePanel.getVisibleRect());
        int oldTurn = turn;
        turn = player.getPlayerTeam();
        Thread aiJob = new Thread(player::performMove);
        aiJob.start();
        try {
            aiJob.join();
        } catch (InterruptedException e) {
        }
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

}