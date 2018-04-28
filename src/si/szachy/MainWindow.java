package si.szachy;

import si.szachy.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener, KeyListener {
    private JButton nastepnaTuraButton;
    private JPanel panelMain;
    private JPanel gamePanel;
    private JTextArea comments;
    private JButton zapiszButton;
    private JButton wczytajButton;
    private JComboBox dodajOrganizmBox;
    private JTabbedPane tabbedPane1;
    private JTextPane legenda;
    private JButton calopalenieButton;
    private Chessboard board;
    private int width = 8, height = 8;
        private int rectSize = 50;
    private Color chessboardColor = new Color(0xd18b47);
    private Color chessboardSecondaryColor = new Color(0xffce9e);

    Piece selectedPiece;
    boolean isSelected;
    Timer t = new Timer(5, this);

    public MainWindow() {
        super("Konrad Zawora 165115");
        setContentPane(panelMain);
        this.board = new Chessboard(width, height);
        // player 1
        for(int i = 0 ; i < 8; i++)
            board.addPiece(new Pawn(board, new Coordinate(i,1), 1));
        board.addPiece(new Rook(board, new Coordinate(0,0), 1));
        board.addPiece(new Rook(board, new Coordinate(7,0), 1));
        board.addPiece(new Knight(board, new Coordinate(1,0), 1));
        board.addPiece(new Knight(board, new Coordinate(6,0), 1));
        board.addPiece(new Bishop(board, new Coordinate(2,0), 1));
        board.addPiece(new Bishop(board, new Coordinate(5,0), 1));
        board.addPiece(new Queen(board, new Coordinate(3,0), 1));
        board.addPiece(new King(board, new Coordinate(4,0), 1));

        // player 0
        for(int i = 0 ; i < 8; i++)
            board.addPiece(new Pawn(board, new Coordinate(i,6), 0));
        board.addPiece(new Rook(board, new Coordinate(0,7), 0));
        board.addPiece(new Rook(board, new Coordinate(7,7), 0));
        board.addPiece(new Knight(board, new Coordinate(1,7), 0));
        board.addPiece(new Knight(board, new Coordinate(6,7), 0));
        board.addPiece(new Bishop(board, new Coordinate(2,7), 0));
        board.addPiece(new Bishop(board, new Coordinate(5,7), 0));
        board.addPiece(new Queen(board, new Coordinate(4,7), 0));
        board.addPiece(new King(board, new Coordinate(3,7), 0));

        Dimension mainDim = new Dimension(width * rectSize + 500, height * rectSize + 50);
        Dimension gameDim = new Dimension(width * rectSize, height * rectSize);
        panelMain.setPreferredSize(mainDim);

        gamePanel.setPreferredSize(gameDim);
        gamePanel.setMinimumSize(gameDim);
        gamePanel.setMaximumSize(gameDim);

        nastepnaTuraButton.setFocusable(false);
        panelMain.setFocusable(false);
        gamePanel.setFocusable(false);
        comments.setFocusable(false);
        zapiszButton.setFocusable(false);
        wczytajButton.setFocusable(false);
        dodajOrganizmBox.setFocusable(false);
        tabbedPane1.setFocusable(false);
        legenda.setFocusable(false);
        calopalenieButton.setFocusable(false);


        // setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        isSelected = false;

        nastepnaTuraButton.addActionListener(but1 -> {
            update(board);
        });
        /*
        calopalenieButton.addActionListener(but2 -> {
            swiat.setCzlowiekFlag(true);
            action();
        });
        zapiszButton.addActionListener(but3 -> {
            swiat.zapiszSwiat();
        });

        wczytajButton.addActionListener(but4 -> {
            swiat.otworzSwiat();
            repaint();
        });
*/
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX() / rectSize;
                int y = e.getY() / rectSize;
                if(isSelected) {
                    if(selectedPiece.isValidMove(x,y)) {
                        if(board.peek(x,y) != null && board.peek(x,y).getOwner() != selectedPiece.getOwner()) {
                            board.peek(x,y).die();
                        }
                        selectedPiece.setCoord(new Coordinate(x, y));
                        selectedPiece.didMove = true;
                        isSelected = false;
                        selectedPiece = null;
                        repaint();
                    }
                    else {
                        isSelected = false;
                        repaint();
                    }
                }
                else if (board.peek(x,y) != null && isSelected == false) {
                     selectedPiece = board.peek(x,y);
                     isSelected = true;
                     Graphics g = gamePanel.getGraphics();
                    Graphics2D g2d = (Graphics2D) g;
                     g.setColor(Color.green);
                     Piece p = board.peek(x,y);
                     int thickness = 4;
                    Stroke oldStroke = g2d.getStroke();
                    g2d.setStroke(new BasicStroke(thickness));
                     for(int i = 0; i < width; i++) {
                         for(int j = 0; j < height; j++) {
                             if (p.isValidMove(i,j)) {
                                 if(board.peek(i,j) != null && board.peek(i,j).getOwner() != selectedPiece.getOwner()) g.setColor(Color.red);
                                 else g.setColor(Color.green);
                                 g.drawRect(i * rectSize + thickness/2, j * rectSize + thickness/2, rectSize-thickness, rectSize-thickness);
                             }
                         }
                     }
                        g.setColor(Color.magenta);
                    g.drawRect(selectedPiece.getX() * rectSize + thickness/2, selectedPiece.getY() * rectSize + thickness/2, rectSize-thickness, rectSize-thickness);
                    g2d.setStroke(oldStroke);
                }
                else {
                    isSelected = false;
                    repaint();
                }
            }
        });

        setVisible(true);
    }

    private void generateGrid() {
        int x = gamePanel.getWidth() / 10;
        int y = gamePanel.getHeight() / 10;
        Graphics g = gamePanel.getGraphics();
        g.setColor(chessboardColor);

        for (int i = 0; i < x; i+=2) {
            for (int j = 0; j < y; j+=2) {
                g.fillRect(i * rectSize, j * rectSize, rectSize, rectSize);
            }
        }
        for (int i = 1; i < x; i+=2) {
            for (int j = 1; j < y; j+=2) {
                g.fillRect(i * rectSize, j * (rectSize), rectSize, rectSize);
            }
        }
    }

    private void paintField(int x, int y) {
        Graphics g = gamePanel.getGraphics();
        g.setColor(Color.black);
        g.fillRect(x * rectSize, y * rectSize, rectSize, rectSize);
    }

    private void clearAll() {
        Graphics g = gamePanel.getGraphics();
        int x = gamePanel.getWidth() / 10;
        int y = gamePanel.getHeight() / 10;
        g.setColor(chessboardSecondaryColor);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                g.fillRect(i * rectSize, j * rectSize, rectSize, rectSize);
            }
        }

    }
/*
    private void comment(Swiat swiat) {
        ArrayList<String> komentarze = swiat.komentuj();
        comments.setText("");
        for (String kom : komentarze)
            comments.append(kom + System.getProperty("line.separator"));
        swiat.czyscKomentarze();
    }
*/
    private void update(Chessboard board) {
        int x = board.getWidth();
        int y = board.getHeight();
        board.updateChessboard();
        Graphics g = gamePanel.getGraphics();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (board.peek(i, j) != null) {
                 //   g.setColor(board.peek(i, j).getColor());
                    g.drawImage(board.peek(i,j).getImage(), i*rectSize,j*rectSize,this);
                //    g.fillRect(i * rectSize, j * rectSize, rectSize, rectSize);
                }
            }
        }

    }
/*
    private void action() {
        swiat.wykonajTure();
        comment(swiat);
        repaint();
    }
*/
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        clearAll();
        generateGrid();
        update(board);

    }

    public void actionPerformed(ActionEvent e) {
    }

    public void keyPressed(KeyEvent e) {
     /*   int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                action();
                break;
            case KeyEvent.VK_ENTER:
                action();
                break;
            case KeyEvent.VK_UP:
                swiat.setCzlowiekDirection('w');
                action();
                break;
            case KeyEvent.VK_DOWN:
                swiat.setCzlowiekDirection('s');
                action();
                break;
            case KeyEvent.VK_LEFT:
                swiat.setCzlowiekDirection('a');
                action();
                break;
            case KeyEvent.VK_RIGHT:
                swiat.setCzlowiekDirection('d');
                action();
                break;

        }
         */
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}