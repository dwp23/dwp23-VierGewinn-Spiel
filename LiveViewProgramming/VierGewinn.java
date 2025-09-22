public class VierGewinn {
    final int rows = 6;
    final int cols = 7;
    int board[];
    int turn;
    int[][] tokenCoordinates;
    ConnectFour cF;

    // a1
    VierGewinn() {

        this.board = new int[rows * cols];
        this.turn = 1;
        cF = new ConnectFour();// Creates a ConnectFour instance
    
        cF.show("Game start! Red: Human, Yellow: AI. Human starts!");
    

    
    }
    // a1

    // Checks if a move in the specified column is valid
    boolean isValidMove(int col) {

        if (col < 0 || col >= cols) {
            cF.show("Column " + col + " does not exist!");
            throw new IllegalArgumentException("Column " + col + " does not exist!");
        }

        return board[col] == 0;

    }

    // Checks if the board is completely full
    boolean isBoardFull() {
        return Arrays.stream(board).noneMatch(cell -> cell == 0);
    }


    void reset() {
        // Reset the board (clear all fields)
        Arrays.fill(board, 0);
    
        // Reset the turn to Player 1
        turn = 1;
    
        // Reset the visual board via the ConnectFour instance
        cF.resetBoard();
    
        // Display a message indicating the game has been reset
        cF.show("The game has been reset.");
    }
    
    // Executes a move and alternates the turn between the players
    // a2
    VierGewinn move(int col) {

        // Check if the selected column is valid
        if (!isValidMove(col)) {
            cF.show("Invalid move in column " + col + ", it is full.");
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

        cF.move(turn, col, x);

        // Check for a win after the move
        if (checkWin(turn)) { 
            //Highlights the winning tokens with a green border
            cF.highlightWinningTokens(tokenCoordinates);
            cF.show((turn == 1 ? "Human player" : "AI") + " has won!");

             // Add a delay to display the winning result before resetting the game
    try {
        Thread.sleep(2000); // Wait 2 seconds before resetting
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Reset the game
    reset();
            return this;
        }

        if (isBoardFull()) {

            cF.show("The board is full! The game ends in a draw");
            return this;
        }

        // Switch turns
        turn = -turn;
        if (turn == -1 ) {// check if it is the AI's turn

            int bestMove = getBestMove();// Find the best Move
            cF.showWithDelay("AI choosed column " + bestMove, 1000);
            try {
                Thread.sleep(900);
               
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            move(bestMove); // Perform the AI's move

        }
        return this;
    }
    // a2

    // Check if the specified player has won the game
    // a3
    boolean checkWin(int turn) {

        // Define directions for checking: horizontal, vertical,
        // and diagonal (up-right, down-right)
        int[] directions = { 1, cols, cols + 1, cols - 1 };

        for (int startIdx = 0; startIdx < board.length; startIdx++) {

            if (board[startIdx] != turn) {
                continue; // Skip fields that do not belong to the player
            }

            // Check in each direction
            for (int direction : directions) {

                boolean win = true;

                int[] winningTokens = new int[4]; // Indizes der Gewinnsteine
                winningTokens[0] = startIdx;
                // Check the next 3 fields in the current direction
                for (int step = 1; step < 4; step++) {
                    int nextIdx = startIdx + step * direction;
                    // Check if the next field is valid
                    if (nextIdx < 0 || nextIdx >= board.length ||
                            (direction == 1 && nextIdx / cols != startIdx / cols) || // Limit horizontal row
                            (direction == cols - 1 && nextIdx % cols > startIdx % cols) || // Limit diagonal left
                            (direction == cols + 1 && nextIdx % cols < startIdx % cols)) { // Limit diagonal right
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

                if (win) {

                    // Saves the coordinates of the four winning tiles in a 2D array
                    tokenCoordinates = new int[4][2];
                    // convert the linear indices (winningTokens[i]) into column and row coordinates
                    for (int i = 0; i < 4; i++) {
                        tokenCoordinates[i][0] = winningTokens[i] % cols; // col
                        tokenCoordinates[i][1] = winningTokens[i] / cols; // row
                    }
                    return true; // End the method, win detected
                }

            }

        }

        return false; // No win detected

    }

    // a3

    // Evaluates the board state for the specified player
    // a4
    int evaluateBoard(int player) {

        int score = 0, opponent = -player; // Opponnent is the other player

        // Evaluate the center of the board (column 3 has more weight)
        for (int row = 0; row < rows; row++) {
            if (board[row * cols + 3] == player) {
                score += 3; // Advantage for the center
            }
        }

        // Check for threats and opportunities
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;

                // check horizontally
                if (col + 3 < cols) { // Ensure we don't go out of bounds
                    int playerCount = 0, opponentCount = 0;

                    for (int i = 0; i < 4; i++) {
                        if (board[index + i] == player)
                            playerCount++;
                        if (board[index + i] == opponent)
                            opponentCount++;
                    }

                    // Evaluate based on player stones
                    score += calculateThreatScore.apply(playerCount, opponentCount);

                }

                // Check vertically
                if (row + 3 < rows) {
                    int playerCount = 0, opponentCount = 0;

                    for (int i = 0; i < 4; i++) {
                        if (board[index + i * cols] == player)
                            playerCount++;
                        if (board[index + i * cols] == opponent)
                            opponentCount++;
                    }

                    score += calculateThreatScore.apply(playerCount, opponentCount);
                }

                // Check diagonally (down right)
                if (row + 3 < rows && col + 3 < cols) {
                    int playerCount = 0, opponentCount = 0;

                    for (int i = 0; i < 4; i++) {
                        if (board[index + i * (cols + 1)] == player)
                            playerCount++;
                        if (board[index + i * (cols + 1)] == opponent)
                            opponentCount++;
                    }

                    score += calculateThreatScore.apply(playerCount, opponentCount);

                }

                // Check diagonally (down-left)
                if (row + 3 < rows && col - 3 >= 0) {
                    int playerCount = 0, opponentCount = 0;

                    for (int i = 0; i < 4; i++) {
                        if (board[index + i * (cols - 1)] == player)
                            playerCount++;
                        if (board[index + i * (cols - 1)] == opponent)
                            opponentCount++;
                    }

                    score += calculateThreatScore.apply(playerCount, opponentCount);

                }
            }
        }
        // Return the calculated score for the current board state
        return score;
    }
    // a4

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

    // Minimax algorithm with Alpha-Beta Pruning for decision making
    // a5
    int minimax(int[] gameState, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Base case 1: Check if the current board state results in a win
        if (checkWin(turn)) {
            return isMaximizingPlayer ? -1000 : 1000; // Immediate win or loss
        }
        // Base case 2: Depth limit reached, evaluate the board state
        if (depth == 0) {
            return evaluateBoard(isMaximizingPlayer ? -1 : 1);// Standard evaluation
        }

        if (isMaximizingPlayer) {

            int maxEval = Integer.MIN_VALUE;

            // Loop through all columns to find valid moves
            for (int col = 0; col < cols; col++) {
                if (isValidMove(col)) {
                    int row = getAvailableRow(col);
                    gameState[row * cols + col] = -1; // AI plays
                    // Recursively calculate the minimax score for the opponent's turn
                    int eval = minimax(gameState, depth - 1, alpha, beta, false);
                    gameState[row * cols + col] = 0; // Undo move

                    maxEval = Math.max(maxEval, eval); // Update the maximum score
                    alpha = Math.max(alpha, eval);

                    if (beta <= alpha)
                        break; // Prune the remaining branches
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (int col = 0; col < cols; col++) {
                if (isValidMove(col)) {
                    int row = getAvailableRow(col);
                    gameState[row * cols + col] = 1; // Opponent plays

                    int eval = minimax(gameState, depth - 1, alpha, beta, true);
                    gameState[row * cols + col] = 0; // Undo move

                    minEval = Math.min(minEval, eval); // Update the minimum score

                    beta = Math.min(beta, eval);

                    if (beta <= alpha)
                        break; // Prune the remaining branches
                }
            }
            return minEval;
        }
    }

    // a5

    // Returns the available row for a move in the specified column
    int getAvailableRow(int col) {
        return IntStream.rangeClosed(0, 5)
                .map(row -> 5 - row) // Iterate from bottom to top
                .filter(row -> board[row * 7 + col] == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No available row in column " + col));
    }

    // a6


    //determines the best column for the next AI move.
    int getBestMove() {
        int bestMove = -1;

        // Step 1: Check if the AI can win immediately
        for (int col = 0; col < 7; col++) {
            if (isValidMove(col)) {
                Move move = new Move(getAvailableRow(col), col);
                board[move.row() * cols + move.col()] = -1; // Simulate AI plays

                if (checkWin(turn)) { // Check if this move wins
                    board[move.row() * cols + move.col()] = 0; // Undo move
                    return move.col(); // Return immediately with this move
                }

                board[move.row() * cols + move.col()] = 0; // Undo move
            }
        }

        // Step 2: Check if the opponent can win and block
        for (int col = 0; col < 7; col++) {
            if (isValidMove(col)) {
                Move move = new Move(getAvailableRow(col), col);
                board[move.row() * cols + move.col()] = 1; // Simulate opponent move

                if (checkWin(turn)) { // Check if opponent would win
                    board[move.row() * cols + move.col()] = 0; // Undo move
                    return move.col(); // Block immediately
                }

                board[move.row() * cols + move.col()] = 0; // Undo move
            }
        }

        // Step 3: If no immediate winning or blocking move, use Minimax
        int bestScore = Integer.MIN_VALUE;

        for (int col = 0; col < 7; col++) {
            if (isValidMove(col)) {
                Move move = new Move(getAvailableRow(col), col);
                board[move.row() * cols + move.col()] = -1; // Simulate AI move
                // Evaluate the move using Minimax with depth 5
                int score = minimax(board, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board[move.row() * cols + move.col()] = 0;// Undo move
                // Update the best score and column
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }

        return bestMove; // Return the column of the best move
    }
    // a6

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

    static record Move(int row, int col) {
    }

}
