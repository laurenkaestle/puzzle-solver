package puzzles.kakuro;

import puzzles.MainGUI;
import puzzles.Observer;
import puzzles.sudoku.SudokuModel;

import java.util.LinkedList;
import java.util.List;

public class KakuroModel {
    private final List<Observer<KakuroModel, String>> observers = new LinkedList<>();
    private int[][] currentGrid;
    private int[][] initialGrid;
    private MainGUI.GameState gameState;

    public void addObserver(Observer<KakuroModel, String> observer) {
        this.observers.add(observer);
    }

    private void alertObservers(String data) {
        for(var observer: observers) {
            observer.update(this, data);
        }
    }

    /**
     * Initialize the kakuro model
     */
    public KakuroModel(int rows, int cols) {
        this.currentGrid = new int[rows][cols];
        this.initialGrid = new int[rows][cols];
        this.gameState = MainGUI.GameState.EDITING;
    }

}
