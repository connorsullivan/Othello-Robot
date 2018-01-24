
// Program:      Othello
// Course:       COSC470
// Description:  Permits two programs, each using this control structure (but each with additional
//                  customized classes and/or methods)to play Othello (i.e, against each other).
//                  Custom parameters include the ability to select the style of play (manual, random, or intelligent),
//                  as well as the ability to specify a non-standard board size.
//                  Regardless of the chosen playstyle, the program will always evaluate all possible moves and...
//                  1) allow the user to manually choose a move, but check to make sure that the move is legal
//                  2) choose a move randomly from this list of legal moves
//                  3) choose a move intelligently from this list of legal moves
// Author:       Connor Sullivan
// Revised:      05/04/16

import java.io.*;
import java.util.ArrayList;

//******************************************************************************
//******************************************************************************

// Class:        OthelloShell
// Description:  Main class for the program. Allows set-up and plays one side.

public class OthelloShell {

    public static char myColor = '?';           //B (black) or W (white) - ? means not yet selected
    public static char opponentColor = '?';     //ditto but opposite

    //INSERT ANY ADDITIONAL GLOBAL VARIABLES HERE
    //===========================================
    //===========================================
    
    //===========================================
    //===========================================
    
    //**************************************************************************
    
    //Method:		main
    //Description:	Calls routines to play Othello
    //Parameters:	none
    //Returns:		nothing
    //Calls:            loadBoard, saveBoard, showBoard, constructor in Board class
    //                  getCharacter, getInteger, getKeyboardInput, constructor in KeyboardInputClass
    
    public static void main(String args[]) {
        
        //INSERT ANY ADDITIONAL CONTROL VARIABLES HERE
        //============================================
        //============================================
        
        //============================================
        //============================================
        
        KeyboardInputClass keyboardInput = new KeyboardInputClass();
        int pollDelay = 250;
        long moveStartTime, moveEndTime, moveGraceTime = 10000;     //times in milliseconds
        Board currentBoard = Board.loadBoard();
        String myMove = "", myColorText = "";
        System.out.println("--- Othello ---");
        System.out.println("Player: Connor Sullivan\n");
        if (currentBoard != null) {                                 //board found, make sure it can be used
            if (currentBoard.status == 1) {                          //is a game in progress?   
                if (keyboardInput.getCharacter(true, 'Y', "YN", 1, "A game appears to be in progress. Abort it? (Y/N (default = Y)") == 'Y') {
                    currentBoard = null;
                } else {
                    System.out.println("Exiting program. Try again later...");
                    System.exit(0);
                }
            }
        }
        if ((currentBoard == null) || (currentBoard.status == 2)) {   //create a board for a new game
            int rows = 8;
            int cols = 8;
            if (keyboardInput.getCharacter(true, 'Y', "YN", 1, "Use standard board? (Y/N: default = Y):") == 'N') {
                rows = keyboardInput.getInteger(true, rows, 4, 26, "Specify the number of rows for the board (default = " + rows + "):");
                cols = keyboardInput.getInteger(true, cols, 4, 26, "Specify the number of columns for the board (default = " + cols + "):");
            }
            int maxTime = 60;
            maxTime = keyboardInput.getInteger(true, maxTime, 10, 600, "Max time (seconds) allowed per move (Default = " + maxTime + "):");
            currentBoard = new Board(rows, cols, maxTime);
            while (currentBoard.saveBoard() == false) {
            }               //try until board is saved (necessary in case of access conflict)
        }

        //INSERT CODE HERE FOR ANY ADDITIONAL SET-UP OPTIONS
        //==================================================
        //==================================================
        
        // Get the preferred style of play from the user (1=manual, 2=random, 3=intelligent) -> default = intelligent (3)
        System.out.println("\nPlease choose a playing style (default = 3):");
        System.out.println("\n1. Manual input (i.e. YOU decide the moves) (NOT recommended)");
        System.out.println("2. Random (I will choose a random (but legal) moves");
        System.out.println("3. Intelligent (I will play to win and show no mercy)");
        int playStyle = keyboardInput.getInteger(true, 3, 1, 3, "\nWhat is your choice?");
        System.out.println("");
        
        //==================================================
        //==================================================
        
        //At this point set-up must be in progress so colors can be assigned
        if (currentBoard.colorSelected == '?') {                    //if no one has chosen a color yet, choose one (player #1)
            myColor = keyboardInput.getCharacter(true, 'B', "BW", 1, "Select color: B=Black; W=White (Default = Black):");
            currentBoard.colorSelected = myColor;

            while (currentBoard.saveBoard() == false) {
            }               //try until the board is saved
            System.out.println("You may now start the opponent's program...");
            while (currentBoard.status == 0) {                      //wait for other player to join in
                currentBoard = null;                                //get the updated board
                while (currentBoard == null) {
                    currentBoard = Board.loadBoard();
                }
            }
        } else {                                                      //otherwise take the other color (this is player #2)
            if (currentBoard.colorSelected == 'B') {
                myColor = 'W';
            } else {
                myColor = 'B';
            }
            currentBoard.status = 1;                                //by now, both players are engaged and play can begin
            while (currentBoard.saveBoard() == false) {
            }               //try until the board is saved
        }

        if (myColor == 'B') {
            myColorText = "Black";
            opponentColor = 'W';
        } else {
            myColorText = "White";
            opponentColor = 'B';
        }
        System.out.println("This player will be " + myColorText + "\n");

        //INSERT CODE HERE FOR ANY ADDITIONAL OUTPUT OPTIONS
        //==================================================
        //==================================================
        
        //==================================================
        //==================================================
        
        //Now play can begin. (At this point each player should have an identical copy of currentBoard.)
        while (currentBoard.status == 1) {
            if (currentBoard.whoseTurn == myColor) {
                if (currentBoard.whoseTurn == 'B') {
                    System.out.println("Black's turn to move...");
                } else {
                    System.out.println("White's turn to move");
                }
                currentBoard.showBoard();
                String previousMove = currentBoard.move;
                moveStartTime = System.currentTimeMillis();

                //CALL METHOD(S) HERE TO SELECT AND MAKE A VALID MOVE
                //===================================================
                //===================================================
                
                // Create a new instance of my custom board class
                OthelloBoard trialBoard = new OthelloBoard(currentBoard);
                // Get the next move (according to the specified playstyle)
                trialBoard.getNextMove(playStyle);
                myMove = trialBoard.myMove;
                // If we actually made a move, myMove will be non-empty...
                // ... so copy the board into the existing currentBoard object
                if (myMove.length() > 0) {
                    for (int r = 0; r < currentBoard.boardRows; r++) {
                        for (int c = 0; c < currentBoard.boardCols; c++) {
                            currentBoard.board[r][c] = trialBoard.board[r][c];
                        }
                    }
                }
                
                //===================================================
                //===================================================
                
                //YOU MAY ADD NEW CLASSES AND/OR METHODS BUT DO NOT
                //CHANGE ANY EXISTING CODE BELOW THIS POINT
                moveEndTime = System.currentTimeMillis();
                if ((moveEndTime - moveStartTime) > (currentBoard.maxMoveTime * 1000 + moveGraceTime)) {
                    System.out.println("\nMaximum allotted move time exceeded--Opponent wins by default...\n");
                    keyboardInput.getKeyboardInput("\nPress ENTER to exit...");
                    currentBoard.status = 2;
                    while (currentBoard.saveBoard() == false) {
                    }       //try until the board is saved
                    System.exit(0);
                }

                if (myMove.length() != 0) {
                    System.out.println(myColorText + " chooses " + myMove + "\n");
                    currentBoard.showBoard();
                    System.out.println("Waiting for opponent's move...\n");
                } else if (previousMove.length() == 0) {               //neither player can move
                    currentBoard.status = 2;                    //game over...
                    System.out.println("\nGame over!");
                    int blackScore = 0;
                    int whiteScore = 0;
                    for (int r = 0; r < currentBoard.boardRows; r++) {
                        for (int c = 0; c < currentBoard.boardCols; c++) {
                            if (currentBoard.board[r][c] == 'B') {
                                blackScore++;
                            } else if (currentBoard.board[r][c] == 'W') {
                                whiteScore++;
                            }
                        }
                    }
                    if (blackScore > whiteScore) {
                        System.out.println("Blacks wins " + blackScore + " to " + whiteScore);
                    } else if (whiteScore > blackScore) {
                        System.out.println("White wins " + whiteScore + " to " + blackScore);
                    } else {
                        System.out.println("Black and White tie with scores of " + blackScore + " each");
                    }
                } else {
                    System.out.println("No move available. Opponent gets to move again...");
                }
                currentBoard.move = myMove;
                currentBoard.whoseTurn = opponentColor;
                while (currentBoard.saveBoard() == false) {
                }           //try until the board is saved
            } else {                                                   //wait a moment then poll again
                try {
                    Thread.sleep(pollDelay);
                } catch (Exception e) {
                }
            }
            currentBoard = null;                                    //get the updated board
            while (currentBoard == null) {
                currentBoard = Board.loadBoard();
            }
        }
        keyboardInput.getKeyboardInput("\nPress ENTER to exit...");
    }
    
    //**************************************************************************
    
}

//******************************************************************************
//******************************************************************************

//Class:        Board
//Description:  Othello board and related parms

class Board implements Serializable {

    char status;        //0=set-up for a new game is in progress; 1=a game is in progress; 2=game is over
    char whoseTurn;     //'?'=no one's turn yet--game has not begun; 'B'=black; 'W'=white
    String move;        //the move selected by the current player (as indicated by whoseTurn)
    char colorSelected; //'B' or 'W' indicating the color chosen by the first player to access the file
    //for a new game ('?' if neither player has yet chosen a color)
    //Note: this may or may not be the color for the player accessing the file
    int maxMoveTime;    //maximum time allotted for a move (in seconds)
    int boardRows;      //size of the board (allows for variations on the standard 8x8 board)
    int boardCols;
    char board[][];     //the board. Positions are filled with: blank = no piece; 'B'=black; 'W'=white
    
    //**************************************************************************
    
    //Method:       Board
    //Description:  Constructor to create a new board object
    //Parameters:	rows - size of the board
    //              cols
    //              time - maximum time (in seconds) allowed per move
    //Calls:		nothing
    //Returns:		nothing

    Board(int rows, int cols, int time) {
        int r, c;
        status = 0;
        whoseTurn = 'B';        //Black always makes the first move
        move = "*";
        colorSelected = '?';
        maxMoveTime = time;
        boardRows = rows;
        boardCols = cols;
        board = new char[boardRows][boardCols];
        for (r = 0; r < boardRows; r++) {
            for (c = 0; c < boardCols; c++) {
                board[r][c] = ' ';
            }
        }
        r = boardRows / 2 - 1;
        c = boardCols / 2 - 1;
        board[r][c] = 'W';
        board[r][c + 1] = 'B';
        board[r + 1][c] = 'B';
        board[r + 1][c + 1] = 'W';
    }

    //**************************************************************************
    
    //Method:       saveBoard
    //Description:  Saves the current board to disk as a binary file named "OthelloBoard"
    //Parameters:	none
    //Calls:		nothing
    //Returns:		true if successful; false otherwise
    
    public boolean saveBoard() {
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("OthelloBoard"));
            outStream.writeObject(this);
            outStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //**************************************************************************
    
    //Method:       loadBoard
    //Description:  Loads the current Othello board and data from a binary file
    //Parameters:   none
    //Calls:        nothing
    //Returns:      a Board object (or null if routine is unsuccessful)
    
    public static Board loadBoard() {
        try {
            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("OthelloBoard"));
            Board boardObject = (Board) inStream.readObject();
            inStream.close();
            return boardObject;
        } catch (Exception e) {
        }
        return null;
    }

    //**************************************************************************
    
    //Method:       showBoard
    //Description:  Displays the current Othello board using extended Unicode characters. Looks fine
    //               in a command window but may not display well in the NetBeans IDE...
    //Parameters:   none
    //Calls:        nothing
    //Returns:      nothing
    
    public void showBoard() {
        int r, c;
        System.out.print("  ");                         //column identifiers
        for (c = 0; c < boardCols; c++) {
            System.out.print(" " + (char) (c + 65));
        }
        System.out.println();

        //top border
        System.out.print("  " + (char) 9484);                   //top left corner \u250C
        for (c = 0; c < boardCols - 1; c++) {
            System.out.print((char) 9472);               //horizontal \u2500
            System.out.print((char) 9516);               //vertical T \u252C
        }
        System.out.print((char) 9472);                   //horizontal \u2500
        System.out.println((char) 9488);                 //top right corner \u2510

        //board rows
        for (r = 0; r < boardRows; r++) {
            System.out.print(" " + (char) (r + 65));         //row identifier
            System.out.print((char) 9474);               //vertical \u2502
            for (c = 0; c < boardCols; c++) {
                System.out.print(board[r][c]);
                System.out.print((char) 9474);           //vertical \u2502
            }
            System.out.println();

            //insert row separators
            if (r < boardRows - 1) {
                System.out.print("  " + (char) 9500);           //left T \u251C
                for (c = 0; c < boardCols - 1; c++) {
                    System.out.print((char) 9472);       //horizontal \u2500
                    System.out.print((char) 9532);       //+ (cross) \u253C
                }
                System.out.print((char) 9472);           //horizontal \u2500
                System.out.println((char) 9508);         //right T \u2524
            }
        }

        //bottom border
        System.out.print("  " + (char) 9492);                   //lower left corner \u2514
        for (c = 0; c < boardCols - 1; c++) {
            System.out.print((char) 9472);               //horizontal \u2500
            System.out.print((char) 9524);               //upside down T \u2534
        }
        System.out.print((char) 9472);                   //horizontal \u2500
        System.out.println((char) 9496);                 //lower right corner \u2518

        return;
    }

    //**************************************************************************
    
}

//******************************************************************************
//******************************************************************************

// Class:        OthelloBoard
// Description:  Custom board object class for the Othello program.
//                  Allows the main board to be copied and modified without affecting the program unintentionally.
//                  Also allows for all possible moves from a current position to be calculated and for the best move
//                  (for a given playstyle) to be selected and returned to the main class.

class OthelloBoard {

    String myMove = "";
    int boardRows;
    int boardCols;
    char[][] board;
    
    // *************************************************************************
    
    // Method:       OthelloBoard
    // Description:  Constructor method to create a copy of the working board class from the main Othello program class.
    // Parameters:   boardObject - the main board object from the Othello program
    // Calls:        nothing
    // Returns:      nothing
    
    @SuppressWarnings("ManualArrayToCollectionCopy")
    OthelloBoard(Board boardObject) {
        
        this.boardRows = boardObject.boardRows;
        this.boardCols = boardObject.boardCols;
        this.board = new char[boardRows][boardCols];
        
        for (int r = 0; r < this.boardRows; r++) {
            for (int c = 0; c < this.boardCols; c++) {
                this.board[r][c] = boardObject.board[r][c];
            }
        }
        
    } // end of constructor
    
    // *************************************************************************
    
    // Method:       getNextMove
    // Description:  Finds the next legal move for a given playstyle. If no moves are possible, nextMove is kept null.
    //               Also, after finding the next move, the method checks to see if there are any chips that can be flipped indirectly
    //               as a result of the move, and flips them if possible.
    // Parameters:   decisionType - the selected playstyle (1=manual (user-input), 2=random (but still legal), 3=intelligent)
    // Calls:        getPossibleMoves
    //               moveManually
    //               moveIntelligently
    //               updateBoard
    // Returns:      nothing
    
    public void getNextMove(int decisionType) {

        ArrayList<Move> legalMoves = getPossibleMoves();
        Move nextMove = null;

        if (!legalMoves.isEmpty()) {

            switch (decisionType) {

                // Move manually
                case 1:
                    nextMove = moveManually(legalMoves);
                    break;
                // Move randomly
                case 2:
                    nextMove = legalMoves.remove((int)(Math.random()*legalMoves.size()));
                    break;
                // Move intelligently
                case 3:
                    nextMove = moveIntelligently(legalMoves);
                    break;

            } // end of switch

        } // end of if statement
        
        if (nextMove != null) {
            
            this.myMove = nextMove.move;
            updateBoard(nextMove);
            
            // Check if any other lines of chips can be flipped
            for (int row = nextMove.emptyRow; row >= 0; row--) {
                if (this.board[row][nextMove.emptyCol] == ' ') {
                    break;
                }
                if (this.board[row][nextMove.emptyCol] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, nextMove.emptyCol));
                }
            }
            for (int row = nextMove.emptyRow; row < this.boardRows; row++) {
                if (this.board[row][nextMove.emptyCol] == ' ') {
                    break;
                }
                if (this.board[row][nextMove.emptyCol] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, nextMove.emptyCol));
                }
            }
            for (int col = nextMove.emptyCol; col < this.boardCols; col++) {
                if (this.board[nextMove.emptyRow][col] == ' ') {
                    break;
                }
                if (this.board[nextMove.emptyRow][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, nextMove.emptyRow, col));
                }
            }
            for (int col = nextMove.emptyCol; col >= 0; col--) {
                if (this.board[nextMove.emptyRow][col] == ' ') {
                    break;
                }
                if (this.board[nextMove.emptyRow][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, nextMove.emptyRow, col));
                }
            }
            int row = nextMove.emptyRow;
            int col = nextMove.emptyCol;
            while (row >= 0 && col >= 0) {
                if (this.board[row][col] == ' ') {
                    break;
                }
                if (this.board[row][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, col));
                }
                row--;
                col--;
            }
            row = nextMove.emptyRow;
            col = nextMove.emptyCol;
            while (row < this.boardRows && col < this.boardCols) {
                if (this.board[row][col] == ' ') {
                    break;
                }
                if (this.board[row][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, col));
                }
                row++;
                col++;
            }
            row = nextMove.emptyRow;
            col = nextMove.emptyCol;
            while (row >= 0 && col < this.boardCols) {
                if (this.board[row][col] == ' ') {
                    break;
                }
                if (this.board[row][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, col));
                }
                row--;
                col++;
            }
            row = nextMove.emptyRow;
            col = nextMove.emptyCol;
            while (row < this.boardRows && col >= 0) {
                if (this.board[row][col] == ' ') {
                    break;
                }
                if (this.board[row][col] == OthelloShell.myColor) {
                    updateBoard(new Move(nextMove.emptyRow, nextMove.emptyCol, row, col));
                }
                row++;
                col--;
            }
            
        } // end of if statement

    } // end of method
    
    // *************************************************************************
    
    // Method:       getPossibleMoves
    // Description:  Calculates all possible moves from a current position on the board and returns them as an ArrayList of custom Move objects
    // Parameters:   none
    // Calls:        Investigation
    // Returns:      legalMoves - an ArrayList of possible moves from the given position on the board
    
    private ArrayList<Move> getPossibleMoves() {

        ArrayList<Move> legalMoves = new ArrayList<>();
        
        // Look through the entire board
        for (int r = 0; r < this.boardRows; r++) {
            for (int c = 0; c < this.boardCols; c++) {
                
                // If the current r,c pair is a corner piece
                if (r == 0 && c == 0 || r == this.boardRows-1 && c == 0 || r == 0 && c == this.boardCols-1 || r == this.boardRows-1 && c == this.boardCols-1) {
                    // Do nothing - (current r,c = corner piece)
                } else {
                    
                    // If the current r,c pair is an opponent piece
                    if (this.board[r][c] == OthelloShell.opponentColor) {
                        
                        // See where the opponent piece ends in all directions (N, S, E, W, NW, SE, NE, SW)
                        for (int direction = 0; direction < 8; direction++) {
                            
                            int oppositeDirection = -1;
                            switch (direction) {
                                case 0: case 2: case 4: case 6:
                                    oppositeDirection = direction+1;
                                    break;
                                case 1: case 3: case 5: case 7:
                                    oppositeDirection = direction-1;
                                    break;
                            } // end of switch
                            
                            Investigation findings = new Investigation(r, c, direction, this, false);
                            int rowHolder, colHolder;
                            if (findings.endChar == OthelloShell.myColor) {
                                
                                // Keep up with these values in case we find a legal move
                                rowHolder = findings.endRow;
                                colHolder = findings.endCol;
                                
                                // Check the other direction for a blank space
                                findings = new Investigation(r, c, oppositeDirection, this, false);
                                
                                // Check if the move is legal
                                if (findings.endChar == ' ') {
                                    legalMoves.add(new Move(rowHolder, colHolder, findings.endRow, findings.endCol));
                                } // end of if statement
                                
                            } // end of if statement
                            if (findings.endChar == ' ') {
                                
                                // Keep up with these values in case we find a legal move
                                rowHolder = findings.endRow;
                                colHolder = findings.endCol;
                                
                                // Check the other direction for a blank space
                                findings = new Investigation(r, c, oppositeDirection, this, true);
                                
                                // Check if the move is legal
                                if (findings.endChar == OthelloShell.myColor) {
                                    legalMoves.add(new Move(findings.endRow, findings.endCol, rowHolder, colHolder));
                                } // end of if statement
                                
                            } // end of if statement
                            
                        } // end of iteration (direction)
                        
                    } // end of if statement
                    
                } // end of if else statement
                
            } // end of iteration (c)
        } // end of iteration (r)
        
        return legalMoves;
        
    } // end of method (getPossibleMoves)
    
    // *************************************************************************
    
    // Method:       moveManually
    // Description:  Allows the user to specify the move that he/she would like to perform, but checks to make sure that the move is legal...
    //               before actually performing the move. User still must make a legal move for the method to accept it.
    //               (legal moves are calculated before allowing the user to decide on a move)
    // Parameters:   legalMoves - a pre-generated ArrayList of possible, legal moves from a given position on the board
    //                              The method will check against this list to see if the user-selected move is legal
    // Calls:        nothing
    // Returns:      a new Move object with the user-specified (and validated) move
    
    private Move moveManually(ArrayList<Move> legalMoves) {
        
        KeyboardInputClass keyboardInput = new KeyboardInputClass();
        boolean selectedValidMove = false;
        int foundColorRow = -1, foundColorCol = -1;
        int rowValue = -1, colValue = -1;
        
        while (!selectedValidMove) {
            
            char rowChoice = ' ', colChoice = ' ';
            while (rowChoice == ' ') {
                rowChoice = keyboardInput.getCharacter(true, ' ', "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1, "\nChoose your row:");
            }
            while (colChoice == ' ') {
                colChoice = keyboardInput.getCharacter(true, ' ', "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1, "Choose your column:");
            }
            
            for (int i = 0; i < Move.ALPHABET_MAP.length; i++) {
                if (rowChoice == Move.ALPHABET_MAP[i]) {
                    rowValue = i;
                }
                if (colChoice == Move.ALPHABET_MAP[i]) {
                    colValue = i;
                }
            }
            
            for (Move move : legalMoves) {
                if (move.emptyRow == rowValue && move.emptyCol == colValue) {
                    selectedValidMove = true;
                    foundColorRow = move.myColorRow;
                    foundColorCol = move.myColorCol;
                    break;
                }
            }
            if (!selectedValidMove) {
                System.out.println("\nThe move you selected (" + rowChoice + ", " + colChoice + ") isn't a valid move. Please choose something else (You're making me look bad!)");
            }
            
        } // end of while loop
        
        return (new Move(foundColorRow, foundColorCol, rowValue, colValue));
        
    } // end of method
    
    // *************************************************************************
    
    // Method:       moveIntelligently
    // Description:  Attempts to make the best possible move by selecting the move with the highest score from the pre-generated list of possible moves
    // Parameters:   legalMoves - a pre-generated ArrayList of possible, legal moves from a given position on the board
    // Calls:        nothing
    // Returns:      bestMove - the best predicted move for a given list of moves
    
    private Move moveIntelligently(ArrayList<Move> legalMoves) {
        
        Move bestMove = legalMoves.get(0);
        for ( Move legalMove : legalMoves ) {
            if (legalMove.score > bestMove.score) {
                bestMove = legalMove;
            }
        }
        return bestMove;
        
    } // end of method
    
    // *************************************************************************
    
    // Method:       updateBoard
    // Description:  Updates the global board class with the selected move on a given turn by
    //               changing the appropriate board pieces from B to W (or vis-versa)
    // Parameters:   newMove - the move to apply to the global board variable
    // Calls:        nothing
    // Returns:      nothing
    
    private void updateBoard(Move newMove) {
        
        if (newMove.myColorRow != newMove.emptyRow || newMove.myColorCol != newMove.emptyCol) {

            switch (newMove.moveType) {

                case "VERTICAL":

                    if (newMove.emptyRow > newMove.myColorRow) {
                        for (int row = newMove.myColorRow; row <= newMove.emptyRow; row++) {
                            this.board[row][newMove.emptyCol] = OthelloShell.myColor;
                        }
                    }
                    if (newMove.emptyRow < newMove.myColorRow) {
                        for (int row = newMove.myColorRow; row >= newMove.emptyRow; row--) {
                            this.board[row][newMove.emptyCol] = OthelloShell.myColor;
                        }
                    }
                    break;

                case "HORIZONTAL":

                    if (newMove.emptyCol > newMove.myColorCol) {
                        for (int col = newMove.myColorCol; col <= newMove.emptyCol; col++) {
                            this.board[newMove.emptyRow][col] = OthelloShell.myColor;
                        }
                    }
                    if (newMove.emptyCol < newMove.myColorCol) {
                        for (int col = newMove.myColorCol; col >= newMove.emptyCol; col--) {
                            this.board[newMove.emptyRow][col] = OthelloShell.myColor;
                        }
                    }
                    break;

                case "DIAGONAL":

                    if (newMove.emptyRow > newMove.myColorRow && newMove.emptyCol > newMove.myColorCol) {
                        for (int i = 0; i <= (newMove.emptyRow - newMove.myColorRow); i++) {
                            this.board[newMove.myColorRow + i][newMove.myColorCol + i] = OthelloShell.myColor;
                        }
                    }
                    if (newMove.emptyRow > newMove.myColorRow && newMove.emptyCol < newMove.myColorCol) {
                        for (int i = 0; i <= (newMove.emptyRow - newMove.myColorRow); i++) {
                            this.board[newMove.myColorRow + i][newMove.myColorCol - i] = OthelloShell.myColor;
                        }
                    }
                    if (newMove.emptyRow < newMove.myColorRow && newMove.emptyCol < newMove.myColorCol) {
                        for (int i = 0; i <= (newMove.myColorRow - newMove.emptyRow); i++) {
                            this.board[newMove.myColorRow - i][newMove.myColorCol - i] = OthelloShell.myColor;
                        }
                    }
                    if (newMove.emptyRow < newMove.myColorRow && newMove.emptyCol > newMove.myColorCol) {
                        for (int i = 0; i <= (newMove.myColorRow - newMove.emptyRow); i++) {
                            this.board[newMove.myColorRow - i][newMove.myColorCol + i] = OthelloShell.myColor;
                        }
                    }
                    break;

            } // end of switch

        } // end of if statement
        
    } // end of method
    
    // *************************************************************************
    
} // end of class

// *****************************************************************************
// *****************************************************************************

// Class:       Move
// Description: A custom object class which holds a particular "move" (for example, placing a board piece on a given blank space)

class Move {

    int myColorRow, myColorCol, emptyRow, emptyCol;
    String moveType;
    String move = "";
    
    // Used for heuristic searches
    int score;
    
    public static final char[] ALPHABET_MAP = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
        'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    // *************************************************************************
    
    // Method:       Move
    // Description:  Constructor method for creating a custom Move object
    // Parameters:   fromRow - the row considered to be moving "from"
    //               fromCol - the col considered to be moving "from"
    //               toRow - the row considered to be moving "to"
    //               toCol - the col considered to be moving "to"
    // Calls:        setMoveDetails
    //               setMoveScore
    // Returns:      nothing
    
    Move ( int fromRow, int fromCol, int toRow, int toCol ) {
        
        myColorRow = fromRow;
        myColorCol = fromCol;
        emptyRow = toRow;
        emptyCol = toCol;
        
        setMoveDetails();
        setMoveScore();
        
    } // end of constructor
    
    // *************************************************************************
    
    // Method:       setMoveDetails
    // Description:  Classifies the move as a VERTICAL, HORIZONTAL, or DIAGONAL move, as well as
    //               generates the correct name for the move (for example "BC")
    // Parameters:   none
    // Calls:        nothing
    // Returns:      nothing
    
    private void setMoveDetails () {
        
        if (emptyRow != myColorRow && emptyCol == myColorCol) {
            moveType = "VERTICAL";
        }
        if (emptyRow == myColorRow && emptyCol != myColorCol) {
            moveType = "HORIZONTAL";
        }
        if (emptyRow != myColorRow && emptyCol != myColorCol) {
            moveType = "DIAGONAL";
        }
        
        move += ALPHABET_MAP[emptyRow%26];
        move += ",";
        move += ALPHABET_MAP[emptyCol%26];
        
    } // end of method
    
    // *************************************************************************
    
    // Method:       setMoveScore
    // Description:  Attempts to score a given move by adding the length of a given move to the score value
    // Parameters:   none
    // Calls:        nothing
    // Returns:      nothing
    
    private void setMoveScore () {
        
        score += Math.abs(emptyRow-myColorRow);
        score += Math.abs(emptyCol-myColorCol);
        
    } // end of method
    
    // *************************************************************************

} // end of class

// *****************************************************************************
// *****************************************************************************

// Class:       Investigation
// Description: Traverses a board in a given direction to see how "far" a given move can go
//              (for example: when a blank space is found to put the myColor piece)

class Investigation {

    char[][] boardSpace;
    int rowCount, colCount;
    
    char beginChar, endChar;
    int startRow, startCol, endRow, endCol;
    String investigationDirection;
    
    // *************************************************************************
    
    // Method:       Investigation
    // Description:  Constructor class for the custom Investigation object
    // Parameters:   fromRow - the starting row of the traversal investigation
    //               fromCol - the starting col of the traversal investigation
    //               direction - the direction to traverse (or "Investigate") the move in
    //               boardToCheck - the board object to perform the investigation on
    //               findMyColorInsteadOfSpace - if TRUE, return when traversal reaches a MyColor space instead of a blank space
    // Calls:        Investigate
    // Returns:      nothing
    
    Investigation (int fromRow, int fromCol, int direction, OthelloBoard boardToCheck, boolean findMyColorInsteadOfSpace) {
        
        this.boardSpace = boardToCheck.board;
        this.rowCount = boardToCheck.boardRows;
        this.colCount = boardToCheck.boardCols;
        
        this.beginChar = boardSpace[fromRow][fromCol];
        this.startRow = fromRow;
        this.startCol = fromCol;
        
        switch (direction) {
            
            case 0:
                investigationDirection = "NORTH";
                break;
            case 1:
                investigationDirection = "SOUTH";
                break;
            case 2:
                investigationDirection = "EAST";
                break;
            case 3:
                investigationDirection = "WEST";
                break;
            case 4:
                investigationDirection = "NORTHWEST";
                break;
            case 5:
                investigationDirection = "SOUTHEAST";
                break;
            case 6:
                investigationDirection = "NORTHEAST";
                break;
            case 7:
                investigationDirection = "SOUTHWEST";
                break;
                
        } // end of switch
        
        Investigate(this.startRow, this.startCol, findMyColorInsteadOfSpace);
        
    } // end of constructor
    
    // *************************************************************************
    
    // Method:       Investigate
    // Description:  Performs the actual traversal of an investigation, looking for either a blank space or MyColor piece
    // Parameters:   row - the starting row of the traversal investigation
    //               col - the starting col of the traversal investigation
    //               findMyColorInsteadOfSpace - if TRUE, return when traversal reaches a MyColor space instead of a blank space
    // Calls:        nothing
    // Returns:      nothing
    
    private void Investigate (int row, int col, boolean findMyColorInsteadOfSpace) {
        
        boolean sawMyColor = false;
        int myColorRow = -1, myColorCol = -1;
        
        while (this.boardSpace[row][col] != ' ') {
            
            if (this.boardSpace[row][col] == OthelloShell.myColor) {
                sawMyColor = true;
                myColorRow = row;
                myColorCol = col;
            }
            
            switch (investigationDirection) {

                case "NORTH":

                    if (row == 0) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row--;
                    }
                    break;

                case "SOUTH":

                    if (row == this.rowCount-1) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row++;
                    }
                    break;

                case "EAST":

                    if (col == this.colCount-1) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        col++;
                    }
                    break;

                case "WEST":

                    if (col == 0) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        col--;
                    }
                    break;

                case "NORTHWEST":

                    if (row == 0 || col == 0) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row--;
                        col--;
                    }
                    break;

                case "SOUTHEAST":

                    if (row == this.rowCount-1 || col == this.colCount-1) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row++;
                        col++;
                    }
                    break;

                case "NORTHEAST":

                    if (row == 0 || col == this.colCount-1) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row--;
                        col++;
                    }
                    break;

                case "SOUTHWEST":

                    if (row == this.rowCount-1 || col == 0) {
                        if (sawMyColor) {
                            this.endChar = OthelloShell.myColor;
                            this.endRow = myColorRow;
                            this.endCol = myColorCol;
                        } else {
                            this.endChar = this.boardSpace[row][col];
                            this.endRow = row;
                            this.endCol = col;
                        }
                        return;
                    } else {
                        row++;
                        col--;
                    }
                    break;

            } // end of switch
            
        } // end of while loop
        
        if (findMyColorInsteadOfSpace) {
            if (sawMyColor) {
                endChar = OthelloShell.myColor;
                endRow = myColorRow;
                endCol = myColorCol;
                return;
            }
        }
        
        this.endChar = this.boardSpace[row][col];
        this.endRow = row;
        this.endCol = col;
        
    } // end of method
    
    // *************************************************************************

} // end of class

//******************************************************************************
//******************************************************************************
