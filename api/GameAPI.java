package api;

public class GameAPI {

    public static final int X = 1; // Maximizing Player
    public static final int O = -1; // Minimizing Player
    public static final int DRAW = 0;
    public static final int EMPYT = 0;

    public int getMove(int[] board, int turn) {
        int bestMove = -1, bestScore = (turn == X)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int move=0; move<9; move++) {
            if (board[move] == EMPYT) {
                board[move] = turn;
                int score = evaluate(board, -turn, 0);
                board[move] = EMPYT;
                if (turn == X && score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                } else if (turn == O && score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    public int evaluate(int[] board, int turn, int depth) {
        if (state(board) != DRAW) return (-depth*state(board));
        int bestScore = (turn == X)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int moveCount = 0;
        for (int move=0; move<9; move++) {
            if (board[move] == EMPYT) {
                moveCount++;
                board[move] = turn;
                int score = evaluate(board, -turn, depth + 1);
                board[move] = EMPYT;
                if (turn == X && score > bestScore)
                    score = bestScore;
                else if (turn == O && score < bestScore) 
                    score = bestScore;
            }
        }
        return (moveCount == 0)? DRAW : bestScore;
    }

    public int state(int[] board) {
        int[][] board2D = new int[3][3];
        for (int i=0; i<3; i++) 
            board2D[i/3][i%3] = board[i];

        if (board2D[0][0] == board2D[1][1] && board2D[1][1] == board2D[2][2])
            return board2D[1][1];

        if (board2D[0][2] == board2D[1][1] && board2D[1][1] == board2D[2][0])
            return board2D[1][1];
        
        for (int row=0; row<3; row++)
            if (board2D[row][0] == board2D[row][1] && board2D[row][1] == board2D[row][2])
                return board2D[row][1];
        
        for (int col=0; col<3; col++) 
            if (board2D[0][col] == board2D[1][col] && board2D[1][col] == board2D[2][col])
                return board2D[1][col];

        return DRAW;
    }
}
