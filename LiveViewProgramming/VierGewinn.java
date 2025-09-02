import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class VierGewinn {
    final int rows = 6;
    final int cols = 7;
    int board[];
    int turn;

    VierGewinn() {

        this.board = new int[rows * cols];
        this.turn = 1;

    }


    // Checks if a move in the specified column is valid

    boolean isValidMove(int col) {

        if (col < 0 || col >= cols) {
            throw new IllegalArgumentException("Column " + col + " does not exist!");
        }

        return board[col] == 0;

    }

    // Checks if the board is completely full
    boolean isBoardFull() {
        return Arrays.stream(board).noneMatch(cell -> cell == 0);
    }


    VierGewinn move(int col) {

        // Check if the selected column is valid
        if (!isValidMove(col)) {
            throw new IllegalArgumentException("Invalid move in column" + col);
        }

        // Find the lowest available row in the specified column
        int x = 0;

        for (int i = rows - 1; i >= 0; i--) {
            int index = i * cols + col;
            if (board[index] == 0) {
                x = i; // Found the empty row
                break;
            } else {
                continue;
            }

        }

        // Place the token for the current player in the chosen column and row
        int index = ((x) * cols + col);
        board[index] = turn;

        // Switch turns
        turn = -turn;

        return this;
    }
    

    
    boolean checkWin(int turn) {

        // Define directions for checking: horizontal, vertical,
        // and diagonal (up-right, down-right)
        int[] directions = { 1, cols, cols + 1, cols - 1 };

        for (int startIdx = 0; startIdx < board.length; startIdx++) {

            if (board[startIdx] != turn) {
                continue;
            }

            for (int direction : directions) {

                boolean win = true;

                int[] winningTokens = new int[4];
                winningTokens[0] = startIdx;
                // Check the next 3 fields in the current direction
                for (int step = 1; step < 4; step++) {
                    int nextIdx = startIdx + step * direction;
                    // Check if the next field is valid
                    if (nextIdx < 0 || nextIdx >= board.length ||
                            (direction == 1 && nextIdx / cols != startIdx / cols) ||
                            (direction == cols - 1 && nextIdx % cols > startIdx % cols) ||
                            (direction == cols + 1 && nextIdx % cols < startIdx % cols)) {
                        win = false;
                        break;
                    }

                    // Check if the player occupies this field
                    if (board[nextIdx] != turn) {
                        win = false;
                        break;
                    }

                    winningTokens[step] = nextIdx;

                }

            }

        }

        return false; // No win detected

    }



    // Calculates the score based on the number of player and opponent stones
    final BiFunction<Integer, Integer, Integer> calculateThreatScore = (playerCount, opponentCount) -> {
        if (playerCount > 0 && opponentCount > 0)
            return 0; // Mixed threat irrelevant
        if (playerCount == 4)
            return 1000; // Win
        if (playerCount == 3)
            return 10; // Potential win
        if (playerCount == 2)
            return 5;
        if (opponentCount == 4)
            return -1000; // Opponent wins
        if (opponentCount == 3)
            return -10;
        if (opponentCount == 2)
            return -5;
        return 0; // No threat
    };
    

    public String getSymbol(int value) {
        return switch (value) {
            case 1 -> "O"; // Player 1
            case -1 -> "X"; // Player 2(KI)
            default -> "-"; // Empty field
        };
    }

    // Generates a string representation of the board for display

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(" \n   ");

        // Column numbers
        for (int col = 0; col < cols; col++) {
            res.append(col).append("   ");
        }
        res.append("\n");

        // Playfield with row and column display
        for (int row = 0; row < rows; row++) {
            res.append("  ");
            for (int col = 0; col < cols; col++) {
                int value = board[row * cols + col];
                res.append(getSymbol(value)).append(" | ");
            }
            res.append("\n");
        }
        return res.toString();
    }

    

}