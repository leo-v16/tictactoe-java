package api;

public class GameAPI {

    public static final int X = 1; // Maximizing Player
    public static final int O = -1; // Minimizing Player
    public static final int DRAW = 0;
    public static final int EMPTY = 0;

    public static int getMove(int[] board, int turn) {
        int bestMove = -1, bestScore = (turn == X)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int move=0; move<9; move++) {
            if (board[move] == EMPTY) {
                board[move] = turn;
                int score = evaluate(board, -turn, 0);
                board[move] = EMPTY;
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

    public static int evaluate(int[] board, int turn, int depth) {
        if (state(board) != DRAW) return ((10-depth)*state(board));
        int bestScore = (turn == X)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int moveCount = 0;
        for (int move=0; move<9; move++) {
            if (board[move] == EMPTY) {
                moveCount++;
                board[move] = turn;
                int score = evaluate(board, -turn, depth + 1);
                board[move] = EMPTY;
                if (turn == X && score > bestScore)
                    bestScore = score;
                else if (turn == O && score < bestScore) 
                    bestScore = score;
            }
        }
        return (moveCount == 0)? DRAW : bestScore;
    }

    public static int state(int[] board) {
        int[][] board2D = new int[3][3];
        for (int i=0; i<9; i++) 
            board2D[i/3][i%3] = board[i];

        if (board2D[0][0] == board2D[1][1] && board2D[1][1] == board2D[2][2])
            return board2D[1][1];

        if (board2D[0][2] == board2D[1][1] && board2D[1][1] == board2D[2][0])
            return board2D[1][1];
        
        for (int pos=0; pos<3; pos++) {
            if (board2D[pos][0] == board2D[pos][1] && board2D[pos][1] == board2D[pos][2])
                return board2D[pos][1];
            if (board2D[0][pos] == board2D[1][pos] && board2D[1][pos] == board2D[2][pos])
                return board2D[1][pos];

        }
        
        return DRAW;
    }
}
