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

    // Sudoku Model Section

    /**
     * Initialize the sudoku grid array
     */
    public SudokuModel() {
        this.currentGrid = new int[9][9];
        this.initialGrid = new int[9][9];
        this.gameState = MainGUI.GameState.EDITING;
    }

    public void setVal(int row, int col, int val) {
        // can only set while editing
        if(gameState == MainGUI.GameState.EDITING) {
            currentGrid[row][col] = val;
            alertObservers("Set (" + (row + 1) + ", " + (col + 1) + ") to " + val);
        } else {
            alertObservers("Reset the board to solve another puzzle.");
        }
    }

    public int getVal(int row, int col) {
        return currentGrid[row][col];
    }

    public void reset() {
        this.initialGrid = new int[9][9];
        this.currentGrid = new int[9][9];
        this.gameState = MainGUI.GameState.EDITING;
        alertObservers("Board Reset!");
    }

    public void solve() {
        this.gameState = MainGUI.GameState.SOLVING;
        // use DFS to solve
        // copy current onto initial
        for(int i = 0; i < 9; i++) {
            System.arraycopy(this.currentGrid[i], 0, this.initialGrid[i], 0, 9);
        }
        solveDFS(0, 0);
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

    private void solveDFS(int row, int col) {
        if(row > 8) {
            this.gameState = MainGUI.GameState.SOLVED;
            return;
        }
        if(this.currentGrid[row][col] != 0) {
            if(col == 8) {
                solveDFS(row + 1, 0);
            } else {
                solveDFS(row, col + 1);
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
                    solveDFS(row + 1, 0);
                } else {
                    solveDFS(row, col + 1);
                }
            }
        }
        if(gameState != MainGUI.GameState.SOLVED) {
            currentGrid[row][col] = 0;
        }
    }

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

    private boolean checkRow(int row, int col) {
        // true if row has one of each num
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

    private boolean checkCol(int row, int col) {
        // true if col has one of each num
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
