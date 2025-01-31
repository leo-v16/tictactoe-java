package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import api.GameAPI;

public class GameView extends Frame implements ActionListener {
    private static final int GRID_SIZE = 3;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private int[] board = new int[GRID_SIZE * GRID_SIZE];
    private boolean playerXTurn = true;
    private Button resetButton = new Button("Reset");
    private Button selectXButton = new Button("Play as X");
    private Button selectOButton = new Button("Play as O");

    public GameView() {
        // Set up the layout and window properties
        setLayout(new BorderLayout());
        setTitle("Tic Tac Toe - AWT");
        setSize(400, 500);

        // Game grid panel
        Panel gamePanel = new Panel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                buttons[row][col] = new Button("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 48));
                buttons[row][col].setBackground(Color.LIGHT_GRAY);
                buttons[row][col].addActionListener(this);
                gamePanel.add(buttons[row][col]);
                board[row * GRID_SIZE + col] = GameAPI.EMPTY;
            }
        }

        // Control panel for options
        Panel controlPanel = new Panel();
        selectXButton.addActionListener(e -> { resetGame(); playerXTurn = true; });
        selectOButton.addActionListener(e -> { resetGame(); playerXTurn = false; aiTurn();});
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(selectXButton);
        controlPanel.add(selectOButton);
        controlPanel.add(resetButton);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Close window event
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();

        // Find the position of the clicked button
        int position = -1;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (buttons[row][col] == clickedButton) {
                    position = row * GRID_SIZE + col;
                    break;
                }
            }
        }

        // Ignore clicks on already marked buttons
        if (position == -1 || board[position] != GameAPI.EMPTY) {
            return;
        }

        // Set the move and update the UI
        board[position] = playerXTurn ? GameAPI.X : GameAPI.O;
        clickedButton.setLabel(playerXTurn ? "X" : "O");
        clickedButton.setBackground(playerXTurn ? Color.CYAN : Color.PINK);

        // Check for win or draw
        if (stateCheck()) {
            return ;
        }

        aiTurn();

        if (stateCheck()) {
            return ;
        }
    }

    private void aiTurn() {
        int aiMove = (playerXTurn)? GameAPI.getMove(board, GameAPI.O) : GameAPI.getMove(board, GameAPI.X);
        if (aiMove != -1) {
            board[aiMove] = playerXTurn? GameAPI.O : GameAPI.X;
            buttons[aiMove / GRID_SIZE][aiMove % GRID_SIZE].setLabel((playerXTurn)? "O" : "X");
            buttons[aiMove / GRID_SIZE][aiMove % GRID_SIZE].setBackground((playerXTurn)? Color.PINK : Color.CYAN);
        }
    }

    public boolean stateCheck() {
        int state = GameAPI.state(board);
        if (state != GameAPI.DRAW) {
            if (state == GameAPI.O) showResult("Player O wins!"); 
            else showResult("Player X wins!");
            return true;
        } else if(isDraw()) {
            showResult("It's a draw!");
            return true;
        }
        return false;
    }

    private boolean isDraw() {
        for (int pos : board) {
            if (pos == GameAPI.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private void showResult(String message) {
        Dialog resultDialog = new Dialog(this, "Game Over", true);
        resultDialog.setLayout(new FlowLayout());
        resultDialog.setSize(200, 100);
        Label resultLabel = new Label(message);
        Button okButton = new Button("OK");

        okButton.addActionListener(e -> resultDialog.setVisible(false));
        resultDialog.add(resultLabel);
        resultDialog.add(okButton);
        resultDialog.setVisible(true);

        resetGame();
    }

    private void resetGame() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                buttons[row][col].setLabel("");
                buttons[row][col].setBackground(Color.LIGHT_GRAY);
                board[row * GRID_SIZE + col] = GameAPI.EMPTY;
            }
        }

        if (!playerXTurn) aiTurn();
        // playerXTurn = true;
    }
}
