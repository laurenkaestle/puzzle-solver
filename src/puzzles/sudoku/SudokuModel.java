package puzzles.sudoku;

import puzzles.MainGUI;
import puzzles.Observer;

import java.util.LinkedList;
import java.util.List;

public class SudokuModel {
    private final List<Observer<SudokuModel, String>> observers = new LinkedList<>();
    private int[][] currentGrid;
    private int[][] initialGrid;
    private MainGUI.GameState gameState;

    public void addObserver(Observer<SudokuModel, String> observer) {
        this.observers.add(observer);
    }

    private void alertObservers(String data) {
        for(var observer: observers) {
            observer.update(this, data);
        }
    }

    /**
     * Initialize the sudoku model
     */
    public SudokuModel() {
        this.currentGrid = new int[9][9];
        this.initialGrid = new int[9][9];
        this.gameState = MainGUI.GameState.EDITING;
    }

    /**
     * used to change a square in the grid given the row, column, and new value
     * @param row       the row of the square to change
     * @param col       the column of the square to change
     * @param val       the value to fill in the square with
     */
    public void setVal(int row, int col, int val) {
        // can only set while editing
        if(gameState == MainGUI.GameState.EDITING) {
            currentGrid[row][col] = val;
            alertObservers("Set (" + (row + 1) + ", " + (col + 1) + ") to " + val);
        } else {
            alertObservers("Reset the board to solve another puzzle.");
        }
    }

    /**
     * used to retrieve a value from the given row and column of the grid
     * @param row       the row of the square
     * @param col       the column of the square
     * @return          the value in the square in the given row and column
     */
    public int getVal(int row, int col) {
        return currentGrid[row][col];
    }

    /**
     * used to reset the grids to empty and the game state to editing so
     * another game can be solved
     */
    public void reset() {
        this.initialGrid = new int[9][9];
        this.currentGrid = new int[9][9];
        this.gameState = MainGUI.GameState.EDITING;
        alertObservers("Board Reset!");
    }

    /**
     * calls the backtracking solver function
     * if the puzzle is solved, it outputs the solution found and a success message
     * if the puzzle isn't solved, it outputs the original grid and a message that
     * the given puzzle can't be solved
     */
    public void solve() {
        this.gameState = MainGUI.GameState.SOLVING;
        for(int i = 0; i < 9; i++) {
            System.arraycopy(this.currentGrid[i], 0, this.initialGrid[i], 0, 9);
        }
        backtrack(0, 0);
        if(gameState == MainGUI.GameState.SOLVED) {
            alertObservers("The puzzle was solved successfully!");
        } else {
            gameState = MainGUI.GameState.IMPOSSIBLE;
            for(int i = 0; i < 9; i++) {
                System.arraycopy(this.initialGrid[i], 0, this.currentGrid[i], 0, 9);
            }
            alertObservers("The given puzzle cannot be solved.");
        }
    }

    /**
     * uses backtracking to find a solution to the puzzle
     * @param row   the row of the square to check next
     * @param col   the column of the square to check next
     */
    private void backtrack(int row, int col) {
        if(row > 8) {
            this.gameState = MainGUI.GameState.SOLVED;
            return;
        }
        if(this.currentGrid[row][col] != 0) {
            if(col == 8) {
                backtrack(row + 1, 0);
            } else {
                backtrack(row, col + 1);
            }
            return;
        }
        for(int i = 1; i < 10; i++) {
            if(gameState == MainGUI.GameState.SOLVED) {
                return;
            }
            currentGrid[row][col] = i;
            if(checkRow(row, col) && checkCol(row, col) && checkSquare(row, col)) {
                if (col == 8) {
                    backtrack(row + 1, 0);
                } else {
                    backtrack(row, col + 1);
                }
            }
        }
        if(gameState != MainGUI.GameState.SOLVED) {
            currentGrid[row][col] = 0;
        }
    }

    /**
     * checks the values in the 3 x 3 square containing the given square in the grid
     * for repeats
     * @param row   the row of the current square
     * @param col   the column of the current square
     * @return      true if the square is legal (has at most one of each number),
     *              false otherwise
     */
    private boolean checkSquare(int row, int col) {
        // true if square has one of each num
        int[] nums = new int[9];
        int r, c;
        int rOffset = row / 3;
        int cOffset = col / 3;
        int rLevel = row % 3;
        int cLevel = col % 3;
        int endSquare = (rLevel * 3) + cLevel;
        for(int i = 0; i < endSquare; i++) {
            r = (rOffset * 3) + (i / 3);
            c = (cOffset * 3) + (i % 3);
            nums[currentGrid[r][c] - 1]++;
            if(nums[currentGrid[r][c] - 1] > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks the values in the current square's row for repeats
     * @param row   the row of the current square
     * @param col   the column of the current square
     * @return      true if the row is legal (has at most one of each number),
     *              false otherwise
     */
    private boolean checkRow(int row, int col) {
        int[] nums = new int[9];
        while(col >= 0) {
            nums[this.currentGrid[row][col] - 1]++;
            if(nums[this.currentGrid[row][col] - 1] > 1) {
                return false;
            }
            col--;
        }
        return true;
    }

    /**
     * checks the values in the current square's column for repeats
     * @param row   the row of the current square
     * @param col   the column of the current square
     * @return      true if the column is legal (has at most one of each number),
     *              false otherwise
     */
    private boolean checkCol(int row, int col) {
        int[] nums = new int[9];
        while(row >= 0) {
            nums[this.currentGrid[row][col] - 1]++;
            if(nums[this.currentGrid[row][col] - 1] > 1) {
                return false;
            }
            row--;
        }
        return true;
    }

}
